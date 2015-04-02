<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>合同详细</title>
<link rel="stylesheet" type="text/css" href="../css/sea.css">
<script type="text/javascript">
function go_back(){
	history.go(-1);
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
						<li class="s-left bread-child"><a class="gray-text" href="#">合同管理</a>
						</li>
						<li class="s-left breadcrumb-right"></li>
						<li class="s-left bread-child"><a class="gray-text" href="#">合同详情</a>
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
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							<!--合同详情展示-->
							
							<div class="mt20">
							
								<div class="module p20" style="height: 423px;">
								<button type="button" onclick="go_back()" >返回</button>
									<div class="tab-content">
										<div class="tab-content-box s-clear" id="holding"
											style="display: block;">
											<div class="uplan-table-box">
												<table width="100%" class="uplan-table">
													<tbody>
														<tr class="uplan-table-th">
															<td width="22%">
																<div class="th-head">合同号</div>
															</td>
															<td width="15%">
																<div class="th-md">附件</div>
															</td>
														</tr>
														<tr class="uplan-tanle-content">
															<td width="22%">
																<div class="content-head left-text u-plan-name">
																${view.mainView.contractCode!''}
																</div>
															</td>
															<td width="15%">
																<div class="content-head">
																<#list view.files as item> ${item.name!''} 
																</#list>
																</div>
															</td>
														</tr>
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
		<!--底部DIV -->
		<#include "/menu/foot.ftl" />
		<!--底部DIV -->
		</div>
	</div>
	
<script type="text/javascript" language="javascript"
	src="../../js/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="../../js/index.js"></script>
<script type="text/javascript" language="javascript"
	src="../../js/jquery.form.js"></script>
</body>

</html>
