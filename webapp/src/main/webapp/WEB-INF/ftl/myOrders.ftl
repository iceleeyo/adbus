<#import "template/template.ftl" as frame>
<#global menu="${orderMenu}">
<@frame.html title="我参与的订单" js=["jquery-dateFormat.js"]>
<script type="text/javascript">
var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-myOrders",
                data: function(d) {
                    return $.extend( {}, d, {
                        "longOrderId" : $('#longOrderId').val(),
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "order.creator", "defaultContent": ""},
            	{ "data": "longOrderId", "defaultContent": ""},
            	
                { "data": "order.startTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
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
                }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );

    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单名称</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#contractCode').change(function() {
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


            <div class="div">
            <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									${orderMenu}
									</div>
            
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>下单用户</th>
                        <th>订单编号</th>
                        <th>起播时间</th>
                        <th>创建时间</th>
                        <th>当前环节</th>
                        <th>当前处理人</th>
                         <th>订单详情</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>