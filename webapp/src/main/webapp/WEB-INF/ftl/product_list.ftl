<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<#include "/menu/webapp.ftl" />
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>产品查询</title>

</head>
<body>
<#include "/menu/webapp.ftl" />
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
						<li class="s-left bread-child"><a class="gray-text" href="#">物料列表</a>
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
						<div class="withdraw-wrap color-white-bg fn-clear">
							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="saveContract"
								enctype="multipart/form-data">
								<!--产品列表展示-->
								<div class="uplan-table-box">
									<table width="100%" class="uplan-table">
										<tr class="uplan-table-th">
											<td width="22%">
												<div class="th-head">素材名称</div>
											</td>
											<td width="15%">
												<div class="th-md">素材类型</div>
											</td>
											<td width="16%">
												<div class="th-md">创建时间</div>
											</td>
										</tr>
										</table>
										<#list list as item>
												<li class="ui-list-item dark">
														<span style="width: 306px; height: 35px; "
															class="ui-list-field text-center w80 fn-left" >
															${item.name}
														</span>
														 <span style="width: 208px; height: 35px; "
															class="ui-list-field text-center  fn-left">
															${item.id}
														
														 </span>
														 <span
															style="width: 224px; height: 35px; "
															class="ui-list-field text-center w80 fn-left">
														<#setting date_format="yyyy-MM-dd HH:MM"> 
														 </span> 
												</li>
										</#list> 
									
								</div>
							</form>
						</div>
					</div>
				</div>
				<!--主体结束-->
			</div>
		</div>
	</div>
<script type="text/javascript" language="javascript"
	src="/${web}/js/index.js"></script>
</body>
</html>
