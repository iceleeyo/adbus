<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="content-type">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>公交广告交易系统</title>
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
    </head>
    <body>
    <form name='loginForm'
          action="login" method='POST'>
        <div class="center frame">
            <div class="div" style="margin-top:75px">
                <caption><h2>公交广告交易系统</h2></caption>
            </div>
            <div class="div">
                <hr/>
            </div>
            <div id="error" class="div">

            </div>
            <div class="div">
                <span>用户名</span><input id="username" name="username">
            </div>
            <div class="div">
                <span>密码</span><input type="password" id="password" name="password">
            </div>
            <div class="div">
                <input name="submit" type="submit" value="登录" />
            </div>
            <input type="hidden"
                   name="${(_csrf.parameterName)!''}" value="${(_csrf.token)!''}" />
        </div>
    </form>
    <script type="text/javascript">
        if ($.QueryString().error) {
            $("#error").html("用户名/密码错误")
        }
    </script>
    </body>
</html>