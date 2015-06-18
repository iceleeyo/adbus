<#macro pickBuses order product city lineLevel categories title="选取车辆" buyLink=false>
<#if city.mediaType == 'body'>
<script type="text/javascript">
    var orderBusesTable;
    function refreshOrderedBuses () {
        orderBusesTable = $('#orderedBusesTable').dataTable( {
            "dom": '<"#toolbar">t',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/order/ajax-orderedbuses",
                data: function(d) {
                    return $.extend( {}, d, {
                        "orderId" : '${order.id}'
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "categoryStr", "defaultContent": ""},
                { "data": "company.name", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "model.name", "defaultContent": ""},
                { "data": "busNumber", "defaultContent": "", "render" : function(data) {
                    return '<span style="color: rgb(245, 135, 8);">' + data + '</span>';
                }},
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        var operations = '';

                        operations+= '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/order/ajax-remove-order-buses?orderId=${order.id}&id=' + data +'">删除</a>';

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
        orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function(data) {
                orderBusesTable.fnDraw(true);
                updateTotalOrdered(data);
            })
        });
    }
    function initComplete() {
        $("div#toolbar").attr("style", "width: 100%;")
        $("div#toolbar").html(
                '<div style="width:100%">' +
                        '<span style="font-size:18px; font-weight:500">已选车辆：</span>'+
                        '<span class="totalOrdered" style="color: rgb(245, 135, 8);">' + ${prod.busNumber - order.selectableBusesNumber} + '</span>/${prod.busNumber} 辆，剩余车辆将机动分配。' +
                        '<span><input type="button" onclick="openPopup()" class="block-btn" value="增加选择" style="float:right" ></span>' +
                '</div>'
        );

    }
    function updateTotalOrdered(order) {
        if (order && order.error && order.selectableBusesNumber) {
            $(".totalOrdered").text('' + (${prod.busNumber} - order.selectableBusesNumber));
            $(".toBeOrdered").text('' + order.selectableBusesNumber);
        }
    }

    function openPopup() {
        $("#orderBusesPopup").dialog("open");
    }
/*
        $.ajax({
            url : "${rc.contextPath}/order/ajax-orderedbuses",
            type : "GET",
            data : {
                "orderId" : '${order.id}',
            },
            success : function(data) {
                $("#orderedBuses").empty();
                for (var i in data) {
                    var o = data[i];
                    var html =
                            '<div>' +
                                    '        <span>类别：</span><span>' + o.categoryStr + '</span>' +
                                    '        <span>营销中心：</span><span>' + (o.company ? o.company.name : '') + '</span>' +
                                    '        <span>线路：</span><span>' + (o.line ? o.line.name : '') + '</span>' +
                                    '        <span>车型：</span><span>' + (o.model ? o.model.name : '') +'</span>' +
                                    '        <span>数量：</span><span>' + o.busNumber + '辆</span>' +
                                    '</div>';

                    $("#orderedBuses").append(html);
                }
            },
            error: function (data, error) {
                alert (error);
            }
        }, "text");
*/

    $(document).ready(function() {
        $( "#orderBusesPopup" ).dialog({
            autoOpen: false,
            height: 490,
            width: 450,
            modal: true,
            buttons: {
            },
            close: function() {
                $(".bus-line, .bus-model, .bus-company").empty();
                $("#busNumber").val("0");
            }
        });

        function refreshCompanies() {
            $(".bus-line, .bus-model, .bus-company").closest("div").hide();
            $(".bus-line, .bus-model, .bus-company").empty();

            var level = '${lineLevel}';
            var cate = $(".bus-category").val();
            if (cate == "")
                return;
            $.ajax({
                url : "${rc.contextPath}/bus/ajax-bus-companies",
                type : "POST",
                data : {
                    "level" : level,
                    "category" : cate
                },
                success : function(data) {
                    //update bus company options
                    $(".bus-company").empty();
                    $(".bus-company").append('<option value="" selected="selected"></option>');
                    for (var i in data) {
                        $(".bus-company").append('<option value="'+ data[i].id +'">'+ data[i].name + ' (共 ' + data[i].busCount + ' 辆)' +'</option>');
                    }
                    $(".bus-company").closest("div").show();
                    $(".bus-line, .bus-model").closest("div").hide();
                    $(".bus-line, .bus-model").empty();
                }
            }, "text");
        }
        function refreshLines() {
            $(".bus-line, .bus-model").closest("div").hide();
            $(".bus-line, .bus-model").empty();

            var level = '${lineLevel}';
            var cate = $(".bus-category").val();
            var company = $(".bus-company").val();
            if (cate == "" || company == "")
                return;
            $.ajax({
                url : "${rc.contextPath}/bus/ajax-bus-lines",
                type : "POST",
                data : {
                    "level" : level,
                    "category" : cate,
                    "companyId" : company
                },
                success : function(data) {
                    //update bus model options
                    $(".bus-line").empty();
                    $(".bus-line").append('<option value="" selected="selected"></option>');
                    for (var i in data) {
                        $(".bus-line").append('<option value="'+ data[i].id +'">'+ data[i].name + ' (共 ' + data[i].busCount + ' 辆)' +'</option>');
                    }
                    $(".bus-line, .bus-model").closest("div").show();
                    $(".bus-model").closest("div").hide();
                    $(".bus-model").empty();
                }
            }, "text");
        }

        function refreshModels() {
            $(".bus-model").closest("div").hide();
            $(".bus-model").empty();

            var level = '${lineLevel}';
            var cate = $(".bus-category").val();
            var company = $(".bus-company").val();
            var line = $(".bus-line").val();
            if (cate == "" || company == "" || line == "")
            return;

            $.ajax({
                url : "${rc.contextPath}/bus/ajax-bus-models",
                type : "POST",
                data : {
                    "level" : level,
                    "category" : cate,
                    "companyId" : company,
                    "lineId" : line
                },
                success : function(data) {
                    //update bus model options
                    $(".bus-model").empty();
                    $(".bus-model").append('<option value="" selected="selected"></option>');
                    for (var i in data) {
                        $(".bus-model").append('<option value="'+ data[i].id +'">'+ data[i].name + ' (共 ' + data[i].busCount + ' 辆)' +'</option>');
                    }
                    $(".bus-model").closest("div").show();
                }
            }, "text");
        }

        $(".bus-category").change(function() {
            refreshCompanies();
        });
        $(".bus-company").change(function() {
            refreshLines();
        });
        $(".bus-line").change(function() {
            refreshModels();
        });

        refreshOrderedBuses();
    });

    function sub() {
        $('#pickBusesForm').ajaxForm(
                function(data) {
                    jDialog.Alert(data.errorMessage);
                    if (data.error) {
                        updateTotalOrdered(data);
                        //refreshOrderedBuses();
                        orderBusesTable.dataTable()._fnAjaxUpdate();
                        $("#orderBusesPopup").dialog("close");
                    }
                }
        ).submit();
    }
</script>
    <div class="p20bs mt10 color-white-bg border-ec">
        <H3 class=".withdraw-title text-xl title-box"><A class="black" href="#">${title}</A></H3><br>
        <div id="orderedBuses">
            <table id="orderedBusesTable" class="display compact" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>类别</th>
                    <th>运营公司</th>
                    <th>线路</th>
                    <th>车型</th>
                    <th>数量（辆）</th>
                    <th>删除</th>
                </tr>
                </thead>

            </table>
        </div>

        <div id="orderBusesPopup"  title="选择车辆">
        <form data-name="withdraw" name="pickBusesForm" id="pickBusesForm"
              class="ui-form" method="post" action="${rc.contextPath}/order/ajax-order-buses"
              enctype="multipart/form-data">
            <input type="hidden" name="order.id" id="order.id" value="${order.id}"/>
        <div class="inputs">
            <div class="ui-form-item">
                <label class="ui-label mt10">线路级别：</label>
                <div>${lineLevel.nameStr}</div>
                <input type="hidden" name="level" id="level" value="${lineLevel}"/>
            </div>
            <div class="ui-form-item">
                <label class="ui-label mt10">共可选（辆）:</label>
                <div class="toBeOrdered">${order.selectableBusesNumber}</div>
            </div>
            <div class="ui-form-item">
                <label class="ui-label mt10">选择类别：</label>
                <select class="ui-input bus-category"
                        name="category" id="category">
                    <option value="" selected="selected"></option>
                    <#list categories as cate>
                        <option value="${cate}">${cate.nameStr}</option>
                    </#list>
                </select>
            </div>

            <div class="ui-form-item" style="display:none">
                <label class="ui-label mt10">营销中心：</label>
                <select class="ui-input bus-company"
                        name="company.id" id="company.id">
                    <option value="" selected="selected"></option>
                </select>
            </div>
            <div class="ui-form-item" style="display:none">
                <label class="ui-label mt10">选择线路：</label>
                <select class="ui-input bus-line"
                        name="line.id" id="line.id">
                    <option value="" selected="selected"></option>
                </select>
            </div>
            <div class="ui-form-item" style="display:none">
                <label class="ui-label mt10">选择车型：</label>
                <select class="ui-input bus-model"
                        name="model.id" id="model.id">
                    <option value="" selected="selected"></option>
                </select>
            </div>
            <div class="ui-form-item">
                <label class="ui-label mt10">选取数量：</label>
                <input class="ui-input validate[required,integer,min[1],max[2000]]"
                        type="number" value="0" name="busNumber"
                        id="busNumber" data-is="isAmount isEnough"
                        autocomplete="off" disableautocomplete="" placeholder="">
            </div>
        </div>
        <div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">
            <input type="button" onclick="sub()" class="block-btn"
                   value="确认选车" >
        </div>
        </form>
        </div>
    </div>
    </#if>
</#macro>