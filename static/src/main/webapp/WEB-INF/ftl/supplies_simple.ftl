<#import "template/template.ftl" as frame> <#global menu="物料展示">
<@frame.html title="物料展示列表">


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
            "ajax":{
                type: "GET",
                url: "${rc.contextPath}/supplies/ajax-simplelist",
                data: function(d) {
                    return $.extend( {}, d, {
                        "name" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
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
                { "data": "suppliesType", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '硬广广告';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'info')
                            return 'INFO字幕';
                        return '其他';
                    } },
				{ "data": "createTime", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        return row.createTime;
                    } },    
              
                
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
                        '    <span>物料名称</span>' +
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
	<#--
	<div class="div" style="margin-top: 25px">
		<caption>
			<h2>物料表</h2>
		</caption>
	</div>
	<div class="div">
		<hr />
	</div>
	-->
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>物料名称</th>
				<th>物料类型</th>
				<th>创建时间</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
