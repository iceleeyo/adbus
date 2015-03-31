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
   
    
     function check(){ 
      	var c=$("#code").val();
		  $.ajax({
		        url:"../contract/contractCodeCheck",
		        type:"POST",
		       	data:{"code":c},
		        success:function(data){
		            alert(data.left+" # "+data.right);
		        }
		    },"text");
    }
</script>  
	</head>
	<body>
	 
    <form name="userForm2" id="userForm2" action="creOrder" enctype="multipart/form-data" method="post"">  
        
          套餐id:<input type="number" name="productId"/><br>
          物料id:  <input type="number" name="suppliesId"/><br>
          
           payType:  <input type="text" name="payType" value="ht_pay"/><br>
           合同号:  <input type="text" id="code" name="contract_code" value="reg4345"/><br> 
           <input type="button"   onclick="check();" value="合同号检查">
          
      <br>
         <input type="submit" value="创建订单" >  
    </form>   
	</body>
</html>

