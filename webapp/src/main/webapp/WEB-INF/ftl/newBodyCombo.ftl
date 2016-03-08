<#import "template/template.ftl" as frame> <#global menu="车身产品定义">
<@frame.html title="车身产品定义"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<script type="text/javascript">
  function sub(){
	  $("#form2").validationEngine({
          validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
          inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
          success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
          promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
          showOneMessage: true,
          maxErrorsPerField: 1,
      });
	if (!$("#form2").validationEngine('validateBeforeSubmit'))
            return;
	   $('#form2').ajaxForm(function(data) {
		  if(data.left){
		      document.getElementById('subutton').setAttribute('disabled',true);
		       $("#subutton").css("background-color","#85A2AD");
		      layer.msg(data.right);
		      var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/product/productV2_list";
			   	clearTimeout(uptime);
						},2000)
		     }else{
		     layer.msg(data.right);
		     }
		}).submit();
	}
	function uploadImaget(formId) {
		var image_name = $("#" + formId + "_file").val();
		if (image_name != '') {
			var imgs = image_name.split(".");
			var img_subfier = imgs[imgs.length - 1].toLocaleLowerCase();
			var img_parr = [ "jpg", "jpeg", "gif", "png" ];

			if (image_name != '') {
				if ($.inArray(img_subfier, img_parr) == -1) {
					jDialog.Alert("请上传['jpg','gif','png','jpeg']格式的图片!");
					document.getElementById(formId).reset();
					return false;
				}
			}
			var options = {
				url : "${rc.contextPath}/upload/saveSimpleFile",
				type : "POST",
				dataType : "json",
				success : function(data) {
					if (data != null && data != "") {
						$("#" + formId + "_img").attr("src",
								"${rc.contextPath}/upload_temp/" + data);
						$("#" + formId + "_url").val(data);
					}
				}
			};
			$("#" + formId).ajaxSubmit(options);
			document.getElementById(formId).reset();
		}
	}
	function accountPrice(){
	var leval=$("#leval option:selected").val();
	var doubleDecker=$("#doubleDecker option:selected").val();
	var days=$("#days option:selected").val();
	var busNumber=$("#busNumber").val();
	   $.ajax({
	     url:"${rc.contextPath}/product/acountPrice",
	     data:{"leval":leval,"doubleDecker":doubleDecker,"busNumber":busNumber,"days":days},
	     type:"POST",
	     success:function(data){
	        $("#sumprice").val(data);
	     }
	   });
	}
	function showdoc() {
		if (flag == true) {
			$("#needimg").show();
			flag = false;
		} else {
			$("#needimg").hide();
			flag = true;
		}
	}
