
<div class="ls-2">
							<div class="pg-side">
								<ul class="pg-side-list">
									<li class="pg-side-item">
										<a class="pg-side-item-t aa-icon" href="${rc.contextPath}/order/myTask/1">
											<i class="s-left pg-icon-a a-icon"></i>
											待办事项
										</a>
										
									</li>
									<li class="pg-side-item">
										<a class="pg-side-item-t bb-icon">
											<i class="s-left pg-icon-a b-icon"></i>
											物料管理
										</a>
										<ul class="pg-side-exp-list" style="display:none;">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/supplies/supplies_test">
													上传物料
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/supplies/list">
													物料列表
												</a>
											</li>
											
										</ul>
									</li>
									<li class="pg-side-item">
										<a class="pg-side-item-t cc-icon">
											<i class="s-left pg-icon-a c-icon"></i>
											产品管理
										</a>
										<ul class="pg-side-exp-list">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/product/new">
													产品定义
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/product/list">
													产品列表
												</a>
											</li>
										</ul>
									</li>
									<li class="pg-side-item">
										<a class="pg-side-item-t dd-icon">
											<i class="s-left pg-icon-a d-icon"></i>
											订单管理
										</a>
										<ul class="pg-side-exp-list">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/myOrders/1">
													我的订单
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/allRuningOrders/1">
												     进行中订单
												</a>
											</li>
											<!--<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/finishedOrders/user/1">
												     已完成的订单(广告主)
												</a>
											</li>-->
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/finishedOrders/manager/1">
												     已完成的订单
												</a>
											</li>
										</ul>
									</li>
									
									<li class="pg-side-item">
										<a class="pg-side-item-t ee-icon" href="#" > 
											<i class="s-left pg-icon-a e-icon"></i>
											合同管理
										</a>
										<ul class="pg-side-exp-list">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/contract/contractEnter">
													添加合同
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t"  href="${rc.contextPath}/contract/list">
													合同列表
												</a>
											</li>
										</ul>
									</li>
									
									<li class="pg-side-item">
										<a class="pg-side-item-t ff-icon">
											<i class="s-left pg-icon-a f-icon"></i>
											用户管理
										</a>
										<ul class="pg-side-exp-list">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/list">
													用户列表
												</a>
											</li>
											
										</ul>
									</li>
									<li class="pg-side-item">
										<a class="pg-side-item-t gg-icon">
											<i class="s-left pg-icon-a g-icon"></i>
											媒体管理
										</a>
										<ul class="pg-side-exp-list">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/timeslot/list">
													媒体时段
												</a>
											</li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/schedule/report">
                                                    剩余时段
                                                </a>
                                            </li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/schedule/list">
                                                    排条单
												</a>
											</li>
										</ul>
									</li>
									<li class="pg-side-item">
										<a class="pg-side-item-t hh-icon">
											<i class="s-left pg-icon-a h-icon"></i>
											报表管理
										</a>
										<ul class="pg-side-exp-list">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/schedule/report">
													时段报表
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="#">
													销售报表
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="#">
													广告主报表
												</a>
											</li>
										</ul>
									</li>
								</ul>
							</div>
</div>
<script type="text/javascript">
    function active(menu) {
        $(".pg-side-item").each(function(){
            if (menu) {
                $(this).find(".side-exp-item-t").each(function(){
                    if ($(this).text().trim() == menu) {
                        $(this).addClass("active");
                    }
                });
            }

            var active = $(this).find(".side-exp-item-t.active");
            if (!active[0]) {
                $(this).find(".pg-side-exp-list").hide();
            } else {
                $(this).find(".pg-side-exp-list").show();
            }
        });
    }

    $(document).ready(function(){
        var menu = '<#if menu??>${menu}<#else></#if>';

        $(".pg-side-item-t").click(function(){
            $(this).parent(".pg-side-item").find(".pg-side-exp-list").toggle();
        });
        active(menu);
    });
</script>