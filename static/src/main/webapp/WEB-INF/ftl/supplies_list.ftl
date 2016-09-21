<#import "template/template.ftl" as frame> <#global menu="物料管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="物料管理" js=["js/jquery-dateFormat.js"]>

<style type="text/css">
.operation
{
	color: #31B533;
    font-weight: 800;
}
</style>

<!-- <script>
        function pages(pageNum) {
            var by = ($("#by").val());
            var name = ($("#name").val());
            var g2 = ($("#textpage").val());
            if (g2 == undefined) {
                g2 = 1;
            }
            if (!isNaN($("#textpage").val())) {
            } else {
                jDialog.Alert("请输入数字");
                return;
            }
            if (parseInt($("#textpage").val()) <= 0) {
                jDialog.Alert("请输入正整数");
                return;
            }
            if ($("#textpage").val() > pageNum) {
                jDialog.Alert("输入的页数超过最大页数");
                return;
            }
            window.location.href = "${rc.contextPath}/supplies/list/" + g2 + "?name="+ name;
        }

        function page(num) {
            var name = $("#name").val();
            var by = ($("#by").val());
            window.location.href = "${rc.contextPath}/supplies/list/" + num + "?name=" + name;
        }

        function sub(){
            var name = $("#name").val();
            window.location.href= "${rc.contextPath}/supplies/list/1"+"?name="+name
        }
    </script> -->
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "aaSorting": [[5, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [6] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/supplies/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[industry]" : $('#industry').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "id", "defaultContent": ""},
            	{ "data": "name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#name').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                            return data;//add by panxh 增加返回值 
                        }

                        if(data.length>15){
                    		return "<span>"+data.substring(0,15)+ ".. </span>";
                    	}else{
                    		return "<span>"+data+" </span>";
                    	}
                } },
                { "data": "suppliesType", "defaultContent": "",
                "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '硬广广告';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'info')
                            return 'INFO字幕';
                        if (data == 'team')
                            return '二类节目';
                        return '';
                } },
                { "data": "industry.name", "defaultContent": ""},
                { "data": "userId", "defaultContent": ""},
                { "data": "created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                 { "data": "operFristcomment", "defaultContent": ""},
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        var operations=  row.industry.name=="垫片"?'<a class="table-link operation" href="javascript:void(0)" onclick="showBlackAdlayer(\'${rc.contextPath}/supplies/blackdetail/\',' + data + ');">查看详情</a>&nbsp;&nbsp;':'<a class="table-link operation" href="javascript:void(0)" onclick="supDetail('+data+')">查看物料</a>&nbsp;&nbsp;';
                        operations +='<a class="table-link operation" href="javascript:delSupp('+data+');" >删除</a>  ';
                        if(row.industry.name=="垫片"){
                            	if(row.stats=='online'){
                      		operations +=	'<a class="table-action operation" href="javascript:void(0);" url="${rc.contextPath}/supplies/changeStats/' + data + '/offline">取消上架</a> &nbsp;'
                       	}else {
                       		operations +=	'<a class="table-action operation" href="javascript:void(0);" url="${rc.contextPath}/supplies/changeStats/' + data + '/online">上架</a> &nbsp;'
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
    }
    
function supDetail(data){
	layer.open({
    		type: 1,
    		title: "物料及资质",
    		skin: 'layui-layer-rim', 
    		area: ['1000px', '529px'], 
    		content:''
			   +' '
			   +'<iframe  style="width:100%;height:100%" frameborder="no" src="${rc.contextPath}/supplies/suppliesDetail/'+data+'"/>'
			});
}

    
function delSupp(Suppid){
	var bln=window.confirm("确定删除该物料吗？");
    if(bln){
	 $.ajax({
			url:"${rc.contextPath}/supplies/delSupp/"+Suppid,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left == true) {
					jDialog.Alert(data.right);
				   var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/supplies/list";
			   	clearTimeout(uptime);
						},2000)
				} else {
					jDialog.Alert(data.right);
				}
			}
      });  
   }
	   
	}
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>物料名称：</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' + 
                 		'    <span>行业</span>' +
                        '&nbsp;&nbsp;<select  class="ui-input ui-input-mini" id="industry" name="industry" > '+
					    '<option value="0" selected="selected">所有行业</option>'  +
					     <#list industries as industry>
					    '<option value="${industry.id}">${industry.name}</option>'+
					      </#list>
					    ' </select>'+
                        '</div>'     
                        
        );

        $('#name, #industry').change(function() {
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
			<h2>物料列表</h2>
		</caption>
	</div>
	<div class="div">
		<hr />
	</div>
	-->
	<div class="withdraw-title">
		<span>物料列表</span> <a class="block-btn"
			href="${rc.contextPath}/supplies/new" style="margin-left: 10px">添加物料</a>
		<@security.authorize ifAnyGranted="ShibaOrderManager"> <a
			class="block-btn" href="${rc.contextPath}/supplies/newBlackAd">添加底板</a>
		</@security.authorize>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th orderBy="id">编号</th>
				<th orderBy="name">物料名称</th>
				<th orderBy="suppliesType">类型</th>
				<th orderBy="industry.id">行业</th>
				<th orderBy="created">属主</th>
				<th orderBy="created">创建时间</th>
				<th orderBy="created">备注</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>

	<div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>温馨提示：未绑定订单的物料才能够删除。
		</div>
	</div>
</div>
</@frame.html>
