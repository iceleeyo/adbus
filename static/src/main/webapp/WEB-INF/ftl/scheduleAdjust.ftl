<#import "template/template.ftl" as frame> <#global menu="排期调整">
<@frame.html title="排期调整"
css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]
js=["js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<style type="text/css">
.operation
{
	color: #31B533;
    font-weight: 800;
}
</style>

<script type="text/javascript">

	
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
               <@security.authorize ifNotGranted="sales,salesManager">
            "aaSorting": [[3, "desc"]],
             </@security.authorize>
  
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                   <@security.authorize ifNotGranted="sales,salesManager">
                { "orderable": false, "targets": [0,1,2,5] },
                 </@security.authorize>
             <@security.authorize ifAnyGranted="sales,salesManager">
               { "orderable": false, "targets": [0,1,2,3,4,5] },
              </@security.authorize>
                
                
               
            ],
            "aLengthMenu": [[10,25, 40, 100], [10,25, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-scheduleAdjust",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[longOrderId]"  : $('#longOrderId').val(),
                        "filter[userId]" : $('#advertizer').val(),
                        "filter[salesMan]" : $('#salesMan').val(),
                        "filter[proName]" : $('#proName').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "order.creator", "defaultContent": ""},
            	{ "data": "longOrderId", "defaultContent": ""},
            		 <@security.authorize ifAnyGranted="sales,salesManager">
            		{ "data": "order.customerJson", "defaultContent": "","render": function(data, type, row, meta) {
            			var customer = $.parseJSON(data); 
						return  ( customer==null || customer=='undefined' 
						||typeof(customer) == "undefined"|| typeof(customer.company) == "undefined" )?"":customer.company;
                    }},
                 </@security.authorize>
            	{ "data": "order.product.name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#order.productId').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                    return data;
                } },
                 { "data": "order.contractCode", "defaultContent": "", "render": function(data, type, row, meta) {
              if(typeof(data)=="undefined"){
                return "";
              }
              var customer = $.parseJSON(row.order.customerJson);
              var customerId=(customer == null || customer=='undefined'
                        || typeof(customer) == "undefined"||typeof(customer.username) == "undefined")?"":customer.username;
                        var b=false;
                         <@security.authorize ifAnyGranted="sales,ShibaFinancialManager">
                           b=true;
                          </@security.authorize>
                          if(b){
                            return "<a class='operation' onclick='eleContract(\"${rc.contextPath}\","+row.order.id+",\""+customerId+"\",0)' >"+data+"</a>";
                          }
                          return data;
              }},
              { "data": "order.startTime", "defaultContent": "","render": function(data, type, row, meta) {
                  var d= $.format.date(data, "yyyy-MM-dd");
                  var d2=getEnd(row.order.endTime,-1);
                    if (typeof(d) == "undefined"){
                      return '';
                    }
                      return d+'至'+d2;
                }},
                { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= '<a target="_blank" class="operation" href="${rc.contextPath}/order/orderDetail/'+row.id+'?scheduleTag=1">排期调整</a>';
                	return tr;
                }},
                
                
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback,
        } );
		table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    
    
   
 
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单号</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                         '    <span>广告主：</span>' +
                        '    <span>' +
                        '        <input id="advertizer" value="">' +
                        '    </span>' +
                
                          <@security.authorize ifAnyGranted="salesManager">
                        '    &nbsp;&nbsp;<span>业务员</span>' +
                        '    <span>' +
                        '        <input id="salesMan" value="">' +
                        '    </span>' +
                         </@security.authorize>
                          '    <span>套餐名称：</span>' +
                        '    <span>' +
                        '        <input id="proName" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#longOrderId,#advertizer,#salesMan,#proName').change(function() {
            table.fnDraw();
        });
        
         <@security.authorize ifAnyGranted="salesManager">
      			 initSalesAutocomplete(table);
	   	  </@security.authorize>
	   	  
	   	  
		
		$( "#advertizer" ).autocomplete({
           minLength: 0,
        source: "${rc.contextPath}/user/autoComplete",
        change: function( event, ui ) { 
          table.fnDraw();
         },
         select: function(event,ui) {
         $('#advertizer').val(ui.item.value);
          table.fnDraw();
         }
    }).focus(function () {
     $(this).autocomplete("search");
    });
    
    
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
      //显示总条数 add by impanxh
    function fnDrawCallback(){
		var record_count = (this.fnSettings().fnRecordsTotal() );
		if(record_count>0){
	 	  //$("#recordsTotal").html("&nbsp;"+record_count+"&nbsp;");
		  }
    }

    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>下单用户</th>
				<th orderBy="longOrderId">订单编号</th>
					 <@security.authorize ifAnyGranted="sales,salesManager">
				<th>代理客户</th>
				</@security.authorize>
				<th>套餐名称</th>
				<th>合同编号</th>
				<th>广告刊期</th>
				<th orderBy="startTime">创建时间</th>
				<th>订单详情</th>

			</tr>
		</thead>

	</table>
</div>
</@frame.html>
