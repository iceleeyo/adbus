<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>secondLevel</title>
		<link rel="stylesheet" type="text/css" href="index_css/base.css">
		<link rel="stylesheet" type="text/css" href="index_css/header.css">
		<link rel="stylesheet" type="text/css" href="index_css/jack.css">
		<link rel="stylesheet" type="text/css" href="index_css/secondLevel.css">
		<link rel="stylesheet" type="text/css" href="index_css/sift.css">
		<link rel="stylesheet" type="text/css" href="index_css/sea.css">
		<link rel="stylesheet" href="index_css/pagination.css">
			
	</head>
<script type="text/javascript">
    function logout(){
       window.location.href = "${rc.contextPath}/logout";
    }

    $(function() {
        $("#city_dropdown a:not(.selected)").click(function(){
            var cityName = $(this).parents("#ttbar-mycity")[0]?$(this).attr("data-id") : $("#ttbar-mycity a.selected").attr("data-id");
            var media = $(this).parents("#ttbar-media")[0]?$(this).attr("data-id") : $("#ttbar-media a.selected").attr("data-id");
            if (!cityName)
                cityName = '北京';
            if (!media)
                media = 'screen';
            $.ajax({
                url : "${rc.contextPath}/f/city/select?c=" + cityName + "&m="+media,
                type : "POST",
                data: {},
                success : function(data) {
                    layer.msg("正在切换到："+ data.name + " " + data.mediaTypeName);
                    var uptime = window.setTimeout(function(){
                        window.location.reload();
                        clearTimeout(uptime);
                    },1000);
                }
            }, "text");
        });
    });
</script>
<style type="text/css">
    .sift-list {  width: 80px;  line-height: 30px;}
    .sift-list a{margin-right: 10px;}

</style>		
	<body>
		<header>
		<div class="grid-12 city-dropdown">
                            <ul class="fl">
<#--<@security.authorize access="isAuthenticated()">-->
                                <li class="dorpdown" id="ttbar-mycity">
                                    <div class="dt cw-icon ui-areamini-text-wrap" style="">
                                        <i class="ci-right"><s>◇</s></i>
                                        <#if city??>
                                            <span class="ui-areamini-text" data-id="${city.name}" title="${city.name}">${city.name}</span>
                                        <#else>
                                            <span class="ui-areamini-text" data-id="${cities[0].name!''}" title="${cities[0].name!''}">${cities[0].name!''}</span>
                                        </#if>
                                    </div>
                                    <div class="dd dorpdown-layer">
                                        <div class="dd-spacer"></div>
                                        <div class="ui-areamini-content-wrap" style="left: auto;">
                                            <div class="ui-areamini-content">
                                                <div class="ui-areamini-content-list" id="city_dropdown">
                                                    <#list cities as c>
                                                        <div class="item">
                                                            <a data-id="${c.name}" href="javascript:void(0)" <#if city?? && city.name == c.name>class="selected"</#if>>${c.name!''}</a>
                                                        </div>
                                                    </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                               
