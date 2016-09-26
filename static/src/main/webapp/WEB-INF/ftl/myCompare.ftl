<#import "template/template.ftl" as frame> <#global menu="我的获拍">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="我的获拍"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"]
>

<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
              "aaSorting": [[2, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0,1,5,6] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/product/myComparePro",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[type]" : $('#protype').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "product.name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    return '<a class="layer-tips" tip="点击可查看套餐详细内容!" onclick="showProductlayer(\'${rc.contextPath}/product/ajaxdetail/\','+row.product.id+');"  >'+row.product.name+'</a>';
                } },
                { "data": "product.type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '硬广广告';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'body')
                            return '车身';
                        if (data == 'info')
                            return 'INFO字幕';
                        return '';
                    } },
                
                { "data": "created", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                } },
                { "data": "saleprice", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                { "data": "comparePrice", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                 { "data": "checkOrder", "defaultContent": "", "render": function(data) {
                    return data == 'Y' ? "已转订单" : "未转订单";
                } },
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        var operations = '';
                        if(row.checkOrder=='N'){
                          operations+= '<a class="table-link" href="${rc.contextPath}/product/win/'+data+'?pid='+row.product.id+'">转订单</a>';
                        }else{
                        operations+= '<a class="table-link" href="${rc.contextPath}/order/orderDetail/'+row.orderid+'?auto=y">查看订单详情</a>';
                        }
                       return operations;
                        
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

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>产品类型</span>' +
                         '<select class="ui-input ui-input-mini" name="protype" id="protype">' +
                    '<option value="" selected="selected">所有类型</option>' +
                  	'<option value="video">视频广告</option>' +
                  	'<option value="image">INFO图片广告</option>' +
                    '<option value="info">INFO字幕广告</option>' +
         			'</select>' +
                        '</div>'
        );

        $('#protype').change(function() {
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
	<div class="withdraw-title" style="padding-top: 0px; text-align: left;">
		竞价产品列表</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>套餐名称</th>
				<th>类型</th>
				<th orderBy="id">交易时间</th>
				<th orderBy="saleprice">竞价底价</th>
				<th orderBy="comparePrice">交易价格</th>
				<th>是否转订单</th>
				<th>操作</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
