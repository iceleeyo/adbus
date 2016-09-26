<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "../template/template.ftl" as frame> <#global menu="创建合同">
<@frame.html title="创建合同 " js=["../js/jquery-ui/jquery-ui.js","../js/datepicker.js","../js/jquery.datepicker.region.cn.js",
"../index_js/sift_common.js","../js/jquery-ui/jquery-ui.auto.complete.js","../js/jquery-dateFormat.js"]
css=["../js/jquery-ui/jquery-ui.css","../css/autocomplete.css","../js/jquery-ui/jquery-ui.auto.complete.css"]>


<script type="text/javascript">
function go_back(){
	history.go(-1);
}
    $(document).ready(function() {
    var seriaNum='${seriaNum}';
       initPayPlanTable('${rc.contextPath}',0,'<@security.authorize
			ifAnyGranted="sales">edit_del</@security.authorize>','contract',seriaNum);
        
        
    });
</script>

<script type="text/javascript">
function split( val ) {
      return val.split( /,\s*/ );
    }
	$(document).ready(function() {
			 $( "#customerName" ).autocomplete({
		        	minLength: 0,
		  			source: "${rc.contextPath}/user/queryMyCustomers",
		  			change: function( event, ui ) { 
		  				$("#orderid").val("");
		  				$("#inputs").html("");
		  			 },
		  			 select: function(event,ui) {
		  			 	$('#customerName').val(ui.item.value);  
		  			 	$('#customerId').val(ui.item.dbId);  
   $("#orderid").autocomplete({
        minLength: 0,
        source : "${rc.contextPath}/payContract/OrderIdComplete?customerName="+$('#customerName').val(),
        focus: function() {
          return false;
        },
        select: function( event, ui ) {
          var terms = split( this.value );
          terms.pop();
          terms.push( ui.item.value );
          terms.push( "" );
          this.value = terms.join( ", " );
          showOrderDetail($("#orderid").val());
          
           return false;
        },
        change: function( event, ui ) { 
		  				showOrderDetail($("#orderid").val());
		  			 },
      }).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
		  			 	
		  			 }
				}).focus(function () {
       				 $(this).autocomplete("search");
   	 			});
					});

function showOrderDetail(orderIds){
$.ajax({
		url :"${rc.contextPath}/payContract/showOrderDetail?orderIds="+orderIds,
		data:{},
		type : "POST",
		success : function(data) {
		$("#inputs").html("");
		 var op="";
		 var price=0;
			$.each(data,function(i,item){
			 op+="<DIV class=\"p20bs color-white-bg border-ec\">"
						   +"<DIV class=\"summary uplan-summary-div\">"
		              +"<UL class=\"uplan-detail-ul\">"
			+"<li class=\"s-left f-iconli\"><span class=\"s-left tt\"><i class=\"s-left ff-icon\"></i>订单信息</span></li>"
			+"<li style=\"width: 200px;\"><SPAN>订单号：</SPAN><a target='_blank' href='${rc.contextPath}/order/orderDetail/"+item.order.id+"'>  "+item.longOrderId+"</a></li>"
			+"<li style=\"width: 200px;\"><SPAN>套餐名称：</SPAN>"
			+"<a class='layer-tips' tip='点击可查看套餐详细内容!' onclick=\"showProductlayer('${rc.contextPath}',"+item.order.product.id+");\">"
			+item.order.product.name+"</a></li>"
			+"<li style=\"width: 200px;\"><SPAN>订单价格：</SPAN>"+item.order.price+"</li>"
			+"<li style=\"width: 200px;\"><SPAN>媒体类型：</SPAN>"+item.order.product.type+"</li>"
			+"<li style=\"width: 200px;\"><SPAN>下单用户：</SPAN>"+item.order.creator+"</li>"
			+"</UL></DIV></DIV><br>"
			price+=item.order.price;
				});
				$("#inputs").html(op);
				$("#price").val(price);
				}
		});
}

	function sub(){
	if (!$("#contractForm").validationEngine('validateBeforeSubmit'))
			return;
		$('#contractForm').ajaxForm(function(data) {
				if(data.right){
					layer.msg("保存成功");
				}else {
					layer.msg("操作失败");
				}
				window.setTimeout(function(){
				window.location.href="${rc.contextPath}/payContract/list",
				   	clearTimeout(uptime);
							},2000) 
		}).submit();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" id="contractForm"
		class="ui-form" method="post" action="${rc.contextPath}/payContract/savePayContract"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>创建合同 </span> 
		</div>
		
		<input type="hidden" name="seriaNum" value="${seriaNum}"/>
		<div class="withdrawInputs">
			<div class="inputs" >
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同编号:</label>
					<input
						class="ui-input  validate[required,minSize[2],maxSize[100]]"
						type="text" name="contractCode" id="contractCode" value="${contractCode}" readonly="readonly"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder=""/>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>选择客户:</label>
					<input id="customerName"  value="" style="height:40px;width:250px">
					<input id="customerId" name="customerId" value="" type="hidden">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>选择订单:</label>
					<input id="orderid" value="" name="orderid" style="height:50px;width:650px"/>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同价格(￥):</label>
					<input id="price" value="" name="price" style="height:30px;width:200px;margin-top:5px;"/>
				</div>
			

			</div>
			
		</div>
		<div id="inputs">
		
		</div>
		
		<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">分期设置</A>
			<div class="withdraw-title">
			 <a class="block-btn"
						style="margin-top: -30px;" href="javascript:void(0);"
						onclick="addPayPlan('${rc.contextPath}',0,${seriaNum},1)">添加分期</a>	</div>
		</H3>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0;padding:0;">
				
	<table id="payPlanTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				                <th>期数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>付款方式</th>
								<th>状态</th>
								<th>付款人</th>
								<th>分期设置人</th>
								<th>处理人</th>
								<th>最后操作时间</th>
								<th>备注</th>
								<@security.authorize ifAnyGranted="ShibaFinancialManager"> 
								<th>操作</th></@security.authorize>
								
							<@security.authorize ifNotGranted="ShibaFinancialManager"> 
								<th></th>
								</@security.authorize>
			</tr>
		</thead>

	</table>
			</TD>
				</TR>
		</TABLE>
	</div>
		
		    <div class="widthdrawBtBox">
				<input type="button" id="subWithdraw" class="block-btn"
					onclick="sub();" value="提交">
			</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>...</li>
			</ol>
		</div>
	
</div>
</@frame.html>
