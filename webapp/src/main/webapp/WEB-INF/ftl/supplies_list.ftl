<#import "template/template.ftl" as frame>
<#global menu="物料管理">
<@frame.html title="物料管理" js=["jquery-dateFormat.js"]>
    <!-- <script>
        function pages(pageNum) {
            var by = ($("#by").val());
            var name = ($("#name").val());
            var g2 = ($("#textpage").val());
            if (g2 == undefined) {
                g2 = 1;
            }
            if (!isNaN($("#textpage").val())) {
            } else {
                jDialog.Alert("请输入数字");
                return;
            }
            if (parseInt($("#textpage").val()) <= 0) {
                jDialog.Alert("请输入正整数");
                return;
            }
            if ($("#textpage").val() > pageNum) {
                jDialog.Alert("输入的页数超过最大页数");
                return;
            }
            window.location.href = "${rc.contextPath}/supplies/list/" + g2 + "?name="+ name;
        }

        function page(num) {
            var name = $("#name").val();
            var by = ($("#by").val());
            window.location.href = "${rc.contextPath}/supplies/list/" + num + "?name=" + name;
        }

        function sub(){
            var name = $("#name").val();
            window.location.href= "${rc.contextPath}/supplies/list/1"+"?name="+name
        }
    </script> -->
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [3] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/supplies/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "id", "defaultContent": ""},
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
                { "data": "suppliesType", "defaultContent": "",
                "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '视频';
                        if (data == 'image')
                            return '图片';
                        if (data == 'info')
                            return '文本';
                        return '';
                    } },
                    { "data": "userId", "defaultContent": ""},
                   { "data": "created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        return  '<a class="table-link" href="${rc.contextPath}/supplies/suppliesDetail/'+data+'">查看附件</a>';
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
                        '    <span>物料名称</span>' +
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
                <caption><h2>物料列表</h2></caption>
            </div>
            <div class="div">
                <hr/>
            </div>-->
            <div class="div">
                <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									物料列表
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th orderBy="name">物料编号</th>
                        <th orderBy="name">物料名称</th>
                        <th orderBy="suppliesType">物料类型</th>
                        <th orderBy="created">创建人</th>
                        <th orderBy="created">创建时间</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>