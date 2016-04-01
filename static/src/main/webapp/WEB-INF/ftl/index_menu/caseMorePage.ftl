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

		<section class="background-four active" style="height: 649px;">
			<div class="container">
				<div class="title-en"></div>
				<div class="title-zh">案例欣赏</div>
				<div class="examples clearfix">
				<a href="caseMore1">
					<div class="example">
						<div class="hover-top">
							<u></u>
						</div>
						<img src="index_img/example1.jpg">
						<div class="desp">
							<span>移动视频广告</span>
							<p>58同城门户推广</p>
						</div>
						<div class="hover-m">
							<div class="hover-bottom"></div>
						</div>
					</div>
				</a>	
				<a href="caseMore2">
					<div class="example">
						<div class="hover-top">
							<u></u>
						</div>
						<img src="index_img/example2.jpg">
						<div class="desp">
							<span>移动视频广告</span>
							<p>立白好爸爸手洗洗衣露产品推广</p>
						</div>
						<div class="hover-m">
							<div class="hover-bottom"></div>
						</div>
					</div>
				</a>
				<a href="caseMore3">
					<div class="example">
						<div class="hover-top">
							<u></u>
						</div>
						<img src="index_img/example3.jpg">
						<div class="desp">
							<span>移动视频广告</span>
							<p>麦当劳产品推广</p>
						</div>
						<div class="hover-m">
							<div class="hover-bottom"></div>
						</div>
					</div>
				</a>
				</div>
				<div class="more">MORE&gt;</div>
			</div>
		</section>

	</div>
	<#include "/template/custom_service.ftl" />
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