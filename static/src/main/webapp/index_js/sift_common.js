	/**
	 * 查看购物车判断登陆状态
	 */
	function tocard(pathurl) {
		var lc = $("#lc").val();
		if (lc == "0") {
			window.location.href = "/login";
		}
		if (lc == "1") {
			window.location.href = pathurl + "/toCard";
		}
	}

function setCarCount(catCount){
	var exp = catCount;
	if (typeof(exp) != "undefined"){
		if(exp>0){
			$("#cardCount").html("[<b>"+exp+"</b>]");
			$("#cardCount_top").html("["+exp+"]");
			
		}
	}
}
/**
 * 查当前购物车条数
 * @param pathUrl
 */
function initCardView(pathUrl){
		$.ajax({
			url : pathUrl + "/carbox/totalView",
			data:{"ts":  new Date().getTime()},
			type : "GET",
			success : function(data) {
				if (typeof(data) != "undefined"){
					if(data.cardCount>0){
						$("#cardCount").html("[<b>"+data.cardCount+"</b>]");
						$("#cardCount_top").html("["+data.cardCount+"]");
					}
				}
		 }}, "text");
}



function checkNow(startDate){ 
	
    var start=new Date(startDate.replace("-", "/").replace("-", "/"));  
    var end=new Date();  
    if(start>end){  
        return true;  
    }  
    return false;  
} 



/**
 * 查当前购物车条数
 * @param pathUrl
 */
function checkTime(start,prouctId){
	if(!checkNow(start)){
		layer.alert("开播时间 请选择今天以后!", {icon: 5});
		return ;
	}
		$.ajax({
			url : "/checkFree",
			data:{"start":  start, "productId":prouctId},
			type : "GET",
			success : function(data) { 
				
				if (typeof(data.scheduled) == "undefined"){
					//console.log(data.status);
					layer.alert("请先登录！", {icon: 5});
				}else {
					if(data.scheduled){
						layer.msg(data.msg,{icon: 1});//6
					} else {
						var t = $.format.date(data.notSchedultDay, "yyyy-MM-dd");
						var msg="库存不足<br> 日期:" +t+" <br>"+data.msg;
						/*layer.open({
						    content: msg,
						    scrollbar: false
						});*/
					layer.alert(msg, {icon: 5});
					}
				}
		 }}, "text");
}
function updateCardMeida(start,carmediaId){
	var forceInput = $("#"+start).val();
	$.ajax({
		url : "/updateCardMeida",
		data:{"start":  forceInput, "mediaId":carmediaId},
		type : "GET",
		success : function(data) { 
			  if(!data){
				  layer.alert("操作数据库异常", {icon: 5}); 
				  
			  }
		}
		}, "text");
}


