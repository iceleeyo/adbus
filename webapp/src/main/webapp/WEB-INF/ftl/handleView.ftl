<#include "/menu/webapp.ftl" />
<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="content-type">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>公交广告交易系统</title>
<script type="text/javascript">
	function go_back() {
		history.go(-1);
	}
	function sub() {
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
	function sub2() {
		//var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		//var isok=$("#div1 :radio[name=rad]:checked").val();
	    //var comment=$("#comment").val();
		$.ajax({
			url : "../order/handle",
			type : "POST",
			data : {
				//"orderid" :orderid,
				"taskid" :taskid
				//"isok":isok,
				//"comment":comment
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
						<input type="text" id="orderid" value="${orderid!''}"/>
						<input type="text" id="taskid" value="${taskid!''}"/>
						<div id="div1" >
						部门领导审批意见：<br>
				            <textarea name="comment" id="comment"></textarea><br>
							<input type="radio" name="rad" value="true" checked="checked">支付正常
				            <input type="radio" name="rad" value="false" >支付异常
				            <button onclick="sub2();" >提交</button>
							</div>
						</div>
						<button type="button" onclick="go_back()">返回</button>
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