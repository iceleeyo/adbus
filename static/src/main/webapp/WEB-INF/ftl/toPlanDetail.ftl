<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "template/template.ftl" as frame> <@frame.html title="订单详细"
js=["js/highslide/highslide-full.js", "js/video-js/video.js","js/jquery-dateFormat.js","index_js/sift_common.js",
"js/video-js/lang/zh-CN.js"] css=["js/highslide/highslide.css",
"js/video-js/video-js.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","css/liselect/pkg-generator.css$ver=1431443489.css"]> <#include "template/preview.ftl" />
<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>

 <#import "template/orderDetail.ftl" as orderDetail/>
 <@orderDetail.orderDetail orderview=orderview quafiles=quafiles
suppliesView=suppliesView/>
<div id="setPayPlan" class="setPayPlan" >
<script type="text/javascript">
$(document).ready(function(){
initPayPlanTable('${rc.contextPath}',${orderview.order.id},'<@security.authorize
			ifAnyGranted="ShibaFinancialManager"></@security.authorize>');
});
</script>

	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">订单分期详情</A>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0">
				
				<div class="withdraw-wrap color-white-bg fn-clear">
	<table id="payPlanTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>期数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>付款方式</th>
								<th>状态</th>
								<th>付款人</th>
								<th>备注</th>
								<@security.authorize ifAnyGranted="ShibaFinancialManager"> 
								<th>操作</th></@security.authorize>
								
							<@security.authorize ifNotGranted="ShibaFinancialManager"> 
								<th></th>
								</@security.authorize>
			</tr>
		</thead>

	</table>
</div>
				
			</TD>
				</TR>
		</TABLE>
	</div>
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
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付方式</TD>
				<TD>
					 ${planView.payName!''} 
				</TD>
			</TR>
			
			<#if planView.plan.payState == "check">
			<TR style="height: 45px;">
					<TD style="text-align: right">支付状态</TD>
					<TD><input name="rad" type="radio" value="true"
						checked="checked" style="padding: 5px 15px;" />支付正常 <input
						name="rad" type="radio" value="false" style="padding: 5px 15px;" />支付异常</TD>
				</TR>
				<TR>
					<TD style="text-align: right">审核意见</TD>
					<TD colspan=3><textarea name="financialcomment"
							id="financialcomment" class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>
				</#if>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<#if planView.plan.payState == "check">
			<button type="button" onclick="pay('userFristPay')" class="block-btn">确认支付</button>
			</#if>
		</div>
		 

	</div>
</div>

 <#include "template/hisDetail.ftl" />
</@frame.html>