function dateInput(inputId,prouctId){
	var forceInput = $("#"+inputId).val();
	checkTime(forceInput,prouctId);
}
var planTable;
function initPayPlanTable(purl,orderId,handle,type,seriaNum) {
	planTable = $('#payPlanTable')
			.dataTable(
					{
						 "dom": '<"#toolbar">rt',
						"searching" : false,
						"iDisplayLength" : 1500,
						"ordering" : false,
						"serverSide" : false,
						"ajax" : {
							type : "GET",
							url : purl+"/order/ajax-getPayPlan",
							data : function(d) {
								return $.extend({}, d, {
									"orderId" : orderId,
									"filter[orderId]" : orderId,
									"filter[seriaNum]" : seriaNum,
									"filter[planType]" : type
								});
							},
							 "dataSrc": function(json) {return json;},
						},
						"columns" : [
								{ "data": "periodNum", "defaultContent": ""}, 
								{ "data": "price", "defaultContent": ""}, 
								{ "data": "day", "defaultContent": "", "render": function(data) {
                                          return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                                     } },
                                 	{ "data": "payType", "defaultContent": "", "render": function(data) {
                                 		if(data==null){
                                 			return '';
                                 		}
                                        return data==0?"线上支付":(data==3?"<a href='javascript:void(0)' onclick='queryPayvoucher(\""+purl+"\","+orderId+");' >汇款</a>":(data==2?"支票支付":(data==4?"现金":"其他方式")));
                                      } }, 
                                      { "data": "payState", "defaultContent": "", "render": function(data) {
                                    	  return data==0?"已支付":(data==1?"未支付":(data==2?"支付失败":"支付待确认"));
                                      } }, 
      								{ "data": "payUser", "defaultContent": ""},
      								{ "data": "setPlanUser", "defaultContent": ""},
      								{ "data": "reduceUser", "defaultContent": ""},
      								{ "data": "updated", "defaultContent": "", "render": function(data) {
                                        return data == null ? "" : $.format.date(data, "yyyy-MM-dd hh:mm");
                                    }},
                                     { "data": "remarks", "defaultContent": "", "render": function(data) {
                                   	  return '<span   class="layer-tips" style="color: #ff9966" tip="'+data+'">'+data.substr(0,10)+'</span>';
                                     } }, 
                                     
                                     
                                     { "data": function( row, type, set, meta) {
                                              return row.id;
                                          },
									"render" : function(data, type, row,
											meta) {
										var operations = '';
										if((row.payUser==null||typeof(row.payUser)=="undefined") && handle.indexOf('edit')!=-1){
											if(handle.indexOf('del')!=-1){
												operations += '<a class="operation" href="javascript:void(0);" onclick="deletePayPlan('+data+');" >删除</a>';
											}
											operations +='&nbsp;&nbsp;<a href="javascript:void(0)"; onclick="toeditPayPlan(\''+purl+'\','+data+');" >修改</a>';
										}
										
										return operations;
									}
								},
									
								 ],
						"language" : {
							"url" : "/js/jquery.dataTables.lang.cn.json"
						},
						"initComplete" : initComplete2,
						"drawCallback" : drawCallback2,
					});
}

//获取32寸屏订单批次
var orderDetailTable;
function initOrderDetailTable(purl,orderId) {
	orderDetailTable = $('#orderDetailTable')
	.dataTable(
			{
				"dom" : '<"#toolbar">t',
				"iDisplayLength" : 1500,
				"searching" : false,
				"ordering" : false,
				"serverSide" : false,
				"ajax" : {
					type : "GET",
					url : purl+"/order/ajax-getPayPlan",
					data : function(d) {
						return $.extend({}, d, {
							"filter[orderId]" : orderId
						});
					},
					"dataSrc": function(json) {return json;},
				},
				"columns" : [
				             { "data": "periodNum", "defaultContent": ""}, 
				             { "data": "periodNum", "defaultContent": ""}, 
				             { "data": "updated", "defaultContent": "", "render": function(data) {
				            	 return data == null ? "" : $.format.date(data, "yyyy-MM-dd hh:mm");
				             }},
				             { "data": "updated", "defaultContent": "", "render": function(data) {
				            	 return data == null ? "" : $.format.date(data, "yyyy-MM-dd hh:mm");
				             }},
				             
				             ],
				             "language" : {
				            	 "url" : "/js/jquery.dataTables.lang.cn.json"
				             },
				             "initComplete" : initComplete2,
				             "drawCallback" : drawCallback2,
			});
}
function drawCallback2() {
	/*$('.table-action').click(function() {
		$.post($(this).attr("url"), function(data) {
		if(data){
			planTable.dataTable()._fnAjaxUpdate();
			 }else{
			 alert("操作失败");
			 }
		})
	});*/
}
function initComplete2() {
	$("div#toolbar").attr("style", "width: 70%;")
	$("div#toolbar").html('');
	bindLayerMouseOver();
}
function deletePayPlan(id){
    layer.confirm('确定删除吗？', {icon: 3}, function(index){
  		layer.close(index);
		    if(true){
		    	 $.ajax({
		    			url:"/order/deletePayPlan/"+id,
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			success:function(data){
		    				if (data.left) {
		    					layer.msg(data.right);
		    					planTable.dataTable()._fnAjaxUpdate();
		    				} else {
		    					layer.msg(data.right,{icon: 5});
		    				}
		    			}
		       });  
		       }
		});		
     
  }
