
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<style type="text/css">
body{
	background-color:#fff;color:#64686d;font-family:'Microsoft Yahei',Arial,Helvetica,sans-serif,Verdana,'Trebuchet MS';font-size:13px;padding:20px;overflow:auto;
}

table{
	border: solid thin #EEEEEE;
	border-collapse: collapse;
	font-size: 13px;
}
table td{
	border: solid thin #EEEEEE;
	height:38px;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 解决360兼容性问题 让360兼容IE8即可 -->
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<script type="text/javascript" src="${rc.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.validate.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/common.js"></script>

<link type="text/css" href="${rc.contextPath}/css/page.css" rel="stylesheet" media="screen" />
<script type="text/javascript" src="${rc.contextPath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/jquery.ulmenu.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/platform.js"></script>
<script type="text/javascript" src="${rc.contextPath}/js/banBackSpace.js"></script>
<title>密码找回</title>



<script type="text/javascript">


function findPwd(){
	var userId=$("#userId").val();
	
	if(userId==""){
		jDialog.Alert("请填写用户名");
		return;
	}
	
	$.ajax({
		url : "${rc.contextPath}/user/send_pwd_link",
		type : "POST",
		data : {"userId":userId},
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
	
	<table id="addtabel" width="100%" >
	<font color="red"><s:property value="msg"/></font>
		<tr>
			<td scope="row" width="50%" align="center"><span style="color:red;">*</span>用户名[登录帐号]：</td>
			<td><input type="text" id="userId" value="" class="Input-Text" placeholder="     请输入您的用户名" /></td>
		</tr>
		
	</table>
	<div class="Form-Buttons">
		<button type="button"  onclick="findPwd()">确认找回密码</button>
	</div>
</form> 
</center>


</body>
</html>