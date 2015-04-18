<#import "template/template.ftl" as frame>
<#global menu="购买产品">
<@frame.html title="下订单" js=["jquery-ui/jquery-ui.js", "datepicker.js", "jquery.datepicker.region.cn.js"] css=["jquery-ui/jquery-ui.css"]>

<script type="text/javascript">

$(document).ready(function(){ 
    //$("#subWithdraw").attr('disabled',false); //移除disabled属性
}); 

	function sub2() {
        var emptyInput = $("#userForm2 input, #userform2 select").filter(function() { return $(this).val() == ""; });
        if( emptyInput[0]) {
            var empty = '';
            emptyInput.each(function() {
                if ($(this)[0].nodeName == 'OPTION') {
                    empty += $(this).parent("select").attr("name") + ' ';
                }else {
                    empty += $(this).attr("name") + ' ';
                }
            });
            jDialog.Alert('请填写完整信息');
        } else {
            $('#userForm2').ajaxForm(function(data) {
                jDialog.Alert(data.right);
                
            }).submit();
            document.getElementById('subWithdraw').setAttribute('disabled','true');
           //var a = document.createElement('a');
    	   //a.href='${rc.contextPath}/order/myTask/1';
    	    //document.body.appendChild(a);
    	    //a.click();
        }
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
								class="ui-form" method="post" action="../creOrder2"
								enctype="multipart/form-data">
								<div class="withdrawInputs">
								    <DIV class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">套餐详情</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
  <LI style="width: 240px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>时长（秒）：</SPAN><SPAN class="con">${prod.duration!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>单日播放次数：</SPAN><SPAN class="con">${prod.playNumber!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN class="con">${prod.firstNumber!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN class="con">${prod.lastNumber!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>高峰时段占比：</SPAN><SPAN class="con">${prod.hotRatio!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type!''}</SPAN></LI>
  <LI style="width: 200px;"><SPAN>套餐播放天数：</SPAN><SPAN class="con">${prod.days!''}</SPAN></LI>
</UL>
</DIV>
</DIV>
<div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">填写订单信息</A></H3><br>
									<div class="inputs">
                                            <input type="hidden" readonly="readonly" name="product.Id" id="productId" value="${prod.id!''}"/>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
												class="ui-form-required">*</span>开播日期</label> <input
                                                class="ui-input datepicker" type="text" name="startTime1"
                                                id="startTime" data-is="isAmount isEnough"
                                                autocomplete="off" disableautocomplete="">
                                        </div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>选择物料:</label>
                                            <select class="ui-input" name="supplies.id" id="suppliesId">
                                                <option value="" selected="selected"></option>
                                                <#list supplies as s>
                                                    <option value="${s.id}">${s.name}</option>
                                                </#list>
                                            </select>
											<p class="ui-term-placeholder"></p>
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub2();" value="确认提交" >
									</div>
								</div>
</div>							

							</form>
</@frame.html>
