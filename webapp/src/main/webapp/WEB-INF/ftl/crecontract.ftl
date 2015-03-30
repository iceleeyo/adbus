<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		  <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
		   <script type="text/javascript" src="../js/jquery.form.js"></script>
		  <script type="text/javascript">  
    i = 2;  
    j = 2;  
    $(document).ready(function(){  
          
        $("#btn_add2").click(function(){  
             $("#newUpload2").append('<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  /><input type="button" value="删除"  onclick="del_2('+j+')"/></div>');  
              j = j + 1;  
        });  
    });  
  
    function del_2(o){  
         document.getElementById("newUpload2").removeChild(document.getElementById("div_"+o));  
    }  
  function sub(){  
		  $.ajax({
		        url:"saveContract",
		        type:"POST",
		        data:$("#userForm2").formSerialize(),
		        success:function(data){
		            alert(data.left+" # "+data.right);
		        }
		    },"text");
    
    }
</script>  
	</head>
	<body>
	 
    <form name="userForm2" id="userForm2" action="saveContract" enctype="multipart/form-data" method="post"">  
        
           合同号<input type="text" name="contractNum"/><br>
           合同名称  <input type="text" name="contractName"/><br>
          上传合同附件 <div id="newUpload2">  
           <div id="div_1">
            <input type="file" name="file">  
            </div>
        </div>  
        <input type="button" id="btn_add2" value="增加一行" >  
      <br>
         <input type="button" onclick="sub();" value="创建合同" >  
    </form>   
	</body>
</html>

