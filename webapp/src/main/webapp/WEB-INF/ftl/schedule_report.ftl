<#import "template/template.ftl" as frame>
<#global menu="剩余时段">
<@frame.html title="剩余时段">

<style type="text/css">
    #table {font-size: 13px;}
    #table td {position:relative;}
    #table td .per-occupied {position:absolute;background-color: #ffad20;left:0;top:0;height:4px;}
    #table td .per-free {position:absolute;background-color: #4acd48;right:0;top:0;height:4px;}
</style>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/box-ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[from]" : "${from}",
                        "filter[days]" : "${days}",
                        "filter[type]" : "${type}"
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
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
    <#list dates as d>
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        var freePer = row.boxes['${d}'] ? (row.boxes['${d}'].remain * 100 / row.boxes['${d}'].size) : 100;
                        return (row.boxes['${d}'] ? row.boxes['${d}'].remainStr : row.slot.durationStr) +
                                '<span class="per-occupied" style="width:' + (100-freePer) + '%"></span>' +
                                '<span class="per-free" style="width:' + freePer + '%"></span>';
                }},
    </#list>
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
                        '    <span>时段名称</span>' +
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
<#--            <div class="div" style="margin-top:25px">
                <caption><h2>产品套餐列表</h2></caption>
            </div>
            <div class="div">
                <hr/>
            </div>-->
            <div class="withdraw-title" style="padding-top: 0px;text-align:center;">
									剩余时段
									</div>
                <table id="table" class="cell-border compact display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>时段名</th>
                        <th>时段</th>
                        <th>时长</th>
                        <#list dates as d>
                            <th>${d?substring(5)}</th>
                        </#list>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>