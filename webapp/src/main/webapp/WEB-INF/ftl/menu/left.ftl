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
								<@security.authorize ifAnyGranted="ShibaSuppliesManager,advertiser,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
									<li class="pg-side-item">
										<a class="pg-side-item-t aa-icon" style="color:#fff;background:#F45C55;" href="${rc.contextPath}/order/myTask/1">
											<i class="s-left pg-icon-a a-icon" style="background:url(${rc.contextPath}/imgs/sidebar-icons.png) no-repeat -20px 0;"></i>
											待办事项
										</a>
										
									</li>
								 </@security.authorize>	
								<@security.authorize ifAnyGranted="bodysales,bodyFinancialManager,bodyContractManager,bodyScheduleManager">
									<li class="pg-side-item">
										<a class="pg-side-item-t aa-icon" style="color:#fff;background:#F45C55;" href="${rc.contextPath}/busselect/myTask/1">
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
									<@security.authorize ifAnyGranted="advertiser,ShibaOrderManager,ShibaFinancialManager,bodyContractManager">
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
													屏幕广告合同列表
												</a>
											</li>
											<#if city.mediaType == 'body'>
											  <@security.authorize ifAnyGranted="bodyContractManager,contract_list"> 
												<li class="pg-side-exp-item">
													<a class="side-exp-item-t"  href="${rc.contextPath}/contract/bus_contractEnter">
														车辆关联合同
													</a>
												</li>
												</@security.authorize>
											</#if>
										</ul>
									</li>	
									</@security.authorize>
									
									
									<@security.authorize ifNotGranted="UserManager">
										 <#if city.mediaType == 'screen'>
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
											</ul>
										</li>
										 </#if>
									 </@security.authorize>
									
									<@security.authorize ifAnyGranted="ShibaSuppliesManager,advertiser,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager,bodysales,bodyContractManager,bodyFinancialManager,bodyScheduleManager">
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
										<@security.authorize ifAnyGranted="bodysales">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/busselect/myOrders/1">
													我的订单
												</a>
										
										</@security.authorize>	
										
										
										<@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">  
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/allRuningOrders/1">
												     进行中订单
												</a>
											</li>
										</@security.authorize>	
										
										<@security.authorize ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">  
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/busselect/body_allRuningOrders">
												     进行中的订单
												</a>
											</li>
										</@security.authorize>	
										
										<@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/join/1">
													我参与订单
												</a>
											</li>
										</@security.authorize>	
										 <#if city.mediaType == 'body'>
										<@security.authorize ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/busselect/join/1">
													我参与订单
												</a>
											</li>
											</@security.authorize>	
										</#if>
										
											<!--<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/finishedOrders/user/1">
												     已完成的订单(广告主)
												</a>
											</li>-->
											  <#if city.mediaType == 'screen'>
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/finished">
												     已完成订单
												</a>
											</li>
											  </#if>
												  <#if city.mediaType == 'body'>
													</li>
													<li class="pg-side-exp-item">
													<a class="side-exp-item-t" href="${rc.contextPath}/busselect/finished">
													     已完成订单
													</a>
												</li>
											    </#if>
										
										</ul>
									</li>
									 </@security.authorize>	
									

									
                                    <#if city.mediaType == 'screen'>
                                    <@security.authorize ifAnyGranted="bodysales,bodyContractManager,bodyScheduleManager,ShibaSuppliesManager,ShibaOrderManager,BeiguangScheduleManager">
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
										 </@security.authorize>	
                                    </#if>
                                    
                                    
                                    <#if city.mediaType == 'body'>
                                    
                                     <@security.authorize ifAnyGranted="bodysales,bodyContractManager,contract_input,contract_list,contract_search,contract_buy">  
                                    <li class="pg-side-item">
                                        <a class="pg-side-item-t gg-icon">
                                            <i class="s-left pg-icon-a g-icon"></i>
                                            合同管理
                                        </a>
                                        <ul class="pg-side-exp-list">
                             			<@security.authorize ifAnyGranted="bodyScheduleManager">  
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/busselect/public_bodyContracts">
												    施工单列表
												</a>
											</li>
									</@security.authorize>	
									
										   <li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/busselect/public_order">
												    线路订购
												</a>
											</li>
											  <li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/busselect/publicOrder_list">
												    网络订单列表
												</a>
											</li>
                               				<@security.authorize ifAnyGranted="bodyContractManager,contract_input">    
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/busselect/offContract_enter">
												    创建合同
												</a>
											</li>
											  </@security.authorize>
											  <@security.authorize ifAnyGranted="bodyContractManager,contract_list"> 
											
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/busselect/offContract_list">
												    车身合同列表
												</a>
											</li>
											
											</@security.authorize>
											<@security.authorize ifAnyGranted="bodyContractManager,contract_search"> 
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/bus/contractSearch">
												    订单及车辆查询
												</a>
											</li>
											</@security.authorize>
											<@security.authorize ifAnyGranted="bodyContractManager,contract_buy">
											 <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/api/wantbuy">
                                                    媒体定购
                                                </a>
                                            </li>
                                            </@security.authorize>
										  </ul>
                                    </li>
                                    </@security.authorize>	
                                    
                                 <@security.authorize ifAnyGranted="meijie_order,meijie_error,meijie_offline,meijie_bus,meijie_busChange,meijie_busModel,meijie_company">     
                                 <li class="pg-side-item">
                                        <a class="pg-side-item-t gg-icon">
                                            <i class="s-left pg-icon-a g-icon"></i>
                                            媒介管理
                                        </a>
                                        <ul class="pg-side-exp-list">
                                         <@security.authorize ifAnyGranted="meijie_order">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/busselect/publishLine_list">
												    订单列表
												</a>
											</li>
											  </@security.authorize>
											   <@security.authorize ifAnyGranted="meijie_error">	
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/bus/mistake_handle">
												    上下刊错误处理
												</a>
											</li>
											 </@security.authorize>
											   <@security.authorize ifAnyGranted="meijie_offline">	
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" target="_Blank" href="${rc.contextPath}/bus/bus_offShelf">
												    车辆下刊
												</a>
											</li>
											 </@security.authorize>
											   <@security.authorize ifAnyGranted="meijie_bus">	
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/list">
                                                    巴士列表
                                                </a>
                                            </li>
                                             </@security.authorize>
                                             
                                                <@security.authorize ifAnyGranted="meijie_busline">	
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/api/lines">
                                                    线路列表
                                                </a>
                                            </li>
                                             </@security.authorize>
                                             
											   <@security.authorize ifAnyGranted="meijie_busChange">	
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/busUpdate_query">
                                                    车辆变更查询
                                                </a>
                                            </li>
                                             </@security.authorize>
											   <@security.authorize ifAnyGranted="meijie_busModel">	
                                          
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/models">
                                                    车型列表
                                                </a>
                                            </li>
                                             </@security.authorize>
											   <@security.authorize ifAnyGranted="meijie_company">	
                                          
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/bus/companies">
                                                    运营公司
                                                </a>
                                            </li>
                                                </@security.authorize>
                                        </ul>
                                    </li>
                                     </@security.authorize>	
                                     
	                                     <#if city.mediaType == 'body'>
	                                       <@security.authorize ifAnyGranted="bodyScheduleManager,bodysales,bodyContractManager,contract_input,contract_list,contract_search,contract_buy,meijie_order,meijie_error,meijie_offline,meijie_bus,meijie_busChange,meijie_busModel,meijie_company"> 
	                                    <li class="pg-side-item">
	                                        <a class="pg-side-item-t ee-icon">
	                                            <i class="s-left pg-icon-a e-icon"></i>
	                                            媒体推荐
	                                        </a>
	                                        <ul class="pg-side-exp-list">
	
	                                            <li class="pg-side-exp-item">
	                                                <a class="side-exp-item-t" href="${rc.contextPath}/api/landmarkM_lines">
	                                                   地标匹配
	                                                </a>
	                                            </li>
	                                            
	                                            <li class="pg-side-exp-item">
	                                                <a class="side-exp-item-t" href="${rc.contextPath}/api/trackM_lines">
	                                                   轨迹匹配
	                                                </a>
	                                            </li>
	                                            
	                                            <li class="pg-side-exp-item">
	                                                <a class="side-exp-item-t" href="${rc.contextPath}/api/media_lines">
	                                                   相关媒体
	                                                </a>
	                                            </li>
	                                        </ul>
	                                    </li>
	                                     </@security.authorize>	
	                                     </#if>
	                                     
                                    </#if>
                                    
                                    
                                    
                                    <@security.authorize ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,contract_input,contract_list,contract_search,contract_buy">
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
                                  <@security.authorize ifAnyGranted="bodyContractManager,report_monthp,report_daylinep">  
                                    <li class="pg-side-item">
                                        <a class="pg-side-item-t gg-icon">
                                            <i class="s-left pg-icon-a g-icon"></i>
                                            报表管理
                                        </a>
                                        <ul class="pg-side-exp-list">
                                         <@security.authorize ifAnyGranted="bodyContractManager,report_monthp">  
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/body/monthp">
                                                    年售出情况
                                                </a>
                                            </li>
                                            </@security.authorize>
                                             <@security.authorize ifAnyGranted="bodyContractManager,report_daylinep">
                                            <li class="pg-side-exp-item">
                                                <a class="side-exp-item-t" href="${rc.contextPath}/report/body/daylinep">
                                                    线路细分
                                                </a>
                                            </li>
                                            </@security.authorize>
                                        </ul>
                                    </li>
                                     </@security.authorize>
                                    </#if>
                                    </@security.authorize>
                                    <@security.authorize ifAnyGranted="sys_userList,sys_roleManager,bodysales,bodyContractManager,ShibaSuppliesManager,advertiser,UserManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
									<li class="pg-side-item">
										<a class="pg-side-item-t ff-icon">
											<i class="s-left pg-icon-a e-icon"></i>
											用户信息
										</a>
										<ul class="pg-side-exp-list">
                                            <@security.authorize ifAnyGranted="UserManager,sys_userList">
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/list">
													用户列表
												</a>
											</li>
                                            </@security.authorize>
                                             <@security.authorize ifAnyGranted="UserManager,sys_roleManager">
                                              <#if city.mediaType == 'body'>
		                                            <li class="pg-side-exp-item">
														<a class="side-exp-item-t" href="${rc.contextPath}/user/role_list">
															角色管理
														</a>
													</li>
											 </#if>
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
											<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/user/edit_pwd">
                                                    修改密码                                                                                                                                                                                        
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