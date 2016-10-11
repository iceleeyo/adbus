<#import "../macro/materialPreview.ftl" as preview> 
<#macro orderDetail orderview quafiles suppliesView="" title="订单详情" suppliesLink=true
viewScheduleLink=true> 
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#assign prod=orderview.order.product>

<script type="text/javascript">
function supDetail(data){
	layer.open({
    		type: 1,
    		title: "物料及资质",
    		skin: 'layui-layer-rim', 
    		area : ['1000px', '529px'], 
    		content:''
			   +' '
			   +'<iframe  style="width:100%;height:100%" frameborder="no" src="${rc.contextPath}/supplies/suppliesDetail/'+data+'"/>'
			});
}
</script>
<DIV class="p20bs color-white-bg border-ec">
	<H3 class="text-xl title-box">
		<p style="text-align: left">
			<A class="black" href="#">${title}-${(orderview.longOrderId)!''}</A>
		</p>
	</H3>
	<DIV class="summary uplan-summary-div">
		<UL class="uplan-detail-ul">

			<li class="s-left f-iconli"><span class="s-left tt"><i
					class="s-left ff-icon"></i>订单信息</span></li>
			<li style="width: 1000px;"><SPAN>套餐名称：</SPAN>
			<SPAN class="con-title"><a class="layer-tips" tip="点击可查看套餐详细内容!"
					onclick="showProductlayer('${rc.contextPath}',${prod.id});">${prod.name!''}</a></SPAN></li>
			<#if !(cpdDetail?exists)>
			<br/>
			<li style="width: 200px;"><SPAN>套餐价格：</SPAN><SPAN class="con"
				style="color: rgb(245, 135, 8);"> 
				 <#assign priceTag=0 />
					 <@security.authorize ifAnyGranted="sales,salesManager,ShibaOrderManager,advertiser,ShibaFinancialManager">
								  <@security.authorize ifAnyGranted="sales,salesManager,advertiser,ShibaFinancialManager"> 
								 	<#assign priceTag=1 />
								  </@security.authorize>
								  <@security.authorize ifAnyGranted="ShibaOrderManager"> 
								 	<#assign priceTag=2 />
								  </@security.authorize>
					 </@security.authorize>
				<#if priceTag == 0>
				  **
				<#elseif priceTag == 1>
				  #{(orderview.order.price)!'';m2M2}
				<#elseif priceTag == 2>
				   <a class="layer-tips" tip="点击修改订单价格!"
					onclick="setOrderPrice('${rc.contextPath}/order/setOrderPrice',${orderview.order.id});">
						<span id="prodPrice">#{(orderview.order.price)!'';m2M2}</span>
				</a>
				</#if>  
			</SPAN><SPAN>元</SPAN></li>
			<#if orderview.order.payType=='dividpay'>
			<li style="width: 200px;"><SPAN>已确认收款：</SPAN><SPAN class="con"
				style="color: rgb(245, 135, 8);"> 
				 <#assign priceTag=0 />
					 <@security.authorize ifAnyGranted="sales,salesManager,ShibaOrderManager,advertiser,ShibaFinancialManager">
								  <@security.authorize ifAnyGranted="sales,salesManager,advertiser,ShibaFinancialManager"> 
								 	<#assign priceTag=1 />
								  </@security.authorize>
								  <@security.authorize ifAnyGranted="ShibaOrderManager"> 
								 	<#assign priceTag=2 />
								  </@security.authorize>
					 </@security.authorize>
				<#if priceTag == 0>
				  **
				<#elseif priceTag == 1>
				  #{(orderview.order.payPrice)!'';m2M2}
				<#elseif priceTag == 2>
				   <span id="prodPrice">#{(orderview.order.payPrice)!'';m2M2}</span>
				</#if>  
			</SPAN><SPAN>元</SPAN></li>
			</#if>  
			 </#if>
			<#if cpdDetail?exists>
				<li style="width: 200px;"><SPAN>套餐底价：</SPAN>${cpdDetail.saleprice!''}</li>
				<li style="width: 200px;"><SPAN><b>成交价</b>：</SPAN><font color='#ff9966'>${cpdDetail.comparePrice!''}</font></li>
			</#if>
			<li style="width: 200px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN></li>

			<@security.authorize
			ifAnyGranted="salesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
			<li style="width: 200px;"><SPAN>下单用户：</SPAN><SPAN class="con">
					<a class="layer-tips" tip="点击查看下单用户信息!"
					onclick="showCustomerlayer('${rc.contextPath}', '${(orderview.order.id)!''}','orderUserDetail');">
						 ${substring((orderview.order.creator)!'', 0, 20)}</a>
				<#--	onclick="showOrderUserlayer('${rc.contextPath}', '${(orderview.order.creator)!''}');">
						${(orderview.order.creator)!''}</a>-->
			</SPAN></li>
			</@security.authorize> 
			
			<@security.authorize
			ifAnyGranted="sales,salesManagersales,salesManager,ShibaOrderManager,ShibaFinancialManager">
			<#if orderview.customerJson?exists>
			<li style="width: 260px;"><SPAN>客户信息：</SPAN><SPAN class="con" >
					<a class="layer-tips" tip="点击查看客户用户信息!"
					onclick="showCustomerlayer('${rc.contextPath}', '${(orderview.order.id)!''}','customerDetail');">
						 ${substring((orderview.customerJson.company)!'', 0, 8)}</a>
			</SPAN></li>
			</#if>
			</@security.authorize> 
			
			
			<@security.authorize ifAnyGranted="advertiser" 
			ifNotGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
			<li style="width: 200px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(orderview.order.creator)!''}</SPAN></li>
			</@security.authorize>
			
		 

			<li style="width: 300px;"><SPAN>订单状态：</SPAN> <SPAN class="con">
					<#if orderview.task_name?has_content> ${orderview.task_name!''}
					<#else>已完成 </#if></SPAN></li>
			<li style="width: 200px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting
					date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''}</SPAN></li>
			<li style="width: 200px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting
					date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''}</SPAN></li>
			
		<@security.authorize ifAnyGranted="ShibaFinancialManager,sales">
			<li style="width:400px;"><SPAN>合同编号：</SPAN><SPAN class="con">
			<#if orderview.order.jpaPayContract??>
			<a target="_blank" href="${rc.contextPath}/payContract/toEditPayContract/${orderview.order.jpaPayContract.id!0}">${(orderview.order.contractCode)!''}</a>
			<#else>
			<a target="_blank" href="${rc.contextPath}/payContract/toEditPayContract/${orderview.order.id!0}">${(orderview.order.contractCode)!''}</a>
			</#if>
			</SPAN></li>
		</@security.authorize>
			<#if (orderview.order.ordRemark!'')?length lt 38>
			<li style="width: 720px;"><SPAN> 备注信息：</SPAN><SPAN class="con">${orderview.order.ordRemark!''}</SPAN></li>
			<#else>
			<li style="width: 720px;"><SPAN>备注信息：</SPAN><SPAN class="con"><a
					class="layer-tips" tip="点击可查详细内容!"
					onclick="showRemark('${orderview.order.ordRemark!''}');">${substring(orderview.order.ordRemark,0,38)}</a></SPAN></li>
			</#if> <#if orderview.closed>
			<li style="width: 720px;"><SPAN> 关闭原因：</SPAN><SPAN class="con"><a
					class="layer-tips" tip="点击可查关闭内容!"
					onclick="showRemark('${orderview.order.closeRemark!''}');">${substring(orderview.order.closeRemark,0,38)}</a></SPAN></li>
			</#if>


			<#-- <li class="s-left f-iconli"><span class="s-left tt"><i class="s-left ff-icon"></i>支付及发票</span></li> 
			 <li style="width: 200px;"><SPAN>分期详情：</SPAN><SPAN class="con">
			 <#if orderview.order.jpaPayContract??>
			 	<a target="_blank" href="${rc.contextPath}/payContract/toEditPayContract/${orderview.order.jpaPayContract.id!0}"  >查看</a>
			 <#else>
			 <a href="javascript:void(0)" onclick="queryPayPlanDetail('${rc.contextPath}',${orderview.order.id!''},'order');" >查看</a>
			 </#if>
			 </SPAN></li>
			<#if orderview.payTypeString?has_content>
			<li style="width: 200px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></li>
			<#if orderview.payTypeString?has_content && orderview.payTypeString=="关联合同">
			<li style="width: 280px;"><SPAN>合同号：</SPAN><SPAN class="con">
					<@security.authorize ifAnyGranted="ShibaFinancialManager">
					<a class="layer-tips" tip="点击可查看合同详细内容!" onclick="contractdetail('${rc.contextPath}',${(orderview.order.contractId)!''});"
					href="javascript:void(0)">${(orderview.order.contractCode)!''}</a>
					</@security.authorize>
					<@security.authorize ifNotGranted="ShibaFinancialManager">
					${(orderview.order.contractCode)!''} 
					</@security.authorize>
			</SPAN></li>
			  <#elseif orderview.payTypeString?has_content && orderview.payTypeString=="线上支付">
			     <li style="width: 200px;"><SPAN>流水号：</SPAN><SPAN class="con">123912800234</SPAN></li>
			  </#if>
			  
			  
			  <@security.authorize ifAnyGranted="advertiser,ShibaFinancialManager">
			      <li style="width: 200px;"><SPAN>支付凭证：</SPAN><SPAN class="con">
			      <#if suppliesView.payvouchers??>
			      <a href="javascript:void(0)" onclick="queryPayvoucher('${rc.contextPath}',${orderview.order.id!''});" >查看</a>
			      <#else>
			               无
			      </#if>
			  </@security.authorize>
			      </SPAN></li>
			   </#if>
			   <#if contract?? && contract.parentid!=0>
			<li style="width: 200px;"><SPAN>结算方式：</SPAN><SPAN class="con">统一结算</SPAN></li>
			</#if>
			<@security.authorize ifAnyGranted="sales,ShibaFinancialManager">
				<li style="width: 200px;"><SPAN>是否开发票：</SPAN><SPAN class="con">
						<#if orderview.order.isInvoice==1 > <a class="layer-tips"
						tip="点击可查看发票详细内容!"
						onclick="invoicedetail('${rc.contextPath}',${orderview.order.id!''});"
						href="javascript:void(0)"> 是(查看详情)</a> <#else> 否 </#if>
				</SPAN></li>
			</@security.authorize> -->

			<li class="s-left f-iconli"><span class="s-left tt"><i
					class="s-left ff-icon"></i>物料及资质</span></li> <#if suppliesLink>
					<#if (orderview.order.supplies.id>1)>
			<li style="width: 200px;"><SPAN>物料资质：</SPAN><SPAN class="con"><a
					href="javascript:void(0)"
					onclick="supDetail(${(suppliesView.mainView.id)!''})">查看</a></SPAN></li>
					</#if>
			<li style="width: 500px;"><SPAN>审批合格号： </SPAN><SPAN class="con">
					<#if suppliesView.mainView.seqNumber?has_content >
					${(suppliesView.mainView.seqNumber)!''} <#else> -- </#if> </SPAN></li> 
			<#if quafiles?? > </#if> </#if>
			
		<@security.authorize ifNotGranted="advertiser">
		    <#if ischedule?? && ischedule=='Y'>
			<#else>
			<li class="s-left f-iconli"><span class="s-left tt"><i
					class="s-left ff-icon"></i>排期信息</span></li> 
					
			<#if orderview.task_name?exists && (orderview.task_name=='已排期待上播' ||
			orderview.task_name=='已上播' ||orderview.task_name=='已排期待上播')>
			<li style="width: 200px;"><SPAN>排期状态：
			
				<a target="_blank" style="color:#00A8E8;" href="${rc.contextPath}/schedule/${orderview.order.id!''}">
					查看排期表 </a></SPAN></li> 
		
					 <#elseif orderview.task_name=='已完成' >
					<li style="width: 200px;"><SPAN>排期状态：
							<a target="_blank" style="color:#00A8E8;"  href="${rc.contextPath}/schedule/${orderview.order.id!''}">
								查看排期表 </a> 
					 <#else>

							<li style="width: 200px;"><SPAN>排期状态：</SPAN> <SPAN
								class="con">暂未排期</SPAN></li> 
					  </#if> 
					</#if> 
			</@security.authorize>
		</UL>
	</DIV>
</DIV>
</#macro>
