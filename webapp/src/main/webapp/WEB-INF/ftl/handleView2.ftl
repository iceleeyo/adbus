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
	var approve2Result=$('#approve1 :radio[name=approve2Result]:checked').val();
	var approve1Comments=$('#approve1 #approve1Comments').val();
	complete('${taskid!''}',[
		{
			key: 'approve1Result',
			value: approve1Result,
			type: 'B'
		},
		{
			key: 'approve2Result',
			value: "approve2Result",
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
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "${rc.contextPath}/order/payment",
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
						<input type="hidden" id="orderid" value="${orderview.order.id!''}"/>
						<input type="hidden" id="taskid" value="${taskid!''}"/>
						 <div class="withdraw-title fn-clear">
						  流程办理
						 <button type="button" onclick="go_back()" class="block-btn" style="margin-left: 60px;">返回</button>
						 </div>	
						<div class="withdrawInputs">
						<div class="inputs">
						<div class="ui-form-item">
                           <!-- 支付-->
                           <div id="payment" style="display: none;">
							 <label class="ui-label mt10">
							 输入合同号：
							 </label>
							 <input id="code" class="ui-input" type="text" value="reg4345" name="contract_code" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">
							<input type="button" onclick="check();" class="block-btn" value="合同号检查" ><br><br>
						    <input type="button" onclick="pay();" class="block-btn" value="确认支付"/>				 
						   </div>	
						</div> 
							<!-- 世巴初审 -->
                            <div id="approve1" style="display: none;">	
                                                                                   世巴初审：<br>
				               <textarea name="approve1Comments" id="approve1Comments"></textarea><br>
							   <input type="radio" name="approve1Result" value="true" checked="checked">物料正常
				               <input type="radio" name="approve1Result" value="false" >物料异常
				               <br><br>
				                                        该物料是否终审过:<br><hr>
				                <input type="radio" name="approve2Result" value="true" checked="checked" >是
				                <input type="radio" name="approve2Result" value="false" >否
				                <br><br>       
				              <button onclick="approve1();" class="block-btn">提交</button>
							</div>
							
							<!-- 世巴提交排期表 -->
                            <div id="submitSchedule" style="display: none;">	
                                 	世巴确认排期表并提交：<br>
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
                                                                                                 北广审核并填写物料ID等信息<br>
				               <textarea name="approve2Comments" id="approve2Comments"></textarea><br>
							   <input type="radio" name="approve2Result" value="true" checked="checked">素材正常
				               <input type="radio" name="approve2Result" value="false" >素材异常
				               <br><br>
				              <button onclick="approve2();" class="block-btn">提交</button>
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
						</div>
						</div>
</@frame.html>