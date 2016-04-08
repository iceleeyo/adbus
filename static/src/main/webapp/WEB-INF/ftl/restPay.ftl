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
			ifAnyGranted="ShibaFinancialManager">edit</@security.authorize>');
});
</script>

	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">分期详情</A>
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
				<A class="black" href="#">订单首付款</A>
			</p>
		</H3>
		<BR>
		
		<!-- plan -->
		<form id="form2"  target="_blank" style="display: none;" action="https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet" method="post">
			    <input type="hidden" name="APIName" value="B2B"/>
			    <input type="hidden" name="APIVersion" value="001.001.001.001"/>
			    <input type="hidden" name="Shop_code" value="0200EC14729207"/>
			    <input type="hidden" name="MerchantURL" value="${callback_plan}"/>
			    <input type="hidden" name="ContractNo" value="${contractNo_plan}"/>
			    <input type="hidden" name="ContractAmt" value="${totalPrice_plan}"/>
			    <input type="hidden" name="Account_cur" value="001"/>
			    <input type="hidden" name="JoinFlag" value="2"/>
			    <input type="hidden" name="Mer_Icbc20_signstr" value="${a1_plan}"/>
			    <input type="hidden" name="Cert" value="${a2_plan}"/>
			    <input type="hidden" name="SendType" value="0"/>
			    <input type="hidden" name="TranTime" value="${TranTime_plan}" />
			    <input type="hidden" name="Shop_acc_num" value="0200004519000100173"/>
			    <input type="hidden" name="PayeeAcct" value="0200004519000100173"/>
			    <input type="hidden" name="GoodsCode" value="CODE_MEDIA"/>
			    <input type="hidden" name="GoodsName" value="SPTC"/>
			    <input type="hidden" name="Amount" value="1"/>
			    <input type="hidden" name="TransFee" value="1"/>
			    <input type="hidden" name="ShopRemark" value=""/>
			    <input type="hidden" name="ShopRem" value=""/>
			    <input type="submit" id="planSubmit" value="确定"/>
			</form>
		
		<form id="form1"  target="_blank" style="display: none;" action="https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet" method="post">
			    <input type="hidden" name="APIName" value="B2B"/>
			    <input type="hidden" name="APIVersion" value="001.001.001.001"/>
			    <input type="hidden" name="Shop_code" value="0200EC14729207"/>
			    <!--若不正确，将无银行反馈信息，注意不能省略"http://"-->
			    <input type="hidden" name="MerchantURL" value="${callback}"/>
			    <input type="hidden" name="ContractNo" value="${contractNo}"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="ContractAmt" value="${totalPrice}"/>
			    <input type="hidden" name="Account_cur" value="001"/>
			    <input type="hidden" name="JoinFlag" value="2"/>
			    <input type="hidden" name="Mer_Icbc20_signstr" value="${a1}"/>
			    <input type="hidden" name="Cert" value="${a2}"/>
			    <input type="hidden" name="SendType" value="0"/>
			    <input type="hidden" name="TranTime" value="${TranTime}" />
			    <input type="hidden" name="Shop_acc_num" value="0200004519000100173"/>
			    <input type="hidden" name="PayeeAcct" value="0200004519000100173"/>
			    <input type="hidden" name="GoodsCode" value="CODE_MEDIA"/>
			    <input type="hidden" name="GoodsName" value="SPTC"/>
			    <input type="hidden" name="Amount" value="1"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="TransFee" value="1"/>
			    <input type="hidden" name="ShopRemark" value=""/>
			    <input type="hidden" name="ShopRem" value=""/>
			    <input type="submit" id="icbcOPer" value="确定"/>
			</form>
			
			<TABLE class="ui-table ui-table-gray">
			
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">分期详情</TD>
				<TD>
					<SPAN></SPAN><SPAN class="con"><a href="javascript:void(0)" onclick="queryPayPlanDetail('${rc.contextPath}',${orderview.order.id!''});" >查看</a></SPAN>
				</TD>
			</TR>
			
			
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">分期付款</TD>
				<TD>
					<input type="radio" name="payWay" value="payAll">余额付款 <font color="orange">#{(payAll)!'';m2M2} </font>
					<#if payNext gt 0 >
					<input type="hidden" id="payNextLocation" value="${payNextLocation!''}"/> 
					<input type="radio" name="payWay" value="payNext">本期付款 <font color="orange">#{(payNext)!'';m2M2}</font>
					</#if>
					
					<input type="hidden" id="allLocation" value="${allLocation!''}"/> 
				</TD>
			</TR>
		</TABLE>
		<TABLE class="ui-table ui-table-gray">
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付方式</TD>
				<TD>
					<input type="radio" name="payType" value="online">在线支付 
					<input type="radio" name="payType" value="remit">汇款支付 
					<input type="radio"	name="payType" value="others">其他支付
				</TD>
				<TD>
					<div id="otherpay">
						<select class="ui-input" name="otherpay" id="otherpay">
							<option value="" selected="selected">请选择支付方式</option>
							<option value="check">支票</option>
							<option value="cash">现金</option>
						</select>
					</div>
				</TD>
			</TR>
			<tbody id="pingzhengTab">
			<TR style="height: 45px;">
				<TD style="text-align: right">上传有效银行支付凭证（可选）</TD>
				<TD colspan=3>
				  <form id="form_img" method="post" enctype="multipart/form-data"> 
                     <img src="" id="showImg" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id ="fileMaterial" name="fileMaterial" type="file" onchange="uploadImaget('form_img');"/>
                 </form>
				</TD>
			</TR>
			</tbody>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button type="button" onclick="pay('userFristPay')" class="block-btn">确认支付</button>
		</div>
		
		<div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>
			<font color="orange"><B>说明</B></font><br>
			余额付款：指订单不采用分期模式支付一次性需要支付的总额<br>
			本期付款：根据北京世巴传媒公司财务部门指出的分期计划,<br>本期款项等于历史未付款项 加 付款日最近一期的款项<br>
			<font color="orange"><B>温馨提示</B></font><br>
			三方一致：合同甲方公司名称-付款方银行开户名称-开具发票抬头名称<br>
			线下付款的账户信息：<br>
			&nbsp;&nbsp;&nbsp;&nbsp;开户行：<B>工行知春路支行</B><br>
			&nbsp;&nbsp;&nbsp;&nbsp;账户名称：<b>北京世巴传媒有限公司</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;收款方账号：<b>0200207909200097152</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;公司地址：北京市海淀区紫竹院路32号15号平房	电话：68427368<br>
		</div>
		</div>

	</div>
</div>

 <#include "template/hisDetail.ftl" />
</@frame.html>






