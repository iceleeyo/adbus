<#import "template/template.ftl" as frame>
<#global menu="合同列表">
<@frame.html title="合同管理" js=["js/jquery-dateFormat.js"]>


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
                { "orderable": false, "targets": [5] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/contract/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractCode]" : $('#contractCode').val(),
                        "filter[contractName]" : $('#contractName').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "userId"},
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
                { "data": "contractName", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#contractName').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                        return data;
                }},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "endDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                } },
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        return '<a target="_blank" class="table-link" href="${rc.contextPath}/contract/contractDetail/' + data +'">查看合同</a>';
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
                        '    <span>合同号</span>' +
                        '        <input id="contractCode" value="">' +
                        '    <span>合同名称</span>' +
                        '        <input id="contractName" value="">' +
                        '</div>'
        );

        $('#contractCode, #contractName').change(function() {
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
                <caption><h2>合同列表</h2></caption>
            </div>
            <div class="div">
                <hr/>
            </div>-->
            <div class="withdraw-title" style="padding-top: 0px;text-align:left;">
									合同列表
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr class="tableTr">
                        <th orderBy="userId">广告主</th>
                        <th orderBy="contractCode">合同号</th>
                        <th orderBy="contractName">合同名称</th>
                        <th orderBy="startDate">生效时间</th>
                        <th orderBy="endDate">失效时间</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>


