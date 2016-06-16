<#import "../template/template.ftl" as frame> <#global menu="用户详情">
<@frame.html title="用户详情" js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

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

function go_back(){
	history.go(-1);
}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="save"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			用户详情
			<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
			<button type="button" onclick="go_back()" class="block-btn"
				style="margin-left: 20px;">返回</button>
		</div>
		<div class="withdrawInputs">
			<div class="inputs">
				<div class="ui-form-item">
					<label class="ui-label mt10">登录名:</label> <input
						readonly="readonly" class="ui-input" type="text" name="username"
						id="name" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.username!''}">


				</div>
				<p class="ui-term-placeholder"></p>
				<div class="ui-form-item">
					<label class="ui-label mt10">真实姓名:</label> <input
						readonly="readonly" class="ui-input" type="text" name="firstName"
						id="amounts" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.user.firstName!''}">
				</div>



				<div class="ui-form-item">
					<label class="ui-label mt10">邮箱地址:</label> <input
						readonly="readonly" class="ui-input" type="text" name="email"
						id="amounts" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.user.email!''}">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10">联系电话:</label> <input
						readonly="readonly" class="ui-input" type="text" name="phone"
						id="amounts" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.phone!''}">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10">所属组:</label> <#if
					userDetail.groups?exists> <#list userDetail.groups as item>
					${item.name!''}&nbsp;&nbsp; <#if item_index % 4 == 0 &&
					item_index!=0><br></#if> </#list> </#if>

				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10">所属公司:</label> <input
						readonly="readonly" class="ui-input" type="text" name="company"
						id="amounts" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.company!''}">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10">所属部门:</label> <input
						readonly="readonly" class="ui-input" type="text" name="department"
						id="amounts" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.department!''}">
				</div>

				<div class="ui-form-item"></div>

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
