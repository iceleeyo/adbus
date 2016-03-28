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
	<header> <!-- 头部开始 --> <#include "/index_menu/index_top.ftl"/>
	<script src="index_js/sift_common.js"></script> <script
		src="index_js/sift_bus.js"></script> <!-- 头部结束 --> </header>
	<div class="content">
		<div class="side-nav">
			<div class="logo2"></div>
			<div class="de-code">
				<img src="index_img/pic1.jpg" height="100" width="100">
			</div>
			<ul class="navibar">
				<li><a href="/">首页</a></li>
				<li class="active"><a href="${rc.contextPath}/secondLevelPage">移动视频</a></li>
				<li><a href="${rc.contextPath}/secondLevelPageBus">车身媒体</a></li>
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
						<span><a href="javascript:void(0)">全屏硬广</a></span> <span><a
							href="javascript:void(0)">INFO图片</a></span> <span><a
							href="javascript:void(0)">INFO字幕</a></span> <span><a
							href="javascript:void(0)">32次</a></span> <span><a
							href="javascript:void(0)">64次</a></span> <span><a
							href="javascript:void(0)">14天</a></span> 
						<span><a href="javascript:void(0)">28天</a></span>
						<span><a href="javascript:void(0)">90天</a></span>
					</div>
				</div>
			</div>
			<div class="middle">
				<!--  <div class="md-nav">媒体产品>移动电视</div> -->
				<div class="ad">
					<div class="banner" id="b04">
						<ul>
							<li><img src="index_img/new-08.jpg" alt="" width="100%"
								height="260"></li>
							<li><img src="index_img/new-09.jpg" alt="" width="100%"
								height="260"></li>
							<li><img src="index_img/new-06.png" alt="" width="100%"
								height="260"></li>
							<li><img src="index_img/new-07.png" alt="" width="100%"
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
							<img src="index_img/food.png"><br>
							<p style="padding-top: 10px;">食品</p>
							<p>
								价格：￥<em>500000.00</em>
							</p>
							<p>周期：3个月</p>
						</div>
						<div class="select-item">
							<img src="index_img/internet.png">
							<p style="padding-top: 10px;">互联网</p>
							<p>
								价格：￥<em>1500000.00</em>
							</p>
							<p>周期：7天</p>
						</div>
						<div class="select-item">
							<img src="index_img/travel.png">
							<p style="padding-top: 10px;">旅游</p>
							<p>
								价格：￥<em>520000.00</em>
							</p>
							<p>周期：1个月</p>
						</div>
						<div class="select-item">
							<img src="index_img/drink.png">
							<p style="padding-top: 10px;">饮料</p>
							<p>
								价格：￥<em>590000.00</em>
							</p>
							<p>周期：3个月</p>
						</div>
					</div>
				</div>
			</div> -->
				
				<div class="recommand timer pd">
					<div class="title">
						<span>限时套装</span>
					</div>
					<div class="re-box clearfix">
						<ul class="select">
							<li class="active"><a href="javascript:void(0)">全部</a></li>
							<li><a href="javascript:void(0)">全屏硬广</a></li>
							<li><a href="javascript:void(0)">INFO字幕</a></li>
							<li><a href="javascript:void(0)">INFO图片</a></li>
							<li><a href="javascript:void(0)">二类节目</a></li>
							<li><a href="${rc.contextPath}/product/sift">更多》</a></li>
						</ul>
						<div class="select-items clearfix">
							<#if auctionList?? && (auctionList?size>0) > <#list auctionList
							as prod>
							<div class="select-item">
								<div class="lasttime">
									<#if (prod.startDate < .now && prod.biddingDate > .now ) >
									<p>截止</p>
									<span style="color: red">
										<p>
											<span id="c_${prod_index}"></span>
										</p>
									</span>
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
								<#if prod.product.imgurl?has_content > <img
									src="${rc.contextPath}/upload_temp/${prod.product.imgurl}"
									height="100" width="100"> <#else> <img
									src="index_img/exp1.jpg" height="100" width="100">
								</#if>

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
			
			<div id="con_body">
