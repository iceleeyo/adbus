<#import "template/template.ftl" as frame> <#global menu="找回密码">
<@frame.html title="找回密码" left=false nav=false
js=["js/jquery-ui/jquery-ui.js", "js/datepicker.js",
"js/jquery.datepicker.region.cn.js"] css=["js/jquery-ui/jquery-ui.css"]>
<head>
<style type="text/css">
body {
	background-color: #fff;
	color: #64686d;
	font-family: 'Microsoft Yahei', Arial, Helvetica, sans-serif, Verdana,
		'Trebuchet MS';
	font-size: 13px;
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
.ls-10{
	margin-left: 7%;
}
</style>
<title>密码找回</title>



<script type="text/javascript">


function findPwd(){
	var userId=$("#userId").val();
	
	if(userId==""){
		jDialog.Alert("请填写用户名");
		return;
	}
	
	$.ajax({
		url : "/user/send_pwd_link",
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
	<div class="pg-container-main">
		<div class="container-12 mt10 s-clear" style="width:70%;">
			<div class="withdraw-wrap color-white-bg fn-clear">
				<div class="Page-Title">
					<span>找回密码</span>
				</div>
				<center>
					<form id="infoForm" name="infoForm" action="" method="post"
						datatype="html" class="Form-Table">

						<font color="red"><s:property value="msg"></s:property></font>
						<table id="addtabel" width="100%">

							<tbody>
								<tr>
									<td scope="row" width="50%" align="center"><span
										style="color: red;">*</span>用户名[登录帐号]：</td>
									<td><input type="text" id="userId" value=""
										class="Input-Text" placeholder="     请输入您的用户名"></td>
								</tr>

							</tbody>
						</table>
						<div class="Form-Buttons">
							<button type="button" onclick="findPwd()">确认找回密码</button>
						</div>
					</form>
				</center>
			</div>
		</div>
	</div>
</body>
</@frame.html>
