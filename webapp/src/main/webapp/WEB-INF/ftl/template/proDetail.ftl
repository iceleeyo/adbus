<#macro proDetail prod title="套餐详情" buyLink=false>
<div class="withdraw-wrap color-white-bg fn-clear">
    <H3 class="text-xl title-box"><A class="black" href="#">${title}-<span style="color: rgb(245, 135, 8);">[${prod.name!''}]</span></A></H3>
    <DIV class="summary mt10 uplan-summary-div">
        <UL class="uplan-detail-ul">
            <LI style="width: <#if buyLink>480px;<#else>720px;</#if>">
                <SPAN>套餐描述：</SPAN><SPAN class="con">${prod.name!''}</SPAN>
            </LI>
    <#if buyLink>
            <LI style="width: 240px;height:45px">
                <a class="block-btn" href="${rc.contextPath}/order/buypro/${prod.id}">购买</a>
            </LI>
    </#if>
            <LI style="width: 240px;">
                <SPAN><#if prod.type == 'body'>媒体费：<#else>套餐价格：</#if></SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}.00</SPAN>
                <SPAN>元</SPAN>
            </LI>
            <#if prod.type == 'body'>
                <LI style="width: 240px;">
                    <SPAN>制作费：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.produceCost!''}.00</SPAN>
                    <SPAN>元</SPAN>
                </LI>
            </#if>
            <LI style="width: 240px;">
                <SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN>
            </LI>
    <#if prod.type == 'body'>
        <LI style="width: 240px;">
            <SPAN>线路级别：</SPAN><SPAN class="con"><#if prod.lineLevel??>${prod.lineLevel.nameStr!''}</#if></SPAN>
        </LI>
        <LI style="width: 240px;">
            <SPAN>巴士数量：</SPAN><SPAN class="con">${prod.busNumber!''}</SPAN>
        </LI>
    </#if>

            <#if prod.type == 'video' || prod.type == 'image' || prod.type == 'info'>
            <LI style="width: 240px;">
                <SPAN>时长（秒）：</SPAN><SPAN class="con">${prod.duration!''}</SPAN>
            </LI>
            <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN>单日播放次数：</SPAN>
                <SPAN class="con">${prod.playNumber!''}</SPAN>
            </LI>
            </#if>
            <#if prod.type == 'video'>
            <LI style="width: 240px;">
                <SPAN>首播次数：</SPAN><SPAN class="con">${prod.firstNumber!''}</SPAN>
            </LI>
            <LI style="width: 240px;">
                <SPAN>末播次数：</SPAN><SPAN class="con">${prod.lastNumber!''}</SPAN>
            </LI>
             <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN>高峰时段占比：</SPAN><SPAN class="con">${prod.hotRatio!''}</SPAN>
            </LI>
            </#if>
            <LI style="width: 200px;">
                <SPAN><#if prod.type == 'video' || prod.type == 'image' || prod.type == 'info'>套餐播放天数：<#else>广告展示天数：</#if></SPAN>
                <SPAN class="con">${prod.days!''}天</SPAN>
            </LI>
             <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>

        </UL>
    </DIV>
</div>
</#macro>