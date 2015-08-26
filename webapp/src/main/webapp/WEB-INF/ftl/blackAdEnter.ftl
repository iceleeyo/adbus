<#import "template/template.ftl" as frame>
<#global menu="底片管理">
<@frame.html title="底片录入" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">
    $(document).ready(function() {
    var industryId=${(contractView.mainView.industryId)!''};
    var contractType='${(contractView.mainView.contractType)!''}';
    $("#industry option").each(function(){
     if($(this).val() == industryId){
     $(this).attr("selected", "selected");  
      }
    });
    $("#contractType option").each(function(){
      if($(this).val()==contractType){
      
     $(this).attr("selected", "selected");  
      }
    });
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
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/contract/blackAdlist"
			   	clearTimeout(uptime);
						},2000)
							
		}).submit();
	}

</script>
<script type="text/javascript">
        function bu(txtObj) {
            txtObj.value = Number(txtObj.value).toFixed(2);
        }
</script>

 
<div class="withdraw-wrap color-white-bg fn-clear">
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="${rc.contextPath}/contract/saveblackAd?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									底片录入
								</div>
								<div class="withdrawInputs">
									<div class="inputs">
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>底片名称:</label>
												<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
												type="text" name="adName" value=""
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>审核号:</label>
												<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
												type="text" name="seqNumber" value=""
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>
										</div>
							
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>时长:</label>
                                                    <input
												class="ui-input validate[required,integer,min[5],max[180]]"
                                                onkeyup="value=value.replace(/[^\d]/g,'')" value="15" name="duration"
												id="duration" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="5-180秒">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10">
											<span  class="ui-form-required">* </span>权重:
                                            </label>
                                            <select data-is="isAmount isEnough" id="sortNumber" name="sortNumber"
												autocomplete="off" disableautocomplete="" style="width:173px; height: 38px;">
                                            	<option value="1">1</option>
                                            	<option value="2">2</option>
                                            	<option value="3">3</option>
                                            	<option value="4">4</option>
                                            </select>
                                        </div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="保存">
									</div>
							</form>
</div>
						
</@frame.html>
