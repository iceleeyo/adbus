<#macro html title="" left=true nav=true css=[] js=[]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <link rel="shortcut icon" href="${rc.contextPath}/images/favicon.ico">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${title!''}</title>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.form.js"></script>
    
  <!--  <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.min.js"></script>-->
    
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/validation/jquery.validationEngine.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/validation/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.dataTables.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-dataTables-fnNameOrdering.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-dataTables-fnNoColumnsParams.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer/layer.js"></script>
       <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/icheck.js"></script>
  
    <#--<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery-ui/jquery-ui.min.js"></script>-->
<#--    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.ui.dialog.js"></script>-->
    <#--<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.datepicker.region.cn.js"></script>-->
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/platform.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/index.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/common.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/banBackSpace.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.validate.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.ulmenu.js"></script>
    <script type="text/javascript" language="javascript" src="${rc.contextPath}/js/jquery.fancybox-1.3.4.pack.js"></script>
    <#list js as entry>
        <script type="text/javascript" language="javascript" src="${rc.contextPath}/${entry}"></script>
    </#list>
    <!--增加lay最新版本-->
	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer.onload.js"></script>
	<script type="text/javascript" language="javascript" src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>


    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/icheck-green.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/one.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/foot.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/page.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/jquery-ui/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/js/validation/validationEngine.jquery.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/table.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/city.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/logMini.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/homepage/css/index.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/homepage/css/seaIn.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/homepage/css/tab.css">
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/homepage/tem/guide.css">
    <#list css as entry>
        <link rel="stylesheet" type="text/css" href="${rc.contextPath}/${entry}">
    </#list>
    <style type="text/css">
        .online-support {
            display: none;
            position: fixed;
            bottom: 20px;
            left: 20px;
            width: 80px;
            height: 32px;
        }
    </style>
</head>
<body>
<div class="page-container">
<!--上部DIV-->
	<#include "../menu/top.ftl" />
<!--下部DIV-->
<div class="page-container">
	<div class="pg-container-main">
        <div class="container-12 mt10 s-clear">


        <#if left>
		<!--菜单开始-->
		<#include "../menu/leftIn.ftl" />
        </#if>
		<!--菜单结束-->
		
		<!--主体开始-->
	<div class="ls-10">
            <#nested>
	<br>
	</div>
		<!--主体结束-->
	</div>
	</div>				
</div>
</div>
<div class="online-support">
    <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3070339185&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=3:3070339185:3" alt="点击这里咨询" title="点击这里咨询"/></a>
</div>

<#include "../menu/foot.ftl" />
</body>
</html>
</#macro>