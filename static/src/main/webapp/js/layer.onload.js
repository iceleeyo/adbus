
/****************************************
 @Name：layer v1.9.3 
 @Author：潘兴华
 */

function bindLayerMouseOverWithTime(time){
	
	$(".layer-tips").bind("mouseover",function(){
        var config={
        	//1.8.5版本设置
           // style: ['background-color:#78BA32; color:#fff', '#78BA32'],
            tips: [1, '#78BA32'],
            maxWidth:300,
            time:time,
            closeBtn:[0, true]
        }
        var tip=$(this).attr("tip");
        var options=$(this).data("tip-options");
        if(options){
            options= eval('(' + options + ')'); ;
        }else{
            options={};
        }
        config= $.extend({},config,options);
        layer.tips(tip, $(this), config)
    })
	
	
}

function bindLayerMouseWithOpen(time){
	$(".layer-tips2").bind("mouseover",function(){
		layer.closeAll();   
		var tip=$(this).attr("tip");
		layer.open({
		 tips: [1, '#78BA32'],
		  type: 1,
		  time:time,
		  shade: false,
		  title: false, //不显示标题
		  content: tip, //捕获的元素
		  cancel: function(index){
		    layer.close(index);
		  }
		});
    })
	
	
}
function bindLayerMouseOver(){
	bindLayerMouseOverWithTime(1200000); 
}

$(document).ready(function(){
	bindLayerMouseOver();
}) 