/*function checkOrderPrice(){
	$.ajax({
		url:"/order/checkOrderPrice/"+$("#orderid").val(),
		type:"POST",
		async:false,
		dataType:"json",
		success:function(data){
			if (data.left) {
			} else {
				layer.alert(data.right,{icon: 5});
			}
		}
	});  
}*/
//弹出添加分期付款的窗口
function addPayPlan(url,orderId,seriaNum,type) {
	layer
			.open({
				type : 1,
				title : "合同分期",
				skin : 'layui-layer-rim',
				area : [ '470px', '400px' ],
				content : '' + '<form id="fenqiform" action='
						+ url
						+ '/order/savePayPlan'
						+ '>'
						+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
						+'<input type="hidden" name="type" value="'+type+'"/>'
						+'<input type="hidden" name="orderId" value="'+orderId+'"/>'
						+'<input type="hidden" name="seriaNum" value="'+seriaNum+'"/>'
						+ '<br><div class="ui-form-item"> <label class="ui-label mt10">金额：</label>'
						+ '<input class="ui-input " type="text" value="" name="price" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\\d.]/g,\'\')}else{this.value=this.value.replace(/[^\\d.]/g,\'\')}"'
						+ 'id="amounts" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
						+ '</div>'
						+ '<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">付款日期:</label>'
						+ '<input class="ui-input datepicker validate[required,custom[date],past[#payDate1]]" type="text" name="payDate" value="" id="payDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
						+ '</div>'
						+ '<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
						+ '<textarea rows="5" cols="30"  data-is="isAmount isEnough" style="resize: none;" name="remarks"></textarea>'
						+ '</div>'
						+ '</div>'
						+ '<div class="widthdrawBtBox" style="position: absolute; bottom: 10px;">'
						+ '<input type="button" onclick="savePayPlan()" class="block-btn" style="margin-left:180px" value="确认" ></div>'
						+ '</form>'
						+ '<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
			});
	var checkin = $('#payDate1').datepicker().on('click', function(ev) {
		$('.datepicker').css("z-index", "999999999");
	}).data('datepicker');
}
//弹出编辑分期付款的窗口
function toeditPayPlan(purl,id) {
	$.ajax({
						url : purl+"/order/queryPayPlanByid/" + id,
						type : "POST",
						data : {},
						success : function(data) {
							layer.open({
										type : 1,
										title : "合同分期修改",
										skin : 'layui-layer-rim',
										area : [ '470px', '400px' ],
										content : ''
												+ '<form id="editPayPlanForm" action='
												+ purl
												+ '/order/savePayPlan'
												+ '>'
												+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
												+'<input type="hidden" name ="id" value="'+data.id+'"/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> '
												+ '<div class="ui-form-item"> <label class="ui-label mt10">金额：</label>'
												+ '<input class="ui-input " type="text" value="'
												+ data.price
												+ '" name="price"  '
												+ 'id="price" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
												+ '</div>'
												+ '<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">付款日期:</label>'
												+ '<input class="ui-input datepicker validate[required,custom[date],past[#payDate1]]" type="text" name="payDate" value="'
												+ $.format.date(data.day,
														"yyyy-MM-dd")
												+ '" id="payDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
												+ '</div>'
												+ '<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
												+ '<textarea rows="4" cols="40"  data-is="isAmount isEnough" style="resize: none;" name="remarks">'
												+ data.remarks
												+ '</textarea>'
												+ '</div>'
												+ '</div>'
												+ '<div class="widthdrawBtBox" style="position: absolute; bottom: 10px;">'
												+ '<input type="button" onclick="editPayPlan()" class="block-btn" style="margin-left:180px" value="确认" ></div>'
												+ '</form>'
												+ '<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
									});
							var checkin = $('#payDate1').datepicker().on(
									'click',
									function(ev) {
										$('.datepicker').css("z-index",
												"999999999");
									}).data('datepicker');
						}
					}, "text");

}






