<#import "template/template.ftl" as frame>

<@frame.html title="公交广告交易系统">
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
    	alert(data.left==true?"执行成功!":"执行失败!");
    	var a = document.createElement('a');
    	a.href='${rc.contextPath}/order/myTask/1';
    	//a.target = 'main';
    	document.body.appendChild(a);
    	a.click();
    });
    
}

//世巴初审
function approve1(){
	var approve1Result=$('#approve1 :radio[name=approve1Result]:checked').val();
	var approve1Comments=$('#approve1 #approve1Comments').val();
	complete('${taskid!''}',[
		{
			key: 'approve1Result',
			value: approve1Result,
			type: 'B'
		},
		{
			key: 'approve2Result',
			value: "false",
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
	complete('${taskid!''}',[
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

//世巴财务审核
function financial() {
    var paymentResult=$('#financialCheck :radio[name=rad]:checked').val();
	var financialcomment=$('#financialcomment').val();
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
	var ctx = '<%=request.getContextPath() %>';
		var c = $("#code").val();
		$.ajax({
			url : "${rc.contextPath}/contract/contractCodeCheck",
			type : "POST",
			data : {
				"code" : c
			},
			success : function(data) {
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
	function pay() {
	var payType="";
	     var rad=document.getElementsByName("payType");
	     if(rad.checked){
	         payType=rad.value;
	     }
	    var contractid=$("#contractCode  option:selected").text();
	   // var contractid=$("#contractCode).val();
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
				alert(data.left + " # " + data.right);
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
                           <DIV class="grid_10">
                           <div id="process" class="section4">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付完成</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">物料审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">正在播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce doing"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">订单完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            		</div>
            <DIV class="color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单详情-${(orderview.order.id)!''}</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
  <LI style="width: 240px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(orderview.order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type!''}</SPAN></LI>
  <LI style="width: 200px;"><SPAN>订单状态：</SPAN><SPAN class="con">待支付</SPAN></LI>
</UL>
</DIV>
</DIV>
<br>
                 <div class="module s-clear u-lump-sum p19">
                      <H3 class="text-xl"><A class="black" href="#">支付订单</A></H3>								
					<div class="u-sum-right">
							<input type="radio" name="payType" onchange="showContract()" value="contract" checked="checked">关联合同
				             <input type="radio" name="payType" value="online" onchange="hideContract()" >线上支付
				             <input type="radio" name="payType" value="others"  onchange="hideContract()">其他
							<br>
							 
								 <select class="ui-input" name="contractCode" id="contractCode">
                                                <option value="" selected="selected">请选择合同</option>
                                                 <#if contracts?exists>
                                                <#list contracts as c>
                                                    <option value="${c.id}">${c.contractName!''}</option>
                                                </#list>
                                                </#if>
                              </select><br>
                              <br>
                              <button type="button" onclick="pay()" class="block-btn" style="margin-left: 60px;">确认支付</button>	
                             
	              </div>
	              
             </div>	

						
			</div> 
			 </DIV>
							<!-- 世巴初审 -->
                            <div id="approve1" style="display: none;">	
                                                                                   世巴初审：<br>
				               <textarea name="approve1Comments" id="approve1Comments"></textarea><br>
							   <input type="radio" name="approve1Result" value="true" checked="checked">物料正常
				               <input type="radio" name="approve1Result" value="false" >物料异常
				               <br><br>
				                                      
				              <button onclick="approve1();" class="block-btn">提交</button>
							</div>
							
							<!-- 世巴提交排期表 -->
                            <div id="submitSchedule" style="display: none;">	
                                                                                          初审意见意见：${(orderview.variables.approve1Comments)!''}    <br>
                                 	世巴确认排期表并提交：<br>
                                 	历史审批结果：<br>
                                 	
                                 	
                                 	
							   <input type="radio" name="ScheduleResult" value="true" checked="checked">通过
				               <input type="radio" name="ScheduleResult" value="false" >不通过
				               <br><br>
				              <button onclick="submitSchedule();" class="block-btn">提交</button>
							</div>
							
							<!-- 北广录入排期表 -->
                            <div id="inputSchedule" style="display: none;">	
                                 	北广录入排期表：<br>
				                                    意见：<textarea name="inputScheduleComments" id="inputScheduleComments" value="已录入排期表"></textarea><br>
				               <br><br>
				               
				              <button onclick="inputSchedule();" class="block-btn">提交</button>
							</div>
							
							<!-- 上播报告 -->
                            <div id="shangboReport" style="display: none;">	
                                 	上播报告：<br>
                                                                                       意见：<textarea name="shangboComments" id="shangboComments" value="已上播"></textarea><br>
							   <input type="radio" name="shangboResult" value="true" checked="checked">已上播
				               <input type="radio" name="shangboResult" value="false" >未上播
				               <br><br>
				              <button onclick="shangboReport();" class="block-btn">提交</button>
							</div>
							
							<!-- 监播报告 -->
                            <div id="jianboReport" style="display: none;">	
                                 	监播报告：<br>
				                                       意见：<textarea name="jianboComments" id="jianboComments" value="正常"></textarea><br>
							   <input type="radio" name="jianboResult" value="true" checked="checked">正常
				               <input type="radio" name="jianboResult" value="false" >异常
				               <br><br>
				              <button onclick="jianboReport();" class="block-btn">提交</button>
							</div>
							 
							  <!-- 北广审核并填写物料ID等信息 -->
                             <div id="approve2" style="display: none;">	
                             
                             

<DIV class="color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单详情-${(orderview.longOrderId)!''}</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
  <LI style="width: 240px;"><SPAN>广告主：</SPAN><SPAN class="con">${(orderview.order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(orderview.order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type!''}</SPAN></LI>
  <LI style="width: 200px;"><SPAN>订单状态：</SPAN><SPAN class="con">待支付</SPAN></LI>
</UL>
</DIV>
</DIV>
<br>
<div class="module s-clear u-lump-sum p19">
<H3 class="text-xl"><A class="black" href="#">订单办理[北广审核并填写物料ID等信息]</A></H3>								
								<div class="u-sum-right">
                  <label class="mt10">审核意见</label>         
				               
				               <textarea name="approve2Comments" id="approve2Comments"></textarea>
							   <input type="radio" name="approve2Result" value="true" checked="checked">素材正常
				               <input type="radio" name="approve2Result" value="false" >素材异常
				               <button onclick="approve2();" class="block-btn">提交</button>
				               <br><br>
				     </div>
</div>	          

<DIV class="p20bs mt20 color-white-bg fn-clear">
<H3 class="text-xl"><A class="black" href="#">历史办理信息</A></H3>	
				               
				               <#if activitis?exists>
				               <div class="uplan-table-box">
											<table width="100%" class="uplan-table">
												<tr class="uplan-table-th">
												<td style="width: 130px;">
														<div class="th-head">签收时间 </div>
													</td>
													<td style="width: 130px;">
														<div class="th-md">办理时间 </div>
													</td>
													<td style="width: 112px;">
														<div class="th-md">操作人</div>
													</td>
													<td style="width: 111px;">
														<div class="th-md">操作类型</div>
													</td>
													
													<!--<td style="width: 111px;">
														<div class="th-md">是否通过</div>
													</td>-->
													<td style="width: 333px;">
														<div class="th-tail">备注</div>
													</td>
												</tr>
											</table>
				               	<#list activitis as act>
				               	<li class="ui-list-item dark">
												<div class="ui-list-item-row fn-clear">
												<span style="width: 130px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														 <#setting
															date_format="yyyy-MM-dd HH:MM">
														${(act.startTime?date)!''} </span> 
													<span style="width: 130px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														 <#setting
															date_format="yyyy-MM-dd HH:MM">
														${(act.endTime?date)!''} </span> 
													<span
														style="width: 118px; height: 35px;"
														class="ui-list-field text-center w80 fn-left"><em
														class="value-small">${act.assignee!''} 
													</em> </span>
													 
													<span
														style="width: 129px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small">
															${act.name}</em> </span>
														<!--	<span
														style="width: 129px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small">
															${act.result?string('yes', 'no')}</em> </span>-->
													 <span
														style="width: 240px; height: 35px;"
														class="ui-list-field text-center w80 fn-left last"><em
														class="value-small">
														 ${act.comment!''}
														</em>
														</span>
															 
											</li>
								</#list>
									</div>
								</#if>
				              
							 </div>
							 
							  <!-- 世巴财务确认 -->
							 <div class="ui-form-item">
							     <div id="financialCheck" style="display: none;">
							        <label class="ui-label mt10">世巴财务确认 </label><br>
				                                           审核意见： <textarea name="financialcomment" id="financialcomment"></textarea><br>
							    <input type="radio" name="rad" value="true" checked="checked">支付正常
				                <input type="radio" name="rad" value="false" >支付异常
				                <br><br>
				                <button onclick="financial();" class="block-btn">提交</button>	
							    </div>
							 </div>
</@frame.html>