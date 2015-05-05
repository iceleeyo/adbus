<style type="text/css">
.side-exp-select {
  cursor: pointer;
  color: #fff;
  background: #7fd4f3;
}

.side-exp-p-select .pg-side-item-t {
  color:#fff!important;
  background:#2da7e0;
}
</style>
<div class="ls-2">
							<div class="pg-side">
								<ul class="pg-side-list">
									<li class="pg-side-item">
										<a class="pg-side-item-t aa-icon" style="color:#fff;background:#ff9966;" href="${rc.contextPath}/order/myTask/1">
											<i class="s-left pg-icon-a a-icon" style="background:url(${rc.contextPath}/imgs/sidebar-icons.png) no-repeat -20px 0;"></i>
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
												<a class="side-exp-item-t" href="${rc.contextPath}/supplies/new">
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
									<@security.authorize ifAnyGranted="ShibaOrderManager">
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
									</@security.authorize>									
									<li class="pg-side-item">
										<a class="pg-side-item-t cc-icon">
											<i class="s-left pg-icon-a c-icon"></i>
											产品管理
										</a>
										<ul class="pg-side-exp-list">
										<@security.authorize ifAnyGranted="ShibaOrderManager">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/product/new">
													产品定义
												</a>
											</li>
										</@security.authorize>
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
										<@security.authorize ifAnyGranted="normaluser,advertiser">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/myOrders/1">
													我的订单
												</a>
											</li>
										</@security.authorize>	
										
										<@security.authorize ifAnyGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">  
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/allRuningOrders/1">
												     进行中订单
												</a>
											</li>
										</@security.authorize>	
										
										<@security.authorize ifAnyGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/join/1">
													我参与的订单
												</a>
											</li>
										</@security.authorize>	
											<!--<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/finishedOrders/user/1">
												     已完成的订单(广告主)
												</a>
											</li>-->
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/finished">
												     已完成的订单
												</a>
											</li>
										</ul>
									</li>
									

									<@security.authorize ifAnyGranted="ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">  

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
												<a class="side-exp-item-t" href="${rc.contextPath}/report/day">
													剩余时段报表
												</a>
											</li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/hour">
                                                    三天时段报表
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/dayp">
                                                    剩余时段比例
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/hourp">
                                                    单日时段比例
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/monthp">
                                                    全年时段比例
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/wow">
                                                    时段两周同比
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/mom">
                                                    时段月同比
                                                </a>
                                            </li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/report/dayorderp">
                                                    销售报表
												</a>
											</li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/dayindustryp">
                                                    行业细分
                                                </a>
                                            </li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="#">
													广告主报表
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
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/enter">
													角色添加
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/invoice">
													发票信息
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/qualification">
													资质信息
												</a>
											</li>
										</ul>
									</li>	
									
									</@security.authorize>								
								</ul>
							</div>
</div>
<script type="text/javascript">
    function active(menu) {
        $(".pg-side-item").each(function(){
            if (menu) {
            var curr=$(this);
            	var st=0;
            	
                $(this).find(".side-exp-item-t").each(function(){
                    if ($(this).text().trim() == menu) {
						 curr.find(".pg-side-exp-list").show();
						 $(this).addClass("side-exp-select");		
						 curr.addClass("side-exp-p-select");
						 st=1;		           
                    } 
                });
                if(st==0){
              	  curr.find(".pg-side-exp-list").hide();
                }
                
            }

            var active = $(this).find(".side-exp-item-t.active");
            if (!active[0]) {
               // $(this).find(".pg-side-exp-list").hide();
            } else {
              //  $(this).find(".pg-side-exp-list").show();
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