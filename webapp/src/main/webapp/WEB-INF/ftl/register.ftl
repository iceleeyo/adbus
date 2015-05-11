<#import "template/template.ftl" as frame>
<#global menu="用户注册">
<@frame.html title="用户注册" left=false nav=false js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js", "js/jquery.datepicker.region.cn.js"] css=["js/jquery-ui/jquery-ui.css"]>

<script type="text/javascript">
    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTriggers:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
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

                        $("#agreement").change(function() {
                            if ($(this).is(":checked")) {
                                $("#register").removeClass("ui-button-disabled");
                            } else {
                                $("#register").addClass("ui-button-disabled");
                            }
                        });

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
        if (!$("#userForm2").validationEngine('validate'))
            return;
        if (!$("#agreement").is(":checked")) {
            jDialog.Alert("请同意免责条款");
            return;
        }
		var username = ($("#username").val());
		var name = ($("#firstName").val());
		var password = $("#password").val();
		var phone = ($("#phone").val());
		var email=($("#email").val());
		if(username==""){
			jDialog.Alert("请填写用户名");
			return;
		}
		if(name==""){
			jDialog.Alert("请填写用户昵称");
			return;
		}
		if(password==""){
			jDialog.Alert("请填写用户密码");
			return;
		}
		if(phone == ""){
			jDialog.Alert("请填写电话");
			return;
		}
		if(email == ""){
			jDialog.Alert("请填写邮件地址");
			return;
		}
		$('#userForm2').ajaxForm(function(data) {
			if(data.user!=null){
				jDialog.Alert("注册成功,现在将进入系统!");
			}		
			var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/order/myTask/1";
			   	clearTimeout(uptime);
            },2000);
		}).submit();

	}
</script>
<style type="text/css">
    .ls-10 {clear: both; float: none; margin: auto;}
    .ad-agreement textarea {width: 100%; font-size: 12px;}
</style>
<div class="withdraw-wrap color-white-bg fn-clear">

							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="doRegister"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									用户注册
									<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
								</div>
								<div class="withdrawInputs">
									<div class="inputs">
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10">
											<span
                                                    class="ui-form-required">*
											</span>用户名[登录帐号]:
                                            </label>
                                            <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[6],maxSize[12],ajax[ajaxUserCall]]"
												type="text" name="username" id="username"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="6-12位英文、数字、下划线">
                                        </div>
                                        <div class="ui-form-item">
											<label class="ui-label mt10">
											<span
												class="ui-form-required">*
											</span>用户昵称:
											</label> 
												<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[6],maxSize[12]]"
												type="text" name="firstName" id="firstName"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="6-12位中英文、数字、下划线">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10">
											<span
												class="ui-form-required">*
											</span>密码:
											</label> 
												<input class="ui-input validate[required,minSize[6],maxSize[20]]"
												type="password" name="password" id="password"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="请输入6-20位密码">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>请确认密码:</label> <input
												class="ui-input validate[required,equals[password]]" type="password" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请再输入一次密码">
											<p class="ui-term-placeholder"></p>

										</div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>联系电话:</label>
                                                    <input
												class="ui-input validate[required,custom[phone]]" type="text" name="phone"
												id="phone" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入联系电话">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>邮箱地址:</label>
                                                    <input
												class="ui-input validate[required,custom[email]]" type="text" name="email"
												id="email" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入邮箱地址">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10">公司名称:</label>
                                                    <input
												class="ui-input" type="text" name="company"
												id="company" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入所属公司名称">
                                        </div>
                                        
<#--                                        <div class="ui-form-item">
                                            <label class="ui-label mt10">其他备注:</label>
                                                    <input
												class="ui-input" type="text" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="其他备注信息">
                                        </div>-->
                                        </div>
<div class = "ad-agreement">
	<TEXTAREA id="agreementstr" name="agreementstr" type="text"  rows="16">
1.特别提示
1.1 广告拟合竞价系统中心（以下称“系统中心”）同意按照本协议的规定提供竞价等相关服务（以下称“本服务”）。为获得本服务，服务使用人（以下称“用户”）应当同意本协议的全部条款并按照页面上的提示完成全部的注册程序。
1.2 一旦注册并使用系统中心提供的本服务，即视为用户已了解并完全同意本条款各项内容，包括系统中心对本协议随时所做的任何修改。

2.服务内容
2.1 本服务的具体内容由系统中心根据实际情况提供，系统中心保留变更、中断或终止部分网络服务的权利。
2.2 用户理解，系统中心仅提供相关的服务，除此之外与相关服务有关的设备（如AP等设置）及所需的费用（如竞价费用等）均应由用户自行负担。

