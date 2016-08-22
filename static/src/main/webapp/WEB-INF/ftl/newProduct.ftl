<#import "template/template.ftl" as frame> <#import "spring.ftl" as
spring /> <#global menu="产品定义"> <#assign action="增加"> <#if
prod??><#assign action="修改"></#if> <@frame.html title="${action}产品套餐"
js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js","js/jquery-ui.min.js","js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-ui-timepicker-addon.js","js/jquery-ui-timepicker-zh-CN.js",
"js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.17.custom.css","css/jquery-ui-timepicker-addon.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
	function go_back() {
		history.go(-1);
	}
	$(document).ready(function() {
		$("#productForm").validationEngine({
			validationEventTrigger : "blur", //触发的事件  validationEventTriggers:"keyup blur",
			inlineValidation : true,//是否即时验证，false为提交表单时验证,默认true
			success : false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
			promptPosition : "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
			showOneMessage : true,
			maxErrorsPerField : 1,
		//failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
		//success : function() { callSuccessFunction() },//验证通过时调用的函数
		});
	});
</script>
<script type="text/javascript">
	$(function() {
		$(".ui_timepicker").datetimepicker({
			//showOn: "button",
			//buttonImage: "./css/images/icon_calendar.gif",
			
			//buttonImageOnly: true,
			showSecond : true,
			timeFormat : 'hh:mm:ss',
			stepHour : 1,
			stepMinute : 1,
			stepSecond : 1
		})
		function toggleFields() {
			var type = $("#type").val();
			$(".toggle").hide();
			$("." + type + "Toggle").show();
		}
		$("#type").change(toggleFields);
		toggleFields();
	});
	function sub2() {
		if (!$("#productForm").validationEngine('validateBeforeSubmit'))
			return;
		var iscompare = $('input:radio[name="iscompare"]:checked').val();
		var startDate1 = $("#startDate1").val();
		var biddingDate1 = $("#biddingDate1").val();
		var firstNumber=$("#firstNumber").val();
		var o = document.getElementsByName("checkone");
        	var dIds='';
        	for(var i=0;i<o.length;i++){
                if(o[i].checked)
                dIds+=o[i].value+',';   
           }
           $("#tagIds").val(dIds);
		if(firstNumber>0){
		  if($("#duration").val()>30){
		       jDialog.Alert("有首播时广告时长不能大于30秒");
				return false;
		  }
		}
		if (iscompare == 1) {
			if (biddingDate1 < startDate1) {
				jDialog.Alert("截止时间不能小于开拍时间");
				return;
			}
		}
		var tis=$("#exclusiveUser").val()==""?'请确认产品定向：公开':'请确认产品定向：定向';
		layer.confirm(tis, {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		$('#productForm').ajaxForm(function(data) {
			jDialog.Alert(data.name);
		}).submit();
		document.getElementById('submit').setAttribute('disabled', true);

		if (iscompare == 0) {
			var a = document.createElement('a');
			a.href = '${rc.contextPath}/product/list';
		} else {
			var a = document.createElement('a');
			a.href = '${rc.contextPath}/product/auction';
		}
		document.body.appendChild(a);
		a.click();
}}
);

	}

	$(document).ready(function() {
	getProTags();
		//author:pxh 2015-05-20 22:36
		$("#exclusiveUser").autocomplete({
			 minLength: 0,
			source : "${rc.contextPath}/user/autoComplete",
			change : function(event, ui) {
				/*if(ui.item!=null){alert(ui.item.value);}*/
				table.fnDraw();
			},
			select : function(event, ui) {
				$('#exclusiveUser').val(ui.item.value);
				//table.fnDraw();
			}
		}).focus(function () {
		 $(this).autocomplete("search");
		});;
	});
	function check_size() {
		return;
	}
	function getProTags() {
	$("#location").html("");
	var belongTag=$("#type  option:selected").val();
		$.ajax({
		url : "${rc.contextPath}/product/getProTags",
		type : "GET",
		data : {
		   "belongTag":belongTag
		},
		success : function(data) {
		   $.each(data, function(i, item) {
				$("#location").append("<input type='checkbox' value='"+item.id+"' name='checkone'  />"+item.locationName+"<br>");
		});
		if(typeof('${locationIds!''}')!="undefined"){
		var o = document.getElementsByName("checkone");
        	for(var i=0;i<o.length;i++){
                if('${locationIds!''}'.indexOf(o[i].value)!=-1){
                  o[i].checked=true;
                }
           }
		}
		}});
	}
