<#import "template/template.ftl" as frame> <#global menu="已完成订单">
<@frame.html title="已完成的订单"
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

	function claim(orderid,taskid){
 	$.ajax({
			url : "${rc.contextPath}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				jDialog.Alert(data.right);
				var uptime = window.setTimeout(function(){
				location.reload([true]);
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	  
	}
	
	
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
             <@security.authorize ifAnyGranted="sales,salesManager">
               "aaSorting": [[4, "desc"]],
             </@security.authorize>
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                   <@security.authorize ifNotGranted="sales,salesManager">
                { "orderable": false, "targets": [0,1,2,5,6] },
                 </@security.authorize>
             <@security.authorize ifAnyGranted="sales,salesManager">
               { "orderable": false, "targets": [0,1,2,3,7,5,6] },
              </@security.authorize>
                
                
               
            ],
            "aLengthMenu": [[10,25, 40, 100], [10,25, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-finishedOrders",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[longOrderId]"  : $('#longOrderId').val()
                        ,"filter[stateKey]" : $('#stateKey').val()
                          <@security.authorize ifAnyGranted="salesManager,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                        ,"filter[userId]" : $('#autocomplete').val()
                         </@security.authorize>
                           <@security.authorize ifAnyGranted="sales,salesManager">
				  			 ,"filter[customerName]" : $('#customerName').val()
							</@security.authorize>
							  ,"filter[salesMan]" : $('#salesMan').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "order.creator", "defaultContent": ""},
            	{ "data": "longOrderId", "defaultContent": ""},
            		 <@security.authorize ifAnyGranted="sales,salesManager">
            		{ "data": "longOrderId", "defaultContent": "","render": function(data, type, row, meta) {
            			var customer = $.parseJSON(row.order.customerJson); 
						return  ( customer==null || customer=='undefined' 
						||typeof(customer) == "undefined"|| typeof(customer.company) == "undefined" )?"":customer.company;
                    }},
                 </@security.authorize>
            	{ "data": "product.name", "defaultContent": "",
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
                { "data": "startTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                { "data": "endTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                { "data": "finishedState", "defaultContent": "" },
                { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' class='operation' href='${rc.contextPath}/order/orderDetail/" +(row.id)+ "?pid="+(row.processInstanceId) +  "'>查看详情</a>";
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
    
    
    <@security.authorize ifAnyGranted="salesManager,sales,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单号：</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' + 
                         '    <span>广告主：</span>' +
                        '    <span>' +
                        '        <input id="autocomplete" value="">' +
                        '    </span>' +
                         <@security.authorize ifAnyGranted="sales,salesManager">
                           '    <span>客户：</span>' +
                        '    <span>' +
                        '        <input id="customerName" style="width:200px" value="">' +
                        '    </span>' +
                          </@security.authorize>
                          <@security.authorize ifAnyGranted="salesManager">
                        '    &nbsp;&nbsp;<span>业务员</span>' +
                        '    <span>' +
                        '        <input id="salesMan" value="">' +
                        '    </span>' +
                         </@security.authorize>
                         
                        '<select class="ui-input ui-input-mini" name="stateKey" id="stateKey">' +
	                    '<option value="defaultAll" selected="selected">所有状态</option>' +
    	              	'<option value="finished">已完成</option>' +
        	          	'<option value="closed">已关闭</option>' +
         				'</select>' +
                        '</div>'
        );

        $('#longOrderId,#autocomplete,#stateKey,#salesMan').change(function() {
            table.fnDraw();
        });
        
         <@security.authorize ifAnyGranted="salesManager">
      			 initSalesAutocomplete(table);
	   	  </@security.authorize>
	   	  
	   	  
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
		<@security.authorize ifAnyGranted="sales,salesManager">
		       initCustomerAutocomplete('${rc.contextPath}',table);
	     </@security.authorize>
    }
 </@security.authorize>
 
<@security.authorize ifAnyGranted="advertiser">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单号</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                        '<select class="ui-input ui-input-mini" name="stateKey" id="stateKey">' +
	                    '<option value="defaultAll" selected="selected">所有状态</option>' +
    	              	'<option value="finished">已完成</option>' +
        	          	'<option value="closed">已关闭</option>' +
         				'</select>' +
                        '</div>'
        );

        $('#longOrderId,#stateKey').change(function() {
            table.fnDraw();
        });
    }
 </@security.authorize>

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
	<#--
	<!-- <div class="div" style="margin-top:25px">
                <caption><h2>待办事项</h2></caption>
            </div> -->
	<div class="tabs">
		<@security.authorize ifAnyGranted="advertiser"> <a id="tab1"
			href="${rc.contextPath}/order/myOrders/1">我的订单</a>
		</@security.authorize> <@security.authorize
		ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
		<a id="tab2" href="${rc.contextPath}/order/allRuningOrders/1">进行中的订单</a>
		</@security.authorize> <@security.authorize
		ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
		<a id="tab4" href="${rc.contextPath}/order/join/1">我参与的订单</a>
		</@security.authorize> <a id="tab3"
			href="${rc.contextPath}/order/finished" class="active">
			已完成的订单<span id="recordsTotal" style="background-color: #ff9966; font-size: 14px; border-radius: 4px;"></span></a>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>下单用户</th>
				<th orderBy="longOrderId">订单编号</th>
					 <@security.authorize ifAnyGranted="sales,salesManager">
				<th>代理客户</th>
				</@security.authorize>
				<th>套餐名称</th>
				<!-- <th>素材号</th>-->
				<th orderBy="startTime">创建时间</th>
				<th orderBy="endTime">结束时间</th>
				
				<th>最终状态</th>
				<th>订单详情</th>

			</tr>
		</thead>

	</table>
</div>
</@frame.html>
