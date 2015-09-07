<#import "template/template.ftl" as frame>
<#global menu="用户注册">
<@frame.html title="用户注册" left=false nav=false js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js", "js/jquery.datepicker.region.cn.js"] css=["js/jquery-ui/jquery-ui.css"]>

<script type="text/javascript">
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
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
        if (!$("#agreement").is(":checked")) {
            jDialog.Alert("请同意免责条款");
            return;
        }
        $('#userForm2').ajaxForm(function(data) {
            if(data.user!=null){
                jDialog.Alert("注册成功,请注意查收邮件进行激活!");
            }
           // var uptime = window.setTimeout(function(){
             //   window.location.href="${rc.contextPath}/order/myTask/1";
               // clearTimeout(uptime);
            //},2000);
        }).submit();
//		$('#userForm2').submit();

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
											</span>用户真实姓名:
											</label> 
												<input class="ui-input  validate[required,custom[chinese],minSize[2],maxSize[12],ajax[ajaxUserCall]]" 
												type="text" name="firstName" id="firstName"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="2-12位中英文">
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
                                                    class="ui-form-required">*</span>邮箱地址:</label>
                                                    <input
												class="ui-input validate[required,custom[email]]" type="text" name="email"
												id="email" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入邮箱地址">
                                        </div>
                                        
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
												class="ui-form-required">*
											</span>公司名称:</label>
                                                    <input
												class="ui-input validate[required]" type="text" name="company"
												id="company" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入所属公司名称">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
												class="ui-form-required">*
											</span>公司地址:</label>
                                                    <input
												class="ui-input validate[required]" type="text" name="companyAddr"
												id="companyAddr" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入所属公司地址">
                                        </div>
                                         <div class="ui-form-item">
											<label class="ui-label mt10">
											<span
												class="ui-form-required">*
											</span>联系人:
											</label> 
												<input class="ui-input  validate[required,custom[chinese],minSize[2],maxSize[12]]" 
												type="text" name="relateman" id="relateman"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="2-12位中英文">
										</div>
										 <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>联系电话:</label>
                                                    <input
												class="ui-input validate[required,custom[mobilephone]]" type="text" name="phone"
												id="phone" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入联系电话">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
												class="ui-form-required">*
											</span>法定代表人:</label>
                                                    <input
												class="ui-input  validate[required]" type="text" name="legalman"
												id="legalman" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入法定代表人">
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
1.1 广告拟合竞价系统中心（以下称“系统中心”）同意按照本协议的规定提供竞价等相关服务（以下称“本服务”）。为获得本服务，服务使用人（以下称“用户”）应当充分阅读并同意本协议的全部条款并按照页面上的提示完成全部的注册程序。
1.2 用户注册并使用系统中心提供的本服务，即表示用户完全接受本协议项下全部条款，用户选择访问或使用系统中心提供的有关服务，视为同意接受本协议全部条款的约束。
1.3 除另有明确规定，系统中心所推出的新品、新功能和新服务，均无条件的适用本协议。
1.4系统中心保留在任何时候修改本协议条款的权利，且无需另行通知。

2.服务内容
2.1 本服务的具体内容由系统中心根据实际情况提供，系统中心保留变更、中断或终止部分网络服务的权利。
2.2 用户理解，系统中心仅提供相关的服务，除此之外与相关服务有关的设备（如AP等设置）及所需的费用（如竞价费用等）均应由用户自行负担。
2.3 用户通过盖章、网络页面点击确认或以其他方式选择接受本服务条款，包括但不限于未点击确认本服务条款而事实上使用了服务，即表示用户与公司已达成协议并同意接受本服务条款的全部约定内容。如若双方盖章文本与网络页面点击确认或以其他方式选择接受之服务条款文本，存有不一致之处，以双方盖章文本为准。 
2.4 本条款中“服务”指：向用户提供网站上所展示的服务、包年包月包时的关系型数据库服务以及相关的技术及网络支持服务。 
2.5 公司网站提供的服务必须符合本服务条款的约定。 
2.6 服务费用 
2.6.1 服务费用将在用户订购页面予以列明公示，用户可自行选择具体服务类型并按列明的价格予以支付。
2.6.2 在用户付费之后，公司才开始为用户提供服务。用户未在下单后7天内付费的，本服务条款以及与用户就服务所达成的一切行为失效。 
2.6.3 服务期满双方愿意继续合作的，用户至少应在服务期满前7天内支付续费款项，以使服务得以继续进行。如续费时公司对产品体系、名称或价格进行调整的，双方同意按照届时有效的新的产品体系、名称或价格履行。 
2.6.4 公司保留在用户未按照约定支付全部费用之前不向用户提供服务和/或技术支持，或者终止服务和/或技术支持的权利。同时，世巴保留对后付费服务中的欠费行为追究法律责任的权利。 
2.6.5 用户完全理解公司价格体系中所有的赠送服务项目或活动均为公司在正常服务价格之外的一次性特别优惠，优惠内容不包括赠送服务项目的修改、更新及维护费用，并且赠送服务项目不可折价冲抵服务价格。

