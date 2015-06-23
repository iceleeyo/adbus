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
function invoicedetail(){
	$.ajax({
			url : "${rc.contextPath}/order/invoiceDetail/"+${orderview.order.id!''},
			type : "POST",
			data : {
			},
			success : function(data) {
			var yingye="";
			var yuserid=""
			var yid=""
			var shuiwu="";
			var sid=""
			var nashui="";
			var nid=""
		
			$.each(data.files, function(i, item) {
			  if(item.type==6){
			   yingye=item.name;
			   yuserid=item.userId;
			   yid=item.id;
			  }
			  if(item.type==7){
			   shuiwu=item.name;
			   sid=item.id;
			  }
			  if(item.type==8){
			   nashui=item.name;
			   nid=item.id;
			  }
			});
				layer.open({
	    		type: 1,
	    		skin: 'layui-layer-rim', //加上边框
	    		area: ['450px', '640px'], //宽高
	    		content: '<form data-name="withdraw" name="userForm2" id="userForm2" class="ui-form" method="post" action="${rc.contextPath}/user/saveInvoice" enctype="multipart/form-data"> <input type="hidden" name="id" value="'+data.detailView.id+'"/>'
						 +'<br/>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 发票抬头: </label>  <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.detailView.title+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">税务登记证号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="taxrenum" value="'+data.detailView.taxrenum+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户银行名称:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="bankname" value="'+data.detailView.bankname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户账号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="accountnum" value="'+data.detailView.accountnum+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册场所地址:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="regisaddr" value="'+data.detailView.regisaddr+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册固定电话:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="fixphone" value="'+data.detailView.fixphone+'" id="fixphone" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">邮寄地址:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="mailaddr" value="'+data.detailView.mailaddr+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">营业执照复印件:</label> <a href="${rc.contextPath}/downloadFile/'+yuserid+'/'+yid+'"> '+yingye+'</a> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">税务登记复印件:</label><a href="${rc.contextPath}/downloadFile/'+yuserid+'/'+sid+'"> '+shuiwu+' </a></div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">纳税人资格认证复印件:</label> <a href="${rc.contextPath}/downloadFile/'+yuserid+'/'+nid+'">'+nashui+' </a></div>'
						 +'<div class="ui-form-item widthdrawBtBox">  </div></form>'
		});
			}
		}, "text");
	
}