<#import "template/template.ftl" as frame> <#global menu="物料管理">
<#import "template/pickBuses.ftl" as pickBuses> <@frame.html
title="物料上传" js=["js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

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


	function sub2() {
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			   var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/supplies/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
		document.getElementById('subWithdraw').setAttribute('disabled',true);
		  $("#subWithdraw").css("background-color","#85A2AD");
	}

	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
	<form id="userForm2" name="userForm2"
		action="put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
		enctype="multipart/form-data" method="post"">
		<div class="withdraw-title fn-clear">
			<span>添加底片</span> <a class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);" onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs">
				<br>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>底片名称</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]"
						type="text" name="name" id="name" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						placeholder="支持中英文、数字、下划线">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>审核号</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]"
						type="text" name="seqNumber" id="seqNumber"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="支持中英文、数字、下划线">
				</div>
				<input type="hidden" name="suppliesType" value="0"> <input
					type="hidden" name="industryId" value="14">
				<div class="ui-form-item videoToggle">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>时长（秒）:</label>
					<select data-is="isAmount isEnough" id="duration" name="duration"
						autocomplete="off" disableautocomplete=""
						style="width: 173px; height: 38px;">
						<option value="5">5</option>
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="30">30</option>
					</select>
					<p class="ui-term-placeholder"></p>
				</div>
				<div class="ui-form-item widthdrawBtBox">
					<input type="button" id="subWithdraw" class="block-btn"
						onclick="sub2();" value="确定添加">
				</div>
			</div>
		</div>
	</form>
</div>
</@frame.html>
