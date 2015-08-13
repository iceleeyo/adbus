/**
 * 获取产品详细
 * 依赖layer 和js/common.js 2个方法
 * author:impanxh
 * @param id
 */
function showProductlayer(tourl,id){
	layer.open({
		type: 1,
		title: "套餐详细",
		skin: 'layui-layer-rim', 
		area: ['900px', '300px'], 
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" src="'+tourl+'/product/prodetail/'+id+'"/>'
	});
	
}
function showBusDetail(tourl,id){
	$.ajax({
		url : tourl  + id,
		type : "POST",
		data : {
		},
		success : function(data) {
			
			layer.open({
				type: 1,
				title: "车辆详细信息",
				skin: 'layui-layer-rim', 
				area: ['450px', '650px'], 
				content: ' <input type="hidden" name="id" value="'+data.id+'"/>'
				+'<br/>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">车牌号: </label>  <input readonly="readonly" class="ui-input-d"'
				+'type="text" name="title" id="title" value="'+data.plateNumber+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">车辆自编号:</label><input readonly="readonly" class="ui-input-d"'
				+'type="text" name="taxrenum" value="'+data.serialNumber+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">线路:</label> <input readonly="readonly" class="ui-input-d"'
				+'type="text" name="bankname" value="'+data.line.name+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div id="bodyPro"><div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">线路级别：</label>'
				+'<input readonly="readonly"  class="ui-input-d" type="text" name="regisaddr" value="'+data.line.levelStr+'" id="lineLevel" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">车辆类别:</label>'
				+'<input class="ui-input-d" readonly="readonly" value="'+data.categoryStr+'" id="busNumber" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">营销中心:</span> </label>'
				+'<input class="ui-input-d" readonly="readonly" value="'+data.company.name+'" id="days" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">上刊日期:</label>'
				+'<input class="ui-input-d" readonly="readonly" value="'+$.format.date(data.startDay, "yyyy-MM-dd")+'" name="produceCost" id="produceCost" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
				+'</div></div>'
				+'<div id="cityPro" style="display:block"><div class="ui-form-item"> <label class="ui-label mt10">下刊日期:</label> <input readonly="readonly"  class="ui-input-d"'
				+'type="text" name="accountnum" value="'+$.format.date(data.endDay, "yyyy-MM-dd")+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10" style="width: 145px;">车辆情况:</label><textarea rows="4" cols="30" readonly="readonly" style="resize: none;margin-left: -20px;" >'+data.description+'</textarea> </div> </div>'
			});
		}
	}, "text");
	
}
//查看垫片详细
function showBlackAdlayer(tourl,id){
	$.ajax({
			url : tourl  + id,
			type : "POST",
			data : {
			},
			success : function(data) {
				
				layer.open({
	    		type: 1,
	    		title: "垫片信息",
	    		skin: 'layui-layer-rim', 
	    		area: ['400px', '400px'], 
	    		content: ' <input type="hidden" name="id" value="'+data.id+'"/>'
						 +'<br/>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">广告名称: </label>  <input readonly="readonly" class="ui-input-d"'
	    				 +'type="text" name="title" id="title" value="'+data.name+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">审核号: </label>  <input readonly="readonly" class="ui-input-d"'
	    				 +'type="text" name="title" id="title" value="'+data.seqNumber+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div id="cityPro" style="display:block"><div class="ui-form-item"> <label class="ui-label mt10">时长（秒）:</label> <input readonly="readonly"  class="ui-input-d"'
                         +'type="text" name="accountnum" value="'+data.duration+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">状态: </label>  <input readonly="readonly" class="ui-input-d"'
	    				 +'type="text" name="title" id="stats" value="" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div></div>'
		});
				if(data.stats=="online"){
					$("#stats").val("已上架"); 
				}else{
					$("#stats").val("已下架");
				}
			}
		}, "text");
	
}
//查看资质
function UserQualifi(tourl,username){
	$.ajax({
			url : tourl +"/user/qua/"+username,
			type : "POST",
			data : {
			},
			success : function(data) {
				layer.open({
	    		type: 1,
	    		title: "用户资质",
	    		skin: 'layui-layer-rim', //加上边框
	    		area: ['650px', '650px'], //宽高
	    		content: ' '
						 +'<br/>'
						 +'<div  id="qualifi"> </div>'
		});
				if(typeof(data)=="undefined"){
					$("#qualifi").append("用户尚未上传资质");
				}else{
					$.each(data, function(i, item) {
						  if(item.type==10){
							  $("#qualifi").append("营业执照：<div> <img src='"+tourl+"/downloadFile/"+item.userId+"/"+item.id+"' /></div>");
						  }
						  if(item.type==11){
							  $("#qualifi").append("税务登记证：<div> <img src='"+tourl+"/downloadFile/"+item.userId+"/"+item.id+"' /></div>");
						  }
						});
					
					
				}
			}
		}, "text");
}
//查看合同详情
function contractdetails(tourl,contractid){
	$.ajax({
		url : tourl +"/contract/ajax-contractDetail/"+contractid,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type: 1,
				title: "合同详情",
				skin: 'layui-layer-rim', //加上边框
				area: ['650px', '450px'], //宽高
				content: '<form data-name="withdraw"  enctype="multipart/form-data"> '
					+'<br/>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10"> 合同编号: </label>  <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="title" id="title" value="'+data.mainView.contractCode+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10"> 合同名称: </label>  <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="title" id="title" value="'+data.mainView.contractName+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10"> 合同类型: </label>  <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="title" id="title" value="'+data.mainView.contractType+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10">广告主:</label> <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="mailaddr" value="'+data.mainView.userId+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10">金额（￥）:</label> <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="taxrenum" value="'+data.mainView.amounts+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10">所在行业:</label> <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="bankname" value="'+data.industryname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10">上刊日期:</label> <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="accountnum" value="'+ $.format.date(data.mainView.startDate, "yyyy-MM-dd")+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item" style="margin-left:110px;"> <label class="ui-label mt10">下刊日期:</label> <input readonly="readonly" class="ui-input-d"'
					+'type="text" name="regisaddr" value="'+$.format.date(data.mainView.endDate, "yyyy-MM-dd")+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item" style="margin-left:80px;" id="contractfile"> <label class="ui-label mt10">附件:</label>  </div>'
					+'<div class="ui-form-item widthdrawBtBox">  </div></form>'
			});
			
			$.each(data.files, function(i, item) {
				$("#contractfile").append("<div style='line-height: 40px;margin-left:5px;'><a class='thumbnail'>"+item.name+"<span><img src='"+tourl+'/'+item.url+"' alt='"+item.name+"'  width='300' height='200' border='0'></span></a><a style='margin-left:20px;' href='"+tourl+"/downloadFile/"+item.userId+"/"+item.id+"'>点击下载附件</a></div><br> ");
			});
		}
	}, "text");
}

