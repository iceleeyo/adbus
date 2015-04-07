<#import "template/template.ftl" as frame>
<#import "spring.ftl" as spring />

<@frame.html title="增加产品套餐">

<script type="text/javascript">
	function sub2() {
        $('#productForm').ajaxForm({
        }).submit();
	}
</script>
							<form data-name="withdraw" name="productForm" id="productForm"
								class="ui-form" method="post" action="create"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">增加产品套餐</div>
								<div class="withdrawInputs">
									<div class="inputs">
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>套餐类型：</label>
                                            <select class="ui-input" name="type" id="type">
                                                <option value="video" selected="selected">video</option>
                                                <option value="image">image</option>
                                                <option value="info">info</option>
                                            </select>
                                        </div>

                                        <div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐名称：</label> <input
												class="ui-input" type="text" value="" name="name"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>时长（秒）:</label> <input
												class="ui-input" type="number" value="15" name="duration"
												id="duration" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder"></p>

										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>单日播放次数:</label>
												<input
												class="ui-input" type="text" value="1" name="playNumber"
												id="playNumber" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>

                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>首播次数:</label>
                                            <input
                                                    class="ui-input" type="number" value="0" name="firstNumber"
                                                    id="firstNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>末播次数:</label>
                                            <input
                                                    class="ui-input" type="number" value="0" name="lastNumber"
                                                    id="lastNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>高峰时段占比:</label>
                                            <input
                                                    class="ui-input" type="number" value="0" name="hotRatio"
                                                    id="hotRatio" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>套餐播放天数:</label>
                                            <input
                                                    class="ui-input" type="number" value="0" name="days"
                                                    id="days" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>套餐价格:</label>
                                            <input
                                                    class="ui-input" type="number" value="0" name="price"
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
