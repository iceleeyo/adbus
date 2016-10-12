<#import "template/template.ftl" as frame> <#global menu="用户信息">
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
	
	function sub(){
	   if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
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
<div class="withdraw-wrap color-white-bg fn-clear">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="/user/u_edit/update"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>用户信息</span> <a class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);" onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs">
				<div class="ui-form-item">
					<label class="ui-label mt10">登录名:</label> <input
						readonly="readonly" class="ui-input" type="text" name="username"
						id="username" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.username!''}">
				</div>
				<p class="ui-term-placeholder"></p>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>真实姓名:</label>
					<input class="ui-input validate[required]" type="text"
						name="firstName" id="firstName" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="${userDetail.user.firstName!''}">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>邮箱地址:</label>
					<input class="ui-input validate[required,custom[email]]"
						type="text" name="email" id="email" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="${userDetail.user.email!''}">
				</div>
				
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>公司联系人:</label>
					<input class="ui-input validate[required]" type="text"
						name="relateman" id="relateman" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="${userDetail.relateman!''}">
				</div>
				
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>联系电话:</label>
					<input class="ui-input validate[required,custom[mobilephone]]"
						type="text" name="phone" id="phone" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="${userDetail.phone!''}">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属公司名称:</label>
					<input class="ui-input validate[required]" type="text"
						name="company" id="company" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="${userDetail.company!''}">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>公司地址:</label>
					<input class="ui-input validate[required]" type="text"
						name="companyAddr" id="companyAddr" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="${userDetail.companyAddr!''}">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>法定代表人:</label>
					<input class="ui-input validate[required]" type="text"
						name="legalman" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.legalman!''}">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">所属部门:</label> <input class="ui-input"
						type="text" name="department" id="department"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.department!''}">
				</div>
					<div class="ui-form-item">
					<label class="ui-label mt10">邮编:</label> <input class="ui-input"
						type="text" name="zipCode" id="zipCode"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="${userDetail.zipCode!''}">
				</div>
			</div>
			 <input type="hidden" name="isSuperUpdate" id="isSuperUpdate"
						 value="${isSuperUpdate!''}"/>
			<div class="widthdrawBtBox">
					<input type="button" id="subWithdraw" class="block-btn"
						onclick="sub();" value="保存">
			</div>
		</div>

		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>1.请选择合法的用户资质进行上传、审核通过(已认证)后可参与竞价商品的竞拍</li>
				<li>2.如果您填写的信息不正确可能会导致下单失败。</li>
				<li>3.您填写的公司名称一定要正确，后续的合同及发票都以此名称为准。</li>
			</ol>
		</div>
	</form>
</div>


</@frame.html>