function contractdetail(tourl,contractid){
	$.ajax({
	
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type: 1,
				title: "合同详情",
				skin: 'layui-layer-rim', //加上边框
				area: ['900px', '350px'], //宽高
				content:''
					   +' '
					   +'<iframe style="width:99%;height:97%" src="'+tourl+'/contract/contractDetail/'+contractid+'"/>'
			});
			
		}
	}, "text");
}

//查看发票详情
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
	    		content: '<form data-name="withdraw" name="userForm2" id="userForm2" class="ui-form" method="post" action="'+tourl+'/user/saveInvoice" enctype="multipart/form-data"> <input type="hidden" name="id" value="'+data.detailView.id+'"/>'
						 +'<br/>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">发票类型:</label>  '+type+'</div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"> 发票抬头: </label>  <input readonly="readonly" class="ui-input-d"'
						 +'type="text" name="title" id="title" value="'+data.detailView.title+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 发票开具内容: </label>  <input readonly="readonly" class="ui-input-d"'
	    				 +'type="text" name="title" id="title" value="'+data.detailView.contents+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 领取方式: </label>  <input readonly="readonly" class="ui-input-d"'
	    				 +'type="text" name="title" id="title" value="'+data.detailView.receway+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">邮寄地址:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="mailaddr" value="'+data.detailView.mailaddr+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">邮寄联系人:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="contactman" value="'+data.detailView.contactman+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">联系方式:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="phonenum" value="'+data.detailView.phonenum+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div id="other"><div class="ui-form-item"> <label class="ui-label mt10">税务登记证号:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="taxrenum" value="'+data.detailView.taxrenum+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户银行名称:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="bankname" value="'+data.detailView.bankname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户账号:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="accountnum" value="'+data.detailView.accountnum+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册场所地址:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="regisaddr" value="'+data.detailView.regisaddr+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册固定电话:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="fixphone" value="'+data.detailView.fixphone+'" id="fixphone" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">营业执照复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+yid+'"> '+yingye+'</a> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">税务登记复印件:</label><a href="'+tourl+'/downloadFile/'+yuserid+'/'+sid+'"> '+shuiwu+' </a></div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">纳税人资格认证复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+nid+'">'+nashui+' </a></div></div>'
						 +'<div class="ui-form-item widthdrawBtBox">  </div></form>'
		});
				if(type=="普通发票"){
					 $("#other").css('display','none');
				}	
			}
		}, "text");
}
//查看电子合同
function eleContract(tourl,orderid){
	$.ajax({
		url : tourl +"/order/eleContract/"+orderid,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
	    		type: 1,
	    		title: "电子合同",
	    		skin: 'layui-layer-rim', 
	    		area: ['650px', '600px'], 
	    		content:''
	    			 +' '
	  			   	 +'<iframe  style="width:99%;height:95%" src="'+tourl+'/user/contract_templete?orderid='+orderid+'"/><div class="ui-form-item widthdrawBtBox"> </div>'
	  			});
		}
	}, "text");
}

