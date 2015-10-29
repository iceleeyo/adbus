<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>确认订单信息</title>
		 
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
				<div class="cart-container cart-step2">
					<div class="c-inner">
						<div class="tabline">
							<div class="arrow arrow-stp1"><span class="ara">1</span><a href="${rc.contextPath}/toCard"><span>查看购物车</span></a></div>
							<div class="arrow arrow-stp2"><span class="ara">2</span><span>确认订单信息</span></div>
							<div class="arrow arrow-stp3"><span class="ara">3</span><span>成功提交订单</span></div>
						</div>
					  <#if infos.media?? && (infos.media?size>0)>
						<div class="panel">
							<div class="panel-head">
								<ul class="clearfix">
									<li class="td td-name">视频产品</li>
									<li class="td td-price">单价</li>
									<li class="td td-amount">数量</li>
									<li class="td td-sum">合计</li>
								</ul>
							</div>
							<#list infos.media as item>
							<div class="panel-item">
								<ul class="item-info clearfix">
									<li class="td td-item">
										<div class="td-inner">
											<p>${substring(item.product.name, 0, 7)}</p>
										</div>
									</li>
									<li class="td td-info">
										<div class="td-inner">
											<span >  时长：${item.product.duration}秒/次</span> 
											<span class="perio">频次：${item.product.playNumber}次/天</span>
											<span class="perio">刊期：${item.product.days}天</span>
										</div>
									</li>
									<li class="td td-price">
										<div class="td-inner">
											<p class="price"><em>￥</em>#{item.price!'' ;m2M2}</p>
										</div>
									</li>
									<li class="td td-amount">
										<div class="td-inner">
											<p class="amount">${item.needCount}</p>
										</div>
									</li>
									<li class="td td-sum">
										<div class="td-inner">
											<p class="sum"><em>￥</em>#{(item.price*item.needCount)!'' ;m2M2}</p>
										</div>
									</li>
								</ul>
							</div>
                           </#list>
						</div>
</#if>
					  <#if infos.body?? && (infos.body?size>0)>
						<div class="panel">
							<div class="panel-head">
								<ul class="clearfix">
									<li class="td td-name">车身产品</li>
									<li class="td td-price">单价(元/月/辆)</li>
									<li class="td td-amount">车辆数</li>
									<li class="td td-sum">合计</li>
								</ul>
							</div>
							<#list infos.body as item>
							<div class="panel-item">
								<ul class="item-info clearfix">
									<li class="td td-item">
										<div class="td-inner">
										<p>${substring(item.product.jpaProductV2.name, 0, 7)}</p>
										</div>
									</li>
									<li class="td td-info">
										<div class="td-inner">
											<p class="rec-line">线路级别：${item.product.leval.nameStr}</p>
											<p class="rec-line">刊期：${item.days/30}个月</p>
										</div>
									</li>
									<li class="td td-price">
										<div class="td-inner">
											<p class="price"><em>￥</em>#{item.price!'' ;m2M2}</p>
										</div>
									</li>
									<li class="td td-amount">
										<div class="td-inner">
											<p class="amount">${item.needCount}</p>
										</div>
									</li>
									<li class="td td-sum">
										<div class="td-inner">
											<p class="sum"><em>￥</em>#{item.totalprice!'' ;m2M2}</p>
										</div>
									</li>
								</ul>
							</div>
                           </#list>
						</div>
</#if>
						<div class="adj">
							
							<div class="stage line">
								<span class="legged">分期付款</span>
								<select id="dividpay" class="selects">
								
								  <option value="3">3</option>
								  <option value="6">6</option>
								  <option value="12">12</option>
								  <option value="24">24</option>
								  <option value="36">36</option>
								</select>
							</div>
							<div class="way" id="payway">
								<span class="legged">支付方式</span>
								<div class="select-items legged">
									<ul class="iradios">
										<li class="active">
											<input type="radio" name="payType" checked="checked" value="online">
											<label class="iradio"></label>
											<span>网上支付</span>
										</li>
										<li>
											<input type="radio"name="payType" value="check">
											<label class="iradio"></label>
											<span>支票</span>
										</li>
										<li>
											<input type="radio"name="payType" value="remit">
											<label class="iradio"></label>
											<span>汇款</span>
										</li>
										<li >
											<input type="radio"name="payType" value="cash">
											<label class="iradio"></label>
											<span>现金</span>
										</li>
									</ul>
								</div>
							</div>
							<div class="sum">
								<span class="legged">总价:<em>￥#{infos.totalPrice!'' ;m2M2}</em></span>
							</div>
							<div class="sure">
							<a href="javascript:void(0);" onclick="payment()">
								<div class="btn-sure">确认支付</div>
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
				  window.location.href="${rc.contextPath}/carbox/paySuccess";
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