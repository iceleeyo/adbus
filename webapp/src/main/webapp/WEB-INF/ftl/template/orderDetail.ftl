<#import "../macro/materialPreview.ftl" as preview>
<#macro orderDetail orderview quafiles suppliesView="" title="订单详情" suppliesLink=true viewScheduleLink=true>
<#assign prod=orderview.order.product>
<DIV class="p20bs color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">${title}-${(orderview.longOrderId)!''}</A></H3>
               <DIV class="summary uplan-summary-div">
              <UL class="uplan-detail-ul">
                  <LI style="width: 400px;"><SPAN>套餐名称：</SPAN><SPAN class="con"><a href="${rc.contextPath}/product/d/${prod.id}" target="_blank">${prod.name!''}</a></SPAN></LI>
  				  <LI style="width: 200px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  				  <LI style="width: 200px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN></LI>
  				  <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
  				  <LI style="width: 200px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(orderview.order.creator)!''}</SPAN></LI>
  				  <LI style="width: 200px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''}</SPAN></LI>
  				  <LI style="width: 200px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''}</SPAN></LI>
  				  <LI style="width: 180px;"><SPAN>订单状态：</SPAN>
  				  <SPAN class="con"><#if orderview.task_name?has_content>
        ${orderview.task_name!''}
    <#else>已完成
<#--
        <#if (orderview.order.stats == 'unpaid') >
            未支付
        <#elseif (orderview.order.stats == 'paid') >
            已支付
        <#elseif (orderview.order.stats == 'scheduled') >
            已排期
        <#elseif (orderview.order.stats == 'started') >
            已上播
        <#elseif (orderview.order.stats == 'completed') >
            已完成
        <#elseif (orderview.order.stats == 'cancelled') >
            已取消
        <#else>
            未知
        </#if>
-->
    </#if></SPAN></LI>
  				  <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
  				  
 				  <LI style="width: 200px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></LI>
 				  <#if orderview.payTypeString?has_content && orderview.payTypeString=="合同">
  				  <LI style="width: 200px;"><SPAN>合同号：</SPAN><SPAN class="con">${(orderview.order.contractCode)!''}</SPAN></LI>
  				   <#elseif orderview.payTypeString?has_content && orderview.payTypeString=="线上支付">
  				   <LI style="width: 200px;"><SPAN>流水号：</SPAN><SPAN class="con">123912800234</SPAN></LI>
  				   </#if>
  				   
				  <LI style="width: 200px;"><SPAN>是否开发票：</SPAN><SPAN class="con">
 				  <#if orderview.order.isInvoice==1 >
                  <a target="_blank" href="${rc.contextPath}/order/invoiceDetail/${orderview.order.userId!''}" > 是</a>
				   <#else>
				      否    
				  </#if></SPAN></LI>
  				  <#if suppliesLink>
				  <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
  				  <LI style="width: 200px;"><SPAN>物料编号：</SPAN><SPAN class="con">${(suppliesView.mainView.seqNumber)!''}</SPAN></LI>
  				  <LI style="width: 200px;"><SPAN>物料详情：</SPAN><SPAN class="con"><a href="${rc.contextPath}/supplies/suppliesDetail/${(suppliesView.mainView.id)!''}">查看物料与资质</a></SPAN></LI>
  <!-- <LI style="width: 720px;"><SPAN>物料：</SPAN>
  <SPAN class="con">
  <@preview.materialPreview view=suppliesView/>
  </SPAN></LI> -->
  <!-- <LI style="width: 720px;"><SPAN>用户资质：</SPAN><SPAN class="con">
  <#if quafiles.files?has_content>
     <#list quafiles.files as item>
      <a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a> &nbsp;&nbsp; &nbsp;
     </#list>
     </#if>
    </SPAN></LI> -->
</#if>
<#if viewScheduleLink>
      <LI style="width: 200px;"><SPAN>排期状态：<a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}" >查看排期表</a></SPAN><SPAN class="con"></SPAN></LI>
</#if>
 <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
</UL>
</DIV>
</DIV>
</#macro>