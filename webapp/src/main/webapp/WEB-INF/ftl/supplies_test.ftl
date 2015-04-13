<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<#include "/menu/webapp.ftl" />
<html>
<head>
<title>物料上传</title>
 
<script type="text/javascript">
	i = 2;
	j = 2;
	$(document).ready(function() {$("#btn_add2").click(function() {
		$("#newUpload2").append('<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;" /><input type="button"  style="margin-top:10px;" value="删除"  onclick="del_2('
		+ j + ')"/></div>');
	j = j + 1;
	}));

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}


	function sub2() {
		var name = ($("#name").val());
		if(name==""){
			jDialog.Alert("请填写物料名称");
			return;
		}
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.left + " # " + data.right);
			var uptime = window.setTimeout(function(){
				window.location.href="/${web}/supplies/list/1"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
	}
	
	function Tchange(obj){
	var suppliesType = $("#selector").val(); 
		jDialog.Alert(obj.value);
		jDialog.Alert(suppliesType);
		if(suppliesType=="video" || suppliesType=="image"){
			$("#text").style.display="none";
			$("#file").style.display="";
		}
		if(suppliesType=="info"){
			$("#text").style.display="";
			$("#file").style.display="none";
		}
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
												class="ui-input" type="text" name="name" id="name"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="">
										</div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>素材类型</label>
											<select class="ui-input" name="suppliesType" id="suppliesType" onchange="Tchange(this)">
												<option value="video" selected>video</option>
												<option value="image">image</option>
												<option value="info">info</option>
											</select>
										</div>

										<div class="ui-form-item" id="text" style="display:none;">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>文本信息</label> <input
												class="ui-input" type="text" name="infoContext"
												id="infoContext" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" style="height: 91px; width: 367px; ">
										</div>
										<div class="ui-form-item" id="file">
											<label class="ui-label mt10">附件上传</label>
											<div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file" id="Sfile">
												</div>
											</div>
											<input type="button" id="btn_add2" value="增加一行"
												style="margin-top: 10px;"><br>
										</div>
										<div class="ui-form-item widthdrawBtBox">
											<input type="button" id="subWithdraw" class="block-btn"
												onclick="sub2();" value="物料上传">
										</div>
									</div>
								</div>
							</form>
							<!--主体结束-->
						</div>
					</div>
				</div>
			</div>
			<!--底部DIV -->
			<#include "/menu/foot.ftl" />
			<!--底部DIV -->
		</div>
	</div>
<script type="text/javascript" language="javascript"
	src="/${web}/js/index.js"></script>
</body>
</html>
