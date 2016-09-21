<#import "../template/template.ftl" as frame> <#global menu="待支付合同">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="待支付合同"
js=["../js/jquery-dateFormat.js","../js/layer-v1.9.3/layer-site.js"]>

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
                { "orderable": false, "targets": [0,1,2,3,4] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/payContract/ajax-notPayContract",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractCode]" : $('#contractCode').val(),
                        "filter[stateKey]" : $('#stateKey').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "contractCode","defaultContent": "", "render": function(data, type, row, meta) {
                
                    return "<a class='operation' onclick='eleContract(\"${rc.contextPath}\",0,0,"+row.id+")' >"+data+"</a>";
                
                }},
                { "data": "orderJson","defaultContent": "", "render": function(data) {
                  var obj = jQuery.parseJSON(data);
                    return  obj;
                } },
                { "data": "price","defaultContent": ""},
                { "data": "payPrice","defaultContent": ""},
                { "data": "created", "defaultContent": "", "render": function(data) {
                    return  $.format.date(data, "yyyy-MM-dd");
                } },
                { "data": "userId","defaultContent": ""},
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                    var operations='';
                        operations +='&nbsp;&nbsp;<a class="table-link operation" target="_blank" href="${rc.contextPath}/payContract/toRestPayContract/' + data +'" >详情</a>';
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
			url:"${rc.contextPath}/payContract/delPayContract/"+conid,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left) {
					layer.msg(data.right);
				   var uptime = window.setTimeout(function(){
				 table.dataTable()._fnAjaxUpdate();
			   	clearTimeout(uptime);
						},2000)
				} else {
					layer.msg(data.right);
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
                         '&nbsp;&nbsp;<select class="ui-input ui-input-mini" name="stateKey" id="stateKey">' +
	                    '<option value="defaultAll" >所有状态</option>' +
    	              	'<option value="notcomplete" selected="selected">待支付合同</option>' +
        	          	'<option value="complete">支付完成合同</option>' +
         				'</select>' +
                        '</div>'
        );

        $('#contractCode,#stateKey').change(function() {
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
	
	<div class="withdraw-title">
		<span>合同列表</span>
	    <a class="block-btn" href="${rc.contextPath}/payContract/newPayContract">创建合同</a>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr class="tableTr">
				<th orderBy="contractCode">合同编号</th>
				<th>所关联订单</th>
				<th>合同价格(元)</th>
				<th>已支付金额(元)</th>
				<th orderBy="endDate">创建时间</th>
				<th orderBy="userId">创建人</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>


