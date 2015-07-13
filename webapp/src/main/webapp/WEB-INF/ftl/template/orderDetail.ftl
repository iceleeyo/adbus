<#import "../macro/materialPreview.ftl" as preview>
<#macro orderDetail orderview quafiles suppliesView="" title="订单详情" suppliesLink=true viewScheduleLink=true>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#assign prod=orderview.order.product>

<script type="text/javascript">

</script>
<DIV class="p20bs color-white-bg border-ec">
                <H3 class="text-xl title-box"><p style="text-align: left"><A class="black" href="#">${title}-${(orderview.longOrderId)!''}</A></p></H3>
               <DIV class="summary uplan-summary-div">
              <UL class="uplan-detail-ul">
              
              	  <li class="s-left f-iconli"><span class="s-left tt"><i class="s-left ff-icon"></i>套餐信息</span></li>
                  <li style="width: 800px;"><SPAN>套餐名称：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查看套餐详细内容!" onclick="showProductlayer('${rc.contextPath}/product/ajaxdetail/',${prod.id});"  >${prod.name!''}</a></SPAN></li>
  				  <#if !(cpdDetail?exists)>
  				  <li style="width: 200px;"><SPAN>套餐价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">
  				  <@security.authorize ifNotGranted="ShibaOrderManager"> #{(orderview.order.price)!'';m2M2}</@security.authorize>
  				  <@security.authorize ifAnyGranted="ShibaOrderManager">
  				  <a class="layer-tips" tip="点击修改订单价格!" onclick="setOrderPrice('${rc.contextPath}/order/setOrderPrice',${orderview.order.id});"  >
  				  <span id="prodPrice">#{(orderview.order.price)!'';m2M2}</span></a>
  				  </@security.authorize>
  				  </SPAN></li>
  				  </#if> 
  				  <#if cpdDetail?exists>
  				    <li style="width: 200px;"><SPAN>套餐底价：</SPAN>${cpdDetail.saleprice!''}</li>
  				    <li style="width: 200px;"><SPAN><b>成交价</b>：</SPAN><font color='#ff9966'>${cpdDetail.comparePrice!''}</font></li>
  				  </#if> 
  				  <li style="width: 200px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN></li>
  				 
  				  <@security.authorize ifAnyGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager"> 
  				  <li style="width: 200px;"><SPAN>下单用户：</SPAN><SPAN class="con">
  				  <a class="layer-tips" tip="点击查看下单用户信息!" onclick="showOrderUserlayer('${rc.contextPath}/user/u_ajax/', '${(orderview.order.creator)!''}');"  >
  				  ${(orderview.order.creator)!''}</a></SPAN></li>
  				  </@security.authorize>
  				  	<@security.authorize ifAnyGranted="advertiser" ifNotGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
  				   <li style="width: 200px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(orderview.order.creator)!''}</SPAN></li>
  				  </@security.authorize>
  				  <li style="width: 800;"></li>
  				  <li style="width: 200px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''}</SPAN></li>
  				  <li style="width: 200px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''}</SPAN></li>
    	
  				  
    			  <li style="width: 200px;"><SPAN>电子合同：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查看电子合同!" onclick="eleContract('${rc.contextPath}',${orderview.order.id!''});"  >查看</a></SPAN></li>
  				  <#if (orderview.order.ordRemark!'')?length lt 38>
	 <li style="width: 720px;"><SPAN> 备注信息：</SPAN><SPAN class="con">${orderview.order.ordRemark!''}</SPAN></li> 
	 <#else>
		<li style="width: 720px;"><SPAN>备注信息：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查详细内容!" onclick="showRemark('${orderview.order.ordRemark!''}');"  >${substring(orderview.order.ordRemark,0,38)}</a></SPAN></li> 
	 </#if>
  <#if orderview.closed>
  	 <li style="width: 720px;"><SPAN> 关闭原因：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查关闭内容!" onclick="showRemark('${orderview.order.closeRemark!''}');"  >${substring(orderview.order.closeRemark,0,38)}</a></SPAN></li>
  </#if>
  
    			 
    			  <li class="s-left f-iconli"><span class="s-left tt"><i class="s-left ff-icon"></i>支付及发票</span></li>
  				  <#if orderview.payTypeString?has_content>
 				  <li style="width: 200px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></li>
 				  <#if orderview.payTypeString?has_content && orderview.payTypeString=="关联合同">
  				  <li style="width: 200px;"><SPAN>合同号：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查看发票详细内容!" onclick="contractdetail('${rc.contextPath}',${(orderview.order.contractId)!''});" href="javascript:void(0)">${(orderview.order.contractCode)!''}</a></SPAN></li>
  				  <#elseif orderview.payTypeString?has_content && orderview.payTypeString=="线上支付">
  				  <li style="width: 200px;"><SPAN>流水号：</SPAN><SPAN class="con">123912800234</SPAN></li>
  				  </#if>
  				  </#if>
  				  <li style="width: 180px;"><SPAN>订单状态：</SPAN>
  				  <SPAN class="con">
  				  	<#if orderview.task_name?has_content>
        				${orderview.task_name!''}
    				<#else>已完成
    				</#if></SPAN></li>
  				  <li style="width: 200px;"><SPAN>是否开发票：</SPAN><SPAN class="con">
 				  <#if orderview.order.isInvoice==1 >
                  <a class="layer-tips" tip="点击可查看发票详细内容!" onclick="invoicedetail('${rc.contextPath}',${orderview.order.id!''});" href="javascript:void(0)"> 是</a>
				   <#else>
				      否    
				  </#if></SPAN></li> 				  
  				   

  				   
				  <li class="s-left f-iconli"><span class="s-left tt"><i class="s-left ff-icon"></i>物料及资质</span></li>
  				  
				  <#if suppliesLink>
  				  <li style="width: 200px;"><SPAN>物料资质：</SPAN><SPAN class="con"><a href="${rc.contextPath}/supplies/suppliesDetail/${(suppliesView.mainView.id)!''}">查看</a></SPAN></li>
				  
				  <#if suppliesView.mainView.seqNumber?has_content >
  				  <li style="width: 200px;"><SPAN>物料编号：</SPAN><SPAN class="con">${(suppliesView.mainView.seqNumber)!''}</SPAN></li>
  				  </#if>
  				  <#if quafiles??  >
  				  
  				  </#if>
				  </#if>

				  <li class="s-left f-iconli"><span class="s-left tt"><i class="s-left ff-icon"></i>排期信息</span></li>
  				 		 
<#if orderview.task_name?exists && (orderview.task_name=='已排期待上播' || orderview.task_name=='已上播' ||orderview.task_name=='已排期待上播' || orderview.task_name=='已完成')>
      <li style="width: 200px;"><SPAN>排期状态：<a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}" >
          <#if city.mediaType == 'screen'>
          查看排期表
          <#elseif city.mediaType == 'body'>
          查看上刊巴士列表
          

          </#if>

</a></SPAN>

</li>
<#else>
<li style="width: 200px;">
<SPAN>排期状态：</SPAN>
<SPAN class="con">未排期</SPAN>
</li>
</#if>
 

</UL>
</DIV>
</DIV>
</#macro>
