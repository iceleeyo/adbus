<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>媒体产品</title>
<link rel="stylesheet" type="text/css" href="index_css/base.css">
<link rel="stylesheet" type="text/css" href="index_css/header.css">
<link rel="stylesheet" type="text/css" href="index_css/jack.css">
<link rel="stylesheet" type="text/css" href="index_css/shibaHome.css">
<link rel="stylesheet" type="text/css" href="index_css/sift.css">
<link rel="stylesheet" type="text/css" href="index_css/index_sea.css">
</head>
<body>
	<header>
		<!-- 头部开始 -->
		<#include "/index_menu/index_top.ftl" />
		<!-- 头部结束 -->
	</header>
	<div id="content"
		style="transition: all 1000ms ease; -webkit-transition: all 1000ms ease; transform: translate3d(0px, 0px, 0px);">

		<section class="background-six" style="height: 649px;">
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
									以德行立足于世，我们用最高的行业标准要求自己<br> <br>
									用能力打造品牌，每一份产品都源自不断地构想与实践<br> <br> 漫漫征途，只为远方<br>
									<br> 不畏险阻，一路同行
								</div>
							</div>
						</div>
					</div>
					<div class="slider view2">
						<div class="view-text">
							<div class="mid">
								北京世巴传媒有限公司（简称“世巴传媒”）成立于2014年4月17日，注册资本5000万，由北京巴士传媒股份有限公司和北京北广传媒移动电视有限公司合资成立。
								<br> <br>世巴传媒电商平台经营内容包括北京全市公交车载电视媒体、电子站牌、场站信息服务显示屏等，依托公交车载移动电视实现北京市区全覆盖的优势，推出栏目植入广告业务，为客户专门定制节目，为客户提供专业、优质的服务。
								<br> <br>平台遵循先进的O2O理念，大胆创新公交媒体营销模式，将互联网思维运用到传统交易中，将线上交易于线下体验相结合，更加方便用户使用，注重用户体验。

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
	<#include "/template/custom_service.ftl" />	
	<script type="text/javascript"
		src="${rc.contextPath}/index_js/unslider.min.js"></script>
	<script type="text/javascript"
		src="${rc.contextPath}/index_js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript">
		$(function() {
			var _winHeight = $(window).height(), _winWidth = $(window).width();
			$('.background-one .slider').css({
				'height' : _winHeight,
				'width' : _winWidth
			});
			$('.background-six .slider').css({
				'height' : '240px',
				'width' : _winWidth
			});

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