3. 用户账号
3.1 用户自行保管其账号和密码。用户账号、密码使用权仅属于初始申请注册人，禁止赠与、借用、租用、转让或者售卖。 
3.2 用户应提供完整、真实、准确和最新的个人资料，该资料对于使用系统中心的服务以及找回丢失的系统中心账号和密码至关重要。如因注册信息不真实而引起的问题由用户本人承担，系统中心不负任何责任并有权暂停或终止用户的账号。 
3.3 用户账号和密码遭到他人非法使用或发生其他任何安全问题，用户应当立即通知系统中心。因黑客行为或用户的过错导致账号、密码被他人非法使用，系统中心不承担任何责任。 
3.4 企业或者个人注册系统中心会员账号，制作、复制、发布、传播信息内容的，应当使用真实身份信息，不得以虚假、冒用的居民身份信息、企业注册信息、组织机构代码信息进行注册。 
3.5 系统中心将建立健全用户信息安全管理制度，按照系统中心保护隐私权政策保障用户信息安全。

4.使用规则
4.1 用户对以其账号发生的或通过其账号发生的一切活动和事件（包括但不限于用户发表的任何内容以及由此产生的任何结果）负全部法律责任。
4.2 用户在使用系统中心服务时，必须遵守中华人民共和国相关法律法规的规定，用户应同意将不会利用本服务进行任何违法或不正当的活动，包括但不限于下列行为：
4.2.1上载、下载、张贴、以电子邮件发送、传输、存储或以其他方式提供任何非法、有害、胁迫、骚扰、侵权、中伤、粗俗、猥亵、诽谤、淫秽、暴力、侵害他人隐私、种族歧视或其他令人不快的包括但不限于资讯、资料、文字、软件、音乐、照片、图形、信息或其他资料（以下简称内容）。 
4.2.2 以任何方式危害未成年人。 
4.2.3 冒充任何人或机构，或以虚伪不实的方式谎称或使人误认为与任何人或任何机构有关。 
4.2.4 伪造标题或以其他方式操控识别资料，使他人产生误解。 
4.2.5 上载、张贴、发送电子邮件或以其他方式传送无权传送的内容。
4.2.6 侵犯他人著作权或其他知识产权，或违反保密、雇佣或不披露协议披露他人商业秘密或保密信息。 
4.2.7 张贴、发送、传输或以其他方式提供任何未经收件人请求或授权的电子邮件信息、广告、促销资料、垃圾邮件等，包括但不限于大批量的商业广告和信息公告。 
4.2.8 上载、张贴、以电子邮件发送、传输、存储或以其他方式提供包含病毒或包含旨在危害、干扰、破坏或限制有关服务（或其任何部分）或任何其他计算机软件、硬件或通讯设备之正常运行的任何其他计算机代码、文档或程序的任何资料。 
4.2.9 干扰或破坏有关服务，或与有关服务连接的任何服务器或网络，或与有关服务连接之网络的任何政策、要求或规定。 
4.2.10采集并存储涉及有关服务任何其他用户的个人信息，以用于任何上述被禁止的活动。 
4.2.11 故意或非故意违反任何相关的中国法律、法规、规章、条例等其他具有法律效力的规范。
如用户在使用本服务时违反任何上述规定，系统中心或及其授权的人有权要求用户改正或直接采取一切必要的措施，以减轻用户不当行为造成的影响。
4.3 系统中心提供需要用户支付一定的费用的收费服务。对此，系统中心会在用户使用前明示，只有用户根据提示确认其愿意支付相关费用，用户才能使用该等收费网络服务。如用户拒绝支付相关费用，则系统中心有权不向用户提供该等收费网络服务。 
4.4 用户为使用系统中心服务，须自行配备进入国际互联网所必需的设备，包括电脑、手机及其他与接入互联网或移动网有关的装置，并自行支付与此服务有关的费用。 
4.5 用户同意系统中心有权在提供网络服务过程中以各种方式投放各种商业性广告或其他任何类型的商业信息。

