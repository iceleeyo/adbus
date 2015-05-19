<#import "template/template.ftl" as frame>
<#global menu="待办事项">
<@frame.html title="待办事项列表" js=["js/jquery-dateFormat.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">


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
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "aaSorting": [[3, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                 { "orderable": false, "targets": [0,1,2,5] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-orderlist",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[longOrderId]"  : $('#longOrderId').val()
                        <@security.authorize ifAnyGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                        ,"filter[userId]" : $('#userId').val()
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
                { "data": "task_createTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                 { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
	                 	 	return  "<a target='_blank' href='${rc.contextPath}/workflow/view/"+row.executionId+"/page/"+row.processInstanceId+"'>"+data+"</a>";
	                   
                  	 
                    }},
                   { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
                   
                  
	                  if(row.task_assignee =='' || row.task_assignee == null){
	                 	 	return  "<a href=\"javascript:;\" onclick=\"claim('"+row.order.id+"','"+( row.task_id)+"');\">签收</a>";
	                  	}else {
	                  	   var taskId = row.task_id;
	                       var tr= "<a href='${rc.contextPath}/order/handleView2?orderid=" +(row.order.id)+ "&taskid="+taskId+ "'>办理</a>&nbsp;";
	                       return tr;
	                    }	
                  	 
                    }
                   
                   },
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
  		table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
	<@security.authorize ifAnyGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单号</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                          '    <span>广告主</span>' +
                        '    <span>' +
                        '        <input id="userId" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#longOrderId, #userId').change(function() {
            table.fnDraw();
        });
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
    }
    </@security.authorize>

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }

    $(document).ready(function() {
        initTable();
    } );
</script>


<div class="withdraw-wrap color-white-bg fn-clear">
              <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									待办事项 
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>下单用户</th>
                            <th>订单编号</th>
                        <th>套餐名称</th>
                       <!-- <th>素材号</th>-->
                        <th orderBy="created">创建时间</th>
                        <th orderBy="taskKey">待办事项</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>