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
    	a.target = 'main';
    	document.body.appendChild(a);
    	a.click();
    });
    
}

//部门经理审核
function approve1(){
	var approve1Result=$('#approve1 :radio[name=approve1Result]:checked').val();
	var approve1Comments=$('#approve1 #approve1Comments').val();
	complete('${taskid}',[
		{
			key: 'approve1Result',
			value: approve1Result,
			type: 'B'
		},{
			key: 'approve2Result',
			value: "true",
			type: 'B'
		},
		{
			key: 'approve1Comments',
			value: approve1Comments,
			type: 'S'
		}
	]);
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
	function pay() {
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "../order/payment",
			type : "POST",
			data : {
				"orderid" :orderid,"taskid" :taskid
			},
			success : function(data) {
				alert(data.left + " # " + data.right);
			}
		}, "text");
	}
	
	function financial() {
		//var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		//var isok=$("#div1 :radio[name=rad]:checked").val();
	    //var comment=$("#comment").val();
		$.ajax({
			url : "../order/handle",
			type : "POST",
			data : {
				//"orderid" :orderid,
				"taskid" :taskid
				//"isok":isok,
				//"comment":comment
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
                        <font color="red">${message!'' }</font>
                        <!-- 支付-->
                        <div id="payment" style="display: none;">
							 <label class="ui-label mt10">
							 输入合同号：
							 </label>
							 <input id="code" class="ui-input" type="text" value="reg4345" name="contract_code" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">
							<input type="button" onclick="check();" class="block-btn" value="合同号检查" >
						<div class="ui-form-item">
						<input type="button" onclick="pay();" class="block-btn" value="确认支付"/>				 
						</div>	
						</div>	
						</div> 
						
							
							
							<!-- 世巴初审 -->
                            <div id="approve1" style="display: none;">	
                                 	世巴初审：<br>
				            <textarea name="approve1Comments" id="approve1Comments"></textarea><br>
							<input type="radio" name="approve1Result" value="true" checked="checked">素材正常
				            <input type="radio" name="approve1Result" value="false" >素材异常
				            <br><br>
				            
				            <button onclick="approve1();" class="block-btn">提交</button>
				            
							</div>
							 
							  <!-- 北广审核并填写物料ID等信息 -->
                             <div id="approve2" style="display: none;">	
                                                                                                 北广审核并填写物料ID等信息
							 </div>
							 
							  <!-- 世巴财务确认 -->
							 <div class="ui-form-item">
                             	
                           
							<div id="financialCheck" style="display: none;">
							
							 <label class="ui-label mt10">
                                                                                世巴财务确认 <br>
							</label>
				           审核意见： <textarea name="comment" id="comment"></textarea><br>
							<input type="radio" name="rad" value="true" checked="checked">支付正常
				            <input type="radio" name="rad" value="false" >支付异常
				            <br><br>
				            <button onclick="financial();" class="block-btn">提交</button>	
							 </div>
							 </div>
							
						</div>
						
						</div>
</@frame.html>