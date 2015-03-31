<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>合同列表</title>


<link rel="stylesheet" type="text/css" href="../../css/sea.css">
<script>
	function search_data() {
		var name = ($("#Cname").val());
		var num = ($("#Cnum").val());
		$("#base_form").submit();

	}
</script>
</head>
<body>
<#assign web="webapp">
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
						<li class="s-left bread-child"><a class="gray-text" href="#">合同管理</a>
						</li>
						<li class="s-left breadcrumb-right"></li>
						<li class="s-left bread-child"><a class="gray-text" href="#">合同列表</a>
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

						<form id="base_form" action="/supplies/list/1" method="post"
							dataType="html" enctype="multipart/form-data" class="Page-Form">
							<div class="module s-clear u-lump-sum p19">
								<div class="u-sum-right">
									<input class="ui-input" type="text" value="" name="name"
										id="Sname" data-is="isAmount isEnough" autocomplete="off"
										disableautocomplete placeholder="合同号" /> <input type="submit"
										id="subWithdraw" class="block-btn" value="查询">
									&nbsp;&nbsp;&nbsp; <input class="ui-input" type="text" value=""
										name="name" id="Sname" data-is="isAmount isEnough"
										autocomplete="off" disableautocomplete placeholder="合同名称" /> <input
										type="submit" id="subWithdraw" class="block-btn" value="查询">
								</div>
							</div>
						</form>
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							<!--合同列表展示-->
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
								<div class="module p20" style="height: 423px;">
									<div class="tab-content">
										<div class="tab-content-box s-clear" id="holding"
											style="display: block;">
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
													<tbody>
														<tr class="uplan-table-th">
															<td width="22%">
																<div class="th-head">合同号</div>
															</td>
															<td width="15%">
																<div class="th-md">合同名称</div>
															</td>
															<td width="16%">
																<div class="th-md">生效时间</div>
															</td>
															<td width="16%">
																<div class="th-md">失效时间</div>
															</td>

														</tr>
														<#list list as item>
														<tr class="uplan-tanle-content">
															<td width="22%">
																<div class="content-head left-text u-plan-name">
																	${item.contractCode!''}</div>
															</td>
															<td width="15%">
																<div class="content-head">
																	<a href="/${web}/contract/contractDetail?contract_id=${item.id!''}">
																	${item.contractName!''}
																	</a>
																</div>
															</td>
															<td width="16%">
																<div class="content-head"><#setting
																	date_format="yyyy-MM-dd">${(item.startDate?date)!''}</div>
															</td>
															<td width="16%">
																<div class="content-head"><#setting
																	date_format="yyyy-MM-dd">${(item.endDate?date)!''}</div>
															</td>
														</tr>
														</#list>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
					<!--主体结束-->
				</div>
			</div>
		</div>
		
	</div>
	<!--底部DIV -->
		<#include "/menu/foot.ftl" />
		<!--底部DIV -->
	<script type="text/javascript" language="javascript"
		src="../../js/jquery.js"></script>
	<script type="text/javascript" language="javascript"
		src="../../js/index.js"></script>

</body>
</html>
