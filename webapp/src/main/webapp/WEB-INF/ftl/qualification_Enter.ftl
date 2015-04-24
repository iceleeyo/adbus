<#import "template/template.ftl" as frame>
<#global menu="资质录入">
<@frame.html title="资质信息录入" js=["jquery-ui/jquery-ui.js", "datepicker.js", "jquery.datepicker.region.cn.js"] css=["jquery-ui/jquery-ui.css"]>

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
		var code = ($("#code").val());
		var name = ($("#name").val());
		var startDate = $("#startDate").val();
		var endDate = ($("#endDate").val());
		var amounts=($("#amounts").val());
		Sfile= ($("#Sfile").val());
		if(Sfile==""){
			jDialog.Alert("请选择合同附件");
			return;
		}
		if(code==""){
			jDialog.Alert("请填写合同号");
			return;
		}
		if(amounts==""){
			jDialog.Alert("请填写合同金额");
			return;
		}
		if(name==""){
			jDialog.Alert("请填写合同名称");
			return;
		}
		if(startDate.length<1){
			jDialog.Alert("请填写合同生效时间");
			return;
		}
		if(endDate.length<1){
			jDialog.Alert("请填写合同失效时间");
			return;
		}
		if(endDate<startDate){
			jDialog.Alert("失效时间不能小于生效时间");
			return;
		}
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/contract/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();

	}
</script>

						
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="saveContract"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									资质信息录入
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
                                            <label class="ui-label mt10">
											<span
                                                    class="ui-form-required">*
											</span>广告主:
                                            </label>
                                            <select class="ui-input" name="userId" id="userId">
                                                <option value="" selected="selected"></option>
                                                <#list users as u>
                                                    <option value="${u.username}">${u.username}</option>
                                                </#list>
                                            </select>
                                        </div>
										<div class="ui-form-item">
											<label class="ui-label mt10">
											<span
												class="ui-form-required">*
											</span>合同号:
											</label> 
												<input class="ui-input"
												type="text" name="contractCode" id="code"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>合同名称:</label> <input
												class="ui-input" type="text" name="contractName"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder"></p>

										</div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>金额:</label>
                                                    <input
												class="ui-input" type="text" name="amounts"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>行业:</label>
                                                     
												<select id="industry" name="industry.id" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" style="width:173px; height: 38px;">
                                                    <#list industries as industry>
                                                        <option value="${industry.id}">${industry.name}</option>
                                                    </#list>
												</select>
												
                                        </div>

										<div class="ui-form-item">
											<label class="ui-label mt10">开始日期:</label> <input
												class="ui-input datepicker" type="text" name="startDate1"
												id="startDate" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10">终止日期:</label> <input
												class="ui-input datepicker" type="text" name="endDate1"
												id="endDate" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required"></span>合同备注:</label>
                                                     <input
												class="ui-input" type="text" name="remark"
												id="remark" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>



                                        <div class="ui-form-item">
											<label class="ui-label mt10">附件上传</label>
											<div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile">
												</div>
											</div>
											<input type="button" id="btn_add2" value="增加一行"
												style="margin-top: 10px;" ><br>
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="创建合同">
									</div>
								</div>
			<div class="worm-tips" >
            <div class="tips-title"><span class="icon"></span> 温馨提示</div>
	          <ol>
              <li>1.请输入正确的合同号及金额。</li>
              <li>2.如果您填写的信息不正确可能会导致下单失败。 </li>
              <li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
	          </ol>
	        </div>
							</form>
						
</@frame.html>
