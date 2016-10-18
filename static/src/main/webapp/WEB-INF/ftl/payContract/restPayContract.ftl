<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "../template/template.ftl" as frame> <#global menu="合同分期支付">
<@frame.html title="合同分期支付 " js=["../js/jquery-ui/jquery-ui.js","../js/datepicker.js","../js/jquery.datepicker.region.cn.js",
"../index_js/sift_common.js","../js/jquery-ui/jquery-ui.auto.complete.js","../js/jquery-dateFormat.js"]
css=["../js/jquery-ui/jquery-ui.css","../css/autocomplete.css","../js/jquery-ui/jquery-ui.auto.complete.css"]>

<script type="text/javascript">
$(function(){
   checkNeedPay(${jpaPayContract.id !''});
});
function go_back(){
	history.go(-1);
}
function checkNeedPay(payContractId){
  $.ajax({
			url : "${rc.contextPath}/order/checkNeedPay",
			type : "POST",
			data : {
				"orderId" :0,
				"payContractId" :payContractId
			},
			success : function(data) {
				if(!data){
				   $("#userFristPay").hide();
				}
			}
		}, "text");
}
</script>
<script type="text/javascript">
function go_back(){
	history.go(-1);
}
    $(document).ready(function() {
        var seriaNum='${jpaPayContract.seriaNum!''}';
        
    initPlan('${rc.contextPath}',${jpaPayContract.id!''},'<@security.authorize ifAnyGranted="sales">doNoting</@security.authorize>','contract',seriaNum);
    initPayPlanTable('${rc.contextPath}',${jpaPayContract.id!''},'<@security.authorize ifAnyGranted="sales">donothing</@security.authorize>','contract',seriaNum);
        
    });
</script>

<script type="text/javascript">
function split( val ) {
      return val.split( /,\s*/ );
    }
	$(document).ready(function() {
	
	$('input').iCheck({
    checkboxClass: 'icheckbox_square-green',
    radioClass: 'iradio_square-green',
    increaseArea: '10%' // optional
  });
	$("#otherpay").hide();
	//$("#contractCode").hide();
    $("#pingzhengTab").hide();
    $('input').on('ifChecked', function(event){
			var p =($(this).val());
			if($(this).attr("name")=='payType'){
				if(p=='contract'){
					showContract();
				}else if(p == 'online'){
					hideall();
				}else if(p == 'remit'){
					hideboth();
				}else{
				 	hideContract();
				}
			}
		});
	
	$("#orderid").val(${jpaPayContract.orderJson});
	showOrderDetail($("#orderid").val())
	var customerName=${jpaPayContract.customerJson}.company;
	var customerId=${jpaPayContract.customerJson}.username;
	$("#customerName").val(customerName);
	$("#customerId").val(customerId);
		  			 	
					});

function showOrderDetail(orderIds){
$.ajax({
		url :"${rc.contextPath}/payContract/showOrderDetail?orderIds="+orderIds,
		data:{},
		type : "POST",
		success : function(data) {
		$("#inputs").html("");
		 var op="";
		 var price=0;
			$.each(data,function(i,item){
			 op+="<DIV class=\"newPay-order\">"
						   +"<DIV class=\"summary uplan-summary-div\">"
		              +"<UL class=\"uplan-detail-ul\">"
			+"<li class=\"s-left f-iconli\"><span class=\"s-left tt\"><i class=\"s-left ff-icon\"></i></span></li>"
			+"<li style=\"width: 200px;\"><SPAN>订单号：</SPAN><a target='_blank' href='${rc.contextPath}/order/orderDetail/"+item.order.id+"'>  "+item.longOrderId+"</a></li>"
			+"<li style=\"width: 200px;\"><SPAN>套餐名称：</SPAN>"
			+"<a class='layer-tips' tip='点击可查看套餐详细内容!' onclick=\"showProductlayer('${rc.contextPath}',"+item.order.product.id+");\">"
			+item.order.product.name+"</a></li>"
			+"<li style=\"width: 200px;\"><SPAN>订单价格：</SPAN>"+item.order.price+"</li>"
			+"<li style=\"width: 200px;\"><SPAN>媒体类型：</SPAN>"+item.order.product.type+"</li>"
			+"<li style=\"width: 200px;\"><SPAN>下单用户：</SPAN>"+item.order.creator+"</li>"
			+"</UL></DIV></DIV>"
			price+=item.order.price;
				});
				$("#inputs").html(op);
				$("#price").val(price);
				}
		});
}


