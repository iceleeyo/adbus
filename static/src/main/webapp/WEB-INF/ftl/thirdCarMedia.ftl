<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>产品详情</title>

</head>
<body>
	<header> <!-- 头部开始 -->
	 <#include "/index_menu/index_top.ftl"/> 
	<!-- 头部结束 --> </header>
	<div class="content">
		<div class="side-nav">

			<div class="de-code">
				<img src="${rc.contextPath}/index_img/pic1.png" height="100"
					width="100">
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
			<!--  <div class="c-top">
				<div class="c-search">
					<div class="search-panel">
						<input type="text" value="">
					</div>
					<div class="search-handle">
						<button class="search-btn" type="submit">搜索</button>
					</div>
					<div class="search-key">
						<span>车身广告</span> <span>车身广告</span> <span>车身广告</span> <span>车身广告</span>
						<span>车身广告</span>
					</div>
				</div>
			</div> -->
			<div class="md-nav">媒体产品>移动视频</div>

			<div class="third-step">
				<div class="detail">
					<div class="detail-bd clearfix">
						<div class="d-left">
							<#if jsonView?has_content > <img id="big_img"
								src="<#if jsonView?? && jsonView.img1_url?has_content>${rc.contextPath}/upload_temp/${jsonView.img1_url}</#if>"
								width="345" height="290">
							<div class="sm-imgs">
								<#if jsonView?? && jsonView.img1_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img1_url}"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img2_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img2_url}"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img3_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img3_url}"
									height="65" width="65"></a></#if>
								<#if jsonView?? && jsonView.img4_url?has_content> <a><img
									src="${rc.contextPath}/upload_temp/${jsonView.img4_url}"
									height="65" width="65"></a></#if>
							</div>
							<#else> <img id="big_img"
								src="${rc.contextPath}/index_img/exp1.jpg" width="345"
								height="290">
							<div class="sm-imgs">
								<a><img src="${rc.contextPath}/index_img/exp2.jpg"
									height="65" width="65"></a> <a><img
									src="${rc.contextPath}/index_img/exp3.jpg" height="65"
									width="65"> </a> <a><img
									src="${rc.contextPath}/index_img/exp1.jpg" height="65"
									width="65"></a>
							</div>
							</#if>
							<div class="clearfix socials">
								
							</div>
						</div>
						<div class="d-right">
							<dl class="d-item">
								<dt>名称：</dt>
								<dd class="pro-til">${jpaProduct.name}</dd>
							</dl>
							<dl class="d-item">
								<dt>类型：</dt>
								<dd>${jpaProduct.type.typeName}</dd>
							</dl>
							<dl class="d-item">
								<dt>次数：</dt>
								<dd>${jpaProduct.duration}秒/次
								<#if jpaProduct.type=='inchof32'>
								&nbsp;12次/小时</dd>
								<#else>
									&nbsp;${jpaProduct.playNumber}次/天</dd>
									</#if>
							</dl>
							<dl class="d-item">
								<dt>刊期：</dt>
								<dd>${jpaProduct.days}天</dd>
							</dl>
							<dl class="d-item ">
								<dt>
									价格：<input type="hidden" id="uprice" value="${jpaProduct.price}" />
								</dt>
								<dd id="price">￥${jpaProduct.price}</dd>
							</dl>
							<input type="hidden" id="sh"/>
							<#if jpaProduct.type=='inchof32'>
								<dl class="d-item number">
								<dt>线路：</dt><br>
								<dd>
				<div class="back">
					<div class="back-items">
					   <div class="back-item">
							<span class="sift-list" qt="lines">
								<a class="item active "  style="display:none" href="#" sort="-1" qc="all">所有</a> 
							<#list lines as item>
							
				             	<a class="item layer-tips" href="#" tip="${item.num}块屏幕,总价${jpaProduct.price*item.num}元" qc="${item.id}">${item.name}<i>×</i></a> 
					                <#if item_index!=0 && item_index%6 == 0>
						            <br>
					             </#if>
					          </#list>
							</span>
						</div>
					
			     </div>
			</div>
								</dd>
							</dl>
								<#else>
							<dl class="d-item number">
								<dt>数量：</dt>
								<dd>
									<span class="stock"> <input type="text" id="needCount"
										onblur="changeMonyByNeedCounts(this.value);" value="1">
										<a class="stock-plus" href="javascript:void(0)"
										onclick="b_leftPlus();">+</a> <a class="stock-dl"
										href="javascript:void(0)" onclick="b_leftDec();">-</a>
									</span>
								</dd>
							</dl>
							</#if>
							<dl class="d-item random">
								<dd>
									上播日期： <input
										class="ui-input datepicker validate[required,custom[date] "
										type="text" id="startdate1" data-is="isAmount isEnough"
										autocomplete="off">
								</dd>
							</dl>

							<div class="btn-group">
								<div>
									<a href="javascript:void(0);"
										onclick="buy('${rc.contextPath}',${jpaProduct.id})"
										class="btn btn-now">立即购买 </a> <a href="javascript:void(0);"
										onclick="putIncar('${rc.contextPath}',${jpaProduct.id})"
										class="btn btn-cart">加入购物车 </a> <@security.authorize
									access="isAuthenticated()"> <input type="hidden" id="lc"
										value="1" /> </@security.authorize> <@security.authorize
									access="! isAuthenticated()"> <input type="hidden" id="lc"
										value="0" /> </@security.authorize>
								</div>
							</div>
						</div>
					</div>

					<div class="l-lau"></div>

					<div class="layout">
						<div class="layout-top">
							<div class="product-info">
								产品介绍 <span></span>
							</div>
						</div>
						<div class="layout-contain">
							<div class="c-head">
								<p class="head-en">MEDIA PROFILE</p>
								<p class="head-zh">媒体情况</p>
								<p class="icon-line"></p>
							</div>
							<div class="pic-table c-head clearfix">
								<div class="tbhead">硬广套装</div>
								<div class="tbbody">
									<#if jsonView??>
									<div class="tbtr">
										<div class="tbtd tb-position tb-left">
											<span class="icon icon-position"></span> <span>${jsonView.locationstr!''}</span>
										</div>
										<div class="tbtd tb-flow">
											<span class="icon icon-flow"></span><span>${jsonView.personnum!''}万人次/日</span>
										</div>
									</div>
									<div class="tbtr">
										<div class="tbtd tb-path tb-left">
											<span class="icon icon-path"></span><span>${jsonView.linesurvey!''}
											</span>
										</div>
										<div class="tbtd tb-medium">
											<span class="icon icon-medium"></span><span>${jsonView.mesurvey!''}</span>
										</div>
									</div>
									<div class="tbtr">
										<div class="tbtd tb-firm tb-left">
											<span class="icon icon-firm"></span><span>${jsonView.mediaowner!''}</span>
										</div>
										<div class="tbtd tb-number">
											<span class="icon icon-number"></span><span>${jsonView.mediacount!''}</span>
										</div>
									</div>
									</#if>
								</div>
							</div>
							<div class="c-head">
								<p class="head-en">MEDIA ADVANTAGE</p>
								<p class="head-zh">媒体优势</p>
								<p class="icon-line"></p>
							</div>
							<img
								src="<#if jsonView?? &&  jsonView.intro1_url?has_content>${rc.contextPath}/upload_temp/${jsonView.intro1_url}<#else>${rc.contextPath}/index_img/thirdVideo_2.png</#if>">
							<div class="c-head">
								<p class="head-en">AUDIENCE ANALYSIS</p>
								<p class="head-zh">受众分析</p>
								<p class="icon-line"></p>
							</div>
							<img
								src="<#if jsonView?? && jsonView.intro2_url?has_content>${rc.contextPath}/upload_temp/${jsonView.intro2_url}<#else>${rc.contextPath}/index_img/thirdVideo_3.png</#if>">
							<div class="c-head">
								<p class="head-en">MEDIA REGIONAL</p>
								<p class="head-zh">合作品牌</p>
								<p class="icon-line"></p>
							</div>
							<img
								src="<#if jsonView?? && jsonView.intro3_url?has_content>${rc.contextPath}/upload_temp/${jsonView.intro3_url}<#else>${rc.contextPath}/index_img/thirdCar_4.png</#if>">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<#include "/template/custom_service.ftl" />

	<script type="text/javascript"
		src="${rc.contextPath}/index_js/jquery-1.11.1.min.js"></script>
	<script src="${rc.contextPath}/index_js/unslider.min.js"></script>
	<script src="${rc.contextPath}/js/datepicker.js"></script>
	<script src="${rc.contextPath}/js/jquery.datepicker.region.cn.js"></script>
	<script src="${rc.contextPath}/js/jquery-ui/jquery-ui.js"></script>

	<script src="${rc.contextPath}/js/jquery-dateFormat.js"></script>

	<script type="text/javascript">
		  $(document).ready(function() {
		   initSwift2('${rc.contextPath}');
		  $("#kanqi li").click(function(){
				var days=$(this).children().attr("qc");		
				changeMonyByDays(days);
			});
	    } );
		function buy(pathurl,id){
		var mediaType='${jpaProduct.type}';
		var sh=$("#sh").val();
		if('inchof32'==mediaType){
		  if(sh=="" || sh=="lines_all,"){
		    layer.msg("请选择线路");
		   return;
		  }
		}
		var startdate1=$("#startdate1").val();
		if(startdate1==""){
		   layer.msg("请填写开播日期");
		   return;
		}
		var lc=$("#lc").val();
		if(lc=="0"){
			islogin(pathurl);
		}	
		var bodIds="";
		if(lc=="1"){	
		         $.ajax({
							url : "${rc.contextPath}/carbox/buy/media",
							data:{"proid":id,"needCount":$("#needCount").val(),"days":0,"startdate1":startdate1,"sh":sh},
							type : "POST",
							success : function(data) {
							if(data.left){
							  window.location.href="${rc.contextPath}/select?meids="+data.right+"&boids="+bodIds+"&startdate1="+startdate1;
							}
						}}, "text");
		}
		}
		function putIncar(pathurl,id){
		var mediaType='${jpaProduct.type}';
		var sh=$("#sh").val();
		if('inchof32'==mediaType){
		  if(sh==""|| sh=="lines_all,"){
		    layer.msg("请选择线路");
		   return;
		  }
		}
		    var startdate1=$("#startdate1").val();
			var lc=$("#lc").val();
			if(lc=="0"){
				islogin(pathurl);
			}
			if(lc=="1"){
		         $.ajax({
							url : "${rc.contextPath}/carbox/putIncar/media",
							data:{"proid":id,"needCount":$("#needCount").val(),"days":0,"startdate1":startdate1,"sh":sh},
							type : "POST",
							success : function(data) {
							layer.msg(data.right);
							setCarCount(data.cardCount);
							setCarCount(data.cardCount_top);
						}}, "text");
		 	}
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
		 //check time
		$('#startdate1').change(function() {
			<@security.authorize access="isAuthenticated()"> 
				<#if jpaProduct?? && jpaProduct.type=='video'> <!-- add by panxh, text image not need check -->
            	checkTime($("#startdate1").val(),${jpaProduct.id});
            	</#if>
            </@security.authorize> 
            
	         <@security.authorize access="!isAuthenticated()">
	         <#if jpaProduct?? && jpaProduct.type=='video'>
	         layer.alert("登录后可查看时间段库存!"); 
	         </#if>
			 </@security.authorize>					
        });
        
        
		   
		});
		
	</script>
</body>
</html>