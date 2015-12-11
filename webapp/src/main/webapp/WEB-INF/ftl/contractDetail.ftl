<#import "template/template_blank.ftl" as frame> <@frame.html
title="合同详细"> <#import "macro/materialPreview.ftl" as preview> <#include
"template/preview.ftl" />
<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>

<#list views as view>
<div class="withdrawB-wrap color-white-bg fn-clear"
	style="width: 780px; margin-left: 0px;">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="saveContract"
		enctype="multipart/form-data">
		<H3 class="text-xl title-box">
			<A class="black" href="#">合同详情【${(view.jpaContract.contractName)!''}】</A>
		</H3>
		<DIV class="summary mt10 uplan-summary-div">
			<UL class="uplan-detail-ul">
				<LI style="width: 240px;"><SPAN>合同编号：</SPAN><SPAN class="con">${(view.jpaContract.contractCode)!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>合同名称：</SPAN><SPAN class="con"
					style="color: rgb(245, 135, 8);">${(view.jpaContract.contractName)!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>合同类型：</SPAN><SPAN class="con">
				<#if view.jpaContract.parentid==0>大合同<#else>子合同</#if>
				</SPAN></LI>
				<LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${(view.jpaContract.type)!''}</SPAN></LI>

				<li style="width: 750; border-bottom: 1px solid #F7F7F7"></li>
				<LI style="width: 240px;"><SPAN>广告主：</SPAN><SPAN class="con">${(view.jpaContract.userId)!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>金额（人民币：元）：</SPAN><SPAN
					class="con">${(view.jpaContract.amounts)!''}</SPAN></LI>

				<LI style="width: 240px;"><SPAN>所在行业：</SPAN><SPAN class="con">${(view.jpaContract.industry.name)!''}</SPAN></LI>
				<li style="width: 750; border-bottom: 1px solid #F7F7F7"></li>
				<LI style="width: 240px;"><SPAN>签订日期：</SPAN><SPAN class="con"><#setting
						date_format="yyyy-MM-dd"> ${(view.jpaContract.signDate)?date!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>开始日期：</SPAN><SPAN class="con"><#setting
						date_format="yyyy-MM-dd"> ${(view.jpaContract.startDate)?date!''}</SPAN></LI>
				<LI style="width: 240px;"><SPAN>终止日期：</SPAN><SPAN class="con"><#setting
						date_format="yyyy-MM-dd"> ${(view.jpaContract.endDate)?date!''}</SPAN></LI>
				<li style="width: 750; border-bottom: 1px solid #F7F7F7"></li>
				<LI style="width: 720px;"><SPAN>合同结算：</SPAN><SPAN
					class="con">${(view.jpaContract.settle)!''}</SPAN></LI>
					<LI style="width: 720px;"><SPAN>备注：</SPAN><SPAN
					class="con">${(view.jpaContract.remark)!''}</SPAN></LI>
				<LI style="width: 720px;"><SPAN>附件【合同扫描件及其他附件】：</SPAN><SPAN
					class="con"> <#list view.files as item>
						<@preview.materialPreview view=view items=[item]/> <a
						class="">${item.name!''}</a><a
						style="margin-left: 20px;"
						href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">点击下载附件</a>
						&nbsp;&nbsp; &nbsp; </#list>
				</SPAN></LI>
			</UL>
		</DIV>
	</form>
</div>
</#list>
</@frame.html>