</script>
<script type="text/javascript">
	function bu(txtObj) {
		txtObj.value = Number(txtObj.value).toFixed(2);
	}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="productForm" id="productForm"
		class="ui-form" method="post" action="save"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>${action}产品套餐</span> <a class="block-btn"
				style="margin-top: -5px;" href="javascript:void(0);"
				onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs_left">
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>媒体类型：</label>
					<select class="ui-input" name="type" id="type" onchange="getProTags()"> 
					<#list
						types as type>
						<option value="${type.name()}"<#if (prod?? && prod.type
							== type.name())>selected="selected"</#if>>${type.typeName}</option>
						</#list>
					</select>
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>套餐名称：</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[60]]"
						type="text" value="<#if prod??>${prod.name!''}<#else></#if>"
						name="name" id="name" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="2-20个字符">
				</div>
				<div
					class="ui-form-item toggle videoToggle imageToggle infoToggle teamToggle inchof32Toggle">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>广告/节目长度：</label>
					<input class="ui-input validate[required,integer,min[5],max[180]]" style="width:150px"
						onkeyup="value=value.replace(/[^\d]/g,'')"
						value="<#if prod??>${prod.duration!''}<#else>25</#if>"
						name="duration" id="duration" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="5-180秒">秒/次
					<p class="ui-term-placeholder"></p>
				</div>

				<div
					class="ui-form-item toggle videoToggle imageToggle infoToggle teamToggle">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>单日播放次数：</label>
					<input class="ui-input validate[required,integer,min[1],max[100]"
						onkeyup="value=value.replace(/[^\d.]/g,'')" type="text"
						value="<#if prod??>${prod.playNumber!''}<#else>24</#if>"
						name="playNumber" id="playNumber" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="1-100次">
				</div>
				<div
					class="ui-form-item toggle inchof32Toggle">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>播放频率：</label>
					<input class="ui-input validate[required,integer,min[1],max[100]" style="width:150px"
						onkeyup="value=value.replace(/[^\d.]/g,'')" type="text"
						value="12" readonly="readonly"
						 data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="1-100次"> 次/小时
				</div>

				<div class="ui-form-item toggle videoToggle">
					<label class="ui-label mt10">首播次数：</label>
					<input class="ui-input validate[integer,min[0],max[30]"
						onkeyup="value=value.replace(/[^\d.]/g,'')"
						value="<#if prod??>${prod.firstNumber!''}<#else>0</#if>"
						name="firstNumber" id="firstNumber" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="0-30次">
				</div>
				<input type="hidden" value="0.1" name="hotRatio"/>
				<input type="hidden" value="0" name="lastNumber"/>
				<div class="ui-form-item ">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>
						<span >套餐播放天数：</span>
						<span class="toggle bodyToggle">广告展示天数：</span> <span
						class="toggle teamToggle">周期(天/期)：</span> </label> <input
						class="ui-input validate[required,integer,min[1],max[360]"
						onkeyup="value=value.replace(/[^\d.]/g,'')"
						value="<#if prod??>${prod.days!''}<#else>7</#if>" name="days"
						id="days" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="最少1天">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>
						<span >套餐价格（元）：</span>
						 </label> <input
						class="ui-input validate[required,number,min[0],max[10000000]"
						onblur="bu(this)"
						onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\d.]/g,'')}else{this.value=this.value.replace(/\D+\./g,'')}"
						value="<#if prod??>${prod.price!''}<#else>0</#if>" name="price"
						id="price" data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required"></span>产品定向：</label>
					<span> <input id="exclusiveUser" name="exclusiveUser"
						value="" placeholder="请选择广告主" style="margin-top: 8px;">
					</span>
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10">套餐描述：</label>
					<textarea rows="4" cols="40" style="resize: none;" name="remarks"><#if prod?exists && prod.remarks?has_content >${prod.remarks!''}</#if></textarea>
				</div>
				
				<div
					class="ui-form-item toggle videoToggle imageToggle infoToggle bodyToggle">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>是否为竞价套餐：</label>
					<div style="margin-top: 10px; float: left;">
						<input type="radio" name="iscompare" value="1"
							onchange="showisAuction()">是
					</div>
					<div style="margin-top: 10px; margin-left: 5px; float: left;">
						<input type="radio" name="iscompare" value="0"
							onchange="hideboth()" checked="checked">否
					</div>
				</div>
				
				<input type="hidden" id="tagIds" name="tagIds" value=""/>
				<div class="ui-form-item">
					<label class="ui-label mt10">首页展示位置:<br>相同位置后设置的商品<br>将替换之前设置的商品</label>
					<div class="recommand timer pd" id="location">
						</div>
				</div>


				<div id="isAuction" style="display: none;">
					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span> <span
							class="toggle videoToggle imageToggle infoToggle">竞拍底价（元）：</span>
							<span class="toggle bodyToggle">竞拍底价（元）：</span> </label> <input
							class="ui-input validate[required,number,min[1]"
							onblur="bu(this)"
							onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\d.]/g,'')}else{this.value=this.value.replace(/\D+\./g,'')}"
							value="<#if prod??>${prod.price!''}<#else>0</#if>"
							name="saleprice" id="saleprice" data-is="isAmount isEnough"
							autocomplete="off" disableautocomplete="">
					</div>

					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>开拍时间: </label> <input
							class="ui-input ui_timepicker validate[required,past[#startDate1]]"
							type="text" name="startDate1" id="startDate1"
							data-is="isAmount isEnough">
					</div>
					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>截止时间：</label> <input
							class="ui-input ui_timepicker validate[required,past[#biddingDate1]]"
							type="text" name="biddingDate1" id="biddingDate1"
							data-is="isAmount isEnough">
					</div>
				</div>

			</div>

			<div class="inputs_right">

				<div class="ui-form-item">
					<label class="ui-label mt10">媒体情况：</label> 媒体位置&nbsp;&nbsp;<input
						style="width: 250px;" class="ui-input" id="exclusiveUser0" name="locationstr"
						value="<#if jsonView??>${jsonView.locationstr!''}</#if>"
						placeholder="例如：北京"> 覆盖人次&nbsp;&nbsp;<input
						style="width: 250px;" class="ui-input" id="exclusiveUser1" name="personnum"
						onkeyup="value=value.replace(/[^\d]/g,'')"
						value="<#if jsonView??>${jsonView.personnum!''}</#if>"
						placeholder="例如：1000万人"> 线路概况&nbsp;&nbsp;<input
						style="width: 250px;" class="ui-input" id="exclusiveUser2" name="linesurvey"
						value="<#if jsonView??>${jsonView.linesurvey!''}</#if>"
						placeholder="例如：北京六环内470余条公交线路"> 媒体概况&nbsp;&nbsp;<input
						style="width: 250px;" class="ui-input" id="exclusiveUser3" name="mesurvey"
						value="<#if jsonView??>${jsonView.mesurvey!''}</#if>"
						placeholder="例如：19寸公交彩色液晶电视"><br> 媒体属主&nbsp;&nbsp;<input
						style="width: 250px;" class="ui-input" id="exclusiveUser4" name="mediaowner"
						value="<#if jsonView??>${jsonView.mediaowner!''}</#if>"
						placeholder="例如：北广传媒"> 媒体数量&nbsp;&nbsp;<input
						style="width: 250px;" class="ui-input" id="exclusiveUser5" name="mediacount"
						value="<#if jsonView??>${jsonView.mediacount!''}</#if>"
						placeholder="例如：12000辆车24000块屏">

				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10">产品搜索标签：</label>
					<textarea rows="6" cols="40" style="resize: none;" name="tags"
						placeholder="用于产品搜索，可以输入多个，每行一个标签。"><#if prod?exists && prod.tags?has_content >${prod.tags!''}</#if></textarea>
				</div>
			</div>

		</div>
		<#if prod?? > <input type="hidden" name="id" value="${prod.id}" />
		</#if> <input id="img1_url" name="img1_url" type="hidden"
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
		<div class="widthdrawBtBox">
			<input type="button" id="submit" onclick="sub2()" class="block-btn"
				value="提交产品"> <input type="button" id="subWithdraw"
				class="block-btn" onclick="showdoc();" value="上传产品图片">
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
				<label class="ui-label mt10"><span class="ui-form-required"></span>媒体优势图</label>
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
				<label class="ui-label mt10"><span class="ui-form-required"></span>受众分析图</label>
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

<script type="text/javascript">
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
	//竞价选项	    
	function showisAuction() {
		$("#isAuction").show();
	}
	function hideboth() {
		$("#isAuction").hide();
	}
	flag = true;
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
</@frame.html>
