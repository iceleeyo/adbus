<#import "template/template.ftl" as frame>
<#global menu="产品查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="产品套餐列表">

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
                { "orderable": false, "targets": [4] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/product/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        return '<a class="table-link" href="${rc.contextPath}/product/d/'+row.id+'">'+row.name+'</a>';
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
                        var operations = '';
                   		 <@security.authorize ifAnyGranted="ShibaOrderManager">  
                     	operations+= (row.enabled ? '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/' + data + '/disable">禁用</a> &nbsp;'
                                :'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/' + data + '/enable">启用</a> &nbsp;')
                        operations +='<a class="table-link" href="${rc.contextPath}/product/' + data +'">编辑</a>&nbsp;';
                        </@security.authorize>
                        if(row.enabled){
                        	<@security.authorize ifAnyGranted="normaluser">
                     	 	 operations+= '<a class="table-link" href="${rc.contextPath}/order/buypro/'+data+'">购买</a>';
                     	    </@security.authorize>
                    	}
                       return operations;
                        
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
            <div class="withdraw-title" style="padding-top: 0px;text-align:left;">
									产品列表
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th orderBy="name">套餐名称</th>
                        <th orderBy="type">类型</th>
                        <th orderBy="price">价格</th>
                        <th orderBy="enabled">状态</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
            </div>
</@frame.html>