//弹出上传物料窗口
function supEnter(tourl,city,type){
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
    		area: ['480px', '520px'], //宽高
    		content: '<form id="userForm1" name="userForm1" action="'+tourl+'/supplies/put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1" enctype="multipart/form-data" method="post"">'
					 +'<br/><br/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs"><div class="inputs">'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>物料名称</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]"'
					 +'type="text" name="name" id="name" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="支持中英文、数字、下划线">'
					 +'</div>'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>时长（秒）</label> <input class="ui-input validate[required,integer,min[5],max[180]]" onkeyup="value=value.replace(/[^\\d]/g,\'\')"'
					 +'type="text" name="duration" id="duration" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					 +'</div>'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>物料类型</label> <select class="ui-input" name="suppliesType" id="suppliesType">'
					 +'</select> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>所属行业:</label> <select id="industryId" class="ui-input" name="industryId" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" >'
					 +'</select> </div>'
					 +'<div class="ui-form-item" id="text" style="display:none;"> <label class="ui-label mt10"><span class="ui-form-required">*</span>文本信息</label>'
					 +'<input class="ui-input" type="text" name="infoContext" id="infoContext" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" style="height: 91px; width: 300px; "> </div>'
					 +'<div class="ui-form-item" id="file"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>物料上传</label> <div id="newUpload2"> <div class="filebox" id="div_1"> <input type="file" name="file" id="Sfile" class="validate[required]"> </div> </div>'
					 +'<input class="btn-sm btn-success" type="button" id="btn_add2" value="增加一行" style="margin-top: 10px;"><br> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>资质上传</label> <div id="newUpload3"> <div id="quadiv_1"> <input type="file" name="qua" id="Sfile1" class="validate[required]"> </div> </div>'
					 +'<input class="btn-sm btn-success" type="button" id="btn_add3" value="增加一行" style="margin-top: 10px;" ><br> </div> <div class="ui-form-item widthdrawBtBox"> <input type="button" id="uploadbutton" class="block-btn" onclick="subSup();" value="开始上传"> </div>'
					 +'<div id="progress1"> <div class="percent"></div> <div class="pbar"></div> <div class="elapsed"></div> </div> </div> </div></form>'

		});
		
		$.each(data, function(i, item) {
				$("#industryId").append(
						$("<option value="+item.id+">" + item.name
								+ "</option>"));
		});
		if(type=="车身"){
				$("#suppliesType").append(
						"<option value='3'>车身</option>"
				);
		}else if(type=="全屏视频"){
			$("#suppliesType").append(
			"<option value='0'>全屏视频</option>"
				);
		}else if(type=="INFO图片"){
			$("#suppliesType").append(
			"<option value='1'>INFO图片</option>"
			);
		}else if(type=="INFO字幕"){
			$("#suppliesType").append(
			"<option value='2'>INFO字幕</option>"
			);
		}else if(type=="团类"){
			$("#suppliesType").append(
			"<option value='4'>团类</option>"
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
		            '<div id="quadiv_'+i+'"><input  name="qua_'+i+'" type="file"  style="margin-top:10px;"  class="validate[required]" />' +
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
function del_2(o) {
	document.getElementById("newUpload2").removeChild(
			document.getElementById("div_" + o));
}
function del_3(o) {
	document.getElementById("newUpload3").removeChild(
			document.getElementById("quadiv_" + o));
}

function showother(){
	$("#other").css('display','block'); 
}
function hideother(){
	$("#other").css('display','none'); 
}

//弹出录入发票窗口
function IvcEnter(tourl){
			layer.open({
    		type: 1,
    		title: "发票信息录入",
    		skin: 'layui-layer-rim', //加上边框
    		area: ['560px', '600px'], //宽高
    		content: '<style type="text/css">.ui-form-item div{display:inline-block}</style><form data-name="withdraw" name="userForm3" id="userForm3" class="ui-form" method="post" action="'+tourl+'/user/saveInvoice" enctype="multipart/form-data">'
    				 +'<input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs" style="margin-left: 10px;"> <div class="inputs"> <div class="ui-form-item"> <label class="ui-labeli mt10"> <span class="ui-form-required">* </span>发票类型: </label>'
    				 +'<div class="mt10 radiobox" style="display:inline-block"> <input type="radio" name="type" checked="checked" onchange="showother()" value="special">&nbsp;增值税专用发票'
    				 +'<input type="radio" name="type"  onchange="hideother()" value="normal">&nbsp;普通发票&nbsp;&nbsp; </div> </div>'
    				 +'<div class="ui-form-item"> <label class="ui-labeli mt10"> <span class="ui-form-required">* </span>发票抬头: </label>'
    				 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="title" id="title" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""></div>'
    				 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>邮寄地址:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="mailaddr" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>邮寄联系人:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="contactman" id="contactman" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>联系方式:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="phonenum" id="phonenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div id="other" style="display:block"> <div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>税务登记证号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
					 +'type="text" name="taxrenum" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>基本户开户银行名称:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="bankname" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>基本户开户账号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="accountnum"'
					 +'id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>注册场所地址:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="regisaddr" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>注册固定电话:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="fixphone" id="fixphone" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>营业执照复印件: </label>'
					 +'<div id="newUpload2"> <div id="div_1"> <input type="file" name="licensefile" id="Sfile" class="validate[required]"> </div> </div> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>税务登记复印件: </label><div id="newUpload2"> <div class="filebox" id="div_1">'
					 +'<input type="file" name="taxfile" id="Sfile2" class="validate[required]"> </div> </div> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>一般纳税人资格认证复印件: </label>'
					 +'<input type="file" name="taxpayerfile" id="Sfile3" class="validate[required]"> </div> </div> </div></div>'
					 +'<div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdrawi" class="block-btn" onclick="subIvc(\''+tourl+'\');" value="提交"></div>'
					 +'<div id="progress1"> <div class="percent"></div> <div class="pbar"></div> <div class="elapsed"></div> </div> </form>'
				});
			$("#userForm3").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "topLeft",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        });
			//initiCheck();
			//initInvoiceRadioIcheck();

}

