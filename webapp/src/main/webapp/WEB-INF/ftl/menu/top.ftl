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
<div class="pg-header">
				<div class="pg-header-top">
					<div class="container-12 s-clear">
						<div class="grid-12 city-dropdown">
                            <ul class="fl">
<#--<@security.authorize access="isAuthenticated()">-->
                                <li class="dorpdown" id="ttbar-mycity">
                                    <div class="dt cw-icon ui-areamini-text-wrap" style="">
                                        <i class="ci-right"><s>◇</s></i>
                                        <#if city??>
                                            <span class="ui-areamini-text" data-id="${city.name}" title="${city.name}">${city.name}</span>
                                        <#else>
                                            <span class="ui-areamini-text" data-id="${cities[0].name!''}" title="${cities[0].name!''}">${cities[0].name!''}</span>
                                        </#if>
                                    </div>
                                    <div class="dd dorpdown-layer">
                                        <div class="dd-spacer"></div>
                                        <div class="ui-areamini-content-wrap" style="left: auto;">
                                            <div class="ui-areamini-content">
                                                <div class="ui-areamini-content-list" id="city_dropdown">
                                                    <#list cities as c>
                                                        <div class="item">
                                                            <a data-id="${c.name}" href="javascript:void(0)" <#if city?? && city.name == c.name>class="selected"</#if>>${c.name!''}</a>
                                                        </div>
                                                    </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                                <li class="dorpdown" id="ttbar-media">
                                    <div class="dt cw-icon ui-areamini-text-wrap" style="">
                                        <i class="ci-right"><s>◇</s></i>
                                        
                                        
                                    <#if city??>
                                    
                                       <#if medetype?? && medetype=="screen">
                                        <a class="selected ui-areamini-text" data-id="${city.mediaType}" title="${city.mediaType.typeName}">屏幕广告</a>
                                        <#else>
                                        <a class="selected ui-areamini-text" data-id="${city.mediaType}" title="${city.mediaType.typeName}">车身广告</span>
                                        </#if>
                                    <#else>
                                      <#if medetype?? && medetype=="screen">
                                        <a class="selected ui-areamini-text" data-id="${city.mediaType}" title="${city.mediaType.typeName}">屏幕广告</a>
                                        <#else>
                                        <a class="selected ui-areamini-text" data-id="${city.mediaType}" title="${city.mediaType.typeName}">车身广告</a>
                                        </#if>
                                    </#if>
                                    </div>
                                   <#--<div class="dd dorpdown-layer">
                                        <div class="dd-spacer"></div>
                                        <div class="ui-areamini-content-wrap" style="left: auto;">
                                            <div class="ui-areamini-content">
                                                <div class="ui-areamini-content-list" id="city_dropdown">
                                                <#list medias as m>
                                                    <div class="item">
                                                        <a data-id="${m.mediaType}" href="javascript:void(0)" <#if city?? && city.id == m.id> cityid="${city.id}" mid="${m.id}" class="selected"</#if>>${m.mediaType.typeName!''}</a>
                                                    </div>
                                                </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>--> 
                                </li>
<#--</@security.authorize>-->
                            </ul>
							<!-- 手机图标<div class="s-left ml10">
								<a class="pg-nav-item" href="#" style="padding-top: 8px;">
									<i class="icon icon-app"></i>
									
								</a>
							</div> -->
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
						</div>
					</div>
				</div>
				<div class="pg-header-main">
					<div class="container-12 s-clear">
						<div class="phmain-logo-b pg-left">
							<a class="phmain-logo" href="#"></a>
						</div>
						<div class="phmain-slogan-b pg-left ml20">
							<a class="phmain-slogan" href="${rc.contextPath}"></a>
						</div>
						<div class="phmain-nav-b pg-right">
							<ul class="pg-nav">
								<li class="pg-nav-item s-left">
									 <#if city??>
                                    <#if  medetype?? && medetype=="screen">
                                       <a class="pg-nav-item-t" href="${rc.contextPath}/index">首页</a>
                                        <#else>
                                       <a class="pg-nav-item-t" href="${rc.contextPath}/index">首页</a>
                                        </#if>
                                    <#else>
                                      <#if medetype?? && medetype=="screen">
                                       <a class="pg-nav-item-t" href="${rc.contextPath}/index">首页</a>
                                        <#else>
                                        <a class="pg-nav-item-t" href="${rc.contextPath}/index">首页</a>
                                        </#if>
                                    </#if>
								</li>
								
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="${rc.contextPath}/screen">移动电视</a>
								</li>
								<!--
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="${rc.contextPath}/body">车身媒体</a>
								</li>
								-->
								
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t pg-nav-hover-us" href="${rc.contextPath}/product/sift_bus">
										<span>车身媒体</span>
										<#--<span class="shdown"></span>-->
									</a>
									
								</li>
                               
                                <li class="pg-nav-item s-left">
                                    <a class="pg-nav-item-t" href="${rc.contextPath}/about-me">关于我们</a>
                                </li>

							</ul>
						</div>
					</div>
				</div>
			</div>
			
			
			
<script type="text/javascript">		
			$(document).ready(
					function() {
					   $.ajax({
							url : "${rc.contextPath}/message/unread",
							type : "GET",
							success : function(data) {
								var msgNumber = Number(data);
								if(msgNumber > 0){
									$("#msgNumber").html("["+data+"]");
									$("#msgNumber").attr("tip","您有["+data+"]个未读消息!"); 
									  bindLayerMouseOver();
								}	
							}
						}, "text");
					}
		);
</script>
			
	