<#--</@security.authorize>-->
                            </ul>
                            </div>
				<nav class="menu">
					<ul class="list-line">
						<li><a href="index.html">首页</a></li>
						<li class="active"><a href="jvascript:void(0)">媒体产品</a></li>
						<li><a href="jvascript:void(0)">传播效果</a></li>
						<li><a href="/caseMore.html">案例欣赏</a></li>
						<li><a href="jvascript:void(0)">合作伙伴</a></li>
						<li><a href="jvascript:void(0)">关于我们</a></li>
					</ul>
					<div class="s-right s-clear">
								<span class="pg-nav-item s-left" style="padding:0;">您好，</span>
									<span>
                                        <@security.authorize access="isAuthenticated()">
                                        <#if medetype?? && medetype=="screen">
                                        <a class="pg-nav-item s-left" href="${rc.contextPath}/order/myTask/1">
                                        <#else>
                                           <a class="pg-nav-item s-left" href="${rc.contextPath}/busselect/myTask/1">
                                        </#if>
                                                                                                                                   我的账户:
                                            <@security.authentication property="principal.user.firstName" />
                                            <@security.authentication property="principal.user.lastName" />
                                        </a>
                                        </@security.authorize>
                                        <@security.authorize access="! isAuthenticated()">
                                            <a class="pg-nav-item s-left" href="${rc.contextPath}/login">请登录</a>
                                            <a class="pg-nav-item s-left" href="${rc.contextPath}/register">免费注册</a>
                                        </@security.authorize>
                                    </span>
									<#--<span class="arrow-down"></span>-->
								<div class="pg-nav-dropdown" style="display: none;">
									<div class="pg-dropdown-box">
										<div class="dropdown-account s-clear">
											<div class="account-img-box s-left">
												<a href="">
													<img src="${rc.contextPath}/imgs/default-img-78.png">
												</a>
											</div>
											<div class="s-left">
												<div class="user-money-handle s-clear grgray-text">
													<span class="balance fsize-14 s-left mr10">账户余额</span>
													<span class="orange-text fsize-14 s-left">
														<em class="fsize-18">0.00</em>
													</span>
												</div>
												<div>
													<a class="s-left pg-btn pg-btn-green pg-btn-md mr4" href="#">充值</a>
													<a class="s-left pg-btn pg-btn-blue pg-btn-md" href="#">提现</a>
												</div>
											</div>
										</div>
										<div class="dropdown-bottom s-clear">
											<div class="dropdown-set s-left"><a class="is-line" href="">我的报表</a></div>
											<div class="dropdown-set s-left"><a class="is-line" href="">我的物料</a></div>
											<div class="dropdown-set s-left"><a href="">我的订单</a></div>
										</div>
									</div>
								</div>
                               
								<!--<a class="pg-nav-item s-left" href="#">
									<i class="icon-msg fsize-12">1</i>
									消息
								</a> -->
								<!--<a class="pg-nav-item s-left" href="#">帮助</a>
								<a class="pg-nav-item s-left" href="#">论坛</a>-->
								<a class="pg-nav-item s-left" href="${rc.contextPath}/message/all">消息<span id="msgNumber" class="layer-tips" style="color:#ff9966"></span></a>
								 <@security.authorize access="isAuthenticated()">
								<a href="javascript:;" class="pg-nav-item s-left" onclick="logout();">[退出]</a>
                                </@security.authorize>
							</div>
				</nav>
			</header>
		<div class="content">
			<div class="side-nav">
				<div class="logo"></div>
				<div class="de-code">
					<img src="index_img/pic1.png" height="100" width="100">
				</div>
				<ul class="navibar">
					<li><a href="index.html">首页</a></li>
					<li class="active"><a>产品媒体</a></li>
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
				<div class="ad">

					<div class="banner" id="b04">
					    <ul>
					        <li><img src="index_img/wp1_1.jpg" alt="" width="100%" height="260" ></li>
					        <li><img src="index_img/wp1_4.jpg" alt="" width="100%" height="260" ></li>
					        <li><img src="index_img/wp1_3.jpg" alt="" width="100%" height="260" ></li>
					        <li><img src="index_img/wp1_2.jpg" alt="" width="100%" height="260" ></li>
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
									<p>价格：50000</p>
									<p>周期：7天</p>
								</div>
								<div class="select-item">
									<img src="index_img/internet.png">
									<p>食品</p>
									<p>价格：50000</p>
									<p>周期：7天</p>
								</div>
								<div class="select-item">
									<img src="index_img/travel.png">
									<p>食品</p>
									<p>价格：50000</p>
									<p>周期：7天</p>
								</div>
								<div class="select-item">
									<img src="index_img/drink.png">
									<p>食品</p>
									<p>价格：50000</p>
									<p>周期：7天</p>
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
							</ul>
							<div class="select-items clearfix">
								<div class="select-item">
									<div class="lasttime">
										<p>剩余时间：</p>
										<p>24天24小时24分钟24秒</p>
									</div>
									<img src="index_img/wp1_1.jpg" height="100" width="100">
									<div class="cost-box">
										<div class="cost">
											价格:<em>50000</em>
										</div>
										<div class="timeline">
											期限: 7天
										</div>
										<div class="ston">
											我要出价
										</div>
									</div>
								</div>
								<div class="select-item">
									<div class="lasttime">
										<p>剩余时间：</p>
										<p>24天24小时24分钟24秒</p>
									</div>
									<img src="index_img/wp1_1.jpg" height="100" width="100">
									<div class="cost-box">
										<div class="cost">
											价格:<em>50000</em>
										</div>
										<div class="timeline">
											期限: 7天
										</div>
										<div class="ston">
											我要出价
										</div>
									</div>
								</div>
								<div class="select-item">
									<div class="lasttime">
										<p>剩余时间：</p>
										<p>24天24小时24分钟24秒</p>
									</div>
									<img src="index_img/wp1_1.jpg" height="100" width="100">
									<div class="cost-box">
										<div class="cost">
											价格:<em>50000</em>
										</div>
										<div class="timeline">
											期限: 7天
										</div>
										<div class="ston">
											我要出价
										</div>
									</div>
								</div>
								<div class="select-item">
									<div class="lasttime">
										<p>剩余时间：</p>
										<p>24天24小时24分钟24秒</p>
									</div>
									<img src="index_img/wp1_1.jpg" height="100" width="100">
									<div class="cost-box">
										<div class="cost">
											价格:<em>50000</em>
										</div>
										<div class="timeline">
											期限: 7天
										</div>
										<div class="ston">
											我要出价
										</div>
									</div>
								</div>
							</div>
					</div>
				</div>
				<div class="recommand customs pd">
					<div class="title"><span>定制自选</span></div>
					<div class="back">
						<div class="back-items">
						
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
							<a href="${rc.contextPath}/toCard">
								<div class="cart">
									加入购物车
								</div>
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

		<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
		<script src="index_js/unslider.min.js"></script>
		<script src="index_js/sift_bus.js"></script>
		<script src="js/jquery.pagination.js"></script>
		<script type="text/javascript">
	    $(document).ready(function() {
	        initPro('${rc.contextPath}',$("#sh").val(),"","",1);
	        initSwift2('${rc.contextPath}');
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
		
		/* $("#leftPlus").click(function(){
			  $(this).parent().find("input").val(parseint($(this).parent().find("input"))+1);
		});
		$("#leftDec").click(function(){
			  $(this).parent().find("input").val(parseint($(this).parent().find("input"))-1);
		}); */
			
		//走马灯图片事件
		$(document).ready(function(e) {
		    var unslider04 = $('#b04').unslider({
				dots: true
			}),
			data04 = unslider04.data('unslider');
			
			$('.unslider-arrow04').click(function() {
		        var fn = this.className.split(' ')[1];
		        data04[fn]();
		    });

			//限时套装Hover事件
			$('.timer .select-item').hover(function(e) {
				/* Stuff to do when the mouse enters the element */
				e.preventDefault();
				$(this).find('.lasttime').css({
					'border-top': '1px solid #ca0d0e',
					'border-right': '1px solid #ca0d0e',
					'border-left': '1px solid #ca0d0e'
				}).end().find('.cost-box').css({
					'border-right': '1px solid #ca0d0e',
					'border-bottom': '1px solid #ca0d0e',
					'border-left': '1px solid #ca0d0e'
				}).end().find('.cost').css(
					'border-bottom', '1px solid #ca0d0e'
				).end().find('.timeline').css(
					'border-bottom', '1px solid #ca0d0e'
				).end().find('.ston').css({
					color: '#fff',
					background: '#ca0d0e'
				});
			}, function(e) {
				/* Stuff to do when the mouse leaves the element */
				e.preventDefault();
				$(this).find('.lasttime').removeAttr('style').end().find('.cost-box').removeAttr('style').end().find('.cost').removeAttr('style').end().find('.timeline').removeAttr('style').end().find('.ston').removeAttr('style');
			});
		});
		
	</script>
	</body>
</html>