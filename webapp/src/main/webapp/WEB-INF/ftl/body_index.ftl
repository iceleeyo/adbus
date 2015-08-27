<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <link rel="shortcut icon" href="./images/favicon.ico">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
  <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/sea.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/homepage.css">
  <link rel="stylesheet" type="text/css" href="homepage/css/index.css">
  <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/one.css">
  <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/foot.css">
  <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/city.css">
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-1.8.3.min.js"></script>
   <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.jcountdown.js"></script>
   <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.jcountdown.site.js"></script>
   <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer/layer.js"></script>
   	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
	<style type="text/css">
	.withdraw-wrap.color-white-bg.fn-clear{
		/* width: 100% !important; */
	}
	.module1{
		padding: 10px 10px 20px 0px;
	}
	#table_wrapper{
		 width: 1060px !important;
		 margin-left: -30px;
	}
	
	</style>
	<title>首页</title>
<script type="text/javascript">
    function logout(){
       window.location.href = "${rc.contextPath}/logout";
    }
function showSite(tourl){
	layer.open({
		type: 1,
		title: "线路站点",
		skin: 'layui-layer-rim', 
		area: ['650px', '660px'], 
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" src="'+tourl+'"/>'
	});
	}
    $(function() {
        $("#city_dropdown a:not(.selected)").click(function(){
            var cityName = $(this).parents("#ttbar-mycity")[0]?$(this).attr("data-id") : $("#ttbar-mycity a.selected").attr("data-id");
            var media = $(this).parents("#ttbar-media")[0]?$(this).attr("data-id") : $("#ttbar-media a.selected").attr("data-id");
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
 

</head>

<body>
	<div class="pg-header">
		<div class="pg-header-top" style="background:#5E637E;margin-top: 14px;"> 
					<div class="container-12 s-clear">
						<div class="grid-12 city-dropdown">
                            <ul class="fl" style="margin-top: -14px">
                                <li class="dorpdown" id="ttbar-mycity" style="margin-left: -40px;">
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
                                                            <a  data-id="${c.name}" href="javascript:void(0)" <#if city?? && city.name == c.name>class="selected"</#if>>${c.name!''}</a>
                                                        </div>
                                                    </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="dorpdown" id="ttbar-media">
                                    <div class="dt cw-icon ui-areamini-text-wrap" style="">
                                        <i class="ci-right"><s>◇</s></i>
                                    <#if city??>
                                        <a class="selected ui-areamini-text"  data-id="${city.mediaType}" title="${city.mediaType.typeName}">车身广告</a>
                                    <#else>
                                        <a class="selected ui-areamini-text"  data-id="${cities[0].mediaType!''}" title="${cities[0].mediaType.typeName!''}">车身广告</a>
                                    </#if>
                                    </div>
                            </ul>
							<div class="s-right s-clear">
								<span class="pg-nav-item s-left" style="padding:0;">您好，</span>
									<span>
                                        <@security.authorize access="isAuthenticated()">
                                        <a class="pg-nav-item s-left" href="${rc.contextPath}/order/myTask/1">
                                        	我的账户:
                                            (<@security.authentication property="principal.user.firstName" />
                                            <@security.authentication property="principal.user.lastName" />)
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
								<a class="pg-nav-item s-left" href="${rc.contextPath}/message/all">消息<span id="msgNumber" class="layer-tips" style="color:#ff9966"></span></a>
								 <@security.authorize access="isAuthenticated()">
								<a href="javascript:;" class="pg-nav-item s-left" onclick="logout();">[退出]</a>
                                </@security.authorize>
							</div>
						</div>
					</div>
				</div>
 
		<div class="pg-header-main">
					<div class="container-12 s-clear">
						<div class="phmain-logo-b pg-left">
							<a class="phmain-logo" href="#"></a>
						</div>
						<div class="phmain-slogan-b pg-left ml20">
							<a class="phmain-slogan" href="${rc.contextPath}"></a>
						</div>
						<div class="phmain-nav-b pg-right">
							<ul class="pg-nav">
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="${rc.contextPath}/body">首页</a>
								</li>
								
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="${rc.contextPath}/">移动电视</a>
								</li>
								
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="${rc.contextPath}/body">车身媒体</a>
								</li>
								
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t pg-nav-hover-us" href="${rc.contextPath}/product/sift">
										<span>产品促销</span>
										<#--<span class="shdown"></span>-->
									</a>
									
								</li>
                                
                                <li class="pg-nav-item s-left">
                                    <a class="pg-nav-item-t" href="${rc.contextPath}/about-me">关于我们</a>
                                </li>

							</ul>
						</div>
					</div>
		</div>
	</div>
	<div class="container-12 maincontent">	
		<div class="broadcast s-clear">
			<div class="vediobox items s-left">

				<div class="banner" id="b04">
				    <ul>
				        <li><img src="homepage/imgs/a01.png" alt="" width="1070" height="300" ></li>
				        <li><img src="homepage/imgs/a05.jpg" alt="" width="1070" height="300" ></li>
				        <li><img src="homepage/imgs/a04.jpg" alt="" width="1070" height="300" ></li>
				    </ul>
				    <a href="javascript:void(0);" class="unslider-arrow04 prev"><img class="arrow" id="al" src="homepage/imgs/arrowl.png" alt="prev" width="20" height="35"></a>
				    <a href="javascript:void(0);" class="unslider-arrow04 next"><img class="arrow" id="ar" src="homepage/imgs/arrowr.png" alt="next" width="20" height="37"></a>
				</div>

			</div>
			<div class="addition items s-left">
			
			</div>
		</div>
		<div class="container">
			<#if (buslineList?size>0) >
			<div class="module1">
			  <div class="title s-clear">
			  	<span>
			  		线路推荐
			  	</span>
			  </div>
			  <div class="fn-clear pg-uplan-product-list text-middle mt15 mb10">
			    <#list buslineList as item>
				  <div class="fn-left pg-uplan-product-item mr15">
							<dl class="bg-color-white">
							  <dt class="ub" style="height: 310px;">
				<div>
		            	<span class="mr20" style="font-size: 16px;">线路名                        
		            		<a href="javascript:;" onclick="showSite('${rc.contextPath}/api/public_lineMap?lineName=${item.line.name}');">

		            		${item.line.name!''}
		            		</a>
		            	</span>
		        </div>  
		        <div style="  margin: -15px 2px 0px 0px;">
		            	<span class="mr20" style="font-size: 12px;">线路级别:         
							 ${item.line.levelStr!''}
						</span>
		        </div>  
		        <div style="  margin: -15px 2px 0px 0px;">
		            	<span class="mr20" style="font-size: 12px;">车辆数:       
							${item.line._cars!''}
						</span>
		        </div>  
		        <div style="  margin: -15px 2px 0px 0px;">
		            	<span class="mr20" style="font-size: 12px;">人车流量:         
							${item.line._persons!''}
						</span>
		        </div>  
							  <div class="img_box" style="width: 98%;height: 170px;">
							  	<img class="img_size" src="${rc.contextPath}${item.impSite}" style="  width: 100%; height: 90%;">
							  </div>
							  </dt>
		  				</dl>
						
					</div>           
					 </#list>	      
				</div>
			</div>
			</#if>
			<div class="module1">
		<#include "index_mapLines.ftl" />
			</div>
		</div>
		<div class="qqbox">
			<dl>
				<dt>客服中心</dt>
				<dd>客服a:<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3271689631&site=qq&menu=yes"><img border="0" width="70" src="http://wpa.qq.com/pa?p=2:3271689631:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a></dd>
				<dd>客服b:<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2799461911&site=qq&menu=yes"><img border="0" width="70" src="http://wpa.qq.com/pa?p=2:2799461911:41" alt="点击这里给我发消息" title="点击这里给我发消息"/></a></dd>
				<dd>销售a:<a class="qq-default"></a></dd>
				<dd>销售b:<a class="qq-default"></a></dd>
				
			</dl>
		</div>
	</div>
	<script src="homepage/js/index.js"></script>
	<script src="homepage/js/unslider.min.js"></script>
	<script type="text/javascript">
		$(".module1 .fn-left.pg-uplan-product-item").mouseenter(function(){
			$(this).css("border", "1px solid rgb(64, 155, 234)");
			$(this).children(".J_click_p").css("background-color","rgb(86, 170, 242)");
			//$(this).find("a").css("color","rgb(255, 255, 255)");
			console.log("success");
		}).mouseleave(function(){
			$(this).removeAttr("style");
			$(this).children(".J_click_p").removeAttr("style");
			$(this).find("a").removeAttr("style");
		});
		$(".module2 .fn-left.pg-uplan-product-item").mouseenter(function(){
			$(this).css("border", "1px solid rgb(239, 122, 48)");
			$(this).children(".J_click_p").css("background-color","rgb(253, 110, 19)");
			$(this).find("a").css("color","rgb(255, 255, 255)");
			console.log("success");
		}).mouseleave(function(){
			$(this).removeAttr("style");
			$(this).children(".J_click_p").removeAttr("style");
			$(this).find("a").removeAttr("style");
		});
		$(".module3 .fn-left.pg-uplan-product-item").mouseenter(function(){
			$(this).css("border", "1px solid rgb(240, 194, 50)");
			$(this).children(".J_click_p").css("background-color","rgb(255, 205, 51)");
			$(this).find("a").css("color","rgb(255, 255, 255)");
			console.log("success");
		}).mouseleave(function(){
			$(this).removeAttr("style");
			$(this).children(".J_click_p").removeAttr("style");
			$(this).find("a").removeAttr("style");
		});
		//鼠标移入移出事件
/*			   $(".select-city").mouseover(function(){
				    $(this).next().show();
				  });
			   $(".select-city").mouseout(function(){
				    $(".item-dropdown").next().hide();
				  });*/
			   $(".item-dropdown").mouseover(function(){
			    $(this).show();
			   });
			   $(".item-dropdown").mouseout(function(){
			    $(this).hide();
			   });

		//鼠标移入移出事件
			   $(".product-type .item span").mouseover(function(){
				    $(this).next().show();
				  });
			   $(".product-type .item span").mouseout(function(){
				    $(".select-dropdown").next().hide();
				  });
			   $(".select-dropdown").mouseover(function(){
			    $(this).show();
			   });
			   $(".select-dropdown").mouseout(function(){
			    $(this).hide();
			   });

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
		});
  function logout(){
       window.location.href = "${rc.contextPath}/logout";
    }

	</script>

</body>
<@security.authorize access="isAuthenticated()">
<script type="text/javascript">		
			$(document).ready(
					function() {
					   $.ajax({
							url : "${rc.contextPath}/message/unread",
							type : "GET",
							success : function(data) {
								var msgNumber = Number(data);
								if(msgNumber > 0){
									$("#msgNumber").html("["+data+"]");
									$("#msgNumber").attr("tip","您有["+data+"]个未读消息!"); 
									  bindLayerMouseOver();
								}	
							}
						}, "text");
					}
		);
</script>
</@security.authorize>
</html>
