<#macro html title="" css=[] js=[]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${title!''}</title>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.form.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.dataTables.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-dataTables-fnNameOrdering.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-dataTables-fnNoColumnsParams.js"></script>

    <#--<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-ui/jquery-ui.min.js"></script>-->
<#--    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.ui.dialog.js"></script>-->
    <#--<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.datepicker.region.cn.js"></script>-->
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/platform.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/index.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/common.js"></script>
    <#list js as entry>
        <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/${entry}"></script>
    </#list>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/sea.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/one.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/account.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/page.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/jquery-ui/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/table.css">
    <#list css as entry>
        <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/${entry}">
    </#list>
</head>
<body>
<div class="page-container">
<!--上部DIV-->
	<#include "../menu/top.ftl" />
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
		<a class="gray-text" href="#">${title!''}</a>
		</li>
		</ul>
		</div>
		<!--顶部导航结束-->
	<div class="container-12 mt10 s-clear">
		<!--菜单开始-->
		<#include "../menu/left.ftl" />
		
		<!--菜单结束-->
		
		<!--主体开始-->
	<div class="ls-10">
		<div class="withdraw-wrap color-white-bg fn-clear">
            <#nested>
		</div>
	</div>
		<!--主体结束-->
	</div>
	</div>				
</div>
</div>

</body>	
</html>
</#macro>