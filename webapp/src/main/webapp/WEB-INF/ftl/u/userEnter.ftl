<#import "../template/template.ftl" as frame> <#global menu="用户添加">
<@frame.html title="用户添加" js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js"]
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
            showOneMessage: true,
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        });
        
        $('.c').click(function() {
            var w = $(this).val();
            if(w=='screen'){
            	$("#c1").show();
            	$("#c2").hide();
            	$("#utype").val("screen")
            }else if(w=='body') {
            	$("#c2").show();
            	$("#c1").hide();
            	$("#utype").val("body")
            }
        });
        
        
    });
</script>

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
  			jDialog.Alert("请选择至少一个角色");
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
				
				var t='${UType}';
				if(t=='body'){
					var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/user/bodyuserlist",
			   	clearTimeout(uptime);
						},2000)
			 }else {
			 	var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/user/list",
				   	clearTimeout(uptime);
							},2000) 
			 
			 }		
		}).submit();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="save"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>用户添加信息录入</span> <input type="hidden" name="utype" id="utype"
				value="screen"> <a class="block-btn"
				style="margin-top: -5px;" href="javascript:void(0);"
				onclick="go_back()">返回</a>
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
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],ajax[ajaxUserCall]]"
						type="text" name="username" id="username"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="6-12位中英文、数字、下划线">


				</div>
				<p class="ui-term-placeholder"></p>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>真实姓名:</label>
					<input
						class="ui-input  validate[required,custom[chinese],minSize[2],maxSize[12],ajax[ajaxUserCall]]"
						type="text" name="firstName" id="firstName"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="2-12位中英文">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>密码:</label>
					<input class="ui-input validate[required,minSize[6],maxSize[20]]"
						type="password" name="password" id="password"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="请输入6-20位密码">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>请确认密码:</label>
					<input class="ui-input validate[required,equals[password]]"
						type="password" data-is="isAmount isEnough" type="text"
						name="password2" id="password2" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="请再输入一次密码">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>邮箱地址:</label>
					<input class="ui-input validate[required,custom[email]]"
						type="text" name="email" id="email" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="请输入邮箱地址">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>联系电话:</label>
					<input class="ui-input validate[required,custom[phone]]"
						type="text" name="phone" id="phone" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="请输入联系电话">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>
						角色选择: </label>
					<div>
					<#if UType?? && UType=="body">
						 <span id="c2" > <#if
							bdGroupsList?exists> <#list bdGroupsList as vkey> <input
							type="checkbox" value="${vkey.id}" name="roles" id="roles" />${vkey.name}
							&nbsp;&nbsp; <#if vkey_index % 4 == 0 && vkey_index!=0> <br>
							</#if> </#list> </#if>
						</span>
						<#else>
						<span id="c1"> <#if groupsList?exists> <#list
							groupsList?keys as vkey> <input type="checkbox" value="${vkey}"
							name="roles" id="roles" />${groupsList[vkey]} &nbsp;&nbsp; <#if
							vkey_index % 4 == 0 && vkey_index!=0> <br> </#if> </#list>
							</#if>
						</span>
                   </#if>
					</div>
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属公司:</label>
					<input class="ui-input validate[required]" type="text"
						name="company" id="company" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="请输入所属公司">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属部门:</label>
					<input class="ui-input validate[required]" type="text"
						name="department" id="department" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="请输入所属部门">
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
				<li>1.请输入正确的姓名、电话及公司信息。</li>
				<li>2.请赋予相应的员工权限。</li>
				<li>3.订单管理员 合同管理 可以下订单 定义产品 并在上播报告阶段做上播处理。</li>
				<li>4.排期管理  录入排期表[录入排单条到上播系统] 功能属于北广。</li>
				<li>5.物料初审 具有 初审 生成排期表 提交排期表 3项功能。</li>
				<li>6.终审 北广终审物料产生物料唯一号。</li>
			</ol>
		</div>
	</form>
</div>
</@frame.html>
