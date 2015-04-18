<#import "template/template.ftl" as frame>
<#global menu="排期表">
<@frame.html title="排期表">

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
            <div class="div" style="margin-top:5px">
                <caption><h2>订单详情</h2></caption>
            </div>
            <div class="div">
                <table id="metatable" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>订单号</th>
                        <th>用户</th>
                        <th>广告素材</th>
                        <th>套餐</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${orderIdSeq}</td>
                        <td>${order.userId}</td>
                        <td><a href="${rc.contextPath}/supplies/suppliesDetail/${order.supplies.id}" target="_blank">${order.supplies.name}</a></td>
                        <td><a href="${rc.contextPath}/product/d/${order.product.id}" target="_blank">${order.product.name}</a></td>
                        <td>${order.startTime}</td>
                        <td>${order.endTime}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="div">
                <table id="table" class="cell-border compact display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
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