//提交物料
function subSup() {
    if (!$("#userForm1").validationEngine('validateBeforeSubmit'))
        return;
	var name = ($("#name").val());
	var infoContext = ($("#infoContext").val());
	var suppliesType = ($("#suppliesType").val());
	Sfile= ($("#Sfile").val());
	Sfile1= ($("#Sfile1").val());
	if(Sfile== "" && infoContext=="" ){
		//jDialog.Alert("请填写完整信息");
		layer.msg('请填写完整信息', {icon: 5});
		return;
	}
    if (!$("#industryId").val()) {
        jDialog.Alert("请选择行业");
        return;
    }
    if (Sfile.lastIndexOf(".") != -1 && suppliesType == "0") {
		var fileType = (Sfile.substring(Sfile.lastIndexOf(".") + 1,Sfile.length)).toLowerCase();
		var suppotFile = new Array();
		suppotFile[0] = "avi";
		suppotFile[1] = "mp4";
		suppotFile[2] = "rmvb";
		var flag=false;
		for (var i = 0; i < suppotFile.length; i++) {
			if (suppotFile[i] == fileType) {
				flag=true;
			}
		}
			if(flag == false)
			{
				layer.msg('文件类型只支持AVI,MP4,RMVB', {icon: 5});
			//jDialog.Alert("文件类型只支持AVI,MP4,RMVB");
			return;
			}
	}

	if (Sfile.lastIndexOf(".") != -1 && suppliesType == "1") {
		var fileType = (Sfile.substring(Sfile.lastIndexOf(".") + 1,Sfile.length)).toLowerCase();
		var suppotFile = new Array();
		suppotFile[0] = "gif";
		suppotFile[1] = "png";
		suppotFile[2] = "jpg";
		var flag=false;
		for (var i = 0; i < suppotFile.length; i++) {
			if (suppotFile[i] == fileType) {
				flag=true;
			}
		}
		if(flag == false)
		{
		layer.msg('文件类型只支持GIF,PNG,JPG', {icon: 5});
			//jDialog.Alert("文件类型只支持GIF,PNG,JPG");
			return;
		}
		
	}
	if (Sfile1.lastIndexOf(".") != -1 ) {
		var fileType = (Sfile1.substring(Sfile1.lastIndexOf(".") + 1,
				Sfile1.length)).toLowerCase();
		var suppotFile = new Array();
		suppotFile[0] = "gif";
		suppotFile[1] = "bmp";
		suppotFile[2] = "jpg";
		var flag=false;
		for (var i = 0; i < suppotFile.length; i++) {
			if (suppotFile[i] == fileType) {
				flag=true;
			}
		}
		if(flag == false)
		{
		layer.msg('文件类型只支持GIF,BMP,JPG', {icon: 5});
			//jDialog.Alert("资质类型只支持GIF,BMP,JPG");
			return;
		}
	}
	
	$('#userForm1').ajaxForm(function(data) {
	//	jDialog.Alert();
	 $("#uploadbutton").attr("disabled",true);
     $("#uploadbutton").css("background-color","#85A2AD");
       layer.msg(data.right);
		var uptime = window.setTimeout(function(){
		$("#supplieid").append(
			$("<option value="+data.left.id+" selected='selected'>" + data.left.name + "</option>")
		);
		$("#cc").trigger("click");
		clearTimeout(uptime);
		},3000)
	}).submit();
	 var uploadProcess={upath:'/upload/process'};
	 $('#progress1').anim_progressbar(uploadProcess);
	 
}

