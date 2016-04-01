<#import "template/template.ftl" as frame> <#global menu="销售员客户管理 ">
<@frame.html title="销售员客户管理 "
js=["js/layer-v1.9.3/layer/layer.js","js/layer.onload.js","js/jquery-dateFormat.js"]
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

<style type="text/css">
.operation
{
	color: #E0296C;
    font-weight: 800;
}
.operation-ok
{
	color: #29E039;
    font-weight: 800;
}
.operation-cancel
{
	color: #E0B229;
    font-weight: 800;
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
               "iDisplayLength" : 50,
            "aLengthMenu": [[20, 50, 100], [20, 50, 100]],
            "columnDefs": [
                {
                    "sClass": "align-left", "targets": [0,1] ,
                    "orderable": false, "targets": [0,1,2,3]
                },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/user/ajax-customerHistory",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[userId]" : '${userId!''}'
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "fieldViewName", "defaultContent": ""},
                { "data": "oldValue", "defaultContent": ""},
                { "data": "newValue", "defaultContent": ""},
                { "data": "operationUser", "defaultContent": ""},
                { "data": "updated", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
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
                        '</div>'
        );

        bindLayerMouseOver();
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
         bindLayerMouseOver();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		客户信息修改历史 
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>修改字段</th>
				<th>原值</th>
				<th>新值</th>
				<th>修改人</th>
				<th>修改时间</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
