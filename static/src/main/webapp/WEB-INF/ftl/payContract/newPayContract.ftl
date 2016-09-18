<#import "../template/template.ftl" as frame> <#global menu="销售员客户管理 ">
<@frame.html title="销售员客户管理 " js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js","../js/jquery-ui/jquery-ui.auto.complete.js"]
css=["js/jquery-ui/jquery-ui.css","../css/autocomplete.css","../js/jquery-ui/jquery-ui.auto.complete.css"]>


<script type="text/javascript">
function go_back(){
	history.go(-1);
}
    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        });
        
        
        
    });
</script>

<script type="text/javascript">
function split( val ) {
      return val.split( /,\s*/ );
    }
	$(document).ready(function() {
			 $( "#customerName" ).autocomplete({
		        	minLength: 0,
		  			source: "${rc.contextPath}/user/queryMyCustomers",
		  			change: function( event, ui ) { 
		  				$("#orderid").val("");
		  				$("#inputs").html("");
		  			 },
		  			 select: function(event,ui) {
		  			 	$('#customerName').val(ui.item.value);    
   $("#orderid").autocomplete({
        minLength: 0,
        source : "${rc.contextPath}/payContract/OrderIdComplete?customerName="+$('#customerName').val(),
        focus: function() {
          return false;
        },
        select: function( event, ui ) {
          var terms = split( this.value );
          terms.pop();
          terms.push( ui.item.value );
          terms.push( "" );
          this.value = terms.join( ", " );
          showOrderDetail($("#orderid").val());
          
           return false;
        },
        change: function( event, ui ) { 
		  				showOrderDetail($("#orderid").val());
		  			 },
      }).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
		  			 	
		  			 }
				}).focus(function () {
       				 $(this).autocomplete("search");
   	 			});
					});

function showOrderDetail(orderIds){
$.ajax({
		url :"${rc.contextPath}/payContract/showOrderDetail?orderIds="+orderIds,
		data:{},
		type : "POST",
		success : function(data) {
		$("#inputs").html("");
		 var op="";
			$.each(data,function(i,item){
			 op+="<DIV class=\"p20bs color-white-bg border-ec\">"
						   +"<DIV class=\"summary uplan-summary-div\">"
		              +"<UL class=\"uplan-detail-ul\">"
			+"<li class=\"s-left f-iconli\"><span class=\"s-left tt\"><i class=\"s-left ff-icon\"></i>订单信息</span></li>"
			+"<li style=\"width: 200px;\"><SPAN>订单号：</SPAN><a target='_blank' href='${rc.contextPath}/order/orderDetail/"+item.order.id+"'>  "+item.longOrderId+"</a></li>"
			+"<li style=\"width: 200px;\"><SPAN>套餐名称：</SPAN>"
			+"<a class='layer-tips' tip='点击可查看套餐详细内容!' onclick=\"showProductlayer('${rc.contextPath}',"+item.order.product.id+");\">"
			+item.order.product.name+"</a></li>"
			+"<li style=\"width: 200px;\"><SPAN>订单价格：</SPAN>"+item.order.price+"</li>"
			+"<li style=\"width: 200px;\"><SPAN>媒体类型：</SPAN>"+item.order.product.type+"</li>"
			+"<li style=\"width: 200px;\"><SPAN>下单用户：</SPAN>"+item.order.creator+"</li>"
			+"</UL></DIV></DIV><br>"
				});
				$("#inputs").html(op);
				}
		});
}

	function sub(){
	if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
			return;
		$('#userForm2').ajaxForm(function(data) {
				if(data.right){
					layer.msg("保存成功");
				}else {
					layer.msg("操作失败");
				}
				window.setTimeout(function(){
				window.location.href="${rc.contextPath}/user/clientList",
				   	clearTimeout(uptime);
							},2000) 
		}).submit();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="${rc.contextPath}/user/saveClientUser"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>客户信息</span>  <a class="block-btn"
				style="margin-top: -5px;" href="javascript:void(0);"
				onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs" >
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同编号:</label>
					<input
						class="ui-input  validate[required,minSize[2],maxSize[100]]"
						type="text" name="company" id="company" value="${contractCode}" readonly="readonly"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>选择客户:</label>
					<input id="customerName" value="" style="width:250px">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>选择订单:</label>
					<input id="orderid" value="" style="height:50px;width:650px"/>
				</div>
			

			</div>
			
		</div>
		<div id="inputs">
		
		   
		</div>
		<div class="widthdrawBtBox">
				<input type="button" id="subWithdraw" class="block-btn"
					onclick="sub();" value="提交">
			</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>...</li>
			</ol>
		</div>
	
</div>
</@frame.html>
