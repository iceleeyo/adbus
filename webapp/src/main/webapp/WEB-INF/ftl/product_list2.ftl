<#import "template/template.ftl" as frame>

<@frame.html title="产品套餐列表">

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
                url: "${rc.contextPath}/product/ajax-list",
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
                        if (filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                    return data;
                } },
                { "data": "type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '视频';
                        if (data == 'image')
                            return '图片';
                        if (data == 'info')
                            return 'Info';
                        return '';
                    } },
                { "data": "price", "defaultContent": "" },
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
                        return (row.enabled ? '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/' + data + '/disable">禁用</a> &nbsp;'
                                :'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/' + data + '/enable">启用</a> &nbsp;')
                        + '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/update/' + data +'">编辑</a>';
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
                        '    <span>套餐名称</span>' +
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
<#--            <div class="div" style="margin-top:25px">
                <caption><h2>产品套餐列表</h2></caption>
            </div>
            <div class="div">
                <hr/>
            </div>-->
            <div class="div">
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>套餐名称</th>
                        <th>类型</th>
                        <th>价格</th>
                        <th>状态</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>