<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<#include "/menu/webapp.ftl" />
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>物料列表</title>
<link rel="stylesheet" type="text/css" href="../../css/page.css">
<link rel="stylesheet" type="text/css" href="../../css/account.css">
<link rel="stylesheet" type="text/css" href="../../css/sea.css">
<link rel="stylesheet" type="text/css" href="../../css/one.css">
<script type="text/javascript" language="javascript"
	src="../../js/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="../../js/common.js"></script>	
<script type="text/javascript" language="javascript"
	src="../../js/platform.js"></script>
<script>
	function pages(pageNum) {
		var by = ($("#by").val());
		var name = ($("#name").val());
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
		window.location.href = "/${web}/supplies/list/" + g2 + "?name="+ name;
	}

	function page(num) {
		var name = $("#name").val();
		var by = ($("#by").val());
		window.location.href = "/${web}/supplies/list/" + num + "?name=" + name;
	}

	function sub(){
		var name = $("#name").val(); 
		window.location.href= "/${web}/supplies/list/1"+"?name="+name
	}
</script>
</head>
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
						<li class="s-left bread-child"><a class="gray-text" href="#">物料管理</a>
						</li>
						<li class="s-left breadcrumb-right"></li>
						<li class="s-left bread-child"><a class="gray-text" href="#">物料列表</a>
						</li>
					</ul>
				</div>
				<!--顶部导航结束-->
				<div class="container-12 mt10 s-clear" style="height: 680px;">
					<!--菜单开始-->
					<#include "/menu/left.ftl" />

					<!--菜单结束-->

					<!--主体开始-->
					<div class="ls-10">
						
<form id="base_form" action="/supplies/list/1" method="post"
dataType="html" enctype="multipart/form-data" class="Page-Form">						
<div class="module s-clear u-lump-sum p19">
<div class="u-sum-right">
		<input class="ui-input" type="text" value="${name!''}" id="name" data-is="isAmount isEnough" autocomplete="off" disableautocomplete  placeholder="物料名称"/>
        <input type="button" id="subWithdraw" class="block-btn" value="查询" onclick="sub();">
</div>
</div>
</form>
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
																<div class="th-head">物料名称</div>
															</td>
															<td width="15%">
																<div class="th-md">物料类型</div>
															</td>
															<td width="16%">
																<div class="th-md">创建时间</div>
															</td>
														</tr>
													</tbody>
												</table>
												<#list list as item>
														<span style="width: 306px; height: 35px; " class="ui-list-field text-center w80 fn-left" >
															<a href="../suppliesDetail?supplies_id=${item.id!''}">
															${item.name}
															</a>
														</span>
														
														 <span style="width: 208px; height: 35px; "
															class="ui-list-field text-center  fn-left">
															${item.suppliesType}
														
														 </span>
														 <span
															style="width: 224px; height: 35px; "
															class="ui-list-field text-center w80 fn-left">
														<#setting
																	date_format="yyyy-MM-dd HH:MM"> ${item.createTime?date}
														 </span> 
												</li>
												</#list> 
												<!-- 分页 -->
												<table class="pag_tbl"
													style="width: 100%; border-width: 0px; margin-top: 10px;">
													<tr>
														<td style="width: 70%; text-align: right;">
															<div id="numpage" style="float: right;">
															${paginationHTML!''}	 
															</div>
														</td>
													</tr>
												</table>
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
		<!--底部DIV -->
		<#include "/menu/foot.ftl" />
		<!--底部DIV -->
	</div>

<script type="text/javascript" language="javascript"
	src="../../js/jquery.ui.dialog.js"></script>
<script type="text/javascript" language="javascript"
	src="../../js/index.js"></script>
</body>

</html>

