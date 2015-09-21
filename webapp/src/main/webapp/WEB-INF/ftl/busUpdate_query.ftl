<#import "template/template.ftl" as frame>
<#global menu="车辆变更查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆变更历史" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
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
               "aaSorting": [[8, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,2,3,4,5,6,8] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-busUpdate_query",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[serinum]" : $('#serinum').val(),
                        "filter[pname]" : $('#pname').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "bus.plateNumber", "defaultContent": ""},
                { "data": "bus.serialNumber", "defaultContent": ""},
                { "data": "bus.oldSerialNumber", "defaultContent": ""},
                { "data": "model.name", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "line.levelStr", "defaultContent": ""},
                { "data": "busCategory", "defaultContent": ""},
                { "data": "company.name", "defaultContent": ""},
                { "data": "bus.description", "defaultContent": ""},
                { "data": "bus.office", "defaultContent": ""},
                { "data": "bus.branch", "defaultContent": ""},
                { "data": "busUpLog.updated", "defaultContent": "","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
                { "data": "busUpLog.updator", "defaultContent": ""},
              
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
        $("div#toolbar").html( '<div>' +
                        '    <span>车辆自编号</span>' +
                        '    <span>' +
                        '        <input id="serinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>车牌号</span>' +
                        '    <span>' +
                        '        <input id="pname" value="">' +
                        '    </span>&nbsp;&nbsp;' + '</div>');
                        
        $('#serinum,#pname').change(function() {
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
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >车牌号</th>
                        <th >车辆自编号</th>
                        <th>旧自编号</th>
                        <th >车型</th>
                        <th >线路</th>
                        <th >线路级别</th>
                        <th >类别</th>
                        <th >营销中心</th>
                         <th>车辆描述</th>
                        <th>公司名称</th>
                        <th>客户名称</th>
                        <th>最后更新时间</th>
                        <th>操作人</th>
                       
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>