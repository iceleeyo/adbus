<#import "template/template.ftl" as frame> 
<#import "template/select_lines.ftl" as select_lines>
<@frame.html title=""
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<script type="text/javascript">

</script>
<script type="text/javascript">
	$(document).ready(function() {
         $("#form002").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
        });
	});
	function SupContract(){
	if (!$("#form02").validationEngine('validateBeforeSubmit'))
            return;
	   $('#form02').ajaxForm(function(data) {
		  if(data.left){
		      document.getElementById('subutton').setAttribute('disabled',true);
		       $("#subutton").css("background-color","#85A2AD");
		      alert(data.right);
		      var uptime = window.setTimeout(function(){
				window.location.reload()
			   	clearTimeout(uptime);
						},2000)
		     }else{
		     alert(data.right);
		     }
		}).submit();
	}
</script>
<div class="color-white-bg fn-clear">

	<div id="relateSup">
<@select_lines.select_lines />	

		<div class="p20bs mt10 color-white-bg border-ec">
	
				<div class="withdraw-title fn-clear">
									合同详情录入
								</div>
				<form data-name="withdraw" name="form02" id="form02"
					class="ui-form" method="post"
					action="${rc.contextPath}/busselect/saveBodyContract"
					enctype="multipart/form-data">
					<input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
					<div class="withdrawInputs">
						<div class="inputs">
							<div class="ui-form-item">
								<label class="ui-label mt10"> <span
									class="ui-form-required">*</span>法人代表:
								</label> <input
									class="ui-input validate[required,custom[noSpecialContratNum],minSize[2],maxSize[120]]"
									type="text" name="legalman" id="code"
									value=""
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="" placeholder="中英文、数字、下划线">
							</div>
							<div class="ui-form-item">
								<label class="ui-label mt10"> <span
									class="ui-form-required">*</span>公司名称:
								</label> <input
									class="ui-input validate[required,custom[noSpecialContratNum],minSize[2],maxSize[120]]"
									type="text" name="company" id="code"
									value=""
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="" placeholder="中英文、数字、下划线">
							</div>
							<div class="ui-form-item">
								<label class="ui-label mt10"><span
									class="ui-form-required">*</span> 地址:
								</label> <input
									class="ui-input validate[required,custom[noSpecialContratNum],minSize[2],maxSize[120]]"
									type="text" name="companyAddr" id="code"
									value=""
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="" placeholder="中英文、数字、下划线">
							</div>
							<div class="ui-form-item">
								<label class="ui-label mt10"><span
									class="ui-form-required">*</span>联系人:</label> <input
									class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[120]]"
									type="text" name="relateMan" value="" 
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="">
								<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>

							</div>
							<div class="ui-form-item">
								<label class="ui-label mt10"><span
									class="ui-form-required">*</span>联系电话:</label> <input
									class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
									type="text" name="phoneNum" value=""
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="">
								<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>

							</div>
							<div class="ui-form-item">
								<label class="ui-label mt10">备注:</label> <textarea rows="4" cols="40" style="resize: none;" name="remark"></textarea>
							</div>
						</div>
					</div>

				</form>
		
			
			<div id="tb2">

				<p style="text-align: center; margin-top: 10px;">
					<button type="button"  id="subutton" onclick="SupContract()"
						class="block-btn">确认</button>
					<br> <br />
				</p>
			</div>
		</div>
		<br>
	</div>
</div>

</@frame.html>






