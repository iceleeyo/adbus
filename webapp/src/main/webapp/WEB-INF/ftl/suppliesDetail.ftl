<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>物料详细</title>
<link rel="stylesheet" type="text/css" href="../css/page.css">
<link rel="stylesheet" type="text/css" href="../css/account.css">
<link rel="stylesheet" type="text/css" href="../css/sea.css">
<link rel="stylesheet" type="text/css" href="../css/one.css">
<script type="text/javascript" language="javascript"
	src="../../js/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="../../js/common.js"></script>	
<script type="text/javascript" language="javascript"
	src="../../js/platform.js"></script>
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
						<li class="s-left bread-child"><a class="gray-text" href="#">物料管理</a>
						</li>
						<li class="s-left breadcrumb-right"></li>
						<li class="s-left bread-child"><a class="gray-text" href="#">物料详细</a>
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
								<button type="button" onclick="go_back()" class="block-btn" style="margin-bottom: 10px;">返回</button>
									<div class="tab-content">
										<div class="tab-content-box s-clear" id="holding"
											style="display: block;">
											<div class="uplan-table-box">
												<table width="100%" class="uplan-table">
													<tbody>
														<tr class="uplan-table-th">
															<td style="width: 225px; ">
																<div class="th-head">物料名称</div>
															</td>
															<td>
																<div class="th-tail">附件</div>
															</td>
														</tr>
													</tbody>
												</table>
												<li class="ui-list-item dark" >
													<div class="ui-list-item-row fn-clear">
														<span style=" height: 35px; width: 231px"
															class="ui-list-field text-center w80 fn-left" >
															 ${view.mainView.name!''}
														</span>
													
														<span style="height: 35px; width: 485px" 
															class="ui-list-field text-center w80 fn-left">
															<#list view.files as item> ${item.name!''} &nbsp; /
   															</#list>
														</span>
													</div>
												</li>
												
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
		<!--底部DIV -->
		<#include "/menu/foot.ftl" />
		<!--底部DIV -->
	</div>
<script type="text/javascript" language="javascript"
	src="../../js/jquery.ui.dialog.js"></script>
<script type="text/javascript" language="javascript"
	src="../../../js/index.js"></script>
</body>   
</html>
