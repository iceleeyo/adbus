<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		  <script type="text/javascript" language="javascript" src="../js/jquery.js"></script>
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
  
</script>  
	</head>
	<body>
	   ${(message.left)!''}
	        欢迎你，${(loginuser.username)!''}
		<form method="post" action="storeFile.do" enctype="multipart/form-data">
			<input type="file" name="file" />
			<input type="submit" />
		</form>
		<h1>springMVC包装类上传文件</h1>   
    <form name="userForm2" action="put" enctype="multipart/form-data" method="post"">  
        <div id="newUpload2">  
           <div id="div_1">
            <input type="file" name="file">  
            </div>
        </div>  
        <input type="button" id="btn_add2" value="增加一行" >  
      <br>
        <input type="text" name="name"/><br>
         <input type="text" name="suppliesType"/><br>
          <input type="text" name="infoContext"/><br>
           <input type="text" name="a1"/><br>
        <input type="submit" value="上传" >  
    </form>   
	</body>
</html>
