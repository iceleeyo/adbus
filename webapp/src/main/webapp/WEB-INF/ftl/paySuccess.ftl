<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>交易成功</title>
		 
		</head>
	<body>
<header>
		<!-- 头部开始 -->
<#include "/index_menu/index_top.ftl" />
		<!-- 头部结束 -->
</header>
		<div class="content">
			<div class="side-nav">
				<div class="logo"></div>
				<div class="de-code">
					<img src="index_img/pic1.png" height="100" width="100">
				</div>
				<ul class="navibar">
					<li><a>首页</a></li>
					<li class="active"><a>产品媒体</a></li>
					<li><a>案例欣赏</a></li>
				</ul>
				<div class="markble">
					<p>世界在你脚下，巴士一路随行</p>
					<p>北巴出品</p>
					<p>北京公交媒体</p>
				</div>
			</div>
			<div class="cover">
				<div class="c-top">
				</div>
				<div class="cart-container cart-step3">
					<div class="c-inner">
						<div class="tabline">
							<div class="arrow arrow-stp1"><span class="ara">1</span><a href="${rc.contextPath}/toCard"><span>查看购物车</span></a></div>
							<div class="arrow arrow-stp2"><span class="ara">2</span><span>确认订单信息</span></div>
							<div class="arrow arrow-stp3"><span class="ara">3</span><span>成功提交订单</span></div>
						</div>
					 <div class="success-content clearfix">
							<p class="success-area"><span></span>交易成功！</p>
							<p class="more-info"><a href="${rc.contextPath}/carbox/carTask">查看订单</a></p>
					</div>
					
					</div>
				</div>
			</div>
	  </div>
	  <div class="jack jacksec" style="height: 296px; top: 160.5px;">
        <ul class="icons">
        	<li class="up"><i></i></li>
            <li class="qq">
            	<i></i>
            </li>
            <li class="tel">
            	<i></i>
            </li>
            <li class="wechat">
            	<i></i>
            </li>
            <li class="down"><i></i></li>
        </ul>
        <a class="switch"></a>
      </div>

		<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
		<!--增加lay最新版本-->
	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer.onload.js"></script>
	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>
		<script src="index_js/unslider.min.js"></script>
		<script type="text/javascript">
		function payment(){
		var paytype=$('#payway :radio[name=payType]:checked').val();
		var divid=$("#dividpay").val();
		var seriaNum=${seriaNum};
		var meids='${meids}';
		var boids='${boids}';
		if(paytype=="" || typeof(paytype)=="undefined"){
		  layer.msg("请选择支付方式");
		  return;
		}
		if(seriaNum=="" || typeof(seriaNum)=="undefined"){
		  layer.msg("没有seriaNum,操作异常");
		  return;
		}
		$.ajax({
			url:"${rc.contextPath}/carbox/payment",
			type:"POST",
			async:false,
			dataType:"json",
			data:{"divid":divid,"seriaNum":seriaNum,"paytype":paytype,"meids":meids,"boids":boids},
			success:function(data){
				if (data.left) {
					layer.msg(data.right);
				   var uptime = window.setTimeout(function(){
				   window.location.href="${rc.contextPath}/order/myOrders/1";
			   	    clearTimeout(uptime);
						},1500)
				} else {
					layer.msg(data.right);
				}
			}
          });  
		}
		
		
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