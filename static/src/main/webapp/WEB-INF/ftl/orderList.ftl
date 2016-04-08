<#import "template/template.ftl" as frame> <#global menu="待办事项">
<@frame.html title="待办事项列表"
css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css","css/layer.css"]
js=["js/layer.min.js","js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer-site.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<style type="text/css">
.operation
{
	color: #31B533;
    font-weight: 800;
}
</style>
<script type="text/javascript">

	function closeOrder22(mainPath,orderid,taskid){
		$.ajax({
			url : mainPath+"/order/closeOrder/"+taskid+"?orderid="+orderid,
			type : "POST",
			success : function(data) {
				//jDialog.Alert(data.right);
				 jDialog.Alert(data.right,function(){
		        var uptime = window.setTimeout(function(){
				$(location).attr('href', mainPath+"/order/myTask/1");
		         clearTimeout(uptime);
		       },2000);});
				//location.reload([true]);
			}
		}, "text");
	}

	function claim(orderid,taskid){
 	$.ajax({
			url : "${rc.contextPath}/order/claim/"+taskid,
			type : "POST",
			success : function(data) {
				//jDialog.Alert(data.right);
				 jDialog.Alert(data.right,function(){
		        var uptime = window.setTimeout(function(){
				$(location).attr('href', "${rc.contextPath}/order/handl/"+taskid);
		         clearTimeout(uptime);
		       },1000);});
				//location.reload([true]);
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
             "scrollX": true,
            <@security.authorize ifAnyGranted="advertiser">
            "aaSorting": [[6, "desc"]],
             </@security.authorize>
               <@security.authorize ifAnyGranted="sales,salesManager">
            "aaSorting": [[7, "desc"]],
             </@security.authorize>
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                <@security.authorize ifAnyGranted="advertiser">
                 { "orderable": false, "targets": [0,1,2,3,4,5,7] },
                  </@security.authorize>
                 <@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                  { "orderable": false, "targets": [0,1,2,3,4,5,7,8] },
                  </@security.authorize>
                    <@security.authorize ifAnyGranted="sales,salesManager">
                  { "orderable": false, "targets": [0,1,2,3,4,5,6,8,9] },
                  </@security.authorize>
                  
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-orderlist",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[longOrderId]"  : $('#longOrderId').val()
                        <@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                        ,"filter[userId]" : $('#autocomplete').val()
                         </@security.authorize>
                          ,"filter[taskKey]" : $('#taskKey').val()
                           <@security.authorize ifAnyGranted="sales,salesManager">
				  			 ,"filter[customerName]" : $('#customerName').val()
							</@security.authorize>
				
                       
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
                        return  (customer == null || customer=='undefined'
                        || typeof(customer) == "undefined"||typeof(customer.company) == "undefined")?"":customer.company;
                    }},
                    </@security.authorize>
            	{ "data": "order.contractCode", "defaultContent": "", "render": function(data, type, row, meta) {
            	var customer = $.parseJSON(row.order.customerJson);
            	var customerId=(customer == null || customer=='undefined'
                        || typeof(customer) == "undefined"||typeof(customer.username) == "undefined")?"":customer.username;
                        return "<a class='operation' onclick='eleContract(\"${rc.contextPath}\","+row.order.id+",\""+customerId+"\")' >"+data+"</a>";
                    }},
                { "data": "order.startTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                   	if (typeof(d) == "undefined"){
                    	return '';
                    }
                    	return d+'至'+$.format.date(row.order.endTime, "yyyy-MM-dd");
                }},
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
                 { "data": "product.type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '全屏硬广';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'body')
                            return '车身';
                        if (data == 'info')
                            return 'INFO字幕';
                        if (data == 'team')
                            return '团类广告';
                        return '';
                    } },
                { "data": "task_createTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                <@security.authorize ifAnyGranted="sales,salesManager,salesManager,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                 { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
	                 	 	return  "<a target='_blank' href='${rc.contextPath}/workflow/view/"+row.executionId+"/page/"+row.processInstanceId+"'>"+data+"</a>";
	                   
                  	 
                    }},
                     </@security.authorize>
                   { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
                   
                  		var tr="";
	                  if(row.task_assignee =='' || row.task_assignee == null){
		                  if(row.task_name=="支付" || row.task_name=="绑定素材"){
		                  tr=  "<a class='operation' href=\"javascript:;\" onclick=\"claim('"+row.order.id+"','"+( row.task_id)+"');\">"+row.task_name+"</a>&nbsp;";
		                  }else if(row.definitionKey =='jianboReport'){
		                  	  	  tr=  "<a class='operation' href=\"javascript:;\" onclick=\"claim('"+row.order.id+"','"+( row.task_id)+"');\">播放确认</a>&nbsp;";
		                 	}else{
		                 	 	tr=  "<a class='operation' href=\"javascript:;\" onclick=\"claim('"+row.order.id+"','"+( row.task_id)+"');\">签收</a>&nbsp;";
		                  	}
	                  	}else {
	                  	  	 var taskId = row.task_id;
	                  	  	 if(row.task_name=="支付" || row.task_name=="绑定素材"){
	                        	 tr= "<a class='operation' href='${rc.contextPath}/order/handl/"+taskId+ "'>"+row.task_name+"</a>&nbsp;";
	                  	  	 }else if(row.definitionKey =='jianboReport'){
		                  	  	  tr= "<a class='operation' href='${rc.contextPath}/order/handl/"+taskId+ "'>播放确认</a>&nbsp;";
		                 	}else if(row.definitionKey =='userFristPay'){
		                  	  	  tr= "<a class='operation' href='${rc.contextPath}/order/handl/"+taskId+ "'>付首款</a>&nbsp;";
		                 	}else{
	                  	  	  	tr= "<a class='operation' href='${rc.contextPath}/order/handl/"+taskId+ "'>办理</a>&nbsp;";
	                  	  	 }
	                    }	
	                    if(row.canClosed==true){
		                    	tr+="<a class='operation' href=\"javascript:;\" tip=\"未支付的订单可以关闭哦!\"  class=\"btn disabled layer-tips\" onclick=\"showCloseRemark('${rc.contextPath}','"+row.order.id+"','"+( row.task_id)+"');\">取消订单</a>&nbsp;";
		                }
                  	  return tr;
                    }
                   
                   },
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
	<@security.authorize ifAnyGranted="sales,salesManager,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单号：</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                           <@security.authorize ifAnyGranted="salesManager,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                          '    <span>广告主：</span>' +
                        '    <span>' +
                        '        <input id="autocomplete" value="">' +
                        '    </span>' +
                         </@security.authorize>
                        <@security.authorize ifAnyGranted="sales,salesManager">
                           '    <span>客户：</span>' +
                        '    <span>' +
                        '        <input id="customerName" style="width:200px" value="">' +
                        '    </span>' +
                          </@security.authorize>
                        '</div>'
        );

        $('#longOrderId, #autocomplete,#customerName').change(function() {
            table.fnDraw();
        });
        //author:impanxh 2015-05-20 22:36 自动补全功能
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
		
		
		bindLayerMouseOver();
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
                    '<select class="ui-input ui-input-mini" name="taskKey" id="taskKey">' +
                    '<option value="defaultAll" selected="selected">所有事项</option>' +
                  	'<option value="payment">支付</option>' +
                  	'<option value="bindstatic">绑定素材</option>' +
         			'</select>' +
                    '</div>'
        );

        $('#longOrderId, #taskKey').change(function() {
            table.fnDraw();
        });
        bindLayerMouseOver();
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
	 	  $("#recordsTotal").html("&nbsp;"+record_count+"&nbsp;");
		  }
    }
 		
    $(document).ready(function() {
        initTable();
       // setInterval("bindLayerMouseOver()",1500);
    } );
</script>


<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title" style="padding-top: 0px; text-align: left;">
		待办事项 <span id="recordsTotal"
			style="background-color: #ff9966; color: #fff; font-size: 14px; border-radius: 4px;"></span>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>下单用户</th>
				<th orderBy="longOrderId">订单编号</th>
				 <@security.authorize ifAnyGranted="sales,salesManager">
				<th>代理客户</th>
				</@security.authorize>
				
				<th>合同编号</th>
				<th>广告刊期</th>
				<th>套餐名称</th>
				<th>媒体类型</th>
				<th orderBy="created">创建时间</th> <@security.authorize
				ifAnyGranted="sales,salesManager,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
				<th orderBy="taskKey">当前节点</th> </@security.authorize>
				<th>操作</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>