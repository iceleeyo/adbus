<#import "template/template.ftl" as frame>
<#global menu="进行中订单">
<@frame.html title="进行中的订单" js=["js/jquery-dateFormat.js"]>
<script type="text/javascript">

	function claim(orderid,taskid){
 	$.ajax({
			url : "${rc.contextPath}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				jDialog.Alert(data.right);
				location.reload([true]);
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
                { "orderable": false, "targets": [0,1,2,4,5,6] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-runningAjax",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[longOrderId]" : $('#longOrderId').val(),
                        "filter[taskKey]" : $('#taskKey').val()
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
                { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                 { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
	                 	 	return  "<a target='_blank' href='${rc.contextPath}/workflow/view/"+row.executionId+"/page/"+row.processInstanceId+"'>"+data+"</a>";
	                   
                  	 
                    }},
                   { "data": "task_assignee", "defaultContent": ""
                   
                   },
                   { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' href='${rc.contextPath}/order/orderDetail/0?taskid=" +(row.task_id)+  "'>查看详情</a>";
                	return tr;
                }}
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();

    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单号</span>' +
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
									所有订单
			</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>用户</th>
                        <th>订单名称</th>
                        <th>套餐名称</th>
                       <!-- <th>素材号</th>-->
                        <th orderBy="created">创建时间</th>
                        <th>当前环节</th>
                        <th>处理人</th>
                         <th>操作</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>