<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>用户登录</title>
		<link rel="stylesheet" type="text/css" href="index_css/login.css">
	<body class="login">
		<div class="contain">
			<div class="lg-logo">
				<img src="index_img/login_logo.png" width="450" height="60">
			</div>
			<div class="lg-content">
				<div class="lg-box">
					<div class="lg-info">
						<form id='loginForm' style="margin-top: -50px;" name='loginForm' class="login-form" action="login" method='POST'>
							<br>
                             <#if (SPRING_SECURITY_LAST_EXCEPTION.message)?? && (SPRING_SECURITY_LAST_EXCEPTION.message)?index_of("Bad")!=-1>
                             	<font color="red" size="3">密码错误!</font>
   								<#else>
   								${(SPRING_SECURITY_LAST_EXCEPTION.message)!''}
							</#if>
							
							<#if (reLoginMsg)?? >
                             	<font color="red" size="3">密码错误!</font>
                             	${session.removeAttribute("reLoginMsg")}
   								<#else>
   								${(reLoginMsg)!''}
							</#if>
                             
                                <div class="login-tips mb10" style="display : none;">
                                    <span class="login-tip">密码不能为空</span>

                                </div>
							<div class="info-item">
								<label>用户名</label>
								<input placeholder="请输入用户名" id="username" class="lg-input lg-username" type="text" name="username">
								<input type="checkbox" name="">
							</div>
							<div class="info-item">
								<label class="text-pwd">
								<a class="lg-input lg-username" href="${rc.contextPath}/user/find_pwd">密码</a>
								</label>
								<input class="lg-input lg-password" type="password" placeholder="请输入密码" id="password" name="password">
							</div>
							<div class="login-item"><a class="s-right" href="${rc.contextPath}/user/find_pwd">忘记密码?</a></div>
							<p class="mt37"></p>
							
							<div class="text-center login-submit">
								<span class="btn-login">
								<input type="submit" value="立即登录" />
								</span>
							</div>
							<div class="login-item p-center">
                                    <span>没有账号？</span>
                                    <a href="/register">免费注册</a>
                            </div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript">

		</script>
	</body>
</html>