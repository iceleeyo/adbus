<#import "template/template.ftl" as frame>
<#import "template/orderDetail.ftl" as orderDetail/>
<#import "template/pickBuses.ftl" as pickBuses>
<@frame.html title="订单办理" js=["js/nano.js","js/highslide/highslide-full.js", "js/video-js/video.js",
"js/video-js/lang/zh-CN.js", "js/jquery-ui/jquery-ui.min.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js","js/jquery-dateFormat.js"]
css=["js/highslide/highslide.css", "js/video-js/video-js.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>
    <#include "template/preview.ftl" />
<script type="text/javascript">
	$(function() {
	//显示当前节点对应的表单信息
	$('.${activityId!'' }').css("display","inline");
	$("#otherpay").hide();
	
});
//Radio反选
var isChecked = false;
function qCheck(obj){
	isChecked = isChecked ? false : true;
    obj.checked = isChecked;
}

function go_back() {
		history.go(-1);
}

/**
 * 完成任务
 * @param {Object} taskId
 */
function complete(taskId, variables) {
	// 转换JSON为字符串
    var keys = "", values = "", types = "";
	if (variables) {
		$.each(variables, function() {
			if (keys != "") {
				keys += ",";
				values += ",";
				types += ",";
			}
			keys += this.key;
			values += this.value;
			types += this.type;
		});
	}
	
var url="${rc.contextPath}/order/"+taskId+"/complete";
	// 发送任务完成请求
    $.post(url,{
        keys: keys,
        values: values,
        types: types
    },function(data){
    	jDialog.Alert(data.left==true?"执行成功!":"执行失败!");
    	var uptime = window.setTimeout(function(){
			var a = document.createElement('a');
    		a.href='${rc.contextPath}/order/myTask/1';
    		//a.target = 'main';
    		document.body.appendChild(a);
    		a.click();
			clearTimeout(uptime);
		},2000)
    	
    });
    
}


//世巴初审
function approve1(){ 
    var approve2ResultValue="";
	var approve1Result=$('#approve1 :radio[name=approve1Result]:checked').val();
	var quaResult=$('#approve1 :radio[name=quaResult]:checked').val();
	var approve1Comments=$('#approve1 #approve1Comments').val();
	var seqNumber=$('#approve1 #seqNumber').val();
	if(quaResult=="true" && approve1Result=="true"){
	   approve1Result="true";
	}else{
	   approve1Result="false";
	}
	if(approve1Comments==""){
	   jDialog.Alert("请填写意见");
	   return;
	}
	
	if(seqNumber!=""){
	   approve2ResultValue="true";
	}else{
	    approve2ResultValue="false";
	}
	
	
	complete('${taskid!''}',[
		{
			key: 'approve1Result',
			value: approve1Result,
			type: 'B'
		},
		{
			key: 'approve2Result',
			value: approve2ResultValue,
			type: 'B'
		},
		{
			key: 'approve1Comments',
			value: approve1Comments,
			type: 'S'
		}
	]);
}

//北广终审
function approve2(){
	var approve2Result=$('#approve2 :radio[name=approve2Result]:checked').val();
	var approve2Comments=$('#approve2 #approve2Comments').val();
	var suppliesid=$('#approve2 #suppliesid').val();
	var seqNumber=$('#approve2 #seqNumber').val();
	if(approve2Comments==""){
	   jDialog.Alert("请填写意见");
	   return;
	}
	if(seqNumber==""){
	   jDialog.Alert("请填写物料编号");
	   return;
	}
	
	bgzs(suppliesid,seqNumber,'${taskid!''}',[
		{
			key: 'approve2Result',
			value: approve2Result,
			type: 'B'
		},
		{
			key: 'approve2Comments',
			value: approve2Comments,
			type: 'S'
		}
	]);
}
function bgzs(suppliesid,seqNumber,taskId, variables) {
	// 转换JSON为字符串
    var keys = "", values = "", types = "";
	if (variables) {
		$.each(variables, function() {
			if (keys != "") {
				keys += ",";
				values += ",";
				types += ",";
			}
			keys += this.key;
			values += this.value;
			types += this.type;
		});
	}
	
	var url="${rc.contextPath}/order/"+taskId+"/complete";
	// 发送任务完成请求
    $.post(url,{
        keys: keys,
        values: values,
        types: types,
        id:suppliesid,
        seqNumber:seqNumber
        
    },function(data){
    	jDialog.Alert(data.left==true?"执行成功!":"执行失败!");
    	var uptime = window.setTimeout(function(){
			var a = document.createElement('a');
    		a.href='${rc.contextPath}/order/myTask/1';
    		//a.target = 'main';
    		document.body.appendChild(a);
    		a.click();
			   	clearTimeout(uptime);
						},2000)
    	
    });
    
}
//世巴财务审核
function financial() {
    var paymentResult=$('#financialCheck :radio[name=rad]:checked').val();
	var financialcomment=$('#financialcomment').val();
	if(financialcomment==""){
	  jDialog.Alert("请填写审核意见");
	  return;
	}
	complete('${taskid!''}',[
		{
			key: 'paymentResult',
			value: paymentResult,
			type: 'B'
		},
		{
			key: 'financialcomment',
			value: financialcomment,
			type: 'S'
		}
	]);
}
//世巴提交排期表
function submitSchedule() {
    var ScheduleResult=$('#submitSchedule :radio[name=ScheduleResult]:checked').val();
	complete('${taskid!''}',[
		{
			key: 'ScheduleResult',
			value: ScheduleResult,
			type: 'B'
		}
	]);
}

//北广录入排期表
function inputSchedule() {
    var ScheduleResult=$('#inputSchedule :radio[name=ScheduleResult]:checked').val();
	var inputScheduleComments=$("#inputScheduleComments").val();
	complete('${taskid!''}',[
		{
			key: 'ScheduleResult',
			value: ScheduleResult,
			type: 'B'
		},
		{
			key: 'inputScheduleComments',
			value: inputScheduleComments,
			type: 'S'
		}
	]);
}

//上播报告
function shangboReport() {
    var shangboResult=$('#shangboReport :radio[name=shangboResult]:checked').val();
	var shangboComments=$("#shangboComments").val();
    jDialog.Alert(shangboComments);
	complete('${taskid!''}',[
		{
			key: 'shangboResult',
			value: shangboResult,
			type: 'B'
		},
		{
			key: 'shangboComments',
			value: shangboComments,
			type: 'S'
		}
	]);
}

//监播报告
function jianboReport() {
    var jianboResult=$('#jianboReport :radio[name=jianboResult]:checked').val();
	var jianboComments=$("#jianboComments").val();
	complete('${taskid!''}',[
		{
			key: 'jianboResult',
			value: jianboResult,
			type: 'B'
		},
		{
			key: 'jianboComments',
			value: jianboComments,
			type: 'S'
		}
	]);
}
function check() {
		var c = $("#code").val();
		$.ajax({
			url : "${rc.contextPath}/contract/contractCodeCheck",
			type : "POST",
			data : {
				"code" : c
			},
			success : function(data) {
				jDialog.Alert(data.right);
			}
		}, "text");
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
		        //contents=$("#contents  option:selected").val();
	            //receway=$("#receway  option:selected").val();
	              receway=$("#receway").val();
	              contents=$("#contents").val();
	           invoiceid=  $("#hiddenINvoiceId").val();
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
	
	function modifyOrder() {
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
				jDialog.Alert(  data.right);
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
</script>
<input type="hidden" id="orderid" value="${orderview.order.id!''}"/>
<input type="hidden" id="taskid" value="${taskid!''}"/>
<div class="payment" >
<div id="process" class="section4">
    <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li><li id="track_time_0" class="tx3">  <#setting date_format="HH:mm:ss">${(orderview.order.created?date)!''}</li></ul></div>
    <div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
    <div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
    <div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
    <div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">物料审核</li><li id="track_time_1" class="tx3"></li></ul></div>
    <div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
    <div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>
    <div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
    <div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
</div>
</div>


    <@orderDetail.orderDetail orderview=orderview quafiles=quafiles suppliesView=suppliesView/>

<#if activityId == "payment" || activityId == "relateSup">
           <!-- 支付-->
           <div id="payment" class="payment relateSup" style="display: none;">
    <#if city.mediaType == 'body'>
           <@pickBuses.pickBuses order=orderview.order product=orderview.product city=city lineLevel=prod.lineLevel categories=categories/>
    </#if>
              <div class="p20bs mt10 color-white-bg border-ec">
                 <H3 class="text-xl title-box"><p style="text-align: left"><A class="black" href="#">支付订单</A></p></H3><BR>	
                 <TABLE class="ui-table ui-table-gray" >
									<!-- <TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
  								</TR>  --> 	
									<TR style="height:45px;">
    									<TD width="20%" style="text-align:right">支付方式</TD>
    									<TD>
    										<input type="radio" name="payType" onchange="showContract()" value="contract" checked="checked">关联合同
				             		<input type="radio" name="payType" value="online" onchange="hideboth()" >线上支付
				             	<input type="radio" name="payType" value="others"  onchange="hideContract()">其他支付</TD>
				             	
				             	<TD>
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
    									<TD style="text-align:right">发票信息</TD>
    									<TD colspan=3>
    									    <input type="checkbox"  id="check1"/>开具发票
 											<#assign  invoicelength=( (InvoiceList?size/4)?ceiling )> 
     									     <a href="javascript:;" onclick="IvcEnter('${rc.contextPath}')">录入发票</a>
    									</TD>
				         </TR>
				            <tbody id="invoiceTab" style="display:none;">
				             	    <TR>
				                  <td style="text-align:right">发票抬头</td>
				                   <TD colspan=3>
				                   <div class="cart_address_wrap" id="cartAddress" style="width:540px;">
						  <#if (InvoiceList?size>0)>
				                <ul class="cart_address_list clearfix" style="height:<#if (invoicelength<1)>10px<#else>${invoicelength*100-20}px</#if>;width:550px;" id="cartAddressList">
				                  <#list InvoiceList as ilist>
				                  <!-- <li data-aid="${ilist.id}" tip="${ (ilist.type==1)?string('专用发票','普通发票')}:${ilist.title}" class="layer-tips">
				                    <span href="javascript:;"  class="cart_address_card addressCard" style="text-decoration:none;" data-aid="${ilist.id}">
				                        <p class="cart_address_zipinfo" >
				                      ${substring(ilist.title,0,11)}</p>
				                        <i class="cart_address_edit" style="display: none;"onclick="qEdit('${rc.contextPath}',${ilist.id})" id="${ilist.id}">编辑</i>
				                    </span>
				                </li> -->
				          <li data-aid="${ilist.id}" tip="${ (ilist.type==1)?string('专用发票','普通发票')}:${ilist.title}" class="layer-tips">
				                    
				                     
				                    <span href="javascript:;"  class="" style="text-decoration:none;" data-aid="${ilist.id}">
				                    <div class="item"><i></i>
				                        <span class="" >
				                      ${substring(ilist.title,0,11)}
				                      <br>
				                      <b class="cart_address_edit" style="display: none;position:inherit;"onclick="qEdit('${rc.contextPath}',${ilist.id})" id="${ilist.id}">编辑</b>
				                      </span>
				                    </div>    
				                    </span>
				                    
				           </li>
				                </#list>
				         			<#else>
				                   	 暂无发票，请录入发票
				                </#if>
				                <input type="hidden" id="hiddenINvoiceId" value="0"/>
				              </ul>
				             </div>
				     </TD>
				     </TR>		
				     
				     <#if (InvoiceList?size>0)>

				               	<TR>
				               		<td style="text-align:right">发票内容</td>
				               		<td colspan="3">
				               			<!-- <select style="margin: 20px;" id="contents">
				               						<option value="">请选择发票开具内容</option>
				               						<option value="广告发布费">广告发布费</option>
				               						<option value="广告制作费">广告制作费</option>
				               						<option value="其他">其他</option>
				               			</select> -->
				               			<div id="conten">
				               			<div class="item"><i></i><a content="广告发布费" class="select-type">广告发布费</a></div>
				               			<div class="item"><i></i><a content="广告制作费" class="select-type">广告制作费</a></div>
				               			<div class="item"><i></i><a content="其他" class="select-type">其他</a></div>
				               			</div>
				               			<input type="hidden" id="contents" value=""/>
				               		</td>
				               	</TR>
				               	
				               	<TR>
				               		<td style="text-align:right">领取方式</td>
				               		<td colspan="3">
				               			<!-- <select  style="margin: 20px;" id="receway">
				               						<option value="">请选择发票领取方式</option>
				               						<option value="自取">自取</option>
				               						<option value="邮寄">邮寄</option>
				               					</select> -->
				               					<div id="rece">
				               		    <div class="item"><i></i><a recew="自取" class="select-type">自取</a></div>
				               			<div class="item"><i></i><a recew="邮寄" class="select-type">邮寄</a></div>
				               			<input type="hidden" id="receway" value=""/>
				               			</div>
				               		</td>
				               	</TR>
				               	</#if>
				            <!-- tbody结束 -->   	
				            </tbody>
								</TABLE>
								<div style="margin: 10px 0 0; text-align:center;">
									<button type="button" onclick="pay()" class="block-btn" >确认支付</button>
								</div>
    							
             </div>	
			</div>
</#if>
<#if activityId == "relateSup">
			 <!-- 支付和绑定素材-->
           <div id="relateSup" class="relateSup" style="display: none;">
<#if city.mediaType == 'body'>
       <@pickBuses.pickBuses order=orderview.order product=orderview.product city=city lineLevel=prod.lineLevel categories=categories/>
</#if>
              <div class="p20bs mt10 color-white-bg border-ec">
                 <H3 class="text-xl title-box">
                    <input type="radio" name="sel" onchange="showtb1()"  checked="checked">支付订单
				    <input type="radio" name="sel"  onchange="showtb2()" >绑定素材
                 </H3><BR>	
                 <TABLE class="ui-table ui-table-gray" id="tb1">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
  								</TR>  	
									<TR style="height:45px;">
    									<TH width="0%">支付</TH>
    									<TD>
    										<input type="radio" name="payType" onchange="showContract()" value="contract" checked="checked">关联合同
				             		<input type="radio" name="payType" value="online" onchange="hideboth()" >线上支付
				             	<input type="radio" name="payType" value="others"  onchange="hideContract()">其他支付</TD>
				             	
				             	<TD>
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
    									<TD width="25%" style="text-align:center;">
    										<button type="button" onclick="pay()" class="block-btn" >确认支付</button>
    									</TD>
  								</TR>
								</TABLE>	<br>
						<TABLE class="ui-table ui-table-gray" id="tb2">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
  								</TR>  	
									<TR style="height:45px;">
    									<TH width="0%"><div >绑定素材</div>></TH>
				             	 <div id="contractCode">
				             	  <select class="ui-input" name="supplies.id" id="suppliesId">
                                                <option value="1" selected="selected"></option>
                                                <#list supplieslist as s>
                                                    <option value="${s.id}">${s.name}</option>
                                                </#list>
                                   </select>
                  		</div>
                  		</TD>
    									<TD width="25%" style="text-align:center;">
    										
    									</TD>
  								</TR>
								</TABLE>	
								<div style="margin: 10px 0 0; text-align:center;">
									<button type="button" onclick="relate()" class="block-btn" >确认</button>
								</div>
             </div>	
			</div>
</#if>
<#if activityId == "modifyOrder">
			<!-- 广告主修改订单 -->
          <div id="modifyOrder" class="modifyOrder" style="display: none;">

              <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><p style="text-align: left"><A class="black" href="#">订单处理-广告主修改订单物料</A></p></H3><br>	
                                
								<TABLE class="ui-table ui-table-gray">
  								<TBODY>
  								<input type="hidden"  id="seqNumber" value="${suppliesView.mainView.seqNumber!''}"/>
  								<TR>
    									<TH>物料详情</TH>
    									<TD colspan=3>
    									<#list suppliesView.files as item> 
		<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a>
	   <#if prod.type=='image'>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
		<a href="${rc.contextPath}/upload_temp/${item.url!''}" target="_Blank">点击查看</a>
		</#if>
 	</#list>${(suppliesView.mainView.infoContext)!''}</TD>
    							</TR>
  								<TR>
    									<TH>更改物料</TH>
    									<TD colspan=3><select class="ui-input" name="supplieid" id="supplieid">
                                                <option value="" selected="selected">请选择物料</option>
                                                <#if supplieslist?exists>
                                                <#list supplieslist as c>
                                                    <option value="${c.id}">${c.name!''}</option>
                                                </#list>
                                                </#if>
                  		               </select>
                  		                &nbsp;&nbsp;&nbsp;
                                            	<a  href="javascript:;" onclick="supEnter('${rc.contextPath}',${city.mediaType},'${orderview.product.type.typeName!''}')">上传物料</a>
                  		               </TD>
    						   </TR>
								</TABLE>	                 
								<div style="margin: 10px 0 0; text-align:center;">
									<button onclick="modifyOrder();" class="block-btn">提交</button>
								</div>               
                </div>	          
					</div>
</#if>
<#if activityId == "approve2">
			<!-- 北广审核并填写物料ID等信息 -->
      <div id="approve2" class="approve2" style="display: none;">
                <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><p align="left"><A class="black" href="#">订单处理-北广对物料进行终审</A></p></H3><br>
								<TABLE class="ui-table ui-table-gray">
  								<TBODY>
  								<TR>
    									<TD width="20%">签收时间</TD>
    									<TD colspan=3 style="border-radius: 0 0 0"><#setting
															date_format="yyyy-MM-dd HH:mm:ss">
															${claimTime!''}</TD>
  								</TR> 
  								<TR>
    									<TD>物料信息</TD>
    									<TD colspan=3>
    									<#list suppliesView.files as item> 
		<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a>
	   <#if prod.type=='image'>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
		<a href="${rc.contextPath}/upload_temp/${item.url!''}" target="_Blank">点击查看</a>
		</#if>
 	</#list>${(suppliesView.mainView.infoContext)!''}</TD>
    							</TR> 
    							<TR>
    									<TD>物料编号</TD>
    									<input type="hidden"  id="suppliesid" value="${suppliesView.mainView.id!''}"/>
    									<TD colspan=3><input  id="seqNumber" type="text" ></TD>
    							</TR>
  								<TR>
    									<TD>审核意见</TD>
    									<TD colspan=3><textarea name="approve2Comments" id="approve2Comments" style="margin: 5px 0;width:400px;margin-top:5px;" ></textarea></TD></TR>
  								
									<TR style="height:45px;">
    									<TD>是否通过</TD>
    									<TD>
    										<input name="approve2Result" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>符合要求 
      									  <input name="approve2Result" type="radio" value="false" style="padding: 5px 15px;"/>不符合要求</TD>
    									
  								</TR>
								</TABLE>	  
								<div style="margin: 10px 0 0; text-align:center;">
									<button onclick="approve2();" class="block-btn">提交审核结果</button>
								</div>                                   							
                </div>          
				 </div>
</#if>
<#if activityId == "submitSchedule">
            <!-- 世巴提交排期表 -->
         <div id="submitSchedule" class="submitSchedule" style="display: none;">
                <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单处理-确认排期表并提交</A></H3><BR>
                <TABLE class="ui-table ui-table-gray">
  								<TBODY>
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=2 style="border-radius: 0 0 0"><#setting
															date_format="yyyy-MM-dd HH:mm:ss">
															${claimTime!''}</TD>
  								</TR>  
  								<TR>
    									<TH width="20%">排期表</TH>
    									<TD colspan=2 style="border-radius: 0 0 0"><a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
  								</TR> 


									<TR style="height:45px;">
										<TH width="20%">是否通过</TH>
    									<TD>
    										<input name="ScheduleResult" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>符合要求 
    									
  								</TR>
							</TABLE>	
							<div style="margin: 10px 0 0; text-align:center;">
									<button onclick="submitSchedule();" class="block-btn">提交确认结果</button>
								</div> 
							</div>	          
					</div>
</#if>
<#if activityId == "approve1">
					<!-- 世巴初审 -->
          <div id="approve1" class="approve1" style="display: none;">
                <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><p style="text-align: left"><A class="black" href="#">订单处理-对物料进行初审</A></p></H3><br>	
                                
								<TABLE class="ui-table ui-table-gray">
  								<TBODY>
  								<input type="hidden"  id="seqNumber" value="${suppliesView.mainView.seqNumber!''}"/>
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=3 style="border-radius: 0 0 0"><#setting
															date_format="yyyy-MM-dd HH:mm:ss">
															${claimTime!''}</TD>
  								</TR>  
  								<TR>
    									<TH>物料详情</TH>
    									<TD colspan=3>
    									<#list suppliesView.files as item> 
		<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">  ${item.name!''}</a>
	   <#if prod.type=='image'>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
		<a href="${rc.contextPath}/upload_temp/${item.url!''}" target="_Blank">点击查看</a>
		</#if>
 	</#list>${(suppliesView.mainView.infoContext)!''}</TD>
    							</TR>
  								
  								
									<TR style="height:45px;">
    									<TH>物料审核</TH>
    									<TD colspan=3>
    										<input name="approve1Result" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>符合要求 
      									<input name="approve1Result" type="radio" value="false" style="padding: 5px 15px;"/>不符合要求</TD>
  								</TR>
									<TR style="height:45px;">
    									<TH>资质审核</TH>
    									<TD colspan=3>
    										<input name="quaResult" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>符合要求 
      									<input name="quaResult" type="radio" value="false" style="padding: 5px 15px;"/>不符合要求</TD>
  								</TR>
  								<TR>
    									<TH>审核意见</TH>
    									<TD colspan=3><textarea name="approve1Comments" id="approve1Comments" style="margin: 5px 0;width:400px;margin-top:5px;"></textarea></TD>
    						   </TR>
								</TABLE>	
								<div style="margin: 10px 0 0; text-align:center;">
									<button onclick="approve1();" class="block-btn">提交审核结果</button>
								</div>                                 
                </div>	          
					</div>
</#if>
<#if activityId == "financialCheck">
					<!-- 世巴财务确认 -->
					<div id="financialCheck" class="financialCheck" style="display: none;">
						<div class="p20bs mt10 color-white-bg border-ec">
              <H3 class="text-xl title-box"><A class="black" href="#">订单处理-财务确认</A></H3><br>
              
							<TABLE class="ui-table ui-table-gray">
  								<TBODY>
								<TR>
    									<TD width="20%">签收时间</TD>
    									<TD colspan=3 style="border-radius: 0 0 0"><#setting
															date_format="yyyy-MM-dd HH:mm:ss">
															${claimTime!''}</TD>
  								</TR>  
  								<TR>
    									<TD>审核意见</TD>
    									<TD colspan=3><textarea name="financialcomment" id="financialcomment" style="margin: 5px 0;width:400px;margin-top:5px;"></textarea></TD></TR>
									<TR style="height:45px;">
    									<TD>支付状态</TD>
    									<TD>
    										<input name="rad" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>支付正常 
      									<input name="rad" type="radio" value="false" style="padding: 5px 15px;"/>支付异常</TD>
    									
  								</TR>
							</TABLE>	               
              				<div style="text-align:center; margin-top:10px;">
    	 										<button onclick="financial();" class="block-btn">提交确认结果</button>
    									</div>			
             </div>	          
					</div>
</#if>

<#if activityId == "inputSchedule">
					<!-- 北广录入排期表 -->
          <div id="inputSchedule" class="inputSchedule" style="display: none;">
              <div class="p20bs mt10 color-white-bg border-ec">
              <H3 class="text-xl title-box"><A class="black" href="#">订单办理-北广录入排期表</A></H3><BR>					
              
              <TABLE class="ui-table ui-table-gray">
  								<TBODY> 	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD style="border-radius: 0 0 0"><#setting
															date_format="yyyy-MM-dd HH:mm:ss">
															${claimTime!''}</TD>
  								</TR>  
  								<TR>
    									<TH width="20%">排期表</TH>
    									<TD style="border-radius: 0 0 0"><a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
  								</TR> 
  								<TR>
    									<TH>排期意见</TH>
    									<TD><textarea name="inputScheduleComments" id="inputScheduleComments" style="margin: 5px 0;width:400px;margin-top:5px;"></textarea></TD>
    							</TR>

							</TABLE>	
							<div style="text-align:center; margin-top:10px;">
   	 										<button onclick="financial();" class="block-btn">提交确认结果</button>
   									</div>	 
              
              </div>	          
							</div>
</#if>
<#if activityId == "shangboReport">
							<!-- 上播报告 -->
              <div id="shangboReport" class="shangboReport" style="display: none;">
              <div class="p20bs mt10 color-white-bg border-ec">
              <H3 class="text-xl title-box"><A class="black" href="#">订单办理-提交上播报告</A></H3><BR>
              <TABLE class="ui-table ui-table-gray">
  								<TBODY>
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=2 style="border-radius: 0 0 0"><#setting
															date_format="yyyy-MM-dd HH:mm:ss">
															${claimTime!''}</TD>
  								</TR>  
  								<TR>
    									<TH width="20%">排期表</TH>
    									<TD colspan=2 style="border-radius: 0 0 0"><a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
  								</TR> 
  								<TR>
    									<TH>上播意见</TH>
    									<TD colspan=2><textarea name="shangboComments" id="shangboComments" style="margin: 5px 0;width:400px;margin-top:5px;">您的广告按照合同要求已安排上播</textarea></TD></TR>
									<TR style="height:45px;">
										  <TH>是否上播</TH>
										  <TD style="border-radius: 0 0 0">
										  <input name="shangboResult" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>是
      								     <input name="shangboResult" type="radio" value="false" style="padding: 5px 15px;"/>否</TD>
										  
    									<TD width="30%" style="text-align:center;">
    	 										
    									</TD>
  								</TR>
							</TABLE>	                                 		
							<div style="margin: 10px 0 0; text-align:center;">
									<button onclick="shangboReport();" class="block-btn">提交确认结果</button>
								</div>					
							
              </div>	          
							</div>
</#if>
<#if activityId == "jianboReport">
							<!-- 监播报告 -->
              <div id="jianboReport" class="jianboReport" style="display: none;">
					    <div class="p20bs mt10 color-white-bg border-ec">
                  <H3 class="text-xl title-box"><A class="black" href="#">订单处理-提交监播报告</A></H3><BR>
							
							    <TABLE class="ui-table ui-table-gray">
  								<TBODY>	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD  style="border-radius: 0 0 0"><#setting
															date_format="yyyy-MM-dd HH:mm:ss">
															${claimTime!''}</TD>
  								</TR>  
  								<TR>
    									<TH width="20%">排期表</TH>
    									<TD  style="border-radius: 0 0 0"><a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
  								</TR> 
  								<TR>
    									<TH>监播意见</TH>
    									<TD ><textarea name="jianboComments" id="jianboComments"   style="margin: 5px 0;width:400px;margin-top:5px;">您的广告已按照合同要求正常播出中</textarea></TD></TR>
									<TR style="height:45px;">
										  <TH>上播状态</TH>
										  <TD style="border-radius: 0 0 0">
										  <input name="jianboResult" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>播放正常
      								<input name="jianboResult" type="radio" value="false" style="padding: 5px 15px;"/>播放异常</TD>
										  
    									
  								</TR>
							</TABLE>	  
							<div style="margin: 10px 0 0; text-align:center;">
									<button onclick="jianboReport();" class="block-btn">提交确认结果</button>
								</div>		
							
	
              </div>	        
							</div>
</#if>
    <#include "template/hisDetail.ftl" />
    
    
    
<script type="text/javascript">


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
			if($(this).attr("id")=='check1' && $(this).attr("type") == 'checkbox'){
				var checked=document.getElementById("check1").checked;
				if(checked){
					document.getElementById("invoiceTab").style.display="";
				}else{
					document.getElementById("invoiceTab").style.display="none";
				} 
			}
		});
 
	});