3.使用规则
3.1 用户在申请使用本服务时，必须向系统中心提供本人真实、正确、最新及完整的个人资料。如果因注册信息不真实而引起的问题及其后果，系统中心不承担任何责任。
3.2 用户的帐号和密码由用户负责保管。每个用户都要对其帐户中的所有活动和事件负全责。如果用户未保管好自己的帐号和密码而对用户、系统中心或第三方造成的损害，用户将负全部责任。
3.3 用户在使用本服务过程中，必须遵循以下原则：
遵守中国有关的法律和法规；
不得为任何非法目的而使用本服务系统；
遵守所有与本服务有关的网络协议、规定和程序；
不得利用本服务系统进行任何可能对互联网的正常运转造成不利影响的行为；
不得利用本服务系统传输任何骚扰性的、中伤他人的、辱骂性的、恐吓性的、庸俗淫秽的或其他任何非法的信息资料；
不得利用本服务系统进行任何不利于系统中心的行为；
3.4 如用户在使用本服务时违反任何上述规定，系统中心或及其授权的人有权要求用户改正或直接采取一切必要的措施，以减轻用户不当行为造成的影响。

4.隐私保护
4.1 保护用户隐私是系统中心的重点原则，系统中心通过技术手段、提供隐私保护服务功能、强化内部管理等办法充分保护用户的个人资料安全。
4.2 系统中心保证不对外公开或向第三方提供用户注册的个人资料，及用户在使用服务时存储的非公开内容，但下列情况除外：a.事先获得用户的明确授权b.按照相关司法机构或政府主管部门的要求；c.维护社会公众利益或系统中心的合法利益。
4.3 在不透露单个用户隐私资料的前提下，系统中心有权对整个用户数据库进行分析并对用户数据库进行商业上的利用。

5.免责声明
5.1 用户将信息等资料上传后，有可能会被其他组织或个人复制、转载、擅改或做其它非法用途，广用户必须充分意识此类风险的存在。用户明确同意其使用本服务所存在的风险（包括但不限于受到第三方侵权或对第三方造成侵权）将完全由其自己承担；因其使用本服务而产生的一切后果也由其自己承担，系统中心对此不承担任何责任。
5.2 系统中心不担保本服务一定能满足用户的要求，也不担保本服务不会中断，对本服务的及时性、安全性、准确性、真实性、完整性也都不作担保。
5.3 系统中心不保证为向用户提供便利而设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由本服务实际控制的任何网页上的内容，系统中心不承担任何责任。
5.4 对于因不可抗力或系统中心不能控制的原因造成的网络服务中断或其它缺陷，系统中心不承担任何责任，但将尽力减少因此而给用户造成的损失和影响。
5.5 用户同意，对于本服务向用户提供的系统产品或者服务的质量缺陷本身及其引发的任何损失，系统中心无需承担任何责任。

6.服务变更、中断或终止
6.1 如因系统维护或升级的需要而需暂停网络服务、服务功能的调整，系统中心将尽可能事先在平台上进行通告。
6.2 如发生下列任何一种情形，系统中心有权单方面中断或终止向用户提供服务而无需通知用户：a.用户提供的个人资料不真实；b.用户违反本服务条款中规定的使用规则；C.未经系统中心同意，不得将平台用于其他商业目的。
6.3 除前款所述情形外，系统中心同时保留在不事先通知用户的情况下随时中断或终止部分或全部本服务的权利，对于所有服务的中断或终止而造成的任何损失，系统中心无需对广告主或广告代理商或任何第三方承担任何责任。

7.违约赔偿
用户同意保障和维护系统中心及其他用户的利益，如因用户违反有关法律、法规或本协议项下的任何条款而给系统中心或任何其他第三人造成损失，用户同意承担由此造成的损害赔偿责任。

8.法律管辖
8.1 本协议的执行和解释及争议的解决均应适用中国法律。
8.2 如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；协商不成时，任何一方均应向系统中心所有者所在地的人民法院提起诉讼。

9.通知和送达
本协议项下所有的通知均可通过网站公告、电子邮件或手机短信等方式进行；该等通知于发送之日视为已送达收件人。

10.其他规定
10.1 本协议构成双方对本协议之约定事项及其他有关事宜的完整协议，除本协议规定的之外，未赋予本协议各方其他权利。
10.2 如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。
10.3 本协议所有条款的最终解释权属于系统中心。

	</TEXTAREA>
	</div>
	<div class="iwifi-ad">&nbsp;</div>
	<div class="iwifi-ad">
		<input id="agreement" type="checkbox"  name=""> 同意《免责条款》
	</div>
    <div class="ui-form-item widthdrawBtBox">
	<div class="iwifi-btn">
        <input type="button" id="register" class="block-btn ui-button-disabled"
               onclick="sub();" value="创建用户">
        <input type="button" id="cancel" class="block-btn"
               onclick="javascript:history.go(-1);" value="返回">
	</div>
	</div>
</div>
	</form>
</div>
</@frame.html>
