<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/validation/jquery.validationEngine.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/validation/jquery.validationEngine-zh_CN.js"></script>
<title>公交媒体电商平台-用户登陆</title>
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/login.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/js/validation/validationEngine.jquery.css">
<style type="text/css">
.s-right {
	color: #009DEB;
}

.rg-content {
	margin-top: 5%;
}

.rg-content .info-submit {
	margin-top: 5%;
	margin-left: 15%;
}

.rg-content .rg-title {
	text-align: left;
}
</style>

<script type="text/javascript">
    function changeImg() {
        var imgSrc = $("#imgObj");
        var src = imgSrc.attr("src");
        imgSrc.attr("src", chgUrl(src));
    }
    //时间戳   
    //为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳   
    function chgUrl(url) {
        var timestamp = (new Date()).valueOf();
        url = url.substring(0, 17);
        if ((url.indexOf("&") >= 0)) {
            url = url + "×tamp=" + timestamp;
        } else {
            url = url + "?timestamp=" + timestamp;
        }
        return url;
    }
</script>


<body class="register">
	<div class="contain">
		<div class="rg-logo">
			<img src="index_img/logo3.png" width="350" height="40">
		</div>
		<div class="rg-content clearfix">
			<div class="rg-title">
				登录 <#if (reLoginMsg)?? > <font color="red" size="3">${(reLoginMsg)!''} </font>
				<#else> ${(reLoginMsg)!''} </#if>
			</div>
			<form name="loginForm" id="loginForm" action="login" method='POST'>
				<div class="field username-field">
					<label><i class="icon icon-username"></i></label> <input
						type="text" value="" class="rg-text" placeholder="注册帐号"
						name="username" id="username" />
				</div>
				<div class="field password-field">
					<label><i class="icon icon-password"></i></label> <input
						type="password" value=""
						class="rg-text validate[required,minSize[6],maxSize[20]]"
						placeholder="密码" name="password" id="password" />
				</div>
				
				<div class="field">
					<input
						type="text" value="" style=" margin-bottom: 25px;  padding-left: 5px;width:90px"
						class="rg-text"
						placeholder="验证码" name="code" id="code" />
						<img id="imgObj" alt="验证码" src="/code" />
						<a href="#" onclick="changeImg()">换一张</a>
				</div>

				<div class="login-item">
					<a class="s-right" style="padding-top: 20px;" href="${rc.contextPath}/user/find_pwd">忘记密码?</a>
				</div>

				<div class="info-submit">
					<span> <input type="submit"
						style="font-family: 'microsoft yahei';font-weight:600;letter-spacing:5px;height: 40px;font-size: 16px; cursor: pointer" id="register"
						class="btn-submit" value="登录">
					</span>
				</div>
			</form>
		</div>

		<div class="tipslog">
			<span>没有帐号? <a href="${rc.contextPath}/register"> 立即注册</a></span>
		</div>
	</div>

	<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
</body>
</html>