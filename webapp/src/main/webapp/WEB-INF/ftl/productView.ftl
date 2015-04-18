<#import "template/template.ftl" as frame>
<#import "spring.ftl" as spring />

<#global menu="产品定义">
<#assign action="增加">
<#if prod??><#assign action="修改"></#if>

<@frame.html title="${action}产品套餐"> 



	  <DIV class="color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">套餐详情</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">

  <LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
   <LI style="width: 240px;"><SPAN>时长：</SPAN><SPAN class="con">${prod.duration!''}</SPAN></LI>
   <LI style="width: 240px;"><SPAN>单日播放次数：</SPAN><SPAN class="con">${prod.playNumber!''}</SPAN></LI>
   <LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN class="con">${prod.firstNumber!''}</SPAN></LI>
   <LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN class="con">${prod.lastNumber!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">
  <#if (!prod?? || prod.type == 'video')>视频</#if>
  <#if (!prod?? || prod.type == 'image')>图片</#if>
  <#if (!prod?? || prod.type == 'info')>文本</#if>
  </SPAN></LI>
</UL>
</DIV>
</@frame.html>
