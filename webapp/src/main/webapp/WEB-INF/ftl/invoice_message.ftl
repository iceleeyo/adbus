<#import "template/template.ftl" as frame>
<#global menu="发票信息录入">
<@frame.html title="发票信息录入" js=["jquery-ui/jquery-ui.js", "datepicker.js", "jquery.datepicker.region.cn.js"] css=["jquery-ui/jquery-ui.css"]>

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
		var title = ($("#title").val());
		var taxrenum = ($("#taxrenum").val());
		var bankname = $("#bankname").val();
		if(title==""){
			jDialog.Alert("请填写发票抬头");
			return;
		}
		if(taxrenum==""){
			jDialog.Alert("请填写税务登记证");
			return;
		}
		if(bankname==""){
			jDialog.Alert("请填写基本户开户银行");
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
								class="ui-form" method="post" action="saveInvoice"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									发票信息录入
									<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
								</div>
								<div class="withdrawInputs" style="margin-left: 10px;">
									<div class="inputs">
									
									<div class="ui-form-item">
											<label class="ui-label mt10">
											<span
												class="ui-form-required">*
											</span>发票类型:
											</label> 
											<input type="radio" name="type" checked="checked" value="normal">&nbsp;普通发票&nbsp;&nbsp;
											<input type="radio" name="type" value="special">&nbsp;普通发票
										</div>
                                        <div class="ui-form-item">
											<label class="ui-label mt10">
											<span
												class="ui-form-required">*
											</span>发票抬头:
											</label> 
												<input class="ui-input"
												type="text" name="title" id="title"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="">
										</div>
										<div id="other">
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>税务登记证号:</label> <input
												class="ui-input" type="text" name="taxrenum"
												id="taxrenum" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder"></p>

										</div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>基本户开户银行名称:</label>
                                                    <input
												class="ui-input" type="text" name="bankname"
												id="bankname" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>基本户开户账号:</label>
                                                    <input
												class="ui-input" type="text" name="accountnum"
												id="accountnum" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>注册场所地址:</label>
                                                    <input
												class="ui-input" type="text" name="regisaddr"
												id="regisaddr" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>注册固定电话:</label>
                                                    <input
												class="ui-input" type="text" name="fixphone"
												id="fixphone" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>营业执照复印件:</label>
                                                    <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile">
												</div>
											</div>
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>税务登记复印件:</label>
                                                    <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile2">
												</div>
											</div>
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>一般纳税人资格认证复印件:</label>
                                                    <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile3">
												</div>
											</div>
                                        </div>
                                        
                                        </div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="上传创建">
									</div>
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
