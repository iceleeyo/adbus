<#import "template/template.ftl" as frame>
<#global menu="已完成的订单">
<@frame.html title="已完成的订单" js=["jquery-dateFormat.js"]>

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
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-finishedOrders",
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
                { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                  { "data": "order.created", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' href='${rc.contextPath}/order/orderDetail/" +(row.id)+ "?pid="+(row.processInstanceId) +  "'>查看详情</a>";
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
                        '    <span>订单号</span>' +
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
            
            <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									已完成的订单
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>下单用户</th>
                            <th>订单编号</th>
                        <th>套餐名称</th>
                       <!-- <th>素材号</th>-->
                        <th>创建时间</th>
                         <th>订单详情</th>
                       
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>