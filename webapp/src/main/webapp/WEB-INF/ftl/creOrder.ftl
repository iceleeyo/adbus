<#import "template/template.ftl" as frame>
<#global menu="购买产品">
<@frame.html title="下订单">

<script type="text/javascript">
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
            alert('请填写' + empty);
        } else {
            $('#userForm2').ajaxForm(function(data) {
                alert(data.left + " # " + data.right);
            }).submit();
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
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
</script>
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="../creOrder2"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">套餐订单录入</div>
								<div class="withdrawInputs">
								    
									<div class="inputs">
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐编号：</label>
                                            <input type="text" readonly="readonly" name="product.Id" id="productId" value="${prod.id!''}"/>
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>媒体类型：</label>
												${prod.type!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐名称：</label>
												${prod.name!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>时长（秒）：</label>
												${prod.duration!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>单日播放次数：</label>
												${prod.playNumber!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>首播次数：</label>
												${prod.firstNumber!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>末播次数：</label>
												${prod.lastNumber!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>高峰时段占比：</label>
												${prod.hotRatio!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐播放天数：</label>
												${prod.days!''}
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐价格：</label>
												${prod.price!''}
										</div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10">开播日期</label> <input
                                                class="ui-input" type="date" name="startTime1"
                                                id="startTime" data-is="isAmount isEnough"
                                                autocomplete="off" disableautocomplete="">
                                        </div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>物料ID:</label>
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
											onclick="sub2();" value="下订单">
									</div>
								</div>

							</form>
</@frame.html>
