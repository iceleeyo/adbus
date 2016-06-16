<#import "../template/template.ftl" as frame> <#global menu="用户详情编辑">
<@frame.html title="用户详情编辑" js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
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
		var firstName = ($("#firstName").val());
		var email=($("#email").val());
		var phone= ($("#phone").val());
		var roles=document.getElementsByName("roles");
		var company= ($("#company").val());
		var department= ($("#department").val());
		if(firstName==""){
			jDialog.Alert("请填写真实姓名");
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
		
		if(company==""){
			jDialog.Alert("请填写所属公司");
			return;
		}
		if(department==""){
			jDialog.Alert("请填写所属部门");
			return;
		} 
		$('#userForm2').ajaxForm(function(data) {
						if(data.left==true){
					jDialog.Alert("保存成功");
				}else {
					jDialog.Alert(data.right);
				}
			    var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/user/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
	}
function go_back(){
	history.go(-1);
}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="update"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>用户信息编辑</span> <a class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);" onclick="go_back()">返回</a>
			<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
		</div>
		<div class="withdrawInputs">
			<div class="inputs">
				<input type="hidden" name="utype" value="${userDetail.utype}">
				<div class="ui-form-item">
					<label class="ui-label mt10">登录名:</label> <input
						readonly="readonly" class="ui-input" type="text" name="username"
						id="name" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.username!''}">


				</div>
				<p class="ui-term-placeholder"></p>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>真实姓名:</label>
					<input class="ui-input" type="text" name="firstName" id="firstName"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.user.firstName!''}">
				</div>



				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>邮箱地址:</label>
					<input class="ui-input" type="text" name="email" id="email"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.user.email!''}">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>联系电话:</label>
					<input class="ui-input validate[required,custom[phone]]"
						type="text" name="phone" id="phone" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="${userDetail.phone!''}">
				</div>
				<input type="hidden" name="ustats" value="${userDetail.ustats!''}">
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属组:</label>
					<#if UType =='body'>
					  
					<#if bdGroupsList?exists> <#list bdGroupsList as vkey> <input
						type="checkbox" value="${vkey.id}" name="roles"<#if
					uGroup?seq_contains(vkey.id)>checked </#if> /> ${vkey.name}
					&nbsp;&nbsp; <#if vkey_index % 4 == 0 && vkey_index!=0><br></#if>
					</#list> </#if> 
					
					   <#else>
					   
					  <#if groupsList?exists>
					   <#list groupsList?keys as vkey> <input type="checkbox" value="${vkey}"
						name="roles"<#if uGroup?seq_contains(vkey)>checked </#if>
					   /> ${groupsList[vkey]}&nbsp;&nbsp; <#if vkey_index % 4 == 0 &&
					   vkey_index!=0><br></#if> 
					  </#list> 
					  </#if>
					
					</#if>
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属公司:</label>
					<input class="ui-input" type="text" name="company" id="company"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.company!''}">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属部门:</label>
					<input class="ui-input" type="text" name="department"
						id="department" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.department!''}">
				</div>
			</div>
			<div class="ui-form-item widthdrawBtBox">
				<input type="button" id="subWithdraw" class="block-btn"
					onclick="sub();" value="确认修改">
				

			</div>
			<div class="worm-tips">
				<div class="tips-title">
					<span class="icon"></span> 温馨提示
				</div>
				<ol>
					<li>1.请输入正确的邮箱地址及联系电话。</li>
					<li>2.如果您填写的信息不正确可能会导致后续操作失败。</li>
					<li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
				</ol>
			</div>
	</form>
</div>
</@frame.html>
