<#include "/menu/webapp.ftl" />
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>下订单</title>

<script type="text/javascript">
	function sub2() {
		$('#userForm2').ajaxForm(function(data) {
			alert(data.left + " # " + data.right);
		}).submit();
	}

	function check() {
		var c = $("#code").val();
		$.ajax({
			url : "/${web}/contract/contractCodeCheck",
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
</head>
<body>
	<div class="page-container">
		<!--上部DIV-->
		<#include "/menu/top.ftl" />

		<!--下部DIV-->
		<div class="page-container">
			<div class="pg-container-main">
				<!--顶部导航开始-->
				<div class="container-12">
					<ul class="breadcrumb ml10 m11 s-clear">
						<li class="s-left fsize-16 bread-homep"><a class="gray-text"
							href="/">首页</a></li>
						<li class="s-left breadcrumb-right"></li>
						<li class="s-left bread-child"><a class="gray-text" href="#">待办事项</a>
						</li>
					</ul>
				</div>
				<!--顶部导航结束-->
				<div class="container-12 mt10 s-clear">
					<!--菜单开始-->
					<#include "/menu/left.ftl" />

					<!--菜单结束-->

					<!--主体开始-->
					<div class="ls-10">
						<div class="withdraw-wrap color-white-bg fn-clear">
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
						</div>
					</div>
					<!--主体结束-->
				</div>
			</div>
		</div>
		<!--底部DIV -->
		<#include "/menu/foot.ftl" />
		<!--底部DIV -->
	</div>
<script type="text/javascript" language="javascript"
	src="/${web}/js/index.js"></script>
</body>
</html>

