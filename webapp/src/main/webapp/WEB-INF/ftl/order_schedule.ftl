<#import "template/template.ftl" as frame>
<#import "template/orderDetail.ftl" as orderDetail/>
<#if mediaType == 'screen'>
    <#global menu="排期表">
<#elseif mediaType == 'body'>
    <#global menu="上刊巴士列表">
</#if>
<@frame.html title=menu>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
    #table {font-size: 14px;}
    #table td {position:relative;}
    #table td .per-middle {position:absolute;background-color: #ffad20;border-left: 1px solid white;border-right: 1px solid white;top:0;height:100%;z-index:1}
    #table td .per-first-or-last {position:absolute;background-color: #4acd48;border-left: 1px solid white;border-right: 1px solid white;top:0;height:100%;z-index:1}
</style>
<#if mediaType == 'screen'>
<script type="text/javascript">
    var table;
    function initTable () {
        $('#metatable').dataTable({
            "dom": 'rt',
            "searching": false,
            "ordering": false,
            "serverSide": false,
        });

        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/order-ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "orderId" : "${orderId}"
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
                {
                    "data" : "slot.name", "defaultContent": ""
                },
                {
                    "data" : "slot.startTimeStr", "defaultContent": ""
                },
                {
                    "data" : "slot.durationStr", "defaultContent": ""
                },
    <#list dates as d>
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        var result = '';
                        if (row.boxes['${d}'] && row.boxes['${d}'].goods) {
                            var box = row.boxes['${d}'];
                            var goods = row.boxes['${d}'].goods;
                            for (var i in goods) {
                                var left = goods[i].inboxPosition * 100 / box['size'];
                                var width = goods[i]['size'] * 100 / box['size'];
                                var style = ((goods[i].first || goods[i].last || goods[i].inboxPosition == 0 || goods[i].inboxPosition + goods[i]['size'] == box['size']) ? 'per-first-or-last': 'per-middle');
                                result += '<span class="' + style + '" style="left:'+ left +'%;width:'+ width +'%"></span>'
                            }
                            return result;
                        }
                        return "";
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
<#elseif mediaType == 'body'>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "columnDefs": [
                { "orderable": false, "targets": [7] },
            ],
            "iDisplayLength" : 10,
            "aLengthMenu": [[10, 20, 100], [10, 20, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/order-body-ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "orderId" : "${orderId}"
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "bus.plateNumber"},
                { "data": "bus.serialNumber"},
                { "data": "bus.line.name"},
                { "data": "bus.line.levelStr"},
                { "data": "bus.model.name"},
                { "data": "bus.categoryStr"},
                { "data": "bus.company.name"},
                { "data": "bus.description"},
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
    }

    function drawCallback() {
    }

    $(document).ready(function() {
        initTable();
    } );
</script>
</#if>

<@orderDetail.orderDetail orderview=orderview suppliesView=suppliesView quafiles="" suppliesLink=true viewScheduleLink=false/>

<#if mediaType == 'screen'>
<div class="p20bs mt10 withdraw-wrap color-white-bg fn-clear">
    <H3 class="text-xl title-box"><A class="black" href="#">排期表</A></H3>
            <div class="div" style="overflow-x: scroll">
                <table id="table" class="cell-border compact display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th style="min-width:60px;">时段名</th>
                        <th>时段</th>
                        <th>时长</th>
                        <#list dates as d>
                            <th style="min-width:60px;">${d?substring(5)}</th>
                        </#list>
                    </tr>
                    </thead>

                </table>
            </div>
</div>
<#elseif mediaType == 'body'>
<div class="p20bs mt10 withdraw-wrap color-white-bg fn-clear">
    <H3 class="text-xl title-box"><A class="black" href="#">上刊巴士列表</A></H3>
    <table id="table" class="display compact" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th orderBy="bus.plateNumber">车牌号</th>
            <th orderBy="bus.serialNumber">车辆自编号</th>
            <th orderBy="bus.line.name">线路</th>
            <th orderBy="bus.line.level">线路级别</th>
            <th orderBy="bus.model.name">车型</th>
            <th orderBy="bus.category">类别</th>
            <th orderBy="bus.company">营销中心</th>
            <th>车辆描述</th>
        </tr>
        </thead>

    </table>
</div>
</#if>
</@frame.html>