$(document).ready(function(){
  $('input').iCheck({
    checkboxClass: 'icheckbox_square-green',
    radioClass: 'iradio_square-green',
    increaseArea: '10%' // optional
  });
  
  $('.item').click(function(){
    $(this).parent().children().removeClass('selected');
  	$(this).addClass('selected');
  	$(this).children().show();
  	
  });
  
  $('.tab.block-btn').click(function(){
		$(this).parent().children().addClass('btn-gray');
		$(this).removeClass('btn-gray');
	});
});
</script>

<script type="text/javascript">



$(document).ready(function(){
   $('#conten .item').click(function(){
    $(this).parent().children().removeClass('selected');
  	$(this).addClass('selected');
  	$(this).children().show();
  	$('#conten .item').each(function(){
		 if($(this).hasClass("selected")){
		   $("#contents").val($(this).find("a").attr("content"));
		 }
		
	});
  });
   $('#rece .item').click(function(){
    $(this).parent().children().removeClass('selected');
  	$(this).addClass('selected');
  	$(this).children().show();
  	$('#rece .item').each(function(){
		 if($(this).hasClass("selected")){
		   $("#receway").val($(this).find("a").attr("recew"));
		 }
		
	});
  });
  
       $('.cart_address_wrap ul li').click(function(){
	$('.cart_address_wrap ul li').each(function(){
		 $(this).find("div").removeClass("selected");
		var tid= $(this).attr("data-aid");
		 $("#"+tid)[0].style.display = "none"; 
		
	});
	var exact_id= $(this).attr("data-aid");
    $(this).find("div").addClass("selected");
    $("#hiddenINvoiceId").val(exact_id);
    $("#"+exact_id)[0].style.display = "inline-block"; 

});

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
			if($(this).attr("id")=='check1' && $(this).attr("type") == 'checkbox'){
				var checked=document.getElementById("check1").checked;
				if(checked){
					document.getElementById("invoiceTab").style.display="";
				}else{
					document.getElementById("invoiceTab").style.display="none";
				} 
			}
		});
 
	});
</script>
</@frame.html>