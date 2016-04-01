<#import "template/template.ftl" as frame> <#global menu="日常检查">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="日常检查"
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
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
                          "aaSorting": [[1, "desc"]],
              "columnDefs": [
                 { "orderable": false, "targets": [0,2,3,4,5,6,7] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-offContract_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractCode]" : $('#contractCode').val(),
                         "filter[otype]" : 'PRIVATE_STATUS'
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "signDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "contractCode", "defaultContent": ""},
                { "data": "company", "defaultContent": ""},
                { "data": "relateMan", "defaultContent": ""},
                { "data": "salesman", "defaultContent": ""},
                { "data": "adway", "defaultContent": ""},
                { "data": "adcontent", "defaultContent": ""},
                { "data": "linecontent", "defaultContent": ""},
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
                        '    <span>合同编号：</span>' +
                        '    <span>' +
                        '        <input id="contractCode" value="">' +
                        '    </span>&nbsp;&nbsp;' +
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
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">日常检查</div>
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th orderBy="created">签订日期</th>
				<th>合同编号</th>
				<th>客户名称</th>
				<th>联系人</th>
				<th>业务员</th>
				<th>广告形式</th>
				<th>广告内容</th>
				<th>发布线路</th>
			</tr>
		</thead>
	</table>
</div>
</@frame.html>
