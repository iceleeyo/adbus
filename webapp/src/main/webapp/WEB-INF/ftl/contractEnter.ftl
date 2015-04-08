<#import "template/template.ftl" as frame>
<#global menu="添加合同">
<@frame.html title="合同录入">

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
		if(code==""){
			jDialog.Alert("请填写合同号");
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
			jDialog.Alert(data.left + " # " + data.right);
			window.location.href = "${rc.contextPath}/contract/list/1"
		}).submit();
	}
</script>

						<div class="withdraw-wrap color-white-bg fn-clear" style="height: 820px; ">
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="saveContract"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									合同详情录入
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

											<input type="text" style="display: none;">
										</div>


										<div class="ui-form-item">
											<label class="ui-label mt10">生效日期</label> <input
												class="ui-input" type="date" name="startDate1"
												id="startDate" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10">失效日期</label> <input
												class="ui-input" type="date" name="endDate1"
												id="endDate" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>



										<div class="ui-form-item">
											<label class="ui-label mt10">附件上传</label>
											<div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file">
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
              <li>1.请确保您输入的提现金额，以及银行帐号信息准确无误。</li>
              <li>2.如果您填写的提现信息不正确可能会导致提现失败，由此产生的提现费用将不予返还。 </li>
              <li>3.在双休日和法定节假日期间，用户可以申请提现，人人贷会在下一个工作日进行处理。由此造成的不便，请多多谅解！</li>
              <li>4.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
	          </ol>
	        </div>
							</form>
						</div>
</@frame.html>
