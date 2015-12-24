
<html xmlns:th="http://www.thymeleaf.org">

<head>
    <title>Spring Thyme Seed Starter Manager</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" media="all" href="../../css/stsm.css" th:href="@{/css/stsm.css}"/>
    <title>请求方法列表</title>
</head>

<body>
<div style="margin: 0;padding: 0;text-align: center"><h1>请求方法列表</h1></div>
<div>
    <ul>
       <#list methodList as c>
        <li  >
            <div>
                <p>#${c.controllerName!''}   #方法: ${c.methodName!''}： #url：${c.requestUrl!''}   #ftl：<font color=red>${c.ftlName!''}</font></p>
                <ul>
                </ul>
            </div>

        </li>
           </#list>
    </ul>
</div>


</body>
</html>