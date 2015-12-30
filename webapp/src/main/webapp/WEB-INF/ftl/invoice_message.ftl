<#import "template/template.ftl" as frame> <#global menu="发票录入">
<@frame.html title="发票信息录入" js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer-site.js"]
css=["js/jquery-ui/jquery-ui.css"]>
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
         if(${invoiceView.mainView.type}==0){
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

						$("#btn_add2")
								.click(
										function() {
											$("#newUpload2")
													.append(
															'<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;"/><input type="button" value="删除" style="margin-top:10px;" onclick="del_2('
																	+ j
																	+ ')"/></div>');
											j = j + 1;
										});

					});

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}


	function sub(){
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
	   document.getElementById('subWithdraw').setAttribute('disabled',true); 
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/user/invoiceList";
			   	clearTimeout(uptime);
						},2000)
		}).submit();
		var type=$('input:radio[name="type"]:checked').val();
		if(type=="special"){
			var uploadProcess={upath:'${rc.contextPath}/upload/process'};
	 		$('#progress1').anim_progressbar(uploadProcess);
		}
		
	}
	
	function showother(){
	     $("#other").css('display','block'); 
	}
	function hideother(){
	     $("#other").css('display','none'); 
	}
	
	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post"
		action="${rc.contextPath}/user/saveInvoice"
		enctype="multipart/form-data">
		<#if invoiceView?? && invoiceView.mainView??> <input type="hidden"
			name="id" value="${(invoiceView.mainView.id)!''}" /> </#if>
		<div class="withdraw-title fn-clear">
			发票信息管理
			<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
		</div>
		<div class="withdrawInputs" style="margin-left: 10px;">
			<div class="inputs">

				<div class="ui-form-item">
					<label class="ui-label mt10"> <span
						class="ui-form-required">* </span>发票类型:
					</label> <#if invoiceView?? && invoiceView.mainView??
					&&invoiceView.mainView.type??> <#if invoiceView.mainView.type=0> <input
						class="ui-input" readonly="readonly" style="border: none;"
						type="text" value="普通发票" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""> <#else> <input
						class="ui-input" readonly="readonly" style="border: none;"
						type="text" value="专用发票" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""> </#if> <#else>
					<div class="mt10 radiobox" style="display: inline-block">
						<input type="radio" name="type" checked="checked"
							onchange="showother()" value="special">&nbsp;增值税专用发票 <input
							type="radio" name="type" onchange="hideother()" value="normal">&nbsp;普通发票&nbsp;&nbsp;
					</div>
					</#if>

				</div>

         
				<div class="ui-form-item">
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
						value="${(invoiceView.mainView.mailaddr)!''}" id="mailaddr"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄联系人:</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[12]]"
						type="text" name="contactman"
						value="${(invoiceView.mainView.contactman)!''}" id="contactman"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>联系方式:</label>
					<input class="ui-input validate[required,custom[mobilephone]]"
						type="text" name="phonenum"
						value="${(invoiceView.mainView.phonenum)!''}" id="phonenum"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<div id="other" style="display: block">
					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>税务登记证号:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="taxrenum"
							value="${(invoiceView.mainView.taxrenum)!''}" id="taxrenum"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
						<p class="ui-term-placeholder"></p>

					</div>
					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>基本户开户银行名称:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="bankname"
							value="${(invoiceView.mainView.bankname)!''}" id="bankname"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>

					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>基本户开户账号:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="accountnum"
							value="${(invoiceView.mainView.accountnum)!''}" id="accountnum"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>

					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>注册场所地址:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="regisaddr"
							value="${(invoiceView.mainView.regisaddr)!''}" id="regisaddr"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>

					<div class="ui-form-item">
						<label class="ui-label mt10"><span
							class="ui-form-required">*</span>注册固定电话:</label> <input
							class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
							type="text" name="fixphone"
							value="${(invoiceView.mainView.fixphone)!''}" id="fixphone"
							data-is="isAmount isEnough" autocomplete="off"
							disableautocomplete="">
					</div>


					<div class="ui-form-item">
						<label class="ui-label mt10"> 银行开户许可证复印件: </label> <#if
						invoiceView?exists > <#list invoiceView.files as item> <#if
						item?has_content && item.type==6> <a
							href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">
							${item.name!''}</a> &nbsp;&nbsp; &nbsp; </#if> </#list> <br>
						<#else>
						<div id="newUpload2">
							<div id="div_1">
								<input type="file" name="licensefile" id="Sfile" >
							</div>
						</div>
						</#if>

					</div>
					<div class="ui-form-item">
						<label class="ui-label mt10"> 税务登记复印件: </label> <#if invoiceView?? >
						<#list invoiceView.files as item> <#if item?has_content &&
						item.type==7> <a
							href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">
							${item.name!''}</a> &nbsp;&nbsp; &nbsp; </#if> </#list> <br>
						<#else>
						<div id="newUpload2">
							<div id="div_1">
								<input type="file" name="taxfile" id="Sfile2" >
							</div>
						</div>
						</#if>

					</div>
					<div class="ui-form-item">
						<label class="ui-label mt10"> 一般纳税人资格认证复印件: </label> <#if
						invoiceView??> <#list invoiceView.files as item> <#if
						item?has_content && item.type==8> <a
							href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">
							${item.name!''}</a> &nbsp;&nbsp; &nbsp; </#if> </#list> <#else>
						<div id="newUpload2">
							<div id="div_1">
								<input type="file" name="taxpayerfile" id="Sfile3" >
							</div>
						</div>
						</#if>

					</div>
					<div class="ui-form-item">
						<label class="ui-label mt10"> 其它: </label> 
						<#if invoiceView??> 
						<#list invoiceView.files as item> 
						   <#if item?has_content && item.type==16> 
							<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">
							${item.name!''}</a> &nbsp;&nbsp; &nbsp; 
							</#if> 
							</#list>
						 <#else>
						  <div id="newUpload2">
							<div id="div_1">
								<input type="file" name="fp_other" id="Sfile3" >
							</div>
						  </div>
						</#if>
					</div>
				</div>
				<div class="ui-form-item widthdrawBtBox">
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
				<!--<li>3.营业执照复印件、税务登记复印件、一般纳税人资格认证复印件格式支持GIF，PNG，JPG。</li>
              <!--<li>4.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>-->
			</dl>
		</div>
	</form>
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
