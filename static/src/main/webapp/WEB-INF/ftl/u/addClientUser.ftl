<#import "../template/template.ftl" as frame> <#global menu="销售员客户管理 ">
<@frame.html title="销售员客户管理 " js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>


<script type="text/javascript">
function go_back(){
	history.go(-1);
}
    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        });
        
        
        
    });
</script>

<script type="text/javascript">
	$(document).ready(function() {
			
					});


function uploadImaget(formId) { 
    var image_name=$("#"+formId+"_file").val();
    if(image_name != ''){
    var imgs=image_name.split(".");
    var img_subfier= imgs[imgs.length-1].toLocaleLowerCase();
    var img_parr = ["jpg", "jpeg", "gif","png"]; 
    
    if(image_name !=''){
        if($.inArray(img_subfier, img_parr) ==-1){
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
             if(data !=null && data!=""){
                  $("#"+formId+"_img").attr("src","${rc.contextPath}/upload_temp/"+data);
                  $("#"+formId+"_url").val(data);
                   } 
                 }
        }; 
        $("#"+formId).ajaxSubmit(options);
        document.getElementById(formId).reset();
        }
}
	function sub(){
	if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
			return;
		$('#userForm2').ajaxForm(function(data) {
				if(data.right){
					layer.msg("保存成功");
				}else {
					layer.msg("操作失败");
				}
				window.setTimeout(function(){
				window.location.href="${rc.contextPath}/user/clientList",
				   	clearTimeout(uptime);
							},2000) 
		}).submit();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="${rc.contextPath}/user/saveClientUser"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>客户信息</span>  <a class="block-btn"
				style="margin-top: -5px;" href="javascript:void(0);"
				onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs">
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>公司名称:</label>
					<input
						class="ui-input  validate[required]" style="width:440px;"
						type="text" name="company" id="company" value="<#if userDetail??>${userDetail.company!''}</#if>"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>公司地址:</label>
					<input
						class="ui-input  validate[required]" style="width:440px;"
						type="text" name="companyAddr" id="companyAddr" value="<#if userDetail??>${userDetail.companyAddr!''}</#if>"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">邮编:</label>
					<input
						class="ui-input "
						type="text" name="zipCode" id="zipCode" value="<#if userDetail??>${userDetail.zipCode!''}</#if>"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>法定代表人:</label>
					<input
						class="ui-input  validate[required,minSize[2],maxSize[100]]"
						type="text" name="legalman" id="legalman" value="<#if userDetail??>${userDetail.legalman!''}</#if>"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>联系人:</label>
					<input
						class="ui-input  validate[required,minSize[2],maxSize[100]]"
						type="text" name="relateman" id="relateman" value="<#if userDetail??>${userDetail.relateman!''}</#if>"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">联系电话:</label>
					<input class="ui-input validate[custom[phone]]"
						type="text" name="phone" id="phone" data-is="isAmount isEnough" value="<#if userDetail??>${userDetail.phone!''}</#if>"
						autocomplete="off" disableautocomplete="">
				</div>
				<#if userDetail?? >
				 <input type="hidden" name="id" value="${userDetail.id}" />
				 <input type="hidden" name="username" value="${userDetail.username}" />
				 </#if>
				<input id ="img1_url" name="user_license" type="hidden" value="<#if jsonView?? && jsonView.user_license?has_content>${jsonView.user_license}</#if>"/>
		 <input id ="img2_url" name="user_tax" type="hidden" value="<#if jsonView?? && jsonView.user_tax?has_content>${jsonView.user_tax}</#if>"/>
		 <input id ="img3_url" name="user_code" type="hidden" value="<#if jsonView?? && jsonView.user_code?has_content>${jsonView.user_code}</#if>"/>
</form>

				
				 <div class="ui-form-item" id="file">
					<label class="ui-label mt10">营业执照副本复印件:</label>
				 <form id="img1" method="post" enctype="multipart/form-data"> 
                     <img src="<#if jsonView?? && jsonView.user_license?has_content>${rc.contextPath}/upload_temp/${jsonView.user_license}</#if>" id="img1_img" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id="img1_file" style="margin-top:-100px;" name="img1_file" type="file" onchange="uploadImaget('img1');"/>
                 </form>
				</div>
				<div class="ui-form-item" id="file">
					<label class="ui-label mt10"><span class="ui-form-required"></span>税务登记证副本复印件:</label>
				 <form id="img2" method="post" enctype="multipart/form-data"> 
                     <img src="<#if jsonView?? && jsonView.user_tax?has_content>${rc.contextPath}/upload_temp/${jsonView.user_tax}</#if>" id="img2_img" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id ="img2_file" style="margin-top:-100px;" name="img2_file" type="file" onchange="uploadImaget('img2');"/>
                 </form>
				</div>
				<div class="ui-form-item" id="file">
					<label class="ui-label mt10"><span class="ui-form-required"></span>组织结构代码证书:</label>
				 <form id="img3" method="post" enctype="multipart/form-data"> 
                     <img src="<#if jsonView?? && jsonView.user_code?has_content>${rc.contextPath}/upload_temp/${jsonView.user_code}</#if>" id="img3_img" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id ="img3_file" style="margin-top:-100px;" name="img3_file" type="file" onchange="uploadImaget('img3');"/>
                 </form>
				</div>

			</div>
			<div class="widthdrawBtBox">
				<input type="button" id="subWithdraw" class="block-btn"
					onclick="sub();" value="提交">
			</div>
		</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>1.请输入正确的电话及公司信息。</li>
			</ol>
		</div>
	
</div>
</@frame.html>
