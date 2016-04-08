<#import "template/template.ftl" as frame> <#global menu="待支付分期订单">
<@frame.html title="待支付分期订单"
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
            "ordering": false,
            "serverSide": true,
              "scrollX": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                  { "orderable": false, "targets": [0,1,2,4,5] },
              
              
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-payPlanOrders",
                data: function(d) {
                    return $.extend( {}, d, {
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
            	{ "data": "order.product.name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    return data;
                } },
                { "data": "order.price", "defaultContent": ""},
                { "data": "order.payPrice", "defaultContent": ""},
                { "data": "order.id", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' class='operation' href='${rc.contextPath}/order/toRestPay/" +data+ "'>查看详情</a>";
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
               ''
        );
	   	  
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
		  }
    }

    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
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
			href="${rc.contextPath}/order/finished" >
			已完成的订单<span id="recordsTotal" style="background-color: #ff9966; font-size: 14px; border-radius: 4px;"></span></a>
			<a id="tab4"
			href="${rc.contextPath}/order/payPlanOrders" class="active">
			待支付分期订单<span id="recordsTotal" style="background-color: #ff9966; font-size: 14px; border-radius: 4px;"></span></a>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>下单用户</th>
				<th orderBy="longOrderId">订单编号</th>
			   <@security.authorize ifAnyGranted="sales,salesManager">
				<th>代理客户</th>
				</@security.authorize>
				<th>产品名称</th>
				<th>订单总价</th>
				<th>已支付金额</th>
				<th>操作</th>

			</tr>
		</thead>

	</table>
</div>
</@frame.html>