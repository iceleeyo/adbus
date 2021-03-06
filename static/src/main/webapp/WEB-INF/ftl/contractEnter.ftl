<#import "template/template.ftl" as frame> <#global menu="屏幕广告合同">
<@frame.html title="合同录入" js=["js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js","js/jquery-ui-timepicker-addon.js","js/jquery-ui-timepicker-zh-CN.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">
	$(document).ready(function() {

		$("#userForm2").validationEngine({
			validationEventTrigger : "blur", //触发的事件  validationEventTriggers:"keyup blur",
			inlineValidation : true,//是否即时验证，false为提交表单时验证,默认true
			success : false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
			promptPosition : "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
			showOneMessage : true,
			maxErrorsPerField : 1,
		//failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
		//success : function() { callSuccessFunction() },//验证通过时调用的函数
		});

		var industryId = $
		{
			(contractView.jpaContract.industryId)
			!''
		}
		;
		var contractType = '${(contractView.jpaContract.contractType)!'
		'}';
		$("#industry option").each(function() {
			if ($(this).val() == industryId) {
				$(this).attr("selected", "selected");
			}
		});
		$("#contractType option").each(function() {
			if ($(this).val() == contractType) {

				$(this).attr("selected", "selected");
			}
		});

	});
</script>

<script type="text/javascript">
	function go_back() {
		history.go(-1);
	}
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
															'<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;" class="validate[required]"/><input class="btn-sm btn-wrong" type="button" value="删除" style="margin-top:10px;" onclick="del_2('
																	+ j
																	+ ')"/></div>');
											j = j + 1;
										});

					});

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}

	function sub() {

		if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
			return;
		var code = ($("#code").val());
		var name = ($("#name").val());
		var startDate = $("#startDate").val();
		var endDate = ($("#endDate").val());
		var amounts = ($("#amounts").val());
		var contractType = ($("#contractType").val());
		Sfile = ($("#Sfile").val());
		
		if (endDate < startDate) {
			jDialog.Alert("终止时间不能小于开始时间");
			return;
		}
		/*
		alert(3);
		return;
		var bb=false;
		var userid=$("#username").val();
		if(typeof(userid)=="undefined"){
		   bb=true;
		}
		else{
		$.ajax({
			url:"${rc.contextPath}/user/isAdvertiser/"+userid,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left == true) {
				   bb=true;
				} else {
					bb=false;
					jDialog.Alert(data.right);
				}
			}
		}); 
		}*/
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			var uptime = window.setTimeout(function() {
				window.location.href = "${rc.contextPath}/contract/list"
				clearTimeout(uptime);
			}, 2000)

		}).submit();
		document.getElementById('subWithdraw').setAttribute('disabled', true);
		$("#subWithdraw").css("background-color", "#85A2AD");
		var uploadProcess = {
			upath : '${rc.contextPath}/upload/process'
		};
		$('#progress1').anim_progressbar(uploadProcess);
	}
	$(document).ready(function() {
		$("#otherindustry").click(function() {

			if ($("#otherindustry").is(":checked")) {
				$("#industry").hide();
				$("#industryname").css("display", "inline");
			} else {
				$("#industryname").css("display", "none");
				$("#industryname").val("");
				$("#industry").show();

			}
		});
		

		//author:pxh 2015-05-20 22:36

		$("#username").autocomplete({
			minLength : 0,
			source : "${rc.contextPath}/user/autoComplete",
			change : function(event, ui) {
				/*if(ui.item!=null){alert(ui.item.value);}*/
				//table.fnDraw();
			},
			select : function(event, ui) {
				$('#username').val(ui.item.value);
				//table.fnDraw();
			},
		}).focus(function() {
			$(this).autocomplete("search");
		});

	});
</script>
<script type="text/javascript">
	function bu(txtObj) {
		txtObj.value = Number(txtObj.value).toFixed(2);
	}
</script>


