/**
 * 获取产品详细
 * 依赖layer 和js/common.js 2个方法
 * author:impanxh
 * @param id
 */
function showProductlayer(tourl,id){
	$.ajax({
			url : tourl  + id,
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
function invoicedetail(tourl,orderid){
	$.ajax({
			url : tourl +"/order/invoiceDetail/"+orderid,
			type : "POST",
			data : {
			},
			success : function(data) {
				var type="";
				if(data.detailView.type==0){
				  type="普通发票";
				 }else{
				    type="专用发票";
				 }
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
	    		title: "发票信息",
	    		skin: 'layui-layer-rim', //加上边框
	    		area: ['450px', '640px'], //宽高
	    		content: '<form data-name="withdraw" name="userForm2" id="userForm2" class="ui-form" method="post" action="${rc.contextPath}/user/saveInvoice" enctype="multipart/form-data"> <input type="hidden" name="id" value="'+data.detailView.id+'"/>'
						 +'<br/>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">发票类型:</label>  '+type+'</div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 发票抬头: </label>  <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.detailView.title+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">税务登记证号:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="taxrenum" value="'+data.detailView.taxrenum+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户银行名称:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="bankname" value="'+data.detailView.bankname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户账号:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="accountnum" value="'+data.detailView.accountnum+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册场所地址:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="regisaddr" value="'+data.detailView.regisaddr+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册固定电话:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="fixphone" value="'+data.detailView.fixphone+'" id="fixphone" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">邮寄地址:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="mailaddr" value="'+data.detailView.mailaddr+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">营业执照复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+yid+'"> '+yingye+'</a> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">税务登记复印件:</label><a href="'+tourl+'/downloadFile/'+yuserid+'/'+sid+'"> '+shuiwu+' </a></div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">纳税人资格认证复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+nid+'">'+nashui+' </a></div>'
						 +'<div class="ui-form-item widthdrawBtBox">  </div></form>'
		});
			}
		}, "text");
}
function supEnter(tourl,city){
	$.ajax({
		url : tourl+"/supplies/getIndustry",
		type : "GET",
		data : {
		},
		success : function(data) {
			layer.open({
    		type: 1,
    		title: "物料录入",
    		skin: 'layui-layer-rim', //加上边框
    		area: ['500px', '520px'], //宽高
    		content: '<form id="userForm1" name="userForm1" action="'+tourl+'/supplies/put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1" enctype="multipart/form-data" method="post"">'
					 +'<br/><br/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs"><div class="inputs">'
					 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>物料名称</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]"'
					 +'type="text" name="name" id="name" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="支持中英文、数字、下划线">'
					 +'</div>'
					 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>物料类型</label> <select class="ui-input" name="suppliesType" id="suppliesType">'
					 +'</select> </div>'
					 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>所属行业:</label> <select id="industryId" class="ui-input" name="industryId" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" >'
					 +'</select> </div>'
					 +'<div class="ui-form-item" id="text" style="display:none;"> <label class="ui-label mt10"><span class="ui-form-required">*</span>文本信息</label>'
					 +'<input class="ui-input" type="text" name="infoContext" id="infoContext" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" style="height: 91px; width: 300px; "> </div>'
					 +'<div class="ui-form-item" id="file"> <label class="ui-label mt10"><span class="ui-form-required">*</span>物料上传</label> <div id="newUpload2"> <div class="filebox" id="div_1"> <input type="file" name="file" id="Sfile" class="validate[required]"> </div> </div>'
					 +'<input class="btn-sm btn-success" type="button" id="btn_add2" value="增加一行" style="margin-top: 10px;"><br> </div>'
					 +'<div class="ui-form-item"> <label class="ui-label mt10">资质上传</label> <div id="newUpload3"> <div id="div_1"> <input type="file" name="qua" id="Sfile1"> </div> </div>'
					 +'<input class="btn-sm btn-success" type="button" id="btn_add3" value="增加一行" style="margin-top: 10px;" ><br> </div> <div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdraw" class="block-btn" onclick="sub2();" value="开始上传"> </div>'
					 +'<div id="progress1"> <div class="percent"></div> <div class="pbar"></div> <div class="elapsed"></div> </div> </div> </div></form>'

		});
		
		$.each(data, function(i, item) {
				$("#industryId").append(
						$("<option value="+item.id+">" + item.name
								+ "</option>"));
		});
		if(city=="body"){
				$("#suppliesType").append(
						"<option value='3'>车身</option>"
				);
		}else{
			$("#suppliesType").append(
						"<option value='0'>视频</option>"+
						"<option value='1'>图片</option>"+
						"<option value='2'>文本</option>"
				);
		};
		$("#userForm1").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "topLeft",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        	});
       	    i = 2;
			j = 2;
	        $("#btn_add2").click(function() {
        $("#newUpload2").append(
                '<div id="div_'+j+'"><input  name="file_'+j+'" type="file"  style="margin-top:10px;"  class="validate[required]" />' +
                '<input class="btn-sm btn-wrong" type="button"  style="margin-top:10px;" value="删除"  onclick="del_2('+ j + ')"/></div>');
        j = j + 1;
    });
    $("#btn_add3").click(function() {
        $("#newUpload3").append(
                '<div id="div_'+i+'"><input  name="qua_'+i+'" type="file"  style="margin-top:10px;"  class="validate[required]" />' +
                '<input class="btn-sm btn-wrong" type="button"  style="margin-top:10px;" value="删除"  onclick="del_3('+ i + ')"/></div>');
        i = i + 1;
    });
	$("#suppliesType").change(function(){
        var suppliesType = $(this).val();
        if(suppliesType=="0" || suppliesType=="1"){
            $("#text").hide();
            $("#file").show();
        }
        if(suppliesType=="2"){
            $("#text").show();
            $("#file").hide();
        }
	});
		}
			
	}, "text");
	

}