<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "template/template.ftl" as frame> <#import
"template/orderDetail.ftl" as orderDetail/> <#import
"template/pickBuses.ftl" as pickBuses> <@frame.html title="订单办理"
js=["js/nano.js","js/highslide/highslide-full.js",
"js/video-js/video.js", "js/video-js/lang/zh-CN.js",
"js/jquery-ui/jquery-ui.min.js","index_js/sift_common.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js","js/jquery-dateFormat.js"
,"js/ajax-pushlet-client.js"
]
css=["js/highslide/highslide.css",
"js/video-js/video-js.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>
<#include "template/preview.ftl" />
<style type="text/css">
.textareaBox {
	border: 1px solid #3BAFDA;
}

.title-box{
    border-bottom: 0px;
}

.withdraw-wrap{
	border-top:0;
}

.ui-table-gray tr{
	background: none;

}

.ui-table-gray tr th{
	//color: white;
}

#payPlanTable th{
    color: white;
}

#payPlanTable {
    width: 100%;
}

#payPlanTable a{
	color: #31B533;
    font-weight: 800;
}

table.dataTable tbody th, table.dataTable tbody td{
	padding: 3px 10px;
}


.banknameformError .parentFormuserForm3 .formError {
	opacity: 0.87;
	position: absolute;
	top: 0px;
	left: 340px !important;
	
}
</style>

