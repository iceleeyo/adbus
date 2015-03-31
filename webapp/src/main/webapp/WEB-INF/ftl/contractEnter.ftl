<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>合同录入</title>

<link rel="stylesheet" type="text/css" href="../css/sea.css">
<link rel="stylesheet" type="text/css" href="../css/one.css">
<link rel="stylesheet" type="text/css" href="../css/account.css">
<script type="text/javascript">
	i = 2;
	j = 2;
	$(document)
			.ready(
					function() {

						$("#btn_add2")
								.click(
										function() {
											$("#newUpload2")
													.append(
															'<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  /><input type="button" value="删除"  onclick="del_2('
																	+ j
																	+ ')"/></div>');
											j = j + 1;
										});
					});

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}

	function sub() {
		$('#userForm2').ajaxForm(function(data) {
			alert(data.left + " # " + data.right);
		}).submit();
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
						<li class="s-left bread-child"><a class="gray-text" href="#">合同详情录入</a>
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
								class="ui-form" method="post" action="saveContract"
								enctype="multipart/form-data">
								<div class="withdraw-title fn-clear">
									合同详情录入
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
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>合同号</label> <input class="ui-input"
												type="text" value="" name="contractCode" id="withdrawAmount"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>合同名称</label> <input
												class="ui-input" type="text" value="" name="contractName"
												id="withdrawAmount" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder"></p>

											<input type="text" style="display: none;">
										</div>


										<div class="ui-form-item">
											<label class="ui-label mt10">生效日期</label> <input
												class="ui-input" type="date" value="" name="startDate1"
												id="withdrawAmount" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10">失效日期</label> <input
												class="ui-input" type="date" value="" name="endDate1"
												id="withdrawAmount" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>



										<div class="ui-form-item">
											<label class="ui-label mt10">附件上传</label>
											<div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file">
												</div>
											</div>
											<input type="button" id="btn_add2" value="增加一行"
												style="margin-top: 10px;"><br>
										</div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="subWithdraw" class="block-btn"
											onclick="sub();" value="创建合同">
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
		src="../js/jquery.js"></script>
	<script type="text/javascript" language="javascript"
		src="../js/jquery.form.js"></script>
	<script type="text/javascript" language="javascript"
		src="../js/index.js"></script>
</body>
</html>
