<#import "template/template_buy.ftl" as frame > <#global menu="产品竞价">
<@frame.html title="产品竞价"
js=["js/jquery.jcountdown.js","js/jquery.jcountdown.site.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/compare/auction.css","css/sea.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
    
  
function compare(pathurl){
	var productid=$("#productid").val();
	var myprice=$("#myprice").val();
	
	var lc=$("#lc").val();
if(lc=="0"){
islogin(pathurl);
}

if(myprice==""){
   layer.msg("请出价");
   return;
}
$.ajax({
			url:"${rc.contextPath}/product/comparePrice",
			type:"POST",
			async:false,
			dataType:"json",
			data:{
			"cpdid":productid,
			"myprice":myprice
			},
			success:function(data){
			     if(data.right=="资质未认证"){
			     	layer.msg("抱歉，您未获得竞拍资格<br><a href='${rc.contextPath}/user/UserQulifi'>点击立即获取资格</a>", {icon: 5});
			     	return ;
			     }else{
			       layer.msg(data.right);
			     }
			var uptime = window.setTimeout(function(){
			window.location.reload();
		   	clearTimeout(uptime);
					},1000)
			}
      }); 
}
	
</script>

<style type="text/css">
.hint {
	padding: 10px 10px 30px 10px;
}

.product_detail_mt h3 {
	display: inline-block;
	padding-right: 20px;
	font-family: '\5fae\8f6f\96c5\9ed1';
	font-size: 16px;
	background: #fff;
}

.hint .mc {
	margin-top: 5px;
}

.hint .mc p {
	margin-top: 15px;
}

.container-12 {
	width: 1150px;
	margin-left: auto;
	margin-right: auto;
}

.ls-3{
	margin-left: 0px;
}
</style>

<script type="text/javascript">
        function bu(txtObj) {
            txtObj.value = Number(txtObj.value).toFixed(2);
        }
</script>

<div class="pg-container">
<div class="navbuy side-nav">
			<div class="de-code">
				<img src="${rc.contextPath}/index_img/pic1.png" height="100" width="100">
			</div>
			<ul class="navibar">
				<li><a href="/">首页</a></li>
				<li class="active"><a href="${rc.contextPath}/secondLevelPage">移动视频</a></li>
				<li><a
					href="${rc.contextPath}/secondLevelPageBus">车身媒体</a></li>
				<li><a href="/caseMore.html">案例欣赏</a></li>
			</ul>
			<div class="markble">
				<p>世界在你脚下，巴士一路随行</p>
				<p>北京公交媒体</p>
			</div>
</div>
	<div class="pg-container-main">
		<div class="container-12 mt10 s-clear">
			<div class="ls-9">
				<div class="product-info s-clear">
					<div class="preview s-left"> <img
							src="<#if jpaCpd.product?? && jpaCpd.product.imgurl?has_content>${rc.contextPath}/upload_temp/${jpaCpd.product.imgurl}<#else>upload_temp/30/7/afa19a2cadb2536fad38fbb3af4de4f2.jpg</#if>" width="360" height="298">
					</div>
					<div class="product-detail s-left">
						<div class="product-title">
							<h3>${(jpaCpd.product.name)!''}</h3>
						</div>
						<div>
							<div class="product-intro">
								<div class="price s-clear">
									<span>当前价：</span> <span class="fsize-24 t-red"><em>¥</em>
										#{jpaCpd.comparePrice!'' ;m2M2} </span> <em class="line"></em> 
									<span>原价：</span><del> #{(jpaCpd.product.price)!'' ;m2M2} </del><em class="line"></em>
									<span>围观数：</span><em>${jpaCpd.pv} 次</em>
								</div>
							</div>
							<div class="product-Sdetail">
								<div class="s-detail">
									<span>广告时长：</span> <em>${prod.duration!''}秒/次</em>
								</div>
								<div class="s-detail">
									<span>广告频次：</span> <em>${prod.playNumber!''}次/天</em>
								</div>
								<div class="s-detail">
									<span>播放天数：</span> <em>${prod.days!''}天</em>
								</div>
							</div>
							<div class="product-time">
								<span class="residue" id="residue">距结束：</span>
								<div class="timer" id="auction1Timer">
									<!--<input type="text" id="time_h">时<input type="text" id="time_m">分<input type="text" id="time_s">秒-->
								</div>
							</div>
							
							<div class="product-form">
								<span class="residue" id="residue">出价</span><span id="cspan"><a class="reduce">-</a> <input
									class="text product-text" onblur="bu(this)"
									onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\d.]/g,'')}else{this.value=this.value.replace(/[^\d.]/g,'')}"
									type="text" id="myprice" value="" /> <a class="plus">+</a>
								</span>
							</div>
							
							<div class="product-intro product-form">
								<div class="price s-clear">
									<span>最低加价：<em>￥1000.00</em></span> <span>最高加价：<em>不限</em></span>
								</div>
							</div>
							<div class="product-btn" style="float: right;">
								<#if (jpaCpd.startDate < .now && jpaCpd.biddingDate > .now ) > <a
									class="btn-bid" href="javascript:void(0)"
									onclick="compare('${rc.contextPath}')">我要出价</a>
								<script type="text/javascript">
										      		$(function(){ 
										      			$("#residue").html("剩余时间");
														var dateTo=new Date("${jpaCpd.biddingDate?string("yyyy-MM-dd HH:mm:ss")}".replace(/-/g, "/"));
														countDate("auction1Timer",dateTo);
													});
										      </script>
								<#elseif (jpaCpd.startDate > .now) > <a class="btn-bid"
									style="background: #f5f5f5; color: #333"
									href="javascript:void(0)">等待开始</a>
								<script type="text/javascript">
										      		$(function(){ 
										      			$("#residue").html("距离开拍");
										      			$("#cspan").css("display","none");
										      			var dateTo=new Date("${jpaCpd.startDate?string("yyyy-MM-dd HH:mm:ss")}".replace(/-/g, "/"));
														countDate("auction1Timer",dateTo);
													});
										      </script>
								<#elseif (jpaCpd.biddingDate < .now) > <a class="btn-bid"
									style="background: #f5f5f5; color: #333"
									href="javascript:void(0)">竞价结束</a>

								<script type="text/javascript">
										      		$(function(){ 
										      			$("#residue").html("结束时间");
										      		$("#cspan").css("display","none");
														var dateTo="${jpaCpd.biddingDate?string("yyyy-MM-dd HH:mm:ss")}";
													   $("#auction1Timer").html(dateTo);
													});
										      </script>
								</#if> <input type="hidden" id="productid"
									value="${(jpaCpd.id)!''}" /> <@security.authorize
								access="isAuthenticated()"> <input type="hidden" id="lc"
									value="1" /> </@security.authorize> <@security.authorize
								access="! isAuthenticated()"> <input type="hidden" id="lc"
									value="0" /> </@security.authorize>
							</div>
						</div>
					</div>
				</div>
				<div class="bidPath">
					<div class="lc" style="margin-left: 50px;">
						<span>竞&nbsp;&nbsp;拍</span><br> <span>流&nbsp;&nbsp;程</span>
					</div>
					<div class="flow"
						style="overflow: hidden; width: 650px; padding-left: 20px;">
						<div class="lc-flow" style="width: 650px;">
							<ul>
								<li class="item01 ui-accordion-item"
									style="width: 100px; overflow: hidden;">
									<div class="status">用户账号当前京豆大于0</div> <span class="num">01/</span>
									<span class="mark"></span> <span class="name">获取资格</span>
								</li>
								<li class="item02 ui-accordion-item"
									style="width: 100px; overflow: hidden;">
									<div class="status">保证竞拍结束时出价最高，获得竞拍商品</div> <span class="num">02/</span>
									<span class="mark"></span> <span class="name">拍得商品</span>
								</li>
								<li class="item03 ui-accordion-item"
									style="width: 100px; overflow: hidden;">
									<div class="status">填写订单信息，提交订单</div> <span class="num">03/</span>
									<span class="mark"></span> <span class="name">确认转订单</span>
								</li>
								<li class="item04 ui-accordion-item"
									style="width: 100px; overflow: hidden;">
									<div class="status">支付已提交的订单</div> <span class="num">04/</span>
									<span class="mark"></span> <span class="name">支付订单</span>
								</li>
								<li class="item05 ui-accordion-item"
									style="width: 100px; overflow: hidden;">
									<div class="status">支付成功后等待收货，竞拍完成</div> <span class="num">05/</span>
									<span class="mark"></span> <span class="name">竞拍成功</span>
								</li>
							</ul>
						</div>
					</div>
				</div>

				<div class="hint">
					<div class="product_detail_mt">
						<h3>温馨提示</h3>
					</div>
					<div class="mc">
						<p>1.用户提交资质信息后，经系统审核通过后用户状态为“认证通过”的用户才有资格参与竞拍；</p>
						<p>2.请您仔细阅读竞拍商品信息页面中的具体说明，对商品要求较高者请您慎重购买；</p>
						<p>3.订单提交成功后请在24小时内进行支付（竞拍成功后请在24小时内转订单），否则系统将取消您的竞拍资格；</p>
						<p>4.订单支付后，尽快提交物料及相关资质，如有需要及时联系客服咨询；</p>
						<p>5.所售商品正常提供发票，详情请参见帮助中心的发票制度说明；</p>
					</div>
				</div>



				<div class="product-ins">
					<div class="ins-title">
						<span>商品介绍</span>
					</div>
				</div>
				<div class="product-contain">
					<div class=" color-white-bg fn-clear" style="margin-left: 0px;">
						<DIV class="summary uplan-summary-div">
							<UL class="uplan-detail-ul">
								<LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN
									class="con">${prod.name!''}</SPAN></LI>
								<li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
								<LI style="width: 240px;"><SPAN>
								<#if prod.type == 'body'>媒体费：<#else>套餐原价：</#if>
								</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">#{prod.price!'' ;m2M2}</SPAN><SPAN>元</SPAN></LI>
								<#if prod.type == 'body'>
								<LI style="width: 240px;"><SPAN>制作费：</SPAN><SPAN
									class="con" style="color: rgb(245, 135, 8);">#{prod.produceCost!'' ;m2M2}</SPAN>
									<SPAN>元</SPAN></LI> </#if>
								<LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN
									class="con">${prod.type.typeName!''}</SPAN></LI> <#if prod.type ==
								'body'>
								<LI style="width: 240px;"><SPAN>线路级别：</SPAN><SPAN
									class="con"><#if
										prod.lineLevel??>${prod.lineLevel.nameStr!''}</#if></SPAN></LI>
								<LI style="width: 240px;"><SPAN>巴士数量：</SPAN><SPAN
									class="con">${prod.busNumber!''}</SPAN></LI> </#if> <#if prod.type
								== 'video' || prod.type == 'image' || prod.type == 'info'>
								<LI style="width: 240px;"><SPAN>时长（秒）：</SPAN><SPAN
									class="con">${prod.duration!''}秒</SPAN></LI>
								<li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
								<LI style="width: 240px;"><SPAN>单日播放次数：</SPAN> <SPAN
									class="con">${prod.playNumber!''}</SPAN></LI> </#if> <#if prod.type
								== 'video'>
								<!-- <LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN
									class="con">${prod.firstNumber!''}</SPAN></LI>
								<LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN
									class="con">${prod.lastNumber!''}</SPAN></LI> -->
								<!--	
								<li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
								<LI style="width: 240px;"><SPAN>高峰时段占比：</SPAN><SPAN
									class="con">${prod.hotRatio!''}</SPAN></LI>--> </#if>
								<LI style="width: 200px;"><SPAN><#if prod.type ==
										'video' || prod.type == 'image' || prod.type ==
										'info'>套餐播放天数：<#else>广告展示天数：</#if></SPAN> <SPAN class="con">${prod.days!''}天</SPAN>
								</LI>
								<li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
								<LI style="width: 720px;"><SPAN>套餐描述：</SPAN><SPAN
									class="con"><a class="layer-tips" tip="点击可查详细内容!"
										onclick="showRemark('${prod.remarks!''}');">${substring(prod.remarks!'',0,38)}</a></SPAN>
								</LI>


							</UL>
						</DIV>
					</div>
				</div>
			</div>
			<div class="ls-3"
				style="float: right; position: absolute; right:-15%; top: 0px; background: #fff;">
				<div class="record-sidebar">
					<div class="record-title">
						<label>出价记录（共<label>${jpaCpd.setcount}</label>次出价）
						</label>
					</div>
					<div class="record-detail">
						<dl>
							<dt>
								<span class="wd1">时间</span> <span class="wd2">详情</span> <span
									class="wd3">状态</span>
							</dt>
							<#list userCpdList as item>
							<dd>
								<span class="wd1">${item.created?string("yyyy-MM-dd HH:mm:ss")}</span>
								<span class="wd2"> <i>${hidname(item.userId!'')}</i>
									<div class="line"></div> <i> ￥#{item.comparePrice!'' ;m2M2}
								</i>
								</span> <#if item_index==0 > <span class="wd3"> <i>领先</i>
								</span> <#else> <span class="wd3 out"> <i>出局</i>
								</span> </#if>
							</dd>
							</#list>
						</dl>
						<div class="more">
							<a>查看更多</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

</div>

<script type="text/javascript">
$(function(){ 
	$('.reduce').click(function(){
		var value = $('#myprice').val()-1;
		document.getElementById("myprice").value = value;
		if(value<1){
			document.getElementById("myprice").value = 0;
		}
	});
	$('.plus').click(function(){
		var value = $('#myprice').val();
		
		if(value=="" || value==0){
			val=1;
			document.getElementById("myprice").value = val;
		}else{
			value=parseInt(value);
			value=value+1;
			document.getElementById("myprice").value = value;
		}
		
	});
});
      </script>
</@frame.html>
