<#import "template/template_blank.ftl" as frame> <#global menu="时段设置">
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
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 9999], [20, 40, "全部"]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/ajax-queryChangeLog",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[orderId]" : $('#orderId').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                 { "data": "created", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "userId", "defaultContent": ""
                      },
                { "data": "startDate", "defaultContent": "",
                    },
                      { "data": "isCallAfterDayAll", "defaultContent": "",
                    },
                
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
	<input type="hidden" id ="orderId" value ="${orderid}">
	<table id="table" class="display compact" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>操作时间</th>
				<th>操作用户</th>
				<th>撤销日期</th>
				<th>撤销当天以后所有排期</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
