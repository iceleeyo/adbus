<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="shortcut icon" href="./images/favicon.ico">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/sea.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/homepage.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/index.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/one.css">
  <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/city.css">
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-1.8.3.min.js"></script>
   <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.jcountdown.js"></script>
   <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.jcountdown.site.js"></script>
   <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer/layer.js"></script>
   	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
	<title>首页</title>
<script type="text/javascript">
    function logout(){
       window.location.href = "${rc.contextPath}/logout";
    }

    $(function() {
        $("#city_dropdown a:not(.selected)").click(function(){
            var cityName = $(this).parents("#ttbar-mycity")[0]?$(this).attr("data-id") : $("#ttbar-mycity a.selected").attr("data-id");
            var media = $(this).parents("#ttbar-media")[0]?$(this).attr("data-id") : $("#ttbar-media a.selected").attr("data-id");
            $.ajax({
                url : "${rc.contextPath}/f/city/select?c=" + cityName + "&m="+media,
                type : "POST",
                data: {},
                success : function(data) {
                    layer.msg("正在切换到："+ data.name + " " + data.mediaTypeName);
                    var uptime = window.setTimeout(function(){
                        window.location.reload();
                        clearTimeout(uptime);
                    },1000);
                }
            }, "text");
        });
    });
</script>
 

</head>

<body>
	<div class="header">
		<div class="pg-header-top" style="background:rgba(20, 118, 40, 0.88)"> 
					<div class="container-12 s-clear">
						<div class="grid-12 city-dropdown">
                            <ul class="fl">
<#--<@security.authorize access="isAuthenticated()">-->
                                <li class="dorpdown" id="ttbar-mycity">
                                    <div class="dt cw-icon ui-areamini-text-wrap" style="">
                                        <i class="ci-right"><s>◇</s></i>
                                        <#if city??>
                                            <span class="ui-areamini-text" data-id="${city.name}" title="${city.name}">${city.name}</span>
                                        <#else>
                                            <span class="ui-areamini-text" data-id="${cities[0].name!''}" title="${cities[0].name!''}">${cities[0].name!''}</span>
                                        </#if>
                                    </div>
                                    <div class="dd dorpdown-layer">
                                        <div class="dd-spacer"></div>
                                        <div class="ui-areamini-content-wrap" style="left: auto;">
                                            <div class="ui-areamini-content">
                                                <div class="ui-areamini-content-list" id="city_dropdown">
                                                    <#list cities as c>
                                                        <div class="item">
                                                            <a data-id="${c.name}" href="javascript:void(0)" <#if city?? && city.name == c.name>class="selected"</#if>>${c.name!''}</a>
                                                        </div>
                                                    </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="dorpdown" id="ttbar-media">
                                    <div class="dt cw-icon ui-areamini-text-wrap" style="">
                                        <i class="ci-right"><s>◇</s></i>
                                    <#if city??>
                                        <a class="selected ui-areamini-text" data-id="${city.mediaType}" title="${city.mediaType.typeName}">车身广告</a>
                                    <#else>
                                        <a class="selected ui-areamini-text" data-id="${cities[0].mediaType!''}" title="${cities[0].mediaType.typeName!''}">车身广告</a>
                                    </#if>
                                    </div>
                                  <#--  <div class="dd dorpdown-layer">
                                        <div class="dd-spacer"></div>
                                        <div class="ui-areamini-content-wrap" style="left: auto;">
                                            <div class="ui-areamini-content">
                                                <div class="ui-areamini-content-list" id="city_dropdown">
                                                <#list medias as m>
                                                  <#if m.mediaType=="body">
                                                    <div class="item">
                                                        <a data-id="${m.mediaType}" href="javascript:void(0)" <#if city?? && city.id == m.id> cityid="${city.id}" mid="${m.id}" class="selected"</#if>>车身广告</a>
                                                    </div>
                                                    </#if>
                                                </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>-->
