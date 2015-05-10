<#import "../template/template.ftl" as frame> <#global menu="用户添加">
<@frame.html title="用户添加" js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js",
"js/jquery.datepicker.region.cn.js"] css=["js/jquery-ui/jquery-ui.css"]>

<script type="text/javascript">
	i = 2;
	j = 2;
	$(document)
			.ready(
					function() {

						$("#btn_add2")
								.click(
										function() {
											$("#newUpload2")
													.append(
															'<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;"/><input type="button" value="删除" style="margin-top:10px;" onclick="del_2('
																	+ j
																	+ ')"/></div>');
											j = j + 1;
										});

					});

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}


	function sub(){
		var num=0;
		var username = $("#username").val();
		var firstName = ($("#firstName").val());
		var password = ($("#password").val());
		var password2 = ($("#password2").val());
		var email=($("#email").val());
		var phone= ($("#phone").val());
		var roles=document.getElementsByName("roles");
		//var roles = new Array();
		var company= ($("#company").val());
		var department= ($("#department").val());
		if(username==""){
			jDialog.Alert("请填写登录名");
			return;
		}
		if(firstName==""){
			jDialog.Alert("请填写真实姓名");
			return;
		}
		if(password==""){
			jDialog.Alert("密码不能为空");
			return;
		}
		if(! (password == password2 )){
			jDialog.Alert("两次输入密码需一致");
			return;
		}
		if(email==""){
			jDialog.Alert("请填写邮箱");
			return;
		}
		if(phone==""){
			jDialog.Alert("请填写联系电话");
			return;
		}
		for(var i=0;i<roles.length;i++)
 		{
 			if(roles[i].checked)
  			num++; 
  			else
  			num=num;
  		}
 		if(num<=0){
  			jDialog.Alert("请选择至少一个分组");
  			return;
  		}
		
		if(company==""){
			jDialog.Alert("请填写所属公司");
			return;
		}
		if(department==""){
			jDialog.Alert("请填写所属部门");
			return;
		}
		$('#userForm2').ajaxForm(function(data) {
				if(data.error ==1){
					jDialog.Alert("用户添加成功");
				}else {
					jDialog.Alert(data.errorMessage);
				}
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/contract/list",
			   	clearTimeout(uptime);
						},2000)
		}).submit();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

<form data-name="withdraw" name="userForm2" id="userForm2"
	class="ui-form" method="post" action="save"
	enctype="multipart/form-data">
	<div class="withdraw-title fn-clear">
		用户添加信息录入
		<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
	</div>
	<div class="withdrawInputs">
		<div class="inputs">

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>登录名:</label>
				<input class="ui-input" type="text" name="username" id="username"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">


			</div>
			<p class="ui-term-placeholder"></p>
			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>真实姓名:</label>
				<input class="ui-input" type="text" name="firstName" id="firstName"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>密码:</label>
				<input class="ui-input" type="text" name="password" id="password"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>请确认密码:</label>
				<input class="ui-input" type="text" name="password2" id="password2"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>邮箱地址:</label>
				<input class="ui-input" type="text" name="email" id="email"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>联系电话:</label>
				<input class="ui-input" type="text" name="phone" id="phone"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>所属组:</label>
                <div>
                    <#if groupsList?exists> <#list groupsList?keys as vkey> <input
                            type="checkbox" value="${vkey}" name="roles" id="roles" />${groupsList[vkey]}
                    </#list> </#if>
                </div>
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>所属公司:</label>
				<input class="ui-input" type="text" name="company" id="company"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>所属部门:</label>
				<input class="ui-input" type="text" name="department"
					id="department" data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="">
			</div>
		</div>
		<div class="ui-form-item widthdrawBtBox">
			<input type="button" id="subWithdraw" class="block-btn"
				onclick="sub();" value="创建用户">
		</div>
	</div>
	<div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span> 温馨提示
		</div>
		<ol>
			<li>1.请输入正确的合同号及金额。</li>
			<li>2.如果您填写的信息不正确可能会导致下单失败。</li>
			<li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
		</ol>
	</div>
</form>
</div>
</@frame.html>
