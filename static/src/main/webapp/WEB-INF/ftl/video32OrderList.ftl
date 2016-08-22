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
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                 { "orderable": false, "targets": [0,1,2,3,4] },
                  
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-video32Orderlist",
                data: function(d) {
                    return $.extend( {}, d, {
                       
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "order.creator", "defaultContent": ""},
            	{ "data": "order.price", "defaultContent": ""},
                { "data": "status", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'paid')
                            return '待支付';
                       else if(data=='payed')
                        return '财务确认';
                        return '';
                    } },
                { "data": "r", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'N')
                            return '待处理';
                       else if(data=='Y')
                        return '审核通过';
                        return '';
                    } },
                { "data": "creater", "defaultContent": ""},
                { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                   	if (typeof(d) == "undefined"){
                    	return '';
                    }
                    	return d;
                }},
                { "data": "id", "defaultContent": "","render": function(data, type, row, meta) {
                	var opt= "<a class='operation' href='${rc.contextPath}/order/hand32Order/"+data+ "'>办理</a>&nbsp;";
                    	return opt;
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
        $("div#toolbar").html(''
        );
    
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
				<th>订单价格</th>
				<th>流程节点</th>
				<th>状态</th>
				<th>处理人</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
