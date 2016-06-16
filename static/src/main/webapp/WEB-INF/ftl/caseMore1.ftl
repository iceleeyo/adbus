<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!doctype html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>案例赏析</title>
<link rel="stylesheet" type="text/css" href="index_css/base.css">
<link rel="stylesheet" type="text/css" href="index_css/jack.css">
<link rel="stylesheet" type="text/css" href="index_css/secondLevel.css">
<link rel="stylesheet" type="text/css" href="index_css/header.css">
</head>
<body>
	<header>
		<!-- 头部开始 -->
		<#include "/index_menu/index_top.ftl"/>
		<script src="index_js/sift_common.js"></script>
		<script src="index_js/sift_bus.js"></script>
		<!-- 头部结束 -->
	</header>
	<div class="content">
		<div class="side-nav">
			<div class="de-code">
				<img src="index_img/pic1.png" height="100" width="100">
			</div>
			<ul class="navibar">
				<li><a href="/">首页</a></li>
				<li><a href="/secondLevelPage">移动视频</a></li>
				<li><a href="/secondLevelPageBus">车身媒体</a></li>
				<li class="active"><a href="/caseMore">案例欣赏</a></li>
			</ul>
			<div class="markble">
				<p>世界在你脚下，巴士一路随行</p>
				<p>北京公交媒体</p>
			</div>
		</div>
		<div class="cover case">
			<div class="c-pagination">
				<a class="icon-left-arrow">&nbsp;</a> <a href="#"
					class="c-page c-more">MORE</a> <a href="#" class="c-page">PREV</a>
				<a href="#" class="c-page">NEXT</a>
			</div>
			<div class="case-info">
				<div class="c-desp">
					<div class="c-title">
						<span class="aline"></span> <span class="ver-ali">项目介绍</span>
					</div>
					<p class="casename">58同城门户推广</p>
					<div class="tags">
						<span class="title">案例标签</span> <span class="icon-tag"></span> <span
							class="tag">全屏</span> <span class="tag">32次</span> <span
							class="tag">15秒</span>
					</div>
				</div>
				<div class="c-desp">
					<div class="c-title">
						<span class="aline"></span> <span class="ver-ali">案例欣赏</span>
					</div>
				</div>
				<div class="c-contain">
					<img src="index_img/58tongcheng.jpg">
					<img src="index_img/21.png"> <img src="index_img/22.png">
					<img src="index_img/31.png"> <img src="index_img/32.png">
					<!-- <div class="c-head">
							<p class="head-en">MAJOR RESEARCH FINDINGS</p>
							<p class="head-zh">研究背景回顾</p>
							<p class="icon icon-line"></p>
						</div>
						<img src="index_img/case-2.png">
						<div class="c-head">
							<p class="head-en">BACKGROUND REVIEW</p>
							<p class="head-zh">主要研究发现</p>
							<p class="icon icon-line"></p>
						</div>
						<img src="index_img/case-3.png">
						<div class="c-head">
							<p class="head-en">DETAILED RESULTS</p>
							<p class="head-zh">详细研究结果</p>
							<p class="icon icon-line"></p>
							<p class="head-zh">广告发布效果</p>
						</div>
						<img src="index_img/case-4.png">
						<div class="c-md">
							<p class="head-zh">广告创意分析</p>
						</div>
						<img src="index_img/case-5.png">
						<div class="c-md">
							<p class="head-zh">品牌认知状况</p>
						</div>
						<img src="index_img/case-6.png"> -->
				</div>
			</div>
		</div>
	</div>

	<#include "/template/custom_service.ftl" />
	<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
	<script src="index_js/unslider.min.js"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>