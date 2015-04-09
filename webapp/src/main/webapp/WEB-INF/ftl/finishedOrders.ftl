<#import "template/template.ftl" as frame>
<#global menu="已完成的订单">
<@frame.html title="已完成的订单">

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
									class="block-btn" value="查询" onclick="sub_code();">
								&nbsp;&nbsp;&nbsp; 
							</div>
						</div>

						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							<div class="mt20">
								<div class="u-tab">
									<ul class="u-tab-items s-clear">
										<li class="u-tab-item u-tab-item-active"><a
											class="u-item-a" href="#holding">展示中 <em class="baget">0</em>
										</a></li>
										<li class="u-tab-item"><a class="u-item-a"
											href="#booking">预订中 <em class="baget">0</em>
										</a></li>
										<li class="u-tab-item"><a class="u-item-a"
											href="#exiting">已结束 <em class="baget">0</em>
										</a></li>

									</ul>
								</div>


								<!--合同列表展示-->
								<div class="module p20" style="height: 423px;">
									<div class="tab-content">
										<div class="tab-plans-type">
											<ul class="tab-plans">
												<li class="tab-plan-item tab-plan-width"><span>全部</span>
												</li>
												<li class="tab-plan-item tab-plan-width"><span>已审核</span>
												</li>
												<li class="tab-plan-item tab-plan-width"><span>已提交</span>
												</li>
												<li class="tab-plan-item tab-plan-width"><span>已展示</span>
												</li>

											</ul>
										</div>
										<div class="uplan-table-box">
											<table width="100%" class="uplan-table">
												<tr class="uplan-table-th">
													<td style="width: 98px;">
														<div class="th-head">下单用户</div>
													</td>
													<td style="width: 112px;">
														<div class="th-md">套餐号</div>
													</td>
													<td style="width: 111px;">
														<div class="th-md">素材号</div>
													</td>
													<td style="width: 103px;">
														<div class="th-md">创建时间</div>
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
														<span
														style="width: 148px; height: 35px;"
														class="ui-list-field text-center w80 fn-left"><em
														class="value-small"> <a
															href="#">
																${(item.order.productId)!''}</a>
													</em> </span>
													 <span style="width: 148px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.order.suppliesId)!''} </span> 
														<span
														style="width: 159px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small"> <#setting
															date_format="yyyy-MM-dd HH:MM">
															${(item.order.createTime?date)!''} </em> </span>
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