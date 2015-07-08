<#import "template/template_blank.ftl" as frame >
<#import "template/proDetail.ftl" as proDetail>
<#global menu="普通产品">
<@frame.html title="普通产品" js=["js/jquery.jcountdown.js","js/jquery.jcountdown.site.js","js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] 
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/compare/auction.css","css/sea.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">
    
    
function compare(pathurl,username,proname){
var productid=$("#productid").val();
var lc=$("#lc").val();
var startTime = $("#startTime").val();
var d = new Date(startTime.replace(/-/g,"/")); 
date = new Date();
var str  = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
var d2 = new Date(str.replace(/-/g,"/")); 
if(lc=="0"){
layer.open({
    		type: 1,
    		title: "您尚未登录",
    		skin: 'layui-layer-rim', 
    		area: ['350px', '350px'], 
    		content:''
    		    + '<form id="loginForm" name="loginForm" class="login-form" action="'+pathurl+'/login" method="POST">'
			   +'<div class="login-item"><input class="login-input input-p gray-input" type="text" placeholder="请输入用户名" id="username" name="username"><span class="login-name-icon icon-position-user"></span> </div>'
                                +'<div class="login-item"><input class="login-input input-p gray-input" type="password" placeholder="请输入密码" id="password" name="password"> <span class="login-name-icon icon-position-user"></span> </div>'
                                +'<div class="login-item s-clear"> <a class="s-right" href="'+pathurl+'/user/find_pwd">忘记密码</a></div>'
                                +'<div class="login-item p-center"><input type="submit" name="submit" value="立即登录" class="login-btn login-btn-size func-submit"/> </div>'
                                +'<div class="login-item p-center"><span>没有账号？</span>  <a href="'+pathurl+'/register">免费注册</a></div></form>'
			});
	layer.msg("请先登录");
   return;
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
        
			layer.open({
    		type: 1,
    		title: "电子合同",
    		skin: 'layui-layer-rim', 
    		area: ['650px', '600px'], 
    		content:''
			   +'<div class = "ad-agreement"> <br/><TEXTAREA id="agreementstr" readonly="true" onkeydown="stop()" name="agreementstr" type="text" cols="85" rows="22" style="margin-left:20px;">'
			   +username+'同意购买'+proname
			   +'\n1.特别提示'
			   +'\n1.1 广告拟合竞价系统中心（以下称“系统中心”）同意按照本协议的规定提供竞价等相关服务（以下称“本服务”）。为获得本服务，服务使用人（以下称“用户”）应当同意本协议的全部条款并按照页面上的提示完成全部的注册程序。'
			   +'\n1.2 一旦注册并使用系统中心提供的本服务，即视为用户已了解并完全同意本条款各项内容，包括系统中心对本协议随时所做的任何修改。'
			   +'</TEXTAREA> </div>'
			   +'<div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdraworder" class="block-btn" onclick="creorder();" value="确认" style="margin:10px 0px -10px 110px;"> </div>'
			});
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
 class="ui-form" method="post" action="${rc.contextPath}/order/confirm?cpdid=${cpdid}"
 enctype="multipart/form-data">
							<div class="product-info s-clear">
								<div class="preview s-left">
									<img src="${rc.contextPath}/imgs/auction.jpg" width="298" height="298">
								</div>
								<div class="product-detail s-left">
									<div class="product-title">
										<h3>${(prod.name)!''}</h3>
									</div>
									<div>
										<div class="product-form">
											
											<div class="range" style="margin-top: 20px;">
												<span>播放次数：<em>${prod.playNumber!''}次</em></span>
												<span>产品周期：<em>${prod.days!''}天</em></span>
						
											</div>
											<div class="" style="margin: 20px 5px;">
                                            <label class="range" style="color: #999;">开播日期:</label> <input
                                                class="ui-input datepicker validate[required,custom[date] layer-tips" 
                                                type="text" name="startTime1"
                                                id="startTime" data-is="isAmount isEnough"
                                                autocomplete="off" disableautocomplete="" tip="可选择3天后日期!">
                                                
                                               	 
                                        </div>
										</div>
										<div class="product-btn" style="margin-top: 30px;">
										<a class="btn-bid" href="javascript:void(0)" onclick="compare('${rc.contextPath}','${username!''}','${prod.name!''}')" >我要购买</a>
											 <input type="hidden" readonly="readonly" name="product.id" id="productId" value="${prod.id!''}"/>
											 <input type="hidden" readonly="readonly" name="supplies.id" id="productId" value="1"/>
										<@security.authorize access="isAuthenticated()">
                                        <input type="hidden" id="lc" value="1"/>	
                                        </@security.authorize>
                                        <@security.authorize access="! isAuthenticated()">
                                         <input type="hidden" id="lc" value="0"/>	
                                        </@security.authorize>
										</div>
									</div>
								</div>
							</div>
</form>
							<div class="product-ins" style="margin-top: 50px;">
								<div class="ins-title">
									<span>商品介绍</span>
								</div>
							</div>
							<div class="product-contain">
								<div class=" color-white-bg fn-clear" style="margin-left: 0px;">
    <DIV class="summary mt10 uplan-summary-div">
        <UL class="uplan-detail-ul">
            <LI style="width: 720px;">
                <SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN>
            </LI>
    <li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN><#if prod.type == 'body'>媒体费：<#else>套餐原价：</#if></SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">#{prod.price!'' ;m2M2}</SPAN>
                <SPAN>元</SPAN>
            </LI>
            <#if prod.type == 'body'>
                <LI style="width: 240px;">
                    <SPAN>制作费：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">#{prod.produceCost!'' ;m2M2}</SPAN>
                    <SPAN>元</SPAN>
                </LI>
            </#if>
            <LI style="width: 240px;">
                <SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN>
            </LI>
    <#if prod.type == 'body'>
        <LI style="width: 240px;">
            <SPAN>线路级别：</SPAN><SPAN class="con"><#if prod.lineLevel??>${prod.lineLevel.nameStr!''}</#if></SPAN>
        </LI>
        <LI style="width: 240px;">
            <SPAN>巴士数量：</SPAN><SPAN class="con">${prod.busNumber!''}</SPAN>
        </LI>
    </#if>

            <#if prod.type == 'video' || prod.type == 'image' || prod.type == 'info'>
            <LI style="width: 240px;">
                <SPAN>时长（秒）：</SPAN><SPAN class="con">${prod.duration!''}</SPAN>
            </LI>
            <li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN>单日播放次数：</SPAN>
                <SPAN class="con">${prod.playNumber!''}</SPAN>
            </LI>
            </#if>
            <#if prod.type == 'video'>
            <LI style="width: 240px;">
                <SPAN>首播次数：</SPAN><SPAN class="con">${prod.firstNumber!''}</SPAN>
            </LI>
            <LI style="width: 240px;">
                <SPAN>末播次数：</SPAN><SPAN class="con">${prod.lastNumber!''}</SPAN>
            </LI>
             <li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width: 240px;">
                <SPAN>高峰时段占比：</SPAN><SPAN class="con">${prod.hotRatio!''}</SPAN>
            </LI>
            </#if>
            <LI style="width: 200px;">
                <SPAN><#if prod.type == 'video' || prod.type == 'image' || prod.type == 'info'>套餐播放天数：<#else>广告展示天数：</#if></SPAN>
                <SPAN class="con">${prod.days!''}天</SPAN>
            </LI>
            <li style="width: 720; border-bottom: 1px solid #F7F7F7"></li>
            <LI style="width:720px;">
                <SPAN>套餐描述：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查详细内容!" onclick="showRemark('${prod.remarks!''}');"  >${substring(prod.remarks!'',0,38)}</a></SPAN>
            </LI>
             

        </UL>
    </DIV>
</div>
							</div>
						</div>
						<div class="ls-3" style="float:right;position:absolute;left:790px;top:0px;">
							<div class="record-sidebar">
								<div class="record-title">
									<label>购买记录（共<label>${logsList?size}</label>次购买）</label>
								</div>
								<div class="record-detail">
									<dl>
										<dt>
											<span class="wd1">时间</span>
											<span class="wd2">详情</span>
											<span class="wd3">状态</span>
										</dt>
										<#list logsList as item>
										<dd>
											<span class="wd1">${item.created?string("yyyy-MM-dd HH:mm:ss")}</span>
											<span class="wd2">
												<i>${hidname(item.userId!'')}</i>
												<div class="line"></div>
												<i>
												</i>
											</span>
											<span class="wd3">
												<i>成交</i>
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
								//限定不能选今天之前的日期
								jQuery(function($){ 
						    	 $.datepicker.regional['zh-CN'] = { 
						         minDate: new Date( (new Date()/1000+86400*3)*1000 ),
						        isRTL: false}; 
						        $.datepicker.setDefaults($.datepicker.regional['zh-CN']); 
						  		  });
							</script>
</@frame.html>
