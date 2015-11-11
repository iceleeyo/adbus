<#import "template/template.ftl" as frame> <#global menu="产品列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="产品套餐列表"
js=["js/jquery-ui/jquery-ui.js","js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"]
>

<script type="text/javascript">
    var table;

    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [5] },
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
                    if(data.length>15){
                       return '<a class="layer-tips" tip="点击可查看套餐详细内容!" onclick="showProductlayer(\'${rc.contextPath}\','+row.id+');">'+data.substring(0,15)+'..</a>';
                    }else{
                    return '<a class="layer-tips" tip="点击可查看套餐详细内容!" onclick="showProductlayer(\'${rc.contextPath}\','+row.id+');">'+data.substring(0,15)+'</a>';
                    }
                } },
                { "data": "type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '全屏视频';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'body')
                            return '车身';
                        if (data == 'info')
                            return 'INFO字幕';
                        if (data == 'team')
                            return '团类广告';
                        return '';
                    } },
                { "data": "price", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                { "data": "exclusiveUser", "defaultContent": "", "render": function(data, type, row) {
                    if (data)
                        return '<span class="invalid">' + data + '</span>';
                    else
                        return '';
                } },
                { "data": "enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } },
                
                <@security.authorize ifAnyGranted="ShibaOrderManager">   
                 { "data": "frontShow", "defaultContent": ""},
                 { "data": "runningCount", "defaultContent": "", "render": function(data, type, row, meta) {
                   return '<a class="table-link" href="${rc.contextPath}/order/product/' + (row.id) +'/1">'+data+'</a> &nbsp;'; 
                } },
                 { "data": "finishedCount", "defaultContent": "", "render": function(data, type, row, meta) {
                    return '<a class="table-link" href="${rc.contextPath}/order/over/' +  (row.id) +'">'+data+'</a> &nbsp;'; 
                } },
                  </@security.authorize>
                
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        var operations = '';
                   		 <@security.authorize ifAnyGranted="ShibaOrderManager">  
                     	operations+= (row.enabled ? '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/' + data + '/disable">禁用</a> &nbsp;'
                                :'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/' + data + '/enable">启用</a> &nbsp;')
                        operations +='<a class="table-link" href="${rc.contextPath}/product/' + data +'">编辑</a> &nbsp;';
                        
                       	if(row.frontShow=='Y'){
                      		operations +=	'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/frontshow/' + data + '/N">取消首页</a> &nbsp;'
                       	}else {
                       		operations +=	'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/frontshow/' + data + '/Y">置首页</a> &nbsp;'
                       	}
                        
                         //operations +='<a class="table-link" href="${rc.contextPath}/order/product/' + data +'/1">进行中订单</a> &nbsp;'; 
                         //operations +='<a class="table-link" href="${rc.contextPath}/order/over/' + data +'">已完成订单</a> &nbsp;';
                        </@security.authorize>
                        if(row.enabled){
                        	<@security.authorize ifAnyGranted="normaluser,advertiser">
                     	 	 operations+= '<a class="table-link" target="_blank" href="${rc.contextPath}/order/iwant/'+data+'">购买</a>';
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
        var r = confirm("确定执行该操作吗")
        if(r==true){
       		$.post($(this).attr("url"), function() {
                table.fnDraw(true);
        	});
        }
        });
    }

    $(document).ready(function() {
        initTable();
    } );
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
	<#--
	<div class="div" style="margin-top: 25px">
		<caption>
			<h2>产品套餐列表</h2>
		</caption>
	</div>
	<div class="div">
		<hr />
	</div>
	-->
	<div class="tabs">
		<a id="tab1" href="${rc.contextPath}/product/list" class="active">定价产品</a>
		<a id="tab2" href="${rc.contextPath}/product/auction">竞价产品</a>
		<div class="taba">
			<@security.authorize ifAnyGranted="ShibaOrderManager"> <a
				class="block-btn" href="${rc.contextPath}/product/new"
				style="width: 90px; float: right; color: #fff">新增产品</a>
			</@security.authorize>
		</div>
	</div>

	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th orderBy="name">套餐名称</th>
				<th orderBy="type">类型</th>
				<th orderBy="price">价格(元)</th>
				<th orderBy="exclusive">定向</th>
				<th orderBy="enabled">状态</th> <@security.authorize
				ifAnyGranted="ShibaOrderManager">
				<th>首页</th>
				<th>进行中</th>
				<th>已结束</th> </@security.authorize>
				<th>管理</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
