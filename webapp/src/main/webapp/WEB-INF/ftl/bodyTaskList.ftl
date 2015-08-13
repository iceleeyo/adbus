<#import "template/template.ftl" as frame>
<#global menu="待办事项">
<@frame.html title="待办事项列表" css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css","css/layer.css"] js=["js/layer.min.js","js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer-site.js"]>
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
				$(location).attr('href', "${rc.contextPath}/busselect/body_handleView?orderid=" +(orderid)+ "&taskid="+taskid);
		         clearTimeout(uptime);
		       },1000);});
				//location.reload([true]);
			}
		}, "text");
	  
	}
	
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "aaSorting": [[4, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                 { "orderable": false, "targets": [0,1,2,3] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-orderlist",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[seriaNum]"  : $('#seriaNum').val()
                        <@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
                        ,"filter[userId]" : $('#autocomplete').val()
                         </@security.authorize>
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "jpaBodyContract.creator", "defaultContent": ""},
            	{ "data": "jpaBodyContract.seriaNum", "defaultContent": ""},
                { "data": "task_createTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                <@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
                 { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
	                 	 	return  "<a target='_blank' href='${rc.contextPath}/workflow/view/"+row.executionId+"/page/"+row.processInstanceId+"'>"+data+"</a>";
	                   
                  	 
                    }},
                     </@security.authorize>
                   { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
                   
                  		var tr="";
	                  if(row.task_assignee =='' || row.task_assignee == null){
		                 	 	tr=  "<a href=\"javascript:;\" onclick=\"claim('"+row.jpaBodyContract.id+"','"+( row.task_id)+"');\">签收</a>&nbsp;";
	                  	}else {
	                  	  	  tr= "<a href='${rc.contextPath}/busselect/body_handleView?orderid=" +(row.jpaBodyContract.id)+ "&taskid="+row.task_id+ "'>办理</a>&nbsp;";
	                    }	
	                    if(row.canClosed==true){
		                    	tr+="<a href=\"javascript:;\" tip=\"未支付的订单可以关闭哦!\"  class=\"btn disabled layer-tips\" onclick=\"showCloseRemark('${rc.contextPath}','"+row.jpaBodyContract.id+"','"+( row.task_id)+"');\">关闭</a>&nbsp;";
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
	<@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>流水号</span>' +
                        '    <span>' +
                        '        <input id="seriaNum" value="">' +
                        '    </span>' +
                          '    <span>合同申请人</span>' +
                        '    <span>' +
                        '        <input id="autocomplete" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#seriaNum, #autocomplete').change(function() {
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
    
    <@security.authorize ifAnyGranted="bodysales">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>流水号</span>' +
                        '    <span>' +
                        '        <input id="seriaNum" value="">' +
                        '    </span>' +
                    '</div>'
        );

        $('#seriaNum').change(function() {
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
              <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									待办事项 <span id="recordsTotal"  style="background-color:#ff9966;color: #fff;font-size: 14px;border-radius: 4px;"></span>
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>合同申请人</th>
                        <th orderBy="seriaNum">流水号</th>
                        <th>法人代表</th>
                        <th orderBy="created">创建时间</th>
                        <@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
                        <th orderBy="taskKey">当前节点</th>
                        </@security.authorize>
                        <th>操作</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>