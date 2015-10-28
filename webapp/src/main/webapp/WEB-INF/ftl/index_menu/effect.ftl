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
<link rel="stylesheet" type="text/css" href="index_css/sea.css">
</head>
<body>
	<header>
		<!-- 头部开始 -->
		<#include "/index_menu/index_top.ftl" />
		<!-- 头部结束 -->
	</header>
	<div id="content" style="transition: all 1000ms ease; -webkit-transition: all 1000ms ease; transform: translate3d(0px, 0px, 0px);">

		<section class="background-three active" style="height: 649px;">
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

	</div>
	<div class="jack" style="height: 296px; top: 160.5px;">
		<ul class="icons">
			<li class="up"><i></i></li>
			<li class="qq"><i></i></li>
			<li class="tel"><i></i></li>
			<li class="wechat"><i></i></li>
			<li class="down"><i></i></li>
		</ul>
		<a class="switch"></a>
	</div>
	<script type="text/javascript" src="index_js/unslider.min.js"></script>
	<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript">
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