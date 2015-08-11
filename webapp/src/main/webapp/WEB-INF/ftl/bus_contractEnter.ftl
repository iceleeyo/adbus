<#import "template/template.ftl" as frame>
<#global menu="合同管理">
<@frame.html title="车辆关联合同" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            maxErrorsPerField: 1,
        });
        
    });
</script>
<script type="text/javascript">
	function sub(){
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
		var plateNumber = ($("#plateNumber").val());
		var startDate = $("#startDate").val();
		var endDate = ($("#endDate").val());
		var contractid=$("#contractCode  option:selected").val();
	            if(contractid==""){
	              jDialog.Alert("请选择合同");
	              return;
	            }
		if(endDate<startDate){
			jDialog.Alert("下刊时间不能小于上刊时间");
			return;
		}
		$.ajax({
			url:"${rc.contextPath}/contract/saveBusContract",
			type:"POST",
			async:false,
			dataType:"json",
			data:{
			 "plateNumber":plateNumber,
			 "contractid":contractid,
			 "startdate":startDate,
			 "enddate":endDate
			},
			success:function(data){
				if (data.left == true) {
				jDialog.Alert("保存成功");
				var uptime = window.setTimeout(function(){
				window.location.reload()
			   	clearTimeout(uptime);
						},2000)
				} else {
					jDialog.Alert(data.right);
				}
			}
		});
			}
</script>

 
<div class="withdraw-wrap color-white-bg fn-clear">
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="${rc.contextPath}/contract/saveContract?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
								enctype="multipart/form-data">
								<#if contractView?? && contractView.mainView??>
								  <input type="hidden" name="id" value="${(contractView.mainView.id)!''}"/>
								</#if>
								<div class="withdraw-title fn-clear">
									车辆关联合同
								</div>
								<div class="withdrawInputs">
									<div class="inputs">
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>车牌号:</label>
												<input class="ui-input validate[required]"
												type="text"  value=""
												id="plateNumber" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder" placeholder="车牌号可写多个用逗号分隔"></p>

										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>合同:</label>
										   <select class="ui-input" name="contractCode" id="contractCode">
                                                <option value="" selected="selected">请选择合同</option>
                                                <#if contracts?exists>
                                                <#list contracts as c>
                                                    <option value="${c.id}">${c.contractName!''}</option>
                                                </#list>
                                                </#if>
                  		                    </select>
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>上刊日期:
															</label> <input
												class="ui-input datepicker validate[required,custom[date],past[#endDate]]" 
												type="text" name="startDate1" value="${(contractView.mainView.startDate?string("yyyy-MM-dd"))!''}"
												id="startDate" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>下刊日期:</label> <input
												class="ui-input datepicker validate[required,custom[date],future[#startDate]"
												type="text" name="endDate1"
												id="endDate" data-is="isAmount isEnough"  value="${(contractView.mainView.endDate?string("yyyy-MM-dd"))!''}"
												autocomplete="off" disableautocomplete="">
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="保存">
									</div>
									
							</form>
</div>
						
</@frame.html>
