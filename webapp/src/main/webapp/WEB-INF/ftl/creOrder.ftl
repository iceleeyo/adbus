<#import "template/template.ftl" as frame>
<#global menu="购买产品">
<@frame.html title="下订单" js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js", "js/jquery.datepicker.region.cn.js"] css=["jquery-ui/jquery-ui.css"]>

<script type="text/javascript">

$(document).ready(function(){ 
    //$("#subWithdraw").attr('disabled',false); //移除disabled属性
}); 

	function sub2() {
        var emptyInput = $("#userForm2 input").filter(function() { return $(this).val() == ""; });
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
    	    window.location.href="${rc.contextPath}/order/myTask/1";
        }
           // var a = document.createElement('a');
    	   // a.href='${rc.contextPath}/order/myTask/1';
    	   // document.body.appendChild(a);
    	   // a.click();
	}
	function sub() {
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
          $('#userForm2').submit();
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
								    <DIV class="p20bs color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">套餐详情-<span style="color: rgb(245, 135, 8);">[${prod.name!''}]</span></A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
  <LI style="width: 720px;"><SPAN>套餐描述：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>套餐价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}.00</SPAN><SPAN>元</SPAN></LI>
  <LI style="width: 240px;"><SPAN>每次播放时长：</SPAN><SPAN class="con">${prod.duration!''}秒</SPAN></LI>
  <LI style="width: 240px;"><SPAN>单日播放次数：</SPAN><SPAN class="con">${prod.playNumber!''}</SPAN><SPAN>次</SPAN></LI>
  <LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN class="con">${prod.firstNumber!''}</SPAN><SPAN>次</SPAN></LI>
  <LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN class="con">${prod.lastNumber!''}</SPAN><SPAN>次</SPAN></LI>
  <LI style="width: 240px;"><SPAN>高峰时段占比：</SPAN><SPAN class="con">${prod.hotRatio!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">
    <#if (!prod?? || prod.type == 'video')>视频</#if>
      <#if (!prod?? || prod.type == 'image')>图片</#if>
      <#if (!prod?? || prod.type == 'info')>文本</#if>
  </SPAN></LI>
  <LI style="width: 200px;"><SPAN>套餐播放天数：</SPAN><SPAN class="con">${prod.days!''}天</SPAN></LI>
</UL>
</DIV>
</DIV>
<div class="p20bs mt10 color-white-bg border-ec">
                <H3 class=".withdraw-title text-xl title-box"><A class="black" href="#">填写订单信息</A></H3><br>
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
											<label class="ui-label mt10">选择物料:</label>
                                            <select class="ui-input" name="supplies.id" id="suppliesId">
                                                <option value="1" selected="selected"></option>
                                                <#list supplies as s>
                                                    <option value="${s.id}">${s.name}</option>
                                                </#list>
                                            </select>
                                            
                                            	<font size="2" color="#CDCDCD" >
                                            	*  可以在此选择物料，也可以稍后上传。
                                            	</font>
											<p class="ui-term-placeholder"></p>
											
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" onclick="sub()" class="block-btn"
											 value="确认提交" >
									</div>
								</div>
</div>							

							</form>
</@frame.html>
