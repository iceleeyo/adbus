<#import "template/template.ftl" as frame>
<#import "template/pickBuses.ftl" as pickBuses>
<@frame.html title="未绑定物料订单" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js","js/layer.onload.js"] css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

<script type="text/javascript">
    $(document).ready(function() {
         $("#otherpay").hide(); 
     });
</script>


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
	    var contractid=-1;
	     var payType="";
	     var invoiceid=0;
	     var contents="";
	     var receway="";
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
	         }else if(payType=="others"){
	             var otp=$("#otherpay  option:selected").val();
	              if(otp==""){
	              jDialog.Alert("请选择支付方式");
	              return;
	               }else{
	               payType=$("#otherpay option:selected").val();
	                }
	         }else{
	            contractid=-1;
	         }
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		if(isinvoice==1){
		        contents=$("#contents  option:selected").val();
	            receway=$("#receway  option:selected").val();
	            invoiceid=$('#invoiceTab :radio[name=invoiceTit]:checked').val();
	            invoiceid=  $("#hiddenINvoiceId").val();//$(this).find("span").attr("data-aid");
	             
	            if(  (invoiceid) == "0"){
	              jDialog.Alert("请选择发票");
	              return;
	            }
	            if(contents==""){
	              jDialog.Alert("请选择发票开具内容");
	              return;
	            }
	            if(receway==""){
	              jDialog.Alert("请选择发票领取方式");
	              return;
	            }
	            
	  }
		$.ajax({
			url : "${rc.contextPath}/order/payment",
			type : "POST",
			data : {
				"orderid" :orderid,
				"taskid" :taskid,
				"contractid":contractid,
				"payType":payType,
				"isinvoice":isinvoice,
				"invoiceid":invoiceid,
				"contents":contents,
				"receway":receway
			},
			success : function(data) {
				jDialog.Alert(data.right);
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
				jDialog.Alert(data.right);
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
	     $("#otherpay").hide();
	}
	function hideContract(){
	     $("#contractCode").hide();
	    $("#otherpay").show();
	}
	function hideboth(){
	     $("#contractCode").hide();
	    $("#otherpay").hide();
	}
	$(document).ready(function(){
		$('input').on('ifChecked', function(event){
			var p =($(this).val());
			if($(this).attr("name")=='payType'){
				if(p=='contract'){
					showContract();
				}else if(p == 'online'){
					hideboth();
				}else {
				 hideContract();
				}
			}
		});
			//add by impanxh 判断开发发票
			$('input').on('ifChanged', function(event){
			if($(this).attr("id")=='invoiceShow' && $(this).attr("type") == 'checkbox'){
				var checked=document.getElementById("invoiceShow").checked;
				if(checked){
					document.getElementById("invoiceTab").style.display="";
				}else{
					document.getElementById("invoiceTab").style.display="none";
				} 
			}
		});
 
	});
	
	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}
	function del_3(o) {
		document.getElementById("newUpload3").removeChild(
				document.getElementById("div_" + o));
	}

	//Radio反选
var isChecked = false;
function qCheck(obj){
	isChecked = isChecked ? false : true;
    obj.checked = isChecked;
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
                  <LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查看套餐详细内容!" onclick="showProductlayer('${rc.contextPath}/product/ajaxdetail/',${prod.id});"  >${prod.name!''}</a></SPAN></LI>
  <LI style="width: 240px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${orderview.order.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN></LI>
  <#if orderview.payTypeString?has_content>
 				  <LI style="width: 240px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></LI>
 				  <#if orderview.payTypeString?has_content && orderview.payTypeString=="合同">
  				  <LI style="width: 240px;"><SPAN>合同号：</SPAN><SPAN class="con">${(orderview.order.contractCode)!''}</SPAN></LI>
  				   <#elseif orderview.payTypeString?has_content && orderview.payTypeString=="线上支付">
  				   <LI style="width: 240px;"><SPAN>流水号：</SPAN><SPAN class="con">123912800234</SPAN></LI>
  				   </#if>
  				   </#if>
  				    <LI style="width: 240px;"><SPAN>是否开发票：</SPAN><SPAN class="con">
 				  <#if orderview.order.isInvoice==1 >
                  <a target="_blank" href="${rc.contextPath}/order/invoiceDetail/${orderview.order.userId!''}" > 是</a>
				   <#else>
				      否    
				  </#if></SPAN></LI>
				  <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
				    <#if suppliesView.mainView.seqNumber?has_content >
  				  <LI style="width: 240px;"><SPAN>物料编号：</SPAN><SPAN class="con">${(suppliesView.mainView.seqNumber)!''}</SPAN></LI>
  				   </#if>
  				   <#if quafiles.files?has_content>
  				  <LI style="width: 240px;"><SPAN>物料详情：</SPAN><SPAN class="con"><a href="${rc.contextPath}/supplies/suppliesDetail/${(suppliesView.mainView.id)!''}">查看物料与用户资质</a></SPAN></LI>
  				  </#if>
  				   <LI style="width: 720px;"><SPAN>备注信息：</SPAN><SPAN class="con"><a class="layer-tips" tip="点击可查详细内容!" onclick="showRemark('${orderview.order.ordRemark!''}');"  >${substring(orderview.order.ordRemark,0,38)}</a></SPAN></LI>

</UL>
</DIV>
</DIV>
<div id="relateSup" >
<#if city.mediaType == 'body'>
<@pickBuses.pickBuses order=order product=prod city=city lineLevel=prod.lineLevel categories=categories/>
</#if>
              <div class="p20bs mt10 color-white-bg border-ec">
                 <H3 class="text-xl title-box" style="text-align: left;">
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
    								<Td style="padding:0,10px;" width="350">支付方式</Td>
    							<TD style="padding:0,10px;" width="550">
    										<input type="radio" name="payType" onchange="showContract()" value="contract" checked="checked">关联合同
				             		<input type="radio" name="payType" value="online" onchange="hideboth()" >线上支付
				             	<input type="radio" name="payType" value="others"  onchange="hideContract()">其他支付</TD>
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
                  		<div id="otherpay" >
				             	<select class="ui-input" name="otherpay" id="otherpay">
                                                <option value="" selected="selected">请选择支付方式</option>
                                                <option value="check" >支票</option>
                                                <option value="remit" >汇款</option>
                                                <option value="cash" >现金</option>
                  		       </select>
                  		      </div>
                  		</TD>
    									
  					</TR>
  					           <TR style="height:45px;">
    									<td >是否开发票</td>
    									<TD colspan=3>
    									    <input type="checkbox" id="invoiceShow"/>开具发票 
    									    <a href="javascript:;" onclick="IvcEnter('${rc.contextPath}')">录入发票</a>
    									    <#assign  invoicelength=( (InvoiceList?size/4)?ceiling )> 
						    
    									</TD>
				               </TR>
				            
				              <TR style="display:none;" id="invoiceTab">
				              <td>发票列表</td>
				              <TD colspan="3">
						   <div class="cart_address_wrap" id="cartAddress" style="width:540px;">
						  
				                <ul class="cart_address_list clearfix" style="height:<#if (invoicelength<1)>80px<#else>${invoicelength*100-20}px</#if>;width:550px;" id="cartAddressList">
				                  <#list InvoiceList as ilist>
				                  <li data-aid="${ilist.id}" tip="${ (ilist.type==1)?string('专用发票','普通发票')}:${ilist.title}" class="layer-tips">
				                    <span href="javascript:;"  class="cart_address_card addressCard" style="text-decoration:none;" data-aid="${ilist.id}">
				                        <p class="cart_address_zipinfo" >
				                      ${substring(ilist.title,0,11)}</p>
				                        <i class="cart_address_edit" style="display: none;"onclick="qEdit('${rc.contextPath}',${ilist.id})" id="${ilist.id}">编辑</i>
				                    </span>
				                </li>
				                </#list>
				                <input type="hidden" id="hiddenINvoiceId" value="0"/>
				              </ul>
				             </div>
				               			<table>
				               			<#if (InvoiceList?size>0)>
				               				<tr>
				               				<td colspan="2">
				               					<select style="margin: 20px;" id="contents">
				               						<option value="">请选择发票开具内容</option>
				               						<option value="广告发布费">广告发布费</option>
				               						<option value="广告制作费">广告制作费</option>
				               						<option value="其他">其他</option>
				               					</select>
				               					
				               					<select id="receway">
				               						<option value="">请选择发票领取方式</option>
				               						<option value="自取">自取</option>
				               						<option value="邮寄">邮寄</option>
				               					</select>
				               				</td>
				               				</tr>
				               				<!--
				               				<tr>
				               					<td>
				               						<a href="javascript:;" onclick="IvcEnter('${rc.contextPath}')">录入发票</a>
				               					</td>
				               				</tr>
				               				-->
				               			</#if>
				               			</table>
				               			
				               	</TD>
				               	</TR>
				               	</TBODY>
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
    									<TD colspan=3><select class="ui-input" name="supplieid" id="supplieid" style="margin: 20px;">
                                                <option value="" selected="selected">请选择物料</option>
                                                <#if supplieslist?exists>
                                                <#list supplieslist as c>
                                                    <option value="${c.id}">${c.name!''}</option>
                                                </#list>
                                                
                                                </#if>
                                       </select>
                  		               <a href="javascript:;" onclick="supEnter('${rc.contextPath}',${city.mediaType})">上传物料</a>
                  		               </TD>
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


<script type="text/javascript">
$(document).ready(function(){
  $('input').iCheck({
    checkboxClass: 'icheckbox_square-green',
    radioClass: 'iradio_square-green',
    increaseArea: '20%' // optional
  });
});
</script>
<script type="text/javascript">




  $(document).ready(function() {
       $('.cart_address_wrap ul li').click(function(){
	$('.cart_address_wrap ul li').each(function(){
		 $(this).find("span").removeClass("selected");
		var tid= $(this).attr("data-aid");
		 $("#"+tid)[0].style.display = "none"; 
	});
	var exact_id= $(this).attr("data-aid");
    $(this).find("span").addClass("selected");
    $("#hiddenINvoiceId").val(exact_id);
    $("#"+($(this).attr("data-aid")))[0].style.display = "block"; 

});

    } );
  </script> 
</@frame.html>






