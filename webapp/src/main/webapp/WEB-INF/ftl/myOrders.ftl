<#import "template/template.ftl" as frame>
<#global menu="${orderMenu}">
<@frame.html title="我参与订单" js=["js/jquery-dateFormat.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
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
                { "orderable": false, "targets": [0,1,2,4,5] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-myOrders",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[longOrderId]" : $('#longOrderId').val()
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
            	
                { "data": "order.startTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
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
                	 var tr= "<a target='_blank' href='${rc.contextPath}/order/orderDetail/0?taskid=" +(row.task_id)+  "'>查看详情</a>";
                	return tr;
                }},
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
	                        '    <span>订单编号</span>' +
	                        '    <span>' +
	                        '        <input id="longOrderId" value="">' +
	                        '    </span>' +
	                             '    <span>广告主</span>' +
                        '    <span>' +
                        '        <input id="userId" value="">' +
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
	
	        $('#longOrderId,#userId, #taskKey').change(function() {
	            table.fnDraw();
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

    $(document).ready(function() {
        initTable();
    } );
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									${orderMenu}
									</div>
            
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>下单用户</th>
                        <th>订单编号</th>
                        <th>起播时间</th>
                        <th orderBy="created">创建时间</th>
                        <th>待办事项</th>
                        <th>当前处理人</th>
                         <th>订单详情</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>