<#--</@security.authorize>-->
                            </ul>
							<div class="s-left ml10">
								<a class="pg-nav-item" href="#" style="padding-top: 8px;">
									<i class="icon icon-app"></i>
									
								</a>
							</div>
							<div class="s-right s-clear">
								<span class="pg-nav-item s-left" style="padding:0;">您好，</span>
									<span>
                                        <@security.authorize access="isAuthenticated()">
                                        <a class="pg-nav-item s-left" href="${rc.contextPath}/order/myTask/1">
                                        	我的账户:
                                            (<@security.authentication property="principal.user.firstName" />
                                            <@security.authentication property="principal.user.lastName" />)
                                        </a>
                                        </@security.authorize>
                                        <@security.authorize access="! isAuthenticated()">
                                            <a class="pg-nav-item s-left" href="${rc.contextPath}/login">请登录</a>
                                        </@security.authorize>
                                    </span>
									<#--<span class="arrow-down"></span>-->
								<div class="pg-nav-dropdown" style="display: none;">
									<div class="pg-dropdown-box">
										<div class="dropdown-account s-clear">
											<div class="account-img-box s-left">
												<a href="">
													<img src="${rc.contextPath}/imgs/default-img-78.png">
												</a>
											</div>
											<div class="s-left">
												<div class="user-money-handle s-clear grgray-text">
													<span class="balance fsize-14 s-left mr10">账户余额</span>
													<span class="orange-text fsize-14 s-left">
														<em class="fsize-18">0.00</em>
													</span>
												</div>
												<div>
													<a class="s-left pg-btn pg-btn-green pg-btn-md mr4" href="#">充值</a>
													<a class="s-left pg-btn pg-btn-blue pg-btn-md" href="#">提现</a>
												</div>
											</div>
										</div>
										<div class="dropdown-bottom s-clear">
											<div class="dropdown-set s-left"><a class="is-line" href="">我的报表</a></div>
											<div class="dropdown-set s-left"><a class="is-line" href="">我的物料</a></div>
											<div class="dropdown-set s-left"><a href="">我的订单</a></div>
										</div>
									</div>
								</div>
                               
								<!--<a class="pg-nav-item s-left" href="#">
									<i class="icon-msg fsize-12">1</i>
									消息
								</a> -->
								<!--<a class="pg-nav-item s-left" href="#">帮助</a>
								<a class="pg-nav-item s-left" href="#">论坛</a>-->
								<a class="pg-nav-item s-left" href="${rc.contextPath}/message/all">消息<span id="msgNumber" class="layer-tips" style="color:#ff9966"></span></a>
								 <@security.authorize access="isAuthenticated()">
								<a href="javascript:;" class="pg-nav-item s-left" onclick="logout();">[退出]</a>
                                </@security.authorize>
							</div>
						</div>
					</div>
				</div>
 
		<div class="pg-header-main">
					<div class="container-12 s-clear">
						<div class="phmain-logo-b pg-left">
							<a class="phmain-logo" href="#"></a>
						</div>
						<div class="phmain-slogan-b pg-left ml20">
							<a class="phmain-slogan" href="${rc.contextPath}"></a>
						</div>
						<div class="phmain-nav-b pg-right">
							<ul class="pg-nav">
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="${rc.contextPath}/body">首页</a>
								</li>
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t pg-nav-hover" href="${rc.contextPath}/intro-video.html">
										<span>移动电视</span>
										<#--<span class="shdown"></span>-->
									</a>
									<ul class="pg-dropdown" style="display: none;">
										<li class="pg-dropdown-angle">
											<span></span>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">&nbsp;&nbsp;视频广告</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">&nbsp;&nbsp;图片广告</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">&nbsp;&nbsp;文字广告</a>
										</li>
									</ul>
								</li>
								
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="${rc.contextPath}/intro-txt.html">车身媒体</a>
								</li>
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t pg-nav-hover-us" href="${rc.contextPath}/intro-price.html">
										<span>产品促销</span>
										<#--<span class="shdown"></span>-->
									</a>
									<ul class="pg-dropdown pg-dropdown-us" style="display: none;">
										<li class="pg-dropdown-angle">
											<span></span>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">管理团队</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">最新动态</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">招贤纳士</a>
										</li>
									</ul>
								</li>
                                <li class="pg-nav-item s-left">
                                    <a class="pg-nav-item-t" href="${rc.contextPath}/intro-ywzn.html">业务指南</a>
                                </li>
                                <li class="pg-nav-item s-left">
                                    <a class="pg-nav-item-t" href="${rc.contextPath}/about-me.html">关于我们</a>
                                </li>

							</ul>
						</div>
					</div>
				</div>
	</div>
	<div class="container-12 maincontent">	
		<div class="broadcast s-clear">
			<!-- <div class="product-type items s-left">
				<div class="item">
					<span>城市列表</span>
					<ul class="item-dropdown" style="display:none;">
							<li class="pg-dropdown-li">
								<a class="pg-dropdown-a" href="">北京</a>
							</li>
							<li class="pg-dropdown-li">
								<a class="pg-dropdown-a" href="">上海</a>
							</li>
							<li class="pg-dropdown-li">
								<a class="pg-dropdown-a" href="">深圳</a>
							</li>
						</ul>
				</div>
				<div class="item">
					<span><a href="intro-video.html">视频广告</a></span>
				</div>
				<div class="item">
					<span><a href="intro-txt.html">Info图片</a></span>
				</div>
				<div class="item">
					<span>新业务介绍</span>
				</div>
				<div class="item">
					<span><a href="intro-price.html">广告刊例</a></span>
				</div>
				<div class="item">
					<span>代理商</span>
				</div>
				
				
			</div> -->
			<div class="vediobox items s-left">

				<div class="banner" id="b04">
				    <ul>
				        <li><img src="homepage/imgs/a01.png" alt="" width="1070" height="300" ></li>
				        <li><img src="homepage/imgs/a05.jpg" alt="" width="1070" height="300" ></li>
				        <li><img src="homepage/imgs/a04.jpg" alt="" width="1070" height="300" ></li>
				    </ul>
				    <a href="javascript:void(0);" class="unslider-arrow04 prev"><img class="arrow" id="al" src="homepage/imgs/arrowl.png" alt="prev" width="20" height="35"></a>
				    <a href="javascript:void(0);" class="unslider-arrow04 next"><img class="arrow" id="ar" src="homepage/imgs/arrowr.png" alt="next" width="20" height="37"></a>
				</div>

			</div>
			<div class="addition items s-left">
			
			</div>
		</div>
		<div class="container">
			
			<#if (auctionList?size>0) >
			<div class="module1">
			  <div class="title s-clear">
			  	<span>
			  		竞价广告
			  	</span>
			  	<a href="${rc.contextPath}/product/sift" class="s-right">更多》</a>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-middle mt15 mb10">
			    <#list auctionList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a href="product/c/${prod.id}">
							<dl class="bg-color-white">
							  <dt class="ub"><span>${substring(prod.product.name,0,13)}</span></dt>
							  <#if prod.product.type=="screen">
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.product.playNumber}</em>/天</span>
							  </dd>
							  <#elseif prod.product.type=="body">
							  <dd>
							  	<span class="mr20">巴士数量</span>                 
							  	<span><em>${prod.product.busNumber}</em>辆</span>
							  </dd>
					          </#if>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.product.days}</em>天</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">底价￥</span>                 
		  						<span><em>#{prod.saleprice!'' ;m2M2}</em></span>  
		  					 </dd>
		          			  <dd>	
		          			  
		          			  <#if (prod.startDate < .now  && prod.biddingDate > .now  ) > 
		  						<span class="mr20">截止</span>    
		  						<span id="c_${prod_index}"><em></em></span>  
		  						<script type="text/javascript">
			  						var dateTo="${prod.biddingDate?string("yyyy-MM-dd HH:mm:ss")}";
									 countDateSimple("c_${prod_index}",dateTo);
								 </script>
								<#elseif (prod.startDate > .now   ) > 
								<span class="mr10">距开拍</span>    
		  						<span id="c_${prod_index}"><em></em></span>  
		  						<script type="text/javascript">
			  						var dateTo="${prod.startDate?string("yyyy-MM-dd HH:mm:ss")}";
									 countDateSimple("c_${prod_index}",dateTo);
								 </script>
								<#elseif (prod.biddingDate < .now   ) > 
								<span class="mr20"><s>已结束</s></span>    
		  						<span id="c_${prod_index}"><s>${prod.startDate?string("yyyy-MM-dd HH:mm")}</s></span>  
								</#if>
								
								
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a href="product/c/${prod.id}">
								<#if (prod.startDate < .now  && prod.biddingDate > .now  ) > 
									我要出价
								<#elseif (prod.startDate > .now   ) > 
									等待开始
								<#elseif (prod.biddingDate < .now   ) > 
									竞价结束
								</#if>
								
								</a>
							</span>
							<a class="text" href="product/c/${prod.id}">（查看详情）</a>
						</p>
					</div> 
				 </#list>	          
				</div>
			</div>
			</#if>
			
			<#if (bodyList?size>0) >
			<div class="module1">
			  <div class="title s-clear">
			  	<span>
			  		车身广告
			  	</span>
			  	<a href="${rc.contextPath}/product/sift" class="s-right">更多》</a>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-middle mt15 mb10">
			    <#list bodyList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a target="_blank" href="order/iwant/${prod.id}"><!--product/d/-->
							<dl class="bg-color-white">
							  <dt class="ub"><span>${substring(prod.name,0,13)}</span></dt>
							  <dd>
							  	<span class="mr20">巴士数量</span>                 
							  	<span><em>${prod.busNumber}</em>辆</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.days}</em>天</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">金额￥</span>                 
		  						<span><em>#{prod.price!'' ;m2M2}</em></span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a target="_blank" href="order/iwant/${prod.id}">马上预定</a>
							</span>
							<a class="text" target="_blank" href="order/iwant/${prod.id}">（查看详情）</a>
						</p>
					</div>           
					 </#list>	      
				</div>
			</div>
			</#if>
			
			
			
			
			
			<#if (videoList?size>0) >
			<div class="module1">
			  <div class="title s-clear">
			  	<span>
			  		视频广告
			  	</span>
			  	<a href="${rc.contextPath}/product/sift" class="s-right">更多》</a>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-middle mt15 mb10">
			    <#list videoList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a target="_blank" href="order/iwant/${prod.id}"><!--product/d/-->
							<dl class="bg-color-white">
							  <dt class="ub"><span>${substring(prod.name,0,13)}</span></dt>
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.playNumber}</em>/天</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.days}</em>周</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">金额￥</span>                 
		  						<span><em>#{prod.price!'' ;m2M2}</em></span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a target="_blank" href="order/iwant/${prod.id}">马上预定</a>
							</span>
							<a class="text" target="_blank" href="order/iwant/${prod.id}">（查看详情）</a>
						</p>
					</div>           
					 </#list>	      
				</div>
			</div>
			</#if>
			
			<#if (imageList?size>0) >
			<div class="module2">
			  <div class="title s-clear">
			  	<span>
			  		图片广告
			  	</span>
			  	<a href="${rc.contextPath}/product/sift" class="s-right">更多》</a><!-- f/prod/list/image -->
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-big mt15 mb10">
			    <#list imageList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a target="_blank" href="order/iwant/${prod.id}">
							<dl class="bg-color-white">
							  <dt class="ub"><span>${substring(prod.name,0,13)}</span></dt>
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.playNumber}</em>/天</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.days}</em>周</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">金额￥</span>                 
		  						<span><em>#{prod.price!'' ;m2M2}</em></span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a target="_blank" href="order/iwant/${prod.id}">马上预定</a>
							</span>
							<a class="text" target="_blank" href="order/iwant/${prod.id}">（查看详情）</a>
						</p>
					</div>           
				 </#list>	 	     
				</div>
			</div>
			</#if>
			
			<#if (noteList?size>0) >
			<div class="module3">
			  <div class="title s-clear">
			  	<span>
			  		文字信息
			  	</span>
			  	<a href="${rc.contextPath}/product/sift" class="s-right">更多》</a><!-- -->
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-big mt15 mb10">
			   <#list noteList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a target="_blank" href="order/iwant/${prod.id}">
							<dl class="bg-color-white">
							  <dt class="ub"><span>${substring(prod.name,0,13)}</span></dt>
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.playNumber}</em>/天</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.days}</em>周</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">金额￥</span>                 
		  						<span><em>#{prod.price!'' ;m2M2}</em></span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a target="_blank" href="order/iwant/${prod.id}">马上预定</a>
							</span>
							<a class="text" target="_blank" href="order/iwant/${prod.id}">（查看详情）</a>
						</p>
					</div>           
					 </#list>      
				</div>
			</div>
			</#if>
		</div>
		<div class="qqbox">
			<dl>
				<dt>客服中心</dt>
				<dd>客服a:<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3271689631&site=qq&menu=yes"><img border="0" width="70" src="http://wpa.qq.com/pa?p=2:3271689631:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a></dd>
				<dd>客服b:<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2799461911&site=qq&menu=yes"><img border="0" width="70" src="http://wpa.qq.com/pa?p=2:2799461911:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a></dd>
				<dd>销售a:<a class="qq-default"></a></dd>
				<dd>销售b:<a class="qq-default"></a></dd>
				
			</dl>
		</div>
	</div>
	<div class="footer" style="background:rgba(81, 211, 137, 0.88);margin-top:250px">
		<div class="container-12 plr10">
			<div class="foot-copyright">
				<span class="foot-cr-link gray-text">© 2015 XXX All rights reserved</span>
				<span class="foot-cr-link gray-text has-border">北京世巴传媒有限公司</span>
				<span class="foot-cr-link gray-text has-border">
					<a class="gray-text" href="">京ICP证 100953号</a>
				</span>
				<span class="foot-cr-link gray-text has-border">京公网安备11010502020657</span>
				<span class="foot-cr-link gray-text has-border">京ICP备12025643号-1</span>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="homepage/js/jquery.js"></script>
	<script src="homepage/js/index.js"></script>
	<script src="homepage/js/unslider.min.js"></script>
	<script type="text/javascript">
		$(".module1 .fn-left.pg-uplan-product-item").mouseenter(function(){
			$(this).css("border", "1px solid rgb(64, 155, 234)");
			$(this).children(".J_click_p").css("background-color","rgb(86, 170, 242)");
			$(this).find("a").css("color","rgb(255, 255, 255)");
			console.log("success");
		}).mouseleave(function(){
			$(this).removeAttr("style");
			$(this).children(".J_click_p").removeAttr("style");
			$(this).find("a").removeAttr("style");
		});
		$(".module2 .fn-left.pg-uplan-product-item").mouseenter(function(){
			$(this).css("border", "1px solid rgb(239, 122, 48)");
			$(this).children(".J_click_p").css("background-color","rgb(253, 110, 19)");
			$(this).find("a").css("color","rgb(255, 255, 255)");
			console.log("success");
		}).mouseleave(function(){
			$(this).removeAttr("style");
			$(this).children(".J_click_p").removeAttr("style");
			$(this).find("a").removeAttr("style");
		});
		$(".module3 .fn-left.pg-uplan-product-item").mouseenter(function(){
			$(this).css("border", "1px solid rgb(240, 194, 50)");
			$(this).children(".J_click_p").css("background-color","rgb(255, 205, 51)");
			$(this).find("a").css("color","rgb(255, 255, 255)");
			console.log("success");
		}).mouseleave(function(){
			$(this).removeAttr("style");
			$(this).children(".J_click_p").removeAttr("style");
			$(this).find("a").removeAttr("style");
		});
		//鼠标移入移出事件
