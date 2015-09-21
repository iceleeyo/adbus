<#import "template/template.ftl" as frame>
<#global menu="订单列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="订单列表" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

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
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-publishLine_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractCode]" : $('#contractCode').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "id", "defaultContent": ""},
                { "data": "offlineContract.contractCode", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "OfflineContract.adcontent", "defaultContent": ""},
                { "data": "model.name", "defaultContent": ""},
                { "data": "id", "defaultContent": ""},
                { "data": "salesNumber", "defaultContent": ""},
                { "data": "remainNuber", "defaultContent": ""},
                { "data": "id", "defaultContent": "", "render": function(data) {
                 return '0';
                }},
                { "data": "days", "defaultContent": ""},
                { "data": "id", "defaultContent": ""},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "created", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                 { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                    var operations ='<a class="table-link" onclick="editPublishLine(\'${rc.contextPath}\','+data+');" href="javascript:void(0)">修改</a>';
                    operations +='&nbsp;&nbsp;<a class="table-link" target="_blank" href="${rc.contextPath}/bus/findBusByLineid/'+data+'">车辆上刊</a>';
                         return operations;
                    }}
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
                        '    <span>合同编号</span>' +
                        '    <span>' +
                        '        <input id="contractCode" value="">' +
                        '    </span>&nbsp;&nbsp;' +
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
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                订单列表
									</div>
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >订单编号</th>
                        <th >合同编号</th>
                        <th >线路</th>
                        <th>广告内容</th>
                        <th>车型</th>
                        <th>车型描述</th>
                        <th>订购数量</th>
                        <th>已上刊数量</th>
                        <th>在刊率</th>
                        <th>刊期</th>
                        <th>营销中心</th>
                        <th>预计上刊日期</th>
                        <th>订单记录时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>