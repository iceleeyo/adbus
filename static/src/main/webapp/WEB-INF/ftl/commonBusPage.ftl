<#import "template/template_buy.ftl" as frame > <#global menu="定价产品">
<@frame.html title="定价产品"
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
			   +'<iframe  style="width:99%;height:90%" src="${rc.contextPath}/user/busContract_templete?productid='+proid+'"/><div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdraworder" class="block-btn" onclick="creorder();" value="确认" style="margin:10px 0px -10px 110px;"> </div>'
			});
		}
}

	function creorder() {
		$.ajax({
		    			url:"${rc.contextPath}/product/buyBodyPro/"+(${prod.id!''}),
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:{},
		    			success:function(data){
		    				if (data.left == true) {
		    					layer.msg(data.right);
		    					    $("#subWithdraworder").attr("disabled",true);
     								  $("#subWithdraworder").css("background-color","#85A2AD");
		    					   var uptime = window.setTimeout(function(){
				                        window.location.href="${rc.contextPath}/product/busOrderV2_list/my";
				                        clearTimeout(uptime);
				                    },1500);
		    					
		    				} else {
		    					layer.msg(data.right,{icon: 5});
		    				}
		    			}
		       });  
		       
		       
   
     //  $('#userForm2').submit();
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
</style>

<script type="text/javascript">
function bu(txtObj) {
	txtObj.value = Number(txtObj.value).toFixed(2);
}
</script>

<div class="pg-container">
<div class="navbuy side-nav">
			<div class="logo"></div>
			<div class="de-code">
				<img src="${rc.contextPath}/index_img/pic1.png" height="100" width="100">
			</div>
			<ul class="navibar">
				<li><a href="/">首页</a></li>
				<li><a href="${rc.contextPath}/secondLevelPage">移动视频</a></li>
				<li class="active"><a
					href="${rc.contextPath}/secondLevelPageBus">车身媒体</a></li>
				<li><a href="/caseMore.html">案例欣赏</a></li>
			</ul>
			<div class="markble">
				<p>世界在你脚下，巴士一路随行</p>
				<p>北巴出品</p>
				<p>北京公交媒体</p>
			</div>
</div>
	<div class="pg-container-main">
		<div class="container-12 mt10 s-clear">
			<div class="ls-9">
				<form data-name="withdraw" name="userForm2" id="userForm2"
					class="ui-form" method="post"
					action="${rc.contextPath}/order/confirm?cpdid=${prod.id!''}"
					enctype="multipart/form-data">
					<div class="product-info s-clear">
						<div class="preview s-left">
							<#if (medetype)=="body"> <img src="${rc.contextPath}/imgs/t8.jpg"
								class="comBusImg"> <#elseif (medetype)=="screen"> <#if
							(prod.type)=="info"> <img src="${rc.contextPath}/imgs/info.png"
								class="comBusImg"> <#elseif (prod.type)=="image"> <img
								src="${rc.contextPath}/imgs/img.png" class="comBusImg">
							<#elseif (prod.type)=="video"> <img
								src="${rc.contextPath}/imgs/video.png" class="comBusImg">
							</#if> </#if>
						</div>
						<div class="product-detail s-left">
							<div class="product-title">
								<h3>${(prod.name)!''}</h3>
							</div>
							<div>
								<div class="product-form">
									<div class="product-intro" style="margin-left: 5px;">
										<div class="price s-clear">
											<span>套餐价格：</span> <span class="fsize-24 t-red"><em>¥</em>
												#{prod.price!'' ;m2M2} </span>
										</div>
									</div>
									<div class="range">
										<span>套餐描述：<em>${prod.remarks!''}</em></span>
									</div>
									<div class="" style="margin: 10px 5px;">
										<label class="range" style="color: #999;">支付金额:</label> <input
											class="ui-input  layer-tips" type="text" name="startTime1"
											id="startTime" tip="支付金额">


									</div>
								</div>
								<div class="product-btn" style="float: right;">
									<a class="btn-bid" href="javascript:void(0)"
										onclick="compare('${rc.contextPath}',${prod.id!''})">我要购买</a>
									<input type="hidden" readonly="readonly" name="product.id"
										id="productId" value="${prod.id!''}" /> <input type="hidden"
										readonly="readonly" name="supplies.id" value="1" /> <input
										type="hidden" readonly="readonly" name="token"
										value="${token!''}" /> <@security.authorize
									access="isAuthenticated()"> <input type="hidden" id="lc"
										value="1" /> </@security.authorize> <@security.authorize
									access="! isAuthenticated()"> <input type="hidden" id="lc"
										value="0" /> </@security.authorize>
								</div>
							</div>
						</div>
					</div>
				</form>
				<div class="bidPath">
					<div class="lc" style="margin-left: 50px;">
						<span>购&nbsp;&nbsp;&nbsp;买</span><br> <span>流&nbsp;&nbsp;&nbsp;程</span>
					</div>
					<div class="flow"
						style="overflow: hidden; width: 700px; padding-left: 20px;">
						<div class="lc-flow" style="width: 800px;">
							<ul>
								<li class="item01 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">用户账号当前京豆大于0</div> <span class="num">01/</span>
									<span class="mark"></span> <span class="name">下订单</span>
								</li>
								<li class="item02 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">保证竞拍结束时出价最高，获得竞拍商品</div> <span class="num">02/</span>
									<span class="mark"></span> <span class="name">支付及绑定物料</span>
								</li>
								<li class="item03 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">填写订单信息，提交订单</div> <span class="num">03/</span>
									<span class="mark"></span> <span class="name">审核物料及资质</span>
								</li>
								<li class="item04 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">支付已提交的订单</div> <span class="num">04/</span>
									<span class="mark"></span> <span class="name">广告排期</span>
								</li>
								<li class="item05 ui-accordion-item"
									style="width: 120px; overflow: hidden;">
									<div class="status">支付成功后等待收货，竞拍完成</div> <span class="num">05/</span>
									<span class="mark"></span> <span class="name">广告播出</span>
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
						<p>1.请您仔细阅读竞拍商品信息页面中的具体说明，对商品要求较高者请您慎重购买；</p>
						<p>2.订单提交成功后请在24小时内进行支付，否则系统将取消您的订单；</p>
						<p>3.订单支付后，尽快提交物料及相关资质，如有需要及时联系客服咨询；</p>
						<p>4.所售商品正常提供发票，详情请参见帮助中心的发票制度说明；</p>
					</div>
				</div>


				<div class="product-ins" style="margin-top: 20px;">
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
								<LI style="width: 720px;"><SPAN>套餐描述：</SPAN><SPAN
									class="con">${prod.remarks!''}</SPAN></LI>
								<iframe style="height: 60%; width: 90%;" scrolling="no"
									frameborder="no" src="/product/showProV2Detail/${prod.id!''}" />
							</UL>
						</DIV>
					</div>
				</div>
			</div>
			<div class="ls-3"
				style="float: right; position: absolute; left: 900px; top: 0px; background: #fff;">
				<div class="record-sidebar">
					<div class="record-title">
						<label>购买记录（共<label>${logCount!''}</label>次购买）
						</label>
					</div>
					<div class="record-detail">
						<dl>
							<dt>
								<span class="wd1">时间</span> <span class="wd2">详情</span> <span
									class="wd3">状态</span>
							</dt>
							<#list logsList as item>
							<dd>
								<span class="wd1">${item.created?string("yyyy-MM-dd HH:mm:ss")}</span>
								<span class="wd2"> <i>${hidname(item.userId!'')}</i>
									<div class="line"></div> <i> ￥#{item.price!'' ;m2M2} </i>
								</span> <span class="wd3"> <i>成交</i>
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
</@frame.html>
