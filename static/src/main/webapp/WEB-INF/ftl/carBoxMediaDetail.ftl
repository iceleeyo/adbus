<#import "template/template_blank.ftl" as frame> <#assign
security=JspTaglibs["/WEB-INF/tlds/security.tld"] /> <@frame.html
title="订单详情"
js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
"js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<style type="text/css">
.center {
	margin: auto;
}

.frame {
	width: 1000px;
}

.div {
	text-align: center;
	margin: 25px;
}

div#toolbar {
	float: left;
}

.processed {
	color: limegreen;
}

.invalid {
	color: red;
}

.hl {
	background-color: #ffff00;
}
</style>

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
               "aaSorting": [[5, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,2,3] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/carbox/ajax-queryCarBoxMedia",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[helpid]" : ${helpid}
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "product.name", "defaultContent": ""},
                 { "data": "product.type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '硬广广告';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'info')
                            return 'INFO字幕';
                        if (data == 'team')
                            return '团类广告';
                        return '';
                    } },
                 { "data": "product.duration", "defaultContent": ""},
                 { "data": "product.playNumber", "defaultContent": ""},
                 { "data": "product.days", "defaultContent": ""},
                 { "data": "product.price", "defaultContent": ""},
                 { "data": "needCount", "defaultContent": ""},
                 { "data": "totalprice", "defaultContent": ""},
              
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
        $("div#toolbar").html('');
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
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>套餐名称</th>
				<th>类型</th>
				<th>时长（秒）</th>
				<th>每天播放次数</th>
				<th>刊期(天)</th>
				<th>价格</th>
				<th>数量</th>
				<th>小计</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
