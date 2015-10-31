<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<title>移动视频产品</title>
<script type="text/javascript">
/**
 * 查看购物车判断登陆状态
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
		<header>
		<!-- 头部开始 -->
<#include "/index_menu/index_top.ftl" />
<script src="index_js/sift_common.js"></script>
<script src="index_js/sift_bus.js"></script>
		<!-- 头部结束 -->
		</header>
		<div class="content">
			<div class="side-nav">
				<div class="logo"></div>
				<div class="de-code">
					<img src="index_img/pic1.png" height="100" width="100">
				</div>
				<ul class="navibar">
					<li><a href="/">首页</a></li>
					<li class="active"><a href="${rc.contextPath}/secondLevelPage">移动视频</a></li>
					<li><a href="${rc.contextPath}/secondLevelPageBus">车身媒体</a></li>
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
							<span>视频广告</span>
							<span>医院周边</span>
							<span>特级</span>
							<span>CBD</span>
							<span>A++</span>
							<span>东城区</span>
						</div>
					</div>
				</div>
				<div class="md-nav">
					媒体产品>移动电视
				</div>
				<div class="ad">

					<div class="banner" id="b04">
					    <ul>
					        <li><img src="index_img/new-06.png" alt="" width="100%" height="260" ></li>
					        <li><img src="index_img/new-07.png" alt="" width="100%" height="260" ></li>
					        <li><img src="index_img/new-08.png" alt="" width="100%" height="260" ></li>
					    </ul>
					</div>
				</div>
				<div class="recommand suit pd">
					<div class="title"><span>推荐套装</span></div>
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
									<p>价格：500000.00</p>
									<p>周期：3个月</p>
								</div>
								<div class="select-item">
									<img src="index_img/internet.png">
									<p>互联网</p>
									<p>价格：1500000.00</p>
									<p>周期：7天</p>
								</div>
								<div class="select-item">
									<img src="index_img/travel.png">
									<p>旅游</p>
									<p>价格：520000.00</p>
									<p>周期：1个月</p>
								</div>
								<div class="select-item">
									<img src="index_img/drink.png">
									<p>饮料</p>
									<p>价格：590000.00</p>
									<p>周期：3个月</p>
								</div>
							</div>
					</div>
				</div>
				<div class="recommand timer pd">
					<div class="title"><span>限时套装</span></div>
					<div class="re-box clearfix">
							<ul class="select">
								<li class="active"><a href="javascript:void(0)">全部</a></li>
								<li><a href="javascript:void(0)">车身广告</a></li>
								<li><a href="javascript:void(0)">视频广告</a></li>
								<li><a href="javascript:void(0)">车内广告</a></li>
								<li><a href="javascript:void(0)">站台广告</a></li>
								<li><a href="${rc.contextPath}/product/sift">更多》</a></li>
							</ul>
							<div class="select-items clearfix">
<#if auctionList?? && (auctionList?size>0) >
   <#list auctionList as prod>
								<div class="select-item">
									<div class="lasttime">
							 <#if (prod.startDate < .now  && prod.biddingDate > .now  ) > 
		  						<p>截止</p>    
		  					<span style="color:red">	<p><span id="c_${prod_index}"></span>  </p></span>
		  						<script type="text/javascript">
			  						var dateTo=new Date("${prod.biddingDate?string("yyyy-MM-dd HH:mm:ss")}".replace(/-/g, "/"));
									 countDownReload("c_${prod_index}",dateTo);
								 </script>
								<#elseif (prod.startDate > .now   ) > 
								<p>距开拍</p>    
		  						<span style="color:red"><p><span id="c_${prod_index}"></span>  </p></span>
		  						<script type="text/javascript">
			  						var dateTo=new Date("${prod.startDate?string("yyyy-MM-dd HH:mm:ss")}".replace(/-/g, "/"));
									 countDownReload("c_${prod_index}",dateTo);
								 </script>
								<#elseif (prod.biddingDate < .now   ) > 
								<p><s>已结束</s></p>    
		  						<p><span id="c_${prod_index}"><s>${prod.startDate?string("yyyy-MM-dd HH:mm")}</s></span>  </p>
								</#if>
									</div>
									<img src="index_img/wp1_1.jpg" height="100" width="100">
									<div class="cost-box">
										<div class="cost">
											底价:￥<em>#{prod.saleprice!'' ;m2M2}</em>
										</div>
										<div class="timeline">
											刊期: ${prod.product.playNumber}天
										</div>
										<div class="ston">
											<a href="${rc.contextPath}/product/c/${prod.id}">
								<#if (prod.startDate < .now  && prod.biddingDate > .now  ) > 
									我要出价
								<#elseif (prod.startDate > .now   ) > 
									等待开始
								<#elseif (prod.biddingDate < .now   ) > 
									竞价结束
								</#if>
								
								</a>
										</div>
									</div>
								</div>
								</#list>
	</#if>							
								
							</div>
					</div>
				</div>
				<div class="recommand customs pd">
					<div class="title"><span>定制自选</span></div>
					<div class="title_cart"><a href="${rc.contextPath}/product/sift"><span>自定义产品</span></a></div>
					
					<div class="back">
						<div class="back-items">
							<div class="back-item">
								<span class="desp">所属地区：</span>
								<span class="sift-list" qt="c">
									<a class="item active" style="background:#ff9966" href="#" sort="-1" qc="all">北京</a>
									<a class="item" href="#" sort="-3"  qc="2" >天津<i>×</i></a>
									<a class="item" href="#" sort="-3" qc="2" >大连<i>×</i></a>
								</span>
							</div>
						
							<div class="back-item">
								<span class="desp">是否竞价：</span>
								<span class="sift-list" qt="p">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#"  qc="2" >竞价商品<i>×</i></a>
									<a class="item" href="#"  qc="3" >一口价<i>×</i></a>
								</span>
							</div>
							<div class="back-item">
								<span class="desp">产品类型：</span>
								<span class="sift-list" qt="t">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#" qc="video">视频<i>×</i></a>
									<a class="item" href="#" qc="image">图片<i>×</i></a>
									<a class="item" href="#" qc="info">文字<i>×</i></a>
								</span>
							</div>
							<div class="back-item">
								<span class="desp">日展次数：</span>
								<span class="sift-list" qt="s">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#" qc="2">0-7（不含）<i>×</i></a>
									<a class="item" href="#" qc="3">7-11（含）<i>×</i></a>
									<a class="item" href="#" qc="4">11以上<i>×</i></a>
								</span>
							</div>
							<div class="back-item">
								<span class="desp">展示期限：</span>
								<span class="sift-list" qt="d">
									<a class="item active" href="#" sort="-1" qc="all">所有</a>
									<a class="item" href="#" qc="2" >1（天）<i>×</i></a>
									<a class="item" href="#" qc="3">2-6（含）<i>×</i></a>
									<a class="item" href="#" qc="4">7（天）<i>×</i></a>
									<a class="item" href="#" qc="5">7天以上<i>×</i></a>
								</span>
							</div>
						</div>
						<div class="select-more">
							<ul>
								<li class="item1">&nbsp</li>
								<li class="item2">价格 <a href="jvascript:void(0)"><span id = "priceSort" style="color:#fff">ｖ</span></a></li>
								<li class="item3">价格<input type="text" id="price1" value="" onkeyup="value=value.replace(/[^\\d]/g,\'\')" onblur="changeByprice('${rc.contextPath}');" class="i-short"><em>—</em><input type="text" id="price2" onblur="changeByprice('${rc.contextPath}');" class="i-short"></li>
							    <li class="item4"> &nbsp</li>
							</ul>
						</div>
<input type="hidden" value = "0" id="ascOrDesc"/> 
						<div class="buy">
						<div id="productList">
						</div>
                            <input type="hidden" id="sh" value=""/>
							<div class="paginate">
								<div id="Pagination" class="pagination"></div>
							</div>

							<div class="cart-box" style="margin-top: 20px;">
							<a onclick="tocard('${rc.contextPath}');">
								<div class="cart">
									查看购物车<span id="cardCount"  style="color:#ff9966" ></span>
								</div> 
								<@security.authorize access="isAuthenticated()"> <input
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
      	
		<script type="text/javascript">
	    $(document).ready(function() {
	    
	        initPro('${rc.contextPath}',$("#sh").val(),"","",1);
	        initSwift2('${rc.contextPath}');
	       } );  
	        
	    var iIndex = 0;	
	    $('.jack .icons .up').on('click', function(e) {
	        e.preventDefault();
	        if(iIndex > 0){
	            iIndex--;
	        } else if(iIndex == 0){
	            iIndex = 0;
	        }else if(opts.loop){
	            iIndex = arrElement.length-1;
	        }
	        $('.menu ul.list-line li').eq(iIndex).addClass('active').siblings('.active').removeClass('active');
	        scrollPage(arrElement[iIndex]);
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
                  // initPro('${rc.contextPath}',$("#sh").val(),0);
                  var price1=$("#price1").val();
	               var price2=$("#price2").val();
                   initPro('${rc.contextPath}',$("#sh").val(),price1,price2,w);
       	 });
		$("#leftDec").click(function(){
			  var oldValue=$(this).next().val();//获取文本框对象现有值
			  if(oldValue>0){
				  $(this).next().val(parseInt(oldValue)-1);
			  }
			  
		});
		
		$("#leftPlus").click(function(){
			var oldValue=$(this).prev().val();//获取文本框对象现有值
			$(this).prev().val(parseInt(oldValue)+1);
		}); 
		
		/* $("#leftPlus").click(function(){
			  $(this).parent().find("input").val(parseint($(this).parent().find("input"))+1);
		});
		$("#leftDec").click(function(){
			  $(this).parent().find("input").val(parseint($(this).parent().find("input"))-1);
		}); */
			

		
	</script>
	</body>
</html>