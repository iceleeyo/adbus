<html>
<head>
      <meta charset="utf-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>

   物料详细
    <br>
   物料名称：  ${view.mainView.name!''}<br>
         附件：
   <#list view.files as item>
      ${item.name!''}
   </#list> 


</body>	    
</html>