//弹出编辑发票窗口
function qEdit(tourl,id){
	$.ajax({
			url : tourl+"/user/invoice_detail/"+id,
			type : "POST",
			data : {
			},
			success : function(data) {
			var type="";
			if(data.mainView.type==0){
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
	    		area: ['500px', '600px'], //宽高
	    		content: '<form data-name="withdraw" name="userForm2" id="userForm2" class="ui-form" method="post" action="'+tourl+'/user/saveInvoice" enctype="multipart/form-data"> <input type="hidden" name="id" value="'+data.mainView.id+'"/>'
						 +'<br/><br/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">发票类型:</label><div id="up" style="padding-top: 10px;">  '+type+'</div></div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="ui-form-required">* </span>发票抬头: </label>  <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.mainView.title+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄地址:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="mailaddr" value="'+data.mainView.mailaddr+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄联系人:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="contactman" value="'+data.mainView.contactman+'" id="contactman" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>联系方式:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="phonenum" value="'+data.mainView.phonenum+'" id="phonenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div id="other"><div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>税务登记证号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="taxrenum" value="'+data.mainView.taxrenum+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>基本户开户银行名称:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="bankname" value="'+data.mainView.bankname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>基本户开户账号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="accountnum" value="'+data.mainView.accountnum+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>注册场所地址:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="regisaddr" value="'+data.mainView.regisaddr+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>注册固定电话:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="fixphone" value="'+data.mainView.fixphone+'" id="fixphone" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">营业执照复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+yid+'"> '+yingye+'</a> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">税务登记复印件:</label><a href="'+tourl+'/downloadFile/'+yuserid+'/'+sid+'"> '+shuiwu+' </a></div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">纳税人资格认证复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+nid+'">'+nashui+' </a></div></div>'
						 +'<div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdrawInvoice" class="block-btn" onclick="subInvoice();" value="确认"> </div></form>'

		});
				if(type=="普通发票"){
					 $("#other").css('display','none');
				}		
		$("#userForm2").validationEngine({
	            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
	            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
	            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
	            promptPosition: "topLeft",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
	            maxErrorsPerField: 1,
	            focusFirstField: true,
	            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
	            //success : function() { callSuccessFunction() },//验证通过时调用的函数
	        	});
			}
		}, "text");
		
}

