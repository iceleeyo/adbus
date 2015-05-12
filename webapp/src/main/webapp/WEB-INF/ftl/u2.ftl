<#import "template/template.ftl" as frame>
<#global menu="添加合同">
<@frame.html title="合同录入" js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/jquery-ui-1.8.16.custom.min.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css"]>




 

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
															'<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;" class="validate[required]"/><input type="button" value="删除" style="margin-top:10px;" onclick="del_2('
																	+ j
																	+ ')"/></div>');
											j = j + 1;
										});

					});

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}
	
	function showResponse(){
		alert(1);
	 }
	 function showRequest() {
	   		alert("name不能为空");
		 }

	function sub(){
              // $('#userForm2').submit();
              
              $('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/contract/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
		  var uploadProcess={upath:'${rc.contextPath}/upload/process'};
		  $('#progress2').anim_progressbar(uploadProcess);
	}
	 
	
	$(function(){
		//donesome
	});
	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="u2_save?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
								</div>
								<div class="withdrawInputs">
									<div class="inputs">
                                        <div class="ui-form-item">
											<label class="ui-label mt10">附件上传</label>
											<div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile" class="validate[required]">
												</div>
											</div>
											<input type="button" id="btn_add2" value="增加一行"
												style="margin-top: 10px;" ><br>
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="创建合同">
											
											<input type="submit" id="subWithdraw2" class="block-btn"
											  value="submit">
											  <br>
											  
												 <div id="progress2">
										            <div class="percent"></div>
										            <div class="pbar"></div>
										            <div class="elapsed"></div>
										        </div>
																						  
											  <br>
											  <!-- 	 <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/bootstrap.css">
												   <label class="control-label">上传进度:</label>  
								                    <div class="controls">  
								                        <div  class="progress progress-success progress-striped" style="width:50%">  
								                            <div  id = 'proBar' class="bar" style="width: 20%"></div>  
								                        </div>  
								                    </div>  
								               -->
									</div>
								</div>
							</form>
</div>
						
</@frame.html>
