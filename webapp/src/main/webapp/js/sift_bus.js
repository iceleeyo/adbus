
var swift_tableObject;

function submitPlan(){
	
	$.ajax({
		url:"/product/ajax-sift_buildPlan",
		type:"POST",
		async:false,
		dataType:"json",
		data : {
			"seriaNum":  $("#sn").val()
		},
		success:function(data){
			if (data.left == true) {
				layer.msg("下单成功");
			} else {
				layer.msg(data.right.msg,{icon: 5});
			}
		}
  }); 
	$('#sendToServer').removeAttr('onclick');
	$("#sendToServer").html('<font color="#F45C55">订单已提交!</font>');
}
function addPlan(){
	var select=$("#sh").val();
	
	if(select =='' ){
		layer.msg("请选择车辆类型!",{icon: 5});
		return ;
	}
	var lc = $("#lc").val()
	if(lc ==0 ){
		layer.msg("为了便于记录,请先登录!",{icon: 5});
		return ;
	}
	$.ajax({
		url:"/product/sift_addPlan/",
		type:"POST",
		async:false,
		dataType:"json",
		data : {
			"select" : $("#sh").val(),
			"number" : $("#busNumber").val(),
			"startDate1" : $("#startDate1").val(),
			"seriaNum":  $("#sn").val()
		},
		success:function(data){
			if (data.left == true) {
				//	swift_tableObject.fnDraw();;
					swift_tableObject.dataTable()._fnAjaxUpdate();
					layer.msg("增加计划成功!");
			} else {
				bb=false;
				layer.msg(data.right.msg,{icon: 5});
			}
		}
  }); 
	
}


function delPlan(id){
	$.ajax({
		url:"/product/ajax-delPlan",
		type:"GET",
		async:false,
		dataType:"json",
		data : {
			"id" : id,
		},
		success:function(data){
			if (data.left == true) {
					layer.msg("删除成功!");
					swift_tableObject.dataTable()._fnAjaxUpdate();
			} else {
				layer.msg(data.right.msg,{icon: 5});
			}
		
		}
  }); 
}


function queryPrice(){
	var c= $('#busNumber').val();
	var ex = /^\d+$/;
	if (!ex.test(c)) {
		c=1;
	}
	$.ajax({
		url:"/product/sift_SelectBodyPrice",
		type:"GET",
		async:false,
		dataType:"json",
		data : {
			"select" : $("#sh").val(),
		},
		success:function(data){
			if(data>0){
				var t='<strong class="swift-bprice" id="jd-price">￥'+(data*c)+'</strong>';
				$("#pd").html(t);
				$("#btn").show();
			}else {
				$("#pd").html("");
				$("#btn").hide();
			}
		}
  }); 
}
function initSwift2(table){
	swift_tableObject =table;
	 
    $('.item i').hide();
      	var isClick = false;
      	$('.sift-list .item').click(function(){

      		if(isClick){
      			 
      			$(this).removeClass('active');
      			isClick = false;
      			if(($(this).parent().has('.active')).length==0){
      			   $(this).parent().children().first().addClass("active");
      			}
      		}else{

      			
      			$(this).addClass('active');
	      		$(this).children().show();
	      		$(this).parent().children().first().removeClass("active");

	      		//add by impanxh
      			if($(this).attr("sort")==-1){
      				var list=$(this).parent().children();
      				$(this).parent().children().removeClass("active");
 					$(this).addClass("active");
      			 }
      		}
      		//add by impanxh
      		var sendContext='';
			$('.sift-list .active').each(function(){
				sendContext+=($(this).parent().attr("qt")+"_"+$(this).attr("qc"))+",";			
			});
			//alert(sendContext);
			$("#sh").val(sendContext);
			queryPrice();
			//重新画
			 table.fnDraw();
			//alert($("#sh").val());
			

      	});
      	$('i').click(function(){
      		$(this).hide();
      		isClick = true;
      		
      	});
	
	
}