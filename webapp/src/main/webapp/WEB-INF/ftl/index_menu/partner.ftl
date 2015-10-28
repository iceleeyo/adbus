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

			<section class="background-five" style="height: 649px;">
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