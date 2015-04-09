<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<#include "/menu/webapp.ftl" />
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>订单审核列表</title>

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
		window.location.href = "/${web}/order/myOrders/manage/"+g2;
	}

	function page(num) {
		var by = ($("#by").val());
		window.location.href = "/${web}/order/myOrders/manage/"+num; 
	}
	
	function claim(orderid,taskid){
 	$.ajax({
			url : "/${web}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				alert(data.right);
				location.reload([true]);
			}
		}, "text");
	  
	}
</script>
</head>
<body>
<body>
	<div class="page-container">
		<!--上部DIV-->
		<#include "/menu/top.ftl" />
		<!--下部DIV-->
		<div class="page-container">
			<div class="pg-container-main">
				<!--顶部导航开始-->
				<div class="container-12">
					<ul class="breadcrumb ml10 m11 s-clear">
						<li class="s-left fsize-16 bread-homep"><a class="gray-text"
							href="/">首页</a></li>
						<li class="s-left breadcrumb-right"></li>
						<li class="s-left bread-child"><a class="gray-text" href="#">订单审核列表</a>
						</li>
					</ul>
				</div>
				<!--顶部导航结束-->
				<div class="container-12 mt10 s-clear">
					<!--菜单开始-->
					<#include "/menu/left.ftl" />

					<!--菜单结束-->

					<!--主体开始-->
					<div class="ls-10">
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
														<span
														style="width: 118px; height: 35px;"
														class="ui-list-field text-center w80 fn-left"><em
														class="value-small"> <a
															href="/${web}/contract/contractDetail?contract_id=${(item.order.id)!''}">
																${(item.order.productId)!''} </a>
													</em> </span>
													 <span style="width: 148px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														${(item.order.suppliesId)!''} </span> 
														<span
														style="width: 129px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small"> <#setting
															date_format="yyyy-MM-dd HH:MM">
															${(item.order.createTime?date)!''} </em> </span>
															
													 <span
														style="width: 140px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small">
														<a class="trace" target="_blank" href='/${web}/workflow/view/${(item.task.executionId)!''}/page/${(item.task.processInstanceId)!''}' title="点击查看流程图">${(item.task.name)!'' }</a>
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
					</div>
				</div>
				<!--主体结束-->
			</div>
		</div>
	</div>
	</div>
<script type="text/javascript" language="javascript"
	src="/${web}/js/index.js"></script>
</body>
</html>
