function initSwift(table){
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
      			if($(this).attr("sort")==-1){
      				var list=$(this).parent().children();
      				$(this).parent().children().removeClass("active");
 					$(this).parent().children().first().addClass("active");
      			 }
      		}
      		//add by impanxh
      		var sendContext='';
			$('.sift-list .active').each(function(){
				sendContext+=($(this).parent().attr("qt")+"_"+$(this).attr("qc"))+",";			
			});
			$("#sh").val(sendContext);
			//重新画
			 table.draw();
      	});
      	$('i').click(function(){
      		$(this).hide();
      		isClick = true;
      	});
}