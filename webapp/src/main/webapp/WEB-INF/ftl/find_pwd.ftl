<#import "template/template.ftl" as frame>
<#global menu="密码找回">
<%@page import="java.util.Date"%>
<%@include file="/inc/common_head.inc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 解决360兼容性问题 让360兼容IE8即可 -->
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>
<script type="text/javascript" src="../js/common.js"></script>

<link type="text/css" href="../css/page.css" rel="stylesheet" media="screen" />
<script type="text/javascript" src="../js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="../js/jquery.form.js"></script>
<script type="text/javascript" src="../js/jquery.ulmenu.js"></script>
<script type="text/javascript" src="../js/platform.js"></script>
<script type="text/javascript" src="../js/banBackSpace.js"></script>
<title>密码找回</title>


<script type="text/javascript">


function findPwd(){
	var userId=$("#userId").val();
	
	if(userId==""){
		jDialog.Alert("请填写用户名");
		return;
	}
	
	$.ajax({
		url : "send_pwd_link.action",
		type : "POST",
		data : $("#infoForm").formSerialize(),
		success : function(data) {
				jDialog.Alert( data.right );
				//alert(data.right);
				//window.parent.location.href = "logout.action";
		}
	}, "text");

	
}

</script>

</head>
<body>
	<div class="Page-Title"> <span>找回密码</span> </div>
<center>

<form id="infoForm" name="infoForm" action="" method="post" dataType="html" class="Form-Table">
	
	<table id="addtabel" width="100%">
	<font color="red"><s:property value="msg"/></font>
		<tr>
			<th scope="row"><span style="color:red;">*</span>用户名[登录帐号]：</th>
			<td><input type="text" name="userInfo.id" id="userId" value="<s:property value="user.id"/>" class="Input-Text" placeholder="请输入您的用户名" /></td>
		</tr>
		
	</table>
	<div class="Form-Buttons">
		<button type="button"  onclick="findPwd()">确认找回密码</button>
	</div>
</form> 
</center>


</body>