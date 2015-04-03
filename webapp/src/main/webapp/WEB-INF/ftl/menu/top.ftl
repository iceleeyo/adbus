<#include "/menu/webapp.ftl" />
<script type="text/javascript">
			function logout(){
			   window.location.href = "/${web}/logout";
			   }
	</script>
<div class="pg-header">
				<div class="pg-header-top">
					<div class="container-12 s-clear">
						<div class="grid-12">
							<div class="s-left ml10">
								<a class="pg-nav-item" href="#">
									<i class="icon icon-app"></i>
									移动客户端
								</a>
							</div>
							<div class="s-right s-clear">
								<span class="pg-nav-item s-left">您好，</span>
								<a class="pg-nav-item-p pg-nav-item-n s-left" href="#">
									<span>${SESSION_U_KEY.username!''}</span>
									<span class="arrow-down"></span>
								</a>
								<div class="pg-nav-dropdown" style="display: none;">
									<div class="pg-dropdown-box">
										<div class="dropdown-account s-clear">
											<div class="account-img-box s-left">
												<a href="">
													<img src="../imgs/default-img-78.png">
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
								<a href="javascript:;" class="pg-nav-item s-left" onclick="logout();">[退出]</a>
								<!--<a class="pg-nav-item s-left" href="#">
									<i class="icon-msg fsize-12">1</i>
									消息
								</a> -->
								<a class="pg-nav-item s-left" href="#">帮助</a>
								<a class="pg-nav-item s-left" href="#">论坛</a>
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
							<a class="phmain-slogan" href="#"></a>
						</div>
						<div class="phmain-nav-b pg-right">
							<ul class="pg-nav">
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="#">首页</a>
								</li>
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t pg-nav-hover" href="#">
										<span>要投广告</span>
										<span class="shdown"></span>
									</a>
									<ul class="pg-dropdown" style="display: none;">
										<li class="pg-dropdown-angle">
											<span></span>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">&nbsp;&nbsp;视频广告</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">&nbsp;&nbsp;图片广告</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">&nbsp;&nbsp;文字广告</a>
										</li>
									</ul>
								</li>
								
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t" href="#">新手指引</a>
								</li>
								<li class="pg-nav-item s-left">
									<a class="pg-nav-item-t pg-nav-hover-us" href="#">
										<span>关于我们</span>
										<span class="shdown"></span>
									</a>
									<ul class="pg-dropdown pg-dropdown-us" style="display: none;">
										<li class="pg-dropdown-angle">
											<span></span>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">管理团队</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">最新动态</a>
										</li>
										<li class="pg-dropdown-li">
											<a class="pg-dropdown-a" href="">招贤纳士</a>
										</li>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
	