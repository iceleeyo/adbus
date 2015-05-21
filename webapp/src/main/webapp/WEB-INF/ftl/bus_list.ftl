<#import "template/template.ftl" as frame>
<#global menu="巴士列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="巴士列表" js=["js/jquery-dateFormat.min.js"]>

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
            "ordering": true,
            "serverSide": true,
            "columnDefs": [
                { "orderable": false, "targets": [7, 9] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[plateNumber]" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "plateNumber"},
                { "data": "serialNumber"},
                { "data": "line.name"},
                { "data": "line.levelStr"},
                { "data": "model.name"},
                { "data": "categoryStr"},
                { "data": "company.name"},
                { "data": "description"},
                { "data": "enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } },
                { "data": function( row, type, set, meta) {
                    return row.id;
                    },
                    "render": function(data, type, row, meta) {
                        var operations = '';
                        <@security.authorize ifAnyGranted="BodyOrderManager">
                            operations+= (row.enabled ? '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/' + data + '/disable">禁用</a> &nbsp;'
                                    :'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/' + data + '/enable">启用</a> &nbsp;')
                            operations +='<a class="table-link" href="${rc.contextPath}/bus/' + data +'">编辑</a>&nbsp;';
                        </@security.authorize>
                        return operations;

                    }
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
        $("div#toolbar").html(
                '<div>' +
                        '    <span>车牌号</span>' +
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
            <div class="withdraw-title">
                巴士列表
									</div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th orderBy="plateNumber">车牌号</th>
                        <th orderBy="serialNumber">车辆自编号</th>
                        <th orderBy="line.name">线路</th>
                        <th orderBy="line.level">线路级别</th>
                        <th orderBy="model.name">车型</th>
                        <th orderBy="category">类别</th>
                        <th orderBy="company">营销中心</th>
                        <th>车辆描述</th>
                        <th orderBy="enabled">状态</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>