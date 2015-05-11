<#import "template/template.ftl" as frame>
<#global menu="上传物料">
<@frame.html title="物料上传">


<script type="text/javascript">
    $(document).ready(function() {
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
	i = 2;
	j = 2;
	$(document).ready(function() {
        $("#btn_add2").click(function() {
            $("#newUpload2").append(
                    '<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;"  class="validate[required]" />' +
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
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
		var name = ($("#name").val());
		var infoContext = ($("#infoContext").val());
		Sfile= ($("#Sfile").val());
		if(Sfile== "" && infoContext=="" ){
			jDialog.Alert("请填写完整信息");
			return;
		}
        if (!$("#industryId").val()) {
            jDialog.Alert("请选择行业");
            return;
        }
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/supplies/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
	}
	
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
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
												class="ui-form-required">*</span>物料名称</label> 
												<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]" 
												type="text" name="name" id="name"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="支持中英文、数字、下划线">
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
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>行业:</label>

                                            <select id="industryId" name="industryId" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="" style="width:173px; height: 38px;">
                                                <#list industries as industry>
                                                    <option value="${industry.id}">${industry.name}</option>
                                                </#list>
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
													<input type="file" name="file" id="Sfile" class="validate[required]">
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
</div>
</@frame.html>
