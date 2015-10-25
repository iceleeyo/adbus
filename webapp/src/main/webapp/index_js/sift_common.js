
function initCardView(pathUrl){
		$.ajax({
			url : pathUrl + "/carbox/totalView",
			data:{"t":  new Date().getTime()},
			type : "GET",
			success : function(data) {
				$("#cardCount").html("[<b>"+data.cardCount+"</b>]");
		 }}, "text");
}
