<#import "template/template.ftl" as frame>
<#global menu="用户列表">
<@frame.html title="用户列表">

<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0,1] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/user/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "name" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "username", "defaultContent": "",
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
                { "data": "groups", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data.length > 1) {
                            var g = '<select class="ui-input" name="groups">';
                            $.each(data, function(i) {
                                g += '<option value="' + data[i].id + '" >' + data[i].id + '</option>';
                            });
                            g += '</select>'
                            return g;
                        } else {
                            return data.length ? data[0].id : "";
                        }
                    } },
                { "data": "enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } },
                { "data": function( row, type, set, meta) {
                    return row.username;
                },
                    "render": function(data, type, row, meta) {
                        return (row.enabled ? '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/user/' + data + '/disable">禁用</a> &nbsp;'
                                :'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/user/' + data + '/enable">启用</a> &nbsp;')
                        + '<a class="table-link" href="${rc.contextPath}/user/' + data +'">编辑</a>';
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
                        '    <span>用户名</span>' +
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
                        <th>用户名</th>
                        <th>所属组</th>
                        <th>状态</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>