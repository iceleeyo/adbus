<#import "template/template.ftl" as frame> <#global menu="修改密码">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="用户信息" js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js"]
css=["js/jquery-ui/jquery-ui.css"]>
<script type="text/javascript">
function go_back(){
	history.go(-1);
}
    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        });
    });
</script>
<script type="text/javascript">
	
	function changePwd(){
	if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
	var password1=$("#password1").val();
	var password2=$("#password2").val();
	var oldpassword=$("#oldpassword").val();
	var userId=$("#userId").val();
	if(password2 !=password1){
		jDialog.Alert("两次密码输入不一样");
		return;
	}
	$.ajax({
		url : "${rc.contextPath}/user/change_pwd",
		type : "POST",
		data : {"userId":userId,"psw":password1,"oldpassword":oldpassword},
		success : function(data) {
				jDialog.Alert( data.right );
				var uptime = window.setTimeout(function(){
				window.location.reload();
			   	clearTimeout(uptime);
						},2000)
		}
	}, "text");
	
}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>修改密码</span> <a class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);" onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs">
				<div class="ui-form-item">
					<label class="ui-label mt10">登录名:</label> <input
						readonly="readonly" class="ui-input" type="text" id="userId"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userid!''}">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>原密码:</label>
					<input class="ui-input validate[required]" type="password"
						id="oldpassword" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>新密码:</label>
					<input class="ui-input validate[required]" type="password"
						id="password1" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>确认新密码:</label>
					<input class="ui-input validate[required]" type="password"
						id="password2" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="">
				</div>

				<div class="ui-form-item widthdrawBtBox">
					<input type="button" id="subWithdraw" class="block-btn"
						onclick="changePwd();" value="保存">
				</div>
			</div>
		</div>

	</form>
</div>


</@frame.html>
