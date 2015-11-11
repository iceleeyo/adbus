<#import "template/template.ftl" as frame> <#global menu="时段设置">
<@frame.html title="媒体时段列表" js=["js/jquery-dateFormat.min.js"]>

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
            "ordering": true,
            "serverSide": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 9999], [20, 40, "全部"]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/timeslot/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "startTime", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var d = new Date(data);
                        d = new Date(data + d.getTimezoneOffset() * 60000);
                        return $.format.date(d, "HH:mm:ss");
                    } },
                { "data": "name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#name').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                    return data;
                } },
                { "data": "duration", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var d = new Date(data * 1000);
                        d = new Date(data * 1000 + d.getTimezoneOffset() * 60000);
                        return $.format.date(d, "mm:ss;").replace(":","'").replace(";","\"");
                    } },
                {
                  "data": "peak", "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        return data? '<span class="processed">是</span>' : "否";
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
                        '    <span>时段名称：</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#name').change(function() {
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
	<div class="withdraw-title">时段设置列表</div>
	<table id="table" class="display compact" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th orderBy="startTime">开始时间</th>
				<th orderBy="id">时段名称</th>
				<th orderBy="duration">时长</th>
				<th orderBy="peak">高峰</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
