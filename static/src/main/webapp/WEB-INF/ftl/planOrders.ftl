<#import "template/template.ftl" as frame> <#global menu="分期订单">
<@frame.html title="分期订单"
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
               "aaSorting": [[4, "asc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
               { "orderable": false, "targets": [2,3,8,9,10] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-planOrders",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[orderId]" : $('#longOrderId').val(),
                        "filter[salesMan]" : $('#salesMan').val()
                        <@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                        ,"filter[userId]" : $('#autocomplete').val()
                         </@security.authorize>
                        ,"filter[payState]" : $('#payState').val()
                           <@security.authorize ifAnyGranted="sales,salesManager">
				  			 ,"filter[customerName]" : $('#customerName').val()
							</@security.authorize>
							
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "plan.order.creator", "defaultContent": ""},
            	{ "data": "longOrderId", "defaultContent": ""},
            		{ "data": "longOrderId", "defaultContent": "","render": function(data, type, row, meta) {
            			if(row.plan.order.customerJson!=''){
            			var customer = $.parseJSON(row.plan.order.customerJson); 
                        return  ( customer==null || customer=='undefined' ||
                        typeof(customer) == "undefined"|| typeof(customer.company) == "undefined" )?"":customer.company;
                        }else {
                        return '';
                        }
                    }},
            	{ "data": "plan.order.product.name", "defaultContent": "",
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
                
                { "data": "plan.day", "defaultContent": "","render": function(data, type, row, meta) {
	                	var d= $.format.date(data, "yyyy-MM-dd");
	                	return d;
	               	 }
                   },
                   { "data": "plan.periodNum", "defaultContent": ""
                   },
                    { "data": "plan.price", "defaultContent": ""
                   },
                    { "data": "plan.payState", "defaultContent": "","render": function(data, type, row, meta) {
	                	 if(data=='payed'){
	                		 return "已支付";
	                	 }else  if(data=='init'){
	                	  return "待支付";
	                	 }else if(data=='fail'){
	                	  return "未收到款项";
	                	 }else if(data=='check'){
	                	  return "待收款确认";
	                	 }
	               	 }
                   },
                   { "data": "plan.setPlanUser", "defaultContent": ""},
                   
                   { "data": "plan.reduceUser", "defaultContent": ""
                   },
                   { "data": "plan.order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a class='operation' target='_blank' href='${rc.contextPath}/order/toPlanDetail/" +(row.plan.id)+  "'>详情</a>";
                	return tr;
                }}
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
                        '    <span>订单号：</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                                    '    <span>广告主：</span>' +
                        '    <span>' +
                        '        <input id="autocomplete" value="" >' +
                        '    </span>' +
                           <@security.authorize ifAnyGranted="salesManager">
                        '    &nbsp;&nbsp;<span>业务员</span>' +
                        '    <span>' +
                        '        <input id="salesMan" value="">' +
                        '    </span>' +
                         </@security.authorize>
                             '<select class="ui-input ui-input-mini" name="payState" id="payState">' +
                    '<option value="defaultAll">所有事项</option>' +
                  	'<option value="check" selected="selected" >待收款确认</option>' +
                  	'<option value="fail">未收到款项</option>' +
                    '<option value="init">待支付</option>' +
                    '<option value="payed">已支付</option>' +
         			'</select>' +
                        '</div>'
        );
        
       
        $('#longOrderId, #autocomplete, #payState,#customerName,#salesMan ').change(function() {
            table.fnDraw();
        });
        $("#salesMan").autocomplete({
			    minLength: 0,
				source : "${rc.contextPath}/user/salesManAutoComplete",
				change : function(event, ui) {
				},
				select : function(event, ui) {
					$('#salesMan').val(ui.item.value);
					table.fnDraw();
				}
			}).focus(function () {
	       	  $(this).autocomplete("search");
	   	 	});
        //author:pxh 2015-05-20 22:36
        $( "#autocomplete" ).autocomplete({
        	 minLength: 0,
  			source: "${rc.contextPath}/user/autoComplete",
  			change: function( event, ui ) { 
  				/*if(ui.item!=null){alert(ui.item.value);}*/
  				table.fnDraw();
  			 },
  			 select: function(event,ui) {
  			 $('#autocomplete').val(ui.item.value);
  				table.fnDraw();
  			 }
		}).focus(function () {
		 $(this).autocomplete("search");
		});
		
		//--
		<@security.authorize ifAnyGranted="sales,salesManager">
		       initCustomerAutocomplete('${rc.contextPath}',table);
	     </@security.authorize>
		//--
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
				<th orderBy="order.creator">用户</th>
				<th orderBy="order.id">订单号</th>
				<th>代理客户</th>
				<th>套餐名称</th>
				<th orderBy="day">支付时间 </th>
				<th orderBy="periodNum">期数</th>
				<th orderBy="price">金额</th>
			   <th orderBy="payState">状态</th>
			   <th>分期设置人</th>
				<th>处理人</th>
				<th>操作</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>