<div class="withdraw-wrap color-white-bg fn-clear">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post"
		action="${rc.contextPath}/contract/saveContract?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
		enctype="multipart/form-data">
		<#if contractView?? && contractView.jpaContract??> <input type="hidden"
			name="id" value="${(contractView.jpaContract.id)!''}" /> </#if>
		<div class="withdraw-title fn-clear">
			<span>合同详情录入</span> <a class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);" onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs">

				<div class="ui-form-item">
					<label class="ui-label mt10"> <span
						class="ui-form-required">* </span>广告主:
					</label> <span> <input id="username" name="userId"
						value="${(contractView.jpaContract.userId)!''}"
						class="ui-input validate[required,custom[noSpecialLetterChinese]]"
						placeholder="请选择广告主" />
					</span>
				</div>


				<div class="ui-form-item">
					<label class="ui-label mt10"> <span
						class="ui-form-required">*</span>合同编号:
					</label> <input
						class="ui-input validate[required,custom[noSpecialContratNum],maxSize[120]]"
						type="text" name="contractCode" id="code"
						value="${(contractView.jpaContract.contractCode)!''}"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="中英文、数字、下划线">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>广告/节目内容名称:</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],maxSize[120]]"
						type="text" name="contractName"
						value="${(contractView.jpaContract.contractName)!''}" id="name"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
					<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>

				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同金额:</label>
					<input class="ui-input validate[required,custom[number]]"
						onblur="bu(this)"
						onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\d.]/g,'')}else{this.value=this.value.replace(/[^\d.]/g,'')}"
						type="text" name="amounts" id="amounts"
						data-is="isAmount isEnough"
						value="${(contractView.jpaContract.amounts)!''}" autocomplete="off"
						disableautocomplete="" placeholder="请输入合同金额" /> 
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"> <span
						class="ui-form-required"> </span>绑定大合同:
					</label> 
					<select class="ui-input" name="parentid" id="parentid" >
						<option value="0" selected="selected">请选择要绑定的合同</option> <#if
						contracts?exists> <#list contracts as c>
						<option value="${c.id}">${c.contractName!''}</option> </#list>
						</#if>
					</select>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"> <span
						class="ui-form-required">* </span>媒体类型:
					</label> 
					<select class="ui-input" name="type" id="type">
					 <#list types as item>
						<option value="${item.name()}" <#if (contractView?? && contractView.jpaContract.type== item.name())
							>selected="selected"</#if>>${item.typeName}</option>
						</#list>
					</select>
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属行业:</label>

					<select id="industry" name="industryId" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						style="width: 173px; height: 38px;"> <#list industries as
						industry>
						<option value="${industry.id}">${industry.name}</option> </#list>
					</select> <input type="checkbox" id="otherindustry" />其他行业 <input
						class="ui-input validate[required]" style="display: none"
						type="text" name="name" id="industryname" placeholder="输入行业名称" />
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>签订日期:
					</label> <input
						class="ui-input datepicker validate[required,custom[date],past[#signDate]]"
						type="text" name="signDate1"
						value="${(contractView.jpaContract.signDate?string("yyyy-MM-dd"))!''}" id="signDate1"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>上刊日期:
					</label> <input
						class="ui-input datepicker validate[required,custom[date],past[#endDate]]"
						type="text" name="startDate1"
						value="${(contractView.jpaContract.startDate?string("yyyy-MM-dd"))!''}" id="startDate"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>下刊日期:</label>
					<input
						class="ui-input datepicker validate[required,custom[date],future[#startDate]"
						type="text" name="endDate1" id="endDate"
						data-is="isAmount isEnough"
						value="${(contractView.jpaContract.endDate?string("yyyy-MM-dd"))!''}"
						autocomplete="off" disableautocomplete="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required"></span>结算方式:</label>
					
					<textarea rows="4" cols="40" name="settle" id="remark" class=""
						data-is="isAmount isEnough" style="resize: none;" name="remark">${(contractView.jpaContract.settle)!''}</textarea>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required"></span>合同备注:</label>
					<textarea rows="4" cols="40"
						value="" id="remark"
						data-is="isAmount isEnough" style="resize: none;" name="remark">${(contractView.jpaContract.remark)!''}</textarea>
				</div>



				<div class="ui-form-item">
					<#if contractView??> <label class="ui-label mt10">合同附件：</label><br>
					<#list contractView.files as item> <#if item?has_content > <a
						href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">
						<img
	src="${rc.contextPath}/upload_temp/${item.url!''}"
	class="m11" width="240" /></a> &nbsp;&nbsp; <br> </#if> </#list> <#else> <label
						class="ui-label mt10"><span class="ui-form-required">*</span>附件上传</label>
					<div id="newUpload2">
						<div id="div_1">
							<input type="file" name="file" id="Sfile"
								class="validate[required]">
						</div>
					</div>
					<input class="btn-sm btn-success" type="button" id="btn_add2"
						value="增加一行" style="margin-top: 10px;"><br> </#if>
				</div>
			</div>
			<div class="widthdrawBtBox">
				<input type="button" id="subWithdraw" class="block-btn"
					onclick="sub();" value="保存合同">
			</div>

			<div id="progress1">
				<div class="percent"></div>
				<div class="pbar"></div>
				<div class="elapsed"></div>
			</div>
		</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>1.请输入正确的合同号及金额。</li>
				<li>2.如果您填写的信息不正确可能会导致下单失败。</li>
				<li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
			</ol>
		</div>
	</form>
</div>

</@frame.html>
