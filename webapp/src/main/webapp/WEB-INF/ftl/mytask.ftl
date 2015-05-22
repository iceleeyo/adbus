<#import "template/template.ftl" as frame>
<#global menu="待办事项">
<@frame.html title="订单审核列表">

<script type="text/javascript">

	function pages(pageNum) {
		var pattern = /^\d+$/;
		var by = ($("#by").val());
		var g2 = ($("#textpage").val());
		if (g2 == undefined) {
			g2 = 1;
		}
		if (!isNaN($("#textpage").val())) {
		} else {
			jDialog.Alert("请输入数字");
			return;
		}
		if (parseInt($("#textpage").val()) <= 0) {
			jDialog.Alert("请输入正整数");
			return;
		}
		if ($("#textpage").val() > pageNum) {
			alert("输入的页数超过最大页数");
			return;
		}
		//  var adsts =$("#adsts").val();
		window.location.href = "${rc.contextPath}/order/list/"+g2;
	}

	function page(num) {
		var by = ($("#by").val());
		window.location.href = "${rc.contextPath}/order/list/"+num; 
	}
	
	function claim(orderid,taskid){
 	$.ajax({
			url : "${rc.contextPath}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				jDialog.Alert(data.right);
				var uptime = window.setTimeout(function(){
				location.replace("${rc.contextPath}/order/list/") 
			   	clearTimeout(uptime);
						},2000)
			}
		}, "text");
	  
	}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
						<div class="module s-clear u-lump-sum p19">
						<div class="withdraw-title" style="padding-top: 0px;text-align:center;">
									待办事项
									</div>
						<div class="u-sum-right">
								<input class="ui-input" type="text" name="code" id="code"
									data-is="isAmount isEnough" autocomplete="off"
									placeholder="套餐号" value="${code!''}" /> <input type="button"
									class="block-btn" value="查询" onclick="sub_code();">
								&nbsp;&nbsp;&nbsp; 
							</div>
						</div>

						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">


								<!--合同列表展示-->
								<div class="module p20" style="height: 423px;">
									<div class="tab-content">
										<div class="tab-plans-type">
											<ul class="tab-plans">
												<li class="tab-plan-item tab-plan-width"><span>全部</span>
												</li>
												<li class="tab-plan-item tab-plan-width"><span>签收</span>
												</li>
												<li class="tab-plan-item tab-plan-width"><span>办理</span>
												</li>
											</ul>
										</div>
										<div class="uplan-table-box">
											<table width="100%" class="uplan-table">
												<tr class="uplan-table-th">
												<td style="width: 100px;">
														<div class="th-head">订单号</div>
													</td>
													<td style="width: 80px;">
														<div class="th-md">下单用户</div>
													</td>
													
													<td style="width: 80px;">
														<div class="th-md">套餐号</div>
													</td>
													<td style="width: 80px;">
														<div class="th-md">素材号</div>
													</td>
													<td style="width: 120px;">
														<div class="th-md">起播日期</div>
													</td>
													
													<td style="width: 120px;">
														<div class="th-md">创建时间</div>
													</td>
													<td style="width: 115px;">
														<div class="th-md">当前环节</div>
													</td>
													<td style="width: 67px; ">
														<div class="th-tail">操作</div>
													</td>
												</tr>
											</table>

											<#list list as item>
											<li class="ui-list-item dark">
												<div class="ui-list-item-row fn-clear">
												    <span style="width: 113px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.longOrderId)!''} </span> 
													<span style="width: 80px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.order.userId)!''} </span> 
														
														
														<span
														style="width: 80px; height: 35px;"
														class="ui-list-field text-center w80 fn-left"><em
														class="value-small"> <a target="_blank"
															href="${rc.contextPath}/order/proDetail?product_id=${(item.order.productId)!''}">
																${(item.order.productId)!''} </a>
													</em> </span>
													 <span style="width: 80px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.order.suppliesId)!''} </span>
																<span
														style="width: 130px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small"> <#setting
															date_format="yyyy-MM-dd">
															${(item.order.startTime?date)!''} </em> </span>
													 
														<span
														style="width: 130px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small"> <#setting
															date_format="yyyy-MM-dd HH:mm">
															${(item.order.created?date)!''} </em> </span>
															
													 <span
														style="width: 120px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small">
														<a class="trace" target="_blank" href='${rc.contextPath}/workflow/view/${(item.executionId)!''}/page/${item.processInstanceId}' title="点击查看流程图">${item.task_name }</a>
														</em>
														</span>
															
															
															 <span
														style="width: 90px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left">
														<em class="value-small"> <#if
															item.assignee?exists> 
															<a href="${rc.contextPath}/order/handleView2?orderid=${(item.order.id)!''}&taskid=${(item.task_id)!''}">办理</a>
															<#else>
															<a href="javascript:;" onclick="claim('${(item.order.id)!''}','${(item.task_id)!''}');">签收</a>

															</#if>
													</em>
													</span>
											</li> </#list>
											<!-- 分页 -->
											<table class="pag_tbl"
												style="width: 100%; border-width: 0px; margin-top: 10px;">
												<tr>
													<td style="width: 70%; text-align: right;">
														<div id="numpage" style="float: right;">
															${paginationHTML!''}</div>
													</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
							</div>
						</form>
</div>
</@frame.html>