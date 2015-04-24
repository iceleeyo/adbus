<#import "template/template.ftl" as frame>
<#global menu="用户添加">
<@frame.html title="用户添加" js=["jquery-ui/jquery-ui.js", "datepicker.js", "jquery.datepicker.region.cn.js"] css=["jquery-ui/jquery-ui.css"]>

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
		 
		$('#userForm2').ajaxForm(function(data) {
		
				if(data.error ==1){
					jDialog.Alert("成功");
				}else {
					jDialog.Alert(data.errorMessage);
				}
			
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/contract/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();

	}
</script>

						
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="save"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									用户添加信息录入
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
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>登录名:</label> <input
												class="ui-input" type="text" name="username"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											

										</div><p class="ui-term-placeholder"></p>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>真实姓名:</label>
                                                    <input
												class="ui-input" type="text" name="firstName"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>密码:</label>
                                                    <input
												class="ui-input" type="text" name="password"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>请确认密码:</label>
                                                    <input
												class="ui-input" type="text" name="password2"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>邮箱地址:</label>
                                                    <input
												class="ui-input" type="text" name="email"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>联系电话:</label>
                                                    <input
												class="ui-input" type="text" name="phone"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>权限:</label>
                                                   <#if groupsList?exists>
              											  <#list groupsList?keys as vkey> 
                                                   			 <input type="checkbox" value="${vkey}" name="roles"/>${groupsList[vkey]}
                                                  			</#list>
           										 </#if> 
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>所属公司:</label>
                                                    <input
												class="ui-input" type="text" name="company"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>所属部门:</label>
                                                    <input
												class="ui-input" type="text" name="department"
												id="amounts" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
                                        </div>
                                        </div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="创建用户">
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
