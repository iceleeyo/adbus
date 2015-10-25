
function setCarCount(catCount){
	var exp = catCount;
	if (typeof(exp) != "undefined"){
		if(exp>0){
			$("#cardCount").html("[<b>"+exp+"</b>]");
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
					}
				}
		 }}, "text");
}
