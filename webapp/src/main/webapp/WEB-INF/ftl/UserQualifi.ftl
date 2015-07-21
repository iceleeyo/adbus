<#import "template/template.ftl" as frame>
<#global menu="资质管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="资质信息录入" js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js"] css=["js/jquery-ui/jquery-ui.css"]>
<script type="text/javascript">
	$(document).ready(function() {

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
</script>
<div class="withdraw-wrap color-white-bg fn-clear">					
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="updateQualifi"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									用户资质
								</div>
	<div class="withdrawInputs">
		<div class="inputs">
			<div class="ui-form-item">
			    <label class="ui-label mt10">用户资质[图片]</label>
			    <#if attachment??>
			         <a href="${rc.contextPath}/downloadFile/${attachment.userId!''}/${attachment.id!''}"
		          onclick="return hs.expand(this)">
		        <img src="${rc.contextPath}/downloadFile/${attachment.userId!''}/${attachment.id!''}"
		             class="m11" width="240"/>
		    </a><br>
		    <label class="ui-label mt10">修改资质</label>
		    <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="user_pic" id="Sfile" class="">
												</div>
											</div>
			    <#else>
			    <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="user_pic" id="Sfile" class="">
												</div>
											</div>
			    </#if>
									  
	    	</div>
	    	<div class="ui-form-item" tip="上传资质图片,审核通过可以参与商品竞价!"> 
				<label class="ui-label mt10">认证状态:</label>
				<div id="up" style="padding-top: 10px;">
				<#if userDetail.ustats=="init">
					未上传
					<#elseif userDetail.ustats=="upload">
					待认证
					<#elseif userDetail.ustats=="unauthentication">
					认证不通过,请重新上传资质
				    <#else>
				    认证通过
				</#if>
				</div>
			</div>
	    	<div class="ui-form-item widthdrawBtBox">
			<input type="button" id="subWithdraw" class="block-btn"
				onclick="sub();" value="保存">
		</div>
		</div>
		</div>
		
			<div class="worm-tips" >
            <div class="tips-title"><span class="icon"></span> 温馨提示</div>
	          <ol>
              <li>1.请选择合法的用户资质[图片]进行上传、审核通过(已认证)后可参与竞价商品的竞拍</li>
              <li>2.如果您填写的信息不正确可能会导致下单失败。 </li>
              <li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
	          </ol>
	        </div>
							</form>
</div>

    
</@frame.html>
