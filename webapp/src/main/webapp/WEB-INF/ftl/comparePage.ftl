<#import "template/template_blank.ftl" as frame >
<#global menu="产品竞价">
<@frame.html title="产品竞价" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] 
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/compare/auction.css","css/sea.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">
    $(document).ready(function() {
    });
    
    
function compare(){
var productid=$("#productid").val();
var myprice=$("#myprice").val();
if(myprice==""){
   alert("请出价");
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
			  alert(data.right);
			}
      }); 
}
	
</script>

<div class="pg-container">
				<div class="pg-container-main">
					<div class="container-12 mt10 s-clear">
						<div class="ls-9">
							<div class="product-info s-clear">
								<div class="preview s-left">
									<img src="${rc.contextPath}/imgs/auction.jpg" width="298" height="298">
								</div>
								<div class="product-detail s-left">
									<div class="product-title">
										<h3>${(jpaCpd.product.name)!''}</h3>
									</div>
									<div>
										<div class="product-intro">
											<div class="price s-clear">
												<span>当前价：</span>
												<span class="fsize-24 t-red"><em>¥</em>${jpaCpd.comparePrice!''}</span>
												<em class="line"></em>
												<span>原价：</span>
												<del>¥${(jpaCpd.product.price)!''}</del>
												<div class="s-right">
													<span>围观数：</span>
													<em>${jpaCpd.pv} 次</em>
												</div>
											</div>
										</div>
										<div class="product-time">
											<span class="residue">剩余时间：</span>
											<div class="timer" id="auction1Timer">
												<input type="text" id="time_h">时<input type="text" id="time_m">分<input type="text" id="time_s">秒
											</div>
										</div>
										<div class="product-form">
											<a class="reduce" href="#">-</a>
											<input class="text product-text" type="text" id="myprice" value="" />
											<a class="plus" href="#">+</a>
											<div class="range">
												<span>最低加价：<em>￥1.00</em></span>
												<span>最高加价：<em>￥5000.00</em></span>
						
											</div>
										</div>
										<div class="product-btn">
											<a class="btn-bid" href="javascript:void(0)" onclick="compare()" >我要出价</a>
											<input type="hidden" id="productid" value="${(jpaCpd.id)!''}"/>	
										</div>
									</div>
								</div>
							</div>
							<div class="bidPath">
								<div class="lc">
									<span>竞&nbsp;&nbsp;&nbsp;拍</span><br>
									<span>流&nbsp;&nbsp;&nbsp;程</span>
								</div>
								<div class="flow" style="overflow: hidden; width: 700px;">
									<div class="lc-flow" style="width: 800px;">
										<ul>
											<li class="item01 ui-accordion-item" style="width: 120px; overflow: hidden;">
												<div class="status">用户账号当前京豆大于0</div>
												<span class="num">01/</span>
												<span class="mark"></span>
												<span class="name">获取资格</span>
											</li>
											<li class="item02 ui-accordion-item" style="width: 120px; overflow: hidden;">
												<div class="status">保证竞拍结束时出价最高，获得竞拍商品</div>
												<span class="num">02/</span>
												<span class="mark"></span>
												<span class="name">拍得商品</span>
											</li>
											<li class="item03 ui-accordion-item" style="width: 120px; overflow: hidden;">
												<div class="status">填写订单信息，提交订单</div>
												<span class="num">03/</span>
												<span class="mark"></span>
												<span class="name">确认转订单</span>
											</li>
											<li class="item04 ui-accordion-item" style="width: 120px; overflow: hidden;">
												<div class="status">支付已提交的订单</div>
												<span class="num">04/</span>
												<span class="mark"></span>
												<span class="name">支付订单</span>
											</li>
											<li class="item05 ui-accordion-item" style="width: 120px; overflow: hidden;">
												<div class="status">支付成功后等待收货，竞拍完成</div>
												<span class="num">05/</span>
												<span class="mark"></span>
												<span class="name">竞拍成功</span>
											</li>
										</ul>
									</div>
								</div>
							</div>
							<div class="product-ins">
								<div class="ins-title">
									<span>商品介绍</span>
								</div>
							</div>
							<div class="product-contain">
								<@proDetail.proDetail prod=prod buyLink=false/>
							</div>
						</div>
						<div class="ls-3" style="float:right;position:absolute;left:790px;top:0px;">
							<div class="record-sidebar">
								<div class="record-title">
									<label>出价记录（共<label>${jpaCpd.setcount}</label>次出价）</label>
								</div>
								<div class="record-detail">
									<dl>
										<dt>
											<span class="wd1">时间</span>
											<span class="wd2">详情</span>
											<span class="wd3">状态</span>
										</dt>
										<#list userCpdList as item>
										<dd>
											<span class="wd1">${item.created?string("yyyy-MM-dd HH:mm:ss")}</span>
											<span class="wd2">
												<i>${item.userId!''}</i>
												<div class="line"></div>
												<i>￥${item.comparePrice!''}</i>
											</span>
											<span class="wd3">
												<i>领先</i>
											</span>
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
				show_time();
				$('.reduce').click(function(){
					var value = $('#addPrice').val()-1;
				});
				$('.plus').click(function(){
					var value = $('#addPrice').val()+1;
				});
			});
			function show_time(){ 
				var time_start = new Date().getTime(); //设定当前时间
				var time_end =  new Date("2015/10/01 00:00:00").getTime(); //设定目标时间
				// 计算时间差 
				var time_distance = time_end - time_start; 
				// 天
				var int_day = Math.floor(time_distance/86400000) 
				time_distance -= int_day * 86400000; 
				// 时
				var int_hour = Math.floor(time_distance/3600000) 
				time_distance -= int_hour * 3600000; 
				// 分
				var int_minute = Math.floor(time_distance/60000) 
				time_distance -= int_minute * 60000; 
				// 秒 
				var int_second = Math.floor(time_distance/1000) 
				// 时分秒为单数时、前面加零 
				if(int_day < 10){ 
					int_day = "0" + int_day; 
				} 
				if(int_hour < 10){ 
					int_hour = "0" + int_hour; 
				} 
				if(int_minute < 10){ 
					int_minute = "0" + int_minute; 
				} 
				if(int_second < 10){
					int_second = "0" + int_second; 
				} 
				// 显示时间 
				$("#time_h").val(int_hour); 
				$("#time_m").val(int_minute); 
				$("#time_s").val(int_second); 
				// 设置定时器
				setTimeout("show_time()",1000); 
			}
      </script>
</@frame.html>
