<#import "template/template.ftl" as frame>

<@frame.html title="公交广告交易系统">


<script type="text/javascript">
	function sub2() {
		$('#userForm2').ajaxForm(function(data) {
			alert(data.left + " # " + data.right);
		}).submit();
	}

	function check() {
	var ctx = '<%=request.getContextPath() %>';
		var c = $("#code").val();
		$.ajax({
			url : "../contract/contractCodeCheck",
			type : "POST",
			data : {
				"code" : c
			},
			success : function(data) {
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
	function go_back() {
		history.go(-1);
	}
	
	function pay() {
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "payment",
			type : "POST",
			data : {
				"orderid" :orderid,"taskid" :taskid
			},
			success : function(data) {
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
</script>
						<input type="hidden" id="orderid" value="${orderid!''}"/>
						<input type="hidden" id="taskid" value="${taskid!''}"/>
							支付方式：<br>
							输入合同号：<input id="code"
												class="ui-input" type="text" value="reg4345" name="contract_code"
												 data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete=""> 
											<input type="button" class="block-btn" onclick="check();" value="合同号检查">
											<br><br>
											<input type="button" class="block-btn" onclick="pay();" value="确认支付"/>
						<br><br>
						<button type="button" class="block-btn" onclick="go_back()">返回</button>
</@frame.html>