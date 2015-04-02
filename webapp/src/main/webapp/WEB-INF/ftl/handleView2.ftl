<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="content-type">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" language="javascript"
	src="../js/index.js"></script>
	      <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
	  <script type="text/javascript" language="javascript" src="../js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="../css/sea.css">
<link rel="stylesheet" type="text/css" href="../css/one.css">
<link rel="stylesheet" type="text/css" href="../css/account.css">

<title>公交广告交易系统</title>

<#include "/menu/webapp.ftl" />
<script type="text/javascript">
	
	$(function() {
	//显示当前节点对应的表单信息
	$('#${activityId!'' }').css("display","inline");
});

function go_back() {
		history.go(-1);
	}


/**
 * 完成任务
 * @param {Object} taskId
 */
function complete(taskId, variables) {
	// 转换JSON为字符串
    var keys = "", values = "", types = "";
	if (variables) {
		$.each(variables, function() {
			if (keys != "") {
				keys += ",";
				values += ",";
				types += ",";
			}
			keys += this.key;
			values += this.value;
			types += this.type;
		});
	}
	
	var url="/${web}/order/"+taskId+"/complete";
	// 发送任务完成请求
    $.post(url,{
        keys: keys,
        values: values,
        types: types
    },function(data){
    	alert(data.left==true?"执行成功!":"执行失败!");
    	var a = document.createElement('a');
    	a.href='/${web}/order/myTask/1';
    	a.target = 'main';
    	document.body.appendChild(a);
    	a.click();
    });
    
}

//部门经理审核
function approve1(){
	var approve1Result=$('#approve1 :radio[name=approve1Result]:checked').val();
	var approve1Comments=$('#approve1 #approve1Comments').val();
	complete('${taskid}',[
		{
			key: 'approve1Result',
			value: approve1Result,
			type: 'B'
		},{
			key: 'approve2Result',
			value: "true",
			type: 'B'
		},
		{
			key: 'approve1Comments',
			value: approve1Comments,
			type: 'S'
		}
	]);
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
	function pay() {
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "../order/payment",
			type : "POST",
			data : {
				"orderid" :orderid,"taskid" :taskid
			},
			success : function(data) {
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
	
	function financial() {
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
						<input type="hidden" id="orderid" value="${orderview.order.id!''}"/>
						<input type="hidden" id="taskid" value="${taskid!''}"/>
						<h1>流程办理</h1>
                        <font color="red">${message!'' }</font>
                            <!-- 支付-->
                            <div id="payment" style="display: none;">
							           支付方式：<br>
							           输入合同号：<input id="code" class="ui-input" type="text" value="reg4345" name="contract_code" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> 
								<input type="button" onclick="check();" value="合同号检查"><br><br>
								<input type="button" onclick="pay();" value="确认支付"/>				 
							</div>	
							<!-- 世巴初审 -->
                             <div id="approve1" style="display: none;">	
                                 世巴初审：<br>
				            <textarea name="approve1Comments" id="approve1Comments"></textarea><br>
							<input type="radio" name="approve1Result" value="true" checked="checked">素材无正常
				            <input type="radio" name="approve1Result" value="false" >素材异常
				            <br>
				            <button onclick="approve1();" >提交</button>
							 </div> 
							 
							  <!-- 北广审核并填写物料ID等信息 -->
                             <div id="approve2" style="display: none;">	
                                                                                                 北广审核并填写物料ID等信息
							 </div>
							 
							  <!-- 世巴财务确认 -->
                             <div id="financialCheck" style="display: none;">	
                                                                               世巴财务确认 ：<br>
				            <textarea name="comment" id="comment"></textarea><br>
							<input type="radio" name="rad" value="true" checked="checked">支付正常
				            <input type="radio" name="rad" value="false" >支付异常
				            <button onclick="financial();" >提交</button>	
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
</body>
</html>