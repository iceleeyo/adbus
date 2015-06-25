<#import "template/template.ftl" as frame>
<#global menu="上传物料">
<#import "template/pickBuses.ftl" as pickBuses>
<@frame.html title="物料上传" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

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
                    '<input class="btn-sm btn-wrong" type="button"  style="margin-top:10px;" value="删除"  onclick="del_2('+ j + ')"/></div>');
            j = j + 1;
        });
        $("#btn_add3").click(function() {
            $("#newUpload3").append(
                    '<div id="quadiv_'+i+'"><input  name="qua_'+i+'" type="file"  style="margin-top:10px;"  class="validate[required]" />' +
                    '<input class="btn-sm btn-wrong" type="button"  style="margin-top:10px;" value="删除"  onclick="del_3('+ i + ')"/></div>');
            i = i + 1;
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
        
    <@security.authorize ifAnyGranted="ShibaOrderManager">    
    //author:pxh 2015-05-20 22:36
	$( "#userId" ).autocomplete({
		source: "${rc.contextPath}/user/autoComplete",
		change: function( event, ui ) { 
		/*if(ui.item!=null){alert(ui.item.value);}*/
		//table.fnDraw();
		},
		select: function(event,ui) {
			$('#userId').val(ui.item.value);
		  	//table.fnDraw();
		}
	});
	 </@security.authorize>
        
    });

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}
	function del_3(o) {
		document.getElementById("newUpload3").removeChild(
				document.getElementById("quadiv_" + o));
	}


	function sub2() {
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
		var name = ($("#name").val());
		var infoContext = ($("#infoContext").val());
		var suppliesType = ($("#suppliesType").val());
		Sfile= ($("#Sfile").val());
		Sfile1= ($("#Sfile1").val());
		if(Sfile== "" && infoContext=="" ){
			jDialog.Alert("请填写完整信息");
			return;
		}
        if (!$("#industryId").val()) {
            jDialog.Alert("请选择行业");
            return;
        }
        if (Sfile.lastIndexOf(".") != -1 && suppliesType == "0") {
			var fileType = (Sfile.substring(Sfile.lastIndexOf(".") + 1,Sfile.length)).toLowerCase();
			var suppotFile = new Array();
			suppotFile[0] = "avi";
			suppotFile[1] = "mp4";
			suppotFile[2] = "rmvb";
			var flag=false;
			for (var i = 0; i < suppotFile.length; i++) {
				if (suppotFile[i] == fileType) {
					flag=true;
				}
			}
				if(flag == false)
				{
				jDialog.Alert("文件类型只支持AVI,MP4,RMVB");
				return;
				}
		}

		if (Sfile.lastIndexOf(".") != -1 && suppliesType == "1") {
			var fileType = (Sfile.substring(Sfile.lastIndexOf(".") + 1,Sfile.length)).toLowerCase();
			var suppotFile = new Array();
			suppotFile[0] = "gif";
			suppotFile[1] = "png";
			suppotFile[2] = "jpg";
			var flag=false;
			for (var i = 0; i < suppotFile.length; i++) {
				if (suppotFile[i] == fileType) {
					flag=true;
				}
			}
			if(flag == false)
			{
				jDialog.Alert("文件类型只支持GIF,PNG,JPG");
				return;
			}
			
		}
		if (Sfile1.lastIndexOf(".") != -1 ) {
			var fileType = (Sfile1.substring(Sfile1.lastIndexOf(".") + 1,
					Sfile1.length)).toLowerCase();
			var suppotFile = new Array();
			suppotFile[0] = "gif";
			suppotFile[1] = "bmp";
			suppotFile[2] = "jpg";
			var flag=false;
			for (var i = 0; i < suppotFile.length; i++) {
				if (suppotFile[i] == fileType) {
					flag=true;
				}
			}
			if(flag == false)
			{
				jDialog.Alert("资质类型只支持GIF,BMP,JPG");
				return;
			}
		}
		
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/supplies/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
		document.getElementById('subWithdraw').setAttribute('disabled',true);
		 var uploadProcess={upath:'${rc.contextPath}/upload/process'};
		  $('#progress1').anim_progressbar(uploadProcess);
	}

	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
							<form id="userForm2" name="userForm2" action="put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
								enctype="multipart/form-data" method="post"">
								<div class="withdraw-title fn-clear">
									上传物料及相关资质
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
										 <@security.authorize ifAnyGranted="ShibaOrderManager">   
											<div class="ui-form-item">
	                                            <label class="ui-label mt10">
												<span
	                                                    class="ui-form-required">
												</span>广告主:
	                                            </label>
	                                            <span>
	                         						<input id="userId" name="userId"
	                         						 class="ui-input " placeholder="请选择广告主" >
	                       						</span>
	                                        </div>
                                         </@security.authorize>
										
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
                                                <#if city.mediaType == 'body'>
                                                    <option value="3" selected="selected">车身</option>
                                                <#else>
												<option value="0" selected="selected">视频</option>
                                                <option value="1">图片</option>
                                                <option value="2">文本</option>
                                                </#if>
                                            </select>
										</div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>所属行业:</label>

                                            <select id="industryId" class="ui-input" name="industryId" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="" >
                                                <#list industries as industry>
                                                    <option value="${industry.id}">${industry.name}</option>
                                                </#list>
                                            </select>

                                        </div>
										<div class="ui-form-item" id="text" style="display:none;">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>文本信息</label> 
												<input
												class="ui-input" type="text" name="infoContext"
												id="infoContext" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" style="height: 91px; width: 367px; ">
										</div>
										<div class="ui-form-item" id="file">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>物料上传</label>
											<div id="newUpload2">
												<div class="filebox" id="div_1">
													<input type="file" name="file" id="Sfile" class="validate[required]">
												</div>
												</div>
											<input class="btn-sm btn-success" type="button" id="btn_add2" value="增加一行"
												style="margin-top: 10px;"><br>
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10">资质上传</label>
											<div id="newUpload3">
												<div id="quadiv_1">
													<input type="file" name="qua" id="Sfile1">
												</div>
											</div>
											<input class="btn-sm btn-success" type="button" id="btn_add3" value="增加一行"
												style="margin-top: 10px;" ><br>
										</div>
										<div class="ui-form-item widthdrawBtBox">
											<input type="button" id="subWithdraw" class="block-btn"
												onclick="sub2();" value="开始上传">
										</div>
									
												 <div id="progress1">
										            <div class="percent"></div>
										            <div class="pbar"></div>
										            <div class="elapsed"></div>
										        </div>
									</div>
								</div>
		<div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span> 温馨提示
		</div>
		<ol>
			<li>1.请提供符合产品要求的物料类型，视频类型格式支持AVI，MP4，RMVB。</li>
			<li>2.图片类型格式支持GIF，PNG，JPG；资质类型格式支持GIF，PNG，JPG。</li>
			<li>3.在必须要的时候，请上传物料说明和广告资质。</li>
			<li>4.如果物料的文件比较大，可能需要一定的时间，请耐心等待。文件大小尽量控制在200M以内。</li>
			<li>5.请勿上传违反国家广告法及相关法律法规的物料文件。</li>
			<li>6.物料的其他技术性要求（待补充）。</li>

		</ol>
	</div>
							</form>
</div>
</@frame.html>