5.知识产权
5.1系统中心服务中包含的任何文字、图表、音频、视频和/或软件（包括但不限于软件中包含的图表、动画、音频、视频、界面实际、数据和程序、代码、文档）等信息或材料均受著作权法、商标法和/或其它法律法规的保护，未经相关权利人书面同意，用户不得以任何方式使用该等信息或材料，但出于使用系统中心服务目的而使用的除外。 
5.2 本协议未授予用户使用系统中心任何商标、服务标记、标识、域名和其他显著品牌特征的权利。 
5.3 除本协议明确允许的以外，用户不得以任何形式或任何方式对系统中心服务部分或全部内容进行修改、出租、租赁、出借、出售、分发、复制、创作衍生品或用于任何商业用途。 
5.4用户在系统中心上发布的信息不得侵犯任何第三人的知识产权，未经相关权利人之事先书面同意，用户不得以任何方式上传、发布、修改、传播或复制任何受著作权保护的材料、商标或属于其他人的专有信息。

6.隐私保护
6.1 保护用户隐私是系统中心的重点原则，系统中心通过技术手段、提供隐私保护服务功能、强化内部管理等办法充分保护用户的个人资料安全。
6.2 系统中心保证不对外公开或向第三方提供用户注册的个人资料，及用户在使用服务时存储的非公开内容，但下列情况除外：a.事先获得用户的明确授权b.按照相关司法机构或政府主管部门的要求；c.维护社会公众利益或系统中心的合法利益。
6.3 在不透露单个用户隐私资料的前提下，系统中心有权对整个用户数据库进行分析并对用户数据库进行商业上的利用。

7.免责声明
7.1 用户将信息等资料上传后，有可能会被其他组织或个人复制、转载、擅改或做其它非法用途，广用户必须充分意识此类风险的存在。用户明确同意其使用本服务所存在的风险（包括但不限于受到第三方侵权或对第三方造成侵权）将完全由其自己承担；因其使用本服务而产生的一切后果也由其自己承担，系统中心对此不承担任何责任。
7.2 系统中心不担保本服务一定能满足用户的要求，也不担保本服务不会中断，对本服务的及时性、安全性、准确性、真实性、完整性也都不作担保。
7.3 系统中心不保证为向用户提供便利而设置的外部链接的准确性和完整性，同时，对于该等外部链接指向的不由本服务实际控制的任何网页上的内容，系统中心不承担任何责任。
7.4 对于因不可抗力或系统中心不能控制的原因造成的网络服务中断或其它缺陷，系统中心不承担任何责任，但将尽力减少因此而给用户造成的损失和影响。
7.5 用户同意，对于本服务向用户提供的系统产品或者服务的质量缺陷本身及其引发的任何损失，系统中心无需承担任何责任。

8.服务变更、中断或终止
8.1 如因系统维护或升级的需要而需暂停网络服务、服务功能的调整，系统中心将尽可能事先在平台上进行通告。
8.2 如发生下列任何一种情形，系统中心有权单方面中断或终止向用户提供服务而无需通知用户：a.用户提供的个人资料不真实；b.用户违反本服务条款中规定的使用规则；C.未经系统中心同意，不得将平台用于其他商业目的。
8.3 除前款所述情形外，系统中心同时保留在不事先通知用户的情况下随时中断或终止部分或全部本服务的权利，对于所有服务的中断或终止而造成的任何损失，系统中心无需对广告主或广告代理商或任何第三方承担任何责任。

9.法律管辖
9.1 本协议的执行和解释及争议的解决均应适用中国法律。
9.2 如双方就本协议内容或其执行发生任何争议，双方应尽量友好协商解决；协商不成时，任何一方均应向系统中心所有者所在地的人民法院提起诉讼。

10.通知和送达
本协议项下所有的通知均可通过网站公告、电子邮件或手机短信等方式进行；该等通知于发送之日视为已送达收件人。

11.其他规定
11.1 本协议构成双方对本协议之约定事项及其他有关事宜的完整协议，除本协议规定的之外，未赋予本协议各方其他权利。
11.2 如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。
11.3 本协议所有条款的最终解释权属于系统中心。


	</TEXTAREA>
	</div>
	<div class="iwifi-ad">&nbsp;</div>
	<div class="iwifi-ad">
		<input id="agreement" type="checkbox"  name=""> 同意《免责条款》
	</div>
    <div class="ui-form-item widthdrawBtBox">
	<div class="iwifi-btn">
        <input type="button" id="register" class="block-btn ui-button-disabled"
               onclick="sub();" value="注册用户">
        </div>
	</div>
</div>
	</form>
</div>
</@frame.html>
