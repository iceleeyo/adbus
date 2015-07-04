<#import "template/template.ftl" as frame>
<#global menu="资质录入">
<@frame.html title="资质信息录入" js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js"] css=["js/jquery-ui/jquery-ui.css"]>
<script type="text/javascript">
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
	$(document).ready(function() {


					});

	function sub(){
		var username = $("#username").val();
		var firstName = ($("#firstName").val());
		var email=($("#email").val());
		var phone= ($("#phone").val());
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
		if(email==""){
			jDialog.Alert("请填写邮箱");
			return;
		}
		if(phone==''){
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
				window.location.reload();
			   	clearTimeout(uptime);
						},2000)
		}).submit();
	}
</script>
						
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="u_edit/update"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									用户信息
								</div>
	<div class="withdrawInputs">
		<div class="inputs">
			<div class="ui-form-item">
				<label class="ui-label mt10">登录名:</label> <input readonly="readonly"
					class="ui-input" type="text" name="username" id="username"
					data-is="isAmount isEnough" autocomplete="off"
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
				<input class="ui-input" type="text" name="phone" id="phone"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="" value="${userDetail.phone!''}">
			</div>
			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>所属公司:</label>
				<input class="ui-input" type="text" name="company" id="company"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="" value="${userDetail.company!''}">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>所属部门:</label>
				<input class="ui-input" type="text" name="department" id="department"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="" value="${userDetail.department!''}">
			</div>
			<div class="ui-form-item">
			    <label class="ui-label mt10">用户资质</label>
			    <#if attachment??>
			         <a href="${rc.contextPath}/downloadFile/${attachment.userId!''}/${attachment.id!''}"
		          onclick="return hs.expand(this)">
		        <img src="${rc.contextPath}/downloadFile/${attachment.userId!''}/${attachment.id!''}"
		             class="m11" width="240"/>
		    </a><br>
		    <label class="ui-label mt10">修改资质</label>
		    <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="user_pic" id="Sfile" class="">
												</div>
											</div>
			    <#else>
			    <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="user_pic" id="Sfile" class="">
												</div>
											</div>
			    </#if>
									  
	    	</div>
	    	<div class="ui-form-item" tip="上传资质图片,审核通过可以参与商品竞价!"> 
				<label class="ui-label mt10">认证状态:</label>
				<#if userDetail.ustats=="init">
					未认证
				    <#else>
				    已认证
				</#if>
			</div>
	    	<div class="ui-form-item widthdrawBtBox">
			<input type="button" id="subWithdraw" class="block-btn"
				onclick="sub();" value="保存">
		</div>
		</div>
		</div>
		
			<div class="worm-tips" >
            <div class="tips-title"><span class="icon"></span> 温馨提示</div>
	          <ol>
              <li>1.请选择合法的用户资质进行上传、审核通过(已认证)后可参与竞价商品的竞拍</li>
              <li>2.如果您填写的信息不正确可能会导致下单失败。 </li>
              <li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
	          </ol>
	        </div>
							</form>
</div>

    
</@frame.html>
