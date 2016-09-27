<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="/css/one.css">
<link rel="stylesheet" type="text/css" href="/css/account.css">
<title>确认订单信息</title>





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
			<!--  
			<div class="c-top">
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
			<div class="cart-container cart-step2" style="padding-top:10px;">
				<div class="c-inner">
					<div class="tabline">
						<div class="arrow arrow-stp1">
							<span class="ara">1</span><a href="${rc.contextPath}/toCard"><span>查看购物车</span></a>
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
								<li class="td td-name">产品名称及描述</li>
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
										<p>${substring(item.product.name, 0, 18)}</p>
									</div>
								</li>
								<li class="td td-info">
									<div class="td-inner">
									<#if item.product.type =='inchof32'>
									<p class="perio">线路：${item.group.name}</p>
									</#if>
										<span> 时长：${item.product.duration}秒/次</span> 
										
										<#if item.product.type !='inchof32'>
										<p class="perio">单日播放次数：${item.product.playNumber}次</p>
										<#else>
										<p class="perio">小时播放次数：12次</p>
										</#if>
										<span class="perio">刊期：${item.product.days}天</span>
										<#if item.startTime?has_content>
										<span class="perio">上播日期：${item.startTime?string("yyyy-MM-dd")}</span>
										</#if>
											
									</div>
								</li>
								<li class="td td-price">
									<div class="td-inner">
										<p class="price">
											<em>￥</em>${item.price}</p>
									</div>
								</li>
								<li class="td td-amount">
									<div class="td-inner">
										<p class="amount">${item.needCount}</p>
									</div>
								</li>
								<li class="td td-sum">
									<div class="td-inner">
										<p class="sum">
											<em>￥</em>${item.price*item.needCount}</p>
									</div>
								</li>
							</ul>
						</div>
						</#list>
					</div>
					</#if> <#if infos.body?? && (infos.body?size>0)>
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
										<p class="price">
											<em>￥</em>${item.price}</p>
									</div>
								</li>
								<li class="td td-amount">
									<div class="td-inner">
										<p class="amount">${item.needCount}</p>
									</div>
								</li>
								<li class="td td-sum">
									<div class="td-inner">
										<p class="sum">
											<em>￥</em>${item.totalprice}</p>
									</div>
								</li>
							</ul>
						</div>
						</#list>
					</div>
					</#if>
					<div class="adj" id="payway0" >
						<div class="way" style="display:none;">
							<span class="legged">是否分期</span>
							<div class="select-items legged">
								<ul class="iradios">
									<li class="active"><input type="radio" 
										name="isdiv" checked="checked" value="0"> <label
										class="iradio iradio2"></label> <span>否</span></li>
									<li><input type="radio"  name="isdiv" value="1">
										<label class="iradio iradio2"></label> <span>是</span></li>
								</ul>
							</div>
						</div>
						<div class="stage line" style="display: none" id="c2">
							<span class="legged">选择期数</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
									<li class="active"><input type="radio" name="payType"
										checked="checked" value="online"> <label
										class="iradio"></label> <span>线上支付(工商银行)</span></li>
									<li><input type="radio" name="payType" value="offline">
										<label class="iradio"></label> <span>线下支付</span></li>
										
										
									<li>
									<input type="radio" name="payType" value="dividpay">
										<label class="iradio"></label> <span>分期付款</span>
										</li>
										
										<li>
									<input type="radio" name="payType" value="payContract">
										<label class="iradio"></label> <span>合同分期</span>
										</li>
										<@security.authorize ifAnyGranted="sales">
										<span style = "margin-left:50px">客户列表</span>
	                         				  <span>
	                               			 <input id="autocomplete" value="" style="width:250px">
	                               			  <input id="customerId" value="" type="hidden">
	                            			</span>
                            			  </@security.authorize>
								</ul>
							</div>
						</div>
						<div class="footall">
							<div class="">
							
							</div>
							<div class="sum">
								<span class="legged">总价:<em>￥${infos.totalPrice}</em></span>
							</div>
							<div class="sure">
								<!-- <a href="javascript:void(0);" onclick="check()"> -->
								<a href="javascript:void(0);" id="subid" onclick="check()">
									<div class="btn-sure">确认支付</div>
								</a>
							</div>
						</div>
						
						<div class="wormB-tips">
							<div class="tips-title">
							<span class="icon"></span>
			<font color="orange"><B>温馨提示</B></font><br>
			三方一致：合同甲方公司名称-付款方银行开户名称-我方开具发票抬头名称<br>
			线下付款的账户信息：<br>
			&nbsp;&nbsp;&nbsp;&nbsp;开户行：<B>工行知春路支行</B><br>
			&nbsp;&nbsp;&nbsp;&nbsp;账户名称：<b>北京世巴传媒有限公司</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;收款方账号：<b>0200207909200097152</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;公司地址：北京市海淀区紫竹院路32号15号平房	电话：68427368<br>
		</div>
		</div>
						
						
						
					</div>
				</div>
			</div>
		</div>
					<!-- icbc begin-->
					
			<form id="form1"  target="_blank"  action="https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet" method="post">
			    <input type="hidden" name="APIName" value="B2B"/>
			    <input type="hidden" name="APIVersion" value="001.001.001.001"/>
			    <input type="hidden" name="Shop_code" value="0200EC14729207"/>
			    <!--若不正确，将无银行反馈信息，注意不能省略"http://"-->
			    <input type="hidden" name="MerchantURL" value="${callback}"/>
			    <input type="hidden" name="ContractNo" value="${contractNo}"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="ContractAmt" value="${totalPrice}"/>
			    <input type="hidden" name="Account_cur" value="001"/>
			    <input type="hidden" name="JoinFlag" value="2"/>
			    <input type="hidden" name="Mer_Icbc20_signstr" value="${a1}"/>
			    <input type="hidden" name="Cert" value="${a2}"/>
			    <input type="hidden" name="SendType" value="0"/>
			    <input type="hidden" name="TranTime" value="${TranTime}" />
			    <input type="hidden" name="Shop_acc_num" value="0200004519000100173"/>
			    <input type="hidden" name="PayeeAcct" value="0200004519000100173"/>
			    <input type="hidden" name="GoodsCode" value="CODE_MEDIA"/>
			    <input type="hidden" name="GoodsName" value="SPTC"/>
			    <input type="hidden" name="Amount" value="1"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="TransFee" value="1"/>
			    <input type="hidden" name="ShopRemark" value=""/>
			    <input type="hidden" name="ShopRem" value=""/>
			    <input type="submit" id="icbcOPer" value="确定"/>
			</form>
			
			<!--end icbc -->
		
	</div>
	<#include "/template/custom_service.ftl" />

	<script type="text/javascript" src="index_js/jquery-1.11.1.min.js"></script>
	<!--增加lay最新版本-->
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/layer.onload.js"></script>
	<script type="text/javascript" language="javascript"
		src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>
		<link rel="stylesheet" type="text/css"
		href="${rc.contextPath}/js/layer-v1.9.3/layer.site.css"> 
		
		
	<script src="index_js/unslider.min.js"></script>
	
	<script type="text/javascript" language="javascript" src="/js/jquery-ui/jquery-ui.js"></script>
