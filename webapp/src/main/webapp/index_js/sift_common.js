

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
					layer.msg(data.status);
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
