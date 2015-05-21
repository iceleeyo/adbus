<#import "template/template.ftl" as frame>
<#import "template/pickBuses.ftl" as pickBuses>
<@frame.html title="未绑定物料订单" js=["js/jquery-ui/jquery-ui.min.js"]>
<script type="text/javascript">

$(function() {
	$("#tb1").show();
	     $("#tb2").hide();
});
function go_back(){
	history.go(-1);
	
}
function showtb1(){
	     $("#tb1").show();
	     $("#tb2").hide();
	}
	function showtb2(){
	     $("#tb1").hide();
	     $("#tb2").show();
	}
	function pay() {
	    var contractid="";
	     var payType="";
	     var temp=document.getElementsByName("payType");
	       var isinvoice=0;
	       if($("input[type='checkbox']").is(':checked')==true){
	       isinvoice=1;
	       }else{
	       isinvoice=0;
	       }
	      for(var i=0;i<temp.length;i++)
         {
           if(temp[i].checked)
            payType = temp[i].value;
         }
	        if(payType=="contract"){
	            contractid=$("#contractCode  option:selected").val();
	            if(contractid==""){
	              jDialog.Alert("请选择合同");
	              return;
	            }
	         }else{
	            contractid=-1;
	         }
	   
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "${rc.contextPath}/order/payment",
			type : "POST",
			data : {
				"orderid" :orderid,
				"taskid" :taskid,
				"contractid":contractid,
				"payType":payType,
				"isinvoice":isinvoice
			},
			success : function(data) {
				jDialog.Alert(data.right);
				var uptime = window.setTimeout(function(){
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myTask/1';
            	document.body.appendChild(a);
             	a.click();
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}
	function relatSup() {
	        var supplieid=$("#supplieid  option:selected").val();
	            if(supplieid==""){
	              jDialog.Alert("请选择物料");
	              return;
	            }
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "${rc.contextPath}/order/modifyOrder",
			type : "POST",
			data : {
				"orderid" :orderid,
				"taskid" :taskid,
				"supplieid":supplieid
			},
			success : function(data) {
				jDialog.Alert(data.left + " # " + data.right);
				var uptime = window.setTimeout(function(){
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myOrders/1';
            	document.body.appendChild(a);
             	a.click();
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}
	function showContract(){
	     $("#contractCode").show();
	}
	function hideContract(){
	     $("#contractCode").hide();
	}
</script>
<div class="color-white-bg fn-clear">
  <div id="process" class="section4">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付与绑定物料</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">物料审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            	</div>
							  <DIV class="p20bs color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单详情-${orderview.longOrderId!''}</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
                  <LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>订单状态：</SPAN><SPAN class="con">已完成</SPAN></LI>
  <LI style="width: 240px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>合同号：</SPAN><SPAN class="con">${(orderview.order.contractCode)!''}</SPAN></LI>
  <LI style="width: 720px;"><SPAN>物料列表：</SPAN><SPAN class="con"><#list suppliesView.files as item>
      <a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a> &nbsp;&nbsp; &nbsp;
    </#list>${(suppliesView.mainView.infoContext)!''}</SPAN></LI>
   <LI style="width: 720px;"><SPAN>用户资质：</SPAN><SPAN class="con"><#list quafiles.files as item>
      <a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a> &nbsp;&nbsp; &nbsp;
    </#list></SPAN></LI>
</UL>
</DIV>
</DIV>
<div id="relateSup" >
<#if city.mediaType == 'body'>
<@pickBuses.pickBuses order=order product=prod city=city lineLevel=prod.lineLevel categories=categories/>
</#if>
              <div class="p20bs mt10 color-white-bg border-ec">
                 <H3 class="text-xl title-box">
                    <input type="button"  onclick="showtb1()" class="block-btn" value="支付订单">
                    <#if order?exists>
                       <#if order.supplies.id = 1 >
                        	<input type="button"  onclick="showtb2()" class="block-btn" value="绑定素材">
                          </#if>
                    </#if>
				 
                 </H3><BR>	
                 <TABLE class="ui-table ui-table-gray" id="tb1">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
  								</TR>  	
									<TR style="height:45px;">
    								<TH style="padding:0,10px;">支付方式</TH>
    							<TD style="padding:0,10px;">
    										<input type="radio" name="payType" onchange="showContract()" value="contract" checked="checked">关联合同
				             		<input type="radio" name="payType" value="online" onchange="hideContract()" >线上支付
				             	<input type="radio" name="payType" value="others"  onchange="hideContract()">其他支付
				             	</TD>
				             	
				             	<TD style="padding:0,10px;">
				             	<div id="contractCode">
				             	<select class="ui-input" name="contractCode" id="contractCode">
                                                <option value="" selected="selected">请选择合同</option>
                                                <#if contracts?exists>
                                                <#list contracts as c>
                                                    <option value="${c.id}">${c.contractName!''}</option>
                                                </#list>
                                                </#if>
                  		</select>
                  		</div>
                  		</TD>
    									
  					</TR>
  					           <TR style="height:45px;">
    									<TH >是否开具发票</TH>
    									<TD colspan=3>
    									    <input type="checkbox"  id="check1"/>开具发票
    									</TD>
				             	    </TR>
  								<TR>
    						        <TD colspan="4" style="text-align:center;">
    										<button type="button" onclick="pay()" class="block-btn" >确认支付</button>
    									</TD>
  								</TR>
								</TABLE>	<br>
						<TABLE class="ui-table ui-table-gray" id="tb2">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD colspan=4 style="border-radius: 5px 5px 0 0;"><H4>绑定物料</H4></TD>
  								</TR>  	
									<TR style="height:45px;">
    									<TH width="0%">绑定素材</TH>
    									<TD colspan=3><select class="ui-input" name="supplieid" id="supplieid">
                                                <option value="" selected="selected">请选择物料</option>
                                                <#if supplieslist?exists>
                                                <#list supplieslist as c>
                                                    <option value="${c.id}">${c.name!''}</option>
                                                </#list>
                                                </#if>
                  		               </select></TD>
				             	    </TR>
				             	  <TR style="height:45px;">
    						        <TD colspan=4 align="center">
									<button type="button" onclick="relatSup()" class="block-btn" >确认</button><br>
									</TD>
  								</TR>
								</TABLE>	<br>
             </div>	
			</div> 
			<input type="hidden" id="orderid" value="${orderview.order.id!''}"/>
<input type="hidden" id="taskid" value="${taskid!''}"/>
</div>
</@frame.html>






