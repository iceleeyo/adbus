<#import "template/template.ftl" as frame> <#global menu="屏幕广告合同">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="屏幕广告合同"
js=["js/jquery-dateFormat.js","js/layer-v1.9.3/layer/layer.js"]>

<style type="text/css">
.operation
{
	color: #31B533;
    font-weight: 800;
}
</style>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [7] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/contract/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractCode]" : $('#contractCode').val(),
                        "filter[contractName]" : $('#contractName').val(),
                        "filter[contractType]" : $('#contractType').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "userId","defaultContent": ""},
            	{ "data": "contractCode", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#contractCode').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                    return data;
                } },
                 { "data": "parentid","defaultContent": "",
                    "render": function(data, type, row, meta) {
                      return data==0?'主合同':'子合同';
                    }},
                { "data": "contractName", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#contractName').val();
                       	if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                        return data;
                }},
                { "data": "type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'video')
                            return '硬广广告';
                        if (data == 'image')
                            return 'INFO图片';
                        if (data == 'info')
                            return 'INFO字幕';
                        if (data == 'team')
                            return '团类广告';
                        if (data == 'mixture')
                            return '混合类型';
                        return '';
                    } },
                { "data": "amounts","render": function(data, type, row, meta) {
                         return data;
                    }},
                { "data": "signDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "endDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                } },
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                    var operations='<a class="table-link operation" onclick="contractdetail(\'${rc.contextPath}\','+data+');" href="javascript:void(0)">详情</a>';
                     <@security.authorize ifAnyGranted="ShibaOrderManager">  
                        operations +='&nbsp;&nbsp;<a class="table-link operation" href="${rc.contextPath}/contract/contract_edit/' + data +'" >编辑</a>';
                        operations +='&nbsp;&nbsp;<a class="table-link operation" href="javascript:delContract('+data+');" >删除</a>  &nbsp;';
                        </@security.authorize>
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
function delContract(conid){
	var bln=window.confirm("确定删除该合同吗？");
    if(bln){
	 $.ajax({
			url:"${rc.contextPath}/contract/delContract/"+conid,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left == true) {
					jDialog.Alert(data.right);
				   var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/contract/list"
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
                        '    <span>合同号：</span>' +
                        '        <input id="contractCode" value="">' +
                        '    <span>合同名称：</span>' +
                        '        <input id="contractName" value="">' +
                        '    <span>合同类型：</span>' +
                        '        <select id="contractType" > ' +
                        '<option value="defaultAll" selected="selected">所有</option><option value="0">主合同</option><option value="1">子合同</option>'+
                        '</div>'
        );

        $('#contractCode, #contractName,#contractType').change(function() {
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
<!--实现层移动-->
<script type="text/javascript">
$('#test').on('click', function(){
    layer.open({
        type: 1,
        area: ['600px', '460px'],
        shadeClose: true, //点击遮罩关闭
        content: '\<\div style="padding:20px;">自定义内容\<\/div>'
    });
});
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<#--
	<div class="div" style="margin-top: 25px">
		<caption>
			<h2>合同列表</h2>
		</caption>
	</div>
	<div class="div">
		<hr />
	</div>
	-->
	<!-- <div class="withdraw-title" style="padding-top: 0px;text-align:left;">
									合同列表
									</div> -->
	<div class="withdraw-title">
		<span>屏幕广告合同列表</span> <@security.authorize
		ifAnyGranted="ShibaOrderManager,bodyContractManager"> <a
			class="block-btn" href="${rc.contextPath}/contract/contractEnter">添加合同</a>
		</@security.authorize>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr class="tableTr">
				<th orderBy="userId">广告主</th>
				<th orderBy="contractCode">合同编号</th>
				<th >合同类型</th>
				<th orderBy="contractName">名称</th>
				<th >媒体类型</th>
				<th orderBy="amounts">金额</th>
				<th orderBy="signDate">签订日期</th>
				<th orderBy="startDate">上刊日期</th>
				<th orderBy="endDate">下刊日期</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>


