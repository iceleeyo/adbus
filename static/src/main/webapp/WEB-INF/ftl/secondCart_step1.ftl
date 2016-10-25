<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>购物车</title>

</head>

<body>
	<header> <!-- 头部开始 --> <#include "/index_menu/index_top.ftl"
	/> <!-- 头部结束 --> </header>
	<div class="content">
		<div class="side-nav">
			<div class="logo"></div>
			<div class="de-code">
				<img src="index_img/pic1.jpg" height="100" width="100">
			</div>
			<ul class="navibar">
				<li><a href="/">首页</a></li>
				<li class="active"><a href="${rc.contextPath}/media">产品媒体</a></li>
				<li><a href="${rc.contextPath}/caseMore">案例欣赏</a></li>
			</ul>
			<div class="markble">
				<p>世界在你脚下，巴士一路随行</p>
				<p>北京公交媒体</p>
			</div>
		</div>
		<div class="cover">
			  
			<div class="c-top"> <!--
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
				</div> -->
			</div>
			
			<div class="cart-container cart-step1" style="padding-top:10px;">
				<div class="c-inner">
					<div class="tabline">
						<div class="arrow arrow-stp1">
							<span class="ara">1</span><span>查看购物车</span>
						</div>
						<div class="arrow arrow-stp2">
							<span class="ara">2</span><span>确认订单信息</span>
						</div>
						<div class="arrow arrow-stp3">
							<span class="ara">3</span><span>成功提交订单</span>
						</div>
					</div>
					<#if infos.media?? && (infos.media?size>0)>
					<div class="panel">
						<div class="panel-head">
							<ul class="clearfix">
								<li class="td td-check">
									<div class="td-inner">
										<div class="cart-check mediaB">
											<input class="hideinput" type="checkbox" name="order">
											<label id="mediaAll"></label>
										</div>
									</div>
								</li>
								<li class="td td-name">移动视频</li>
								<li class="td td-price">单价</li>
								<li class="td td-amount">数量</li>
								<li class="td td-sum">合计</li>
								<li class="td td-handle">操作</li>
							</ul>
						</div>
						<#list infos.media as item>

						<div class="panel-item">
							<ul class="item-info clearfix_">
								<li class="td td-check clearfix">
									<div class="td-inner">
										<div class="cart-check mediaB">
											<input class="hideinput" type="checkbox" id="${item.id}"
												pid="p_${item.id}"
												name="checkone" value="${item.id}"> <label
												class="chilbox" rowid="${item.id}"></label>
										</div>
									</div>
								</li>
								<li class="td td-item">
									<div class="td-inner">
										<p>${substring(item.product.name, 0, 15)}</p>
										
									</div>
								</li>
								<li class="td td-info">
									<div class="item-rect">
									<#if item.product.type =='inchof32'>
									<p class="rec-line">线路：${item.group.name}</p>
									</#if>
										<p class="rec-line">时长（秒）：${item.product.duration}</p>
										<#if item.product.type !='inchof32'>
										<p class="rec-line">单日播放次数：${item.product.playNumber}次</p>
										<#else>
										<p class="rec-line">小时播放次数：12次</p>
										</#if>
										<p class="rec-line">刊期：${item.product.days}天</p>
									<p class="rec-line">上播日期： <input style="width:80px"
										class="ui-input datepicker validate[required,custom[date] "
										type="text" value="<#if item.startTime?has_content>${item.startTime?string("yyyy-MM-dd")}</#if>" id="st_${item.product.id}" data-is="isAmount isEnough"
										autocomplete="off" onchange="dateInput('st_${item.product.id}',${item.product.id}),updateCardMeida('st_${item.product.id}',${item.id})">
										</p>
										<@security.authorize ifAnyGranted="sales">
											<p class="rec-line"><input class="hideinput" id="isChangeOrder" onclick="updateCardMeida('st_${item.product.id}',${item.id})" type="checkbox"  value="N" name="isChangeOrder">换版订单 	
										</@security.authorize>
										</p>
										<span class="btn-edit"></span>
									</div>
								</li>
								<li class="td td-price">
									<div class="td-inner">
										<p class="price">
											<em>￥</em>${item.price}</p>
											<input type="hidden" id="price_${item.id}" value="${item.price}">
									</div>
								</li>
								<input type="hidden" id="pid_${item.id}"
									value="${item.product.id}">
								<input type="hidden" id="uprice_${item.id}"
									value="${item.price}">
								<li class="td td-amount"><span class="icon icon-plus"
									onclick="leftDec(${item.id});"></span> <input type="text"
									id="sum_${item.id}" value="1"
									onblur="meblur(${item.id})"> 
									<!-- ${item.needCount}  -->
									<span
									class="icon icon-sub" onclick="notAllow()"></span></li><!-- leftPlus(${item.id}); -->
									
								<li class="td td-sum">
									<div class="td-inner">
										<p class="sum">
											<em>￥</em><span id="mexiaoji_${item.id}">${item.totalprice}</span>
										</p>
									</div>
								</li>

								<li rowid="${item.id}" class="td td-handle"><a
									href="javascript:void(0);" onclick="removeOne(${item.id});">
										<p class="del-like"></p>
								</a></li>
							</ul>
						</div>
						</#list>
					</div>
					</#if> <#if (infos.body?size>0)>
					<div class="panel">
						<div class="panel-head">
							<ul class="clearfix">
								<li class="td td-check">
									<div class="td-inner">
										<div class="cart-check bodyB">
											<input class="hideinput" type="checkbox" name="order">
											<label id="bodyAll"></label>
										</div>
									</div>
								</li>
								<li class="td td-name">车身产品</li>
								<li class="td td-price">单价(元/月/辆)</li>
								<li class="td td-amount">车辆数</li>
								<li class="td td-sum">合计</li>
								<li class="td td-handle">操作</li>
							</ul>
						</div>
						<#list infos.body as item>
						<div class="panel-item">
							<ul class="item-info clearfix_">

								<li class="td td-check clearfix">
									<div class="td-inner">
										<div class="cart-check bodyB">
											<input class="hideinput" type="checkbox" name="b_checkone"
												value="${item.id}"> <label class="chilbox" rowid="${item.id}"></label>
										</div>
									</div>
								</li>
								<li class="td td-item">
									<div class="td-inner">
										<p>${substring(item.product.jpaProductV2.name, 0, 9)}</p>
									</div>
								</li>

								<li class="td td-info">
									<div class="item-rect">
										<p class="rec-line">线路级别：${item.product.leval.nameStr}</p>
										<p class="rec-line">车型：<#if item.product.doubleDecker> 双层
											<#else> 单层 </#if></p>
										<p class="rec-line">
											刊期： <#if item.isDesign==0> ${item.product.days/30}个月
							<input	type="hidden" id="b_days_${item.id}" value="${item.product.days/30}" />
							 <#else> ${item.days/30}个月
							<input type="hidden" id="b_days_${item.id}" value="${item.days/30}" /> </#if>
												
										</p>
										<span class="btn-edit"></span>
									</div>
								</li>
								<li class="td td-price">
									<div class="td-inner">
										<p class="price">
											<em>￥</em>${item.price}</p>
											<input type="hidden" id="price_b_${item.id}" value="${item.price}">
									</div>
								</li>
								<input type="hidden" id="b_uprice_${item.id}"
									value="${item.price}">
								<input type="hidden" id="b_pid_${item.id}"
									value="${item.product.id}">
								<li class="td td-amount"><span class="icon icon-plus"
									onclick="b_leftDec(${item.id},${item.isDesign});"></span> <input
									type="text" id="b_sum_${item.id}"
									onblur="boblur(${item.id},${item.isDesign});"
									value="${item.needCount}"> <span class="icon icon-sub"
									onclick="b_leftPlus(${item.id},${item.isDesign});"></span></li>
								<li class="td td-sum">
									<div class="td-inner">
										<p class="sum">
											<em>￥</em><span id="boxiaoji_${item.id}">${item.totalprice}
										</p>
									</div>
								</li>
								<li rowid="${item.id}" class="td td-handle"><a
									href="javascript:void(0);" onclick="b_removeOne(${item.id});">
										<p class="del-like"></p>
								</a></li>
							</ul>
						</div>
						</#list>
					</div>
					</#if>

				</div>
				<div class="acount-fix">
					<div class="acount-top">
						<div class="top-left"></div>
						<div class="top-right"></div>
					</div>
					<div class="acount-bottom">
						<div class="acount-inner clearfix">
							<div class="inner-left">
								<div class="cart-check">
									<input class="hideinput" type="checkbox" name="item"> <label
										id="all"></label>
								</div>
								全选
							</div>
							<div class="inner-right">
								<span>总价:</span> <span id="aprice" class="acount-price">￥${infos.totalPrice}</span>
								<a href="javascript:void(0);" onclick="selectPro()">
									<div class="btn-over">确认选择</div>
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<#include "/template/custom_service.ftl" />

		<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
		<script src="index_js/unslider.min.js"></script>
		<script src="index_js/sift_common.js"></script>
		<!--增加lay最新版本-->
		<script type="text/javascript" language="javascript"
			src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
		<script type="text/javascript" language="javascript"
			src="${rc.contextPath}/js/layer.onload.js"></script>
		<script type="text/javascript" language="javascript"
			src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>
			
	<script src="${rc.contextPath}/js/datepicker.js"></script>
	<script src="${rc.contextPath}/js/jquery.datepicker.region.cn.js"></script>
	<script src="${rc.contextPath}/js/jquery-ui/jquery-ui.js"></script>
		<script src="${rc.contextPath}/js/jquery-dateFormat.js"></script>
	
		<script type="text/javascript">
		function notAllow(){
			layer.msg("暂时只支持购买单件商品");
		}
		function selectPro(){
		var me= document.getElementsByName("checkone");
        	var medIds='';
        	for(var i=0;i<me.length;i++){
                if(me[i].checked)
                medIds+=me[i].value+',';   
           }
		var bo= document.getElementsByName("b_checkone");
        	var boIds='';
        	for(var i=0;i<bo.length;i++){
                if(bo[i].checked)
                boIds+=bo[i].value+',';   
           }
           if(medIds=="" && boIds==""){
        	   layer.msg("请选择商品");
        	   return false;
           }
   		layer.confirm('确定选择这些商品吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		          window.location.href="${rc.contextPath}/select?meids="+medIds+"&boids="+boIds;
		       }
		});		
		}
		
		
		function removeOne(id){
		layer.confirm('确定删除吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
	 $.ajax({
			url:"${rc.contextPath}/carbox/delOneCarBox/media/"+id,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left) {
					layer.msg(data.right);
					updateMoney();
					initCardView('${rc.contextPath}');
				   $("ul li[rowid="+id+"]").parent().parent().remove();
				} else {
					layer.msg(data.right);
				}
			}
      });  
   }
		});		
		}
		function b_removeOne(id){
		layer.confirm('确定删除吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
	 $.ajax({
			url:"${rc.contextPath}/carbox/delOneCarBox/body/"+id,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left) {
				updateMoney();
				initCardView('${rc.contextPath}');
					layer.msg(data.right);
				$("ul li[rowid="+id+"]").parent().parent().remove();
				} else {
					layer.msg(data.right);
				}
			}
      });  
   }
		});		
		}
		function me_changexiaoji(sot,needC,uprice){
		var t=needC*uprice;
		 $("#mexiaoji_"+sot).html(t);
		}
				function meblur(id){
				  var sot=id;
				 $.ajax({
						url : "${rc.contextPath}/carbox/saveCard/media",
						data:{"proid":$("#pid_"+sot).val(),"needCount":$("#sum_"+sot).val(),"uprice":$("#uprice_"+sot).val(),"IsDesign":0},
						type : "POST",
						success : function(data) {
						   //window.location.reload();
						   updateMoney();
						   me_changexiaoji(sot,$("#sum_"+sot).val(),$("#uprice_"+sot).val());
					}}, "text");
				}
				function boblur(id,isdesing){
				  var sot=id;
				  bo_changexiaoji(sot,$("#b_sum_"+sot).val(),$("#b_uprice_"+sot).val());
				 $.ajax({
						url : "${rc.contextPath}/carbox/saveCard/body",
						data:{"proid":$("#b_pid_"+sot).val(),"needCount":$("#b_sum_"+sot).val(),"IsDesign":isdesing},
						type : "POST",
						success : function(data) {
						updateMoney();
					}}, "text");
				}
		function leftDec(id){
			    var sot=id;
			    var y=$("#sum_"+sot).val();
			    if(y>1){
			    $("#sum_"+sot).val(parseInt(y)-1);
			    me_changexiaoji(sot,$("#sum_"+sot).val(),$("#uprice_"+sot).val());
					$.ajax({
						url :  "${rc.contextPath}/carbox/saveCard/media",
						data:{"proid":$("#pid_"+sot).val(),"needCount":$("#sum_"+sot).val(),"uprice":$("#uprice_"+sot).val(),"IsDesign":0},
						type : "POST",
						success : function(data) {
						updateMoney();
							}}, "text");
			  }
		}
		function leftPlus(id){
			var sot=id;
			var y=$("#sum_"+sot).val();
			$("#sum_"+sot).val(parseInt(y)+1);
			 me_changexiaoji(sot,$("#sum_"+sot).val(),$("#uprice_"+sot).val());
					$.ajax({
						url : "${rc.contextPath}/carbox/saveCard/media",
						data:{"proid":$("#pid_"+sot).val(),"needCount":$("#sum_"+sot).val(),"uprice":$("#uprice_"+sot).val(),"IsDesign":0},
						type : "POST",
						success : function(data) {
						 updateMoney();
							}}, "text");
		}
		function bo_changexiaoji(sot,needC,uprice){
		var t=$("#b_days_"+sot).val()*needC*uprice;
		 $("#boxiaoji_"+sot).html(t);
		}
		function b_leftDec(id,isdesing){
			    var sot=id;
			    var y=$("#b_sum_"+sot).val();
			    if(y>1){
			    $("#b_sum_"+sot).val(parseInt(y)-1);
			     bo_changexiaoji(sot,$("#b_sum_"+sot).val(),$("#b_uprice_"+sot).val());
					$.ajax({
						url :  "${rc.contextPath}/carbox/saveCard/body",
						data:{"proid":$("#b_pid_"+sot).val(),"needCount":$("#b_sum_"+sot).val(),"IsDesign":isdesing},
						type : "POST",
						success : function(data) {
						 //window.location.reload();
						 updateMoney();
							}}, "text");
			  }
		}
		function b_leftPlus(id,isdesing){
			var sot=id;
			var y=$("#b_sum_"+sot).val();
			$("#b_sum_"+sot).val(parseInt(y)+1);
			 bo_changexiaoji(sot,$("#b_sum_"+sot).val(),$("#b_uprice_"+sot).val());
					$.ajax({
						url : "${rc.contextPath}/carbox/saveCard/body",
						data:{"proid":$("#b_pid_"+sot).val(),"needCount":$("#b_sum_"+sot).val(),"IsDesign":isdesing},
						type : "POST",
						success : function(data) {
						 //window.location.reload();
						 updateMoney();
							}}, "text");
		}
		function updateMoney(){
				 $.ajax({
						url : "${rc.contextPath}/carbox/carboxMoney",
						data:{},
						type : "POST",
						success : function(data) {
								$("#aprice").html("￥"+data);
								updateSelectMoney();
					    }}, "text");
					    
		}
		
		function updateSelectMoney(){
			var selectPrice=0;
			$('.chilbox').each(function(){
						 if($(this).prev()[0].checked){
							var t=$(this).attr("rowid");//先取的id
							//var tprice =$("#price_"+t).val();//再根据id取的价格
							var tprice =$("#mexiaoji_"+t).html();
							//alert(tprice);
							if (typeof(tprice) == "undefined"){
								tprice =$("#boxiaoji_"+t).html();
							}
							selectPrice+=Number(tprice);
						}
			 });
			 if(selectPrice>=0){
				 $("#aprice").html("￥"+selectPrice);
			 }
		} 
		
		
			$(document).ready(function(e) {
			
			
			initCardView('${rc.contextPath}');
				$('.td-info .item-rect').hover(function() {
					$(this).addClass('item-rect-hover');
				}, function() {
					$(this).removeClass('item-rect-hover');
				});
			});
				
				 	
			/* $('#all').on('click', function(event) {
				event.preventDefault();
				console.log($(this).parent().find($("input"))[0].checked);
				if(!$(this).parent().find($("input"))[0].checked){
					$('.cart-check').addClass('active');
					$('.cart-check').find($("input"))[0].checked = true;
		        }
		        else{
		        	$('.cart-check').removeClass('active');        
		            $('.cart-check').find($("input"))[0].checked = false;
		        }
		        	event.stopPropagation();
						
			}); */
			
			$('#all').on('click', function(event) {
				event.preventDefault();
				/* console.log($('.cart-check').find($("input"))[0].checked); */
				function ifChecked (cartCheck, bool) {
					$(cartCheck).each(function(i){
					   $(this).find($("input"))[0].checked = bool;
					 });
				}
				if(!$(this).parent().find($("input"))[0].checked){
					$('.cart-check').addClass('active');
					ifChecked(".cart-check", true);
		        }
		        else{
		        	$('.cart-check').removeClass('active');  
		        	ifChecked(".cart-check", false);  
		        }
		        	event.stopPropagation();
						updateSelectMoney();
			});
			
			$('#mediaAll').on('click', function(event) {
				event.preventDefault();
				/* console.log($('.cart-check').find($("input"))[0].checked); */
				function ifChecked (cartCheck, bool) {
					$(cartCheck).each(function(i){
					   $(this).find($("input"))[0].checked = bool;
					 });
				}
				if(!$(this).parent().find($("input"))[0].checked){
					$('.mediaB').addClass('active');
					ifChecked(".mediaB", true);
		        }
		        else{
		        	$('.mediaB').removeClass('active');  
		        	ifChecked(".mediaB", false);  
		        }
		        	event.stopPropagation();
				updateSelectMoney();		
			});
			
			$('#bodyAll').on('click', function(event) {
				event.preventDefault();
				/* console.log($('.cart-check').find($("input"))[0].checked); */
				function ifChecked (cartCheck, bool) {
					$(cartCheck).each(function(i){
					   $(this).find($("input"))[0].checked = bool;
					 });
				}
				if(!$(this).parent().find($("input"))[0].checked){
					$('.bodyB').addClass('active');
					ifChecked(".bodyB", true);
		        }
		        else{
		        	$('.bodyB').removeClass('active');  
		        	ifChecked(".bodyB", false);  
		        }
		        	event.stopPropagation();
					updateSelectMoney();	
			});
				
			$("label.chilbox").on('click', function(){
				console.log($(this).prev()[0].checked); 
				if(!$(this).prev()[0].checked){
					$(this).parent().addClass('active');
					$(this).prev()[0].checked = true;
		        }
		        else{
		        	$(this).parent().removeClass('active'); 
		        	$(this).prev()[0].checked = false;
		        }
		        	event.stopPropagation();
					updateSelectMoney();  
			});
		</script>
</body>
</html>