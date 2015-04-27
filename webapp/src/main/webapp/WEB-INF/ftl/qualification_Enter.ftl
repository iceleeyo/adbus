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
		
		Sfile= ($("#Sfile").val());
		if(Sfile==""){
			jDialog.Alert("请上传资质信息");
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
								class="ui-form" method="post" action="savequalifi"
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
											<label class="ui-label mt10">资质信息上传</label>
											<div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile">
												</div>
											</div>
											<input type="button" id="btn_add2" value="增加附件"
												style="margin-top: 10px;" ><br>
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="提交资质信息">
									</div>
								</div>
			<div class="worm-tips" >
            <div class="tips-title"><span class="icon"></span> 温馨提示</div>
	          <ol>
              <li>1.请选择合法的用户资质进行上传。</li>
              <li>2.如果您填写的信息不正确可能会导致下单失败。 </li>
              <li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
	          </ol>
	        </div>
							</form>
						
</@frame.html>