</script>
<div class="withdraw-wrap color-white-bg fn-clear"
	style="margin-top: 10px;">
		<div class="withdrawInputs">
		<div class="withdraw-title fn-clear">
			<span>车身产品定义</span> 
		</div>
		<form data-name="withdraw" name="form2" id="form2" class="ui-form"
		method="post" action="${rc.contextPath}/product/saveBodyCombo" enctype="multipart/form-data">
		           
		           <div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>车身产品名称：</label>
				<input class="ui-input validate[required]" type="text" value=""
					name="name" id="name" data-is="isAmount isEnough"
					autocomplete="off" disableautocomplete="" placeholder="2-20个字符">
			</div>
		            <div class="ui-form-item ">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>线路级别：</label> <select class="ui-input" onchange="accountPrice();"
							name="leval" id="leval"> <#if lineLevels??> <#list
							lineLevels as level>
							<option value="${level.name()}">${level.nameStr}</option>
							</#list> </#if>
						</select>
					</div>
					<div class="ui-form-item ">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>车型:</label> <select class="ui-input" onchange="accountPrice();"
							name="doubleDecker" id="doubleDecker">
							<option value="false">单层</option>
							<option value="true">双层</option>
						</select>
					</div>
					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>巴士数量(默认):</label> <input
							class="ui-input validate[required,number,min[1]" type="number"
							value="1" name="busNumber" id="busNumber" readonly="readonly"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>
					<div class="ui-form-item toggle bodyToggle">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>刊期(默认):</label> <select class="ui-input"
							name="days" id="days">
							<option value="30">30</option>
							<!--<option value="60">60</option>
							<option value="90">90</option>
							<option value="180">180</option>
							<option value="360">360</option>-->
						</select>天
					</div>
			<div class="ui-form-item toggle bodyToggle">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>价格(￥):</label>
				<input class="ui-input validate[required]" type="text" value="3900" name="price" id="sumprice" 
					data-is="isAmount isEnough" readonly="readonly" autocomplete="off"
					disableautocomplete="">
			</div>
			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>商区：</label>
				<input class="ui-input validate[required]" type="text" value=""
					name="addressList" id="name" data-is="isAmount isEnough"
					autocomplete="off" disableautocomplete="" placeholder="多个商区以,分开">
			</div>
			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>城区：</label>
				<input class="ui-input validate[required]" type="text" value=""
					name="smallAdressList" id="name" data-is="isAmount isEnough"
					autocomplete="off" disableautocomplete="" placeholder="多个城区以,分开">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>人次：</label>
				<input class="ui-input validate[required]" type="text" value=""
					name="personAvg" id="name" data-is="isAmount isEnough"
					autocomplete="off" disableautocomplete="" placeholder="数值">
			</div>

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>套餐描述：</label>
				<textarea rows="4" class=" validate[required]" cols="40"
					style="resize: none;" name="remarks"></textarea>
			</div>
			
			<div class="ui-form-item">
					<label class="ui-label mt10">媒体情况：</label>
					媒体位置&nbsp;&nbsp;<input style="width:250px;" id="exclusiveUser" name="locationstr" value="" placeholder="例如：北京" >&nbsp;&nbsp;&nbsp;&nbsp;
					覆盖人次&nbsp;&nbsp;<input style="width:250px;" id="exclusiveUser" name="personnum" onkeyup="value=value.replace(/[^\d]/g,'')" value="" placeholder="" >万&nbsp;&nbsp;&nbsp;&nbsp;<br>
					线路概况&nbsp;&nbsp;<input style="width:250px;" id="exclusiveUser" name="linesurvey" value="" placeholder="例如：北京六环内
                                      470余条公交线路" >&nbsp;&nbsp;&nbsp;&nbsp;
					媒体概况&nbsp;&nbsp;<input style="width:250px;" id="exclusiveUser" name="mesurvey" value="" placeholder="例如：19寸公交彩色液晶电视" >&nbsp;&nbsp;&nbsp;&nbsp;<br>
					媒体属主&nbsp;&nbsp;<input style="width:250px;" id="exclusiveUser" name="mediaowner" value="" placeholder="例如：北广传媒" >&nbsp;&nbsp;&nbsp;&nbsp;
					媒体数量&nbsp;&nbsp;<input style="width:250px;" id="exclusiveUser" name="mediacount" value="" placeholder="例如：12000辆车24000块屏" >&nbsp;&nbsp;
			</div>
		 <input id="img1_url" name="img1_url" type="hidden"
		    value="<#if jsonView?? && jsonView.img1_url?has_content>${jsonView.img1_url}</#if>" />
		<input id="img2_url" name="img2_url" type="hidden"
			value="<#if jsonView?? && jsonView.img2_url?has_content>${jsonView.img2_url}</#if>" />
		<input id="img3_url" name="img3_url" type="hidden"
			value="<#if jsonView?? && jsonView.img3_url?has_content>${jsonView.img3_url}</#if>" />
		<input id="img4_url" name="img4_url" type="hidden"
			value="<#if jsonView?? && jsonView.img4_url?has_content>${jsonView.img4_url}</#if>" />
		<input id="intro1_url" name="intro1_url" type="hidden"
			value="<#if jsonView?? && jsonView.intro1_url?has_content>${jsonView.intro1_url}</#if>" />
		<input id="intro2_url" name="intro2_url" type="hidden"
			value="<#if jsonView?? && jsonView.intro2_url?has_content>${jsonView.intro2_url}</#if>" />
		<input id="intro3_url" name="intro3_url" type="hidden"
			value="<#if jsonView?? && jsonView.intro3_url?has_content>${jsonView.intro3_url}</#if>" />
		<input id="intro4_url" name="intro4_url" type="hidden"
			value="<#if jsonView?? && jsonView.intro4_url?has_content>${jsonView.intro4_url}</#if>" />
			<div class="ui-form-item widthdrawBtBox" style="padding: 20px 0px 0px 0px; text-align: center;">
					<input type="button" id="subutton" class="block-btn" onclick="sub();" value="生成套餐">
		           <input type="button" id="subWithdraw" class="block-btn" onclick="showdoc();" value="上传产品图片">
				
			</div>
