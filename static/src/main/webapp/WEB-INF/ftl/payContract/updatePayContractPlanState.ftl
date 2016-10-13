<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "../template/template.ftl" as frame> <#global menu="分期合同">
<@frame.html title="分期合同办理 " js=["../js/jquery-ui/jquery-ui.js","../js/datepicker.js","../js/jquery.datepicker.region.cn.js",
"../index_js/sift_common.js","../js/jquery-ui/jquery-ui.auto.complete.js","../js/jquery-dateFormat.js"]
css=["../js/jquery-ui/jquery-ui.css","../css/autocomplete.css","../js/jquery-ui/jquery-ui.auto.complete.css"]>


<script type="text/javascript">
function go_back(){
	history.go(-1);
}
    $(document).ready(function() {
        
        var seriaNum='${jpaPayContract.seriaNum!''}';
        
        initPlan('${rc.contextPath}',${jpaPayContract.id!''},'<@security.authorize ifAnyGranted="sales">doNoting</@security.authorize>','contract',seriaNum);
        
       initPayPlanTable('${rc.contextPath}',${jpaPayContract.id!''},'<@security.authorize
			ifAnyGranted="sales">edit_del</@security.authorize>','contract',seriaNum);
        
    });
</script>

<script type="text/javascript">
function split( val ) {
      return val.split( /,\s*/ );
    }
	$(document).ready(function() {
	$("#orderid").val(${jpaPayContract.orderJson});
	showOrderDetail($("#orderid").val())
	var customerName=${jpaPayContract.customerJson}.company;
	var customerId=${jpaPayContract.customerJson}.username;
	$("#customerName").val(customerName);
	$("#customerId").val(customerId);
    
		  			 	
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
			 op+="<DIV class=\"newPay-order\">"
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
	
	
		<#if planView.plan.payState == "check">
		function pay(pid) {
	       var rad=document.getElementsByName("rad");
           for(var i=0;i<rad.length;i++)
         {
           if(rad[i].checked)
            rad = rad[i].value;
         }
         console.log(rad);
		$.ajax({
			url : "${rc.contextPath}/order/updatePlan",
			type : "POST",
			data : {
				"rad" :rad,
				"remarks" :$("#remarks").val(),
				"id" :pid
			 
			},
			success : function(data) {
				jDialog.Alert(data.right);
				var uptime = window.setTimeout(function(){
					var a = document.createElement('a');
	    	        a.href='${rc.contextPath}/payContract/updatePayContractPlanState/'+pid;
	            	document.body.appendChild(a);
	             	a.click();
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}

	</#if>

</script>
	
	
	
	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" id="contractForm"
		class="ui-form" method="post" action="${rc.contextPath}/payContract/savePayContract"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>收款确认 </span> 
		</div>
		<input type="hidden" value="${jpaPayContract.id}" name="id"/>
		<div class="withdrawInputs">
			<div class="inputs" >
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同编号:</label>
					<input
						class="ui-input  validate[required,minSize[2],maxSize[100]]"
						type="text" name="contractCode" readonly="readonly" id="contractCode" value="${jpaPayContract.contractCode}" readonly="readonly"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder=""/>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>代理客户:</label>
					<input id="customerName"  value="" style="height:40px;width:250px" readonly="readonly">
					<input id="customerId" name="customerId" value="" type="hidden">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>订单编号:</label>
					<input id="orderid"  readonly="readonly" value="" name="orderid" style="height:50px;width:650px"/>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同价格(￥):</label>
					<input id="price" readonly="readonly" value="${jpaPayContract.price}" name="price" style="height:40px;width:200px;margin-top:5px;"/>
				</div>
			

			</div>
			
		</div>
		<div id="inputs">
		
		</div>
		
		<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">分期计划</A>
		  <div class="withdraw-title">
	    	</div>
		</H3>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0;padding:0;">
				
	  <table id="planTableOfPlan" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				                <th>期数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>分期设置人</th>
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
		
		<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">收款详情</A>
	
		</H3>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0;padding:0;">
				
	<table id="payPlanTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				                <th>次数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>付款方式</th>
								<th>状态</th>
								<th>付款人</th>
								<th>确认人</th>
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
			
			
			
			
<div id="userFristPay" class="userFristPay" >
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="#">用户分期付款确认</A>
			</p>
		</H3>
		<BR>
		
	 
			<TABLE class="ui-table ui-table-gray">
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">分期期数</TD>
				<TD>
					 #{(planView.plan.periodNum)!''} 
				</TD>
			</TR>
				<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付状态</TD>
				<TD>
					<font color="red"> 
					<#switch planView.plan.payState>
					  <#case "payed">
					  		已支付
					     <#break>
					  <#case "init">
					  	待支付
					     <#break>
					  <#case "fail">
						 用户支付失败, 未收到款项
					     <#break>
					  <#case "check">
					 	 待收款确认
					     <#break>
					  <#default>
					</#switch>
					
					 </font>
				</TD>
			</TR>
			
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">本期支付金额</TD>
				<TD>
					<font color="orange">#{(planView.plan.price)!'';m2M2} </font>
				</TD>
			</TR>
			
			<#if planView.payName?exists >
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付方式</TD>
				<TD>
					 ${planView.payName!''} 
				</TD>
			</TR>
			</#if>
			<#if planView.plan.payState == "check">
			<TR style="height: 45px;">
					<TD style="text-align: right">支付状态</TD>
					<TD><input name="rad" type="radio" value="Y"
						checked="checked" style="padding: 5px 15px;" />支付正常 <input
						name="rad" type="radio" value="N" style="padding: 5px 15px;" />支付异常</TD>
				</TR>
				<TR>
					<TD style="text-align: right">审核意见</TD>
					<TD colspan=3><textarea name="remarks"
							id="remarks" class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>
				</#if>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<#if planView.plan.payState == "check">
			<button type="button" onclick="pay('${planId!''}')" class="block-btn">确认</button>
			</#if>
		</div>
		 

	</div>
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