//提交发票
function subInvoice(){
    if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
        return;
   document.getElementById('subWithdrawInvoice').setAttribute('disabled',true); 
   $("#subWithdrawInvoice").css("background-color","#85A2AD");
	$('#userForm2').ajaxForm(function(data) {
		layer.msg(data.right);
	$("#cc").trigger("click");
	}).submit();

}


function setPriceHelp(tourl,orderid){
	var p= ($("#price").val());
	if(p<=0 || p==""){
		layer.msg('订单金额必须大于0', {icon: 5});
		//jDialog.Alert("资质类型只支持GIF,BMP,JPG");
		return;
	}
	document.getElementById('uploadbutton').setAttribute('disabled',true); 
	$("#uploadbutton").css("background-color","#85A2AD");
	$.ajax({
		url : tourl ,
		type : "POST",
		data : {
			orderId:orderid,
			price:p
		},
		success : function(data) {
			  layer.msg(data.right);
				var uptime = window.setTimeout(function(){
					$("#cc").trigger("click");
					$("#prodPrice").html(p);
					clearTimeout(uptime);
					},2500)
		}
		}, "text");
	
	
}
function setOrderPrice(tourl,orderid){
			var postPath =  tourl;
			layer.open({
    		type: 1,
    		title: "订单价格设置信息",
    		skin: 'layui-layer-rim', 
    		area: ['420px', '340px'], 
    		content: 
			 '<br/><br/><form id="priceForm"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs"><div class="inputs" style="margin-left:-25px;">'
			 +'<div class="ui-form-item"> <label class="ui-labels mt10" style="width:170px;"><span class="ui-form-required">*</span>订单价格</label>' 
			 +'<input class="ui-inputLayer validate[required,custom[number],min[1]]" onblur="bu(this)" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\\d.]/g,\'\')}else{this.value=this.value.replace(/[^\\d.]/g,\'\')}"'
			 +' type="text" name="price" id="price" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="请输入订单金额">'
			 +'</div>'
			  +' <div class="ui-form-item widthdrawBtBox" style="margin-left:-20px;"> <input type="button" id="uploadbutton" class="block-btn" onclick="setPriceHelp(\''+postPath+'\','+orderid+');" value="修改订单价格"> </div>'
			 +' </div> </div></form>' 
			
			});
			$("#priceForm").validationEngine({
	            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
	            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
	            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
	            promptPosition: "topLeft",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
	            maxErrorsPerField: 1,
	            focusFirstField: true,
	            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
	            //success : function() { callSuccessFunction() },//验证通过时调用的函数
	        	});


}

function setLockTime(){
	/*var postPath =  tourl;*/
	layer.open({
	type: 1,
	title: "锁定时间设置",
	skin: 'layui-layer-rim', 
	area: ['420px', '340px'], 
	content: 
	 '<br/><br/><form id="priceForm"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs"><div class="inputs" style="margin-left:-25px;">'
	 +'<div class="ui-form-item"> <label class="ui-labels mt10" style="width:170px;margin-left:-200px;"><span class="ui-form-required">*</span>请选择锁定时间</label>' 
	 +'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text" name="startD" value="" id="LockTime" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
	 +'</div>'
	  +' <div class="ui-form-item widthdrawBtBox" style="margin-left:-20px;margin-top:20px;"> <input type="button" id="uploadbutton" class="block-btn" onclick="setPriceHelp();" value="确认"> </div>'
	 +' </div> </div></form>' 
	
	});
	var checkin = $('#LockTime').datepicker()
	.on('click', function (ev) {
	        $('.datepicker').css("z-index", "999999999");
	}).data('datepicker');
	
	$("#priceForm").validationEngine({
        validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
        inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
        success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
        promptPosition: "topLeft",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
        maxErrorsPerField: 1,
        focusFirstField: true,
        //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
        //success : function() { callSuccessFunction() },//验证通过时调用的函数
    	});


}

