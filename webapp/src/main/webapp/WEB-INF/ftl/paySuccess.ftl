<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="/css/one.css">
<link rel="stylesheet" type="text/css" href="/css/account.css">
<title>交易成功</title>

</head>
<body>
	<header> <!-- 头部开始 --> <#include "/index_menu/index_top.ftl"
	/> <!-- 头部结束 --> </header>
	<div class="content">
		<div class="side-nav">
			<div class="logo"></div>
			<div class="de-code">
				<img src="${rc.contextPath}/index_img/pic1.png" height="100"
					width="100">
			</div>
			<ul class="navibar">
				<li><a href="/">首页</a></li>
				<li class="active"><a href="${rc.contextPath}/media">产品媒体</a></li>
				<li><a href="/caseMore.html">案例欣赏</a></li>
			</ul>
			<div class="markble">
				<p>世界在你脚下，巴士一路随行</p>
				<p>北巴出品</p>
				<p>北京公交媒体</p>
			</div>
		</div>
		<div class="cover">
			<div class="c-top"></div>
			<div class="cart-container cart-step3">
				<div class="c-inner">
					<div class="tabline">
						<div class="arrow arrow-stp1">
							<span class="ara">1</span><a href="${rc.contextPath}/toCard"><span>查看购物车</span></a>
						</div>
						<div class="arrow arrow-stp2">
							<span class="ara">2</span><span>确认订单信息</span>
						</div>
						<div class="arrow arrow-stp3">
							<span class="ara">3</span><span>成功提交订单</span>
						</div>
					</div>
					<div class="success-content clearfix">
						<p class="success-area">
							<span></span>交易成功！
						</p>
						
						<div class="worm-tips">
							<div class="tips-title">
							<span class="icon"></span>
			<font color="orange"><B>温馨提示</B></font><br>
			三方一致：合同甲方公司名称-付款方银行开户名称-我方开具发票抬头名称<br>
			线下付款的账户信息：<br>
			&nbsp;&nbsp;&nbsp;&nbsp;开户行：<B>工行知春路支行</B><br>
			&nbsp;&nbsp;&nbsp;&nbsp;账户名称：<b>北京世巴传媒有限公司</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;收款方账号：<b>0200207909200097152</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;公司地址：北京市海淀区紫竹院路32号15号平房	电话：68427368<br>
		</div>
		</div>
						<p class="more-info">
						<#if metype?? && metype=='body'>
							<a href="${rc.contextPath}/carbox/carTask">查看订单</a>
							<#else>
							<a href="${rc.contextPath}/order/myTask/1">查看订单</a>
					     </#if>
						</p>
					</div>

				</div>
			</div>
		</div>
	</div>
	<div class="jack jacksec" style="height: 296px; top: 160.5px;">
		<ul class="icons">
			<li class="up"><i></i></li>
			<li class="qq"><i></i></li>
			<li class="tel"><i></i></li>
			<li class="wechat"><i></i></li>
			<li class="down"><i></i></li>
		</ul>
		<a class="switch"></a>
	</div>

	<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
	<script src="index_js/unslider.min.js"></script>
	<script type="text/javascript">
		
		
			$(document).ready(function(e) {
				//默认.active下radio选中
				if($('li').is('.active')){
					$('.active').prev()[0].checked = true;
				}
				/* console.log($('.active').prev()[0].checked); */ 

				$('.cart-check label').on('click', function(event) {
					event.preventDefault();

					$(this).parent().addClass('active');
					$(this).prev().checked = true;
				});

				$('.legged .iradio').on('click', function(event) {
					event.preventDefault();
					$(this).prev()[0].checked = true;
					$(this).parent().siblings().removeClass('active').end().addClass('active');
				});
			});
		</script>
</body>
</html>