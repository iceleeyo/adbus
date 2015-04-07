<#import "template/template.ftl" as frame>

<@frame.html title="下订单">

<script type="text/javascript">
	function sub2() {
		$('#userForm2').ajaxForm(function(data) {
			alert(data.left + " # " + data.right);
		}).submit();
	}

	function check() {
		var c = $("#code").val();
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
								class="ui-form" method="post" action="creOrder"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">套餐订单录入</div>
								<div class="withdrawInputs">
									<div class="inputs">
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐ID：</label> <input
												class="ui-input" type="number" value="" name="productId"
												id="withdrawAmount" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>物料ID:</label> <input
												class="ui-input" type="number" value="" name="suppliesId"
												id="withdrawAmount" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder"></p>

										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>支付方式:</label> 
												<input
												class="ui-input" type="text" value="ht_pay" name="payType"
												id="withdrawAmount" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											

										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>合同号:</label> <input id="code"
												class="ui-input" type="text" value="reg4345" name="contract_code"
												id="withdrawAmount" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											
											<input type="button" onclick="check();" class="block-btn" value="合同号检查" style="width: 118px; ">

										</div>

									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub2();" value="下订单">
									</div>
								</div>

							</form>
</@frame.html>
