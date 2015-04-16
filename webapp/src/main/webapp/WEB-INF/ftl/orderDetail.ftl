<#import "template/template.ftl" as frame>
<@frame.html title="订单详细">
<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
 <div id="process" class="section4">
 
            		<#if orderview.task_name="世巴提交排期表" || orderview.task_name="北广录入排期表">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="世巴财务确认">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="支付">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="世巴初审"||orderview.task_name="北广终审">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="上播报告">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		<#elseif orderview.task_name="监播报告">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		<#else>
            		   <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		</#if>
            	</div>
							  <DIV class="color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单详情-${(orderview.longOrderId)!''}</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
 <LI style="width: 240px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(orderview.order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type!''}</SPAN></LI>
  <LI style="width: 200px;"><SPAN>订单状态：</SPAN><SPAN class="con">${orderview.task_name!''}</SPAN></LI>
</UL>
</DIV>
</DIV>
</@frame.html>






