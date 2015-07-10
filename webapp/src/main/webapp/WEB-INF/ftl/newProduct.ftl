<#import "template/template.ftl" as frame>
<#import "spring.ftl" as spring />

<#global menu="产品定义">
<#assign action="增加">
<#if prod??><#assign action="修改"></#if>

<@frame.html title="${action}产品套餐" js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery-ui-timepicker-addon.js","js/jquery-ui-timepicker-zh-CN.js", "js/jquery.datepicker.region.cn.js","js/jquery-ui/jquery-ui.auto.complete.js"] css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.17.custom.css","css/jquery-ui-timepicker-addon.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
    $(document).ready(function() {
        $("#productForm").validationEngine({
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
    $(function(){
 $(".ui_timepicker").datetimepicker({
            //showOn: "button",
            //buttonImage: "./css/images/icon_calendar.gif",
            //buttonImageOnly: true,
            showSecond: true,
            timeFormat: 'hh:mm:ss',
            stepHour: 1,
            stepMinute: 1,
            stepSecond: 1
        })
        function toggleFields(){
            var type = $("#type").val();
            $(".toggle").hide();
            $("."+type+"Toggle").show();
        }
        $("#type").change(toggleFields);
        toggleFields();
    });
	function sub2() {
        if (!$("#productForm").validationEngine('validateBeforeSubmit'))
            return;
        var iscompare=$('input:radio[name="iscompare"]:checked').val();
        var startDate1=$("#startDate1").val();
        var biddingDate1=$("#biddingDate1").val();
        if(iscompare==1){
        if(biddingDate1<startDate1){
			jDialog.Alert("截止时间不能小于开拍时间");
			return;
		  }
        }
        $('#productForm').ajaxForm(function(data) {
            jDialog.Alert(data.name);
        }).submit();
        document.getElementById('submit').setAttribute('disabled',true);
        
        if(iscompare==1){
	   		 var a = document.createElement('a');
	    	 a.href='${rc.contextPath}/product/list';
    	}else {
    		 var a = document.createElement('a');
	    	 a.href='${rc.contextPath}/product/auction';
    	}
    	 document.body.appendChild(a);
	   	 a.click(); 
	}
		
 $(document).ready(function() {
   
		        //author:pxh 2015-05-20 22:36
		        $( "#exclusiveUser" ).autocomplete({
		  			source: "${rc.contextPath}/user/autoComplete",
		  			change: function( event, ui ) { 
		  				/*if(ui.item!=null){alert(ui.item.value);}*/
		  				table.fnDraw();
		  			 },
		  			 select: function(event,ui) {
		  			 $('#exclusiveUser').val(ui.item.value);
		  				//table.fnDraw();
		  			 }
				});
	    });

</script>
<script type="text/javascript">

        function bu(txtObj) {
            txtObj.value = Number(txtObj.value).toFixed(2);
        }
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
							<form data-name="withdraw" name="productForm" id="productForm"
								class="ui-form" method="post" action="save"
								enctype="multipart/form-data">
                                <input type="hidden" name="id" id="id" value="<#if prod??>${(prod.id)!''}<#else>0</#if>"/>
								<div class="withdraw-title fn-clear">${action}产品套餐</div>
								<div class="withdrawInputs">
									<div class="inputs" style="padding-left: 20px;">
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>媒体类型：</label>
                                            <select class="ui-input" name="type" id="type">
                                                <#list types as type>
                                                    <option value="${type.name()}" <#if (prod?? && prod.type == type.name())>selected="selected"</#if>>${type.typeName}</option>
                                                </#list>
                                            </select>
                                        </div>

                                        <div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐名称：</label> <input
												class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[20]]"
                                                type="text" value="<#if prod??>${prod.name!''}<#else></#if>" name="name"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="2-20个字符">
										</div>
										<div class="ui-form-item toggle videoToggle imageToggle infoToggle">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>时长（秒）:</label> <input
												class="ui-input validate[required,integer,min[15],max[180]]"
                                                onkeyup="value=value.replace(/[^\d]/g,'')" value="<#if prod??>${prod.duration!''}<#else>15</#if>" name="duration"
												id="duration" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="15-180秒">
											<p class="ui-term-placeholder"></p>

										</div>

										<div class="ui-form-item toggle videoToggle imageToggle infoToggle">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>单日播放次数:</label>
												<input
												class="ui-input validate[required,integer,min[1],max[100]"
                                                onkeyup="value=value.replace(/[^\d.]/g,'')" type="text" value="<#if prod??>${prod.playNumber!''}<#else></#if>" name="playNumber"
												id="playNumber" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="1-100次">
										</div>

                                        <div class="ui-form-item toggle videoToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>首播次数:</label>
                                            <input
                                                    class="ui-input validate[required,integer,min[0],max[30]"
                                                    onkeyup="value=value.replace(/[^\d.]/g,'')" value="<#if prod??>${prod.firstNumber!''}<#else>0</#if>" name="firstNumber"
                                                    id="firstNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="" placeholder="0-30次">
                                        </div>
                                        <div class="ui-form-item toggle videoToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>末播次数:</label>
                                            <input
                                                    class="ui-input validate[required,integer,min[0],max[30]"
                                                    onkeyup="value=value.replace(/[^\d.]/g,'')" value="<#if prod??>${prod.lastNumber!''}<#else>0</#if>" name="lastNumber"
                                                    id="lastNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="" placeholder="0-30次">
                                        </div>
                                        <div class="ui-form-item toggle videoToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>高峰时段占比:</label>
                                            <input
                                                    class="ui-input validate[required,number,min[0],max[1]"
                                                    onkeyup="value=value.replace(/[^\d.]/g,'')" value="<#if prod??>${prod.hotRatio!''}<#else>0.1</#if>" name="hotRatio"
                                                    id="hotRatio" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="" placeholder="0-1之间的小数，例如：0.2表示高峰占比20%。">
                                        </div>
                                        <div class="ui-form-item toggle bodyToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>线路级别：</label>
                                            <select class="ui-input" name="lineLevel" id="lineLevel">
                                                <#if lineLevels??>
                                                <#list lineLevels as level>
                                                    <option value="${level.name()}" <#if (prod?? && prod.lineLevel == level.name())>selected="selected"</#if>>${level.nameStr}</option>
                                                </#list>
                                                </#if>
                                            </select>
                                        </div>
                                        <div class="ui-form-item toggle bodyToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>巴士数量:</label>
                                            <input
                                                    class="ui-input validate[required,number,min[1]"
                                                    type="number" value="<#if prod??>${prod.busNumber!''}<#else>0</#if>" name="busNumber"
                                                    id="busNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>
                                                <span class="toggle videoToggle imageToggle infoToggle">套餐播放天数:</span>
                                                <span class="toggle bodyToggle">广告展示天数:</span>
                                                </label>
                                            <input
                                                    class="ui-input validate[required,integer,min[1],max[360]"
                                                    onkeyup="value=value.replace(/[^\d.]/g,'')" value="<#if prod??>${prod.days!''}<#else>7</#if>" name="days"
                                                    id="days" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="" placeholder="最少1天">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>
                                                <span class="toggle videoToggle imageToggle infoToggle">套餐价格（元）:</span>
                                         
                                            </label>
                                            <input
                                                    class="ui-input validate[required,number,min[1]"
                                                    onblur="bu(this)" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\d.]/g,'')}else{this.value=this.value.replace(/\D+\./g,'')}" value="<#if prod??>${prod.price!''}<#else>0</#if>" name="price"
                                                    id="price" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                     

                                        
                                        <div class="ui-form-item toggle bodyToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>制作费:</label>
                                            <input
                                                    class="ui-input validate[required,number,min[1]"
                                                    type="number" value="<#if prod??>${prod.produceCost!''}<#else>0</#if>" name="produceCost"
                                                    id="produceCost" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required"></span>产品定向:</label>
                                            <span>
                         						<input id="exclusiveUser" name="exclusiveUser" value=""
                         						 placeholder="请选择广告主" style="margin-top: 8px;" >
                       						</span>
                                        </div>
										
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>是否为竞价套餐：</label> 
												<div style="margin-top: 10px;float: left;"><input type="radio" name="iscompare" value="1" onchange="showisAuction()">是</div>
												<div style="margin-top: 10px;margin-left:5px;float: left;"><input type="radio" name="iscompare" value="0" onchange="hideboth()" checked="checked">否</div>
										</div>
                                     
                                        
                                     <div id="isAuction" style="display: none;">
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>
                                                <span class="toggle videoToggle imageToggle infoToggle">竞拍底价（元）:</span>
                                         
                                            </label>
                                            <input
                                                    class="ui-input validate[required,number,min[1]"
                                                    onblur="bu(this)" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\d.]/g,'')}else{this.value=this.value.replace(/\D+\./g,'')}" value="<#if prod??>${prod.price!''}<#else>0</#if>" name="saleprice"
                                                    id="saleprice" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        
                                        <div class="ui-form-item">
											<label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>开拍时间:
															</label> <input
												class="ui-input ui_timepicker validate[required,past[#startDate1]]" 
												type="text" name="startDate1" 
												id="startDate1" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
                                        <div class="ui-form-item">
											<label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>截止时间:
															</label> <input
												class="ui-input ui_timepicker validate[required,past[#biddingDate1]]" 
												type="text" name="biddingDate1" 
												id="biddingDate1" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
									</div>
									
									<div class="ui-form-item">
											<label class="ui-label mt10">套餐描述：</label> 
											<textarea rows="4" cols="40" style="resize: none;" name="remarks"></textarea>
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="submit" class="block-btn"
											onclick="sub2();" value="确定">
									</div>
								</div>

							</form>
</div>
<script type="text/javascript">
//竞价选项	    
function showisAuction(){
	 $("#isAuction").show();
}	    
function hideboth(){
	$("#isAuction").hide();
}


</script>
</@frame.html>
