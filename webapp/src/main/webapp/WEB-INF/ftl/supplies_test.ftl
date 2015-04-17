<#import "template/template.ftl" as frame>
<#global menu="上传物料">
<@frame.html title="物料上传">

<script type="text/javascript">
	i = 2;
	j = 2;
	$(document).ready(function() {
        $("#btn_add2").click(function() {
            $("#newUpload2").append(
                    '<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;" />' +
                    '<input type="button"  style="margin-top:10px;" value="删除"  onclick="del_2('+ j + ')"/></div>');
            j = j + 1;
        });

        $("#suppliesType").change(function(){
            var suppliesType = $(this).val();
            if(suppliesType=="0" || suppliesType=="1"){
                $("#text").hide();
                $("#file").show();
            }
            if(suppliesType=="2"){
                $("#text").show();
                $("#file").hide();
            }
        });
    });

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}


	function sub2() {
		var name = ($("#name").val());
		var infoContext = ($("#infoContext").val());
		Sfile= ($("#Sfile").val());
		if(name==""){
			jDialog.Alert("请填写物料名称");
			return;
		}
		if(Sfile==""&& infoContext==""){
			jDialog.Alert("请填写完整信息");
			return;
		}
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.left + " # " + data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/supplies/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
	}
</script>
							<form id="userForm2" name="userForm2" action="put"
								enctype="multipart/form-data" method="post"">
								<div class="withdraw-title fn-clear">
									上传物料
									<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
								</div>
								<div class="withdrawInputs">
									<div class="inputs">

										<br>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>物料名称</label> <input
												class="ui-input" type="text" name="name" id="name"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>物料类型</label>
											<select class="ui-input" name="suppliesType" id="suppliesType">
												<option value="0" selected="selected">视频</option>
												<option value="1">图片</option>
												<option value="2">文本</option>
											</select>
										</div>

										<div class="ui-form-item" id="text" style="display:none;">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>文本信息</label> <input
												class="ui-input" type="text" name="infoContext"
												id="infoContext" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" style="height: 91px; width: 367px; ">
										</div>
										<div class="ui-form-item" id="file">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>附件上传</label>
											<div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile">
												</div>
											</div>
											<input type="button" id="btn_add2" value="增加一行"
												style="margin-top: 10px;"><br>
										</div>
										<div class="ui-form-item widthdrawBtBox">
											<input type="button" id="subWithdraw" class="block-btn"
												onclick="sub2();" value="物料上传">
										</div>
									</div>
								</div>
							</form>
</@frame.html>
