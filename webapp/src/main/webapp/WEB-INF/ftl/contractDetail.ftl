<#import "template/template.ftl" as frame>

<@frame.html title="合同详细">

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							  <DIV class="color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">合同详情</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
  <LI style="width: 240px;"><SPAN>合同编号：</SPAN><SPAN class="con">${(view.mainView.contractCode)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>合同名称：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${(view.mainView.contractName)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>广告主：</SPAN><SPAN class="con">${(view.mainView.userId)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>金额：</SPAN><SPAN class="con">${(view.mainView.amounts)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>行业：</SPAN><SPAN class="con">${(view.mainView.industry)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>开始日期：</SPAN><SPAN class="con"><#setting
															date_format="yyyy-MM-dd"> ${(view.mainView.startDate)?date!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>终止日期：</SPAN><SPAN class="con"><#setting
															date_format="yyyy-MM-dd"> ${(view.mainView.endDate)?date!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>附件：</SPAN><SPAN class="con"><#list view.files as item> ${item.name!''}
															</#list></SPAN></LI>
</UL>
</DIV>
</DIV>
						</form>
</@frame.html>






