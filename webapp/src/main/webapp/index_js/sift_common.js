

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


/**
 * 查当前购物车条数
 * @param pathUrl
 */
function checkTime(start,prouctId){
		$.ajax({
			url : "/checkFree",
			data:{"start":  start, "productId":prouctId},
			type : "GET",
			success : function(data) {
				alert(data);
				if (typeof(data) == "undefined"){
					
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


function dateInput(inputId,prouctId){
	var forceInput = $("#"+inputId).val();
	checkTime(forceInput,prouctId);
}