<script type="text/javascript" language="javascript"
	src="/js/jquery-ui/jquery-ui.auto.complete.js"></script>
	
	<link rel="stylesheet" type="text/css" href="/js/jquery-ui/jquery-ui.auto.complete.css">
	<link rel="stylesheet" type="text/css" href="/css/autocomplete.css">
	
	
	<script type="text/javascript">
		function contractConFirm(){
		var customerId=$("#customerId").val();
		var paytype=$('#payway :radio[name=payType]:checked').val();
		var cusId='';
		if(typeof(customerId)=="undefined"){
		      customerId=cusId;
		}
					 layer.open({
					    		type: 1,
					    		title: "电子合同",
					    		skin: 'layui-layer-rim', 
					    		area: ['800px', '600px'], 
					    		content:''
								   +' '
								   +'<iframe  style="width:99%;height:90%" src="${rc.contextPath}/user/contract_templete?paytype='+paytype+'&customerId='+customerId+'&meids=${meids!''}"/><div class="ui-form-item widthdrawBtBox" style="width: 42%;"> <input type="button" id="subWithdraworder" class="block-btn" onclick="payment();" value="确认" style="margin:10px 0px -10px 45%;"> </div>'
								});
		}
		function check(){
		var customerId=$("#customerId").val();
		var bid='${boids!''}';
		if(bid==""){
		$.ajax({
		url : "${rc.contextPath}/user/getUserDetail",
		type : "GET",
		data : {
		},
		success : function(data) {
					<@security.authorize ifAnyGranted="sales">
					if(customerId==''  || typeof(customerId)=="undefined" ){
						layer.confirm('您还没有选择客户信息,确定下单吗？', {
						  btn: ['确定','取消下单'] //按钮
						}, function(){
						 	  layer.closeAll();
							  contractConFirm();
						}, function(){
						});
					}else  {
							contractConFirm();	
					}	
					 </@security.authorize>
					 <@security.authorize ifNotGranted="sales">
					  if(typeof(data.company)=="undefined" || data.company=="" || typeof(data.legalman)=="undefined" || data.legalman==""){
			             layer.msg("请从‘用户信息’菜单进去完善相关信息");
			             return;
			            }
					 		contractConFirm();
					  </@security.authorize>
				 }
			});//end ajax
			}else{
			payment();
			}
			
		}
		
		function canelPay(runningNum){
							var i=5;
							 setInterval(function(){               
		           				 if(i == 1) { window.location.href="${rc.contextPath}/order/myTask/1";}
	               				$("#payMsg").html("订单已产生,但支付还未成功！<br><font color='red'>"+(i--)+"</font>秒后跳转到后台");
	           				 },1000);
		}
		function checkPayStatus(runningNum){
				$.ajax({
					url:"${rc.contextPath}/carbox/ajax-checkPayStats",
					type:"POST",
					dataType:"json",
					data:{"runningNum":runningNum },
					success:function(data){
						if (data) {
							var i=3;
							 setInterval(function(){               
		           				 if(i == 1) { window.location.href="${rc.contextPath}/carbox/paySuccess/media";}
	               				$("#payMsg").html("支付成功！,<font color='red'>"+(i--)+"</font>秒后跳转到后台");
	           				 },1000);
						}else{
							var i=5;
							 setInterval(function(){               
		           				 if(i == 1) { window.location.href="${rc.contextPath}/order/myTask/1";}
	               				$("#payMsg").html("支付还未成功！,<font color='red'>"+(i--)+"</font>秒后跳转到后台");
	           				 },1000);
	           			 }
					}
		          }); 
          
          }
		
		function payment(){
		var paytype=$('#payway :radio[name=payType]:checked').val();
		var isdiv=$('#payway0 :radio[name=isdiv]:checked').val();
		var divid=$("#dividpay").val();
		var seriaNum=${seriaNum};
		var runningNum=${runningNum};
		var meids='${meids!''}';
		var boids='${boids!''}';
		var startdate1='${startdate1!''}';
		if(paytype=="" || typeof(paytype)=="undefined"){
		  layer.msg("请选择支付方式");
		  return;
		}
		if(seriaNum=="" || typeof(seriaNum)=="undefined"){
		  layer.msg("没有seriaNum,操作异常");
		  return;
		}
		if(runningNum=="" || typeof(runningNum)=="undefined"){
		  layer.msg("没有流水号,操作异常");
		  return;
		}
		var customerId=$("#customerId").val();
		 $("#subid").attr("onclick",'');
		$.ajax({
			url:"${rc.contextPath}/carbox/payment",
			type:"POST",
			dataType:"json",
			data:{"divid":divid,"isdiv":isdiv,"seriaNum":seriaNum,
			"paytype":paytype,"meids":meids,"boids":boids,
			"startdate1":startdate1,"runningNum":runningNum,"customerId":customerId},
			success:function(data){
				if (data.left) {
					if(boids==""){
						if(data.right.paytype =='online'){
							$("input[name='ContractNo']").attr("value", data.right.runningNum);
							    layer.closeAll();
							  layer.msg('<h3 style="line-height: 45px;font-size: 15px;"><span id="payMsg">请您在新打开的页面插上U盾完成支付！<span></h3><br><span class="tip_font">•支付完成前请不要关闭此窗口<br>•支付失败时，可以迅速联系我们客服(010-88510188)</span>'
							  +'<br><br><a class="block-btn" href="javascript:void(0);"  onclick="checkPayStatus(' + data.right.runningNum
							  +')">确认成功 </a><a class="fail-btn" href="javascript:void(0);"'
							  +'  onclick="canelPay()">确认失败 </a>',{time: 300000,icon:9});
							  $('#icbcOPer').click(); 
						} else {
							window.location.href="${rc.contextPath}/carbox/paySuccess/media";
						}
					} else {//car
						window.location.href="${rc.contextPath}/carbox/paySuccess/body";
					}
				} else {
					layer.msg(data.right.msg,{time: 5000,icon: 5});
				}
			}
          });  
		}
		  $(document).ready(function() {
		  $('.legged .iradio2').on('click', function(event) {
					$(this).prev()[0].checked = true;
					var w = ($(this).prev()[0]).value;
            if(w==0){
            	$("#c2").hide();
            }else if(w==1) {
            	$("#c2").show();
            }
				});
		});
			$(document).ready(function(e) {
			 bindLayerMouseWithOpen(6000);
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
				//---
				<@security.authorize ifAnyGranted="sales">
		        $( "#autocomplete" ).autocomplete({
		        	minLength: 0,
		  			source: "${rc.contextPath}/user/queryMyCustomers",
		  			change: function( event, ui ) { 
		  				/*if(ui.item!=null){alert(ui.item.value);}*/
		  			 },
		  			 select: function(event,ui) {
		  			 	$('#autocomplete').val(ui.item.value);
		  			 	$('#customerId').val(ui.item.dbId);
		  			 	
		  			 }
				}).focus(function () {
       				 $(this).autocomplete("search");
   	 			});
				//--end autocomplete
				  </@security.authorize>
			});
		</script>
</body>
</html>