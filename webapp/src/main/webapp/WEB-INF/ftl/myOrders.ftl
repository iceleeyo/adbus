<#import "template/template.ftl" as frame>

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
			alert("请输入数字");
			return;
		}
		if (parseInt($("#textpage").val()) <= 0) {
			alert("请输入正整数");
			return;
		}
		if ($("#textpage").val() > pageNum) {
			alert("输入的页数超过最大页数");
			return;
		}
		//  var adsts =$("#adsts").val();
		window.location.href = "${rc.contextPath}/order/myOrders/"+g2;
	}

	function page(num) {
		var by = ($("#by").val());
		window.location.href = "${rc.contextPath}/order/myOrders/"+num;
	}
	
	function claim(orderid,taskid){
 	$.ajax({
			url : "${rc.contextPath}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				alert(data.right);
				location.reload([true]);
			}
		}, "text");
	  
	}
</script>

						<div class="module s-clear u-lump-sum p19">
							<div class="u-sum-right">
								<input class="ui-input" type="text" name="code" id="code"
									data-is="isAmount isEnough" autocomplete="off"
									placeholder="套餐号" value="${code!''}" /> <input type="button"
									class="block-btn" value="查询" onclick="#">
								&nbsp;&nbsp;&nbsp; 
							</div>
						</div>

						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							<div class="mt20">
								<div class="module p20" style="height: 423px;">
										<div class="uplan-table-box">
											<table width="100%" class="uplan-table">
												<tr class="uplan-table-th">
													<td style="width: 98px;">
														<div class="th-head">下单用户</div>
													</td>
													<td style="width: 112px;">
														<div class="th-md">订单号</div>
													</td>
													<td style="width: 112px;">
														<div class="th-md">套餐号</div>
													</td>
													<td style="width: 103px;">
														<div class="th-md">起播日期</div>
													</td>
													<td style="width: 111px;">
														<div class="th-md">素材号</div>
													</td>
													<td style="width: 103px;">
														<div class="th-md">创建时间</div>
													</td>
													<td style="width: 115px;">
														<div class="th-md">当前环节</div>
													</td>
													<td style="width: 67px; ">
														<div class="th-md">当前处理人</div>
													</td>
												</tr>
											</table>

											<#list list as item>
											<#if (item.order.userId)?exists>
											<li class="ui-list-item dark">
												<div class="ui-list-item-row fn-clear">
													<span style="width: 113px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.order.userId)!''} </span> 
														<span style="width: 113px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.longOrderId)!''} </span> 
														<span
														style="width: 118px; height: 35px;"
														class="ui-list-field text-center w80 fn-left"><em
														class="value-small"> 
                                                       <a  target="_blank" href="${rc.contextPath}/order/proDetail?product_id=${(item.order.productId)!''}">																${(item.order.productId)!''} </a>
													</em> </span>
													<span
														style="width: 129px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small"> <#setting
															date_format="yyyy-MM-dd HH:mm">
															${(item.order.startTime?date)!''} </em> </span>
													 <span style="width: 148px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.order.suppliesId)!''} </span> 
														<span
														style="width: 129px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small"> <#setting
															date_format="yyyy-MM-dd HH:mm">
															${(item.order.created?date)!''} </em> </span>
															
													 <span
														style="width: 140px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small">
														<a class="trace" target="_blank" href='${rc.contextPath}/workflow/view/${(item.task.executionId)!''}/page/${(item.task.processInstanceId)!''}' title="点击查看流程图">${(item.task.name)!'' }</a>
														</em>
														</span>
															
															
															 <span
														style="width: 90px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left">
														<em class="value-small"> ${(item.task.assignee)!''}
													</em>
													</span>
											</li> 
											<#else>
											</#if>
											</#list>
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

</@frame.html>