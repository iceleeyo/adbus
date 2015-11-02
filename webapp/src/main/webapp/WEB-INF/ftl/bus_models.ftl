<#import "template/template.ftl" as frame>
<#global menu="车型管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车型管理" js=["js/jquery-dateFormat.min.js"]>

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
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "columnDefs": [
    <@security.authorize ifAnyGranted="BodyOrderManager">
                { "orderable": false, "targets": [4] },
    </@security.authorize>
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-all-models",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[manufacturer]" : $('#manufacturer').val(),
                        "filter[description]" : $('#description').val(),
                        "filter[doubleDecker]" : $('#doubleDecker').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "name", "defaultContent": ""},
                { "data": "description", "defaultContent": ""},
                { "data": "adSlot", "defaultContent": ""},
                { "data": "doubleDecker", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">双层</span>';
                        default :
                            return '<span class="processed">单层</span>';
                    }
                } },
                { "data": "manufacturer", "defaultContent": ""},
                { "data": function( row, type, set, meta) {
                    return row.id;
                    },
                    "render": function(data, type, row, meta) {
                         var operations ='<a onclick="showBusModelDetail(\'${rc.contextPath}\','+data+')">编辑</a>&nbsp;&nbsp;';
                         operations += '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-busmodel?id=' + data +'">删除</a>';
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
                        '    <span>车型：</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +
                        '   &nbsp; &nbsp; <span>生产商：</span>' +
                        '    <span>' +
                        '        <input id="manufacturer" value="">' +
                        '    </span>' +
                        '   &nbsp; &nbsp; <span>车型描述：</span>' +
                        '    <span>' +
                        '        <input id="description" value="">' +
                        '    </span>' +
                        '   &nbsp; &nbsp; <span>单双层：</span>' +
                        '    <span>' +
                        '        <select id="doubleDecker"><option value="defaultAll" selected="selected">所有</option><option value="false">单层</option><option value="true">双层</option></select>' +
                        '    </span>' +
                        '</div>'
        );

        $('#name,#manufacturer,#doubleDecker,#description').change(function() {
            table.fnDraw();
        });
    }

    function drawCallback() {
        $('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data.left){
			    layer.msg(data.right);
				 table.dataTable()._fnAjaxUpdate();
				 }else{
				  layer.msg(data.right);
				 }
			});
		});
    }

    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                车型管理
                 <a class="block-btn" style="margin-left: 10px;" href="javascript:void(0);" onclick="addBusModel('${rc.contextPath}')">添加车型</a>&nbsp;
                
									</div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th orderBy="name">车型</th>
                        <th orderBy="description">车型描述</th>
                        <th orderBy="description">广告位尺寸</th>
                        <th orderBy="double_decker">单双层</th>
                        <th orderBy="manufacturer">生产商</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>