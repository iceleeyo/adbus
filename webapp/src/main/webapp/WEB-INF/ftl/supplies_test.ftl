<#import "template/template.ftl" as frame>

<@frame.html title="物料上传">

<script type="text/javascript">
	i = 2;
	j = 2;
	$(document).ready(function() {
        $("#btn_add2").click(function() {
            $("#newUpload2").append('<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;" /><input type="button"  style="margin-top:10px;" value="删除"  onclick="del_2('
                    + j + ')"/></div>');
            j = j + 1;
        });
    });

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}


	function sub2() {
		var name = ($("#name").val());
		if(name==""){
			jDialog.Alert("请填写物料名称");
			return;
		}
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.left + " # " + data.right);
		}).submit();
	}
	
	function Tchange(obj){
		jDialog.Alert(obj.value);
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
												class="ui-form-required">*</span>素材标题</label> <input
												class="ui-input" type="text" name="name" id="name"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>素材类型</label>
											<select class="ui-input" name="suppliesType" id="suppliesType" onchange="Tchange(this);">
												<option value="0" selected="selected">video</option>
												<option value="1">image</option>
												<option value="2">info</option>
											</select>
										</div>

										<div class="ui-form-item" id="text" style="display:none;">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>文本类型时 文本内容</label> <input
												class="ui-input" type="text" name="infoContext"
												id="infoContext" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
										<div class="ui-form-item" id="file">
											<label class="ui-label mt10">附件上传</label>
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