function uploadImaget(formId) { 
    var image_name=$("#fileMaterial").val();
    if(image_name != ''){
    var imgs=image_name.split(".");
    var img_subfier= imgs[imgs.length-1].toLocaleLowerCase();
    var img_parr = ["jpg", "jpeg", "gif","png"]; 
    
    if(image_name !=''){
        if($.inArray(img_subfier, img_parr) ==-1){
            jDialog.Alert("请上传['jpg','gif','png','jpeg']格式的图片!");
            return false;
        }
    }
    var options = { 
            url : "${rc.contextPath}/upload/savePayvoucher/${jpaPayContract.id!''}",
            type : "POST",
            dataType : "json",
            success : function(data) {
             if(data !=null && data!=""){
                  $("#showImg").attr("src","${rc.contextPath}/upload_temp/"+data);
                   } 
                 }
        }; 
        $("#" +formId+"").ajaxSubmit(options);
        document.getElementById(formId).reset();
        }
}
function showContract(){
	     //$("#contractCode").show();
	     $("#otherpay").hide();
	     $("#pingzhengTab").hide();
	}
	function hideContract(){
	     //$("#contractCode").hide();
	     $("#otherpay").show();
	     $("#pingzhengTab").hide();
	}
	function hideboth(){
		
	     //$("#contractCode").hide();
	     $("#otherpay").hide();
	     $("#pingzhengTab").show();
	}
	function hideall(){
		
	     //$("#contractCode").hide();
	     $("#otherpay").hide();
	     $("#pingzhengTab").hide();
	}
	function pay(tp) {
	     var paytype=$('#userFristPay :radio[name=payType]:checked').val();
	     var payPrice=$("#payPrice").val();
	     var payDate1=$("#payDate1").val();
            if(typeof(paytype)=="undefined"){
            	 layer.msg("请选择支付方式");
	         	 return;
            }
            if(payPrice==""){
            	 layer.msg("请输入金额");
	         	 return;
            }
            if(payDate1==""){
            	 layer.msg("请选择付款日期");
	         	 return;
            }
	         else if(paytype=="others"){
	             var otp=$("#otherpay  option:selected").val();
	              if(otp==""){
	              layer.msg("请选择支付方式");
	              return;
	               }else{
	               paytype=$("#otherpay option:selected").val();
	                }
	         }
		
		$.ajax({
			url : "${rc.contextPath}/order/updatePlanState",
			type : "POST",
			data : {
				"orderid" :0,
				"contractId" :${jpaPayContract.id},
				"paytype":paytype,
				"price":payPrice,
				"payDate":payDate1,
				 "tableType":1,
				 "type":1
			},
			success : function(data) {
				layer.msg(data.right);
				setTimeout('handle()',1500);
				}
		}, "text");
	}
	
	function handle(){
	  window.location.reload();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" id="contractForm"
		class="ui-form" method="post" action="${rc.contextPath}/payContract/savePayContract"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>合同分期支付 </span> 
		</div>
		<input type="hidden" value="${jpaPayContract.id}" name="id"/>
		<div class="withdrawInputs">
			<div class="inputs" >
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同编号:</label>
					<input
						  name="contractCode"  id="contractCode" value="${jpaPayContract.contractCode}"  style="height:40px;width:250px" readonly="readonly"/>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>代理客户:</label>
					<input id="customerName"  value="" style="height:40px;width:250px" readonly="readonly">
					<input id="customerId" name="customerId" value="" type="hidden">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>选择订单:</label>
					<input id="orderid" value="" name="orderid" readonly="readonly" style="height:50px;width:650px"/>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同价格(￥):</label>
					<input id="price" value="${jpaPayContract.price}" readonly="readonly" name="price" style="height:40px;width:250px;margin-top:5px;"/>
				</div>
			

			</div>
			
		</div>
		</form>
		<div id="inputs">
		
		</div>
		
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">分期计划</A>
		  <div class="withdraw-title">
	    	</div>
		</H3>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0;padding:0;">
				
	  <table id="planTableOfPlan" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				                <th>次数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>分期设置人</th>
								<th>最后操作时间</th>
								<th>备注</th>
								<@security.authorize ifAnyGranted="ShibaFinancialManager"> 
								<th>操作</th></@security.authorize>
							<@security.authorize ifNotGranted="ShibaFinancialManager"> 
								<th></th>
								</@security.authorize>
			</tr>
		</thead>
	   </table>
			</TD>
				</TR>
		</TABLE>
</div>


	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">收款详情</A>
		  <div class="withdraw-title">
		
	    	</div>
		</H3>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0;padding:0;">
				
	  <table id="payPlanTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				                <th>次数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>付款方式</th>
								<th>状态</th>
								<th>付款人</th>
								<th>操作人</th>
								<th>最后操作时间</th>
								<th>备注</th>
								<@security.authorize ifAnyGranted="ShibaFinancialManager"> 
								<th>操作</th></@security.authorize>
								
							<@security.authorize ifNotGranted="ShibaFinancialManager"> 
								<th></th>
								</@security.authorize>
			</tr>
		</thead>
	   </table>
			</TD>
				</TR>
		</TABLE>
</div>

<div id="userFristPay" class="userFristPay" >
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="#">合同分期付款</A>
			</p>
		</H3>
		<BR>
			<TABLE class="ui-table ui-table-gray">
			
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">付款金额</TD>
				<TD>
				  <input type="text" id="payPrice" value="" onkeyup="value=value.replace(/[^\d]/g,'')" /> 
				</TD>
				</TR>
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">付款日期</TD>
				<TD>
				  <input class="ui-input datepicker validate[required,custom[date],past[#payDate1]]" type="text"  value="" id="payDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">
				</TD>
			</TR>
		</TABLE>
		<TABLE class="ui-table ui-table-gray">
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付方式</TD>
				<TD>
					<input type="radio" name="payType" value="remit">汇款支付 
					<input type="radio"	name="payType" value="others">其他支付
				</TD>
				<TD>
					<div id="otherpay">
						<select class="ui-input" name="otherpay" id="otherpay">
							<option value="" selected="selected">请选择支付方式</option>
							<option value="check">支票</option>
							<option value="cash">现金</option>
						</select>
					</div>
				</TD>
			</TR>
			<tbody id="pingzhengTab">
			<TR style="height: 45px;">
				<TD style="text-align: right">上传有效银行支付凭证（可选）</TD>
				<TD colspan=3>
				  <form id="form_img" method="post" enctype="multipart/form-data"> 
                     <img src="" id="showImg" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id ="fileMaterial" name="fileMaterial" type="file" onchange="uploadImaget('form_img');"/>
                 </form>
				</TD>
			</TR>
			</tbody>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button type="button" onclick="pay('userFristPay')" class="block-btn">确认支付</button>
		</div>
		
		<div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>
			
			<font color="orange"><B>温馨提示</B></font><br>
			三方一致：合同甲方公司名称-付款方银行开户名称-开具发票抬头名称<br>
			线下付款的账户信息：<br>
			&nbsp;&nbsp;&nbsp;&nbsp;开户行：<B>工行知春路支行</B><br>
			&nbsp;&nbsp;&nbsp;&nbsp;账户名称：<b>北京世巴传媒有限公司</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;收款方账号：<b>0200207909200097152</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;公司地址：北京市海淀区紫竹院路32号15号平房	电话：68427368<br>
		</div>
		</div>

	</div>
</div>
</@frame.html>
