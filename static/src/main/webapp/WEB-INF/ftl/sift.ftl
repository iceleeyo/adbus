<#import "template/template_intro.ftl" as frame> <#global menu="">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="商品sift" left=false nav=false
js=["js/sift.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js",
"js/jquery.datepicker.region.cn.js"]
css=["css/sift.css","css/account.css","js/jquery-ui/jquery-ui.css"]>
<head>

</head>
<body>


	<div class="pg-container">
		<div class="pg-container-main">
			<div class="container-12">
				<ul class="breadcrumb ml10 m11 s-clear">
					<li class="s-left fsize-16 bread-homep"><a class="gray-text"
						href="/">首页</a></li>
					<li class="s-left breadcrumb-right"></li>
					<li class="s-left bread-child"><a class="gray-text" href="#">商品搜索</a>
					</li>
				</ul>
			</div>
			<div class="container-12 mt10 s-clear">
				<div class="sift-box">
					<div class="sift-item s-clear">
						<span>产品名称：</span>
						<div class="sift-search">
							<input class="" id="name" type="text" placeholder="搜索产品"
								style="height: 39px;"> <a class="btn-search" href="#"></a>
						</div>
					</div>
					<div class="sift-item s-clear">
						<span>是否竞价：</span>
						<div class="sift-list" qt="p">
							<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
								class="item" href="#" qc="2">竞价商品<i>×</i></a> <a class="item"
								href="#" qc="3">一口价<i>×</i></a>
						</div>
					</div>
					<#if city.mediaType=="body">
					<div class="sift-item s-clear">
						<span>线路级别：</span>
						<div class="sift-list" qt="lev">
							<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
								class="item" href="#" qc="S">特级<i>×</i></a> <a class="item"
								href="#" qc="APP">A++<i>×</i></a> <a class="item" href="#"
								qc="AP">A+<i>×</i></a> <a class="item" href="#" qc="A">A<i>×</i></a>
							<a class="item" href="#" qc="LATLONG">经纬线<i>×</i></a>
						</div>
					</div>
					<#else>
					<div class="sift-item s-clear">
						<span>产品类型：</span>
						<div class="sift-list" qt="t">
							<a class="item active" href="#" sort="-1" qc="all">所有</a> 
							<a class="item" href="#" qc="video">硬广广告<i>×</i></a>
							<a class="item" href="#" qc="image">INFO图片<i>×</i></a> 
							<a class="item" href="#" qc="info">INFO文字<i>×</i></a>
						</div>
					</div>
					<div class="sift-item s-clear">
						<span>日曝光次数：</span>
						<div class="sift-list" qt="s">
							<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
								class="item" href="#" qc="2">0-7（不含）<i>×</i></a> <a class="item"
								href="#" qc="3">7-11（含）<i>×</i></a> <a class="item" href="#"
								qc="4">11以上<i>×</i></a>
						</div>
					</div>
					</#if>
					<div class="sift-item s-clear">
						<span>展示期限：</span>
						<div class="sift-list" qt="d">
							<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
								class="item" href="#" qc="2">1（天）<i>×</i></a> <a class="item"
								href="#" qc="3">2-6（含）<i>×</i></a> <a class="item" href="#"
								qc="4">7（天）<i>×</i></a> <a class="item" href="#" qc="5">7天以上<i>×</i></a>
						</div>
					</div>


				</div>
			</div>
		</div>


		<div class="withdraw-wrap color-white-bg fn-clear"
			style="margin-top: 30px;">
			<div class="withdraw-title fn-clear">产品列表</div>
			<table id="table" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th orderBy="name">套餐名称</th>
						<th orderBy="type">类型</th>
						<th orderBy="type">类型</th>
						<th orderBy="price">价格(元)</th> <@security.authorize
						ifAnyGranted="ShibaOrderManager">
						<th orderBy="exclusive">定向</th> </@security.authorize>
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


	</div>

	</div>

	<script type="text/javascript">
      </script>
	<input type="hidden" id="sh" value="" />

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
                url: "${rc.contextPath}/product/sift_data",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                         "filter[sh]" : $('#sh').val(),
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    
                    return '<a class="layer-tips" tip="点击可查看套餐详细内容!" onclick="showProductlayer(\'${rc.contextPath}\','+row.id+');"  >'+row.name+'</a>';
                    //    return '<a class="table-link" href="${rc.contextPath}/product/d/'+row.id+'">'+row.name+'</a>';
                } },
                { "data": "type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '视频';
                        if (data == 'image')
                            return '图片';
                        if (data == 'body')
                            return '车身';
                        if (data == 'info')
                            return 'Info';
                        return '';
                    } },
                    
                       { "data": "iscompare", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == '1')
                            return '竞价商品';
                        if (data == '0')
                            return '一口价';
                        return '';
                    } },
                { "data": "price", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                 <@security.authorize ifAnyGranted="ShibaOrderManager"> 
                { "data": "exclusiveUser", "defaultContent": "", "render": function(data, type, row) {
                    if (data)
                        return '<span class="invalid">' + data + '</span>';
                    else
                        return '';
                } }, 
                 </@security.authorize>
                 
                { "data": "enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">销售中</span>';
                        default :
                            return '<span class="invalid">已下架</span>';
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
                        if(row.enabled==true){
                        	if(row.iscompare==1){
                     	 		 operations+= '<a class="table-link" href="${rc.contextPath}/product/c/'+data+'?pid='+data+'">竞价</a>';
                     	 	}else {
                     	 		 operations+= '<a class="table-link" href="${rc.contextPath}/m/public_detail/'+data+'">购买</a>';
                     	 	}
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
        return table;
       
    }

    function initComplete() {
        $("div#toolbar").html(
        );

        $('#name, #sh').change(function() {
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
        initSwift(table)
    } );
    
   
</script>






</body>
</@frame.html>
