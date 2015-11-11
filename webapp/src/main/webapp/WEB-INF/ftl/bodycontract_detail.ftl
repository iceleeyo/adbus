<#import "template/template.ftl" as frame> <#import
"template/select_lines.ftl" as select_lines> <@frame.html title="我的订单"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<@select_lines.select_lines seriaNum=bodycontract.seriaNum
activityId="as"/>
<div class="p20bs mt10 color-white-bg border-ec">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="saveContract"
		enctype="multipart/form-data">
		<H3 class="text-xl title-box">
			<A class="black" href="#">合同详情-[流水号：${(bodycontract.seriaNum)!''}]</A>
		</H3>
		<DIV class="summary mt10 uplan-summary-div">
			<UL class="uplan-detail-ul">
				<LI style="width: 240px;"><SPAN>法人代表：</SPAN><SPAN class="con">${(bodycontract.legalman)!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>公司名称：</SPAN><SPAN class="con">${(bodycontract.company)!''}</SPAN></LI>
				<li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
				<LI style="width: 240px;"><SPAN>联系人：</SPAN><SPAN class="con">${(bodycontract.relateMan)!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>联系电话：</SPAN><SPAN class="con">${(bodycontract.phoneNum)!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>地址：</SPAN><SPAN class="con">${(bodycontract.companyAddr)!''}</SPAN></LI>
				<li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
				<li style="width: 720px;"><SPAN> 备注信息：</SPAN><SPAN class="con">${bodycontract.remark!''}</SPAN></li>

			</UL>
		</DIV>
	</form>
</div>
<#include "template/hisDetail.ftl" /> </@frame.html>