function bu(txtObj) {
    txtObj.value = Number(txtObj.value).toFixed(2);
}

function showRemark(contentString){
		layer.open({
	    		type: 1,
	    		title: "备注详情",
	    		skin: 'layui-layer-rim', 
	    		area: ['420px', '540px'], 
	    		content: ''
						 +'<br/><div>'
						 +'<textarea  type="textarea" style="margin-left:25px;height: 391px; width: 367px;">'
						 + contentString
						 +'</textarea>'
                         +'</div>'
		});
}
/**
 * 关闭订单
 * @param mainPath
 * @param orderid
 * @param taskid
 */
function closeOrder(mainPath,orderid,taskid){
	var closeRemark=$("#closeRemark").val();
	$("#uploadbutton").attr("disabled",true);
    $("#uploadbutton").css("background-color","#85A2AD");
	$.ajax({
		url : mainPath+"/order/closeOrder/"+taskid+"?orderid="+orderid,
		type : "POST",
		data : {
			orderid:orderid,
			closeRemark:closeRemark
		},
		success : function(data) {
			layer.msg(data.right);
			var uptime = window.setTimeout(function(){
				$("#cc").trigger("click");
				$(location).attr('href', mainPath+"/order/myTask/1");
				clearTimeout(uptime);
				},2500)
		}
	}, "text");
}
function showCloseRemark(mainPath,orderid,taskid){
	layer.open({
    		type: 1,
    		title: "请录入订单关闭原因",
    		skin: 'layui-layer-rim', 
    		area: ['420px', '540px'], 
    		content: ''
					 +'<br/><div>'
					 +'<textarea onclick="this.innerHTML=\'\';" id="closeRemark" type="textarea" style="margin-left:25px;height: 391px; width: 367px;">'
					 + '请输入订单关闭原因'
					 +'</textarea><br>'
					 +' <div class="ui-form-item widthdrawBtBox" style="margin-left:-20px;margin-top:20px;"> <input type="button" id="uploadbutton" class="block-btn" onclick="closeOrder(\''+mainPath+'\',\''+orderid+'\','+taskid+');" value="提交"> </div>'
                     +'</div>'
	});
}

/**
 * 初始化icheck by impanxh
 * 
 */
function initiCheck(){
$('input').iCheck({
    checkboxClass: 'icheckbox_square-green',
    radioClass: 'iradio_square-green',
    increaseArea: '20%' // optional
  });
}
/**
 * 发票类型选择radio by impanxh
 * 
 */
function initInvoiceRadioIcheck(){
	$('input').on('ifChecked', function(event){
		var p =($(this).val());
			if($(this).attr("name")=='type'){
				if(p=='normal'){
				hideother();
				}else {
				 showother();
				}
			}
		});
}

function getEmptyIfNull(exp){
if (typeof(exp) == "undefined")
{
   return ''
}
return exp;
}

/**
 * 查看 订单用户信息
 * @param tourl
 * @param id
 */
