<#import "template/template.ftl" as frame> <#global menu="待办事项">
<@frame.html title="待办事项列表"
css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css","css/layer.css"]
js=["js/layer.min.js","js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer-site.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
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
			url : "${rc.contextPath}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				//jDialog.Alert(data.right);
				 jDialog.Alert(data.right,function(){
		        var uptime = window.setTimeout(function(){
				$(location).attr('href', "${rc.contextPath}/order/handleView2?orderid=" +(orderid)+ "&taskid="+taskid);
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
            "aaSorting": [[4, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                 { "orderable": false, "targets": [0,1,2,3,5] },
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
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "order.creator", "defaultContent": ""},
            	{ "data": "longOrderId", "defaultContent": ""},
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
                            return '视频';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'body')
                            return '车身';
                        if (data == 'info')
                            return 'INFO字幕';
                        return '';
                    } },
                { "data": "task_createTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                <@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                 { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
	                 	 	return  "<a target='_blank' href='${rc.contextPath}/workflow/view/"+row.executionId+"/page/"+row.processInstanceId+"'>"+data+"</a>";
	                   
                  	 
                    }},
                     </@security.authorize>
                   { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
                   
                  		var tr="";
	                  if(row.task_assignee =='' || row.task_assignee == null){
		                  if(row.task_name=="支付" || row.task_name=="绑定素材"){
		                  tr=  "<a href=\"javascript:;\" onclick=\"claim('"+row.order.id+"','"+( row.task_id)+"');\">"+row.task_name+"</a>&nbsp;";
		                  }else if(row.definitionKey =='jianboReport'){
		                  	  	  tr=  "<a href=\"javascript:;\" onclick=\"claim('"+row.order.id+"','"+( row.task_id)+"');\">播放确认</a>&nbsp;";
		                 	}else{
		                 	 	tr=  "<a href=\"javascript:;\" onclick=\"claim('"+row.order.id+"','"+( row.task_id)+"');\">签收</a>&nbsp;";
		                  	}
	                  	}else {
	                  	  	 var taskId = row.task_id;
	                  	  	 if(row.task_name=="支付" || row.task_name=="绑定素材"){
	                        	 tr= "<a href='${rc.contextPath}/order/handleView2?orderid=" +(row.order.id)+ "&taskid="+taskId+ "'>"+row.task_name+"</a>&nbsp;";
	                  	  	 }else if(row.definitionKey =='jianboReport'){
		                  	  	  tr= "<a href='${rc.contextPath}/order/handleView2?orderid=" +(row.order.id)+ "&taskid="+taskId+ "'>播放确认</a>&nbsp;";
		                 	}else{
	                  	  	  tr= "<a href='${rc.contextPath}/order/handleView2?orderid=" +(row.order.id)+ "&taskid="+taskId+ "'>办理</a>&nbsp;";
	                  	  	 }
	                    }	
	                    if(row.canClosed==true){
		                    	tr+="<a href=\"javascript:;\" tip=\"未支付的订单可以关闭哦!\"  class=\"btn disabled layer-tips\" onclick=\"showCloseRemark('${rc.contextPath}','"+row.order.id+"','"+( row.task_id)+"');\">关闭</a>&nbsp;";
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
	<@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
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
                        '</div>'
        );

        $('#longOrderId, #autocomplete').change(function() {
            table.fnDraw();
        });
        //author:impanxh 2015-05-20 22:36 自动补全功能
        $( "#autocomplete" ).autocomplete({
  			source: "${rc.contextPath}/user/autoComplete",
  			change: function( event, ui ) { 
  				/*if(ui.item!=null){alert(ui.item.value);}*/
  				table.fnDraw();
  			 },
  			 select: function(event,ui) {
  			 $('#autocomplete').val(ui.item.value);
  				table.fnDraw();
  			 }
		});
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
				<th>套餐名称</th>
				<th>媒体类型</th>
				<th orderBy="created">创建时间</th> <@security.authorize
				ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
				<th orderBy="taskKey">当前节点</th> </@security.authorize>
				<th>操作</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
