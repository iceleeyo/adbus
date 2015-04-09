<#import "template/template.ftl" as frame>
<#global menu="视频时段">
<@frame.html title="视频时段列表" js=["jquery-dateFormat.min.js"]>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
</style>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/timeslot/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "name" : $('#name').val()
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
            <div class="div">
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>开始时间</th>
                        <th>时段名称</th>
                        <th>时长</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>