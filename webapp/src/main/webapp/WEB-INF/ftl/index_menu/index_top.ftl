<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/base.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/header.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/jack.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/jquery-ui/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/jquery-ui-1.8.16.custom.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/secondLevel.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/sift.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/sea.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/index_css/pagination.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/layer.css">
		<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/logMini.css">
			
	</head>
<script type="text/javascript">
    function logout(){
       window.location.href = "${rc.contextPath}/logout";
    }

    $(function() {
        $("#city_dropdown a:not(.selected)").click(function(){
            var cityName = $(this).parents("#ttbar-mycity")[0]?$(this).attr("data-id") : $("#ttbar-mycity a.selected").attr("data-id");
            var media = $(this).parents("#ttbar-media")[0]?$(this).attr("data-id") : $("#ttbar-media a.selected").attr("data-id");
            if (!cityName)
                cityName = '北京';
            if (!media)
                media = 'screen';
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
<style type="text/css">
    .sift-list {  width: 80px;  line-height: 30px;}
    .sift-list a{margin-right: 10px;}

</style>		

				<nav class="menu">
					<ul class="list-line">
						<li><a href="/">首页</a></li>
						<li class="active"><a href="media">媒体产品</a></li>
						<li><a href="jvascript:void(0)">传播效果</a></li>
						<li><a href="/caseMore.html">案例欣赏</a></li>
						<li><a href="jvascript:void(0)">合作伙伴</a></li>
						<li><a href="jvascript:void(0)">关于我们</a></li>
					</ul>
					<div class="s-right s-clear">
								<span class="pg-nav-item s-left" style="padding:0;">您好，</span>
									<span>
                                        <@security.authorize access="isAuthenticated()">
                                        <#if medetype?? && medetype=="screen">
                                        <a class="pg-nav-item s-left" href="${rc.contextPath}/order/myTask/1">
                                        <#else>
                                           <a class="pg-nav-item s-left" href="${rc.contextPath}/busselect/myTask/1">
                                        </#if>
                                                                                                                                   我的账户:
                                            <@security.authentication property="principal.user.firstName" />
                                            <@security.authentication property="principal.user.lastName" />
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
                               
								<!--<a class="pg-nav-item s-left" href="#">
									<i class="icon-msg fsize-12">1</i>
									消息
								</a> -->
								<!--<a class="pg-nav-item s-left" href="#">帮助</a>
								<a class="pg-nav-item s-left" href="#">论坛</a>-->
								<a class="pg-nav-item s-left" href="${rc.contextPath}/message/all">消息<span id="msgNumber" class="layer-tips" style="color:#ff9966"></span></a>
								 <@security.authorize access="isAuthenticated()">
								<a href="javascript:;" class="pg-nav-item s-left" onclick="logout();">[退出]</a>
                                </@security.authorize>
							</div>
				</nav>


		<script type="text/javascript" src="${rc.contextPath}/index_js/jquery-1.11.1.min.js"></script>
		<script src="${rc.contextPath}/index_js/unslider.min.js"></script>
	
		<script src="js/jquery.pagination.js"></script>
		<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer/layer.js"></script>
    	<!--增加lay最新版本-->
		<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
		<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer.onload.js"></script>
		<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>
		<script type="text/javascript">
			
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

			//限时套装Hover事件
			$('.timer .select-item').hover(function(e) {
				/* Stuff to do when the mouse enters the element */
				e.preventDefault();
				$(this).find('.lasttime').css({
					'border-top': '1px solid #ca0d0e',
					'border-right': '1px solid #ca0d0e',
					'border-left': '1px solid #ca0d0e'
				}).end().find('.cost-box').css({
					'border-right': '1px solid #ca0d0e',
					'border-bottom': '1px solid #ca0d0e',
					'border-left': '1px solid #ca0d0e'
				}).end().find('.cost').css(
					'border-bottom', '1px solid #ca0d0e'
				).end().find('.timeline').css(
					'border-bottom', '1px solid #ca0d0e'
				).end().find('.ston').css({
					color: '#fff',
					background: '#ca0d0e'
				});
			}, function(e) {
				/* Stuff to do when the mouse leaves the element */
				e.preventDefault();
				$(this).find('.lasttime').removeAttr('style').end().find('.cost-box').removeAttr('style').end().find('.cost').removeAttr('style').end().find('.timeline').removeAttr('style').end().find('.ston').removeAttr('style');
			});
		});
		
	</script>