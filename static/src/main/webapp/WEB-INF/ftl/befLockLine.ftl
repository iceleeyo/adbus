<#import "template/template.ftl" as frame> <#global menu="锁定线路">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="锁定线路" js=["js/jquery-ui/jquery-ui.js",
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
	$(document).ready(function() {

					});

	function sub(){
	 if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
	$.ajax({
	    url :"${rc.contextPath}/busselect/tolockline/"+$("#code").val(),
		type : "POST",
		data : {
		},
		success : function(data) {
		     if(data.left){
					window.location.href="${rc.contextPath}/busselect/to_lockLine/"+data.right;
				}else {
					jDialog.Alert("不存在该合同");
				}
		}
	}, "text");
	}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action=""
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>锁定线路</span> 
		</div>
		<div class="withdrawInputs">
			<div class="inputs">
				<p class="ui-term-placeholder"></p>
				<p class="ui-term-placeholder"></p>
				<p class="ui-term-placeholder"></p>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同编号:</label>
					<input class="ui-input validate[required]" type="text"
						name="code" id="code" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						value="">
				</div>
				<div class="ui-form-item widthdrawBtBox">
					<input type="button" id="subWithdraw" class="block-btn"
						onclick="sub();" value="确定">
				</div>
			</div>
		</div>

		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>1.请输入正确的合同编号。</li>
				<li>2.合同部的同事可能已经添加了线路批次，您可以再核对一下。</li>
				<li>3.如果有需要，您可以修改线路批次。</li>
			</ol>
		</div>
	</form>
</div>


</@frame.html>
