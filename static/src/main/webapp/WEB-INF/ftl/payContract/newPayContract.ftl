<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "../template/template.ftl" as frame> <#global menu="创建合同">
<@frame.html title="创建合同 " js=["../js/jquery-ui/jquery-ui.js","../js/datepicker.js","../js/jquery.datepicker.region.cn.js",
"../index_js/sift_common.js","../js/jquery-ui/jquery-ui.auto.complete.js","../js/jquery-dateFormat.js"]
css=["../js/jquery-ui/jquery-ui.css","../css/autocomplete.css","../js/jquery-ui/jquery-ui.auto.complete.css"]>


<script type="text/javascript">
function go_back(){
	history.go(-1);
}
    $(document).ready(function() {
    var seriaNum='${seriaNum}';
       initPlan('${rc.contextPath}',0,'<@security.authorize
			ifAnyGranted="sales">edit_del</@security.authorize>','contract',seriaNum);
        
        
    });
</script>

<script type="text/javascript">
function split( val ) {
      return val.split( /,\s*/ );
    }
	$(document).ready(function() {
	
	
			$("#salesNameId").autocomplete({
        minLength: 0,
        source : "${rc.contextPath}/user/salesAutoComplete",
        focus: function() {
          return false;
        },
        select: function( event, ui ) {
         $('#salesNameId').val(ui.item.value);
           return false;
        },
        change: function( event, ui ) { 
		  			 },
      }).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
	
	
	
			 $( "#customerName" ).autocomplete({
		        	minLength: 0,
		  			source: "${rc.contextPath}/user/queryMyCustomers",
		  			change: function( event, ui ) { 
		  				$("#orderid").val("");
		  				$("#inputs").html("");
		  			 },
		  			 select: function(event,ui) {
		  			 	$('#customerName').val(ui.item.value);  
		  			 	$('#customerId').val(ui.item.dbId);  
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
		 var price=0;
			$.each(data,function(i,item){
			var ischange='否';
			if(item.order.isChangeOrder){
			ischange='是';
			}
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
			+"<li style=\"width: 200px;\"><SPAN>是否是否为换版订单：</SPAN>"+ischange+"</li>"
			+"</UL></DIV></DIV>"
			price+=item.order.price;
				});
				$("#inputs").html(op);
				$("#price").val(price);
				}
		});
}

	function sub(){
	if (!$("#contractForm").validationEngine('validateBeforeSubmit'))
			return;
		$('#contractForm').ajaxForm(function(data) {
				if(data.right){
					layer.msg("保存成功");
						layer.confirm('保存成功！', {
						  btn: ['去支付页面','回到合同列表'] 
						}, function(){
							 window.location.href="${rc.contextPath}/payContract/toRestPayContract/"+data.hold.contractInfo.id;		
						}, function(){
						  window.location.href="${rc.contextPath}/payContract/list";
						  
						});
				}else {
					layer.msg("操作失败");
				}
				<#--
				window.setTimeout(function(){
				window.location.href="${rc.contextPath}/payContract/list",
				   	clearTimeout(uptime);
							},2000) -->
		}).submit();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" id="contractForm"
		class="ui-form" method="post" action="${rc.contextPath}/payContract/savePayContract"
		enctype="multipart/form-data">
		<div class="withdraw-title fn-clear">
			<span>创建合同 </span> 
		</div>
		
		<input type="hidden" name="seriaNum" value="${seriaNum}"/>
		<div class="withdrawInputs">
			<div class="inputs" >
				<#--<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同编号:</label>
					<input
						class="ui-input  validate[required,minSize[2],maxSize[100]]"
						type="text" name="contractCode" id="contractCode" value="${contractCode}" readonly="readonly"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder=""/>
				</div>-->
				
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>业务员:</label>
					<input id="salesNameId2" name="salesName"  value="" style="height:40px;width:250px">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>选择客户:</label>
					<input id="customerName"  value="" style="height:40px;width:250px">
					<input id="customerId" name="customerId" value="" type="hidden">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>选择订单:</label>
					<textarea id="orderid" value="" name="orderid" style="height:70px;width:650px"></textarea>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>合同价格(￥):</label>
					<input id="price" value="" name="price" style="height:30px;width:200px;margin-top:5px;"/>
				</div>
				
				<div class="ui-form-item">
					<label class="ui-label mt10">备注:</label>
					<textarea id="remark"   name="remark"  type="textarea" style="height: 151px; width: 655px;"></textarea>
				</div>
			

			</div>
			
		</div>
		<div id="inputs">
		
		</div>
		
		<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">分期计划</A>
			<div class="withdraw-title">
			 <a class="block-btn"
						style="margin-top: -30px;" href="javascript:void(0);"
						onclick="addPayPlan('${rc.contextPath}',0,${seriaNum},1,0)">添加分期</a>	</div>
		</H3>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0;padding:0;">
				
	<table id="planTableOfPlan" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				                <th>期数</th>
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
		
		    <div class="widthdrawBtBox">
				<input type="button" id="subWithdraw" class="block-btn"
					onclick="sub();" value="提交">
			</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示,合同相关事项
			</div>
			<ol>
				<li>1:分期信息录入</li>
				<li>2:分期信息录入后可在  待办事项中 ->'待支付合同' 进行支付</li>
				<li>3:财务可在  待办事项中 ->'待收款确认合同' 菜单中对用户支付进行确认</li>
				<li>4:销售可在订单进行中对订单进行关闭操作 ,适用于订单未完成所有分期提前关闭的场景</li>
				<li>   4.1:如果相应的合同有订单支付了 账务未处理 请先联系财务进行收款确认 再关闭</li>
			</ol>
		</div>
	
</div>
</@frame.html>
