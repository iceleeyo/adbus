<#import "../macro/materialPreview.ftl" as preview>

<DIV class="p20bs color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单详情-${(orderview.longOrderId)!''}</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
                  <LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(orderview.order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">
    <#if (!prod?? || prod.type == 'video')>视频</#if>
      <#if (!prod?? || prod.type == 'image')>图片</#if>
      <#if (!prod?? || prod.type == 'info')>文本</#if>
  </SPAN></LI>
  <LI style="width: 240px;"><SPAN>订单状态：</SPAN><SPAN class="con">
    <#if orderview.task_name?has_content>
        ${orderview.task_name!''}
    <#else>
        <#if (orderview.order.stats == 0) >
            未支付
        <#elseif (orderview.order.stats == 1) >
            已支付
        <#elseif (orderview.order.stats == 2) >
            已排期
        <#elseif (orderview.order.stats == 3) >
            已上播
        <#elseif (orderview.order.stats == 4) >
            已完成
        <#elseif (orderview.order.stats == 5) >
            已取消
        <#else>
            未知
        </#if>
    </#if></SPAN></LI>
  <LI style="width: 240px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>是否开发票：</SPAN><SPAN class="con">
  <#if orderview.order.isInvoice==1 >
        <a target="_blank" href="${rc.contextPath}/order/invoiceDetail/${orderview.order.userId!''}" > 是</a>
   <#else>
      否    
  </#if>
  
  </SPAN></LI>
  <LI style="width: 240px;"><SPAN>合同号：</SPAN><SPAN class="con">${(orderview.order.contractCode)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>物料编号：</SPAN><SPAN class="con">${(suppliesView.mainView.seqNumber)!''}</SPAN></LI>
  <LI style="width: 720px;"><SPAN>物料：</SPAN><SPAN class="con">
  <@preview.materialPreview view=suppliesView/>
  </SPAN></LI>
  <LI style="width: 720px;"><SPAN>用户资质：</SPAN><SPAN class="con">
  <#if quafiles.files?has_content>
     <#list quafiles.files as item>
      <a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a> &nbsp;&nbsp; &nbsp;
     </#list>
     </#if>
    </SPAN></LI>
  <#if (orderview.order.stats >= 2) >
      <LI style="width: 240px;"><SPAN><a target="_blank" href="${rc.contextPath}/schedule/${order.id!''}" >查看排期表</a></SPAN><SPAN class="con"></SPAN></LI>
  </#if>

</UL>
</DIV>
</DIV>