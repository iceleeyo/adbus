<#macro html title="" left=true nav=true css=[] js=[]> <#assign
security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="shortcut icon" href="${rc.contextPath}/images/favicon.ico">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${title!''}</title>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/platform.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/index.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/common.js"></script>
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/css/city.css">
<#list css as entry>
<link rel="stylesheet" type="text/css" href="${rc.contextPath}/${entry}">
</#list>
<style type="text/css">
.online-support {
	display: none;
	position: fixed;
	bottom: 20px;
	left: 20px;
	width: 80px;
	height: 32px;
}
</style>
</head>
<body>
	<div class="page-container">
		<!--上部DIV-->
		<#include "../menu/top.ftl" />
		<!--下部DIV-->
		<div class="page-container">
			<div class="pg-container-main">
				<div class="container-12 mt10 s-clear">
					<#if nav>
					<!--顶部导航开始-->
					<div class="container-12">
						<ul class="breadcrumb m11 s-clear">
							<li class="s-left fsize-16 bread-homep"><a class="gray-text"
								href="${rc.contextPath}/index.html">首页</a></li>
							<li class="s-left breadcrumb-right"></li>
							<li class="s-left bread-child"><a class="gray-text" href="#">${title!''}</a>
							</li>
						</ul>
					</div>
					<!--顶部导航结束-->
					</#if>
					<div class="ls-10">
						<#nested> <br>
					</div>
					<!--主体结束-->
				</div>
			</div>
		</div>
	</div>
	<div class="online-support">
		<a target="_blank"
			href="http://wpa.qq.com/msgrd?v=3&uin=3070339185&site=qq&menu=yes"><img
			border="0" src="http://wpa.qq.com/pa?p=3:3070339185:3" alt="点击这里咨询"
			title="点击这里咨询" /></a>
	</div>

	<div class="footer">
		<div class="container-12 plr10">
			<div class="foot-copyright">
				<span class="foot-cr-link gray-text">© 2015 XXX All rights
					reserved</span> <span class="foot-cr-link gray-text has-border">北京世巴传媒有限公司</span>
				<span class="foot-cr-link gray-text has-border"> <a
					class="gray-text" href="">京ICP证 100953号</a>
				</span> <span class="foot-cr-link gray-text has-border">京公网安备11010502020657</span>
				<span class="foot-cr-link gray-text has-border">京ICP备12025643号-1</span>
			</div>
		</div>
	</div>
</body>
</html>
</#macro>
