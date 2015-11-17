<style type="text/css">
.side-exp-select {
	cursor: pointer;
	color: #fff;
	background: #7fd4f3;
}

.side-exp-p-select .pg-side-item-t {
	color: #fff !important;
	background: #2da7e0;
}
</style>
<div class="ls-2">
	<div class="pg-side">
		<ul class="pg-side-list">
			<@security.authorize
			ifAnyGranted="ShibaSuppliesManager,advertiser,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
			<li class="pg-side-item"><a class="pg-side-item-t aa-icon"
				
				href="${rc.contextPath}/order/myTask/1"> <i
					class="s-left pg-icon-a a-icon"></i> 待办事项
			</a></li> </@security.authorize> <@security.authorize
			ifAnyGranted="bodysales,bodyFinancialManager,bodyContractManager,bodyScheduleManager">
			<li class="pg-side-item"><a class="pg-side-item-t aa-icon"
				style="color: #fff; background: #F45C55;"
				href="${rc.contextPath}/busselect/myTask/1"> <i
					class="s-left pg-icon-a a-icon"></i> 待办事项
			</a></li> </@security.authorize> 
				<@security.authorize ifAnyGranted="bodyOnlineManager,advertiser">
			<li class="pg-side-item">
			      <a class="pg-side-item-t aa-icon"  href="${rc.contextPath}/carbox/carTask">
					 <i class="s-left pg-icon-a d-icon"></i> 车身网上订单
			       </a>
			</li> 
			</@security.authorize> 
			<@security.authorize
			ifAnyGranted="advertiser,ShibaOrderManager,BeiguangMaterialManager,ShibaSuppliesManager">
			<li class="pg-side-item"><a class="pg-side-item-t bb-icon">
					<i class="s-left pg-icon-a b-icon"></i> 物料管理
			</a>
				<ul class="pg-side-exp-list" style="display: none;">

					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/supplies/list"> 物料管理 </a></li>

				</ul></li> </@security.authorize> <@security.authorize
			ifAnyGranted="advertiser,ShibaOrderManager,ShibaFinancialManager,bodyContractManager">
			<li class="pg-side-item"><a class="pg-side-item-t ee-icon"
				href="#"> <i class="s-left pg-icon-a g-icon"></i> 合同管理
			</a>
				<ul class="pg-side-exp-list">
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/contract/list"> 屏幕广告合同 </a></li>
				</ul></li> </@security.authorize> <@security.authorize
			ifAnyGranted="ShibaOrderManager">
			<li class="pg-side-item"><a class="pg-side-item-t cc-icon">
					<i class="s-left pg-icon-a h-icon"></i> 产品中心
			</a>
				<ul class="pg-side-exp-list">
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/product/new"> 产品定义 </a></li>
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/product/list"> 产品列表 </a></li>
				</ul></li> </@security.authorize> <@security.authorize
			ifAnyGranted="ShibaSuppliesManager,advertiser,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager,bodysales,bodyContractManager,bodyFinancialManager,bodyScheduleManager">
			<li class="pg-side-item"><a class="pg-side-item-t dd-icon">
					<i class="s-left pg-icon-a d-icon"></i> 订单管理
			</a>
				<ul class="pg-side-exp-list">
					<@security.authorize ifAnyGranted="advertiser"> <#if city.mediaType
					== 'screen'>
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/order/myOrders/1"> 我的订单 </a></li>
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/product/myAuctionList"> 我的获拍 </a></li> </#if>
					<#if city.mediaType == 'body'>
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/product/productV2_list"> 车身套餐列表 </a></li>
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/product/busOrderV2_list/my"> 我的订单 </a></li>
					</#if> </@security.authorize> <@security.authorize
					ifAnyGranted="bodysales">
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/busselect/myOrders/1"> 我的订单 </a>
						</@security.authorize> <@security.authorize
						ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
					
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/order/allRuningOrders/1"> 进行中订单 </a></li>
					</@security.authorize> <@security.authorize
					ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/busselect/body_allRuningOrders">
							进行中的订单 </a></li> </@security.authorize> <@security.authorize
					ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/order/join/1"> 我参与订单 </a></li>
					</@security.authorize> <#if city.mediaType == 'body'>
					<@security.authorize
					ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/busselect/join/1"> 我参与订单 </a></li>
					</@security.authorize> </#if>

					<!--<li class="pg-side-exp-item">
												<a class="side-exp-item-t" href="${rc.contextPath}/order/finishedOrders/user/1">
												     已完成的订单(广告主)
												</a>
											</li>-->
					<#if city.mediaType == 'screen'>
					<li class="pg-side-exp-item"><a class="side-exp-item-t"
						href="${rc.contextPath}/order/finished"> 已完成订单 </a></li> </#if> <#if
					city.mediaType == 'body'></li>
			<li class="pg-side-exp-item"><a class="side-exp-item-t"
				href="${rc.contextPath}/busselect/finished"> 已完成订单 </a></li> </#if>

		</ul>
		</li> </@security.authorize> <#if city.mediaType == 'screen'>
		<@security.authorize
		ifAnyGranted="bodysales,bodyContractManager,bodyScheduleManager,ShibaSuppliesManager,ShibaOrderManager,BeiguangScheduleManager">
		<li class="pg-side-item"><a class="pg-side-item-t gg-icon"> <i
				class="s-left pg-icon-a f-icon"></i> 媒体管理
		</a>
			<ul class="pg-side-exp-list">

				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/schedule/report"> 剩余时段 </a></li>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/schedule/list"> 排条单 </a></li>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/schedule/mediaInventory"> 媒体库存查询 </a></li>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/timeslot/list"> 时段设置 </a></li>
			</ul></li> </@security.authorize> </#if> <@security.authorize
		ifAnyGranted="bodyContractManager,sale_packageDesign,sale_packageList,sale_packageOrder">
		<li class="pg-side-item"><a class="pg-side-item-t gg-icon"> <i
				class="s-left pg-icon-a i-icon"></i> 套餐管理
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize
				ifAnyGranted="bodyContractManager,sale_packageDesign">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/product/newBodyPro"> 车身套餐定义 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="bodyContractManager,sale_packageList">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/product/productV2_list"> 车身套餐列表 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="bodyContractManager,sale_packageOrder">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/product/busOrderV2_list/all"> 车身订单列表 </a></li>
				</@security.authorize>
			</ul></li> </@security.authorize> <@security.authorize
		ifAnyGranted="contract_input,contract_list,contract_buy">
		<li class="pg-side-item"><a class="pg-side-item-t gg-icon"> <i
				class="s-left pg-icon-a g-icon"></i> 合同管理
		</a>
			<ul class="pg-side-exp-list">
				<#--<@security.authorize ifAnyGranted="bodyScheduleManager">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					target="_Blank"
					href="${rc.contextPath}/busselect/public_bodyContracts"> 施工单列表
				</a></li> </@security.authorize>

				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					target="_Blank" href="${rc.contextPath}/busselect/public_order">
						线路订购 </a></li>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					target="_Blank" href="${rc.contextPath}/busselect/publicOrder_list">
						网络订单列表 </a></li> --> <@security.authorize
				ifAnyGranted="bodyContractManager,contract_input">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/offContract_enter"> 创建合同 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="bodyContractManager,contract_list">

				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/offContract_list"> 车身合同列表 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="bodyContractManager,contract_buy">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/wantbuy"> 媒体定购 </a></li>
				</@security.authorize>
			</ul></li> </@security.authorize> <@security.authorize
		ifAnyGranted="report_airmedia,meijie_online,meijie_onlinecount,meijie_change,meijie_offline,meijie_search,meijie_error,meijie_linecheck">
		<li class="pg-side-item"><a class="pg-side-item-t gg-icon"> <i
				class="s-left pg-icon-a f-icon"></i> 媒介管理
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize ifAnyGranted="meijie_online">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/publishLine_list"> 上刊发布 </a></li>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/befLockLine"> 锁定线路 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="meijie_onlinecount">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/orders"> 上刊发布统计 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="report_airmedia">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/use"> 空媒体查询 </a></li> </@security.authorize>
				<@security.authorize ifAnyGranted="meijie_change">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/list_changeDate"> 调刊补刊 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="meijie_offline">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/bus_offShelf"> 车辆下刊 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="meijie_search">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/busAndOrderSearch"> 车辆及订单查询 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="meijie_error">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/mistake_handle"> 上下刊错误处理 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="meijie_servicebus">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/list_sales"> 业务车辆查询 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="meijie_linecheck">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/linesCheck"> 线路核实 </a></li>
				</@security.authorize>

			</ul></li> </@security.authorize> <@security.authorize
		ifAnyGranted="res_busmanage,res_busquery,res_linemanage,res_linequery,res_busChange,res_lineChange,res_buschangecount,res_model,res_company">
		<li class="pg-side-item"><a class="pg-side-item-t ee-icon"> <i
				class="s-left pg-icon-a k-icon"></i> 资源管理
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize ifAnyGranted="res_busmanage">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/mlist"> 车辆管理 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="res_busquery">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/list"> 车辆查询 </a></li> </@security.authorize>
				<@security.authorize ifAnyGranted="res_linemanage">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/linesManage"> 线路管理 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="res_linequery">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/lines"> 线路查询 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="res_buschangecount">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/adJustLog"> 调车统计 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="res_busChange">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/busUpLog_list"> 车辆变更历史 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="res_lineChange">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/lineUpLog_list"> 线路变更历史 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="res_model">

				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/models"> 车型管理 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="res_company">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/bus/companies"> 营销中心 </a></li>
				</@security.authorize>
			</ul></li> </@security.authorize> <@security.authorize
		ifAnyGranted="check_remind,check_adquery,check_matchbusquery,check_dailycheck">
		<li class="pg-side-item"><a class="pg-side-item-t ee-icon"> <i
				class="s-left pg-icon-a m-icon"></i> 检查部门 
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize ifAnyGranted="check_remind">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/reminder"> 到期提示 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="check_adquery">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/adquery"> 广告查询 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="check_matchbusquery">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/matchbusquery"> 配车查询 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="check_dailycheck">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/busselect/dailycheck"> 日常检查 </a></li>
				</@security.authorize>
			</ul></li> </@security.authorize> <@security.authorize
		ifAnyGranted="landmatch,trackmatch,relatemedia">
		<li class="pg-side-item"><a class="pg-side-item-t ee-icon"> <i
				class="s-left pg-icon-a j-icon"></i> 媒介推荐
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize ifAnyGranted="landmatch">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/landmarkM_lines"> 地标匹配 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="trackmatch">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/trackM_lines"> 轨迹匹配 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="relatemedia">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/api/media_lines"> 相关媒体 </a></li>
				</@security.authorize>
			</ul></li> </@security.authorize> <@security.authorize
		ifAnyGranted="report_monthcount,report_meusersumary,report_yearsalecount,report_line">
		<li class="pg-side-item"><a class="pg-side-item-t gg-icon"> <i
				class="s-left pg-icon-a l-icon"></i> 车身报表管理
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize ifAnyGranted="report_monthcount">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/report/publishCountM"> 月发布统计 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="report_meusersumary">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/report/public/MediaUsageCountM"> 媒体使用汇总
				</a></li> </@security.authorize> <@security.authorize
				ifAnyGranted="report_yearsalecount">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/report/body/monthp"> 年售出情况 </a></li>
				</@security.authorize> <@security.authorize
				ifAnyGranted="report_line">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/report/body/daylinep"> 线路细分 </a></li>
				</@security.authorize>
			</ul></li> </@security.authorize> <#if _utype == 'screen'> <@security.authorize
		ifAnyGranted="ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,contract_input,contract_list,contract_search,contract_buy">
		<li class="pg-side-item"><a class="pg-side-item-t hh-icon"> <i
				class="s-left pg-icon-a c-icon"></i> 视屏报表管理
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize ifAnyGranted="ShibaFinancialManager">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/report/daysalesp"> 财务收入 </a></li>

				</@security.authorize>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/report/day"> 时段报表 </a></li>

				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/report/dayorderp"> 销售报表 </a></li>
			</ul></li> </@security.authorize> </#if>

		<li class="pg-side-item"><a class="pg-side-item-t ff-icon"> <i
				class="s-left pg-icon-a e-icon"></i> 用户信息
		</a>
			<ul class="pg-side-exp-list">
				<@security.authorize
				ifAnyGranted="sys_userList,body_roleManager,bodysales,bodyContractManager,ShibaSuppliesManager,advertiser,UserManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
				
			 
				
				<@security.authorize ifAnyGranted="UserManager,sys_userList">
				<#if UType != 'body'>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/user/list"> 视频用户列表 </a></li>
					</#if>
				
				</@security.authorize>
				
				
				 <@security.authorize ifAnyGranted="body_roleManager"> 
				 <li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/user/bodyuserlist"> 车身用户列表 </a></li>
				   <li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/user/role_list"> 车身角色管理 </a>
					</li>
				</@security.authorize>
				 <@security.authorize
				ifAnyGranted="advertiser">
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/user/invoiceList"> 发票列表 </a> <a
					class="side-exp-item-t" href="${rc.contextPath}/user/UserQulifi">
						资质管理 </a></li> </@security.authorize> </@security.authorize>
						
						
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/user/qualification"> 个人信息 </a></li>
				<li class="pg-side-exp-item"><a class="side-exp-item-t"
					href="${rc.contextPath}/user/edit_pwd"> 修改密码 </a></li>
			</ul></li> <@security.authorize ifAnyGranted="advertiser">
		<li class="pg-side-item"><a class="pg-side-item-t aa-icon"
			href="http://wpa.qq.com/msgrd?v=3&uin=3070339185&site=qq&menu=yes"
			target="_blank"> <i class="s-left pg-icon-a a-icon"
				style="background:url(${rc.contextPath}/imgs/cs.png) no-repeat;"></i>
				在线客服
		</a></li> </@security.authorize>
		</ul>
	</div>
