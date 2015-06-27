<#import "template/template.ftl" as frame>
<#import "template/proDetail.ftl" as proDetail>
<#global menu="购买产品">
<@frame.html title="未绑定物料订单" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css"]>



<script type="text/javascript">
    $(document).ready(function() {
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

$(document).ready(function(){ 
    //$("#subWithdraw").attr('disabled',false); //移除disabled属性
}); 

	function creorder() {
       
         $('#userForm2').submit();
	}
 function isagree(username,proname){
        var startTime = $("#startTime").val();
        var d = new Date(startTime.replace(/-/g,"/")); 
        date = new Date();
        var str  = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
        var d2 = new Date(str.replace(/-/g,"/")); 
        if(startTime=""){
         jDialog.Alert('请填写开播日期');
         return;
         }
           var days=Math.floor((d-d2)/(24*3600*1000));
        if(days<3) {
            jDialog.Alert('开播日期请选择3天以后');
            return;
         } 
         //author :impanxh 阻止2次点击 ,当所有表单都验证通过时才提交 抄自注册页面
         if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
         $("#subWithdraworder").attr("disabled",true);
         $("#subWithdraworder").css("background-color","#85A2AD");
			layer.open({
    		type: 1,
    		title: "电子合同",
    		skin: 'layui-layer-rim', 
    		area: ['650px', '600px'], 
    		content:''
			   +'<div class = "ad-agreement"> <TEXTAREA id="agreementstr" name="agreementstr" type="text" cols="85" rows="22" style="margin-left:20px;">'
			   +username+'统一购买'+proname
			   +'\n1.特别提示'
			   +'\n1.1 广告拟合竞价系统中心（以下称“系统中心”）同意按照本协议的规定提供竞价等相关服务（以下称“本服务”）。为获得本服务，服务使用人（以下称“用户”）应当同意本协议的全部条款并按照页面上的提示完成全部的注册程序。'
			   +'\n1.2 一旦注册并使用系统中心提供的本服务，即视为用户已了解并完全同意本条款各项内容，包括系统中心对本协议随时所做的任何修改。'
			   +'</TEXTAREA> </div>'
			   +'<div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdraworder" class="block-btn" onclick="creorder();" value="确认" style="margin:10px 0px -10px 110px;"> </div>'
			});

}
	function check() {
		var c = $("#contractCode").val();
		$.ajax({
			url : "${rc.contextPath}/contract/contractCodeCheck",
			type : "POST",
			data : {
				"code" : c
			},
			success : function(data) {
				jDialog.Alert(data.right);
			}
		}, "text");
	}
</script>

							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="../confirm"
								enctype="multipart/form-data">
                                <@proDetail.proDetail prod=prod buyLink=false/>


<div class="p20bs mt10 color-white-bg border-ec">
                <H3 class=".withdraw-title text-xl title-box"><A class="black" href="#">填写订单信息</A></H3><br>
									<div class="inputs">
                                            <input type="hidden" readonly="readonly" name="product.Id" id="productId" value="${prod.id!''}"/>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
												class="ui-form-required">*</span>开播日期</label> <input
                                                class="ui-input datepicker validate[required,custom[date] layer-tips" 
                                                type="text" name="startTime1"
                                                id="startTime" data-is="isAmount isEnough"
                                                autocomplete="off" disableautocomplete="" tip="可选择3天后日期!">
                                                
                                               	 
                                        </div>
										<div class="ui-form-item">
											<label class="ui-label mt10">选择物料:</label>
                                            <select class="ui-input" 
                                            name="supplies.id" id="supplieid">
                                                <option value="1" selected="selected"></option>
                                                <#list supplies as s>
                                                    <option value="${s.id}">${s.name}</option>
                                                </#list>
                                            </select>&nbsp;&nbsp;&nbsp;&nbsp;
                                            	<a  href="javascript:;" onclick="supEnter('${rc.contextPath}',${city.mediaType})">上传物料</a>
											<p class="ui-term-placeholder"></p>
											
										</div>
										
										<div class="ui-form-item" id="orderRemark">
											<label class="ui-label mt10">备注信息:</label>
                                            <textarea rows="4" cols="40" style="resize: none;" name="ordRemark"></textarea>
											<p class="ui-term-placeholder"></p>
											
										</div>
										
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" onclick="isagree('${username!''}','${prod.name!''}')" class="block-btn"
										id="subWithdraw" value="确认提交" >
									</div>
								</div>

							</form>
							
							<script type="text/javascript">
								//限定不能选今天之前的日期
								jQuery(function($){ 
						    	 $.datepicker.regional['zh-CN'] = { 
						         minDate: new Date( (new Date()/1000+86400*3)*1000 ),
						        isRTL: false}; 
						        $.datepicker.setDefaults($.datepicker.regional['zh-CN']); 
						  		  });
							</script>
</@frame.html>
