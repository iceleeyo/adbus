<#import "template/template.ftl" as frame>
<@frame.html title="订单详细">
<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
<div id="process" class="section4">
 
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
           </div>
							  <DIV class="color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单详情-${longorderid!''}</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
  <LI style="width: 240px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>订单状态：</SPAN><SPAN class="con">已完成</SPAN></LI>
  <LI style="width: 240px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>合同号：</SPAN><SPAN class="con">${(orderview.order.contractCode)!''}</SPAN></LI>
  <LI style="width: 240px;">
  <table class="con" style="width: 748px;margin-top: 10px;">
  	<tr>
  		<td align="center">物料列表</th>
  	</tr>
  	<#list suppliesView.files as item> 
  	<tr>
  	<th>
		<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a>
	<#if prod.type==1>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
		<a href="${rc.contextPath}/upload_temp/${item.url!''}" target="_Blank">点击查看</a></#if>
 	</#list>${(suppliesView.mainView.infoContext)!''}
  	</th>
  	</tr>
  </table>
	
   </LI><p>
  <LI style="width: 240px;"><SPAN><a target="_blank" href="${rc.contextPath}/schedule/${order.id!''}">查看排期表</a></SPAN><SPAN class="con"></SPAN></LI>
</UL>
</DIV>
</DIV>
</@frame.html>






