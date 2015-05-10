<#import "template/template.ftl" as frame>
<#import "macro/materialPreview.ftl" as preview>

<@frame.html title="订单办理" js=["js/highslide/highslide-full.js", "js/video-js/video.js", "js/video-js/lang/zh-CN.js"]
css=["js/highslide/highslide.css", "js/video-js/video-js.css"]>
    <#include "template/preview.ftl" />

<script type="text/javascript">
	$(function() {
	//显示当前节点对应的表单信息
	$('#${activityId!'' }').css("display","inline");
});
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
    	var a = document.createElement('a');
    	a.href='${rc.contextPath}/order/myTask/1';
    	//a.target = 'main';
    	document.body.appendChild(a);
    	a.click();
    });
    
}


//世巴初审
function approve1(){ 
    var approve2ResultValue="";
	var approve1Result=$('#approve1 :radio[name=approve1Result]:checked').val();
	var approve1Comments=$('#approve1 #approve1Comments').val();
	var seqNumber=$('#approve1 #seqNumber').val();
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
    	var a = document.createElement('a');
    	a.href='${rc.contextPath}/order/myTask/1';
    	//a.target = 'main';
    	document.body.appendChild(a);
    	a.click();
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
	    var contractid="";
	     var payType="";
	     var temp=document.getElementsByName("payType");
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
				"payType":payType
			},
			success : function(data) {
				jDialog.Alert(data.left + " # " + data.right);
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myOrders/1';
            	document.body.appendChild(a);
             	a.click();
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
				jDialog.Alert(data.left + " # " + data.right);
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myOrders/1';
            	document.body.appendChild(a);
             	a.click();
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
<input type="hidden" id="orderid" value="${orderview.order.id!''}"/>
<input type="hidden" id="taskid" value="${taskid!''}"/>
           <!-- 支付-->
           <div id="payment" style="display: none;">
              <div id="process" class="section4">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">物料审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            	</div>
                <#include "template/orderDetail.ftl" />
              <div class="p20bs mt10 color-white-bg border-ec">
                 <H3 class="text-xl title-box"><A class="black" href="#">支付订单</A></H3><BR>	
                 <TABLE class="ui-table ui-table-gray">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
  								</TR>  	
									<TR style="height:45px;">
    									<TH width="0%">支付</TH>
    									<TD>
    										<input type="radio" name="payType" onchange="showContract()" value="contract" checked="checked">关联合同
				             		<input type="radio" name="payType" value="online" onchange="hideContract()" >线上支付
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
                  		</TD>
    									<TD width="25%" style="text-align:center;">
    										<button type="button" onclick="pay()" class="block-btn" >确认支付</button>
    									</TD>
  								</TR>
								</TABLE>	<br>
             </div>	
             <#include "template/hisDetail.ftl" />
			</div> 
			<!-- 广告主修改订单 -->
          <div id="modifyOrder" style="display: none;">	
                             <#include "template/orderDetail.ftl" />
                <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单处理</A></H3><br>	
                                
								<TABLE class="ui-table ui-table-gray">
  								<TBODY>
  								<input type="hidden"  id="seqNumber" value="${suppliesView.mainView.seqNumber!''}"/>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主修改订单物料</H4></TD>
  								</TR>  	
  								<TR>
    									<TH>物料详情</TH>
    									<TD colspan=3>
                                            <@preview.materialPreview view=suppliesView/>
                                        </TD>
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
                  		               </select></TD>
    						   </TR>
    						   <TR>
    						   <TD colspan=4 align="center">
									<button onclick="modifyOrder();" class="block-btn">提交</button>
									</TD>
  								</TR>
								</TABLE>	                                
                </div>	          
                               <#include "template/hisDetail.ftl" />
					</div>
			<!-- 北广审核并填写物料ID等信息 -->
      <div id="approve2" style="display: none;">	
                               <#include "template/orderDetail.ftl" />
                <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单处理</A></H3><br>
								<TABLE class="ui-table ui-table-gray">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>北广对物料进行终审</H4></TD>
  								</TR>  	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=3 style="border-radius: 0 0 0">2015-1-30 10:30:30</TD>
  								</TR> 
  								<TR>
    									<TH>物料</TH>
    									<TD colspan=3>
                                            <@preview.materialPreview view=suppliesView/>
                                        </TD>
    							</TR> 
    							<TR>
    									<TH>填写物料编号</TH>
    									<input type="hidden"  id="suppliesid" value="${suppliesView.mainView.id!''}"/>
    									<TD colspan=3><input  id="seqNumber" type="text" ></TD>
    							</TR>
  								<TR>
    									<TH>审核意见</TH>
    									<TD colspan=3><textarea name="approve2Comments" id="approve2Comments" style="margin: 5px 0;width:400px;height:120px; margin-top:5px;" ></textarea></TD></TR>
  								
									<TR style="height:45px;">
    									<TH>是否通过</TH>
    									<TD>
    										<input name="approve2Result" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>符合要求 
      									  <input name="approve2Result" type="radio" value="false" style="padding: 5px 15px;"/>不符合要求</TD>
    									
  								</TR>
  								<TR height="60">
  									<TD colspan=4 style="text-align:center;">
    	 										<button onclick="approve2();" class="block-btn">提交审核结果</button>
    								</TD>
  								</TR>
								</TABLE>	                                    							
                </div>          
                 <#include "template/hisDetail.ftl" />
				 </div>
							
			   <!-- 世巴提交排期表 -->
         <div id="submitSchedule" style="display: none;">	
                             <#include "template/orderDetail.ftl" />
                <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单处理</A></H3><BR>
                <TABLE class="ui-table ui-table-gray">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD colspan=3 width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>世巴确认排期表并提交</H4></TD>
  								</TR>  	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=2 style="border-radius: 0 0 0">2015-1-30 10:30:30</TD>
  								</TR>  
  								<TR>
    									<TH width="20%">排期表</TH>
    									<TD colspan=2 style="border-radius: 0 0 0"><a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
  								</TR> 


									<TR style="height:45px;">
										<TH>是否通过</TH>
    									<TD>
    										<input name="ScheduleResult" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>符合要求 
      									<input name="ScheduleResult" type="radio" value="false" style="padding: 5px 15px;"/>不符合要求</TD>
    									<TD width="30%" style="text-align:center;">
    	 										<button onclick="submitSchedule();" class="block-btn">提交确认结果</button>
    									</TD>
  								</TR>
							</TABLE>	 
							</div>	          
                    <#include "template/hisDetail.ftl" />
					</div>
					<!-- 世巴初审 -->
          <div id="approve1" style="display: none;">	
                             <#include "template/orderDetail.ftl" />
                <div class="p20bs mt10 color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单处理</A></H3><br>	
                                
								<TABLE class="ui-table ui-table-gray">
  								<TBODY>
  								<input type="hidden"  id="seqNumber" value="${suppliesView.mainView.seqNumber!''}"/>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>世巴对物料进行初审</H4></TD>
  								</TR>  	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=3 style="border-radius: 0 0 0">2015-1-30 10:30:30</TD>
  								</TR>  
  								<TR>
    									<TH>物料详情</TH>
    									<TD colspan=3>
                                            <@preview.materialPreview view=suppliesView/>
                                        </TD>
    							</TR>
  								<TR>
    									<TH>审核意见</TH>
    									<TD colspan=3><textarea name="approve1Comments" id="approve1Comments" style="margin: 5px 0;width:400px;margin-top:5px;"></textarea></TD>
    						   </TR>
  								
									<TR style="height:45px;">
    									<TH>是否通过</TH>
    									<TD>
    										<input name="approve1Result" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>符合要求 
      									<input name="approve1Result" type="radio" value="false" style="padding: 5px 15px;"/>不符合要求</TD>
    									<TD colspan=2 width="30%" style="text-align:center;">
    	 										<button onclick="approve1();" class="block-btn">提交审核结果</button>
    									</TD>
  								</TR>
								</TABLE>	                                
                </div>	          
                               <#include "template/hisDetail.ftl" />
					</div>
							
					<!-- 世巴财务确认 -->
					<div id="financialCheck" style="display: none;">
							   <#include "template/orderDetail.ftl" />
						<div class="p20bs mt10 color-white-bg border-ec">
              <H3 class="text-xl title-box"><A class="black" href="#">订单处理</A></H3><br>	
              
							<TABLE class="ui-table ui-table-gray">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>世巴财务确认</H4></TD>
  								</TR>  	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=3 style="border-radius: 0 0 0">2015-1-30 10:30:30</TD>
  								</TR>  
  								<TR>
    									<TH>审核意见</TH>
    									<TD colspan=3><textarea name="financialcomment" id="financialcomment" style="margin: 5px 0;width:400px;margin-top:5px;"></textarea></TD></TR>
									<TR style="height:45px;">
    									<TH>支付状态</TH>
    									<TD>
    										<input name="rad" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>支付正常 
      									<input name="rad" type="radio" value="false" style="padding: 5px 15px;"/>支付异常</TD>
    									<TD colspan=2 width="30%" style="text-align:center;">
    	 										<button onclick="financial();" class="block-btn">提交确认结果</button>
    									</TD>
  								</TR>
							</TABLE>	               
              							
             </div>	          
              <#include "template/hisDetail.ftl" />
					</div>
							 
								    
							
					<!-- 北广录入排期表 -->
          <div id="inputSchedule" style="display: none;">
                             <#include "template/orderDetail.ftl" />	
              <div class="p20bs mt10 color-white-bg border-ec">
              <H3 class="text-xl title-box"><A class="black" href="#">订单办理</A></H3><BR>					
              
              <TABLE class="ui-table ui-table-gray">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD colspan=3 width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>北广录入排期表</H4></TD>
  								</TR>  	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=3 style="border-radius: 0 0 0">2015-1-30 10:30:30</TD>
  								</TR>  
  								<TR>
    									<TH width="20%">排期表</TH>
    									<TD colspan=3 style="border-radius: 0 0 0"><a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
  								</TR> 
  								<TR>
    									<TH>排期意见</TH>
    									<TD colspan=3><textarea name="inputScheduleComments" id="inputScheduleComments" style="margin: 5px 0;width:400px;margin-top:5px;"></textarea></TD></TR>
								<TR style="height:45px;">
    									<TD colspan=4 width="30%" style="text-align:center;">
    	 										<button onclick="inputSchedule();" class="block-btn">提交确认结果</button>
    									</TD>
  								</TR>
							</TABLE>	 
              
              </div>	          
                               <#include "template/hisDetail.ftl" />
							</div>
							
							
							<!-- 上播报告 -->
              <div id="shangboReport" style="display: none;">
              <#include "template/orderDetail.ftl" />
              <div class="p20bs mt10 color-white-bg border-ec">
              <H3 class="text-xl title-box"><A class="black" href="#">订单办理</A></H3><BR>
              <TABLE class="ui-table ui-table-gray">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=3 style="border-radius: 5px 5px 0 0;"><H4>世巴提交上播报告</H4></TD>
  								</TR>  	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=2 style="border-radius: 0 0 0">2015-1-30 10:30:30</TD>
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
    	 										<button onclick="shangboReport();" class="block-btn">提交确认结果</button>
    									</TD>
  								</TR>
							</TABLE>	                                 							
							
              </div>	          
                               <#include "template/hisDetail.ftl" />
							</div>
							
							<!-- 监播报告 -->
              <div id="jianboReport" style="display: none;">	
                               <#include "template/orderDetail.ftl" />
					    <div class="p20bs mt10 color-white-bg border-ec">
                  <H3 class="text-xl title-box"><A class="black" href="#">订单处理</A></H3>								
							
							    <TABLE class="ui-table ui-table-gray">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=3 style="border-radius: 5px 5px 0 0;"><H4>世巴提交监播报告</H4></TD>
  								</TR>  	
  								<TR>
    									<TH width="20%">签收时间</TH>
    									<TD colspan=2 style="border-radius: 0 0 0">2015-1-30 10:30:30</TD>
  								</TR>  
  								<TR>
    									<TH width="20%">排期表</TH>
    									<TD colspan=2 style="border-radius: 0 0 0"><a target="_blank" href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
  								</TR> 
  								<TR>
    									<TH>监播意见</TH>
    									<TD colspan=2><textarea name="jianboComments" id="jianboComments"   style="margin: 5px 0;width:400px;margin-top:5px;">您的广告已按照合同要求正常播出中</textarea></TD></TR>
									<TR style="height:45px;">
										  <TH>上播状态</TH>
										  <TD style="border-radius: 0 0 0">
										  <input name="jianboResult" type="radio" value="true" checked="checked" style="padding: 5px 15px;"/>播放正常
      								<input name="jianboResult" type="radio" value="false" style="padding: 5px 15px;"/>播放异常</TD>
										  
    									<TD width="30%" style="text-align:center;">
    	 										<button onclick="jianboReport();" class="block-btn">提交确认结果</button>
    									</TD>
  								</TR>
							</TABLE>	  
							
	
              </div>	        
                               <#include "template/hisDetail.ftl" />
							</div>

</@frame.html>