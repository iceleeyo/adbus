<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="shortcut icon" href="./images/favicon.ico">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" type="text/css" href="homepage/css/sea.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/homepage.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/index.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/one.css">
  <title>首页</title>
</head>
<body>
	<div class="header">
		<div class="header-nav">
			<div class="container-12 s-clear">
				<div class="grid-12">
					<div class="s-left ml10" style="position:relative">
						<!-- <a class="header-nav-assist" href="#">
							<i class="icon icon-app"></i>
							当前城市：北京
						</a> -->
                      <!--  当前城市2-->
						<a class="select-city" href="#">
							<span> <!--北京2--></span>
						</a>
						<ul class="select-dropdown" style="display:none;">
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
					<div class="s-right s-clear">
						<a class="header-nav-item s-left" href="login">
							<span>登录</span>
						</a>
						<a class="header-nav-item s-left" href="register">
							<span>注册</span>
						</a>
						
					</div>
				</div>
			</div>
		</div>
		<div class="header-content">
			<div class="container-12 s-clear">
				<div class="header-logo s-left">
					<img src="homepage/imgs/logo1.png">
				</div>
				<div class="show-item s-right">
                    <a href="/" class="selected">首页</a>
					<a href="intro-video.html">移动电视</a>
					<a href="intro-txt.html">车身媒体</a>
					<a href="intro-price.html">产品促销</a>
					<a href="intro-ywzn.html">业务指南</a>
					<a href="about-me.html">关于我们</a>
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
				        <li><img src="homepage/imgs/sss.jpg" alt="" width="820" height="300" ></li>
				        <li><img src="homepage/imgs/ddd.jpg" alt="" width="820" height="300" ></li>
				        <li><img src="homepage/imgs/bbb.png" alt="" width="820" height="300" ></li>
				        <li><img src="homepage/imgs/ddd.png" alt="" width="820" height="300" ></li>
				        <li><img src="homepage/imgs/03.jpg" alt="" width="820" height="300" ></li>
				    </ul>
				    <a href="javascript:void(0);" class="unslider-arrow04 prev"><img class="arrow" id="al" src="homepage/imgs/arrowl.png" alt="prev" width="20" height="35"></a>
				    <a href="javascript:void(0);" class="unslider-arrow04 next"><img class="arrow" id="ar" src="homepage/imgs/arrowr.png" alt="next" width="20" height="37"></a>
				</div>

			</div>
			<div class="addition items s-left">
				<div class="b2">
					<div class="b3_1" style="height:170px;padding:0px">
						<div id="player" style="border:none;width:170px;padding:0px;">
							<p>你需安装<a href="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0">Adobe Flash Player 9.0</a>查看以下视频</p>
							<script type="text/javascript" src="http://www.bjmtv.com/templates/new/js/swfobject.js"></script>
							<script type="text/javascript">
								var s2 = new SWFObject('http://www.bjmtv.com/templates/new/swf/flvplayer.swf','mpl','222','160','9');
								s2.addParam('allowscriptaccess','always');
								s2.addParam('allowfullscreen','true');
								s2.addParam('menu','false');
								//是否自动播放
								s2.addParam('play','true');
								s2.addParam('wmode','opaque');
								s2.addVariable('width','216');
								s2.addVariable('height','165');
								s2.addVariable("backcolor","0x000000");
								s2.addVariable("frontcolor","0xCCCCCC");
								s2.addVariable("lightcolor","0x99CC33");
								s2.addVariable('file','rtmp://www.bjmtv.com:8000/live');
								s2.addVariable('id','live1');
								s2.addVariable('autostart','true');
								s2.write('player');
							</script>
						</div>	
					</div>	
			
				</div>
			
				<div class="item">
					<dl>
						<dd>公告1：电商平台试运行。</dd>
						<dd>公告2：电商平台试运行。</dd>
						<dd>公告3：电商平台试运行。</dd>
					</dl>
				</div>
			</div>
		</div>
		<div class="adds"><img src="homepage/imgs/321.jpg" alt="" width="960" height="100"></div>
		<div class="container">
			
			<#if (auctionList?size>0) >
			<div class="module1">
			  <div class="title s-clear">
			  	<span>
			  		竞价广告
			  	</span>
			  	<a href="f/prod/list/video" class="s-right">更多》</a>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-middle mt15 mb10">
			    <#list auctionList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a href="product/d/${prod.id}">
							<dl class="bg-color-white">
							  <dt class="ub"><span>${prod.product.name}</span>套餐</dt>
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.product.playNumber}</em>/天</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.product.days}</em>天</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">底价</span>                 
		  						<span><em>${prod.saleprice}</em>元</span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a href="product/c/${prod.id}">
								<#if (prod.startDate < .now  && prod.biddingDate > .now  ) > 
									我要出价
								</#if>
								<#if (prod.startDate > .now   ) > 
									等待开始
								</#if>
								<#if (prod.biddingDate < .now   ) > 
									竞价结束
								</#if>
								
								</a>
							</span>
							<a class="text" href="product/d/${prod.id}">（查看详情）</a>
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
			  	<a href="f/prod/list/video" class="s-right">更多》</a>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-middle mt15 mb10">
			    <#list videoList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a href="product/d/${prod.id}">
							<dl class="bg-color-white">
							  <dt class="ub"><span>${prod.name}</span>套餐</dt>
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.playNumber}</em>/天</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.days}</em>周</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">金额</span>                 
		  						<span><em>${prod.price}</em>元</span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a href="order/iwant/${prod.id}">马上预定</a>
							</span>
							<a class="text" href="product/d/${prod.id}">（查看详情）</a>
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
			  	<a href="f/prod/list/image" class="s-right">更多》</a>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-big mt15 mb10">
			    <#list imageList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a href="product/d/${prod.id}">
							<dl class="bg-color-white">
							  <dt class="ub"><span>${prod.name}</span>套餐</dt>
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.playNumber}</em>/天</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.days}</em>周</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">金额</span>                 
		  						<span><em>${prod.price}</em>元</span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a href="order/iwant/${prod.id}">马上预定</a>
							</span>
							<a class="text" href="product/d/${prod.id}">（查看详情）</a>
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
			  	<a href="f/prod/list/info" class="s-right">更多》</a>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-big mt15 mb10">
			   <#list noteList as prod>
				  <div class="fn-left pg-uplan-product-item mr15">
						<a href="product/d/${prod.id}">
							<dl class="bg-color-white">
							  <dt class="ub"><span>${prod.name}</span>套餐</dt>
							  <dd>
							  	<span class="mr20">曝光次数</span>                 
							  	<span><em>${prod.playNumber}</em>/天</span>
		            </dd>
		            <dd>
		            	<span class="mr20">展示期限</span>                 
		            	<span><em>${prod.days}</em>周</span>
		            </dd>
		  					<dd>
		  						<span class="mr20">金额</span>                 
		  						<span><em>${prod.price}</em>元</span>               
		  					</dd>
		  				</dl>
						</a>
						<p class="J_click_p" data="218">
							<span id="J_count_time_b" data2="0" data1="328261">
								<a href="order/iwant/${prod.id}">马上预定</a>
							</span>
							<a class="text" href="product/d/${prod.id}">（查看详情）</a>
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
	<div class="footer">
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
	</script>
</body>
</html>
