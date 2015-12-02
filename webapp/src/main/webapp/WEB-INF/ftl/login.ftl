<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>用户登录</title>
<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="index_css/login.css">
<script type="text/javascript">
$(document).ready(function() {
    if ($.cookie("rmbUser") == "true") {
        $("#ck_rmbUser").attr("checked", true);
        $("#username").val($.cookie("username"));
        $("#password").val($.cookie("password"));
    }
});

//记住用户名密码
function saveUser() {
    if ($("#ck_rmbUser").attr("checked")) {
        var str_username = $("#username").val();
        var str_password = $("#password").val();
        $.cookie("rmbUser", "true", {
            expires:7
        });
        //存储一个带7天期限的cookie
        $.cookie("username", str_username, {
            expires:7
        });
        $.cookie("password", str_password, {
            expires:7
        });
    } else {
        $.cookie("rmbUser", "false", {
            expire:-1
        });
        $.cookie("username", "", {
            expires:-1
        });
        $.cookie("password", "", {
            expires:-1
        });
    }
}
</script>
<body class="login">
	<div class="contain">
		<div class="rg-logo">
			<img src="index_img/login_logo.png" width="450" height="60">
		</div>
		<div class="lg-content">
			<div class="lg-box">
				<div class="lg-info">
					<form id='loginForm' style="margin-top: -50px;" name='loginForm'
						class="login-form" action="login" method='POST'>
						<br> <#if (SPRING_SECURITY_LAST_EXCEPTION.message)?? &&
						(SPRING_SECURITY_LAST_EXCEPTION.message)?index_of("Bad")!=-1> <font
							color="red" size="3">密码错误!</font> <#else>
						${(SPRING_SECURITY_LAST_EXCEPTION.message)!''} </#if> <#if
						(reLoginMsg)?? > <font color="red" size="3">
							${(reLoginMsg)!''} </font> <#else> ${(reLoginMsg)!''} </#if>

						<div class="login-tips mb10" style="display: none;">
							<span class="login-tip">密码不能为空</span>

						</div>
						<div class="info-item">
							<label>用户名</label> <input placeholder="请输入用户名" id="username"
								class="lg-input lg-username" type="text" name="username">
						</div>
						<div class="info-item">
							<label class="text-pwd"> <a class="lg-input lg-username"
								href="${rc.contextPath}/user/find_pwd">密码</a>
							</label> <input class="lg-input lg-password" type="password"
								placeholder="请输入密码" id="password" name="password">
						</div>
						<div class="login-item">
							<label for="ck_rmbUser" class="ipt-c"><input
								name="ck_rmbUser" id="ck_rmbUser" value="" type="checkbox" />记住登录状态</label>
							<a class="s-right" href="${rc.contextPath}/user/find_pwd">忘记密码?</a>
						</div>
						<p class="mt37"></p>

						<div class="text-center login-submit">
							<span class="btn-login"> <input type="submit" onclick="saveUser()" value="立即登录" />
							</span>
						</div>
						<div class="login-item p-center">
							<!-- <span>没有账号？</span> <a href="/register">免费注册</a> -->
							<a target="_black"
								href="http://down.tech.sina.com.cn/content/40975.html"><span
								style="color: #ff9966">浏览器推荐(谷歌浏览器Chrome)</span></a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>