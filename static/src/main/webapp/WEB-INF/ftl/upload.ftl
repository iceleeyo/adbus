<html>
<head>
<script type="text/javascript"
	src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	i = 2;
	j = 2;
	$(document).ready(function() {
        $("#btn_add2").click(function() {
            $("#newUpload2").append(
                    '<div id="div_'+j+'"><input  name="file" type="file"  style="margin-top:10px;" />' +
                    '<input type="button"  style="margin-top:10px;" value="删除"  onclick="del_2('+ j + ')"/></div>');
            j = j + 1;
        });

    });

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}


	function sub2() {
		$('#userForm2').ajaxForm(function(data) {
			alert("sdsd");
			var uptime = window.setTimeout(function(){
			   	clearTimeout(uptime);
						},2000)
		}).submit();
	}
	
</script>
</head>

<body>

	<form id="userForm2" name="userForm2" action="upload1"
		enctype="multipart/form-data" method="post">
		<div id="newUpload2">
			<div id="div_1">
				<input type="file" name="file" id="Sfile">
			</div>
		</div>
		<input type="button" id="btn_add2" value="增加一行"
			style="margin-top: 10px;"><br> <input type="submit"
			value="物料上传">
		</div>
		</div>
	</form>
</body>
</html>

