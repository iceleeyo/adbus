<#import "template_blank.ftl" as frame> <@frame.html title="客户详细">
<#include "preview.ftl" />
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN">
<#if view.orderUserJson??>
<div class="withdrawB-wrap color-white-bg fn-clear">
	<DIV class="summary mt10 uplan-summary-div">
		<UL class="uplan-detail-ul">
			<LI style="width: 720px;"><SPAN>公司名称：</SPAN><SPAN class="con" >${view.orderUserJson.company!''}</SPAN>
			</LI>
			<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
			<LI style="width: 320px;"><SPAN>公司地址： </SPAN><SPAN class="con"
				 > ${view.orderUserJson.companyAddr!''}</SPAN> <SPAN></SPAN>
			</LI>  
			<LI style="width: 240px;"><SPAN>法定代表人：</SPAN><SPAN class="con">${view.orderUserJson.legalman!''}</SPAN>
			</LI>  
			<LI style="width: 240px;"><SPAN>联系人：</SPAN><SPAN class="con">${view.orderUserJson.relateman!''}</SPAN>
			</LI>
			<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
			<LI style="width: 240px;"><SPAN>联系电话：</SPAN><SPAN class="con">${view.orderUserJson.phone!''}</SPAN></LI>
		 	<LI style="width: 240px;"><SPAN>邮编：</SPAN><SPAN class="con">
					${view.orderUserJson.zipCode!''}</SPAN> <SPAN></SPAN></LI> 
					<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
					 <#if view.orderUserJson.email??>
					<LI style="width: 240px;"><SPAN>邮箱：</SPAN><SPAN class="con">
					${view.orderUserJson.email!''}</SPAN> <SPAN></SPAN></LI> 
					</#if>
					 <#if view.orderUserJson.firstName??>
					<LI style="width: 240px;"><SPAN>下单用户姓名：</SPAN><SPAN class="con">
					${view.orderUserJson.firstName!''}</SPAN> <SPAN></SPAN></LI> 
					<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
						</#if>
			
			<LI style="width: 100%;padding-top: 20px;"><SPAN>营业执照复印件副本：</SPAN>
				 <#if view.orderUQualifiView?? && view.orderUQualifiView.user_license?has_content>
				 <SPAN class="con">
				  <a><img src="${rc.contextPath}/upload_temp/${view.orderUQualifiView.user_license!''}" class="m11"
									height="65" width="65"></a></SPAN></#if>	</LI>
									
			
			<LI style="width: 100%;padding-top: 20px;"><SPAN>税务登记复印件副本：</SPAN>
			 <#if view.orderUQualifiView?? && view.orderUQualifiView.user_tax?has_content><SPAN class="con">
				 <a><img src="${rc.contextPath}/upload_temp/${view.orderUQualifiView.user_tax!''}" class="m11"
									height="65" width="65"></a></SPAN></#if>	</LI> 
									
									
			<LI style="width: 100%;padding-top: 20px;"><SPAN>组织机构代码证书：</SPAN>
			 <#if view.orderUQualifiView?? && view.orderUQualifiView.user_code?has_content><SPAN class="con">
				 <a><img src="${rc.contextPath}/upload_temp/${view.orderUQualifiView.user_code!''}" class="m11"
									height="65" width="65"></a></SPAN></#if>	</LI>  			 
			
		</UL>
	</DIV>
</div>
<#else>
查询失败..
</#if> </@frame.html>
