<#import "template/template.ftl" as frame>
<#global menu="合同管理">
<@frame.html title="合同列表">


<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/contract/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "contractCode" : $('#contractCode').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "contractCode", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#contractCode').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                    return data;
                } },
                { "data": "contractName", "defaultContent": ""},
                { "data": "startDate", "defaultContent": ""},
                { "data": "endDate", "defaultContent": "" },
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        return '<a class="table-link" href="${rc.contextPath}/contract/' + data +'">编辑</a>';
                    }},
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
                        '    <span>合同号</span>' +
                        '    <span>' +
                        '        <input id="contractCode" value="">' +
                        '    </span>' +
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

<#--            <div class="div" style="margin-top:25px">
                <caption><h2>合同列表</h2></caption>
            </div>
            <div class="div">
                <hr/>
            </div>-->
            <div class="div">
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>合同号</th>
                        <th>合同名称</th>
                        <th>生效时间</th>
                        <th>失效时间</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>