<script type="text/javascript">
	$(function() {
	//显示当前节点对应的表单信息
	$('.${activityId!'' }').css("display","inline");
	$("#otherpay").hide();
	$("#contractCode").hide();
    $("#pingzhengTab").hide();
	
	$("#generateSchedule #startdate1").change(function() {
           $("#ischeckInventory").val(0);
           $("#sureButton").css({"background-color":"#f2f2f2"});
		   $("#sureButton").css({"color":"#fff"});
     });
	
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
	 layer.load(1);			
	 setTimeout(function(){
	  layer.closeAll('loading');
	 }, 4000);
var url="${rc.contextPath}/order/"+taskId+"/complete";
	// 发送任务完成请求
    $.post(url,{
        keys: keys,
        values: values,
        types: types
    },function(data){
     layer.closeAll('loading');
    	jDialog.Alert(data.left==true? data.right :"执行失败!");
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
	   jDialog.Alert("请填写审批合格号");
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
	 layer.load(1);			
	 setTimeout(function(){
	  layer.closeAll('loading');
	 }, 4000);
	var url="${rc.contextPath}/order/"+taskId+"/complete";
	// 发送任务完成请求
    $.post(url,{
        keys: keys,
        values: values,
        types: types,
        id:suppliesid,
        seqNumber:seqNumber
        
    },function(data){
     layer.closeAll('loading');
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
		},{
			key: 'filterTime',
			value: ${.now?long},
			type: 'L'
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

//分期设置
function comitPayPlan() {
layer.confirm('确定提交吗？', {icon: 3}, function(index){
  		layer.close(index);
		    if(true){
$.ajax({
		url:"${rc.contextPath}/order/checkOrderPrice/"+$("#orderid").val(),
		type:"POST",
		async:false,
		dataType:"json",
		success:function(data){
			if (data.left) {
			complete('${taskid!''}',[{
			key: 'indexpay',
			value: 'false',
			type: 'B'
		}]);
			} else {
			layer.msg('分期总价为  ￥<font color="red">'+data.right+'</font>,和订单价格不相等',{icon: 5});
			//layer.confirm('分期总价和订单价格不相等,确定提交吗？', {icon: 3}, function(index){
  		     // layer.close(index);
		      //if(true){
		     // complete('${taskid!''}',[]);
			//	}}); 
			}
		}
	});  
	}});  
	
}
//用户首付款
function userFristPay() {
	complete('${taskid!''}',[
		
	]);
}
//上播报告
function shangboReport() {
    var shangboResult=$('#shangboReport :radio[name=shangboResult]:checked').val();
	var shangboComments=$("#shangboComments").val();
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
	
	//---------------------排期部分js---作者panxh
	 var w=0;
	var _interval;
	function _closeLayer(){
		$("#cc").trigger("click");
	}
	function _scheduleProgess() {
    $.ajax({
        url:"${rc.contextPath}/schedule/process",
        type:"GET",
        success:function(data) {
            // layer.closeAll('loading');
            $("#infoText").prepend("<span style='margin-left:130px'>" + data.show + "</span><br>");
            if (data.result != null) {
                layer.closeAll("loading");
                clearInterval(_interval);
                $("#infoText").prepend('<span style="margin-left:130px"><input type="button" id="subWithdraw" class="block-btn"   onclick="_closeLayer();" value="关闭"></span><br>');
                //-------
                if (data.result.scheduled) {
                    layer.msg("库存充足可排期.");
                    _closeLayer();
                    $("#ischeckInventory").val(1);
                    $("#sureButton").css({
                        "background-color":"rgb(245, 135, 8)"
                    });
                    $("#sureButton").css({
                        color:"#fff"
                    });
                } else {
                    var w = $.format.date(data.result.notSchedultDay, "yyyy-MM-dd");
                    //	layer.msg("日期:<font color='red'>"+w+"</font>  库存不足<br>"+data.msg, {icon: 5});
                    _closeLayer();
                    layer.confirm("日期:<font color='red'>" + w + "</font>  库存不足<br>根据当前订单信息库存信息如下:<br><br>" + data.result.msg + "<br><br>是否让系统推荐一个可排期的日期?", {
                        icon:3
                    }, function(index) {
                        layer.close(index);
                        layer.load(1);
                        setTimeout(function() {
                            layer.closeAll("loading");
                        }, 6e4 * 10);
                        var orderid = $("#orderid").val();
                        $.ajax({
                            url:"${rc.contextPath}/schedule/queryFeature/" + orderid + "?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624",
                            type:"POST",
                            success:function(data) {
                                layer.closeAll("loading");
                            }
                        }, "text");
                        //--------begin queryFeature ----- 
                        initCheckFeautreInfo();
                    });
                }
            }
        }
    }, "text");
}
	 			
					
	function initSchedultInfo(){
		var t= "<span id='infoText'></span>";
		layer.open({
		title:'[执行过程窗口]',
	    type: 1,
	    skin: 'layui-layer-demo', //样式类名
	    closeBtn: 0, //不显示关闭按钮
	    shift: 2,
	    area: ['400px', '400px'], 
	    shadeClose: false, //开启遮罩关闭
	    content: '<div><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'+t+"</div>"
		});
		//_interval =setInterval(function () { _scheduleProgess(); }, 1700);

	}
	
	
	function _checkFeautreProgess() {
    $.ajax({
        url:"${rc.contextPath}/schedule/session/_checkFeature",
        type:"GET",
        success:function(data) {
            $("#infoText2").prepend("<span style='margin-left:130px'>" + data.show + "</span><br>");
            if (data.result != null) {
                clearInterval(_interval);
                _closeLayer();
                $("#infoText2").prepend('<span style="margin-left:130px"><input type="button" id="subWithdraw" class="block-btn"   onclick="_closeLayer();" value="关闭"></span><br>');
                //关闭检查窗口
                if (data.scheduled) {
                    var w = $.format.date(data.notSchedultDay, "yyyy-MM-dd");
                    var t = "从日期    <font color='red'>" + w + "</font>   起有档期可安排!";
                    layer.alert(t, {
                        icon:6
                    });
                } else {
                    layer.alert(data.msg, {
                        icon:6
                    });
                }
            }
        }
    }, "text");
	}
	
	
	
	function initCheckFeautreInfo(){
		var t= "<span id='checkResultSpan'></span>";
		layer.open({
		title:'[执行过程窗口]',
	    type: 1,
	    skin: 'layui-layer-demo', //样式类名
	    closeBtn: 0, //不显示关闭按钮
	    shift: 2,
	    area: ['400px', '400px'], 
	    shadeClose: false, //开启遮罩关闭
	    content: '<div><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'+t+"</div>"
		});
		//_interval =setInterval(function () { _checkFeautreProgess(); }, 1700);

	}
	
	
	
function checkInventory() {
      var orderid = $("#orderid").val();
      var startdate1= $("#generateSchedule #startdate1").val();
      
      var bm=$('#generateSchedule :radio[name=bm]:checked').val();
      
      if(orderid==""){
         layer.msg("信息丢失,请刷新页面");
          return;
      }
      if(startdate1==""){
         layer.msg("实际开播日期必填");
          return;
      }
		layer.load(1);
		setTimeout(function(){
		    layer.closeAll('loading');
		}, 60000*10);
		$.ajax({
			url : "${rc.contextPath}/schedule/testsch/"+orderid+"/true?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624",
			type : "POST",
			data : { 
			"startdate1":startdate1,
			"bm":bm
			},
			success : function(data) {
				 layer.closeAll('loading');
			}
		}, "text");
	}
function confirmSchedule() {
	  var ischeckInventory= $("#ischeckInventory").val();
      var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
      var startdate1= $("#generateSchedule #startdate1").val();
        var bm=$('#generateSchedule :radio[name=bm]:checked').val();
      var mediatype='${orderview.order.type}';
      if(mediatype=='video' && ischeckInventory==0){
         layer.msg("确认排期前请先检查库存", {icon: 5});
         return;
      }
      if(orderid=="" || taskid==""){
         layer.msg("信息丢失,请刷新页面", {icon: 5});
          return;
      }
      if(startdate1==""){
         layer.msg("实际开播日期必填", {icon: 5});
          return;
      }
		 	layer.confirm("实际开播日期为" + startdate1 + ",确定排期吗？", {
		    icon:3
		}, function(index) {
		    layer.close(index);
		    if (true) {
		        layer.load(1);
		        setTimeout(function() {
		            layer.closeAll("loading");
		        }, 60000 * 10);
		        $.ajax({
		            url:"${rc.contextPath}/schedule/testsch/" + orderid + "/false?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624",
		            type:"POST",
		            data:{
		                startdate1:startdate1,
		                taskid:taskid,
		                bm:bm
		            },
		            success:function(data) {
		                layer.closeAll("loading");
		                if(data.lock){
		               		layer.alert(data.msg, {
			                    icon:6
			                });
		                } else {
		                
		               //jDialog.Alert(data.scheduled == true ? "执行成功!" :"执行失败!");
		                var uptime = window.setTimeout(function() {
		                    var a = document.createElement("a");
		                    a.href = "${rc.contextPath}/order/myTask/1";
		                    document.body.appendChild(a);
		                    a.click();
		                    clearTimeout(uptime);
		                }, 5000);
		                }
		            }
		        }, "text");
		    }
		});
	}
	
function pay(tp) {
	    var contractid=-1;
	     var payType="";
	      var payWay="";
	     var invoiceid=0;
	     var contents="";
	     var receway="";
	     var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
	     var temp=document.getElementsByName("payType");
	       var payWayTemp=document.getElementsByName("payWay");
	       var isinvoice=0;
	      for(var i=0;i<temp.length;i++)
         {
           if(temp[i].checked)
            payType = temp[i].value;
         }
           for(var i=0;i<payWayTemp.length;i++)
         {
           if(payWayTemp[i].checked)
            payWay = payWayTemp[i].value;
         }
         if(tp=='userFristPay'){
         	if(payWay==""){
         		 jDialog.Alert("请选择分期付款方式");
	         	 return;
         	}
         }
      	   var payWayPost = payWay =='payAll'?$("#allLocation").val():$("#payNextLocation").val();
            if(payType==""){
            	 jDialog.Alert("请选择支付方式");
	         	 return;
            }else if(payType=="contract"){
	            contractid=$("#contractCode  option:selected").val();
	            if(contractid==""){
	              jDialog.Alert("请选择合同");
	              return;
	            }
	         }else if(payType=="online"){
	         layer.msg('<h3 style="line-height: 45px;font-size: 15px;"><span id="payMsg">请您在新打开的页面插上U盾完成支付！<span></h3><br><span class="tip_font">•支付完成前请不要关闭此窗口<br>•支付失败时，可以迅速联系我们客服(010-88510188)</span>'
							  +'<br><br><a class="block-btn" href="javascript:void(0);"  onclick="checkPayStatus(' + orderid
							  +')">确认成功 </a><a class="fail-btn" href="javascript:void(0);"'
							  +'  onclick="canelPay()">确认失败 </a>',{time: 300000,icon:9});
							  if( payWay =='payAll'){
							  		$('#icbcOPer').click();
							  } 
							   if( payWay =='payNext'){
							  		$('#planSubmit').click();
							  } 
							  return;
	         }
	         else if(payType=="others"){
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
				"receway":receway,
				"payWay":payWay,
				"payNextLocation":payWayPost
			},
			success : function(data) {
				jDialog.Alert(data.right);
				var uptime = window.setTimeout(function(){
				if(data.left.suppliesId=='1'){
				  var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myTask/1';
            	document.body.appendChild(a);
             	a.click();
				}else{
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myOrders/1';
            	document.body.appendChild(a);
             	a.click();
             	}
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}
	function canelPay(){
							var i=5;
							 setInterval(function(){               
		           				 if(i == 1) { window.location.href="${rc.contextPath}/order/myTask/1";}
	               				$("#payMsg").html("<br><font color='red'>"+(i--)+"</font>秒后跳转到后台");
	           				 },1000);
		}
		function checkPayStatus(orderId){
				$.ajax({
					url:"${rc.contextPath}/carbox/ajax-checkPayStats",
					type:"POST",
					dataType:"json",
					data:{"orderId":orderId },
					success:function(data){
						if (data) {
							var i=3;
							 setInterval(function(){               
		           				 if(i == 1) { window.location.href="${rc.contextPath}/carbox/paySuccess/media";}
	               				$("#payMsg").html("支付成功！,<font color='red'>"+(i--)+"</font>秒后跳转到后台");
	           				 },1000);
						}else{
							var i=5;
							 setInterval(function(){               
		           				 if(i == 1) { window.location.href="${rc.contextPath}/order/myTask/1";}
	               				$("#payMsg").html("支付还未成功！,<font color='red'>"+(i--)+"</font>秒后跳转到后台");
	           				 },1000);
	           			 }
					}
		          }); 
          
          }
	function modifyOrder() {
	        var supplieid=$("#supplieid  option:selected").val();
	            var stardate1=$("#startdate1").val();
	            if(stardate1==""){
	              layer.msg("请选择开播日期");
	              return;
	            }
	            if(supplieid==""){
	              layer.msg("请选择物料");
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
				"supplieid":supplieid,
				"startdate1":stardate1
			},
			success : function(data) {
				jDialog.Alert(  data.right);
				var uptime = window.setTimeout(function(){
				if(typeof(data.left.payType)=="undefined"){
				  var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myTask/1';
            	document.body.appendChild(a);
             	a.click();
				}else{
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myOrders/1';
            	document.body.appendChild(a);
             	a.click();
             	}
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}
	function showContract(){
	     $("#contractCode").show();
	     $("#otherpay").hide();
	     $("#pingzhengTab").hide();
	}
	function hideContract(){
	     $("#contractCode").hide();
	     $("#otherpay").show();
	     $("#pingzhengTab").hide();
	}
	function hideboth(){
		
	     $("#contractCode").hide();
	     $("#otherpay").hide();
	     $("#pingzhengTab").show();
	}
	function hideall(){
		
	     $("#contractCode").hide();
	     $("#otherpay").hide();
	     $("#pingzhengTab").hide();
	}
	
	//物料详情浮窗
	function supDetail(data){

	layer.open({
    		type: 1,
    		title: "物料及资质",
    		skin: 'layui-layer-rim', 
    		area: ['1000px', '529px'], 
    		content:''
			   +' '
			   +'<iframe  style="width:99%;height:99%" frameborder="no" src="${rc.contextPath}/supplies/suppliesDetail/'+data+'"/>'
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
            url : "${rc.contextPath}/upload/savePayvoucher/${orderview.order.id!''}",
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
</script>
<input type="hidden" id="orderid" value="${orderview.order.id!''}" />
<input type="hidden" id="taskid" value="${taskid!''}" />


<@orderDetail.orderDetail orderview=orderview quafiles=quafiles
suppliesView=suppliesView/> 

<#if activityId == "payment" || activityId == "relateSup">
<!-- 支付-->
<div id="payment" class="payment relateSup" style="display: none;">
	<#if city.mediaType == 'body'> <@pickBuses.pickBuses
	order=orderview.order product=orderview.product city=city
	lineLevel=prod.lineLevel categories=categories/> </#if>
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 style="border-bottom:0;" class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="#">支付订单</A>
			</p>
		</H3>
		<BR>
		
		<form id="form1"  target="_blank" style="display: none;" action="https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet" method="post">
			    <input type="hidden" name="APIName" value="B2B"/>
			    <input type="hidden" name="APIVersion" value="001.001.001.001"/>
			    <input type="hidden" name="Shop_code" value="0200EC14729207"/>
			    <!--若不正确，将无银行反馈信息，注意不能省略"http://"-->
			    <input type="hidden" name="MerchantURL" value="${callback}"/>
			    <input type="hidden" name="ContractNo" value="${contractNo}"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="ContractAmt" value="${totalPrice}"/>
			    <input type="hidden" name="Account_cur" value="001"/>
			    <input type="hidden" name="JoinFlag" value="2"/>
			    <input type="hidden" name="Mer_Icbc20_signstr" value="${a1}"/>
			    <input type="hidden" name="Cert" value="${a2}"/>
			    <input type="hidden" name="SendType" value="0"/>
			    <input type="hidden" name="TranTime" value="${TranTime}" />
			    <input type="hidden" name="Shop_acc_num" value="0200004519000100173"/>
			    <input type="hidden" name="PayeeAcct" value="0200004519000100173"/>
			    <input type="hidden" name="GoodsCode" value="CODE_MEDIA"/>
			    <input type="hidden" name="GoodsName" value="SPTC"/>
			    <input type="hidden" name="Amount" value="1"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="TransFee" value="1"/>
			    <input type="hidden" name="ShopRemark" value=""/>
			    <input type="hidden" name="ShopRem" value=""/>
			    <input type="submit" id="icbcOPer" value="确定"/>
			</form>
			
			
		<TABLE class="ui-table ui-table-gray">
			<!-- <TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
  								</TR>  -->
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付方式</TD>
				<TD>
					<input type="radio" name="payType" value="online">在线支付 
					<input type="radio" name="payType" value="remit">汇款支付 
					<input type="radio"	name="payType" value="others">其他支付
				</TD>
				<TD>
					<#--<div id="contractCode">
						<select class="ui-input" name="contractCode" id="contractCode">
							<option value="" selected="selected">请选择合同</option> <#if
							contracts?exists> <#list contracts as c>
							<option value="${c.id}">${c.contractName!''}</option> </#list>
							</#if>
						</select>
					</div>-->
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
			<button type="button" onclick="pay('')" class="block-btn">确认支付</button>
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
</#if>

<!-- 用户首付款-->
<#if activityId == "userFristPay">
<div id="userFristPay" class="userFristPay" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="#">订单首付款</A>
			</p>
		</H3>
		<BR>
		
		<!-- plan -->
		<form id="form2"  target="_blank" style="display: none;" action="https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet" method="post">
			    <input type="hidden" name="APIName" value="B2B"/>
			    <input type="hidden" name="APIVersion" value="001.001.001.001"/>
			    <input type="hidden" name="Shop_code" value="0200EC14729207"/>
			    <input type="hidden" name="MerchantURL" value="${callback_plan}"/>
			    <input type="hidden" name="ContractNo" value="${contractNo_plan}"/>
			    <input type="hidden" name="ContractAmt" value="${totalPrice_plan}"/>
			    <input type="hidden" name="Account_cur" value="001"/>
			    <input type="hidden" name="JoinFlag" value="2"/>
			    <input type="hidden" name="Mer_Icbc20_signstr" value="${a1_plan}"/>
			    <input type="hidden" name="Cert" value="${a2_plan}"/>
			    <input type="hidden" name="SendType" value="0"/>
			    <input type="hidden" name="TranTime" value="${TranTime_plan}" />
			    <input type="hidden" name="Shop_acc_num" value="0200004519000100173"/>
			    <input type="hidden" name="PayeeAcct" value="0200004519000100173"/>
			    <input type="hidden" name="GoodsCode" value="CODE_MEDIA"/>
			    <input type="hidden" name="GoodsName" value="SPTC"/>
			    <input type="hidden" name="Amount" value="1"/>
			    <input type="hidden" name="TransFee" value="1"/>
			    <input type="hidden" name="ShopRemark" value=""/>
			    <input type="hidden" name="ShopRem" value=""/>
			    <input type="submit" id="planSubmit" value="确定"/>
			</form>
		
		<form id="form1"  target="_blank" style="display: none;" action="https://corporbank3.dccnet.com.cn/servlet/ICBCINBSEBusinessServlet" method="post">
			    <input type="hidden" name="APIName" value="B2B"/>
			    <input type="hidden" name="APIVersion" value="001.001.001.001"/>
			    <input type="hidden" name="Shop_code" value="0200EC14729207"/>
			    <!--若不正确，将无银行反馈信息，注意不能省略"http://"-->
			    <input type="hidden" name="MerchantURL" value="${callback}"/>
			    <input type="hidden" name="ContractNo" value="${contractNo}"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="ContractAmt" value="${totalPrice}"/>
			    <input type="hidden" name="Account_cur" value="001"/>
			    <input type="hidden" name="JoinFlag" value="2"/>
			    <input type="hidden" name="Mer_Icbc20_signstr" value="${a1}"/>
			    <input type="hidden" name="Cert" value="${a2}"/>
			    <input type="hidden" name="SendType" value="0"/>
			    <input type="hidden" name="TranTime" value="${TranTime}" />
			    <input type="hidden" name="Shop_acc_num" value="0200004519000100173"/>
			    <input type="hidden" name="PayeeAcct" value="0200004519000100173"/>
			    <input type="hidden" name="GoodsCode" value="CODE_MEDIA"/>
			    <input type="hidden" name="GoodsName" value="SPTC"/>
			    <input type="hidden" name="Amount" value="1"/>
			    <!--金额为不带小数点的到分的一个字符串，即“112390”代表的是“1123.90元”-->
			    <input type="hidden" name="TransFee" value="1"/>
			    <input type="hidden" name="ShopRemark" value=""/>
			    <input type="hidden" name="ShopRem" value=""/>
			    <input type="submit" id="icbcOPer" value="确定"/>
			</form>
			
			<TABLE class="ui-table ui-table-gray">
			
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">分期详情</TD>
				<TD>
					<SPAN></SPAN><SPAN class="con"><a href="javascript:void(0)" onclick="queryPayPlanDetail('${rc.contextPath}',${orderview.order.id!''},'order');" >查看</a></SPAN>
				</TD>
			</TR>
			
			
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">分期付款</TD>
				<TD>
					<input type="radio" name="payWay" value="payAll">余额付款 <font color="orange">#{(payAll)!'';m2M2} </font>
					<#if payNext gt 0 >
					<input type="hidden" id="payNextLocation" value="${payNextLocation!''}"/> 
					<input type="radio" name="payWay" value="payNext">本期付款 <font color="orange">#{(payNext)!'';m2M2}</font>
					</#if>
					
					<input type="hidden" id="allLocation" value="${allLocation!''}"/> 
				</TD>
			</TR>
		</TABLE>
		<TABLE class="ui-table ui-table-gray">
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付方式</TD>
				<TD>
					<input type="radio" name="payType" value="online">在线支付 
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
			<font color="orange"><B>说明</B></font><br>
			余额付款：指订单不采用分期模式支付一次性需要支付的总额<br>
			本期付款：根据北京世巴传媒公司财务部门指出的分期计划,<br>本期款项等于历史未付款项 加 付款日最近一期的款项<br>
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
</#if>

 <#if activityId == "relateSup">
<!-- 支付和绑定素材-->
<div id="relateSup" class="relateSup" style="display: none;">
	<#if city.mediaType == 'body'> <@pickBuses.pickBuses
	order=orderview.order product=orderview.product city=city
	lineLevel=prod.lineLevel categories=categories/> </#if>
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<input type="radio" name="sel" onchange="showtb1()" checked="checked">支付订单
			<input type="radio" name="sel" onchange="showtb2()">绑定素材
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray" id="tb1">
			<TBODY>
				<TR class="dark"
					style="height: 40px; text-align: center; border-radius: 5px 5px 0 0;">
					<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
				</TR>
				<TR style="height: 45px;">
					<TH width="0%">支付</TH>
					<TD><input type="radio" name="payType"
						onchange="showContract()" value="contract" checked="checked">关联合同
						<input type="radio" name="payType" value="online"
						onchange="hideboth()">线上支付 <input type="radio"
						name="payType" value="others" onchange="hideContract()">其他支付</TD>

					<TD>
						<div id="contractCode">
							<select class="ui-input" name="contractCode" id="contractCode">
								<option value="" selected="selected">请选择合同</option> <#if
								contracts?exists> <#list contracts as c>
								<option value="${c.id}">${c.contractName!''}</option> </#list>
								</#if>
							</select>
						</div>
						<div id="otherpay">
							<select class="ui-input" name="otherpay" id="otherpay">
								<option value="" selected="selected">请选择支付方式</option>
								<option value="check">支票</option>
								<option value="remit">汇款</option>
								<option value="cash">现金</option>
							</select>
						</div>
					</TD>
					<TD width="25%" style="text-align: center;">
						<button type="button" onclick="pay('')" class="block-btn">确认支付</button>
					</TD>
				</TR>
		</TABLE>
		<br>
		<TABLE class="ui-table ui-table-gray" id="tb2">
			<TBODY>
				<TR class="dark"
					style="height: 40px; text-align: center; border-radius: 5px 5px 0 0;">
					<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
				</TR>
				<TR style="height: 45px;">
					<TH width="0%"><div>绑定素材</div>></TH>
					<div id="contractCode">
						<select class="ui-input" name="supplies.id" id="suppliesId">
							<option value="1" selected="selected"></option> <#list
							supplieslist as s>
							<option value="${s.id}">${s.name}</option> </#list>
						</select>
					</div>
					</TD>
					<TD width="25%" style="text-align: center;"></TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button type="button" onclick="relate()" class="block-btn">确认</button>
		</div>
	</div>
</div>
</#if> 

<#if activityId == "modifyOrder">
<!-- 广告主修改订单 -->
<div id="modifyOrder" class="modifyOrder" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="">订单处理-广告主修改订单物料</A>
			</p>
		</H3>
		<br>

		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<input type="hidden" id="seqNumber"
					value="${suppliesView.mainView.seqNumber!''}" />
				<TR>
					<TH>开播日期</TH>
					<TD colspan=3>
                    <input class="ui-input datepicker validate[required,custom[date],past[#upDate1]]" type="text"  value="<#if orderview.order.startTime?has_content>${(orderview.order.startTime)?string("yyyy-MM-dd")}</#if>" id="startdate1" onchange="dateInput('startdate1',${orderview.order.product.id})"; data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> 						
						</TD>
				</TR>
				<#if (orderview.order.supplies.id>1)>
				<TR>
					<TH>物料详情</TH>
					<TD colspan=3><a href="javascript:void(0)"
						onclick="supDetail(${(suppliesView.mainView.id)!''})">查看</a></TD>
				</TR>
				</#if>
				<TR>
					<TH>更改物料</TH>
					<TD colspan=3><select class="ui-input" name="supplieid"
						id="supplieid">
							<option value="" selected="selected">请选择物料</option> <#if
							supplieslist?exists> <#list supplieslist as c>
							<option value="${c.id}">${c.name!''}</option> </#list> </#if>
					</select> &nbsp;&nbsp;&nbsp; 
					<input class="btn-sm btn-success" type="button"  onclick="supEnter('${rc.contextPath}',${city.mediaType},'${orderview.product.type!''}')" id="btn_add2" value="上传物料" style="margin-top: 10px;">
					</TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="modifyOrder();" class="block-btn">提交</button>
		</div>
	</div>
</div>
</#if>
<#if activityId == "generateSchedule">
<!-- 生成排期表 -->
<div id="generateSchedule" class="generateSchedule" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="">生成排期表</A><!-- <input type="text" onclick="tttt()">-->
			</p>
		</H3>
		<br>

		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<input type="hidden" id="ischeckInventory" value="0" />
				<TR>
					<TH>预计开播日期</TH>
					<TD colspan=3>
					<#if orderview.order.startTime?has_content>
					<#setting date_format="yyyy-MM-dd"> ${(orderview.order.startTime)!''}
					 </#if>	
					</TD>
				</TR>
				<TR>
					<TH>实际开播日期</TH>
					<TD colspan=3>
                    <input class="ui-input datepicker validate[required,custom[date],past[#upDate1]]" type="text"  value="${orderview.order.startTime?string("yyyy-MM-dd")}" id="startdate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> 						
					</TD>
				</TR>
				<#if paymentResult>
						  <#if orderview.order.type=='video'>
				<TR>
					<TH>只排高峰时段</TH>
					<TD colspan=3>
                    <input type="radio" name="bm" value="1"/>是
					<input name="bm" type="radio" value="0" checked=checked/>否					
					</TD>
				</TR>
				 </#if>
				 </#if>	 
				<TR>
					<TH>检查库存</TH>
					<TD colspan=3>
						<#if paymentResult>
						  <#if orderview.order.type=='video'>
						<button onclick="checkInventory();" class="block-btn">检查库存</button> 
						<button id="sureButton" onclick="confirmSchedule();" class="block-btn" style="background:#f2f2f2"><font style="font-weight:bold;font-style:italic;">确定排期</font></button>
						  <#else>
						  <button id="sureButton" onclick="confirmSchedule();" class="block-btn" >确定排期</button>
						 </#if>
						<#else>
						订单未经财务确认,暂不能排期!
						 </#if>	
					</TD>
				</TR>
				
				
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<span id="cc1"> </span>
		</div>
	</div>
</div>
</#if>

 <#if activityId == "approve2">
<!-- 北广审核并填写物料ID等信息 -->
<div id="approve2" class="approve2" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p align="left">
				<A class="black" href="#">订单处理-北广对物料进行终审</A>
			</p>
		</H3>
		<br>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TD width="20%">签收时间</TD>
					<TD colspan=3 style="border-radius: 0 0 0;">
						<#setting date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}
					</TD>
				</TR>
				<TR>
					<TD>物料信息</TD>
					<TD colspan=3><a href="javascript:void(0)"
						onclick="supDetail(${(suppliesView.mainView.id)!''})">查看</a></TD>
				</TR>
				<TR>
					<TD>审批合格号</TD>
					<input type="hidden" id="suppliesid"
						value="${suppliesView.mainView.id!''}" />
					<TD colspan=3><input id="seqNumber" class="textareaBox"
						type="text"></TD>
				</TR>
				<TR>
					<TD>审核意见</TD>
					<TD colspan=3><textarea name="approve2Comments"
							id="approve2Comments" class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>

				<TR style="height: 45px;">
					<TD>是否通过</TD>
					<TD><input name="approve2Result" type="radio" value="true"
						checked="checked" style="padding: 5px 15px;" />符合要求 <input
						name="approve2Result" type="radio" value="false"
						style="padding: 5px 15px;" />不符合要求</TD>

				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="approve2();" class="block-btn">提交审核结果</button>
		</div>
	</div>
</div>
</#if> <#if activityId == "submitSchedule">
<!-- 世巴提交排期表 -->
<div id="submitSchedule" class="submitSchedule" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">订单处理-确认排期表并提交</A>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR>
					<TH width="20%">排期表</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><a target="_blank"
						href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
				</TR>


				<TR style="height: 45px;display:none;">
					<TH width="20%">是否通过</TH>
					<TD><input name="ScheduleResult" type="radio" value="true"
						checked="checked" style="padding: 5px 15px;" />符合要求
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="submitSchedule();" class="block-btn">提交确认结果</button>
		</div>
	</div>
</div>
</#if> <#if activityId == "approve1">
<!-- 世巴初审 -->
<div id="approve1" class="approve1" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="#">订单处理-对物料进行初审</A>
			</p>
		</H3>
		<br>

		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<input type="hidden" id="seqNumber"
					value="${suppliesView.mainView.seqNumber!''}" />
				<TR>
					<TH width="20%">签收时间</TH>
					<TD colspan=3 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR>
					<TH>物料详情</TH>
					<TD colspan=3><a href="javascript:void(0)"
						onclick="supDetail(${(suppliesView.mainView.id)!''})">查看</a></TD>
				</TR>


				<TR style="height: 45px;">
					<TH>物料审核</TH>
					<TD colspan=3><input name="approve1Result" type="radio"
						value="true" checked="checked" style="padding: 5px 15px;" />符合要求
						<input name="approve1Result" type="radio" value="false"
						style="padding: 5px 15px;" />不符合要求</TD>
				</TR>
				<TR style="height: 45px;">
					<TH>资质审核</TH>
					<TD colspan=3><input name="quaResult" type="radio"
						value="true" checked="checked" style="padding: 5px 15px;" />符合要求
						<input name="quaResult" type="radio" value="false"
						style="padding: 5px 15px;" />不符合要求</TD>
				</TR>
				<TR>
					<TH>审核意见</TH>
					<TD colspan=3><textarea name="approve1Comments"
							id="approve1Comments" class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="approve1();" class="block-btn">提交审核结果</button>
		</div>
	</div>
</div>
</#if> <#if activityId == "financialCheck">
<!-- 世巴财务确认 -->
<div id="financialCheck" class="financialCheck" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">订单处理-财务确认</A>
		</H3>
		<br>

		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<#if orderview.order.payType != "dividpay">
					<TR>
						<TD width="20%">已支付金额</TD>
						<TD colspan=3 style="border-radius: 0 0 0">
						  <font color="orange"><B>#{ (orderview.order.price)!'';m2M2} </B></font><br> 
						  </TD>
					</TR>
				 </#if> 
				 
				 <#if orderview.order.payType == "dividpay">
					<TR>
						<TD width="20%">已支付金额</TD>
						<TD colspan=3 style="border-radius: 0 0 0">
						  <font color="orange"><B>#{ (payed)!'';m2M2}</B></font><br> 
						  </TD>
					</TR>
					
					<TR>
						<TD width="20%">待支付金额</TD>
						<TD colspan=3 style="border-radius: 0 0 0">
						  <font color="orange"><B>#{ (needPay)!'';m2M2}</B></font><br> 
						  </TD>
					</TR>
				 </#if> 
				 <TR>
						<TD width="20%">分期详情</TD>
						<TD colspan=3 style="border-radius: 0 0 0">
						  <SPAN> </SPAN><SPAN class="con"><a href="javascript:void(0)" onclick="queryPayPlanDetail('${rc.contextPath}',${orderview.order.id!''},'order');" >查看</a></SPAN><br> 
						  </TD>
					</TR>
				
				<TR>
					<TD width="20%">签收时间</TD>
					<TD colspan=3 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>				
				<TR style="height: 45px;">
					<TD>支付状态</TD>
					<TD><input name="rad" type="radio" value="true"
						checked="checked" style="padding: 5px 15px;" />支付正常 <input
						name="rad" type="radio" value="false" style="padding: 5px 15px;" />支付异常</TD>
				</TR>
				<TR>
					<TD>审核意见</TD>
					<TD colspan=3><textarea name="financialcomment"
							id="financialcomment" class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>
		</TABLE>
		<div style="text-align: center; margin-top: 10px;">
			<button onclick="financial();" class="block-btn">提交确认结果</button>
		</div>
	</div>
</div>
</#if> <#if activityId == "inputSchedule">
<!-- 北广录入排期表 -->
<div id="inputSchedule" class="inputSchedule" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">订单办理-北广录入排期表</A>
		</H3>
		<BR>

		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR>
					<TH width="20%">排期表</TH>
					<TD style="border-radius: 0 0 0"><a target="_blank"
						href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
				</TR>
				<TR>
					<TH>排期意见</TH>
					<TD><textarea name="inputScheduleComments"
							id="inputScheduleComments" class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>
		</TABLE>
		<div style="text-align: center; margin-top: 10px;">
			<button onclick="financial();" class="block-btn">提交确认结果</button>
		</div>

	</div>
</div>
</#if>

 <#if activityId == "setPayPlan">
<!-- 分期设置 -->
<div id="setPayPlan" class="setPayPlan" style="display: none;">
<script type="text/javascript">
$(document).ready(function(){
initPayPlanTable('${rc.contextPath}',$("#orderid").val(),'<@security.authorize
			ifAnyGranted="sales">edit_del</@security.authorize>','order');
});
</script>
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">分期设置</A>
			<div class="withdraw-title">
			 <a class="block-btn"
						style="margin-top: -30px;" href="javascript:void(0);"
						onclick="addPayPlan('${rc.contextPath}',${orderview.order.id!''},'',0)">添加分期</a>	</div>
		</H3>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
				<TD colspan=2 style="border-radius: 0 0 0;padding:0;">
				
	<table id="payPlanTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>期数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>付款方式</th>
								<th>状态</th>
								<th>付款人</th>
								<th>分期设置人</th>
									<th>处理人</th>
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
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="comitPayPlan();" class="block-btn">提交</button>
		</div>

	</div>
</div>
</#if>


 <#if activityId == "shangboReport">
<!-- 上播报告 -->
<div id="shangboReport" class="shangboReport" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">订单办理-提交上播报告</A>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR>
					<TH width="20%">排期表</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><a target="_blank"
						href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
				</TR>
				<TR>
					<TH>上播意见</TH>
					<TD colspan=2><textarea name="shangboComments"
							id="shangboComments" class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>
				<TR style="height: 45px;">
					<TH>是否上播</TH>
					<TD style="border-radius: 0 0 0"><input name="shangboResult"
						type="radio" value="true" checked="checked"
						style="padding: 5px 15px;" />是 <input name="shangboResult"
						type="radio" value="false" style="padding: 5px 15px;" />否</TD>

					<TD width="30%" style="text-align: center;"></TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="shangboReport();" class="block-btn">提交确认结果</button>
		</div>

	</div>
</div>
</#if>

 <#if activityId == "jianboReport">
<!-- 监播报告 -->
<div id="jianboReport" class="jianboReport" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">订单处理-用户确认</A>
		</H3>
		<BR>

		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR>
					<TH width="20%">排期表</TH>
					<TD style="border-radius: 0 0 0"><a target="_blank"
						href="${rc.contextPath}/schedule/${orderview.order.id!''}">查看排期表</a></TD>
				</TR>
				<TR>
					<TH>意见</TH>
					<TD><textarea name="jianboComments" id="jianboComments"
							class="textareaBox"
							style="margin: 5px 0; width: 400px; margin-top: 5px;"></textarea></TD>
				</TR>
				<TR style="height: 45px;">
					<TH>确认结果</TH>
					<TD style="border-radius: 0 0 0"><input name="jianboResult"
						type="radio" value="true" checked="checked"
						style="padding: 5px 15px;" />播放正常 <input name="jianboResult"
						type="radio" value="false" style="padding: 5px 15px;" />播放异常</TD>


				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="jianboReport();" class="block-btn">提交确认结果</button>
		</div>


	</div>
</div>
</#if> <#include "template/hisDetail.ftl" />



<script type="text/javascript">
$(document).ready(function(){

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
  
       $('.cart_address_wrap ul li').live("click",function(){
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
					hideall();
				}else if(p == 'remit'){
					hideboth();
				}else{
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
	//提交发票

</script>




<#if activityId == "generateSchedule">


<script type="text/javascript">  
       	// PL._init();  
        PL.joinListen("/schedulePush/<@security.authentication property="principal.user.id"/>");  
        var isFrist=0;
        function onData(event) {
			    var msg = decodeURIComponent(event.get("message"));
			    var f = decodeURIComponent(event.get("from"));
			    var json = event.get("json");
			    if (isFrist == 0) {
			        isFrist = 1;
			        initSchedultInfo();
			    }
			    if (msg != null) {
			        if (f == "schInfo") {
			            $("#infoText").prepend("<span style='margin-left:130px'>" + msg + "</span><br>");
			        } else {
			            $("#checkResultSpan").prepend("<span style='margin-left:130px'>" + msg + "</span><br>");
			        }
			    }
			    if (typeof json != "undefined") {
			        isFrist = 0;
			        layer.closeAll("loading");
			        var t2 = decodeURIComponent(json);
			        var obj = jQuery.parseJSON(t2);
			        
			        //-----------
			        if (obj.reqType == "schInfo") {
			        	if(obj.lock==false){
				            if (obj.scheduled == true) {
				                layer.msg(obj.msg);//提示
				                _closeLayer();
				                $("#ischeckInventory").val(1);
				                $("#sureButton").css({
				                    "background-color":"rgb(245, 135, 8)"
				                });
				                $("#sureButton").css({
				                    color:"#fff"
				                });
				                if(obj.scheduleOver){
						            var uptime = window.setTimeout(function() {
					                    var a = document.createElement("a");
					                    a.href = "${rc.contextPath}/order/myTask/1";
					                    document.body.appendChild(a);
					                    a.click();
					                  //  clearTimeout(uptime);
					                }, 5000);
				                }
				                
				            } else {
				                var w = $.format.date(obj.notSchedultDay, "yyyy-MM-dd");
				                _closeLayer();
				                layer.confirm("日期:<font color='red'>" + w + "</font>  库存不足<br>根据当前订单信息库存信息如下:<br><br>" + obj.msg + "<br><br>是否让系统推荐一个可排期的日期?", {
				                    icon:3
				                }, function(index) {
				                    layer.close(index);
				                    layer.load(1);
				                    setTimeout(function() {
				                        layer.closeAll("loading");
				                    }, 60000 * 10);
				                    var orderid = $("#orderid").val();
				                    $.ajax({
				                        url:"${rc.contextPath}/schedule/queryFeature/" + orderid + "?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624",
				                        type:"POST",
				                        success:function(data) {
				                            layer.closeAll("loading");
				                        }
				                    }, "text");
				                    //--------begin queryFeature ----- 
				                    initCheckFeautreInfo();
				                    isFrist = 2;
				                });
				            }
			            } else {
				            layer.alert(obj.msg, {
				                    icon:6
				                });
			            }
			        } else if (obj.reqType == "_checkFeature") {
			            layer.closeAll("loading");
			            // $("#checkResultSpan").prepend("<span style='margin-left:130px'>"+(obj.show)+"</span><br>");
			            _closeLayer();
			            $("#checkResultSpan").prepend('<span style="margin-left:130px"><input type="button" id="subWithdraw" class="block-btn"   onclick="_closeLayer();" value="关闭"></span><br>');
			            if (obj.scheduled) {
			                var w = $.format.date(obj.notSchedultDay, "yyyy-MM-dd");
			                var t = "从日期    <font color='red'>" + w + "</font>   起有档期可安排!";
			                layer.alert(t, {
			                    icon:6
			                });
			            } else {
			                layer.alert(obj.msg, {
			                    icon:6
			                });
			            }
			            isFrist = 0;
			        }
			    }
		}
        
        function tttt(){
			layer.confirm("日期:  库存不足<br>根据当前订单信息库存信息如下:<br><br><br><br>是否让系统推荐一个可排期的日期?", {
			    icon:3
			}, function(index) {
			    layer.close(index);
			    layer.load(1);
			    setTimeout(function() {
			        layer.closeAll("loading");
			    }, 60000 * 10);
			    var orderid = $("#orderid").val();
			    $.ajax({
			        url:"${rc.contextPath}/schedule/queryFeature/" + orderid + "?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624",
			        type:"POST",
			        success:function(data) {
			            layer.closeAll("loading");
			        }
			    }, "text");
			    //--------begin queryFeature ----- 
			    initCheckFeautreInfo();
			});      
        }
</script>  
    
    
  </#if>  
</@frame.html>
