<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	  <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <title>物料列表</title>
      <script type="text/javascript" language="javascript" src="../../js/jquery.js"></script>
	  <script type="text/javascript" language="javascript" src="../../js/jquery.form.js"></script>
	  <script type="text/javascript" language="javascript" src="../../js/index.js"></script>
      <link rel="stylesheet" type="text/css" href="../../css/sea.css">
      <link rel="stylesheet" type="text/css" href="../../css/one.css">
      <link rel="stylesheet" type="text/css" href="../../css/account.css">
      <link rel="stylesheet" type="text/css" href="../../css/page.css" media="screen" /> 
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
		<li class="s-left fsize-16 bread-homep">
		<a class="gray-text" href="/">首页</a>
		</li>
		<li class="s-left breadcrumb-right"></li>
		<li class="s-left bread-child">
		<a class="gray-text" href="#">物料列表</a>
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
     	 <form data-name="withdraw" name="userForm2" id="userForm2" class="ui-form" method="post" action="saveContract" enctype="multipart/form-data">
	       
          
      	 
      	 	<!--合同列表展示-->
      	 	<div class="List-Table">
			<table width="100%">
				<tr>
					<td scope="col">素材名称</td>
					<td scope="col">素材类型</td>
					<td scope="col">创建时间</td>
				</tr>
				<tbody>
					<#list list as item>
						<tr>
							<td >${item.name}</td>
							<td >${item.id}</td>
							 
						</tr>
					</#list> 
				</tbody>
			</table>
			</form>
			</div>
		</div>
	</div>
		<!--主体结束-->
	</div>
	</div>				
</div>
</div>

</body>	
</html>