</form>
<div id="needimg" style="display: none; margin-top: 40px;">
		<div id="needimg_left">

			<div class="ui-form-item" id="file">
				<label class="ui-label mt10">产品缩略图1</label>
				<form id="img1" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.img1_url?has_content>${rc.contextPath}/upload_temp/${jsonView.img1_url}</#if>"
						id="img1_img"  border="1px solid #d0d0d0;" />
					<input id="img1_file" class="select_img" name="img1_file"
						type="file" onchange="uploadImaget('img1');" />
				</form>
			</div>
			<div class="ui-form-item" id="file">
				<label class="ui-label mt10"><span class="ui-form-required"></span>产品缩略图2</label>
				<form id="img2" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.img2_url?has_content>${rc.contextPath}/upload_temp/${jsonView.img2_url}</#if>"
						id="img2_img"  border="1px solid #d0d0d0;" />
					<input id="img2_file" class="select_img" name="img2_file"
						type="file" onchange="uploadImaget('img2');" />
				</form>
			</div>
			<div class="ui-form-item" id="file">
				<label class="ui-label mt10"><span class="ui-form-required"></span>产品缩略图3</label>
				<form id="img3" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.img3_url?has_content>${rc.contextPath}/upload_temp/${jsonView.img3_url}</#if>"
						id="img3_img"  border="1px solid #d0d0d0;" />
					<input id="img3_file" class="select_img" name="img3_file"
						type="file" onchange="uploadImaget('img3');" />
				</form>
			</div>
			<div class="ui-form-item" id="file">
				<label class="ui-label mt10"><span class="ui-form-required"></span>产品缩略图4</label>
				<form id="img4" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.img4_url?has_content>${rc.contextPath}/upload_temp/${jsonView.img4_url}</#if>"
						id="img4_img"  border="1px solid #d0d0d0;" />
					<input id="img4_file" class="select_img" name="img4_file"
						type="file" onchange="uploadImaget('img4');" />
				</form>
			</div>
		</div>
		<div id="needimg_right">
			<div class="ui-form-item" id="file">
				<label class="ui-label mt10"><span class="ui-form-required"></span>媒体区域图</label>
				<form id="intro1" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.intro1_url?has_content>${rc.contextPath}/upload_temp/${jsonView.intro1_url}</#if>"
						id="intro1_img" 
						border="1px solid #d0d0d0;" /> <input id="intro1_file"
						class="select_img" name="intro1_file" type="file"
						onchange="uploadImaget('intro1');" />

				</form>
			</div>
			<div class="ui-form-item" id="file">
				<label class="ui-label mt10"><span class="ui-form-required"></span>媒体展示图</label>
				<form id="intro2" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.intro2_url?has_content>${rc.contextPath}/upload_temp/${jsonView.intro2_url}</#if>"
						id="intro2_img" 
						border="1px solid #d0d0d0;" /> <input id="intro2_file"
						class="select_img" name="intro2_file" type="file"
						onchange="uploadImaget('intro2');" />
				</form>
			</div>
			<div class="ui-form-item" id="file">
				<label class="ui-label mt10"><span class="ui-form-required"></span>合作品牌图</label>
				<form id="intro3" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.intro1_ur3?has_content>${rc.contextPath}/upload_temp/${jsonView.intro3_url}</#if>"
						id="intro3_img" 
						border="1px solid #d0d0d0;" /> <input id="intro3_file"
						class="select_img" name="intro3_file" type="file"
						onchange="uploadImaget('intro3');" />
				</form>
			</div>
			<div class="ui-form-item" id="file">
				<label class="ui-label mt10"><span class="ui-form-required"></span>其他介绍图</label>
				<form id="intro4" method="post" enctype="multipart/form-data">
					<img
						src="<#if jsonView?? && jsonView.intro4_url?has_content>${rc.contextPath}/upload_temp/${jsonView.intro4_url}</#if>"
						id="intro4_img" 
						border="1px solid #d0d0d0;" /> <input id="intro4_file"
						class="select_img" name="intro4_file" type="file"
						onchange="uploadImaget('intro4');" />
				</form>
			</div>
		</div>
	</div>

</div>

</@frame.html>
