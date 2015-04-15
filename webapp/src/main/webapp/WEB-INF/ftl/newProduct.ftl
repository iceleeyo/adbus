<#import "template/template.ftl" as frame>
<#import "spring.ftl" as spring />

<#global menu="产品定义">
<#assign action="增加">
<#if prod??><#assign action="修改"></#if>

<@frame.html title="${action}产品套餐">

<script type="text/javascript">
    $(function(){
        $("#type").change(function(){
            var type = $(this).val();
            if (type == 'video') {
                $(".toggle").show();
            } else {
                $(".toggle").hide();
            }
        });
    });
	function sub2() {
        $('#productForm').ajaxForm(function(data) {
            alert(data.error + " # " + data.name);
        }).submit();
            var a = document.createElement('a');
    	    a.href='${rc.contextPath}/product/list';
    	    document.body.appendChild(a);
    	    a.click();
	}
</script>
							<form data-name="withdraw" name="productForm" id="productForm"
								class="ui-form" method="post" action="save"
								enctype="multipart/form-data">
                                <input type="hidden" name="id" id="id" value="<#if prod??>${(prod.id)!''}<#else>0</#if>"/>
								<div class="withdraw-title fn-clear">${action}产品套餐</div>
								<div class="withdrawInputs">
									<div class="inputs">
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>套餐类型：</label>
                                            <select class="ui-input" name="type" id="type">
                                                <option value="video" <#if (!prod?? || prod.type == 'video')>selected="selected"</#if>>视频</option>
                                                <option value="image" <#if (prod?? && prod.type == 'image')>selected="selected"</#if>>图片</option>
                                                <option value="info" <#if (prod?? && prod.type == 'info')>selected="selected"</#if>>文本</option>
                                            </select>
                                        </div>

                                        <div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐名称：</label> <input
												class="ui-input" type="text" value="<#if prod??>${prod.name!''}<#else></#if>" name="name"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>时长（秒）:</label> <input
												class="ui-input" type="number" value="<#if prod??>${prod.duration!''}<#else>15</#if>" name="duration"
												id="duration" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder"></p>

										</div>

										<div class="ui-form-item toggle">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>单日播放次数:</label>
												<input
												class="ui-input" type="text" value="<#if prod??>${prod.playNumber!''}<#else>1</#if>" name="playNumber"
												id="playNumber" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>

                                        <div class="ui-form-item toggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>首播次数:</label>
                                            <input
                                                    class="ui-input" type="number" value="<#if prod??>${prod.firstNumber!''}<#else>0</#if>" name="firstNumber"
                                                    id="firstNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item toggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>末播次数:</label>
                                            <input
                                                    class="ui-input" type="number" value="<#if prod??>${prod.lastNumber!''}<#else>0</#if>" name="lastNumber"
                                                    id="lastNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item toggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>高峰时段占比:</label>
                                            <input
                                                    class="ui-input" type="number" value="<#if prod??>${prod.hotRatio!''}<#else>0</#if>" name="hotRatio"
                                                    id="hotRatio" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>套餐播放天数:</label>
                                            <input
                                                    class="ui-input" type="number" value="<#if prod??>${prod.days!''}<#else>0</#if>" name="days"
                                                    id="days" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>套餐价格:</label>
                                            <input
                                                    class="ui-input" type="number" value="<#if prod??>${prod.price!''}<#else>0</#if>" name="price"
                                                    id="price" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="submit" class="block-btn"
											onclick="sub2();" value="确定">
									</div>
								</div>

							</form>
</@frame.html>
