<#import "template_blank.ftl" as frame>
<@frame.html title="套餐详细">
<#include "preview.ftl" />
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN"> 
<div class="withdraw-wrap color-white-bg fn-clear" style="margin-right: -30px;">
    
    <DIV class="summary mt10 uplan-summary-div">
        <UL class="uplan-detail-ul">
            <LI style="width:720px;">
                <SPAN>套餐名称：</SPAN><SPAN class="con">${view.name!''}</SPAN>
            </LI>
    <li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN><#if view.type == 'body'>媒体费：<#else>套餐价格：</#if></SPAN><SPAN class="con" style="color: rgb(245, 135, 8);"> #{(view.price)!'';m2M2}</SPAN>
                <SPAN>元</SPAN>
            </LI>
            <#if view.type == 'body'>
                <LI style="width: 240px;">
                    <SPAN>制作费：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);"> #{(view.produceCost)!'';m2M2}</SPAN>
                    <SPAN>元</SPAN>
                </LI>
            </#if>
            <LI style="width: 240px;">
                <SPAN>媒体类型：</SPAN><SPAN class="con">${view.type.typeName!''}</SPAN>
            </LI>
    <#if view.type == 'body'>
    	<LI style="width: 240px;">
            <SPAN>广告展示天数：</SPAN><SPAN class="con">${view.days!''}</SPAN>
        </LI>
        
        <LI style="width: 240px;">
            <SPAN>线路级别：</SPAN><SPAN class="con"><#if view.lineLevel??>${view.lineLevel.nameStr!''}</#if></SPAN>
        </LI>
        <LI style="width: 240px;">
            <SPAN>巴士数量：</SPAN><SPAN class="con">${view.busNumber!''}</SPAN>
        </LI>
    </#if>

            <#if view.type == 'video' || view.type == 'image' || view.type == 'info'>
            <LI style="width: 240px;">
                <SPAN>时长（秒）：</SPAN><SPAN class="con">${view.duration!''}</SPAN>
            </LI>
            <li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN>单日播放次数：</SPAN>
                <SPAN class="con">${view.playNumber!''}</SPAN>
            </LI>
            </#if>
            <#if view.type == 'video'>
            <LI style="width: 240px;">
                <SPAN>首播次数：</SPAN><SPAN class="con">${view.firstNumber!''}</SPAN>
            </LI>
            <LI style="width: 240px;">
                <SPAN>末播次数：</SPAN><SPAN class="con">${view.lastNumber!''}</SPAN>
            </LI>
             <li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN>高峰时段占比：</SPAN><SPAN class="con">${view.hotRatio!''}</SPAN>
            </LI>
            </#if>
            <LI style="width: 200px;">
                <SPAN><#if view.type == 'video' || view.type == 'image' || view.type == 'info'>
                	套餐播放天数：
                <#elseif view.type == 'team'>周期(天/期)：
                <#else>广告展示天数：
                </#if>
                </SPAN>
                <SPAN class="con">${view.days!''}天</SPAN>
            </LI>
            <li style="width: 730; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width:720px;">
                <SPAN>套餐描述：</SPAN><SPAN class="con">${substring(view.remarks!'',0,38)}</SPAN>
            </LI>
             

        </UL>
    </DIV>
</div>
</@frame.html>