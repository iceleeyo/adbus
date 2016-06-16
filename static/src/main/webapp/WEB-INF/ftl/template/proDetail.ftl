<#import "template_blank.ftl" as frame> <@frame.html title="套餐详细">
<#include "preview.ftl" />
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN">
<#if product??>
<div class="withdrawB-wrap color-white-bg fn-clear">
	<DIV class="summary mt10 uplan-summary-div">
		<UL class="uplan-detail-ul">
			<LI style="width: 480px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${product.name!''}</SPAN>
			</LI>
			<LI style="width: 240px;"><SPAN>套餐价格：</SPAN><SPAN class="con"
				style="color: rgb(245, 135, 8);"> #{(product.price)!'';m2M2}</SPAN>
				<SPAN>元</SPAN></LI>
			<LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${product.type.typeName!''}</SPAN>
			</LI> <#if product.type == 'video' || product.type == 'image' ||
			product.type == 'info'>
			<LI style="width: 240px;"><SPAN>时长（秒）：</SPAN><SPAN class="con">${product.duration!''}</SPAN>
			</LI>
			<LI style="width: 240px;"><SPAN>单日播放次数：</SPAN> <SPAN class="con">${product.playNumber!''}</SPAN>
			</LI> </#if> <#if product.type == 'video'>
			<LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN class="con">${product.firstNumber!''}</SPAN>
			</LI>
			<LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN class="con">${product.lastNumber!''}</SPAN>
			</LI>
			<LI style="width: 240px;"><SPAN>高峰时段占比：</SPAN><SPAN class="con">${product.hotRatio!''}</SPAN>
			</LI> </#if>
			<LI style="width: 240px;"><SPAN><#if product.type ==
					'video' || product.type == 'image' || product.type == 'info'>
					套餐播放天数： <#elseif product.type == 'team'>周期(天/期)： <#else>广告展示天数：
					</#if> </SPAN> <SPAN class="con">${product.days!''}天</SPAN></LI> <#if
			product.remarks?has_content>
			<LI style="width: 480px;"><SPAN>套餐描述：</SPAN><SPAN class="con">${substring(product.remarks!'',0,38)}</SPAN>
			</LI> </#if> <#if product.imgurl?has_content>
			<LI style="width: 480px;padding-top: 20px;"><SPAN>缩略图：</SPAN><SPAN class="con">
					<#if jsonView?? && jsonView.img1_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img1_url}" class="m11"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img2_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img2_url}" class="m11"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img3_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img3_url}" class="m11"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img4_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img4_url}" class="m11"
									height="65" width="65"></a></#if>
			</SPAN></LI> <#else>
			<LI style="width: 480px;padding-top: 20px;"><SPAN>缩略图：</SPAN><SPAN class="con">
					<img src="${rc.contextPath}/index_img/wp1_1.jpg" height="100" class="m11"
					width="100" style="padding-top: 15px;">
			</SPAN></LI> </#if>
		</UL>
	</DIV>
</div>
<#else>
<div class="withdrawB-wrap color-white-bg fn-clear">
	<DIV class="summary mt10 uplan-summary-div">
		<UL class="uplan-detail-ul">
			<LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${view.name!''}</SPAN>
			</LI>
			<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
			<LI style="width: 240px;"><SPAN> <#if view.type ==
					'body'>媒体费：<#else>套餐价格：</#if></SPAN><SPAN class="con"
				style="color: rgb(245, 135, 8);"> #{(view.price)!'';m2M2}</SPAN> <SPAN>元</SPAN>
			</LI> <#if view.type == 'body'>
			<LI style="width: 240px;"><SPAN>制作费：</SPAN><SPAN class="con"
				style="color: rgb(245, 135, 8);">
					#{(view.produceCost)!'';m2M2}</SPAN> <SPAN>元</SPAN></LI> </#if>
			<LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${view.type.typeName!''}</SPAN>
			</LI> <#if view.type == 'body'>
			<LI style="width: 240px;"><SPAN>广告展示天数：</SPAN><SPAN class="con">${view.days!''}</SPAN>
			</LI>

			<LI style="width: 240px;"><SPAN>线路级别：</SPAN><SPAN class="con"><#if
					view.lineLevel??>${view.lineLevel.nameStr!''}</#if></SPAN></LI>
			<LI style="width: 240px;"><SPAN>巴士数量：</SPAN><SPAN class="con">${view.busNumber!''}</SPAN>
			</LI> </#if> <#if view.type == 'video' || view.type == 'image' ||
			view.type == 'info'>
			<LI style="width: 240px;"><SPAN>时长（秒）：</SPAN><SPAN class="con">${view.duration!''}</SPAN>
			</LI>
			<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
			<LI style="width: 240px;"><SPAN>单日播放次数：</SPAN> <SPAN class="con">${view.playNumber!''}</SPAN>
			</LI> </#if> <#if view.type == 'video'>
			<LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN class="con">${view.firstNumber!''}</SPAN>
			</LI>
			<LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN class="con">${view.lastNumber!''}</SPAN>
			</LI>
			<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
			<LI style="width: 240px;"><SPAN>高峰时段占比：</SPAN><SPAN class="con">${view.hotRatio!''}</SPAN>
			</LI> </#if>
			<LI style="width: 200px;"><SPAN><#if view.type == 'video'
					|| view.type == 'image' || view.type == 'info'> 套餐播放天数： <#elseif
					view.type == 'team'>周期(天/期)： <#else>广告展示天数： </#if> </SPAN> <SPAN
				class="con">${view.days!''}天</SPAN></LI>
			<li style="width: 730; border-bottom: 1px solid #F7F7F7"></li> <#if
			view.remarks?has_content>
			<LI style="width: 720px;"><SPAN>套餐描述：</SPAN><SPAN class="con">${substring(view.remarks!'',0,38)}</SPAN>
			</LI> </#if> <#if view.imgurl?has_content>
			<LI style="width: 100%;padding-top: 20px;"><SPAN>缩略图：</SPAN><SPAN class="con">
					<#if jsonView?? && jsonView.img1_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img1_url}" class="m11"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img2_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img2_url}" class="m11"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img3_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img3_url}" class="m11"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img4_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img4_url}" class="m11"
									height="65" width="65"></a></#if>
			</SPAN></LI> <#else>
			<LI style="width: 100%;padding-top: 20px;"><SPAN>缩略图：</SPAN><SPAN class="con">
					<img src="${rc.contextPath}/index_img/wp1_1.jpg" height="100" class="m11"
					width="100" style="padding-top: 15px;">
			</SPAN></LI> </#if>
		</UL>
	</DIV>
</div>
</#if> </@frame.html>
