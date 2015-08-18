

<div class="p20bs mt10 color-white-bg border-ec">
<form data-name="withdraw" name="userForm2" id="userForm2" class="ui-form" method="post" action="saveContract"
	enctype="multipart/form-data">
<H3 class="text-xl title-box"><A class="black" href="#">合同详情-[流水号：${(bodycontract.seriaNum)!''}]</A></H3>
  <DIV class="summary mt10 uplan-summary-div">
  <UL class="uplan-detail-ul">
  <LI style="width: 240px;"><SPAN>法人代表：</SPAN><SPAN class="con">${(bodycontract.legalman)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>公司名称：</SPAN><SPAN class="con">${(bodycontract.company)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>联系人：</SPAN><SPAN class="con">${(bodycontract.relateMan)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>联系电话：</SPAN><SPAN class="con">${(bodycontract.phoneNum)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>地址：</SPAN><SPAN class="con">${(bodycontract.companyAddr)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>预留截止时间：</SPAN><SPAN class="con">
  <#if bodycontract.lockExpiredTime?? >
       <#if activityId == "financialCheck" || activityId == "usertask2">
          <a class="layer-tips" tip="点击设置" href="javascript:void(0);" onclick="setLockTime('${rc.contextPath}',${(bodycontract.id)!''})" >
          <#setting date_format="yyyy-MM-dd">${(bodycontract.lockExpiredTime?date)!''}</a>
        <#else>
          <#setting date_format="yyyy-MM-dd">${(bodycontract.lockExpiredTime?date)!''}
     </#if>     
          <#else>
                           未设置
  </#if>        
  </SPAN></LI>
  <li style="width: 720px;"><SPAN> 备注信息：</SPAN><SPAN class="con">${bodycontract.remark!''}</SPAN></li> 
  							    
</UL>
</DIV>
</form>
              
</div>

