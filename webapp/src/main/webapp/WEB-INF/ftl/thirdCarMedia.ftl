<!doctype html>
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>thirdCarMedia</title>
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/base.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/header.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/jack.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/jquery-ui/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/jquery-ui-1.8.16.custom.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/secondLevel.css">	</head>
	<body>
		<header>
				<span class="area">北京</span>
				<nav class="menu">
					<ul class="list-line">
						<li><a href="jvascript:void(0)">首页</a></li>
						<li class="active"><a href="jvascript:void(0)">媒体产品</a></li>
						<li><a href="jvascript:void(0)">传播效果</a></li>
						<li><a href="jvascript:void(0)">案例欣赏</a></li>
						<li><a href="jvascript:void(0)">合作伙伴</a></li>
						<li><a href="jvascript:void(0)">关于我们</a></li>
					</ul>
					<ul class="login">
						<li><a href="#">登录</a></li>|
						<li><a href="#">注册</a></li>
					</ul>
				</nav>
			</header>
		<div class="content">
			<div class="side-nav">
				<div class="logo"></div>
				<div class="de-code">
					<img src="${rc.contextPath}/index_img/pic1.png" height="100" width="100">
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
					<div class="c-search">
						<div class="search-panel">
							<input type="text" value="">
						</div>
						<div class="search-handle">
							<button class="search-btn" type="submit" >搜索</button>
						</div>
						<div class="search-key">
							<span>车身广告</span>
							<span>车身广告</span>
							<span>车身广告</span>
							<span>车身广告</span>
							<span>车身广告</span>
						</div>
					</div>
				</div>
				<div class="md-nav">
					媒体产品>车身媒体
				</div>		

				<div class="third-step">
					<div class="detail">
						<div class="detail-bd clearfix">
							<div class="d-left">
								<img src="${rc.contextPath}/index_img/bg1.png" width="345" height="345">
								<div class="sm-img/">
									<img id="ima" src="../index_img/bg1.png" height="65" width="65">
									<img src="" height="65" width="65">
									<img src="" height="65" width="65">
								</div>
								<div class="clearfix socials">
									<ul class="social">
										<li>
											<a href="" class="qq icon"></a>
										</li>
										<li>
											<a href="" class="wechat icon"></a>
										</li>
										<li>
											<a href="" class="weibo icon"></a>
										</li>
										<li>
											<a href="" class="email icon"></a>
										</li>
									</ul>
									<div class="focus">
										<a href="javascript:void(0)" class="btn btn-focus">+ 关注</a>
									</div>
								</div>
							</div>
							<div class="d-right">
								<dl class="d-item">
									<dt>名称：</dt>
									<dd>
										${jpaProduct.name}
									</dd>
								</dl>
								<dl class="d-item">
									<dt>类型：</dt>
									<dd>
										${jpaProduct.type.typeName}
									</dd>
								</dl>
								<dl class="d-item">
									<dt>次数：</dt>
									<dd>
										${jpaProduct.duration}秒/次  &nbsp;${jpaProduct.playNumber}次/天
									</dd>
								</dl>
								<dl class="d-item">
									<dt>刊期：</dt>
									<dd>
										${jpaProduct.days}天
									</dd>
								</dl>
								<dl class="d-item ">
									<dt>价格：<input type="hidden" id="uprice" value="${jpaProduct.price}"/> 
									</dt>
									<dd id="price" >
										￥${jpaProduct.price}
									</dd>
								</dl>
								<dl class="d-item number">
									<dt>数量：</dt>
									<dd>
										<span class="stock">
											<input type="text" id="needCount" onblur="changeMonyByNeedCounts(this.value);" value="1">
											<a class="stock-plus" href="javascript:void(0)" onclick="b_leftPlus();">+</a>
											<a class="stock-dl" href="javascript:void(0)" onclick="b_leftDec();" >-</a>
										</span>
									</dd>
								</dl>
								<dl class="d-item random">
									<dd>
										上播日期：
                                           	<input  class="ui-input datepicker validate[required,custom[date] " 
                                                type="text"  id="startTime" data-is="isAmount isEnough"
                                                autocomplete="off" >
									</dd>
								</dl>

								<div class="btn-group">
									<div>
										<a href="javascript:void(0);"  onclick="buy(${jpaProduct.id})" class="btn btn-now">立即购买</a>
										<a href="javascript:void(0);"  onclick="putIncar(${jpaProduct.id})" class="btn btn-cart">加入购物车</a>
									</div>							
								</div>
							</div>
						</div>
						
						<div class="l-lau">
							<a href="javascript:void(0)" class="btn btn-contact">联系客服</a>
						</div>

						<div class="layout">
							<div class="layout-top">
								<div class="product-info">
									产品介绍
									<span></span>
								</div>
							</div>
							<div class="layout-contain">
								<div class="c-head">
									<p class="head-en">MEDIA PROFILE</p>
									<p class="head-zh">媒体情况</p>
									<p class="icon icon-line"></p>
								</div>
								<img src="${rc.contextPath}/index_img/thirdCar_1.png">
								<div class="c-head">
									<p class="head-en">MEDIA REGIONAL</p>
									<p class="head-zh">媒体区域</p>
									<p class="icon icon-line"></p>
								</div>
								<img src="${rc.contextPath}/index_img/thirdCar_2.png">
								<div class="c-head">
									<p class="head-en">MEDIA SHOW</p>
									<p class="head-zh">媒体展示</p>
									<p class="icon icon-line"></p>
								</div>
								<img src="${rc.contextPath}/index_img/thirdCar_3.png">
								<div class="c-head">
									<p class="head-en">MEDIA REGIONAL</p>
									<p class="head-zh">合作品牌</p>
									<p class="icon icon-line"></p>
								</div>
								<img src="${rc.contextPath}/index_img/thirdCar_4.png">
							</div>
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

		<script type="text/javascript" src="${rc.contextPath}/index_js/jquery-1.11.1.min.js"></script>
		<script src="${rc.contextPath}/index_js/unslider.min.js"></script>
		<script src="${rc.contextPath}/js/datepicker.js"></script>
		<script src="${rc.contextPath}/js/jquery.datepicker.region.cn.js"></script>
		<script src="${rc.contextPath}/js/jquery-ui/jquery-ui.js"></script>
		<script type="text/javascript">
		  $(document).ready(function() {
		  $("#kanqi li").click(function(){
				var days=$(this).children().attr("qc");		
				changeMonyByDays(days);
			});
	    } );
		function buy(id){
		var bodIds="";
		         $.ajax({
							url : "${rc.contextPath}/carbox/buy/media",
							data:{"proid":id,"needCount":$("#needCount").val(),"days":0},
							type : "POST",
							success : function(data) {
							if(data.left){
							  window.location.href="${rc.contextPath}/select?meids="+data.right+"&boids="+bodIds;
							}
						}}, "text");
		 }
		function putIncar(id){
		         $.ajax({
							url : "${rc.contextPath}/carbox/putIncar/media",
							data:{"proid":id,"needCount":$("#needCount").val(),"days":0},
							type : "POST",
							success : function(data) {
							alert(data.right);
						}}, "text");
		 }
		function changeMonyByNeedCounts(needc){
		       var p=needc*$("#uprice").val();
				 $("#price").html("￥"+p);
		}
		function b_leftDec(){
			    var y=$("#needCount").val();
			    if(y>1){
			    $("#needCount").val(parseInt(y)-1);
			  }
			  changeMonyByNeedCounts($("#needCount").val());
		}
		
		function b_leftPlus(){
			var y=$("#needCount").val();
			$("#needCount").val(parseInt(y)+1);
			changeMonyByNeedCounts($("#needCount").val());
		}
		
		
		$(document).ready(function(e) {
		   $('.perio li').on('click', function(event) {
		   	event.preventDefault();

		   	$(this).siblings().removeClass('active').end().addClass('active');
		   });
		});
	</script>
	</body>
</html>