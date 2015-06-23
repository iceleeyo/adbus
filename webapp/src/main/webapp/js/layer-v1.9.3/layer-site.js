/**
 * 获取产品详细
 * 依赖layer 和js/common.js 2个方法
 * author:impanxh
 * @param id
 */
function showProductlayer(id){
	$.ajax({
			url : "/product/ajaxdetail/"+id,
			type : "POST",
			data : {
			},
			success : function(data) {
				layer.open({
	    		type: 1,
	    		title: "套餐信息",
	    		skin: 'layui-layer-rim', 
	    		area: ['420px', '540px'], 
	    		content: ' <input type="hidden" name="id" value="'+data.id+'"/>'
						 +'<br/>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="ui-form-required">* </span>套餐描述: </label>  <input readonly="readonly" class="ui-input "'
	    				 +'type="text" name="title" id="title" value="'+data.name+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>套餐价格:</label><input readonly="readonly" class="ui-input "'
                         +'type="text" name="taxrenum" value="'+formatCurrency(data.price)+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>媒体类型:</label> <input readonly="readonly" class="ui-input "'
                         +'type="text" name="bankname" value="'+getTypeString(data.type)+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>时长（秒）:</label> <input readonly="readonly"  class="ui-input "'
                         +'type="text" name="accountnum" value="'+data.duration+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>单日播放次数:</label> <input readonly="readonly"  class="ui-input"'
                         +'type="text" name="regisaddr" value="'+data.playNumber+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>首播次数:</label> <input readonly="readonly"  class="ui-input"'
                         +'type="text" name="fixphone" value="'+data.firstNumber+'" id="fixphone" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>末播次数:</label> <input readonly="readonly"  class="ui-input"'
                         +'type="text" name="mailaddr" value="'+data.lastNumber+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>高峰时段占比:</label> <input readonly="readonly"  class="ui-input"'
                         +'type="text" name="mailaddr" value="'+data.hotRatio+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>套餐播放天数:</label> <input readonly="readonly"  class="ui-input "'
                         +'type="text" name="mailaddr" value="'+data.days+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
		});
			}
		}, "text");
	
}