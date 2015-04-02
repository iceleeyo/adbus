<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="content-type">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" language="javascript"
	src="../js/index.js"></script>
	      <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
	  <script type="text/javascript" language="javascript" src="../js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="../../css/page.css">
<link rel="stylesheet" type="text/css" href="../../css/account.css">
<link rel="stylesheet" type="text/css" href="../../css/sea.css">
<link rel="stylesheet" type="text/css" href="../../css/one.css">

<title>公交广告交易系统</title>
<script type="text/javascript">
	function sub2() {
		$('#userForm2').ajaxForm(function(data) {
			alert(data.left + " # " + data.right);
		}).submit();
	}

	function check() {
	var ctx = '<%=request.getContextPath() %>';
		var c = $("#code").val();
		$.ajax({
			url : "../contract/contractCodeCheck",
			type : "POST",
			data : {
				"code" : c
			},
			success : function(data) {
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
	function go_back() {
		history.go(-1);
	}
	
	function pay() {
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "payment",
			type : "POST",
			data : {
				"orderid" :orderid,"taskid" :taskid
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
						<input type="hidden" id="orderid" value="${orderid!''}"/>
						<input type="hidden" id="taskid" value="${taskid!''}"/>
							支付方式：<br>
							输入合同号：<input id="code"
												class="ui-input" type="text" value="reg4345" name="contract_code"
												 data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete=""> 
											<input type="button" class="block-btn" onclick="check();" value="合同号检查">
											<br><br>
											<input type="button" class="block-btn" onclick="pay();" value="确认支付"/>
						<br><br>
						<button type="button" class="block-btn" onclick="go_back()">返回</button>
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

</body>
</html>