function showOrderUserlayer(tourl,uid){
	$.ajax({
			url : tourl  +"/user/u_ajax/"+ uid,
			type : "GET",
			data : {
			},
			success : function(data) {
				layer.open({
	    		type: 1,
	    		title: "下单用户信息",
	    		skin: 'layui-layer-rim', 
	    		area: ['450px', '550px'], 
	    		content: 
	    				 '<br><div class="ui-form-item"> <label class="ui-label mt10"> <span class="ui-form-required">* </span>真实姓名: </label>  <input readonly="readonly" class="ui-input-d"'
	    				 +'type="text" name="title" id="title" value="'+data.left.user.firstName+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>邮箱地址:</label><input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="taxrenum" value="'+getEmptyIfNull(data.left.user.email)+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>联系电话:</label> <input readonly="readonly" class="ui-input-d"'
                         +'type="text" name="bankname" value="'+getEmptyIfNull(data.left.phone)+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>所属公司:</label> <input readonly="readonly"  class="ui-input-d"'
                         +'type="text" name="accountnum" value="'+getEmptyIfNull(data.left.company)+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>所属部门:</label> <input readonly="readonly"  class="ui-input-d"'
                         +'type="text" name="regisaddr" value="'+getEmptyIfNull(data.left.department)+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item" id="Userqualifi"> <label class="ui-label mt10"><span class="ui-form-required">*</span>用户资质:</label><br> </div>'
	 });
				if(typeof(data.right)=="undefined" || data.right==""){
					$("#Userqualifi").append("用户尚未上传资质");
				}else{
					$.each(data.right, function(i, item) {
						  if(item.type==10){
							  $("#Userqualifi").append("营业执照：<div> <img src='"+tourl+"/downloadFile/"+item.userId+"/"+item.id+"' /></div>");
						  }
						  if(item.type==11){
							  $("#Userqualifi").append("税务登记证：<div> <img src='"+tourl+"/downloadFile/"+item.userId+"/"+item.id+"' /></div>");
						  }
						});
				}
			}
		}, "text");
	
}

//浮窗登录


function islogin(pathurl){
	
	layer.open({
		type: 1,
		title: "您尚未登录",
		skin: 'layui-layer-rim', 
		area: ['490px', '350px'], 
		content:''
             +'<div class="login-info module"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><form id="loginForm" name="loginForm" class="login-form"><fieldset>'
		   	 +'<div class="login-item"><input class="login-input input-p gray-input" type="text" placeholder="请输入用户名" id="username" name="username"><span class="login-name-icon icon-position-user"></span> </div>'
             +'<div class="login-item"><input class="login-input input-p gray-input" type="password" placeholder="请输入密码" id="password" name="password"> <span class="login-name-icon icon-position-user"></span> </div>'
             +'<div class="login-item s-clear"> <a class="s-right" href="'+pathurl+'/user/find_pwd">忘记密码</a></div>'
             +'<div class="login-item p-center"><input type="button" onclick="loglayer(\''+pathurl+'\')" name="submit" value="立即登录" class="login-btn login-btn-size func-submit"/> </div>'
             +'<div class="login-item p-center"><span>没有账号？</span>  <a href="'+pathurl+'/register">免费注册</a></div></fieldset></form></div>'
		});
	layer.msg("请先登录");
	return;
	
}
function loglayer(pathurl){
	var username=$("#username").val();
	var password=$("#password").val();
	$.ajax({
		url : pathurl+"/loginForLayer",
		type : "POST",
		data : {
			username:username,
			password:password
		},
		success : function(data) {
			if((data.left)==true){
				layer.msg("登陆成功");
				var uptime = window.setTimeout(function(){
					$("#cc").trigger("click");
			  		window.location.reload();
				},1000)
			}else{
				layer.msg(data.right);
			}
		}
		}, "text");
}


function subIvc(tourl){
    if (!$("#userForm3").validationEngine('validateBeforeSubmit'))
        return;
	$('#userForm3').ajaxForm(function(data) {
		 $("#subWithdrawi").attr("disabled",true);
	     $("#subWithdrawi").css("background-color","#85A2AD");
	       layer.msg(data.right);
	       $("#cc").trigger("click");
			if(typeof(data.left)!="undefined"){
			$("#cartAddressList").prepend(" <li data-aid='"+data.left.id+"' tip='"+data.left.title+"' class='layer-tips'>"
			+"<span href='javascript:;' style='text-decoration:none;' data-aid='"+data.left.id+"'>"
		    +" <div class='item'><i style='display: inline;'></i><span class=''>"+data.left.title +" <br>"
			+"  <b class='cart_address_edit' style='display: none;position:inherit;' onclick='qEdit(\""+tourl+"\","+data.left.id+")' id='"+data.left.id+"'>编辑</b>"
			+"</span></div> </span></li>");
			}
	}).submit();
	var type=$('input:radio[name="type"]:checked').val();
	if(type=="special"){
	 var uploadProcess={upath:'${rc.contextPath}/upload/process'};
	 $('#progress1').anim_progressbar(uploadProcess);
	}
}

