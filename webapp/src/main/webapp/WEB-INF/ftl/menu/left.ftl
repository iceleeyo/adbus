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
								<@security.authorize ifAnyGranted="advertiser,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
									<li class="pg-side-item">
										<a class="pg-side-item-t aa-icon" style="color:#fff;background:#ff9966;" href="${rc.contextPath}/order/myTask/1">
											<i class="s-left pg-icon-a a-icon" style="background:url(${rc.contextPath}/imgs/sidebar-icons.png) no-repeat -20px 0;"></i>
											待办事项
										</a>
										
									</li>
								 </@security.authorize>	
                                    <@security.authorize ifAnyGranted="advertiser,ShibaOrderManager,BeiguangMaterialManager,ShibaSuppliesManager">
                                    <li class="pg-side-item">
										<a class="pg-side-item-t bb-icon">
											<i class="s-left pg-icon-a b-icon"></i>
											物料管理
										</a>
										<ul class="pg-side-exp-list" style="display:none;">
											
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/supplies/list">
													物料管理
												</a>
											</li>
											
										</ul>
									</li>
                                    </@security.authorize>
									<@security.authorize ifAnyGranted="advertiser,ShibaOrderManager,ShibaFinancialManager">
									<li class="pg-side-item">
										<a class="pg-side-item-t ee-icon" href="#" > 
											<i class="s-left pg-icon-a g-icon"></i>
											合同管理
										</a>
										<ul class="pg-side-exp-list">
									<!-- 	<@security.authorize ifAnyGranted="ShibaOrderManager">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/contract/contractEnter">
													添加合同
												</a>
											</li>
											</@security.authorize>
									 -->
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t"  href="${rc.contextPath}/contract/list">
													合同管理
												</a>
											</li>
										</ul>
									</li>	
									</@security.authorize>
									<li class="pg-side-item">
										<a class="pg-side-item-t cc-icon">
											<i class="s-left pg-icon-a f-icon"></i>
											产品中心
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
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/product/auction">
													竞价产品列表
												</a>
											</li>
											
										</ul>
									</li>
									<@security.authorize ifAnyGranted="advertiser,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
									<li class="pg-side-item">
										<a class="pg-side-item-t dd-icon">
											<i class="s-left pg-icon-a d-icon"></i>
											订单管理
										</a>
										<ul class="pg-side-exp-list">
										<@security.authorize ifAnyGranted="advertiser">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/myOrders/1">
													我的订单
												</a>
											</li>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/product/myAuctionList">
													我的获拍
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
													我参与订单
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
												     已完成订单
												</a>
											</li>
										
										</ul>
									</li>
									 </@security.authorize>	
									

									<@security.authorize ifAnyGranted="ShibaOrderManager,BeiguangScheduleManager">
                                    <#if city.mediaType == 'screen'>
									<li class="pg-side-item">
										<a class="pg-side-item-t gg-icon">
											<i class="s-left pg-icon-a g-icon"></i>
											媒体管理
										</a>
										<ul class="pg-side-exp-list">
											
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
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/timeslot/list">
													时段设置
												</a>
											</li>
										</ul>
									</li>
                                    </#if>
                                    <#if city.mediaType == 'body'>
                                    <li class="pg-side-item">
                                        <a class="pg-side-item-t gg-icon">
                                            <i class="s-left pg-icon-a g-icon"></i>
                                            车身广告
                                        </a>
                                        <ul class="pg-side-exp-list">

                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/list">
                                                    巴士列表
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/lines">
                                                    线路列表
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/models">
                                                    车型列表
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/companies">
                                                    运营公司
                                                </a>
                                            </li>
                                        </ul>
                                    </li>
                                    </#if>
                                    </@security.authorize>
                                    <@security.authorize ifAnyGranted="ShibaOrderManager,ShibaFinancialManager">
                                    <#if city.mediaType == 'screen'>
									<li class="pg-side-item">
										<a class="pg-side-item-t hh-icon">
											<i class="s-left pg-icon-a c-icon"></i>
											报表管理
										</a>
										<ul class="pg-side-exp-list">
                                        <@security.authorize ifAnyGranted="ShibaFinancialManager">
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/daysalesp">
                                                    财务收入
                                                </a>
                                            </li>

                                        </@security.authorize>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/report/day">
				时段报表
												</a>
											</li>

											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/report/dayorderp">
                                                    销售报表
												</a>
											</li>
<#--											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="#">
													广告主报表
												</a>
											</li>-->
										</ul>
									</li>
                                    </#if>
                                    <#if city.mediaType == 'body'>
                                    <li class="pg-side-item">
                                        <a class="pg-side-item-t gg-icon">
                                            <i class="s-left pg-icon-a g-icon"></i>
                                            报表管理
                                        </a>
                                        <ul class="pg-side-exp-list">
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/body/monthp">
                                                    年售出情况
                                                </a>
                                            </li>
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/body/daylinep">
                                                    线路细分
                                                </a>
                                            </li>
                                        </ul>
                                    </li>
                                    </#if>
                                    </@security.authorize>
                                    <@security.authorize ifAnyGranted="advertiser,UserManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
									<li class="pg-side-item">
										<a class="pg-side-item-t ff-icon">
											<i class="s-left pg-icon-a e-icon"></i>
											用户信息
										</a>
										<ul class="pg-side-exp-list">
                                            <@security.authorize ifAnyGranted="UserManager">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/list">
													用户列表
												</a>
											</li>
                                            </@security.authorize>
                                            
                                           <@security.authorize ifAnyGranted="advertiser">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/invoiceList">
													发票列表
												</a>
												 <a class="side-exp-item-t" href="${rc.contextPath}/user/UserQulifi">
													资质管理
												</a>
												
											</li>
											 </@security.authorize>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/qualification">
                                                                                                                                              个人信息
												</a>
											</li>
										</ul>
									</li>	
									
									</@security.authorize>
									
									<@security.authorize ifAnyGranted="advertiser">
                                    <li class="pg-side-item">
                                        <a class="pg-side-item-t aa-icon" href="http://wpa.qq.com/msgrd?v=3&uin=3070339185&site=qq&menu=yes" target="_blank">
                                            <i class="s-left pg-icon-a a-icon" style="background:url(${rc.contextPath}/imgs/cs.png) no-repeat;"></i>
                                            在线客服
                                        </a>
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
            	
                var list = $(this).find(".side-exp-item-t");
                list.each(function(){
                    if ($(this).text().trim() == menu) {
						 curr.find(".pg-side-exp-list").show();
						 $(this).addClass("side-exp-select");		
						 curr.addClass("side-exp-p-select");
						 st=1;		           
                    } 
                });

                //try to find from top level
                if (st == 0 && list.length == 0) {
                    var top = $(this).find(".pg-side-item-t");
                    if (top.text().trim() == menu) {
                        curr.addClass("side-exp-p-select");
                        st = 1;
                    }
                }
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
        //move single sub-menu to top
        $(".pg-side-item").each(function(){
            var list = $(this).find(".pg-side-exp-list .side-exp-item-t");
            if (list.length == 1) {
                var top = $(this).find(".pg-side-item-t");
                if (top) {
                    top.attr("href", $(list[0]).attr("href"));
                    top.find(".pg-icon-a")[0].nextSibling.data=$(list[0]).text();
                    $(this).find(".pg-side-exp-list").remove();
                }
            }
        });

        //open selected menu
        var menu = '<#if menu??>${menu}<#else></#if>';
        $(".pg-side-item-t").click(function(){
            $(this).parent(".pg-side-item").find(".pg-side-exp-list").toggle();
        });
        
        active(menu);
    });
</script>