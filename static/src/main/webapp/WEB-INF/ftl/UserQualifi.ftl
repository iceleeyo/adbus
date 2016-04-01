<#import "template/template.ftl" as frame> <#global menu="资质管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="资质信息录入" js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js"]
css=["js/jquery-ui/jquery-ui.css"]>
<script type="text/javascript">
	$(document).ready(function() {
 $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            maxErrorsPerField: 1,
        });
					});

	function sub(){
		$('#userForm2').ajaxForm(function(data) {
				if(data.left==true){
					jDialog.Alert("保存成功");
				}else {
					jDialog.Alert(data.right);
				}
			var uptime = window.setTimeout(function(){
				window.location.reload();
			   	clearTimeout(uptime);
						},1500)
		}).submit();
	}
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
	function showform(){
	    $("#updateform").css("display","inline");
	}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	
		<div class="withdraw-title fn-clear">用户资质</div>
		<div class="withdrawInputs">
			<div class="inputs">
				   
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
				   
				   
				<div class="ui-form-item" tip="上传资质图片,审核通过可以参与商品竞价!">
					<label class="ui-label mt10">认证状态:</label>
					<div id="up" style="padding-top: 10px;"><#if
						userDetail.ustats=="init"> 未上传 <#elseif
						userDetail.ustats=="upload"> 待认证 <#elseif
						userDetail.ustats=="unauthentication"> 认证不通过,请重新上传资质 <#else> 认证通过
						</#if></div>
				</div>
				<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="updateQualifi"
		enctype="multipart/form-data">
		 <input id ="img1_url" name="user_license" type="hidden" value="<#if jsonView?? && jsonView.user_license?has_content>${jsonView.user_license}</#if>"/>
		 <input id ="img2_url" name="user_tax" type="hidden" value="<#if jsonView?? && jsonView.user_tax?has_content>${jsonView.user_tax}</#if>"/>
		 <input id ="img3_url" name="user_code" type="hidden" value="<#if jsonView?? && jsonView.user_code?has_content>${jsonView.user_code}</#if>"/>
				<div class="widthdrawBtBox">
						<input type="button"id="subWithdraw" class="block-btn" onclick="sub();" value="保存">
				</div>
			</div>
		</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>1.请选择合法的用户资质[图片]进行上传、审核通过(已认证)后可参与竞价商品的竞拍</li>
				<li>2.如果您填写的信息不正确可能会导致下单失败。</li>
				<li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
				<li>4.资质经过认证后再修改则需重新审核。</li>
			</ol>
		</div>
	</form>
</div>


</@frame.html>
