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
						},2000)
		}).submit();
	}
	function showform(){
	    $("#updateform").css("display","inline");
	}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="updateQualifi"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">用户资质</div>
		<div class="withdrawInputs">
			<div class="inputs">
				   <div class="ui-form-item">
					<label class="ui-label mt10">营业执照副本复印件:
					</label> 
			<#if attachments?has_content && typelist?? && typelist?seq_contains(10)> 
				<#list attachments as item>
				 <#if item?has_content && item.type==10> 
					<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
						onclick="return hs.expand(this)"> <img
						src="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
						class="m11" width="240" /></a><br> 
					</#if> 
				 </#list> <br>
					 <label class="ui-label mt10">修改营业执照副本复印件:</label>
						<div id="div_1">
							<input type="file" name="user_license" id="Sfile2"/>
						</div>
					<#else>
					<div id="newUpload2">
						<div id="div_1">
							<input type="file" name="user_license" id="Sfile2"
								>
						</div>
					</div>
					</#if>

				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">税务登记证副本复印件:
					</label> <#if attachments?has_content && typelist?? && typelist?seq_contains(10)> <#list attachments as item> <#if
					item?has_content && item.type==11> <a
						href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
						onclick="return hs.expand(this)"> <img
						src="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
						class="m11" width="240" /></a><br> </#if> </#list> <br>
					<label class="ui-label mt10">修改税务登记证副本复印件:</label>
						<div id="div_1">
							<input type="file" name="user_tax" id="Sfile2"/>
						</div>
					<#else>
					<div id="newUpload2">
						<div id="div_1">
							<input type="file" name="user_tax" id="Sfile2"
								>
						</div>
					</div>
					</#if>

				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10">组织结构代码证书:
					</label> <#if attachments?has_content && typelist?? && typelist?seq_contains(10)> <#list attachments as item> <#if
					item?has_content && item.type==14> 
						<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
						onclick="return hs.expand(this)"> 
						<img src="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
						class="m11" width="240" /></a><br> </#if> </#list> <br>
					 <label class="ui-label mt10">修改组织结构代码证书:</label>
						<div id="div_1">
							<input type="file" name="user_code" id="Sfile2"/>
						</div>
					<#else>
					<div id="newUpload2">
						<div id="div_1">
							<input type="file" name="user_code" id="Sfile2"
								>
						</div>
					</div>
					</#if>
				</div>
				<div class="ui-form-item" tip="上传资质图片,审核通过可以参与商品竞价!">
					<label class="ui-label mt10">认证状态:</label>
					<div id="up" style="padding-top: 10px;"><#if
						userDetail.ustats=="init"> 未上传 <#elseif
						userDetail.ustats=="upload"> 待认证 <#elseif
						userDetail.ustats=="unauthentication"> 认证不通过,请重新上传资质 <#else> 认证通过
						</#if></div>
				</div>
				<div class="ui-form-item widthdrawBtBox">
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