function toeditPayDay(purl,id) {
	$.ajax({
						url : purl+"/order/queryPayPlanByid/" + id,
						type : "POST",
						data : {},
						success : function(data) {
							layer.open({
										type : 1,
										title : "合同分期修改",
										skin : 'layui-layer-rim',
										area : [ '470px', '400px' ],
										content : ''
												+ '<form id="editPayPlanForm" action='
												+ purl
												+ '/order/savePayPlan'
												+ '>'
												+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
												+ '<div class="ui-form-item"><input type="hidden" name ="id" value="'+data.id+'"/><input type="hidden" name ="orderId" value="'+data.order.id+'"/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> '
												 
												+ '</div>'
											 
												+ '<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">付款日期:</label>'
												+ '<input class="ui-input datepicker validate[required,custom[date],past[#payDate1]]" type="text" name="payDate" value="'
												+ $.format.date(data.day,
														"yyyy-MM-dd")
												+ '" id="payDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
												+ '</div>'
												+ '<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
												+ '<textarea rows="4" cols="40"  data-is="isAmount isEnough" style="resize: none;" name="remarks">'
												+ data.remarks	
												+ '</textarea>'
												+ '</div>'
												
												+ '</div>'
												+ '<div class="widthdrawBtBox" style="position: absolute; bottom: 10px;">'
												+ '<input type="button" onclick="ajaxEditPayday()" class="block-btn" style="margin-left:180px" value="确认" ></div>'
												+ '</form>'
												+'<div class="worm-tips"><div class="tips-title"> <span>注:为了保证订单分期的总额和订单总金额相等,分期金额不允许调整!</div></div>'
												+ '<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
									});
							var checkin = $('#payDate1').datepicker().on(
									'click',
									function(ev) {
										$('.datepicker').css("z-index",
												"999999999");
									}).data('datepicker');
						}
					}, "text");

}
function GetDateStr() { 
	var dd = new Date(); 
	var y = dd.getFullYear(); 
	var m = dd.getMonth()+1;//获取当前月份的日期 
	var d = dd.getDate(); 
	m=m<10?"0"+m : m;
	d=d<10?"0"+d : d;
	return y+"-"+m+"-"+d; 
	} 
function savePayPlan() {
//	 var today=GetDateStr();
	na = $("#periodNum").val();
	amounts = $("#amounts").val();
	payDate1 = $("#payDate1").val();
	if (na == "") {
		layer.msg("请填写期数");
		return;
	}
	if (amounts == "") {
		layer.msg("请填写付款金额");
		return;
	}
	if (payDate1 == "") {
		layer.msg("请选择付款日期");
		return;
	}
//	if (payDate1<today) {
//		layer.msg("请选择今天以后的日期");
//		return;
//	}
	$('#fenqiform').ajaxForm(function(data) {
		if (data.left) {
			layer.msg("添加成功");
			planTable.dataTable()._fnAjaxUpdate();
			$("#cc").trigger("click");
		} else {
			layer.msg(data.right, {icon: 5});
		}
	}).submit();
}

function ajaxEditPayday() {
	var today=GetDateStr();
	na = $("#periodNum").val();
	amounts = $("#price").val();
	payDate = $("#payDate1").val(); 
	if (payDate == "") {
		layer.msg("请选择付款日期");
		return;
	}
	if (payDate1<today) {
		layer.msg("请选择今天以后的日期");
		return;
	}
	$('#editPayPlanForm').ajaxForm(function(data) {
		if (data.left) {
			layer.msg("修改成功");
			$('#table').dataTable()._fnAjaxUpdate();
			$("#cc").trigger("click");
		} else {
			layer.msg(data.right, {icon: 5});
		}
	}).submit();
}
function editPayPlan() {
	var today=GetDateStr();
	na = $("#periodNum").val();
	amounts = $("#price").val();
	payDate = $("#payDate1").val();
	if (na == "") {
		layer.msg("请填写期数");
		return;
	}
	if (amounts == "") {
		layer.msg("请填写付款金额");
		return;
	}
	if (payDate == "") {
		layer.msg("请选择付款日期");
		return;
	}
	
	if (payDate1<today) {
		layer.msg("请选择今天以后的日期");
		return;
	}
	$('#editPayPlanForm').ajaxForm(function(data) {
		if (data.left) {
			layer.msg("修改成功");
			planTable.dataTable()._fnAjaxUpdate();
			$("#cc").trigger("click");
		} else {
			layer.msg(data.right);
		}
	}).submit();
}

//查看订单分期详情
function queryPayPlanDetail(tourl, orderId,type) {
	layer.open({
		type : 1,
		title : "订单分期详情",
		skin : 'layui-layer-rim',
		area : [ '1200px', '500px' ],
		content : '' + ' '
				+ '<iframe style="width:99%;height:98%" frameborder="no" src="'
				+ tourl + '/order/queryPayPlanDetail/' + orderId + '?type='+type+'"/>'
	});

}