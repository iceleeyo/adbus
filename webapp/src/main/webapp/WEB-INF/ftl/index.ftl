<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!doctype html>
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>公交媒体电商平台</title>
		<link rel="stylesheet" type="text/css" href="index_css/base.css">
		<link rel="stylesheet" type="text/css" href="index_css/header.css">
		<link rel="stylesheet" type="text/css" href="index_css/jack.css">
		<link rel="stylesheet" type="text/css" href="index_css/shibaHome.css">
		<link rel="stylesheet" type="text/css" href="index_css/sift.css">
		<link rel="stylesheet" type="text/css" href="index_css/index_sea.css">
		<script src="js/jquery.pagination.js"></script>
		<script type="text/javascript" language="javascript" src="js/layer/layer.js"></script>
    	<!--增加lay最新版本-->
		<script type="text/javascript" language="javascript" src="js/layer-v1.9.3/layer/layer.js"></script>
		<script type="text/javascript" language="javascript" src="js/layer.onload.js"></script>
		<script type="text/javascript" language="javascript" src="js/layer-v1.9.3/layer-site.js"></script>
	</head>
	<script type="text/javascript">
	        /**
	         * 查看购物车判断登陆状态
	         */
	        function tocard(pathurl){
	        	var lc=$("#lc").val();
	        	if(lc=="0"){
	        		window.location.href="/login";
	        	}
	        	if(lc=="1"){
	        		window.location.href=pathurl+"/toCard";
	        	}
	        }
		</script>	
			<link rel="stylesheet" type="text/css" href="index_css/line-spin-clockwise-fade.css">
    	<link rel="stylesheet" type="text/css" href="index_css/loaders.css" />
		<script type="text/javascript">

	       var _PageHeight = document.documentElement.clientHeight,
	            _PageWidth = document.documentElement.clientWidth;
	        var _LoadingTop = _PageHeight > 150 ? (_PageHeight - 150) / 2 : 0,
	            _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
	        var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:' + _PageHeight + 'px;top:0;background:#484848;opacity:1;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 50px; color: #fff; font-family:\'Microsoft YaHei\';text-align: center;"><div class="la-ball-spin-clockwise-fade-rotating la-3x"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>Loading......</div></div>';
	        document.write(_LoadingHtml)
	        document.onreadystatechange = completeLoading;
	        function completeLoading() {
	            if (document.readyState == "complete") {
	                var loadingMask = document.getElementById('loadingDiv');
	                loadingMask.parentNode.removeChild(loadingMask);
	            }
	        }
		</script>
	<body>
		<header>
				<div class="logo"></div>
				<nav class="menu">
					<ul class="list-line">
						<li class="active"><a href="jvascript:void(0)">首页</a></li>
						<li><a href="jvascript:void(0)">媒体产品</a></li>
						<li><a href="jvascript:void(0)">传播效果</a></li>
						<li><a href="/caseMore.html">案例欣赏</a></li>
						<li><a href="jvascript:void(0)">合作伙伴</a></li>
						<li><a href="jvascript:void(0)">关于我们</a></li>
					</ul>
					<div class="s-right s-clear">
					
								<span class="pg-nav-item s-left" style="padding:0;">您好，</span>
									<span>
                                        <@security.authorize access="isAuthenticated()">
                                        <#if medetype?? && medetype=="screen">
                                        <a class="pg-nav-item s-left" href="${rc.contextPath}/order/myTask/1">
                                        <#else>
                                           <a class="pg-nav-item s-left" href="${rc.contextPath}/busselect/myTask/1">
                                        </#if>
                                                                                                                                   我的账户:
                                            <@security.authentication property="principal.user.firstName" />
                                            <@security.authentication property="principal.user.lastName" />
                                        </a>
                                        </@security.authorize>
                                        <@security.authorize access="! isAuthenticated()">
                                            <a class="pg-nav-item s-left" href="${rc.contextPath}/logMini">请登录</a>
                                            <a class="pg-nav-item s-left" href="${rc.contextPath}/register">免费注册</a>
                                        </@security.authorize>
                                    </span>
									<#--<span class="arrow-down"></span>-->
								<!--<a class="pg-nav-item s-left" href="#">
									<i class="icon-msg fsize-12">1</i>
									消息
								</a> -->
								<!--<a class="pg-nav-item s-left" href="#">帮助</a>
								<a class="pg-nav-item s-left" href="#">论坛</a>-->
								<@security.authorize access="isAuthenticated()">
								<a class="pg-nav-item s-left" href="${rc.contextPath}/message/all">消息<span id="msgNumber" class="layer-tips" style="color:#ff9966"></span></a>
								<span class="pg-nav-item s-left">
								<a onclick="tocard('${rc.contextPath}');"><img class="shop_icon" alt="" src="../index_img/icon_cart.png">
								<span id="cardCount_top"  style="color:#ff9966" ></span>
								</a>
								</span>
								<@security.authorize access="isAuthenticated()"> <input
								type="hidden" id="lc" value="1" /> </@security.authorize>
								<@security.authorize access="! isAuthenticated()"> <input
								type="hidden" id="lc" value="0" /> </@security.authorize>
								<@security.authorize access="isAuthenticated()">
								<a href="javascript:;" class="pg-nav-item s-left" onclick="logout();">[退出]</a>
                                </@security.authorize>
                                </@security.authorize>
							</div>
				</nav>
			</header>
		<div id="content">
			
			<section class="background-one">
				<div class="slide-container">
					<div class="slide-back">
						<div class="slider view1">
							<div class="view-text">
								<div class="left"></div>
								<div class="right">
								  <div class="top">
								  	<span>专注投入          展望未来</span>
								  	<p class="spec"></p>
								  </div>
								  <div class="mid">
								  	专注于媒体销售，专业于户外传播
								  </div>
								  <div class="bot clearfix">
								  	<span>辉煌过去，只为更好未来</span>
								  	<p class="info"><a href="javascript: void(0)">more</a></p>
								  </div>
							  </div>
							</div>
						</div>
						<div class="slider view2">
							<div class="view-text">
								<div class="left"></div>
								<div class="right">
								  <div class="top">
								  	<span>销售控 只为你</span>
								  </div>
								  <div class="mid">
								  	<p class="spec"></p>
								  </div>
								  <div class="bot clearfix">
								  	<span>不是非要量身定做，只是为你想的更多，我们想，再上一个好案例。</span>
								  	<p class="info"><a href="javascript: void(0)">more</a></p>
								  </div>
							    </div>
							</div>
						</div>
						<div class="slider view3">
							<div class="view-text">
								<div class="left"></div>
								<div class="right">
								  <div class="top">
								  	<p class="spec"></p>
								  </div>
								  <div class="mid">
								  	<span>行动派 论能力</span>
								  </div>
								  <div class="bot clearfix">
								  	<span>我说--世界上没有最好的广告位，只有最好的广告销售员，掌握更多资源，为您垄断最优线路。</span>
								  	<p class="info"><a href="javascript: void(0)">more</a></p>
								  </div>
							    </div>
							</div>
						</div>
						<div class="slider view4">
							<div class="view-text">
								<div class="left"></div>
								<div class="right">
								  <div class="top">
								  	<span>先入为主 布局<em>未来</em></span>
								  </div>
								  <div class="mid">
								  	<p class="spec"></p>
								  </div>
								  <div class="bot clearfix">
								  	<span>全面布局车身与车内，抢占头等商机，精品位置资源，多种广告形式，覆盖全市，辐射全国。</span>
								  	<p class="info"><a href="javascript: void(0)">more</a></p>
								  </div>
							  </div>
							</div>
						</div>
					</div>
				</div>
				<div class="inner-container">
					<div class="navi">
						<a class="active"></a>
						<a></a>
						<a></a>
						<a></a>
					</div>
					<div class="cdown"></div>
				</div>
			</section>
			<section class="background-two">
				<div class="container">
					<div class="title-en">
					</div>
					<div class="title-zh">媒体产品</div>
					<ul class="items clearfix">
						<li class="item">
							<a href="secondLevelPageBus">
								<div class="ad ad1">
								<u class="cl"></u>
								<u class="cr"></u>
								<i></i>
								</div>
								<span>车身广告</span>
							</a>
						</li>
						<li class="item">
							<a href="secondLevelPage">
								<div class="ad ad2"><u class="cl"></u>
								<u class="cr"></u>
								<i></i></div>
								<span>移动视频</span>
							</a>
						</li>
						<li class="item">
							<a href="#">
								<div class="ad ad3"><u class="cl"></u>
								<u class="cr"></u>
								<i></i></div>
								<span>优惠组合</span>
							</a>
						</li>
						<li class="item">
							<a href="#">
								<div class="ad ad4"><u class="cl"></u>
								<u class="cr"></u>
								<i></i></div>
								<span>车内广告</span>
							</a>
						</li>
						<li class="item">
							<a href="#">
								<div class="ad ad5"><u class="cl"></u>
								<u class="cr"></u>
								<i></i></div>
								<span>站牌广告</span>
							</a>
						</li>
					</ul>
				</div>
			</section>
			<section class="background-three">
				<div class="container">
					<div class="title-en">
					</div>
					<div class="title-zh">传播效果</div>
					<div class="descrip">
						<p>公交媒体广告能有效针对商品的目标消费群体选择最适合的地点进行发布，从而直接击中目标消费者。公交媒体作为优势户外媒体之一，具有独特的传播优势。</p>
						<p>作为在城市生活中无处不在的公交车辆，每一处现在我们的身边，这种不经意间的信息传播是人们无法避免、不可抗拒觉广告媒体，在市区的大街小巷及周边乡镇往返穿梭，线路固定且较长，有突出反复诉求的效果，可增加广告受众的数量和接触频率。
					</div>
					<div class="dec"><p>第三方检测平台<p></div>
					<div class="plain">
						<a class="left"></a>
						<a class="right"></a>
					</div>
				</div>
			</section>
			<section class="background-four">
				<div class="container">
					<div class="title-en">
					</div>
					<div class="title-zh">案例欣赏</div>
					<div class="examples clearfix">
						<div class="example">
						  <div class="hover-top">
							<u></u>
						  </div>
								<img src="index_img/example1.png">
								<div class="desp">
									<span>车身广告</span>
									<p>窝窝团网站推广</p>
									<span>白色 简介 突出</span>
								</div>
								<div class="hover-m">
									<div class="hover-bottom"></div>
								</div>
							</div>
						<div class="example">
						<div class="hover-top">
								<u></u>
							</div>
							<img src="index_img/example2.png">
							<div class="desp">
								<span>站牌广告</span>
								<p>美汁源果粒橙产品推广</p>
								<span>直接 传播面广</span>
							</div>
							<div class="hover-m">
									<div class="hover-bottom"></div>
								</div>
						</div>
						<div class="example">
						<div class="hover-top">
								<u></u>
							</div>
							<img src="index_img/example3.png">
							<div class="desp">
								<span>车身广告</span>
								<p>蒙牛QQ星儿童牛奶产品推广</p>
								<span>醒目 直白 受众广</span>
							</div>
							<div class="hover-m">
									<div class="hover-bottom"></div>
								</div>
						</div>
					</div>
					<div class="more">MORE></div>
				</div>
			</section>
			<section class="background-five">
				<div class="container">
					<div class="title-en">
						
					</div>
					<div class="title-zh">合作伙伴</div>
					<ul class="adv">
						<li class="mobile"></li>
						<li class="mengniu"></li>
						<li class="lenovo"></li>
						<li class="jd"></li>
						<li class="suning"></li>
						<li class="apple"></li>
						<li class="weiqian"></li>
						<li class="unilever"></li>
						<li class="cocacola"></li>
						<li class="taobao"></li>
					</ul>
				</div>
			</section>
			<section class="background-six">
				<ul class="menu">
					<li class="active">思想</li>
					<li>关于</li>
					<li>联系</li>
					<li class="sl big"></li>
					<li class="sl small"></li>
				</ul>
				<div class="slide-container">
					<div class="slide-back">
						<div class="slider view1">
							<div class="view-text">
								<div class="right">
								  <div class="top">
								  	<h2>崇德尚能</h2>
								  </div>
								  <div class="mid">
								  	以德行立足于世，我们用最高的行业标准要求自己<br><br>
								  	用能力打造品牌，每一份产品都源自不断地构想与实践<br><br>
								  	漫漫征途，只为远方<br><br>
								  	不畏险阻，一路同行
								  </div>
							    </div>
							</div>
						</div>
						<div class="slider view2">
							<div class="view-text">
								  <div class="mid">
								  	北京世巴传媒有限公司（简称“世巴传媒”）成立于2014年4月17日，注册资本5000万，由北京巴士传媒股份有限公司和北京北广传媒移动电视有限公司合资成立。
    								<br><br>世巴传媒电商平台经营内容包括北京全市公交车载电视媒体、电子站牌、场站信息服务显示屏等，依托公交车载移动电视实现北京市区全覆盖的优势，推出栏目植入广告业务，为客户专门定制节目，为客户提供专业、优质的服务。
    								<br><br>平台遵循先进的O2O理念，大胆创新公交媒体营销模式，将互联网思维运用到传统交易中，将线上交易于线下体验相结合，更加方便用户使用，注重用户体验。

								  </div>
							</div>
						</div>
						<div class="slider view3">
							<div class="view-text">
								<div class="left">
									<img src="index_img/wp1_1.jpg" height="180" width="180">
								</div>
								<div class="right">
								  <h3>010-87899736</h3>
								  <div>地址：北京市海淀区紫竹院路32号</div>
								  <div>邮编：100048</div>
								  <div>网址：http://www.wbmedia.com.cn</div>
							  </div>
							</div>
						</div>
					</div>
			</section>
	  </div>
	  <div class="jack" style="height: 296px; top: 160.5px;">
        <ul class="icons">
        	<li class="up"><i></i></li>
            <li class="qq">
            	<i></i>
            </li>
            <li class="tel">
            	<i></i>
            </li>
            <li class="wechat">
            	<i></i>
            </li>
            <li class="down"><i></i></li>
        </ul>
        <a class="switch"></a>
      </div>
		<script type="text/javascript" src="index_js/unslider.min.js"></script>
		<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript" src="index_js/index.js"></script>
		<script type="text/javascript">
		/**
		 * 登出
		 */
		function logout(){
			window.location.href = "${rc.contextPath}/logout";
		}
		$(function(){
			var _winHeight = $(window).height(),
			    _winWidth = $(window).width();
			$('.background-one .slider').css({'height': _winHeight, 'width': _winWidth});
			$('.background-six .slider').css({'height': '240px', 'width': _winWidth});

			$("#content").switchPage({
				'loop' : true,
				'keyboard' : true,
				'direction' : 'vertical'
			});
			
			$('.background-one').slidePic(_winHeight, _winWidth);
			$('.background-six').slidePic(240, _winWidth);
		});
	</script>
	</body>
</html>