<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		  <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
		   <script type="text/javascript" src="../js/jquery.form.js"></script>
		  <script type="text/javascript">  
  
    function sub2(){   
		  $('#userForm2').ajaxForm(function(data){
           		 alert(data.left+" # "+data.right);
        	}).submit();
   }
</script>  
	</head>
	<body>
	 
    <form name="userForm2" id="userForm2" action="creOrder" enctype="multipart/form-data" method="post"">  
        
          套餐id:<input type="number" name="productId"/><br>
          物料id:  <input type="number" name="suppliesId"/><br>
          
      <br>
         <input type="submit" value="创建订单" >  
    </form>   
	</body>
</html>

