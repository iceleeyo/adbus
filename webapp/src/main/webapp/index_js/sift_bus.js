
var swift_tableObject;



function goV2Detail(r){
    window.location.href = r;
 }




function submitPlan(){
    	layer.open({
		type: 1,
		title: "电子合同",
		skin: 'layui-layer-rim', 
		area: ['650px', '630px'], 
		content:''
		   +' '
		   +'<iframe  style="width:99%;height:90%" src="/user/busContract_templete?productid=1"/><div class="ui-form-item widthdrawBtBox"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> <input type="button" id="subWithdraworder" class="block-btn" onclick="creorder();" value="确认" style="margin:10px 0px -10px 110px;"> </div>'
		});
}

function creorder(){
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
				$("#cc").trigger("click");
				
				 var uptime = window.setTimeout(function(){
                     window.location.href=pathurl+"/product/busOrderV2_list/my";
                     clearTimeout(uptime);
                 },1500);
				 
				 
			} else {
				layer.msg(data.right.msg,{icon: 5});
			}
		}
  }); 
	$('#sendToServer').removeAttr('onclick');
	$("#sendToServer").html('<font color="red">订单已提交!</font>');
	$(".del").css("display","none");
}

function addPlan(pathurl){
	var select=$("#sh").val();
	var lc=$("#lc").val();
	if(lc=="0"){
		islogin(pathurl);
		return;
	}
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
				layer.msg("增加计划成功!");
					swift_tableObject.dataTable()._fnAjaxUpdate();
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


function initPro(pathUrl,sh,page){
	$("#productList").html("");
	$.ajax({
		url : pathUrl + "/product/sift_data?filter[sh]="+sh+"&length=10&page="+page,
		data:{},
		type : "POST",
		success : function(data) {
			var initPagination = function() {
				var num_entries = data.totalElements;
				// 创建分页
				$("#Pagination").pagination(num_entries, {
					num_edge_entries: 1, //边缘页数
					num_display_entries: 4, //主体页数
					callback: pageselectCallback,
					items_per_page:data.size, //每页显示1项,
					prev_text:'<',
					next_text:'>',
					prev_show_always:false,
					next_show_always:false

				});
			 }();
			 
			function pageselectCallback(page_index, jq){
//				alert(page_index);
				initPro2(pathUrl,sh,page_index*data.size);
				return false;
			}
				
		}}, "text");
}
function initPro2(pathUrl,sh,page){
	$("#productList").html("");
	$.ajax({
		url : pathUrl + "/product/sift_data?filter[sh]="+sh+"&length=10&start="+page,
		data:{},
		type : "POST",
		success : function(data) {
			var i=1;
			var j=0;
			var sump=4;
			$.each(data.content,function(i,item){
				$("#productList").prepend(
						"<div class=\"cont\">"+
						"<div class=\"activity inline-b\"><span>"+item.name.substring(0,7)+"</span>&nbsp;&nbsp;"+item.days+"天</div>"+
						"<div class=\"price inline-b\">"+
						"<p class=\"p-one\"><em>"+item.price+"</em>元</p>"+
						"<p class=\"p-two\">"+item.duration+"秒/次&nbsp;&nbsp;&nbsp;"+item.playNumber+"次/天</p></div>"+
						"<div class=\"num f-left inline-b\">"+
						"<input type=\"button\" class=\"icon f-left dec\" id='leftDec"+i+"' />"+
						"<input class='f-left' id='sum"+i+"' value=\"0\">"+
						"<input type=\"button\" class=\"icon f-left plus\" id='leftPlus"+i+"' /></div>"+
						"<div class=\"map f-left inline-b\">"+
						"<div class=\"map-box\"></div></div></div>"
				);
				$("#leftDec"+i+"").click(function(){
					j--;
					var oldValue=$(this).next().val();//获取文本框对象现有值
					if(oldValue>0){
						$(this).next().val(parseInt(oldValue)-1);
					}
					$("#totalnum").val(j);
					$("#totalprice").val(sump);
				});
				$("#leftPlus"+i+"").click(function(){
					j++;
					var oldValue=$(this).prev().val();//获取文本框对象现有值
					$(this).prev().val(parseInt(oldValue)+1);
					$("#totalnum").val(j);
				}); 
				i++;
				
			});
			
		}}, "text");
}
function initSwift2(purl){
/*	swift_tableObject =table;*/
	 
    $('.item i').hide();
      	var isClick = false;
      	$('.sift-list .item').click(function(){

      		if(isClick){
      			 
      			$(this).removeClass('active');
      			isClick = false;
      			if(($(this).parent().has('.active')).length==0){
      			}
      		}else{

      			
      			$(this).addClass('active');
	      		$(this).children().show();
	      		$(this).parent().children().first().removeClass("active");

	      		//add by impanxh
      			if($(this).attr("sort")==-1){
      				var list=$(this).parent().children();
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
			initPro(purl,sendContext,0)
			//重新画
			/* table.fnDraw();*/
			//alert($("#sh").val());
			

      	});
      	$('i').click(function(){
      		$(this).hide();
      		isClick = true;
      		
      	});
	
	
}