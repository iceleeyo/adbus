<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="content-type">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>公交广告交易系统登录</title>
        <script type="text/javascript" language="javascript" src="js/jquery.js"></script>
        <script type="text/javascript" language="javascript" src="js/querystring-0.9.0.js"></script>
        <style type="text/css">
            .center {margin: auto;}
            .frame {width: 600px;}
            .div {text-align:center; margin:25px;}
            span {display:inline-block; width: 50px; text-align: right; margin: 0 5px;}
            form {width: 100%; border: 0}
            #error {font-size: 12px; color: red;}
        </style>

      <link rel="stylesheet" type="text/css" href="css/sea.css">
      <link rel="stylesheet" type="text/css" href="css/login.css">
    </head>
    <body>
    <form name='loginForm'
          action="login" method='POST'>
        <div class="login-container">
			<div class="pg-header">
				<div class="pg-header-top">
				</div>
				<div class="pg-header-main">
					<div class="container-12 s-clear">
						<div class="phmain-logo-b pg-left">
							<a class="phmain-logo" href="#"></a>
						</div>
						<div class="phmain-slogan-b pg-left ml20">
							<a class="phmain-slogan" href="#"></a>
						</div>
						<div class="login-nav-right pg-right">
							<span>登录</span>
						</div>
					</div>
				</div>
			</div>
			<div class="pg-container">
				<div class="pg-container-main">
					<div class="container-12 mt10 s-clear">
						<div class="login-content s-clear">
							<div class="s-left login-left">
								<div class="login-box">
									<img src="images/rrdai-login.png">
								</div>
							</div>
							<div class="s-right login-right">
								<div class="login-info module">
									<form class="login-form" method="" action="">
									   <fieldset><br>
									   		<div class="login-tips mb10" style="display : none;">
									   			<span class="login-tip">密码不能为空</span>
									   		</div>
									   		<div class="login-item">
									   			<input class="login-input input-p gray-input" type="text" placeholder="请输入用户名" id="username" name="username">
									   			<span class="login-name-icon icon-position-user"></span>
									   		</div>
									   		<div class="login-item">
									   			<input class="login-input input-p gray-input" type="text" placeholder="请输入密码">
									   			<span class="login-name-icon icon-position-pwd"></span>
									   		</div>
									   		<div class="login-item s-clear">
									   			<span class="icon-select s-left icon-yes"></span>
									   			<label class="s-left">记住用户名</label>
									   			<a class="s-right" href="">忘记密码</a>
									   		</div>
									   		<div class="login-item p-center">
									   			<p class="mt37"></p>
									   			<a class="login-btn login-btn-size" href="#">立即登录</a>
									   		</div>
									   		<div class="login-item p-center">
									   			<span>没有账号？</span>
									   			<a href="#">免费注册</a>
									   		</div>
									   </fieldset>	
									</form>
									<div class="login-bottom p-center s-clear">
										<label class="s-left pl20">您还可以使用合作账号登录</label>
									
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
    </form>
    <script type="text/javascript">
        if ($.QueryString().error) {
            $("#error").html("用户名/密码错误")
        }
    </script>
    </body>
</html>