<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/base.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/header.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/jack.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/js/jquery-ui/jquery-ui.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/css/jquery-ui-1.8.16.custom.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/secondLevel.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/sift.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/index_sea.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/pagination.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/css/layer.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/css/logMini.css">

</head>
<script type="text/javascript">
	function logout() {
		window.location.href = "${rc.contextPath}/logout";
	}
</script>
<style type="text/css">
.sift-list {
	width: 80px;
	line-height: 30px;
}

.sift-list a {
	margin-right: 10px;
}
</style>

<nav class="menu">
<ul class="list-line">
	<li><a href="/">首页</a></li>
	<li class="active"><a href="${rc.contextPath}/media">媒体产品</a></li>
	<li><a href="${rc.contextPath}/effect">传播效果</a></li>
	<li><a href="/caseMore.html">案例欣赏</a></li>
	<li><a href="${rc.contextPath}/partner">合作伙伴</a></li>
	<li><a href="${rc.contextPath}/aboutme">关于我们</a></li>
</ul>
<div class="s-right s-clear">
	<span class="pg-nav-item s-left" style="padding: 0;">您好，</span> <span>
		<@security.authorize access="isAuthenticated()"> <#if medetype?? &&
		medetype=="screen"> <a class="pg-nav-item s-left"
		href="${rc.contextPath}/order/myTask/1"> <#else> <a
			class="pg-nav-item s-left"
			href="${rc.contextPath}/busselect/myTask/1"> </#if> 我的账户:
				<@security.authentication property="principal.user.firstName" />
				<@security.authentication property="principal.user.lastName" /> </a>
			</@security.authorize> <@security.authorize access="!
			isAuthenticated()"> <a class="pg-nav-item s-left"
			href="${rc.contextPath}/login">请登录</a> <a class="pg-nav-item s-left"
			href="${rc.contextPath}/register">免费注册</a> </@security.authorize> 
	</span> <#--<span class="arrow-down"></span>--> <@security.authorize
	access="isAuthenticated()"> <a class="pg-nav-item s-left"
		href="${rc.contextPath}/message/all">消息<span id="msgNumber"
		class="layer-tips" style="color: #ff9966"></span></a> <span
		class="pg-nav-item s-left"> <a
		onclick="tocard('${rc.contextPath}');"><img class="shop_icon"
			alt="" src="${rc.contextPath}/index_img/icon_cart.png"> <span
			id="cardCount_top" style="color: #ff9966"></span> </a>
	</span> <@security.authorize access="isAuthenticated()"> <a
		href="javascript:;" class="pg-nav-item s-left" onclick="logout();">[退出]</a>
	</@security.authorize> </@security.authorize>
</div>
</nav>

<script type="text/javascript"
	src="${rc.contextPath}/index_js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/jquery.jcountdown.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/jquery.jcountdown.site.js"></script>
<script src="${rc.contextPath}/index_js/unslider.min.js"></script>

<script src="js/jquery.pagination.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/layer/layer.js"></script>
<!--增加lay最新版本-->
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/layer.onload.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>
<script type="text/javascript">
	//走马灯图片事件
	$(document).ready(
			function(e) {
				var unslider04 = $('#b04').unslider({
					dots : true
				}), data04 = unslider04.data('unslider');

				$('.unslider-arrow04').click(function() {
					var fn = this.className.split(' ')[1];
					data04[fn]();
				});

				//限时套装Hover事件
				$('.timer .select-item').hover(
						function(e) {
							/* Stuff to do when the mouse enters the element */
							e.preventDefault();
							$(this).find('.lasttime').css({
								'border-top' : '1px solid #ca0d0e',
								'border-right' : '1px solid #ca0d0e',
								'border-left' : '1px solid #ca0d0e'
							}).end().find('.cost-box').css({
								'border-right' : '1px solid #ca0d0e',
								'border-bottom' : '1px solid #ca0d0e',
								'border-left' : '1px solid #ca0d0e'
							}).end().find('.cost').css('border-bottom',
									'1px solid #ca0d0e').end()
									.find('.timeline').css('border-bottom',
											'1px solid #ca0d0e').end().find(
											'.ston').css({
										color : '#fff',
										background : '#ca0d0e'
									});
						},
						function(e) {
							/* Stuff to do when the mouse leaves the element */
							e.preventDefault();
							$(this).find('.lasttime').removeAttr('style').end()
									.find('.cost-box').removeAttr('style')
									.end().find('.cost').removeAttr('style')
									.end().find('.timeline')
									.removeAttr('style').end().find('.ston')
									.removeAttr('style');
						});
			});
</script>