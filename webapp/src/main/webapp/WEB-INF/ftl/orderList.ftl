<#import "template/template.ftl" as frame>
<#global menu="待办事项">
<@frame.html title="待办事项列表" js=["jquery-dateFormat.js"]>
<script type="text/javascript">


	function claim(orderid,taskid){
 	$.ajax({
			url : "${rc.contextPath}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				alert(data.right);
				location.reload([true]);
			}
		}, "text");
	  
	}
	
	
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
                url: "${rc.contextPath}/order/ajax-orderlist",
                data: function(d) {
                    return $.extend( {}, d, {
                        "productId" : $('#productId').val(),
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
                { "data": "order.startTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
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

    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>套餐号</span>' +
                        '    <span>' +
                        '        <input id="productId" value="">' +
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
						 
<#--            <div class="div" style="margin-top:25px">
                <caption><h2>待办事项</h2></caption>
            </div>
            <div class="div">
                <hr/>
            </div>-->
            <div class="div">
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>下单用户</th>
                            <th>订单号</th>
                        <th>套餐号</th>
                       <!-- <th>素材号</th>-->
                        <th>创建时间</th>
                        <th>当前环节</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>