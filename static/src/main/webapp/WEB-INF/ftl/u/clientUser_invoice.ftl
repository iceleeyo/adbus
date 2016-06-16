<#import "../template/template.ftl" as frame> <#global menu="发票录入">
<@frame.html title="发票信息录入" js=["js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"]/>
<style type="text/css">
.ui-form-item div {
	display: inline-block;
}

dd {
	text-indent: 1em;
}

dt {
	padding: 4px 0px;
}
</style>
<script type="text/javascript">
    $(document).ready(function() {
    <#if invoiceView??>
         if(${jpaInvoice.type}==0){
       $("#other").css('display','none');
    }
    </#if>
  
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
	$(document)
			.ready(
					function() {


					});



	function sub(){
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
	   document.getElementById('subWithdraw').setAttribute('disabled',true); 
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
	
	function showother(){
	     $("#other").css('display','block'); 
	}
	function hideother(){
	     $("#other").css('display','none'); 
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
	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post"
		action="${rc.contextPath}/user/saveClientInvoice?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
		enctype="multipart/form-data">
		
		<div class="withdraw-title fn-clear">
			发票信息管理
		</div>
		<div class="withdrawInputs" style="margin-left: 10px;">
			<div class="inputs">
				<div class="ui-form-item">
			 <#if jpaInvoice?? && jpaInvoice.id gt 0 > 
			    <input type="hidden" name="id" value="${(jpaInvoice.id)!''}" />
			 </#if>
				<input type="hidden" name="userId" value="${(userDetail.username)!''}" />
					<label class="ui-label mt10"> <span
						class="ui-form-required">* </span>发票抬头:
					</label> <input
						class="ui-input validate[required,custom[noSpecialLetterChinese],maxSize[120]]"
						type="text" name="title" id="title" readonly="readonly" style="border: none;"
						value="${(userDetail.company)!''}"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<#if !(userDetail.company?has_content)>
				<div class="ui-form-item">
					发票抬头必须填所属公司名称，请在用户信息菜单完善相关信息
				</div>
				</#if>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄地址:</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],maxSize[120]]"
						type="text" name="mailaddr"
						value="${(jpaInvoice.mailaddr)!''}" id="mailaddr"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄联系人:</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[12]]"
						type="text" name="contactman"
						value="${(jpaInvoice.contactman)!''}" id="contactman"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>联系方式:</label>
					<input class="ui-input validate[required,custom[mobilephone]]"
						type="text" name="phonenum"
						value="${(jpaInvoice.phonenum)!''}" id="phonenum"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<div id="other" style="display: block">
					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>税务登记证号:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="taxrenum"
							value="${(jpaInvoice.taxrenum)!''}" id="taxrenum"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
						<p class="ui-term-placeholder"></p>

					</div>
					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>基本户开户银行名称:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="bankname"
							value="${(jpaInvoice.bankname)!''}" id="bankname"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>

					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>基本户开户账号:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="accountnum"
							value="${(jpaInvoice.accountnum)!''}" id="accountnum"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>

					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>注册场所地址:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="regisaddr"
							value="${(jpaInvoice.regisaddr)!''}" id="regisaddr"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>

					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>注册固定电话:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="fixphone"
							value="${(jpaInvoice.fixphone)!''}" id="fixphone"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>
         <input id ="img1_url" name="bank_license" type="hidden" value="<#if jsonView?? && jsonView.bank_license?has_content>${jsonView.bank_license}</#if>"/>
		 <input id ="img2_url" name="user_tax" type="hidden" value="<#if jsonView?? && jsonView.user_tax?has_content>${jsonView.user_tax}</#if>"/>
		 <input id ="img3_url" name="common_tag" type="hidden" value="<#if jsonView?? && jsonView.common_tag?has_content>${jsonView.common_tag}</#if>"/>
		 <input id ="img4_url" name="other_qualifi" type="hidden" value="<#if jsonView?? && jsonView.other_qualifi?has_content>${jsonView.other_qualifi}</#if>"/>

			</form>		
			<div class="ui-form-item" id="file">
					<label class="ui-label mt10">银行开户许可证复印件:</label>
				 <form id="img1" method="post" enctype="multipart/form-data"> 
                     <img src="<#if jsonView?? && jsonView.bank_license?has_content>${rc.contextPath}/upload_temp/${jsonView.bank_license}</#if>" id="img1_img" width="200" height="100" border="1px solid #d0d0d0;"/>
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
					<label class="ui-label mt10"><span class="ui-form-required"></span>一般纳税人资格认证复印件:</label>
				 <form id="img3" method="post" enctype="multipart/form-data"> 
                     <img src="<#if jsonView?? && jsonView.common_tag?has_content>${rc.contextPath}/upload_temp/${jsonView.common_tag}</#if>" id="img3_img" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id ="img3_file" style="margin-top:-100px;" name="img3_file" type="file" onchange="uploadImaget('img3');"/>
                 </form>
				</div>
				<div class="ui-form-item" id="file">
					<label class="ui-label mt10"><span class="ui-form-required"></span>其他资质文件:</label>
				 <form id="img4" method="post" enctype="multipart/form-data"> 
                     <img src="<#if jsonView?? && jsonView.other_qualifi?has_content>${rc.contextPath}/upload_temp/${jsonView.other_qualifi}</#if>" id="img4_img" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id ="img4_file" style="margin-top:-100px;" name="img4_file" type="file" onchange="uploadImaget('img4');"/>
                 </form>
				</div>
				
				</div>
				<div class="widthdrawBtBox">
					<input type="button" id="subWithdraw" class="block-btn"
						onclick="sub();" value="提交">
				</div>
				
			</div>
		</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<dl>
				<dt>1.&nbsp如果您开具的是普通发票：</dt>
				<dd>*发票抬头可选择填写个人或公司名称。</dd>
				<dd>*您所填写的普通发票邮寄地址将绑定到您的账号，该账号订单所开普通发票都将邮寄到该地址。</dd>
				<dt>2.&nbsp如果您开具的是增值税专用发票：</dt>
				<dd>*发票抬头可选择填写个人或公司名称。</dd>
				<dd>*您所填写的增票邮寄地址将绑定到您的账号，该账号订单所开增值税专用发票都将邮寄到该地址。</dd>
				<dd>*税务登记证号（必须是您公司《税务登记证》的编号，一般为15位，请仔细核对后输入）。</dd>
				<dd>*开户银行（必须是您公司银行开户许可证上的开户银行）。</dd>
				<dd>*银行账号（必须是您公司开户许可证上的银行账号）。</dd>
				<dd>*注册地址（必须是您公司营业执照上的注册地址）。</dd>
				<dd>*固定电话（请提供能与您公司保持联系的有效电话）。</dd>
				<dd>*若您首次开具增值税专用发票，需要提供加盖公章的①营业执照复印件②税务登记证复印件③一般纳税人资格认证复印件，填写资质信息后选择上传资质证件扫描件。我们收到您的开票资料后，会尽快为您处理。</dd>
				<dd>*请务必确保相关信息的真实准确性，我司将根据您所提供的信息进行增值税专用发票的开具，如因填写失误造成发票开具错误，将会导致贵公司不能及时入账抵扣，带来税金损失。</dd>
	
			</dl>
		</div>
	
</div>
<script type="text/javascript">
$(document).ready(function(){
 	 $('input').iCheck({
	    checkboxClass: 'icheckbox_square-green',
	    radioClass: 'iradio_square-green',
	    increaseArea: '20%' // optional
	  }); 
$('input').on('ifChecked', function(event){
			var p =($(this).val());
			if($(this).attr("name")=='type'){
				if(p=='normal'){
				hideother();
				}else {
				 showother();
				}	
			}
			});
 	  initiCheck();
	  initInvoiceRadioIcheck();
});
</script>
</@frame.html>
