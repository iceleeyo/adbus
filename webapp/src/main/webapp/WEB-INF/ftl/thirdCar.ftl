<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!doctype html>
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>thirdCar</title>
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/base.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/header.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/jack.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/jquery-ui/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/jquery-ui-1.8.16.custom.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/secondLevel.css">	</head>
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
					<img src="${rc.contextPath}/index_img/pic1.png" height="100" width="100">
				</div>
				<ul class="navibar">
					<li><a>首页</a></li>
					<li ><a href="${rc.contextPath}/secondLevelPage">移动视频</a></li>
					<li class="active"><a href="${rc.contextPath}/secondLevelPageBus">车身媒体</a></li>
					<li><a href="/caseMore.html">案例欣赏</a></li>
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
								<img src="${rc.contextPath}/index_img/wp1_1.jpg" width="360" height="290">
								<div class="sm-imgs">
									<img id="ima" src="${rc.contextPath}/index_img/wp1_1.jpg" height="65" width="65">
									<img src="${rc.contextPath}/index_img/wp1_1.jpg" height="65" width="65">
									<img src="${rc.contextPath}/index_img/wp1_1.jpg" height="65" width="65">
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
									<dt>车型：</dt>
									<dd>
									<#if  busOrderDetailV2.doubleDecker==true>
									双层
									<#else>
									单层
									</#if>
										
									</dd>
								</dl>
								<dl class="d-item">
									<dt>级别：</dt>
									<dd>
										${busOrderDetailV2.leval.nameStr}
									</dd>
								</dl>
								<dl class="d-item">
									<dt>线路：</dt>
									<dd>
										${busOrderDetailV2.jpaProductV2.name}
									</dd>
								</dl>
								<dl class="d-item ">
									<dt>价格：<input type="hidden" value="${busOrderDetailV2.price}" id="uprice"/>
									 <input type="hidden" value="${busOrderDetailV2.days}" id="udays"/>
									</dt>
									<dd id="price" >
										￥${busOrderDetailV2.price}
									</dd>
								</dl>
								<dl class="d-item perio">
									<dt>刊期：</dt>
									<dd>
										<ul class="clearfix" id="kanqi">
											<li class="active"><a href="javascript:void(0)" qc="30">一个月</a></li>
											<li><a href="javascript:void(0)" qc="90">一个季度</a></li>
											<li><a href="javascript:void(0)" qc="360">一年</a></li>
										</ul>
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
										上刊日期：
                                           	<input  class="ui-input datepicker validate[required,custom[date] " 
                                                type="text"  id="startTime" data-is="isAmount isEnough"
                                                autocomplete="off" >
									</dd>
								</dl>

								<div class="btn-group">
									<div>
										<a href="javascript:void(0);"  onclick="buy('${rc.contextPath}',${busOrderDetailV2.id})" class="btn btn-now">立即购买</a>
										<a href="javascript:void(0);"  onclick="putIncar('${rc.contextPath}',${busOrderDetailV2.id})" class="btn btn-cart">加入购物车</a>
								<@security.authorize access="isAuthenticated()"> <input
								type="hidden" id="lc" value="1" /> </@security.authorize>
								<@security.authorize access="! isAuthenticated()"> <input
								type="hidden" id="lc" value="0" /> </@security.authorize>
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
										<div class="pic-table c-head clearfix">
									<div class="tbhead">硬广套装</div>
									<div class="tbbody">
										<div class="tbtr">
											<div class="tbtd tb-position tb-left">
												<span class="icon icon-position"></span>
												<span>北京</span>
											</div>
											<div class="tbtd tb-flow">
												<span class="icon icon-flow"></span><span>1300万人次/日</span>
											</div>
										</div>
										<div class="tbtr">
											<div class="tbtd tb-path tb-left">
												<span class="icon icon-path"></span><span>北京六环内<br>470余条公交线路</span>
											</div>
											<div class="tbtd tb-medium">
												<span class="icon icon-medium"></span><span>19寸公交彩色液晶电视</span>
											</div>
										</div>
										<div class="tbtr">
											<div class="tbtd tb-firm tb-left">
												<span class="icon icon-firm"></span><span>北广传媒</span>
											</div>
											<div class="tbtd tb-number">
												<span class="icon icon-number"></span><span>12000辆车24000块屏</span>
											</div>
										</div>
									</div>
								</div>
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
		function buy(pathurl,id){
		var medIds="";
		var lc=$("#lc").val();
		if(lc=="0"){
			islogin(pathurl);
		}
		
		if(lc=="1"){	
		var days=$("#kanqi .active").children().attr("qc");
				$.ajax({
						url : "${rc.contextPath}/carbox/buy/body",
						data:{"proid":id,"needCount":$("#needCount").val(),"days":days},
						type : "POST",
						success : function(data) {
						if(data.left){
						  window.location.href="${rc.contextPath}/select?meids="+medIds+"&boids="+data.right;
						}
					}}, "text");
		}

		 }
		function putIncar(pathurl,id){
			var lc=$("#lc").val();
			if(lc=="0"){
				islogin(pathurl);
			}
			if(lc=="1"){
				var days=$("#kanqi .active").children().attr("qc");
	         		$.ajax({
						url : "${rc.contextPath}/carbox/putIncar/body",
						data:{"proid":id,"needCount":$("#needCount").val(),"days":days},
						type : "POST",
						success : function(data) {
						alert(data.right);
					}}, "text");
			}
			
		 }
		function changeMonyByNeedCounts(needc){
		       var days=$("#kanqi .active").children().attr("qc");
		       var p=days/$("#udays").val()*needc*$("#uprice").val();
				 $("#price").html("￥"+p);
		}
		function changeMonyByDays(days){
		 var p=days/$("#udays").val()*$("#needCount").val()*$("#uprice").val();
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