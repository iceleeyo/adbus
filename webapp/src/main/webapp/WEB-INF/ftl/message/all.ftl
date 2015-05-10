<#import "../template/template.ftl" as frame>
<#global menu="系统消息">
<@frame.html title="系统消息" js=["js/jquery-dateFormat.js"]>
<script type="text/javascript">


	function claim(orderid){
		window.location.href= "${rc.contextPath}/order/orderDetail/"+orderid;
	  
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
                url: "${rc.contextPath}/message/ajax-messagelist",
                data: function(d) {
                    return $.extend( {}, d, {
                        "productId" : $('#productId').val(),
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "longOrderId", "defaultContent": "","render": function(data, type, row, meta) {
	                 	 	return  "<a href=\"javascript:;\" onclick=\"claim('"+ (row.mainView.orderid)+"');\">"+(row.longOrderId)+"</a>";
                    }
                },
                { "data": "mainView.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                 { "data": "mainView.sub_type", "defaultContent": "",
	                 "render": function(data, type, row, meta) {
                        if (data == 'payFail')	 
                            return '您的订单支付失败';
                        if (data == 'pass')
                            return '您的订单已通过审核';
                        if (data == 'deny')
                            return '您的订单需要修改';
                               if (data == 'jianbo')
                            return '您的广告已经播出完成';
                               if (data == 'shangbo')
                            return '您的广告订单已经上播';
                               if (data == 'effective')
                            return '您的广告投放订单已经生效';
                        return '';
                   	 }
                    },
                   { "data": "mainView.main_type", "defaultContent": "","render": function(data, type, row, meta) {
                        if (data == 'unread')
                            return '未读';
                        if (data == 'read')
                            return '已读';
                        if (data == 'del')
                            return '已删除';
                        return '';
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

<div class="withdraw-wrap color-white-bg fn-clear">
              <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									系统消息
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                            <th>订单编号</th>
                       <!-- <th>素材号</th>-->
                        <th>消息发生时间</th>
                        <th>内容</th>
                        <th>状态</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>