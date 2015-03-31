<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>物料上传</title>
<script type="text/javascript" language="javascript"
	src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>
<script type="text/javascript" language="javascript"
	src="../js/index.js"></script>
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
		$.ajax({
			url : "put",
			type : "POST",
			contentType : "multipart/form-data",
			data : $("#userForm2").serialize(),//formSerialize
			success : function(data) {
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
	function sub2() {
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
						<li class="s-left bread-child"><a class="gray-text" href="#">物料管理</a>
						</li>
						<li class="s-left breadcrumb-right"></li>
						<li class="s-left bread-child"><a class="gray-text" href="#">上传物料</a>
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
							<form id="userForm2" name="userForm2" action="put"
								enctype="multipart/form-data" method="post"">
								<div class="withdraw-title fn-clear">
									上传物料
									<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
								</div>
								<div class="withdrawInputs">
									<div class="inputs">

										<br>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>素材标题</label> <input
												class="ui-input" type="text" name="name" id="withdrawAmount"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>素材类型</label> <select
												class="ui-input" name="suppliesType">
												<option value="video">video</option>
												<option value="image">image</option>
												<option value="info">info</option>
											</select>
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>文本类型时 文本内容</label> <input
												class="ui-input" type="text" name="infoContext"
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
										<div class="ui-form-item widthdrawBtBox">
											<input type="submit" id="subWithdraw" class="block-btn"
												onclick="sub();" value="物料上传">
										</div>
									</div>
								</div>
							</form>
							<!--主体结束-->
						</div>
					</div>
					<!--底部DIV -->
		<#include "/menu/foot.ftl" />
		<!--底部DIV -->
				</div>
</body>
</html>
