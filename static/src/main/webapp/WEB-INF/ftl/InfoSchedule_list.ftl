<#import "template/template.ftl" as frame> <#global menu="排条单">
<@frame.html title="排条单" js=["js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.js", "js/datepicker.js",
"js/jquery.datepicker.region.cn.js",
"js/jquery-dataTables-fnFakeRowspan.js"<#--,
"js/tabletools/js/dataTables.tableTools.js"-->]
css=["js/jquery-ui/jquery-ui.css"<#--,
"js/tabletools/css/dataTables.tableTools.min.css"-->]>

<style type="text/css">
#table.dataTable thead th:first-child,#table.dataTable thead td:first-child,#table.dataTable.cell-border tbody tr th:first-child,#table.dataTable.cell-border tbody tr td:first-child
	{
	display: none;
}

#table {
	font-size: 13px;
}

#table td {
	position: relative;
}

#table td .per-occupied {
	position: absolute;
	background-color: #ffad20;
	left: 0;
	top: 0;
	height: 4px;
}

#table td .per-free {
	position: absolute;
	background-color: #4acd48;
	right: 0;
	top: 0;
	height: 4px;
}

.report-toolbar {
	float: left !important;
	margin-top: 40px;
}
</style>
<style type="text/css">
.ui-datepicker-calendar.only-month {
	display: none;
}

.report-toolbar {
	float: right;
}

.report-toolbar .ui-label-mini {
	font-size: 12px;
	line-height: 35px;
}

div#toolbar {
	float: left;
	margin-left: 89%;
	padding: 10px 0 10px 0;
	margin-top: -40px;
}
</style>
<script type="text/javascript">
    $(function(){
            $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

        $("#day").change(function() {
            $(location).attr('href', "info-list?day=" + $("#day").val());
        });
    });

</script>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "width": "10%", "targets": 1 },
                { "width": "20%", "targets": 2 },
                { "width": "60%", "targets": 3 }
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/info-ajax-list/info",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[day]" : "${day}",
                        "filter[type]" : "${type}"
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
                { "data": "", "render" : function () { return "";}},
                {
                    "data" : "id", "defaultContent": ""
                },
                {
                    "data" : "order.supplies.seqNumber", "defaultContent": ""
                },
                {
                    "data" : "order.supplies.infoContext", "defaultContent": ""
                },
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback
        });
        //table.fnFakeRowspan(1, [0,1, 2]).fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div style="float:right">' +
                        '    <span><a href="javascript:void()" id="export_xls" class="btn-sm btn-success">导出Excel</a>' +
                        '    </span>' +
                        '</div>'
        );

        $("#export_xls").click(function(){
            location.href='${rc.contextPath}/schedule/exportInfoImglist/info?filter[day]=${day}';
        });

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
    
    
//订单物料详情
    function supDetail(data){
	layer.open({
    		type: 1,
    		title: "物料及资质",
    		skin: 'layui-layer-rim', 
    		area: ['1000px', '529px'], 
    		content:''
			   +' '
			   +'<iframe frameborder="no" style="width:99%;height:99%" src="${rc.contextPath}/supplies/suppliesDetail/'+data+'"/>'
			});
}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">

	<div class="withdraw-title">
		<div class="tabs">
			<a id="tab1" href="${rc.contextPath}/schedule/list">视频广告排条单</a> <a
				id="tab2" href="${rc.contextPath}/schedule/img-list">图片广告排条单</a> <a
				id="tab3" class="active" href="${rc.contextPath}/schedule/info-list">字幕广告排条单[${day}]</a>
		</div>
		<div class="report-toolbar">
			<input class="ui-input ui-input-mini datepicker" type="text"
				name="day" id="day" data-is="isAmount isEnough" autocomplete="off"
				disableautocomplete="">
		</div>
	</div>
	<table id="table" class="cell-border compact display" cellspacing="0"
		style="width: 100%; border-left-style: solid; border-left-width: 1px; border-left-color: rgb(221, 221, 221);">
		<thead>
			<tr>
				<th></th>
				<th>序号</th>
				<th>广告编号</th>
				<th>字幕服务信息内容</th>
			</tr>
		</thead>
	</table>
	</tbody>
</div>
</@frame.html>
