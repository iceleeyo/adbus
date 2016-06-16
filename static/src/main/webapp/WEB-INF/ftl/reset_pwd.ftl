<#import "template/template.ftl" as frame> <#global menu="密码找回">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<style type="text/css">
body {
	background-color: #fff;
	color: #64686d;
	font-family: 'Microsoft Yahei', Arial, Helvetica, sans-serif, Verdana,
		'Trebuchet MS';
	font-size: 13px;
	padding: 20px;
	overflow: auto;
}

table {
	border: solid thin #EEEEEE;
	border-collapse: collapse;
	font-size: 13px;
}

table td {
	border: solid thin #EEEEEE;
	height: 38px;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 解决360兼容性问题 让360兼容IE8即可 -->
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<script type="text/javascript"
	src="${rc.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
	src="${rc.contextPath}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/common.js"></script>

<link type="text/css" href="${rc.contextPath}/css/page.css"
	rel="stylesheet" media="screen" />
<script type="text/javascript" src="${rc.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript"
	src="${rc.contextPath}/js/jquery.ulmenu.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/platform.js"></script>
<script type="text/javascript"
	src="${rc.contextPath}/js/banBackSpace.js"></script>


<script type="text/javascript">


function changePwd(){
	var password1=$("#password1").val();
	var password2=$("#password2").val();
	var userId=$("#userId").val();
	
    if(userId.trim()==""){
       jDialog.Alert("请输入用户名");
    }
	
	if(password1==""){
		jDialog.Alert("请填写新密码");
		return;
	}
	if(password1.length>15){
		jDialog.Alert("密码过长!");
		return;
	}
	
	if(password2 !=password1){
		jDialog.Alert("两次密码输入不一样");
		return;
	}
	
	$.ajax({
		url : "${rc.contextPath}/user/change_pwd",
		type : "POST",
		data : {"userId":userId,"psw":password1},
		success : function(data) {
				jDialog.Alert( data.right );
				//window.location.reload();
		}
	}, "text");
	
}

</script>

</head>
<body>
	<div class="Page-Title">
		<span>修改密码</span>
	</div>
	<center>

		<form id="infoForm" name="infoForm" action="" method="post"
			dataType="html" class="Form-Table">

			<table id="addtabel" width="100%">
				<font color="red"><s:property value="msg" /></font>
				<tr>
					<td scope="row" align="center" width="50%"><span
						style="color: red;">*</span>用户名[登录帐号]：</td>
					<td><input type="text" id="userId" value="${userId!''} "
						class="Input-Text" readonly="readonly" /></td>
				</tr>
				<tr>
					<td scope="row" align="center"><span style="color: red;">*</span>新密码：</td>
					<td><input type="password" id="password1" value=""
						class="Input-Text" placeholder="" /></td>
				</tr>
				<tr>
					<td scope="row" align="center"><span style="color: red;">*</span>确认新密码：</td>
					<td><input type="password" id="password2" value=""
						class="Input-Text" placeholder="" /></td>
				</tr>

			</table>
			<div class="Form-Buttons">
				<button type="button" onclick="changePwd()">修改密码</button>
			</div>
		</form>
	</center>


</body>