</div>
<script type="text/javascript">
	function active(menu) {
		$(".pg-side-item").each(function() {
			if (menu) {
				var curr = $(this);
				var st = 0;

				var list = $(this).find(".side-exp-item-t");
				list.each(function() {
					if ($(this).text().trim() == menu) {
						curr.find(".pg-side-exp-list").show();
						$(this).addClass("side-exp-select");
						curr.addClass("side-exp-p-select");
						st = 1;
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
				if (st == 0) {
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

	$(document)
			.ready(
					function() {
						//move single sub-menu to top
						$(".pg-side-item")
								.each(
										function() {
											var list = $(this)
													.find(
															".pg-side-exp-list .side-exp-item-t");
											if (list.length == 1) {
												var top = $(this).find(
														".pg-side-item-t");
												if (top) {
													top.attr("href", $(list[0])
															.attr("href"));
													top.find(".pg-icon-a")[0].nextSibling.data = $(
															list[0]).text();
													$(this)
															.find(
																	".pg-side-exp-list")
															.remove();
												}
											}
										});

						//open selected menu
						var menu = '<#if menu??>${menu}<#else></#if>';
						$(".pg-side-item-t").click(
								function() {
									$(this).parent(".pg-side-item").find(
											".pg-side-exp-list").toggle();
								});

						active(menu);
					});
</script>