<div id="container">
			 <div id="ftinformation">
				<div class="arrow_up">	
				</div>
			<span>热卖<br>推荐</span>
			<img src="images/example1.png">
			
			</div> 
			<div id="ftinformationr">
				<div class="arrow_up">	
				</div>
			<span>热卖<br>推荐</span>
			<img src="images/example1.png">
			</div>
			<div class="down">
				<div class="down_left">
				
				<div class="down_texts">
					最新的资讯<br>永远在来的路上
				</div>
			</div>
			<div class="down_right">
				<div class="down_textt">
					2次/天  3分钟 7天<br>
					<span class="price">价格</span><span class="pricenumber">50000</span>
				</div>
				<div class="down_textf">
					<span class="buynow">立即购买</span>
				</div>
             </div>
			</div>
			<div class="downs">
				<div class="downs_left">
				
				<div class="downs_texts">
					全屏大不同<br>占有屏幕，占领眼球
				</div>
			</div>
			<div class="downs_right">
				<div class="downs_textt">
					32次/天  15秒 7天<br>
					<span class="price">价格</span><span class="pricenumber">980000</span>
				</div>
				<div class="downs_textf">
					<span class="buynow">立即购买</span>
				</div>
             </div>
			</div>
			<div id="teamcase">
				<div class="team_left">
					<div class="team_leftup">
						<img class="kid" src="images/JXH2w.jpg">
					</div>
					<div class="team_leftdown">
						<div class="time">2次/天 3分钟 1个月
						<span class="time_price">价格</span><span class="time_pricenumber">200000</span></div>
						<div class="team_bnt">
					   <span class="buynows">立即购买</span>
				        </div>
				    
					</div>
				</div>
				<div class="team_center">
					<div class="team_centerup">
						<img class="kid" src="images/JXH2w.jpg">
					</div>
					<div class="team_centerdown">
						<div class="time">2次/天 3分钟 7天
						<span class="time_price">价格</span><span class="time_pricenumber">50000</span></div>
						<div class="team_bnt">
					   <span class="buynows">立即购买</span>
				        </div>
					
					</div>
				</div>
				<div class="team_right">
					<div class="team_rightup">
						<img class="kid" src="images/JXH2w.jpg">
					</div>
					<div class="team_rightdown">
						<div class="time">2次/天 3分钟 半年
						<span class="time_price">价格</span><span class="time_pricenumber">1200000</span></div>
						<div class="team_bnt">
					   <span class="buynows">立即购买</span>
				        </div>
					
					</div>
				</div>
			</div>
			<div id="advertisement">
				<div class="adver_title">
					全屏广告
				</div>
				
					<div class="pro_ineng">
					<img src="images/adertisement.png">
				
				</div>
			</div>
			<div class="topleft">
				<img class="tl_first" src="images/JXH2w.jpg">
				<div class="arrow_ups">
					<div class="arrowtext">热卖推荐</div>
					</div>
				<div class="tl_text">
					<img class="tltitle" src="images/advers.png"><br>
					<span class="tl">全屏大不同</span><br>
					<span class="tls">每三十二次，连续7天<br>滚动播出</span>
					<br><span class="tl_price">价格</span><span class="tl_pricenumber">980000</span>
					<div class="tl_bnt">
					   <span class="tl_buynows">立即购买</span>
				        </div>
				</div>
			</div>
			<div class="topright">
				<img class="tr_first" src="images/JXH2w.jpg">
				<div class="tr_text">
					<img class="tltitle" src="images/advers.png"><br>
					<span class="tl">全屏大不同</span><br>
					<span class="tls">占有屏幕,占领眼球</span>
					<br><span class="tl_price">价格</span><span class="tl_pricenumber">980000</span>
					<div class="tl_bnt">
					   <span class="tl_buynows">立即购买</span>
				</div>
			</div>
		</div>
			<div class="botleft">
				<img class="tl_first" src="images/JXH2w.jpg">
				<div class="arrow_ups">
					<div class="arrowtext">热卖推荐</div>
					</div>
				<div class="bl_text">
					<img class="bltitle" src="images/advers.png"><br>
					<span class="bl">全屏大不同</span><br>
					<span class="bls">滚动播出</span>
					<br><span class="bl_price">价格</span><span class="bl_pricenumber">980000</span>
					<div class="bl_bnt">
					   <span class="bl_buynows">立即购买</span>
				        </div>
			</div>
		</div>
			<div class="botright">
				<img class="br_first" src="images/JXH2w.jpg">
				<img class="br_firstr" src="images/time.png">
			<div class="br_text">
		<img class="brtitle" src="images/advers.png"><br>
					<span class="br">全屏大不同</span><br>
					<span class="brs">滚动播出</span>
					<br><span class="br_price">价格</span><span class="br_pricenumber">980000</span>
					<div class="br_bnt">
					   <span class="br_buynows">立即购买</span>
				        </div>
			</div>
		</div>
           <div id="program">
				<div class="pro_title">
					团类节目
				</div>
				<div class="pro_ineng">
					<img src="images/teamment.png">
				</div>
			</div>
			<div class="pro">
			<div class="pro_topletf">
				<img class="pro_tl" src="images/JXH2w.jpg">
				<div class="pro_tltext">
					<img class="pro_title" src="images/foodfas.png"><br>
					<span class="pross">食色性也</span><br>
					<span class="tls">2次/天 3分钟 7天</span><br>
					<span class="pro_price">价格</span>
					<span class="pro_pricenumber">50000</span>
					<div class="pro_bnt">
					   <span class="pro_buynows">立即购买</span>
				        </div>
				</div>
			</div>
		    <div class="pro_topright">
		    	<img class="pro_tr" src="images/JXH2w.jpg">
				<div class="pro_trtext">
					<img class="pro_rtitle" src="images/kid.png"><br>
					<span class="pross">食色性也</span><br>
					<span class="tls">2次/天 3分钟 7天</span><br>
					<span class="pro_price">价格</span>
					<span class="pro_pricenumber">50000</span>
					<div class="pro_bnt">
					   <span class="pro_buynows">立即购买</span>
				        </div>
	       </div>
		</div>
            </div>
            <div class="prodown">
            	<div class="pd_left">
            		<img class="pd_film" src="images/JXH2w.jpg">
            		<div class="pd_text">
            		<img class="pdtitle" src="images/mfilminfor.png"><br>
					<span class="prod">最新资讯在来的路上</span><br>
					<!-- <span class="bls">滚动播出</span>
					<br><span class="bl_price">价格</span><span class="bl_pricenumber">980000</span> -->
					<div class="pd_bnt">
					   <span class="pd_buynows">立即购买</span>
				        </div>
            		</div>
            	</div>
            	<div class="pd_center">
            		<img class="pd_film" src="images/JXH2w.jpg">
            		<div class="pd_text">
            		<img class="pdtitle" src="images/mtrip.png"><br>
					<span class="prod">世界这么大带你去看看</span><br>
					 <span class="bls">2次/天 3分 1个月</span>
					<br><span class="bl_price">价格</span><span class="bl_pricenumber">980000</span> 
					<div class="pd_bnt">
					   <span class="pd_buynows">立即购买</span>
				        </div>
            		</div>
            	</div>
            	<div class="pd_right">
            		<img class="pd_film" src="images/JXH2w.jpg">
            		<div class="pd_text">
            		<img class="pdtitle" src="images/youy.png"><br>
					<span class="prod">拉近你我心距离</span><br>
					<span class="bls">2次/天 3分钟 半年</span>
					<br><span class="bl_price">价格</span><span class="bl_pricenumber">980000</span>
					<div class="pd_bnt">
					   <span class="pd_buynows">立即购买</span>
				        </div>
            		</div>
            	</div>
            </div>
            <div id="info">
				<div class="info_title">
					图片字幕
				</div>
				<div class="pro_ineng">
					<img src="images/infoment.png">
				</div>
			</div>
			<div id="foot">
				<div class="footleft">
					<img class="foot_img" src="images/JXH2w.jpg">
					<div class="footl_text">
						<img class="footrtitle" src="images/infotupian.png"><br>
					<span class="footr">节目下方滚动播出</span><br>
					<span class="bls">2次/天 3分钟 半年</span>
					<br><span class="footr_price">价格</span><span class="footr_pricenumber">980000</span>
					<div class="footr_bnt">
					   <span class="footr_buynows">立即购买</span>
				        </div>
					</div>
				</div>
				<div class="footright">
					<img class="foot_img" src="images/JXH2w.jpg">
					<div class="footr_text">
						<img class="footrtitle" src="images/infozimu.png"><br>
					<span class="footr">节目下方滚动播出</span><br>
					<span class="bls">2次/天 3分钟 半年</span>
					<br><span class="footr_price">价格</span><span class="footr_pricenumber">980000</span>
					<div class="footr_bnt">
					   <span class="footr_buynows">立即购买</span>
				        </div>
					</div>
			</div>
		</div>
	</div>
	</div>			
			<a id="force" name="force"></a>
			<div class="recommand customs pd" id="tohere"  name="tohere" >
		
			
				<div class="title">
					<span>自选产品</span>
				</div>
				<!-- 暂时隐掉
				<div class="title_cart">
					<a href="${rc.contextPath}/product/sift"><span>自定义产品</span></a>
				</div>
				-->

				<div class="back">
					<div class="back-items">
						<!-- 暂时隐掉
						 <div class="back-item">
							<span class="desp">所属地区：</span> <span class="sift-list" qt="c">
								<a class="item active" style="background: #ff9966" href="#"	sort="-1" qc="all">北京</a> 
								<a class="item" href="#" sort="-3" qc="2">天津<i>×</i></a>
								<a class="item" href="#" sort="-3" qc="2">大连<i>×</i></a>
								<a class="item" href="#" sort="-3" qc="2">唐山<i>×</i></a>
								<a class="item" href="#" sort="-3" qc="2">抚顺<i>×</i></a>
							</span>
						</div>
						 -->
						<div class="back-item">
							<span class="desp">产品类型：</span> <span class="sift-list" qt="t">
								<a class="item active" href="#" sort="-1" qc="all">所有</a>
								<a class="item" href="#" qc="video">全屏硬广<i>×</i></a> 
								<a class="item" href="#" qc="image">INFO图片<i>×</i></a> 
								<a class="item" href="#" qc="info">INFO字幕<i>×</i></a>
								
							</span>
						</div>
						<div class="back-item">
							<span class="desp">日展次数：</span> 
							<span class="sift-list" qt="s">
								<a class="item active" href="#" sort="-1" qc="all">所有</a>
								<a class="item" href="#" qc="0-31">0-31（含）<i>×</i></a> 
								<a class="item"	href="#" qc="32-90">32-90（含）<i>×</i></a> 
								<a class="item" href="#" qc="91-3000">91次以上<i>×</i></a>
							</span>
						</div>
						<div class="back-item">
							<span class="desp">展示期限：</span> <span class="sift-list" qt="d">
								<a class="item active" href="#" sort="-1" qc="all">所有</a> 
								<a class="item" href="#" qc="1-7">1-7（含）<i>×</i></a>
								 <a class="item" href="#" qc="8-30">8-30（含）<i>×</i></a> 
								<a class="item" href="#" qc="31-90">31-90（含）<i>×</i></a> 
								<a class="item" href="#" qc="91-2000">91天以上<i>×</i></a>
							</span>
						</div>
					</div>
					<div class="select-more">
						<ul>
							<li class="item2">按价格排序<a href="jvascript:void(0)"><span
									id="priceSort" style="color: #fff">ｖ</span></a></li>
							<li class="item3">价格筛选<input type="text" id="price1"
								value="" onkeyup="value=value.replace(/[^\\d]/g,\'\')"
								onblur="changeByprice('${rc.contextPath}');" class="i-short"><em>—</em><input
								type="text" id="price2"
								onblur="changeByprice('${rc.contextPath}');" class="i-short"></li>
							<li class="item4">&nbsp</li>
						</ul>
					</div>
					<input type="hidden" value="0" id="ascOrDesc" />
					<div class="buy">
						<div id="productList"></div>
						<input type="hidden" id="sh" value="" />
						<div class="paginate">
							<div id="Pagination" class="pagination"></div>
						</div>
						<a href="#force" id="goto"  ></a>

						<div class="cart-box" style="margin-top: 20px;">
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
	       } );  
	        
	    $('.search-btn').click(function(){
	    	initPro('${rc.contextPath}',$("#sh").val(),"","",1);
	    	$('.middle,.title,.title_cart,.back-items').hide();
   			$('.select-more').css('margin-top','30px');
   		} );  
	   		
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
		
	</script>
</body>
</html>