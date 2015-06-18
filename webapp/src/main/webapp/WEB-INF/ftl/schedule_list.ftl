<#import "template/template.ftl" as frame>
<#global menu="排条单">
<@frame.html title="排条单" js=["js/jquery-dateFormat.js", "js/jquery-ui/jquery-ui.js", "js/datepicker.js",
"js/jquery.datepicker.region.cn.js", "js/jquery-dataTables-fnFakeRowspan.js"<#--, "js/tabletools/js/dataTables.tableTools.js"-->]
css=["js/jquery-ui/jquery-ui.css"<#--, "js/tabletools/css/dataTables.tableTools.min.css"-->]>

<style type="text/css">
    #table.dataTable thead th:first-child, #table.dataTable thead td:first-child,
    #table.dataTable.cell-border tbody tr th:first-child,
    #table.dataTable.cell-border tbody tr td:first-child {display: none;}
    #table {font-size: 13px;}
    #table td {position:relative;}
    #table td .per-occupied {position:absolute;background-color: #ffad20;left:0;top:0;height:4px;}
    #table td .per-free {position:absolute;background-color: #4acd48;right:0;top:0;height:4px;}
</style>
<style type="text/css">
    .ui-datepicker-calendar.only-month {
        display: none;
    }
    .report-toolbar {
        float: right;
    }

    .report-toolbar .ui-label-mini {
        font-size: 12px;line-height: 35px;
    }
</style>
<script type="text/javascript">
    $(function(){
            $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

        $("#day").change(function() {
            $(location).attr('href', "list?day=" + $("#day").val());
        });
    });

</script>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
/*            "tableTools": {
                "aButtons": [   {
                                "sExtends":     "xls",
                                "sButtonText": "导出Excel"
                                },
                                {
                                    "sExtends":     "pdf",
                                    "sButtonText": "导出PDF"
                                },
                                {
                                    "sExtends":     "print",
                                    "sButtonText": "打印预览"
                                }],
                "mColumns": [0, 1, 1, 1, 1, 1],
                "sSwfPath": "${rc.contextPath}/js/tabletools/swf/copy_csv_xls_pdf_2.swf"
            },*/
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "width": "10%", "targets": 1 },
                { "width": "15%", "targets": 2 },
                { "width": "10%", "targets": 3 },
                { "sClass": "align-left", "targets": 4 },
                { "width": "10%", "targets": 5 }
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/box-detail-ajax-list",
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
                { "data": "slot.name", "defaultContent": "",
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
                {
                    "data" : "slot.startTimeStr", "defaultContent": ""
                },
                {
                    "data" : "slot.durationStr", "defaultContent": ""
                },
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        var box = row.boxes['${day}'];
                        return box && box.goods && box.goods.length ?
                                '<a href="${rc.contextPath}/supplies/suppliesDetail/'
                                        + box.goods[0].order.supplies.id + '" target="_blank">'
                                        + box.goods[0].order.supplies.seqNumber + '&nbsp;'
                                        +box.goods[0].order.supplies.name + '</a>'
                                : '';
                }},
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        var box = row.boxes['${day}'];
                        return box && box.goods && box.goods.length ? box.goods[0].order.product.duration : '';
                }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback
        });
        table.fnFakeRowspan(1, [1, 2, 3]).fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>时段名称</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +
                        '    <span><a href="javascript:void()" id="export_xls" class="btn-sm btn-success">导出Excel</a>' +
                        '    </span>' +
                        '</div>'
        );

        $("#export_xls").click(function(){
            location.href='${rc.contextPath}/schedule/list.xls?filter[day]=${day}';
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
</script>
<div class="withdraw-wrap color-white-bg fn-clear">

            <div class="withdraw-title">
                                媒体排条单[${day}]
                <div class="report-toolbar">
                    <input
                            class="ui-input ui-input-mini datepicker" type="text" name="day"
                            id="day" data-is="isAmount isEnough"
                            autocomplete="off" disableautocomplete="">
                </div>
            </div>
            <table id="table" class="cell-border compact display" cellspacing="0"
                   style="width: 100%; border-left-style: solid; border-left-width: 1px; border-left-color: rgb(221, 221, 221);">
                <thead>
                <tr>
                    <th></th>
                    <th>时段名</th>
                    <th>时段</th>
                    <th>包长</th>
                    <th>广告名称</th>
                    <th>时长</th>
                </tr>
                </thead>
            </table>
            </tbody>
</div>
</@frame.html>