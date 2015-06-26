<#import "template/template.ftl" as frame>
<#global menu="${typeStr}套餐列表">
<@frame.html title="${typeStr}套餐列表" left=false nav=false
js=["homepage/js/index.js","homepage/js/unslider.min.js"] css=["homepage/css/homepage.css",
"homepage/css/index.css","homepage/css/one.css"]>
<style type="text/css">
    .ls-10 {clear: both; float: none; margin: auto;width:960px;}
    .module1 .title span,.module2 .title span,.module3 .title span {
        font-size: 18px;
        width: 140px;
        height: 45px;
        line-height: 45px;
    }
</style>
<script type="text/javascript">
$(function() {
    $(".module1 .fn-left.pg-uplan-product-item").mouseenter(function(){
        $(this).css("border", "1px solid rgb(64, 155, 234)");
        $(this).children(".J_click_p").css("background-color","rgb(86, 170, 242)");
        $(this).find("a").css("color","rgb(255, 255, 255)");
    }).mouseleave(function(){
                $(this).removeAttr("style");
                $(this).children(".J_click_p").removeAttr("style");
                $(this).find("a").removeAttr("style");
            });
    $(".module2 .fn-left.pg-uplan-product-item").mouseenter(function(){
        $(this).css("border", "1px solid rgb(239, 122, 48)");
        $(this).children(".J_click_p").css("background-color","rgb(253, 110, 19)");
        $(this).find("a").css("color","rgb(255, 255, 255)");
    }).mouseleave(function(){
                $(this).removeAttr("style");
                $(this).children(".J_click_p").removeAttr("style");
                $(this).find("a").removeAttr("style");
            });
    $(".module3 .fn-left.pg-uplan-product-item").mouseenter(function(){
        $(this).css("border", "1px solid rgb(240, 194, 50)");
        $(this).children(".J_click_p").css("background-color","rgb(255, 205, 51)");
        $(this).find("a").css("color","rgb(255, 255, 255)");
    }).mouseleave(function(){
                $(this).removeAttr("style");
                $(this).children(".J_click_p").removeAttr("style");
                $(this).find("a").removeAttr("style");
            });

});
</script>
<div class="container">

<div class="module${type+1}">
    <div class="title s-clear">
			  	<span>
			  		${typeStr}广告套餐
			  	</span>
    </div>
    <div class="fn-clear pg-uplan-product-list text-big  mb10">
        <#list list as prod>
        <div class="fn-left pg-uplan-product-item mr15 mt15">
            <a href="${rc.contextPath}/product/d/${prod.id}">
                <dl class="bg-color-white">
                    <dt class="ub">${prod.name}</dt>
                    <dd>
                        <span class="mr20">曝光次数</span>
                        <span><em>${prod.playNumber}</em>/天</span>
                    </dd>
                    <dd>
                        <span class="mr20">展示期限</span>
                        <span>
                            <#if (prod.days % 30 == 0)>
                                <em>${prod.days/30}</em>月
                            <#elseif (prod.days % 7 == 0)>
                                <em>${prod.days/7}</em>周
                            <#else>
                                <em>${prod.days}</em>天
                            </#if>
                        </span>
                    </dd>
                    <dd>
                        <span class="mr20">金额</span>
                        <span><em><#setting number_format=",###">${prod.price}</em>元</span>
                    </dd>
                </dl>
            </a>
            <p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a href="${rc.contextPath}/order/iwant/${prod.id}">马上预定</a>
							</span>
                <a class="text" href="${rc.contextPath}/product/d/${prod.id}">（查看详情）</a>
            </p>
        </div>
        </#list>
    </div>
</div>
</div>

</@frame.html>