/*			   $(".select-city").mouseover(function(){
				    $(this).next().show();
				  });
			   $(".select-city").mouseout(function(){
				    $(".item-dropdown").next().hide();
				  });*/
			   $(".item-dropdown").mouseover(function(){
			    $(this).show();
			   });
			   $(".item-dropdown").mouseout(function(){
			    $(this).hide();
			   });

		//鼠标移入移出事件
			   $(".product-type .item span").mouseover(function(){
				    $(this).next().show();
				  });
			   $(".product-type .item span").mouseout(function(){
				    $(".select-dropdown").next().hide();
				  });
			   $(".select-dropdown").mouseover(function(){
			    $(this).show();
			   });
			   $(".select-dropdown").mouseout(function(){
			    $(this).hide();
			   });

		//走马灯图片事件
		$(document).ready(function(e) {
		    var unslider04 = $('#b04').unslider({
				dots: true
			}),
			data04 = unslider04.data('unslider');
			
			$('.unslider-arrow04').click(function() {
		        var fn = this.className.split(' ')[1];
		        data04[fn]();
		    });
		});
  function logout(){
       window.location.href = "${rc.contextPath}/logout";
    }

	</script>

</body>
<@security.authorize access="isAuthenticated()">
<script type="text/javascript">		
			$(document).ready(
					function() {
					   $.ajax({
							url : "${rc.contextPath}/message/unread",
							type : "GET",
							success : function(data) {
								var msgNumber = Number(data);
								if(msgNumber > 0){
									$("#msgNumber").html("["+data+"]");
									$("#msgNumber").attr("tip","您有["+data+"]个未读消息!"); 
									  bindLayerMouseOver();
								}	
							}
						}, "text");
					}
		);
</script>
</@security.authorize>
</html>
