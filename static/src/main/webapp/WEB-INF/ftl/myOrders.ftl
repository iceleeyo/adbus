<#import "template/template.ftl" as frame> <#global menu="${orderMenu}">
<@frame.html title="我参与的订单"
css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]
js=["js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js","js/jquery.datepicker.region.cn.js","js/jquery-dateFormat.js"]>
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
              "scrollX": true,
                  <@security.authorize ifNotGranted="sales">
            "aaSorting": [[3, "desc"]],
             </@security.authorize>
             <@security.authorize ifAnyGranted="sales">
               "aaSorting": [[4, "desc"]],
             </@security.authorize>
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                    <@security.authorize ifNotGranted="sales">
                  { "orderable": false, "targets": [0,1,2,4,5] },
                 </@security.authorize>
             <@security.authorize ifAnyGranted="sales">
               { "orderable": false, "targets": [0,1,2,3,7,5,6] },
              </@security.authorize>
              
              
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-myOrders",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[longOrderId]" : $('#longOrderId').val()
                         <@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                        ,"filter[userId]" : $('#autocomplete').val()
                         </@security.authorize>
                        ,"filter[taskKey]" : $('#taskKey').val()
                         <@security.authorize ifAnyGranted="sales">
				  			 ,"filter[customerName]" : $('#customerName').val()
							</@security.authorize>
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "order.creator", "defaultContent": ""},
            	{ "data": "longOrderId", "defaultContent": ""},
            	{ "data": "order.product.name", "defaultContent": ""},
            	 <@security.authorize ifAnyGranted="sales">
            		{ "data": "longOrderId", "defaultContent": "","render": function(data, type, row, meta) {
            			var customer = $.parseJSON(row.order.customerJson); 
                        return  (customer == null || customer=='undefined'
                        || typeof(customer) == "undefined"||typeof(customer.company) == "undefined")?"":customer.company;
                    }},
                 </@security.authorize>
                { "data": "order.startTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	if (typeof(d) == "undefined"){
                		return '';
                	}
                	return d+'至'+$.format.date(row.order.endTime, "yyyy-MM-dd");
                }},
                { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                 { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
		                 	var tew=  "<a target='_blank' href='${rc.contextPath}/workflow/view/"+row.executionId+"/page/"+row.processInstanceId+"'>"+data+"</a>";
	                 	 	return tew;
	                   
                  	 
                    }},
                   { "data": "task_assignee", "defaultContent": ""
                   
                   },
                   { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' class='operation' href='${rc.contextPath}/order/orderDetail/0?taskid=" +(row.task_id)+  "'>查看详情</a>&nbsp;&nbsp;";
                	 if(row.approve1Result!=true){
                	  tr+= "<a  class='operation' onclick='editOrderStartD(\"${rc.contextPath}\","+row.order.id+");'>修改订单</a>";
                	 }
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
    
    	<@security.authorize ifAnyGranted="sales,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
	    	function initComplete() {
	        $("div#toolbar").html(
	                '<div>' +
	                        '    <span>订单编号：</span>' +
	                        '    <span>' +
	                        '        <input id="longOrderId" value="">' +
	                        '    </span>' +
	                    <@security.authorize ifAnyGranted="salesManager,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
	                             '    <span>广告主：</span>' +
                        '    <span>' +
                        '        <input id="autocomplete" value="">' +
                        '    </span>' +
                          </@security.authorize>
                          <@security.authorize ifAnyGranted="sales">
                           '    <span>客户：</span>' +
                        '    <span>' +
                        '        <input id="customerName" style="width:200px" value="">' +
                        '    </span>' +
                          </@security.authorize>
	                         '<select class="ui-input ui-input-mini" name="taskKey" id="taskKey">' +
	                    '<option value="defaultAll" selected="selected">所有事项</option>' +
	                  	'<option value="payment">待支付</option>' +
	                  	'<option value="auth">已支付待审核</option>' +
	                    '<option value="report">已排期待上播</option>' +
	                    '<option value="over">已上播</option>' +
	         			'</select>' +
	                    '</div>'
	        );
	
	        $('#longOrderId,#autocomplete, #taskKey').change(function() {
	            table.fnDraw();
	        });
	        //--
		<@security.authorize ifAnyGranted="sales">
		       initCustomerAutocomplete('${rc.contextPath}',table);
	     </@security.authorize>
		//--
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
	    }
   </@security.authorize>
 <@security.authorize ifAnyGranted="advertiser">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单编号</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                         '<select class="ui-input ui-input-mini" name="taskKey" id="taskKey">' +
                    '<option value="defaultAll" selected="selected">所有事项</option>' +
                  	'<option value="payment">待支付</option>' +
                  	'<option value="auth">已支付待审核</option>' +
                    '<option value="report">已排期待上播</option>' +
                    '<option value="over">已上播</option>' +
         			'</select>' +
                    '</div>'
        );

        $('#longOrderId, #taskKey').change(function() {
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
	<!-- <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									
									</div> -->

	<div class="tabs">
		<@security.authorize ifAnyGranted="advertiser"> <#if
		orderMenu=="我的订单"> <a id="tab1"
			href="${rc.contextPath}/order/myOrders/1" class="active">${orderMenu}<span
			id="recordsTotal"
			style="background-color: #ff9966; font-size: 14px; border-radius: 4px;"></span></a>
		<#else> <a id="tab1" href="${rc.contextPath}/order/myOrders/1">${orderMenu}</span></a>

		</#if> </@security.authorize> <@security.authorize
		ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
		<a id="tab2" href="${rc.contextPath}/order/allRuningOrders/1">进行中的订单</a>
		</@security.authorize> <@security.authorize
		ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
		<#if orderMenu=="我参与订单"> <a id="tab4" class="active"
			href="${rc.contextPath}/order/join/1">我参与的订单<span
			id="recordsTotal"
			style="background-color: #ff9966; font-size: 14px; border-radius: 4px;"></span></a>
		<#else> <a id="tab4" href="${rc.contextPath}/order/join/1">我参与的订单<span
			id="recordsTotal"
			style="background-color: #ff9966; font-size: 14px; border-radius: 4px;"></span></a>
		</#if> </@security.authorize> <a id="tab3"
			href="${rc.contextPath}/order/finished">已完成的订单</a>
	</div>

	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>下单用户</th>
				<th>订单编号</th>
				<th>套餐名称</th>
				 <@security.authorize ifAnyGranted="sales">
				<th>客户</th>
				</@security.authorize>
				<th>广告刊期</th>
				<th orderBy="created">创建时间</th>
				<th>待办事项</th>
				<th>当前处理人</th>
				<th>订单详情</th>
			</tr>
		</thead>

	</table>

</div>
</@frame.html>
<!-- 针对tab的js -->
