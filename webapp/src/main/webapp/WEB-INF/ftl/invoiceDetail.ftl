<#import "template/template.ftl" as frame>
<@frame.html title="发票详细" js=["js/highslide/highslide-full.js", "js/video-js/video.js", "js/video-js/lang/zh-CN.js"]
css=["js/highslide/highslide.css", "js/video-js/video-js.css"]>
    <#include "template/preview.ftl" />

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
                
    <DIV class="p20bs color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">发票详情</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
              <LI style="width: 240px;"><SPAN>发票类型：</SPAN><SPAN class="con">
      <#if ((invoiceView.mainView.type)?has_content && invoiceView.mainView.type == 1)>
               专用发票
      <#else>
              普通发票
      </#if>
  </SPAN></LI>
  <LI style="width: 240px;"><SPAN>发票抬头：</SPAN><SPAN class="con">${(invoiceView.mainView.title)!''}</SPAN></LI>
  <#if ((invoiceView.mainView.type)?has_content && invoiceView.mainView.type == 1)>
  <LI style="width: 240px;"><SPAN>税务登记证号：</SPAN><SPAN class="con">${(invoiceView.mainView.taxrenum)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>基本户开户银行名称：</SPAN><SPAN class="con" >${(invoiceView.mainView.bankname)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>基本户开户账号：</SPAN><SPAN class="con">${(invoiceView.mainView.accountnum)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>注册场所地址：</SPAN><SPAN class="con">${(invoiceView.mainView.regisaddr)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>注册场所电话：</SPAN><SPAN class="con">${(invoiceView.mainView.fixphone)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>邮寄地址地址：</SPAN><SPAN class="con">${(invoiceView.mainView.mailaddr)!''}</SPAN></LI>
  <LI style="width: 720px;"><SPAN>附件：</SPAN><SPAN class="con">
  <#if invoiceView.files?has_content>
     <#list invoiceView.files as item>
      <a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a> &nbsp;&nbsp; &nbsp;
     </#list>
     </#if>
    </SPAN></LI>
</#if>
</UL>
</DIV>
   
         
      
</DIV>
</@frame.html>






