
<html xmlns:th="http://www.thymeleaf.org">

<head>
    <title>URI FTL</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" media="all" href="../../css/stsm.css" th:href="@{/css/stsm.css}"/>
    <link rel="stylesheet" type="text/css"
	href="${rc.contextPath}/css/table.css">
    <title>请求方法列表</title>
<style type="text/css">

.conDiv{
	margin:0 auto;
}
</style>
</head>

<body>
<div style="margin: 0;padding: 0;text-align: center"><h1 class="h1">方法路径表</h1></div>
<div class="conDiv">
    <table class="conTab" >
    <thead>
     <tr>
     <th></th>
     	<th>Controller</th>
     	<th> 方法：</th>
     	<th> url：</th>
     	<th> ftl：</th>
     </tr>
    </thead>
    <tbody>
       <#list methodList as c>
       <tr>
        <td>${c_index+1}</td>
        <td>${c.controllerName!''}</td>
     	<td>${c.methodName!''}</td>
     	<td>${c.requestUrl!''}</td>
     	<td><font color="red">${c.ftlName!''}</font></td>
       </tr>    
       </#list>
    </tbody>
    </table>
</div>


</body>
</html>