<#import "template/template.ftl" as frame>
<#import "template/orderDetail.ftl" as orderDetail/>
<@frame.html title="订单详细" js=["js/highslide/highslide-full.js", "js/video-js/video.js", "js/video-js/lang/zh-CN.js","js/layer-v1.9.3/layer-site.js","js/jquery-dateFormat.js"]
css=["js/highslide/highslide.css", "js/video-js/video-js.css"]>
    <#include "template/preview.ftl" />

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
 <div id="process" class="section4">
 
 	 <#if sections.submitOrder ??> 
    	<#assign _order=sections.submitOrder> 
     </#if> 
      <#if sections.payment ??> 
    	<#assign _payment=sections.payment> 
     </#if>
      <#if sections.approve1 ??> 
    	<#assign _approve1=sections.approve1> 
     </#if> 
      <#if sections.shangboReport ??> 
    	<#assign _shangboReport=sections.shangboReport> 
     </#if>  
      <#if sections.shangboReport ??> 
    	<#assign _shangboReport=sections.shangboReport> 
     </#if>  
            		<#if orderview.task_name="世巴提交排期表" || orderview.task_name="北广录入排期表">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3">2015-3-14</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3">2015-3-14</li></ul></div>
            		<#elseif orderview.task_name="世巴财务确认">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li></ul></div>
            		<#elseif orderview.task_name="支付完成">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li></ul></div>
            		<#elseif orderview.task_name="世巴初审"||orderview.task_name="北广终审" >
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单<li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li></ul></div>
            		<#elseif orderview.task_name="上播报告" >
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li></ul></div>
            		<#elseif orderview.task_name="监播报告">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>		
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<#elseif orderview.task_name="已支付待审核">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['submitOrder'])?date)!''}</li><li id="track_time_0" class="tx3"> <#setting date_format="HH:mm:ss">${((operTimeTree['submitOrder'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['payment'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['payment'])?date)!''}</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['approve1'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['approve1'])?date)!''}</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['shangboReport'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['shangboReport'])?date)!''}</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li><li id="track_time_0" class="tx3"> </li></ul></div>
            		<#elseif orderview.task_name="已排期待上播">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['submitOrder'])?date)!''}</li><li id="track_time_0" class="tx3"> <#setting date_format="HH:mm:ss">${((operTimeTree['submitOrder'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['payment'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['payment'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['approve1'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['approve1'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['shangboReport'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['shangboReport'])?date)!''}</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li><li id="track_time_0" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="已上播">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['submitOrder'])?date)!''}</li><li id="track_time_0" class="tx3"> <#setting date_format="HH:mm:ss">${((operTimeTree['submitOrder'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['payment'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['payment'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['approve1'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['approve1'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['shangboReport'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['shangboReport'])?date)!''}</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li><li id="track_time_0" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="待支付">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['submitOrder'])?date)!''}</li><li id="track_time_0" class="tx3"> <#setting date_format="HH:mm:ss">${((operTimeTree['submitOrder'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li></li><li class="tx2">审核</li><li id="track_time_1" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['approve1'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['approve1'])?date)!''}</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['payment'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['payment'])?date)!''}</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['shangboReport'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['shangboReport'])?date)!''}</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li><li id="track_time_0" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="已支付待审核">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['submitOrder'])?date)!''}</li><li id="track_time_0" class="tx3"> <#setting date_format="HH:mm:ss">${((operTimeTree['submitOrder'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"><#setting date_format="yyyy-MM-dd">${((operTimeTree['payment'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['payment'])?date)!''}</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['approve1'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['approve1'])?date)!''}</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"> <#setting date_format="yyyy-MM-dd">${((operTimeTree['shangboReport'])?date)!''}</li><li id="track_time_0" class="tx3"><#setting date_format="HH:mm:ss">${((operTimeTree['shangboReport'])?date)!''}</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li><li id="track_time_0" class="tx3"> </li></ul></div>
            		<#else>
            		   <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3">2015-3-14</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li></ul></div>
            		</#if>
            	</div>
    <@orderDetail.orderDetail orderview=orderview quafiles=quafiles suppliesView=suppliesView/>
    <#include "template/hisDetail.ftl" />
</@frame.html>






