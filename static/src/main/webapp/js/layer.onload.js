
/****************************************
 @Name：layer v1.9.3 
 @Author：潘兴华
 */
function bindLayerMouseOver(){
	  $(".layer-tips").bind("mouseover",function(){
	        var config={
	        	//1.8.5版本设置
	           // style: ['background-color:#78BA32; color:#fff', '#78BA32'],
	            tips: [1, '#78BA32'],
	            maxWidth:300,
	            time:3000,
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

$(document).ready(function(){
	bindLayerMouseOver();
})