<#import "../template/template.ftl" as frame> <#global menu="待办事项">
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
            "aaSorting": [[5, "desc"]],
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
             { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                    var operations='';
                        operations +='&nbsp;&nbsp;<a class="table-link operation" target="_blank" href="${rc.contextPath}/payContract/toRestPayContract/' + data +'" >支付</a>';
                 operations +='&nbsp;&nbsp;<a class="table-link operation" href="${rc.contextPath}/payContract/toEditPayContract/' + data +'" >关闭</a>';
                        
                         return operations;
                    }},
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
               
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback
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
    function fnDrawCallback(){
	    var record_count = (this.fnSettings().fnRecordsTotal() );
	    if(record_count>0){
	      $("#notPayContracts").html("&nbsp;"+record_count+"&nbsp;"); 
	      }
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
 <div class="tabs"> 
    <@security.authorize ifAnyGranted="ShibaFinancialManager">
    <a href="${rc.contextPath}/order/planContract" class="">
    待收款确认合同 <span id="planContract"
      style="background-color: #ff9966; color: #fff; font-size: 14px; border-radius: 4px;"></span>
    </a>
    </@security.authorize>
    <a href="${rc.contextPath}/order/myTask/1" class="">
    待办事项 <span id="orderTaskCount"
      style="background-color: #ff9966; color: #fff; font-size: 14px; border-radius: 4px;"></span>
    </a>
    <@security.authorize ifAnyGranted="ShibaFinancialManager">
    <a href="${rc.contextPath}/order/planOrders" class="">
    待收款分期订单 <span id="planOrders"
      style="background-color: #ff9966; color: #fff; font-size: 14px; border-radius: 4px;"></span>
    </a>
    </@security.authorize>
    <@security.authorize ifAnyGranted="sales,ShibaOrderManager,advertiser">
    
     <@security.authorize ifAnyGranted="ShibaOrderManager,advertiser">
 	   <a href="${rc.contextPath}/order/payPlanOrders" class="">
    待支付分期订单 <span id="payPlanOrders"
      style="background-color: #ff9966; color: #fff; font-size: 14px; border-radius: 4px;"></span>
    </a>
     </@security.authorize>
      <@security.authorize ifAnyGranted="sales">
    <a href="${rc.contextPath}/payContract/notPayContract" class="active">
    待支付合同 <span id="notPayContracts"
      style="background-color: #ff9966; color: #fff; font-size: 14px; border-radius: 4px;"></span>
    </a>
     </@security.authorize>
    </@security.authorize>
  </div>
	
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr class="tableTr">
				<th>管理</th>
				<th orderBy="contractCode">合同编号</th>
				<th>所关联订单</th>
				<th>合同价格(元)</th>
				<th>已确认支付(元)</th>
				<th orderBy="created">创建时间</th>
				<th orderBy="userId">创建人</th>
				
			</tr>
		</thead>

	</table>
</div>



<script type="text/javascript">
    $(document).ready(function() {
        queryTaskCount('orderTaskCount,notPayContracts,planOrders,planContract,payPlanOrders');
    } );
</script> 



</@frame.html>


