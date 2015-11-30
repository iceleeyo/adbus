<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>用户注册</title>
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/js/jquery-ui/jquery-ui.min.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/index_css/login.css">
<link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/js/validation/validationEngine.jquery.css">
<body class="register">
	<div class="contain">
		<div class="rg-logo">
			<img src="index_img/logo-screen.jpg" width="160" height="20">
		</div>
		<div class="rg-content clearfix">
			<div class="rg-title">用户注册</div>
			<form data-name="withdraw" name="userForm2" id="userForm2"
				method="post" action="doRegister" enctype="multipart/form-data">
				<div class="field username-field">
					<label><i class="icon icon-username"></i></label> <input
						type="text" value=""
						class="rg-text validate[required,custom[noSpecialLetterChinese],minSize[6],maxSize[12],ajax[ajaxUserCall]]"
						placeholder="用户名" name="username" id="username"
						data-is="isAmount isEnough" autocomplete="off" /> <i>*</i>
				</div>
				<div class="field password-field">
					<label><i class="icon icon-password"></i></label> <input
						type="password" value=""
						class="rg-text validate[required,minSize[6],maxSize[20]]"
						placeholder="密码" name="password" id="password"
						data-is="isAmount isEnough" autocomplete="off" /> <i>*</i>
				</div>
				<div class="field pwdagain-field">
					<label><i class="icon icon-pwdagain"></i></label> <input
						type="password" value=""
						class="rg-text validate[required,equals[password]]"
						data-is="isAmount isEnough" autocomplete="off" placeholder="确认密码" />
					<i>*</i>
				</div>
				<div class="field contact-field">
					<label><i class="icon icon-contact"></i></label> <input type="text"
						name="relateman" id="relateman" value=""
						class="rg-text validate[required,custom[chinese],minSize[2],maxSize[12]]"
						placeholder="联系人" data-is="isAmount isEnough" autocomplete="off" />
					<i>*</i>
				</div>
				<div class="field qq-field">
					<label><i class="icon icon-qq"></i></label> <input type="text"
						name="email" value=""
						class="rg-text validate[required,custom[email]]"
						placeholder="邮箱地址"> <i>*</i>
				</div>
				<div class="field company-field">
					<label><i class="icon icon-company"></i></label> <input type="text"
						name="company" id="company" value=""
						class="rg-text validate[required]" placeholder="公司名称"> <i>*</i>
				</div>
				<div class="field tel-field">
					<label><i class="icon icon-tel"></i></label> <input type="text"
						value="" name="phone" id="phone"
						class="rg-text validate[required,custom[mobilephone]]"
						placeholder="联系电话"> <i>*</i>
				</div>

				<div class="agreement">
					<input type="checkbox" id="agreement" value=""> <label
						class="verify">同意<a href="#">《免责条款》</a></label>
				</div>

				<div class="info-submit">
					<span> <input type="button"
						style="font-family: 'microsoft yahei';font-weight:600;letter-spacing:5px;margin-top: 10px; cursor: pointer;heigth:40px;font-size:14px;" id="register"
						class="btn-submit" onclick="sub();" value="注册用户">
					</span>
				</div>
			</form>
		</div>

		<div class="tips">
			<span>已有帐号? <a href="${rc.contextPath}/login"> 立即登录</a></span>
		</div>
	</div>

	<script type="text/javascript"
		src="${rc.contextPath}/index_js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/jquery-ui/jquery-ui.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/jquery.form.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/jquery.validate.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/validation/jquery.validationEngine.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/validation/jquery.validationEngine-zh_CN.js"></script>
	<!--增加lay最新版本-->
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/layer.onload.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>
	<script type="text/javascript">
    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        });
    });
</script>

	<script type="text/javascript">
function sub(){
    if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
        return;
    if (!$("#agreement").is(":checked")) {
        layer.msg("请同意免责条款");
        return;
    }
    $('#userForm2').ajaxForm(function(data) {
        if(data.user!=null){
           layer.msg("注册成功,请注意查收邮件进行激活!");
        }
       // var uptime = window.setTimeout(function(){
         //   window.location.href="${rc.contextPath}/order/myTask/1";
           // clearTimeout(uptime);
        //},2000);
    }).submit();
}
</script>
</body>
</html>