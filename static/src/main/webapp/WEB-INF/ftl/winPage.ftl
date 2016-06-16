<#import "template/template_blank.ftl" as frame > <#import
"template/proDetail.ftl" as proDetail> <#global menu="我的获拍">
<@frame.html title="我的获拍"
js=["js/jquery.jcountdown.js","js/jquery.jcountdown.site.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/compare/auction.css","css/sea.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">
    


    
function compare(pathurl,proid){
var startTime = $("#startTime").val();
var d = new Date(startTime.replace(/-/g,"/")); 
date = new Date();
var str  = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
var d2 = new Date(str.replace(/-/g,"/")); 	
var lc=$("#lc").val();
if(lc=="0"){
islogin(pathurl);
}
        if(startTime=""){
         jDialog.Alert('请填写开播日期');
         return;
         }
           var days=Math.floor((d-d2)/(24*3600*1000));
        if(days<3) {
            jDialog.Alert('开播日期请选择3天以后');
            return;
         } 
//author :impanxh 阻止2次点击 ,当所有表单都验证通过时才提交 抄自注册页面
         if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
        if(lc=="1"){
        	layer.open({
    		type: 1,
    		title: "电子合同",
    		skin: 'layui-layer-rim', 
    		area: ['650px', '630px'], 
    		content:''
			   +' '
			   +'<iframe  style="width:99%;height:90%" src="${rc.contextPath}/user/contract_templete?productid='+proid+'"/><div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdraworder" class="block-btn" onclick="creorder();" value="确认" style="margin:10px 0px -10px 110px;"> </div>'
			});
		}
}

function creorder() {
	$("#subWithdraworder").attr("disabled",true);
	$("#subWithdraworder").css("background-color","#85A2AD");
	$('#userForm2').submit();
}
</script>

<script type="text/javascript">
        function bu(txtObj) {
            txtObj.value = Number(txtObj.value).toFixed(2);
        }
</script>

<div class="pg-container">
	<div class="pg-container-main">
		<div class="container-12 mt10 s-clear">
			<div class="ls-9">
				<form data-name="withdraw" name="userForm2" id="userForm2"
					class="ui-form" method="post"
					action="${rc.contextPath}/order/confirm?cpdid=${cpdid}"
					enctype="multipart/form-data">
					<div class="product-info s-clear">
						<div class="preview s-left">
							<#if (medetype)=="body"> <img src="${rc.contextPath}/imgs/19.jpg"
								width="298" height="298"> <#elseif (medetype)=="screen"> <img
								src="${rc.contextPath}/imgs/yingguang.jpg" width="298"
								height="298"> </#if>
						</div>
						<div class="product-detail s-left">
							<div class="product-title">
								<h3>${(jpaCpd.product.name)!''}</h3>
							</div>
							<div class="product-form">
								<div class="product-intro" style="margin-left: 5px;">
									<div class="price s-clear">
										<span>当前价：</span> <span class="fsize-24 t-red"><em>¥</em>
											#{jpaCpd.comparePrice!'' ;m2M2} </span> <em class="line"></em> <span>原价：</span>
										<del> #{(jpaCpd.product.price)!'' ;m2M2} </del>
										<div class="s-right" style="margin-top: 13;">
											<span>围观数：</span> <em>${jpaCpd.pv} 次</em>
										</div>
									</div>

									<div class="range" style="line-height: 50px;">
										<#if (prod.type)!="body"> <span>播放次数：<em>${prod.playNumber!''}次</em></span>
										</#if> <span>产品周期：<em>${prod.days!''}天</em></span> <span>媒体类型：
											<em>${prod.type.typeName!''}</em>
										</span> <#if (prod.type)=="body"> <span>线路级别： <em><#if
												prod.lineLevel??>${prod.lineLevel.nameStr!''}</#if></em></span> <span>巴士数量：
											<em>${prod.busNumber!''}</em>
										</span> </#if> <#if (prod.type)!="body"> <span>时长（秒）： <em>${prod.duration!''}</em></span>
										</#if>
									</div>
								</div>
								<div class="" style="margin: 5px 5px;">
									<label class="range" style="color: #999;">开播日期:</label> <input
										class="ui-input datepicker validate[required,custom[date] layer-tips"
										type="text" name="startTime1" id="startTime"
										data-is="isAmount isEnough" autocomplete="off"
										disableautocomplete="" tip="可选择3天后日期!">


								</div>
							</div>
							<div class="product-btn" style="float: right;">
								<a class="btn-bid" href="javascript:void(0)"
									onclick="compare('${rc.contextPath}',${prod.id!''})">我要购买</a>
								<input type="hidden" readonly="readonly" name="product.id"
									id="productId" value="${prod.id!''}" /> <input type="hidden"
									readonly="readonly" name="supplies.id" id="productId" value="1" />
								<input type="hidden" readonly="readonly" name="price"
									value="${jpaCpd.comparePrice!''}" /> <input type="hidden"
									readonly="readonly" name="token" value="${token!''}" />
								<@security.authorize access="isAuthenticated()"> <input
									type="hidden" id="lc" value="1" /> </@security.authorize>
								<@security.authorize access="! isAuthenticated()"> <input
									type="hidden" id="lc" value="0" /> </@security.authorize>
							</div>
							<!--  
										<div class="product-btn">
										<#if (jpaCpd.startDate < .now  && jpaCpd.biddingDate > .now  ) > 
											<a class="btn-bid" href="javascript:void(0)" onclick="compare('${rc.contextPath}')" >我要出价</a>
											 <script type="text/javascript">
										      		$(function(){ 
										      			$("#residue").html("剩余时间");
														var dateTo="${jpaCpd.biddingDate?string("yyyy-MM-dd HH:mm:ss")}";
														countDate("auction1Timer",dateTo);
													});
										      </script>
										<#elseif (jpaCpd.startDate > .now) > 
											<a class="btn-bid" style="background: #f5f5f5;color:#333" href="javascript:void(0)">等待开始</a>
											 <script type="text/javascript">
										      		$(function(){ 
										      			$("#residue").html("距离开拍");
										      			$("#cspan").css("display","none");
														var dateTo="${jpaCpd.startDate?string("yyyy-MM-dd HH:mm:ss")}";
														countDate("auction1Timer",dateTo);
													});
										      </script>
										<#elseif (jpaCpd.biddingDate < .now) > 
											<a class="btn-bid" style="background: #f5f5f5;color:#333" href="javascript:void(0)">竞价结束</a> 
											
											 <script type="text/javascript">
										      		$(function(){ 
										      			$("#residue").html("结束时间");
										      		$("#cspan").css("display","none");
														var dateTo="${jpaCpd.biddingDate?string("yyyy-MM-dd HH:mm:ss")}";
													   $("#auction1Timer").html(dateTo);
													});
										      </script>
										</#if>
											<input type="hidden" id="productid" value="${(jpaCpd.id)!''}"/>	
											
										<@security.authorize access="isAuthenticated()">
                                        <input type="hidden" id="lc" value="1"/>	
                                        </@security.authorize>
                                        <@security.authorize access="! isAuthenticated()">
                                         <input type="hidden" id="lc" value="0"/>	
                                        </@security.authorize>
										</div>
										 -->
						</div>
					</div>
				</form>
				<div class="bidPath">
					<div class="lc">
						<span>竞&nbsp;&nbsp;&nbsp;拍</span><br> <span>流&nbsp;&nbsp;&nbsp;程</span>
					</div>
					<div class="flow" style="overflow: hidden; width: 700px;">
						<div class="lc-flow" style="width: 800px;">
							<ul>
								<li class="item01 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">用户账号当前京豆大于0</div> <span class="num">01/</span>
									<span class="mark"></span> <span class="name">获取资格</span>
								</li>
								<li class="item02 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">保证竞拍结束时出价最高，获得竞拍商品</div> <span class="num">02/</span>
									<span class="mark"></span> <span class="name">拍得商品</span>
								</li>
								<li class="item03 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">填写订单信息，提交订单</div> <span class="num">03/</span>
									<span class="mark"></span> <span class="name">确认转订单</span>
								</li>
								<li class="item04 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">支付已提交的订单</div> <span class="num">04/</span>
									<span class="mark"></span> <span class="name">支付订单</span>
								</li>
								<li class="item05 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">支付成功后等待收货，竞拍完成</div> <span class="num">05/</span>
									<span class="mark"></span> <span class="name">竞拍成功</span>
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
					<div class=" color-white-bg fn-clear" style="margin-left: 0px;">
						<DIV class="summary mt10 uplan-summary-div">
							<UL class="uplan-detail-ul">
								<LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN
									class="con">${prod.name!''}</SPAN></LI>
								<li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
								<LI style="width: 240px;"><SPAN><#if prod.type ==
										'body'>媒体费：<#else>套餐原价：</#if></SPAN><SPAN class="con"
									style="color: rgb(245, 135, 8);">#{prod.price!'' ;m2M2}</SPAN>
									<SPAN>元</SPAN></LI> <#if prod.type == 'body'>
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
									class="con">${prod.duration!''}</SPAN></LI>
								<li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
								<LI style="width: 240px;"><SPAN>单日播放次数：</SPAN> <SPAN
									class="con">${prod.playNumber!''}</SPAN></LI> </#if> <#if prod.type
								== 'video'>
								<LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN
									class="con">${prod.firstNumber!''}</SPAN></LI>
								<LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN
									class="con">${prod.lastNumber!''}</SPAN></LI>
								<li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
								<LI style="width: 240px;"><SPAN>高峰时段占比：</SPAN><SPAN
									class="con">${prod.hotRatio!''}</SPAN></LI> </#if>
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
				style="float: right; position: absolute; left: 790px; top: 0px;">
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
<script type="text/javascript">
								//限定不能选今天之前的日期
								jQuery(function($){ 
						    	 $.datepicker.regional['zh-CN'] = { 
						         minDate: new Date( (new Date()/1000+86400*3)*1000 ),
						        isRTL: false}; 
						        $.datepicker.setDefaults($.datepicker.regional['zh-CN']); 
						  		  });
							</script>
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
