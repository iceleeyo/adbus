<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>车身广告产品</title>
<style type="text/css">
.buy .cont .price{
	margin-top: -2px !important;
}
</style>
<script type="text/javascript">
/**
 * 加入购物车判断登陆状态
 */
function tocard(pathurl){
	var lc=$("#lc").val();
	if(lc=="0"){
		islogin(pathurl);
	}
	if(lc=="1"){
		window.location.href=pathurl+"/toCard";
	}
}
</script>
</head>
<body>
	<header> <!-- 头部开始 --> <#include "/index_menu/index_top.ftl"
	/> <script src="index_js/sift_common.js"></script> <script
		src="index_js/sift_body.js"></script> <!-- 头部结束 --> </header>
	<div class="content">
		<div class="side-nav">
			<div class="logo"></div>
			<div class="de-code">
				<img src="index_img/pic1.jpg" height="100" width="100">
			</div>
			<ul class="navibar">
				<li><a href="/">首页</a></li>
				<li><a href="${rc.contextPath}/secondLevelPage">移动视频</a></li>
				<li class="active"><a
					href="${rc.contextPath}/secondLevelPageBus">车身媒体</a></li>
				<li><a href="${rc.contextPath}/caseMore">案例欣赏</a></li>
			</ul>
			<div class="markble">
				<p>世界在你脚下，巴士一路随行</p>
				<p>北京公交媒体</p>
			</div>
		</div>
		<div class="cover">
			<div class="c-top">
				<div class="c-search">
					<div class="search-panel">
						<input type="text" id="searchText" value="">
					</div>
					<div class="search-handle">
						<button class="search-btn" type="submit" id="search-btn">搜索</button>
					</div>
					<div class="search-key">
						<span><a href="javascript:void(0)">车身广告</a></span> <span><a
							href="javascript:void(0)">视频广告</a></span> <span><a
							href="javascript:void(0)">医院周边</a></span> <span><a
							href="javascript:void(0)">特级</a></span> <span><a
							href="javascript:void(0)">CBD</a></span> <span><a
							href="javascript:void(0)">A++</a></span> <span><a
							href="javascript:void(0)">东城区</a></span>
					</div>
				</div>
			</div>
			<div class="middle">
			<div class="md-nav">媒体产品>车身广告</div>
			<div class="ad">

				<div class="banner" id="b04">
					<ul>
						<li><img src="index_img/new-06.png" alt="" width="100%"
							height="260"></li>
						<li><img src="index_img/new-07.png" alt="" width="100%"
							height="260"></li>
						<li><img src="index_img/new-08.jpg" alt="" width="100%"
							height="260"></li>
						<li><img src="index_img/new-09.jpg" alt="" width="100%"
							height="260"></li>
					</ul>
				</div>
			</div>
			<!-- <div class="recommand suit pd">
				<div class="title">
					<span>推荐套装</span>
				</div>
				<div class="re-box clearfix">
					<ul class="select">
						<li class="active"><a href="javascript:void(0)">全部</a></li>
						<li><a href="javascript:void(0)">50万-100万</a></li>
						<li><a href="javascript:void(0)">100万-400万</a></li>
						<li><a href="javascript:void(0)">400万-1000万</a></li>
						<li><a href="javascript:void(0)">1000万以上</a></li>
					</ul>
					<div class="select-items clearfix">
						<div class="select-item">
							<img src="index_img/food.png">
							<p>食品</p>
							<p>价格：500000</p>
							<p>周期：3个月</p>
						</div>
						<div class="select-item">
							<img src="index_img/internet.png">
							<p>互联网</p>
							<p>价格：1500000</p>
							<p>周期：7天</p>
						</div>
						<div class="select-item">
							<img src="index_img/travel.png">
							<p>旅游</p>
							<p>价格：520000</p>
							<p>周期：1个月</p>
						</div>
						<div class="select-item">
							<img src="index_img/drink.png">
							<p>饮料</p>
							<p>价格：590000</p>
							<p>周期：3个月</p>
						</div>
					</div>
				</div>
			</div> -->
			
			<!--
			<div class="recommand timer pd">
				<div class="title">
					<span>限时套装</span>
				</div>
				<div class="re-box clearfix">
					<ul class="select">
						<li class="active"><a href="javascript:void(0)">全部</a></li>
						<li><a href="javascript:void(0)">车身广告</a></li>
						<li><a href="javascript:void(0)">视频广告</a></li>
						<li><a href="javascript:void(0)">车内广告</a></li>
						<li><a href="javascript:void(0)">站台广告</a></li>
						<li><a href="${rc.contextPath}/product/sift_bus">更多》</a></li>
					</ul>
					<div class="select-items clearfix">
						<#if auctionList?? && (auctionList?size>0) > <#list auctionList as
						prod>
						<div class="select-item">
							<div class="lasttime">
								<#if (prod.startDate < .now && prod.biddingDate > .now ) >
								<p>截止</p>
								<span style="color: red"><p>
										<span id="c_${prod_index}"></span>
									</p></span>
								<script type="text/javascript">
			  						var dateTo=new Date("${prod.biddingDate?string("yyyy-MM-dd HH:mm:ss")}".replace(/-/g, "/"));
									 countDownReload("c_${prod_index}",dateTo);
								 </script>
								<#elseif (prod.startDate > .now ) >
								<p>距开拍</p>
								<span style="color: red"><p>
										<span id="c_${prod_index}"></span>
									</p></span>
								<script type="text/javascript">
			  						var dateTo=new Date("${prod.startDate?string("yyyy-MM-dd HH:mm:ss")}".replace(/-/g, "/"));
									 countDownReload("c_${prod_index}",dateTo);
								 </script>
								<#elseif (prod.biddingDate < .now ) >
								<p>
									<s>已结束</s>
								</p>
								<p>
									<span id="c_${prod_index}"><s>${prod.startDate?string("yyyy-MM-dd HH:mm")}</s></span>
								</p>
								</#if>
							</div>
							<img src="${rc.contextPath}/upload_temp/${prod.product.imgurl}"
								height="100" width="100">
							<div class="cost-box">
								<div class="cost">
									底价:￥<em>${prod.saleprice}</em>
								</div>
								<div class="timeline">刊期: ${prod.product.playNumber}天</div>
								<div class="ston">
									<a href="${rc.contextPath}/product/c/${prod.id}"> <#if
										(prod.startDate < .now && prod.biddingDate > .now ) > 我要出价
										<#elseif (prod.startDate > .now ) > 等待开始 <#elseif
										(prod.biddingDate < .now ) > 竞价结束 </#if> </a>
								</div>
							</div>
						</div>
						</#list> </#if>
					</div>
				</div>
			</div>
			</div>
			-->
			<div class="recommand customs pd">
				<div class="title">
					<span>定制自选</span>
				</div>
				<div class="title_cart">
					<a href="${rc.contextPath}/product/sift_bus"><span>自定义产品</span></a>
				</div>
				<div class="back">
					<div class="back-items">
						<div class="back-item">
							<span class="desp">地区：</span> <span class="sift-list" qt="c">
								<a class="item active" style="background: #ff9966" href="#"
								sort="-1" qc="all">北京</a> <a class="item" href="#" sort="-3"
								qc="2">天津<i></i></a> <a class="item" href="#" sort="-3" qc="2">大连<i></i></a>
							</span>
						</div>

						<div class="back-item">
							<span class="desp">商圈：</span> <span class="sift-list" qt="B">
								<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
								class="item" href="javascript:;" qc="王府井">王府井<i>×</i></a> <a
								class="item" href="javascript:;" qc="西单">西单<i>×</i></a> <a
								class="item" href="javascript:;" qc="国贸">国贸<i>×</i></a> <a
								class="item" href="javascript:;" qc="公主坟">公主坟<i>×</i></a> <a
								class="item" href="javascript:;" qc="中关村">中关村<i>×</i></a> <a
								class="item" href="javascript:;" qc="东直门">东直门<i>×</i></a>
							</span>

						</div>
						<div class="back-item">
							<span class="desp">城区：</span> <span class="sift-list" qt="C">
								<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
								class="item" href="javascript:;" qc="海淀" sort="-1">海淀<i>×</i></a>
								<a class="item" href="javascript:;" qc="朝阳">朝阳<i>×</i></a> <a
								class="item" href="javascript:;" qc="丰台">丰台<i>×</i></a> <a
								class="item" href="javascript:;" qc="东城">东城<i>×</i></a> <a
								class="item" href="javascript:;" qc="西城">西城<i>×</i></a> <a
								class="item" href="javascript:;" qc="西单">西单<i>×</i></a> <a
								class="item" href="javascript:;" qc="通州">通州<i>×</i></a> <a
								class="item" href="javascript:;" qc="大兴"><i>大兴</i></a> <a
								class="item" href="javascript:;" qc="密云"><i>密云</i></a>
							</span>

						</div>
						<div class="back-item">
							<span class="desp">车型：</span> <span class="sift-list" qt="D">
								<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
								class="item" href="javascript:;" qc="1_0">双层<i>×</i></a> <a
								class="item" href="javascript:;" qc="1_S">双层特级<i>×</i></a> <a
								class="item" href="javascript:;" qc="1_APP">双层A++<i>×</i></a> <a
								class="item" href="javascript:;" qc="1_AP">双层A+<i>×</i></a> <br>
								<a style="margin-left: 80px;" class="item" href="javascript:;"
								qc="0_0">单层<i>×</i></a> <a class="item" href="javascript:;"
								qc="0_S">单层特级<i>×</i></a> <a class="item" href="javascript:;"
								qc="0_APP">单层A++<i>×</i></a> <a class="item"
								href="javascript:;" qc="0_AP">单层A+<i>×</i></a>
							</span>

						</div>
					</div>
					<div class="select-more">
						<ul>
							<!--<li class="item1">销量</li>-->
							<li class="item1">&nbsp</li>
							<li class="item2">价格 <a href="jvascript:void(0)"><span
									id="priceSort" style="color: #fff">ｖ</span></a></li>
							<li class="item3">价格<input id="price1" class="i-short"><em>—</em><input
								id="price2" class="i-short"></li>
							<li class="item4">&nbsp</li>
						</ul>
						<input type="hidden" value="0" id="ascOrDesc" />
					</div>

					<div class="buy">
						<div id="productList"></div>
						<!--
							<div class="cont">
								<div class="activity inline-b"><span>特11</span> 双层 特级</div>
								<div class="price inline-b">
									<p class="p-one"><em>1700</em>元/月/辆</p>
									<p class="p-two">5000000人次/月</p>
								</div>
								<div class="num f-left inline-b">
									<input type="button" class="icon f-left dec" id="leftDec" />
									<input class="f-left" id="sum" value="0">
									<input type="button" class="icon f-left plus" id="leftPlus" />
								</div>
								<div class="map f-left inline-b">
									<div class="map-box"></div>
								</div>
							</div>
							-->
						<input type="hidden" id="sh" value="" />
						<div class="paginate">
							<div id="Pagination" class="pagination"></div>
						</div>

						<div class="cart-box">
							<a onclick="tocard('${rc.contextPath}');">
								<div class="cart">
									查看购物车<span id="cardCount" style="color: #ff9966"></span>
								</div> <@security.authorize access="isAuthenticated()"> <input
								type="hidden" id="lc" value="1" /> </@security.authorize>
								<@security.authorize access="! isAuthenticated()"> <input
								type="hidden" id="lc" value="0" /> </@security.authorize>
							</a>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<#include "/template/custom_service.ftl" />

	<script type="text/javascript">
		
		   $(document).ready(function() {
	        initPro('${rc.contextPath}',$("#sh").val(),"","",1);
	        initSwift2('${rc.contextPath}');
	        $('#price1, #price2').change(function() {
                 initPro('${rc.contextPath}',$("#sh").val(),0);
       		 });
       	   $('#priceSort').click(function() {
                  var w = $('#ascOrDesc').val();
                  if(w==0){
                    $('#ascOrDesc').val(1);
                      $('#priceSort').html("＾"); 
                  }else{
                   $('#ascOrDesc').val(0); 
                      $('#priceSort').html("ｖ"); 
                  }
                   initPro('${rc.contextPath}',$("#sh").val(),0);
       	 });
       	 
       	 
	    } );
	    
		$('.search-btn').click(function(){
		    initPro('${rc.contextPath}',$("#sh").val(),"","",1);
		    $('.middle,.title,.title_cart,.back-items').hide();
	   		$('.select-more').css('margin-top','30px');
	   	} );  
	    
	    $("#leftDec").click(function(pathurl){
	    	
			  var oldValue=$(this).next().val();//获取文本框对象现有值
			  if(oldValue>0){
				  $(this).next().val(parseInt(oldValue)-1);
			  }
			
		});
		
		/*$("#leftPlus").click(function(){
			var oldValue=$(this).prev().val();//获取文本框对象现有值
			$(this).prev().val(parseInt(oldValue)+1);
		}); 
		
		 $("#leftPlus").click(function(){
			  $(this).parent().find("input").val(parseint($(this).parent().find("input"))+1);
		});
		$("#leftDec").click(function(){
			  $(this).parent().find("input").val(parseint($(this).parent().find("input"))-1);
		}); */
			

	</script>
</body>
</html>