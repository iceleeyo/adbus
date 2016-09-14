
/**
 * 获取产品详细
 * 依赖layer 和js/common.js 2个方法
 * author:impanxh
 * @param id
 * test
 */
function hiddleLayer() {
	 layer.closeAll();	 
}

function alertCompleteMsg(status){
	  if(status==0){ layer.msg("连接电商平台网络错误!", {icon: 5}); }
      else if (status == 404) { layer.msg("404错误", {icon: 5});  }
      else if (status == 203) { layer.msg("电商平台通信协议失败", {icon: 5});  }
      else if (status == 500) {  layer.msg("500错误,服务器暂时抽筋了", {icon: 5});}
}
function isNotEmptyString(str) {
		if (str != null && typeof(str) != "undefined" && str != "") {
			return str;
		}	
		return '';
	}
function showProductlayer(tourl,id){
	layer.open({
		type: 1,
		title: "套餐详细",
		skin: 'layui-layer-rim', 
		area: ['900px', '600px'], 
		content:''
			+' '
			+'<iframe style="width:100%;height:100%" frameborder="no" src="'+tourl+'/product/prodetail/'+id+'"/>'
	});
	
}
function showCustomerlayer(tourl,id,fname){
	layer.open({
		type: 1,
		title: "客户详细",
		skin: 'layui-layer-rim', 
		area: ['900px', '600px'], 
		content:''
			+' '
			+'<iframe style="width:100%;height:100%" frameborder="no" src="'+tourl+'/order/customer/'+id+"?fname="+fname+'"/>'
	});
	
}
function showProV2Detail(tourl,id){
	layer.open({
		type: 1,
		title: "套餐详细",
		skin: 'layui-layer-rim', 
		area: ['900px', '300px'], 
		content:''
			+' '
			+'<iframe style="width:100%;height:98%" frameborder="no" src="'+tourl+'/product/showProV2Detail/'+id+'"/>'
	});
	
}
function showProV2DetailByOrderID(tourl,id){
	layer.open({
		type: 1,
		title: "套餐详细",
		skin: 'layui-layer-rim', 
		area: ['900px', '300px'], 
		content:''
			+' '
			+'<iframe style="width:100%;height:98%" frameborder="no" src="'+tourl+'/product/showProV2DetailByOrderID/'+id+'"/>'
	});
	
}
//查看车辆上刊历史
function showbusOnline_history(tourl,id){
	layer.open({
		type: 1,
		title: "历史上刊记录",
		skin: 'layui-layer-rim', 
		area: ['1020px', '600px'],  
		content:''
			+' '
			+'<iframe style="width:100%;height:98%" frameborder="no" src="'+tourl+'/bus/busOnline_history/'+id+'"/>'
	});
}
//查看车身订单（carBoxBody）
function queryCarBoxBody(tourl,id){
	layer.open({
		type: 1,
		title: "订单详细",
		skin: 'layui-layer-rim', 
		area: ['1020px', '600px'],  
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" frameborder="no" src="'+tourl+'/carbox/queryCarBoxBody/'+id+'"/>'
	});
}
//查看视频订单（carBoxMeida）
function queryCarBoxMedia(tourl,id){
	layer.open({
		type: 1,
		title: "订单详细",
		skin: 'layui-layer-rim', 
		area: ['1020px', '600px'],  
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" frameborder="no" src="'+tourl+'/carbox/queryCarBoxMedia/'+id+'"/>'
	});
}
//查看车辆上刊列表
function showbusOnline_list(tourl,id){
	layer.open({
		type: 1,
		title: "所上刊车辆列表",
		skin: 'layui-layer-rim', 
		area: ['1020px', '600px'],  
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" frameborder="no" src="'+tourl+'/bus/querybusOnline/'+id+'"/>'
	});
}
//查看车辆变更历史
function showbusUpdate_history(tourl,serialNumber){
	layer.open({
		type: 1,
		title: "车辆变更历史",
		skin: 'layui-layer-rim', 
		area: ['1120px', '600px'],  
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" frameborder="no" src="'+tourl+'/bus/busUpdate_history/'+serialNumber+'"/>'
	});
	
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
	    		area: ['500px', '700px'], //宽高
	    		content: ' '
						 +'<br/>'
						 +'<div  id="qualifi" style="padding-left:50px;"> </div>'
		});
				if(typeof(data)=="undefined" || data==null ||data==""){
					$("#qualifi").append("用户尚未上传资质");
				}else{
						  if(data.user_license!=""){
							  $("#qualifi").append("营业执照：<div > <img style='height:200px; width:300px;' src='"+tourl+"/upload_temp/"+data.user_license+"' /></div>");
						  }
						  if(data.user_tax!=""){
							  $("#qualifi").append("税务登记证：<div > <img style='height:200px; width:300px;' src='"+tourl+"/upload_temp/"+data.user_tax+"' /></div>");
						  }
						  if(data.user_code!=""){
							  $("#qualifi").append("组织机构代码证书：<div > <img style='height:200px; width:300px;' src='"+tourl+"/upload_temp/"+data.user_code+"' /></div>");
						  }
				}
			}
		}, "text");
}
//查看支付凭证
function queryPayvoucher(tourl,orderid){
	$.ajax({
		url : tourl +"/user/queryPayvoucher/"+orderid,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type: 1,
				title: "支付凭证",
				skin: 'layui-layer-rim', //加上边框
				area: ['500px', '500px'], //宽高
				content: ' '
					+'<br/>'
					+'<div  id="qualifi" style="padding-left:50px;"> </div>'
			});
			if(typeof(data)=="undefined"){
				$("#qualifi").append("用户没上传支付凭证");
			}else{
				$.each(data, function(i, item) {
					if(item.type==15){
						$("#qualifi").append("<div > <img style='height:200px; width:300px;' src='"+tourl+"/downloadFile/"+item.userId+"/"+item.id+"' /></div>");
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
				area: ['750px', '450px'], //宽高
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
				area: ['900px', '450px'], //宽高
				content:''
					   +' '
					   +'<iframe style="width:100%;height:97%" frameborder="no" src="'+tourl+'/contract/contractDetail/'+contractid+'"/>'
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
			var other="";
			var otherid=""
		
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
			  if(item.type==16){
				  other=item.name;
				  otherid=item.id;
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
						 +'<div class="ui-form-item"> <label class="ui-label mt10">纳税人资格认证复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+nid+'">'+nashui+' </a></div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">其它:</label> <a href="'+tourl+'/downloadFile/'+otherid+'/'+otherid+'">'+other+' </a></div></div>'
						 +'<div class="ui-form-item widthdrawBtBox">  </div></form>'
		});
				if(type=="普通发票"){
					 $("#other").css('display','none');
				}	
			}
		}, "text");
}
//查看电子合同
function eleContract(tourl,orderid,customerId){
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
	    		area: ['800px', '650px'], 
	    		content:''
	    			 +' '
	  			   	 +'<iframe style="width:99%;height:96%" frameborder="no" src="'+tourl+'/user/contract_templete?customerId='+customerId+'&orderid='+orderid+'"/><div class="ui-form-item widthdrawBtBox"> </div>'
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
    		area: ['720px', '560px'], //宽高
    		fix: true, //不固定
    		content: '<form id="userForm1" name="userForm1" action="'+tourl+'/supplies/put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1" enctype="multipart/form-data" method="post"">'
					 +'<br/><br/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs"><div class="inputs">'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>广告名称</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]"'
					 +'type="text" name="name" id="name" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="支持中英文、数字、下划线">'
					 +'</div>'
					 +'<div class="ui-form-item" style="display:none;"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>时长（秒）</label> <input class="ui-input validate[required,integer,min[5],max[180]]" onkeyup="value=value.replace(/[^\\d]/g,\'\')"'
					 +'type="text" name="duration" id="duration" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="" value="30">'
					 +'</div>'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>物料类型</label> <select class="ui-input" name="suppliesType" id="suppliesType">'
					 +'</select> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>所属行业:</label> <select id="industryId" class="ui-input" name="industryId" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" >'
					 +'</select> </div>'
					 +'<div class="ui-form-item"><label class="ui-labels mt10">备注：</label>'
					 +'<textarea rows="4" cols="40" style="resize: none;" name="operFristcomment"></textarea></div>'
					 +'<div class="ui-form-item" id="text" style="display:none;"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>文本信息</label>'
					 +'<textarea rows="4" cols="30" name="infoContext" id="infoContext" style="resize: none;" ></textarea>'
					 +'</div>'
					 +'<div class="ui-form-item" id="file"> <label class="ui-labels mt10"><span class="ui-form-required">*</span>物料上传</label> <div id="newUpload2"> <div class="filebox" id="div_1"> <input type="file" name="file" id="Sfile" class="validate[required]"> </div> </div>'
					 +'<input class="btn-sm btn-success" type="button" id="btn_add4" value="增加一行" style="margin-top: 10px;"><br> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labels mt10">资质上传</label> <div id="newUpload3"> <div id="quadiv_1"> <input type="file" name="qua" id="Sfile1"> </div> </div>'
					 +'<input class="btn-sm btn-success" type="button" id="btn_add3" value="增加一行" style="margin-top: 10px;" ><br> </div> <div class="widthdrawBtBox" style="margin-top: 25;"> <input type="button" id="uploadbutton" style="margin-right: 20px;" class="block-btn" onclick="subSup();" value="开始上传"><input type="button" id="subWithdraw" class="block-btn" onclick="showdoc();" value="查看物料规格说明"> </div>'
					 +'<div id="progress1"> <div class="percent"></div> <div class="pbar"></div> <div class="elapsed"></div> </div> </div> </div></form>'
					 //视频说明
					 +'<div id="videoType" style="display: none"> <div class="worm-tips">'
					 +'<div class="tips-title"> <span class="icon"></span> 全屏视频广告规格要求说明 </div>'
					 +'<ol> <li>一、广告素材基础:</li> <li>1.广告时长为：5秒，10秒，15秒，30秒。</li>'
					 +'<li>2.广告发布素材为模拟，需要准备DVC pro带，需把硬广吐到带子中，同时需提供电子版的视频文件（要求：720*576像素，PAL制，<font color="red">MPEG-2</font>文件格式帧速率为：<font color="red">24</font>帧每秒。 </li>'
					 +'<li>3.以上物料素材需在广告首个发布日的7个工作日前送达。</li> <li>4.硬广前后需各加<font color="red">1-3秒</font>静帧 </li> <li>5.硬广前一秒静帧前必须加上北广的5秒倒计时</li> <li>6.<font color="red">声音要求在-12至18db</font></li> <li>7.<font color="red">需记录起始码</font></li>'
					 +'<li>二、广告素材内容、技术禁止和限制:</li> <li>1.杂帧、跳帧、加帧。</li> <li>2.非创意黑场画面。</li><li>3.模糊。</li><li>4.画面出框。</li><li>5.声画不同步。</li><li>6.吞音、破音。</li><li>7.三秒以上静帧无伴音。</li><li>8.字幕有错或误导含义。</li>'
					 +'</ol> </div> </div>'
					 
					 //图片说明
					 +'<div id="otherType" style="display: none"> <div class="worm-tips"> <div class="tips-title"><span class="icon"></span> 温馨提示</div>'
					 +'<ol> <li>1.请提供符合产品要求的物料类型，视频类型格式支持<font color="red">AVI，MP4，RMVB，MPEG-2，MPG</font>。</li>'
					 +'<li>2.图片类型格式支持<font color="red">GIF，PNG，JPG</font>；资质类型格式支持<font color="red">GIF，PNG，JPG，PDF</font>。</li>'
					 +'<li>3.在必须要的时候，请上传物料说明和广告资质。</li> <li>4.如果物料的文件比较大，可能需要一定的时间，请耐心等待。文件大小尽量控制在200M以内。</li><li>5.请勿上传违反国家广告法及相关法律法规的物料文件。</li><li>6.视频节目物料素材应符合合同附件《节目制作规范》的相关要求。</li>'
					 +'<li>7.广告物料及节目播出带（三期备播节目物料）须在广告/节目首个发布日的7个工作日前将送达乙方指定地址。如因延误或错误送达导致广告/节目延误发布的，责任由甲方承担。</li>'
					 +'</ol> </div> </div>'
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
		}else if(type=="video"){
			$("#suppliesType").append(
			"<option value='0'>全屏硬广</option>"
				);
			$("#text").hide();
            $("#file").show();
		}else if(type=="image"){
			$("#suppliesType").append(
			"<option value='1'>INFO图片</option>"
			);
			$("#text").hide();
            $("#file").show();
		}else if(type=="info"){
			$("#suppliesType").append(
			"<option value='2'>INFO字幕</option>"
			);
			$("#text").show();
            $("#file").hide();
		}else if(type=="team"){
			$("#suppliesType").append(
			"<option value='4'>团类</option>"
			);
			$("#text").hide();
            $("#file").show();
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
		$("#btn_add4").click(function() {
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
        	alert("ss");
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
	$.ajax({
		url : tourl+"/user/getUserDetail",
		type : "GET",
		data : {
		},
		success : function(data) {
			layer.open({
    		type: 1,
    		title: "发票信息录入",
    		skin: 'layui-layer-rim', //加上边框
    		area: ['560px', '570px'], //宽高
    		content: '<style type="text/css">.ui-form-item div{display:inline-block}</style><form data-name="withdraw" name="userForm3" id="userForm3" class="ui-form" method="post" action="'+tourl+'/user/saveInvoice" enctype="multipart/form-data">'
    				 +'<input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs" style="margin-left: 10px;"> <div class="inputs"> <div class="ui-form-item"> <label class="ui-labeli mt10"> <span class="ui-form-required">* </span>发票类型: </label>'
    				 +'<div class="mt10 radiobox" style="display:inline-block"> <input type="radio" name="type" checked="checked" onchange="showother()" value="special">&nbsp;增值税专用发票'
    				 +'<input type="radio" name="type"  onchange="hideother()" value="normal">&nbsp;普通发票&nbsp;&nbsp; </div> </div>'
    				 +'<div class="ui-form-item"> <label class="ui-labeli mt10"> <span class="ui-form-required">* </span>发票抬头: </label>'
    				 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[120]]" type="text" readonly="readonly" name="title" id="title" value="'+isNotEmptyString(data.company)+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""></div>'
    				 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>邮寄地址:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[120]]" type="text" name="mailaddr" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>邮寄联系人:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[120]]" type="text" name="contactman" id="contactman" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>联系方式:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[120]]" type="text" name="phonenum" id="phonenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
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
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10">银行开户许可证复印件: </label>'
					 +'<div id="newUpload2"> <div id="div_1"> <input type="file" name="licensefile" id="Sfile"> </div> </div> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10">税务登记复印件: </label><div id="newUpload2"> <div class="filebox" id="div_1">'
					 +'<input type="file" name="taxfile" id="Sfile2"> </div> </div> </div>'
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10">一般纳税人资格认证复印件: </label>'
					 +'<input type="file" name="taxpayerfile" id="Sfile3"> </div> '
					 +'<div class="ui-form-item"> <label class="ui-labeli mt10">其它: </label>'
					 +'<div id="newUpload2"> <div id="div_1"> <input type="file" name="fp_other" id="Sfile"> </div> </div> </div> </div> </div> </div>'
					 +'<div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdrawi" style="margin-left:50px;" class="block-btn" onclick="subIvc(\''+tourl+'\');" value="提交"></div>'
					 +'<div id="progress1"> <div class="percent"></div> <div class="pbar"></div> <div class="elapsed"></div> </div></form>'
				});
			$("#userForm3").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "topLeft",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            maxErrorsPerField: 1,
        });
		}
		
	}, "text");
	
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
		suppotFile[3] = "mpeg2";
		suppotFile[4] = "mpg";
		var flag=false;
		for (var i = 0; i < suppotFile.length; i++) {
			if (suppotFile[i] == fileType) {
				flag=true;
			}
		}
			if(flag == false)
			{
				layer.msg('文件类型只支持AVI,MP4,RMVB,MPEG2,MPG', {icon: 5});
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
		suppotFile[3] = "pdf";
		var flag=false;
		for (var i = 0; i < suppotFile.length; i++) {
			if (suppotFile[i] == fileType) {
				flag=true;
			}
		}
		if(flag == false)
		{
		layer.msg('资质类型只支持GIF,BMP,JPG,PDF', {icon: 5});
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
flag=true;
function showdoc(){
	if(flag == true)
	{
		if(suppliesType=="0"){
    		$("#videoType").show();
		}else{
    		$("#otherType").show();
		}
		flag=false;
	}else{
		if(suppliesType=="0"){
    		$("#videoType").hide();
		}else{
    		$("#otherType").hide();
		}
		flag=true;
	}
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
				var other="";
			var otherid=""
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
			  if(item.type==16){
				  other=item.name;
				  otherid=item.id;
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
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄联系人:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[2],maxSize[120]]"'
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
						 +'<div class="ui-form-item"> <label class="ui-label mt10">纳税人资格认证复印件:</label> <a href="'+tourl+'/downloadFile/'+yuserid+'/'+nid+'">'+nashui+' </a></div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">其它:</label> <a href="'+tourl+'/downloadFile/'+otherid+'/'+otherid+'">'+other+' </a></div></div>'
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
//弹出添加车型
function addBusModel(url) {
	layer.open({
		type : 1,
		title : "添加车型",
		skin : 'layui-layer-rim',
		area : [ '550px', '500px' ],
		content : ''
			+ '<form id="addBusModelform" action='+url+'/busselect/saveBusModel>'
			+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
			+'<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> <label class="ui-label mt10">型号：</label>'
			+'<input class="ui-input validate[required]" type="text" value="" name="name"  '
			+'id="namestr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">广告位尺寸：</label>'
			+'<input class="ui-input validate[required]" type="text" value="" name="adSlot"  '
			+'id="adSlot" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">广告商：</label>'
			+'<input   class="ui-input validate[required]" type="text" value="" name="manufacturer"  '
			+'id="manufacturer" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">单双层：</label>'
			+'<select  class="ui-input bus-model" onchange="changemodeldec(\''+url+'\')" id="doubleDecker3" name="doubleDecker" >  '
			+'<option value="false" selected="selected">单层</option><option value="true" >双层</option></select></div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">车型描述：</label>'
			+'<select class="ui-input "  name="modeldescId"  '
			+'id="modeldescId"></select>'
			+'</div>'
			+ '</div>'
			+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
			+ '<input type="button" onclick="subBusModel()" class="block-btn" value="确认" ></div>'
			+ '</form>'
			+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
	});
	$.ajax({
	       url : url + "/bus/findModedesc/false",
	       type : "GET",
	       success : function(data) {
	      $.each(data, function(i, item) {
			$("#modeldescId").append(
					$("<option value="+item.id+">" + item.description
							+ "</option>"));
	         });
}}, "text");
}
//车型信息修改
function showBusModelDetail(pathUrl,id){
	$.ajax({
		url : pathUrl  +"/api/busmodelDetail/"+ id,
		type : "POST",
		data : {
		},
		success : function(data) {
			var m='';
			var modedid=0;
			if(typeof(data.modeldesc)!="undefined"){
				m=data.modeldesc.description;
				modedid=data.modeldesc.id;
			}else{
				m='暂无描述';
			}
			layer.open({
				type: 1,
				title: "车型信息修改",
				skin: 'layui-layer-rim', 
				area: ['650px', '500px'], 
				content: ''
					+ '<form id="addBusModelform" action='+pathUrl+'/busselect/saveBusModel>'
					+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
					+'<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> <label class="ui-label mt10">型号：</label>'
					+'<input type="hidden" name="id" value="'+data.id+'"/><input class="ui-input validate[required]" type="text" value="'+data.name+'" name="name"  '
					+'id="namestr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">广告位尺寸：</label>'
					+'<input class="ui-input validate[required]" type="text" value="'+isNotEmptyString(data.adSlot)+'" name="adSlot"  '
					+'id="adSlot" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">广告商：</label>'
					+'<input   class="ui-input validate[required]" type="text" value="'+isNotEmptyString(data.manufacturer)+'" name="manufacturer"  '
					+'id="manufacturer" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">单双层：</label>'
					+'<select  class="ui-input bus-model" name="doubleDecker" onchange="changemodeldec(\''+pathUrl+'\')" id="doubleDecker3" >  '
					+'<option value="false" >单层</option><option value="true" >双层</option></select></div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">车型描述：</label>'
					+'<select class="ui-input "  name="modeldescId"  '
					+'id="modeldescId"><option value="'+modedid+'">'+isNotEmptyString(m)+'</option></select>'
					+'</div>'
					+ '</div>'
					+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
					+ '<input type="button" onclick="subBusModel()" class="block-btn" value="确认" ></div>'
					+ '</form>'
					+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
			});
			$("#doubleDecker3 option").each(function(){
				   if(Boolean($(this).val()) == data.doubleDecker){
				     $(this).attr("selected", "selected");  
				   }
				});
			$.ajax({
			       url : pathUrl + "/bus/findModedesc/"+data.doubleDecker,
			       type : "GET",
			       success : function(data) {
			      $.each(data, function(i, item) {
			    	  if(item.id!=modedid){
					$("#modeldescId").append(
							$("<option value="+item.id+">" + item.description
									+ "</option>"));
							}
			         });
		}}, "text");
		}
		
	}, "text");
	
}
function changemodeldec(purl){
	$("#modeldescId").empty();
	var doubleDecker=$("#doubleDecker3  option:selected").val();
	$.ajax({
	       url : purl + "/bus/findModedesc/"+doubleDecker,
	       type : "GET",
	       success : function(data) {
	      $.each(data, function(i, item) {
			$("#modeldescId").append(
					$("<option value="+item.id+">" + item.description
							+ "</option>"));
	         });
 }}, "text");
}
//弹出添加线路窗口
function addline(url) {
	layer.open({
				type : 1,
				title : "添加线路",
				skin : 'layui-layer-rim',
				area : [ '550px', '650px' ],
				content : ''
						+ '<form id="addLineform" action='+url+'/busselect/saveLine>'
						+ '<div class="inputs" style="margin-top: 15px;margin-left: -30px;">'
						+'<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> <label class="ui-label mt10"><span class="ui-form-required">* </span>线路名称：</label>'
						+'<input class="ui-input " id="namestr" type="text" value="" name="name" onblur="checkline(this.value)" '
						+' data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">* </span>首末站：</label>'
       	 				+'<input class="ui-input validate[required]" type="text" value="" name="routelocation"  '
       	 				+'id="routelocation" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10">线路总里程：</label>'
       	 				+'<input class="ui-input " type="text" value="" name="tolength"  '
       	 				+'id="tolength" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">* </span>所属公司：</label>'
       	 				+'<input   class="ui-input validate[required]" type="text" value="" name="office"  '
       	 				+'id="office" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">* </span>所属分公司：</label>'
       	 				+'<input class="ui-input validate[required] " type="text" value="" name="branch"  '
       	 				+'id="branch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10">线路类型：</label>'
       	 				+'<input class="ui-input " type="text" value="" name="linetype"  '
       	 				+'id="linetype" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 			+'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">* </span>车辆数：</label>'
   	 				+'<input class="ui-input " type="text" value="" name="cars"  onkeyup="value=value.replace(/[^\\d]/g,\'\')" '
   	 				+'id="busnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10">车辆详情：</label>'
       	 				+'<input class="ui-input " type="text" value="" name="description"  '
       	 				+'id="description" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10">线路级别：</label>'
						+'<select  class="ui-input bus-model" name="level" id="level">  '
       	 				+'<option value="0" selected="selected">S(特级)</option><option value="1" > A++</option> <option value="2">A+</option><option value="3">A</option></select></div>'
       	 			    +'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle"><span class="ui-form-required">* </span>所属营销中心：</span> </label>'
    				    + '<select  class="ui-input bus-model" name="companyId" id="companyId"> <option value="" selected="selected">请选择营销中心</option> </select>'
    				    +'</div>'
    			    	+'<div class="ui-form-item"> <label class="ui-label mt10">首班时间：</label>'
       	 				+'<input class="ui-input " type="text" value="" name="startTime"  '
       	 				+'id="description" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10">末班时间：</label>'
       	 				+'<input class="ui-input " type="text" value="" name="endTime"  '
       	 				+'id="description" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
    			    	+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10"><span class="ui-form-required">* </span>变更日期:</label>'
    					+'<input class="ui-input datepicker validate[required,custom[date],past[#upDate1]]" type="text" name="updated1" value="" id="upDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
    					+'</div>'
       	 				+ '</div>'
						+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
						+ '<input type="button" onclick="subLine()" class="block-btn" value="确认" ></div>'
						+ '</form>'
						+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
			});
	var checkin = $('#upDate1').datepicker()
	.on('click', function (ev) {
		$('.datepicker').css("z-index", "999999999");
	}).data('datepicker');
	   $.ajax({
	       url : url + "/bus/findAllCompany",
	       type : "GET",
	       success : function(data) {
	      $.each(data, function(i, item) {
			$("#companyId").append(
					$("<option value="+item.id+">" + item.name
							+ "</option>"));
	         });
    }}, "text");
}

//线路信息修改
function showLineDetail(pathUrl,id){
	$.ajax({
		url : pathUrl  +"/api/ajaxdetail/"+ id,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type: 1,
				title: "线路信息修改",
				skin: 'layui-layer-rim', 
				area: ['550px', '650px'], 
				content: ''
					+ '<form id="addLineform" action='+pathUrl+'/busselect/saveLine>'
					+ '<div class="inputs" style="margin-top: 15px;margin-left: -30px;">'
					+'<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> <label class="ui-label mt10">线路名称：</label>'
					+'<input type="hidden" name="id" value="'+data.id+'"/><input class="ui-input "  type="text" value="'+data.name+'" name="name"  '
					+'id="namestr" onblur="checkline(this.value)" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">首末站：</label>'
   	 				+'<input class="ui-input validate[required]" type="text" value="'+isNotEmptyString(data.routelocation)+'" name="routelocation"  '
   	 				+'id="routelocation" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">线路总里程：</label>'
   	 				+'<input class="ui-input " type="text" value="'+isNotEmptyString(data.tolength)+'" name="tolength"  '
   	 				+'id="tolength" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">所属公司：</label>'
   	 				+'<input   class="ui-input validate[required]" type="text" value="'+data.office+'" name="office"  '
   	 				+'id="office" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">所属分公司：</label>'
   	 				+'<input class="ui-input validate[required] " type="text" value="'+data.branch+'" name="branch"  '
   	 				+'id="branch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">线路类型：</label>'
   	 				+'<input class="ui-input " type="text" value="'+isNotEmptyString(data.linetype)+'" name="linetype"  '
   	 				+'id="linetype" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 			    +'<div class="ui-form-item"> <label class="ui-label mt10">车辆数：</label>'
	 				+'<input class="ui-input " type="text" value="'+data._cars+'" name="cars"  onkeyup="value=value.replace(/[^\\d]/g,\'\')" '
	 				+'id="busnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">车辆详情：</label>'
   	 				+'<input class="ui-input " type="text" value="'+isNotEmptyString(data.description)+'" name="description"  '
   	 				+'id="description" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">线路级别：</label>'
					+'<select  class="ui-input bus-model" name="level" id="level">  '
   	 				+'<option value="0" selected="selected">S(特级)</option><option value="1" > A++</option> <option value="2">A+</option><option value="3">A</option></select></div>'
   	 			    +'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">所属营销中心：</span> </label>'
			     	+ '<select  class="ui-input bus-model" name="companyId" id="companyId"> <option value="'+data.company.id+'" selected="selected">'+data.company.name+'</option> </select>'
			    	+'</div>'
			    	+'<div class="ui-form-item"> <label class="ui-label mt10">首班时间：</label>'
   	 				+'<input class="ui-input " type="text" value="'+isNotEmptyString(data.startTime)+'" name="startTime"  '
   	 				+'id="description" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
   	 				+'<div class="ui-form-item"> <label class="ui-label mt10">末班时间：</label>'
   	 				+'<input class="ui-input " type="text" value="'+isNotEmptyString(data.endTime)+'" name="endTime"  '
   	 				+'id="description" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
   	 				+'</div>'
			    	+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">变更日期:</label>'
					+'<input class="ui-input datepicker validate[required,custom[date],past[#upD]]" type="text" name="updated1" value="'+$.format.date(data.updated, "yyyy-MM-dd")+'" id="upDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
					+'</div>'
   	 				+ '</div>'
					+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
					+ '<input type="button" onclick="subLine()" class="block-btn" value="确认" ></div>'
					+ '</form>'
					+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
			});
			var checkin = $('#upDate1').datepicker()
			.on('click', function (ev) {
				$('.datepicker').css("z-index", "999999999");
			}).data('datepicker');
			var companyN=data.company.name;
			$.ajax({
				url : pathUrl + "/bus/findAllCompany",
				type : "GET",
				success : function(data) {
					$.each(data, function(i, item) {
						if(companyN!=item.name){
							$("#companyId").append(
									$("<option value="+item.id+">" + item.name
											+ "</option>"));
						}
					});
				}}, "text");
			
		}
		
	}, "text");
	
}

function subLine(){
	if (!$("#addLineform").validationEngine('validateBeforeSubmit'))
        return;
	var namestr=$("#namestr").val();
	var level=$("#level").val();
	var cars=$("#busnum").val();
	if($.trim(namestr)==""){layer.msg("请填写线路名称");return;}
	if($("#companyId  option:selected").val()==""){layer.msg("请选择营销中心");return;}
	if(level==""){layer.msg("请线路级别");return;}
	if(cars==""){layer.msg("请填车辆数");return;}
	$('#addLineform').ajaxForm(function(data) {
		if(data.left){
		     layer.msg(data.right);
		     table.dataTable()._fnAjaxUpdate();
		       $("#cc").trigger("click");
		     }else{
		     layer.msg(data.right);
		     }
		}).submit();
	}
function subBusModel(){
	if (!$("#addBusModelform").validationEngine('validateBeforeSubmit'))
		return;
	var modeldescId=$("#modeldescId  option:selected").val();
	if(modeldescId==0){
		layer.msg("请选择车型描述");
		return;
	}
	$('#addBusModelform').ajaxForm(function(data) {
		if(data.left){
			layer.msg(data.right);
			table.dataTable()._fnAjaxUpdate();
			$("#cc").trigger("click");
		}else{
			layer.msg(data.right);
		}
	}).submit();
}
//弹出添加分期付款的窗口
function addfenqi(url,seriaNum) {
	layer.open({
		type : 1,
		title : "合同分期",
		skin : 'layui-layer-rim',
		area : [ '470px', '400px' ],
		content : ''
			+ '<form id="fenqiform" action='+url+'/busselect/saveDivid?seriaNum='+seriaNum+'>'
			+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
			+'<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> <label class="ui-label mt10">期数：</label>'
			+'<input class="ui-input " type="text" value="" name="name"  '
			+'id="namestr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">金额：</label>'
			+'<input class="ui-input " type="text" value="" name="amounts" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\\d.]/g,\'\')}else{this.value=this.value.replace(/[^\\d.]/g,\'\')}"'
			+'id="amounts" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">付款日期:</label>'
			+'<input class="ui-input datepicker validate[required,custom[date],past[#payDate1]]" type="text" name="payDate1" value="" id="payDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
			+'<textarea rows="4" cols="40"  data-is="isAmount isEnough" style="resize: none;" name="remarks"></textarea>'
			+'</div>'
			+ '</div>'
			+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
			+ '<input type="button" onclick="subDivid()" class="block-btn" value="确认" ></div>'
			+ '</form>'
			+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
	});
	var checkin = $('#payDate1').datepicker()
	.on('click', function (ev) {
		$('.datepicker').css("z-index", "999999999");
	}).data('datepicker');
}
//弹出编辑分期付款的窗口
function editDividPay(tourl,id){
	$.ajax({
		url : tourl+"/busselect/queryDividPayByid/"+id,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type : 1,
				title : "合同分期修改",
				skin : 'layui-layer-rim',
				area : [ '400px', '400px' ],
				content : ''
						+ '<form id="fenqiform2" action='+tourl+'/busselect/saveDivid?seriaNum='+data.seriaNum+'>'
						+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
						+'<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/> <label class="ui-label mt10">期数：</label>'
						+'<input type="hidden" name="id" value="'+data.id+'"/><input class="ui-input " type="text" value="'+data.name+'" name="name"  '
						+'id="namestr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
       	 				+'<div class="ui-form-item"> <label class="ui-label mt10">金额：</label>'
						+'<input class="ui-input " type="text" value="'+data.amounts+'" name="amounts"  '
						+'id="amounts" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
       	 				+'</div>'
						+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">付款日期:</label>'
						+'<input class="ui-input datepicker validate[required,custom[date],past[#payDate1]]" type="text" name="payDate1" value="'+$.format.date(data.payDate, "yyyy-MM-dd")+'" id="payDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
						+'</div>'
						+'<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
						+'<textarea rows="4" cols="40"  data-is="isAmount isEnough" style="resize: none;" name="remarks">'+data.remarks+'</textarea>'
       	 				+'</div>'
						+ '</div>'
						+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
						+ '<input type="button" onclick="subDivid2()" class="block-btn" value="确认" ></div>'
						+ '</form>'
						+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
			});
		var checkin = $('#payDate1').datepicker()
		.on('click', function (ev) {
		        $('.datepicker').css("z-index", "999999999");
		}).data('datepicker');
		}
	}, "text");
	
}
//处理车身订单
function handlebodyorder(tourl,id){
	$.ajax({
		url : tourl+"/carbox/queryCarHelperyByid/"+id,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type : 1,
				title : "车身订单处理",
				skin : 'layui-layer-rim',
				area : [ '500px', '360px' ],
				content : ''
					+ '<form id="carhelperform" action='+tourl+'/carbox/editCarHelper>'
					+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
					+'<input type="hidden" name="id" value="'+data.id+'"/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">状态：</label>'
					+'<select class="ui-input ui-input-mini" name="stat" id="stats">' 
	              	+'<option value="init">待审核</option>' 
	              	+'<option value="pass">审核通过</option>' 
	              	+'<option value="refu">拒绝订单</option>' 
	     			+'</select>'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
					+'<textarea rows="4" cols="40"  data-is="isAmount isEnough" style="resize: none;" name="remarks">'+isNotEmptyString(data.remarks)+'</textarea>'
					+'</div>'
					+ '</div>'
					+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
					+ '<input type="button" onclick="subhelper()" class="block-btn" value="确认" ></div>'
					+ '</form>'
					+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
			});
			$("#stats option").each(function(){
				   if($(this).val() == data.stats){
				     $(this).attr("selected", "selected");  
				   }
				});
		}
	}, "text");
	
}
function subhelper(){
	$('#carhelperform').ajaxForm(function(data) {
		if(data.left){
			layer.msg("修改成功");
			table.dataTable()._fnAjaxUpdate();
			$("#cc").trigger("click");
		}else{
			layer.msg(data.right);
		}
	}).submit();
}
function subDivid(){
	na=$("#namestr").val();
	amounts=$("#amounts").val();
	payDate1=$("#payDate1").val();
	if(na==""){layer.msg("请填写期数");return;}
	if(amounts==""){layer.msg("请填写付款金额");return;}
	if(payDate1==""){layer.msg("请选择付款日期");return;}
	$('#fenqiform').ajaxForm(function(data) {
		if(data.left){
		     layer.msg("添加成功");
		         orderBusesTable2.dataTable()._fnAjaxUpdate();
		       $("#cc").trigger("click");
		     }else{
		     layer.msg(data.right);
		     }
		}).submit();
	}
function subDivid2(){
	na=$("#namestr").val();
	amounts=$("#amounts").val();
	payDate1=$("#payDate1").val();
	if(na==""){layer.msg("请填写期数");return;}
	if(amounts==""){layer.msg("请填写付款金额");return;}
	if(payDate1==""){layer.msg("请选择付款日期");return;}
	$('#fenqiform2').ajaxForm(function(data) {
		if(data.left){
			layer.msg("修改成功");
			orderBusesTable2.dataTable()._fnAjaxUpdate();
			$("#cc").trigger("click");
		}else{
			layer.msg(data.right);
		}
	}).submit();
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
function editOrderStartD(tourl,orderid){
	$.ajax({
		url : tourl+"/order/findOrderAndSup/"+orderid,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
	    		type: 1,
	    		title: "修改订单",
	    		skin: 'layui-layer-rim', 
	    		area: ['420px', '340px'], 
	    		content: 
				 '<br/><br/><form ><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs" id="editOrderStartTimeForm"><div class="inputs" style="margin-left:-25px;">'
	    		 +'<div class="ui-form-item"> <label class="ui-labels mt10">物料</label> <select class="ui-input"  id="supid">'
				 +'<option value="'+isNotEmptyString(data.left.supplies.id)+'" selected="selected">'+isNotEmptyString(data.left.supplies.name)+'</option></select> </div>'
				 +'<div class="ui-form-item"> <label class="ui-labels mt10" style="width:170px;margin-left:-200px;"><span class="ui-form-required">*</span>开播时间</label>' 
				 +'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text"  value="'+isNotEmptyString($.format.date(data.left.startTime, "yyyy-MM-dd"))+'" id="startD" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
				 +'</div>'
				 +'<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
				 +'<input class="ui-input " type="text" value="'+isNotEmptyString(data.left.ordRemark)+'" name="ordRemark"  '
				 +'id="ordRemark" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
				 +'</div>'
				 +' <div class="ui-form-item widthdrawBtBox" style="margin-left:-120px;"> <input type="button" id="uploadbutton" class="block-btn" onclick="editOrderStartTime(\''+tourl+'\','+orderid+');" value="修改"> </div>'
				 +' </div> </div></form>' 
				});
			var checkin = $('#startD').datepicker()
			.on('click', function (ev) {
			        $('.datepicker').css("z-index", "999999999");
			}).data('datepicker');
			$.each(data.right, function(i, item) {
				if(data.left.supplies.id!=item.id){
					$("#supid").append(
							$("<option value="+item.id+">" + item.name
									+ "</option>"));
				}
		});
		}
	}, "text");
}
function editOrderStartTime(tourl,orderid){
	var startD= $("#startD").val();
	var supid= $("#supid  option:selected").val();
	if (startD==""){
		layer.msg("请输入开播时间");
		return;
	}
	if (supid==""){
		layer.msg("请选择物料");
		return;
	}
	document.getElementById('uploadbutton').setAttribute('disabled',true); 
	$("#uploadbutton").css("background-color","#85A2AD");
	$.ajax({
		url : tourl+"/order/editOrderStartTime/"+orderid ,
		type : "POST",
		data : {
			"startD":startD,
			"supid" :supid,
			"ordRemark":$("#ordRemark").val()
		},
		success : function(data) {
			layer.msg(data.right);
			var uptime = window.setTimeout(function(){
				$("#cc").trigger("click");
				clearTimeout(uptime);
			},1500)
		}
	}, "text");
	
	
}
function setLockTime(tourl,id){
	layer.open({
	type: 1,
	title: "预留截止时间设置",
	skin: 'layui-layer-rim', 
	area: ['420px', '340px'], 
	content: 
	 '<br/><br/><form id="priceForm"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="withdrawInputs"><div class="inputs" style="margin-left:-25px;">'
	 +'<div class="ui-form-item"> <label class="ui-labels mt10" style="width:170px;margin-left:-200px;"><span class="ui-form-required">*</span>请选择预留截止时间</label>' 
	 +'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text"  value="" id="LockDate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
	 +'</div>'
	  +' <div class="ui-form-item widthdrawBtBox" style="margin-left:-20px;margin-top:20px;"> <input type="button" id="uploadbutton" class="block-btn" onclick="setLockDate(\''+tourl+'\','+id+');" value="确认"> </div>'
	 +' </div> </div></form>' 
	
	});
	var checkin = $('#LockDate').datepicker()
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
    	});
}
function setLockDate(tourl,id){
	var p= ($("#LockDate").val());
	if(p==""){
		layer.msg('请输入日期');
		return;
	}
	document.getElementById('uploadbutton').setAttribute('disabled',true); 
	$("#uploadbutton").css("background-color","#85A2AD");
	$.ajax({
		url : tourl+"/busselect/setLockDate/"+id ,
		type : "POST",
		data : {
			"lockDate":p
		},
		success : function(data) {
			layer.msg(data.right);
			var uptime = window.setTimeout(function(){
				window.location.reload();
				$("#cc").trigger("click");
				clearTimeout(uptime);
			},2500)
		}
	}, "text");
	
	
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
					 +'<textarea onclick="this.innerHTML=\'\';" id="closeRemark" type="textarea" style="margin-left:25px;height: 391px; width: 367px;border:1px;">'
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

//上传施工照片
function uploadWorkPhoto(tourl,bodycontractid,busid){
			layer.open({
    		type: 1,
    		title: "上传施工照片",
    		skin: 'layui-layer-rim', //加上边框
    		area: ['350px', '280px'], //宽高
    		content: '<form id="wpform"  action="'+tourl+'/busselect/work_online/'+bodycontractid+'/'+busid+'" enctype="multipart/form-data" method="post"">'
					 +'<br/><br/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="inputs">'
					 +'<div class="ui-form-itemB"> <label class="ui-labelB mt10">施工图片上传</label> <div id="newUpload3"> <div id="quadiv_1"> <input type="file" name="workphoto" id="Sfile1" class="validate[required]"> </div> </div>'
					 +'<input class="btn-sm btn-success" type="button" id="btn_add3" value="增加一行" style="margin-top: 10px;" ><br> </div> <div class="ui-form-itemB widthdrawBtBox"> <input type="button" id="uploadWP" class="block-btn" onclick="uploadWorkP();" value="确定安装"> </div>'
					 +'<div id="progress1"> <div class="percent"></div> <div class="pbar"></div> <div class="elapsed"></div> </div> </div></form>'

		});
		i = 2;
		j = 2;
		$("#btn_add3").click(function() {
		    $("#newUpload3").append(
		            '<div id="quadiv_'+i+'"><input  name="wokp_'+i+'" type="file"  style="margin-top:10px;"  class="validate[required]" />' +
		            '<input class="btn-sm btn-wrong" type="button"  style="margin-top:10px;" value="删除"  onclick="del_3('+ i + ')"/></div>');
		    i = i + 1;
		});
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

function editPublishLine(tourl,id){
	$.ajax({
		url : tourl  +"/busselect/queryPublishLineByid/"+ id,
		type : "GET",
		data : {
		},
		success : function(data) {
	layer.open({
	type : 1,
	title : "信息修改",
	skin : 'layui-layer-rim',
	area : [ '400px', '550px' ],
	content : ''
			+ '<form id="editPublishLineform" action='+tourl+'/busselect/savePublishLine2>'
			+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
			+ '<div class="ui-form-item"><input type="hidden" name="id" value="'+data.id+'"/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'
			+ '<label class="ui-label mt10">选择线路：</label>'
			+ '<input class="ui-input" value="'+data.line.name+'"  id="line_id" data-is="isAmount isEnough">'
			+ '</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">订购数量：</label>'
			+'<input class="ui-input " type="text" value="'+data.salesNumber+'" name="salesNumber" onkeyup="value=value.replace(/[^\\d]/g,\'\')" '
			+'id="busNumber" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
				+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">批次：</label>'
			+'<input class="ui-input " type="text" value="'+data.batch+'" name="batch"  '
			+'id="batch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">媒体类型：</label>'
			+'<input class="ui-input " type="text" value="'+data.mediaType+'" name="mediaType"  '
			+'id="batch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">刊期：</label>'
			+'<input class="ui-input " type="text" value="'+data.days+'" name="days"  '
			+'id="batch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+'<div class="ui-form-item"> <label class="ui-label mt10">备注：</label>'
			+'<input class="ui-input " type="text" value="'+data.remarks+'" name="remarks"  '
			+'id="batch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
			+'</div>'
			+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
			+ '<input type="button" onclick="editLine(\''+tourl+'\')" class="block-btn" value="保存" ></div>'
			+ '</div>'
			+ '<input type="hidden" value="'+data.line.id+'" name="lineId" id="db_id"></form>'
			+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
});
	
	
	$("#line_id").autocomplete({
	minLength: 0,
	source : tourl+"/busselect/autoComplete",
	change : function(event, ui) {
	/*if(ui.item!=null){alert(ui.item.value);}*/
	//table.fnDraw();
	},
	select : function(event, ui) {
	$('#line_id').val(ui.item.value);
	//table.fnDraw();
	//initProvince(ui.item.dbId);
	$("#db_id").val(ui.item.dbId);
	}
	}).focus(function () {
			 $(this).autocomplete("search");
		 });
			}
	}, "text");
}
//编辑保存
function editLine(purl) {
	var lineid=$("#db_id").val();
	if(lineid==0){
	   alert("请选择线路");
	   return;
	}
	if($("#busNumber").val()==0){
	 alert("数量要大于0");
	   return;
	   }
		$('#editPublishLineform').ajaxForm(function(data) {
		if(data.left){
		     layer.msg("编辑成功");
		       orderBusesTable.dataTable()._fnAjaxUpdate();
		       $("#cc").trigger("click");
		     }else{
		     layer.msg(data.right);
		     }
		}).submit();
	}
function publishAmount(tourl,id){
	$.ajax({
		url : tourl  +"/busselect/queryPublishLineByid/"+ id,
		type : "GET",
		data : {
		},
		success : function(data) {
			layer.open({
				type : 1,
				title : "发布费详情",
				skin : 'layui-layer-rim',
				area : [ '400px', '450px' ],
				content : ''
					+ '<form id="publishform01" action='+tourl+'/busselect/savePublishLine?seriaNum='+data.seriaNum+'>'
					+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
					+'<div class="ui-form-item"> <label class="ui-label mt10">批次：</label>'
					+'<input class="ui-input " type="text" value="'+data.batch+'" name="batch"  '
					+'id="batch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">发布费单价：</label>'
					+'<input class="ui-input " type="text" value="'+data.unitPrice+'" name="unitPrice"  '
					+'id="unitPrice" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">发布价值：</label>'
					+'<input class="ui-input " type="text" value="'+data.publishValue+'" name="publishValue" '
					+'id="publishValue" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">折扣率：</label>'
					+'<input class="ui-input " type="text" value="'+data.discountrate+'" name="discountrate"  '
					+'id="discountrate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">优惠后金额：</label>'
					+'<input class="ui-input " type="text" value="'+data.discountPrice+'" name="discountPrice"  '
					+'id="discountPrice" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
					+ '</div></div>'
					+ '</form>'
					+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
			});
		}
	}, "text");
}

	
//增加车辆
function addBus(pathUrl){
			layer.open({
				type: 1,
				title: "添加车辆",
				skin: 'layui-layer-rim', 
				area: ['700px', '620px'], 
				content: ''
				+ '<form id="publishform01" action='+pathUrl+'/bus/saveBus>'
				+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
				+'<div class="ui-form-item"> <label class="ui-label mt10">*车牌号： </label><input class="ui-input validate[required]"'
				+'type="text" name="plateNumber" id="plateNumber" value="" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">*车辆自编号：</label><input class="ui-input validate[required]"'
				+'type="text" name="serialNumber" value=""  data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
				+ '<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'
				+ '<label class="ui-label mt10">*选择线路：</label>'
				+ '<input class="ui-input" value=""  id="line_id" data-is="isAmount isEnough">'
				+ '</div>'
				+ '<div id="four"><div class="ui-form-item" id="model_Id">'
				+ '<label class="ui-label mt10">*选择车型：</label>'
				+ '<select  class="ui-input bus-model" name="modelId" id="model_id"> <option value="0">请选择车型</option> </select>'
				+ '</div>'
				+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">车辆类别：</label>'
				+'<select class="ui-input ui-input-mini" name="category" id="category">' 
              	+'<option value="0">包车</option>' 
              	+'<option value="1">班车</option>' 
              	+'<option value="2">机动车</option>' 
              	+'<option value="3">运营车</option>' 
     			+'</select>'
				+'</div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">营销中心：</span> </label>'
				+ '<select  class="ui-input bus-model" name="companyId" id="companyId"> <option value="" selected="selected">请选择营销中心</option> </select>'
				+'</div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">公司名称：</span> </label>'
				+'<input class="ui-input-d"  value="" name="office" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">客户：</span> </label>'
				+'<input class="ui-input-d"  value="" id="branch" name="branch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">车史：</span> </label>'
				+'<input class="ui-input-d"  value="" id="branch" name="bushis" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">车型描述：</label><textarea rows="4" name="description" cols="30"  style="resize: none;" ></textarea> </div></div>'
				+ '<div class="ui-form-item widthdrawBtBox" style="margin-left:40px;">'
				+ '<input type="button" onclick="saveBus()" class="block-btn" value="确认" ></div></div>'
				+ '<input type="hidden" value="" name="lineId" id="db_id"></form>'
			});
					    $.ajax({
		       url : pathUrl + "/bus/findAllCompany",
		       type : "GET",
		       success : function(data) {
		      $.each(data, function(i, item) {
				$("#companyId").append(
						$("<option value="+item.id+">" + item.name
								+ "</option>"));
		         });
	    }}, "text");
			
			$("#line_id").autocomplete({
				minLength: 0,
					source : pathUrl+"/busselect/autoComplete",
					change : function(event, ui) {
					},
					select : function(event, ui) {
						$('#line_id').val(ui.item.value);
						initmodel(ui.item.dbId);
						$("#db_id").val(ui.item.dbId);
					}
				}).focus(function () {
		       				 $(this).autocomplete("search");
		   				 });
		}
		


function textdown(e) {
    textevent = e;
    if (textevent.keyCode == 8) {
        return;
    }
    if (document.getElementById('description1').value.length >= 100) {
    	/*
        alert("比如：要更新的新自编号,目前新自编号")
        if (!document.all) {
            textevent.preventDefault();
        } else {
            textevent.returnValue = false;
        }*/
    }
}
function textup() {
    var s = document.getElementById('description1').value;
    //判断ID为text的文本区域字数是否超过100个 
    if (s.length > 100) {
      //  document.getElementById('description1').value = s.substring(0, 100);
    }
}
function saveBus3(){
	 layer.msg('正在开发中');
}
function addBusBatch(pathUrl){
	layer.open({
		type: 1,
		title: "车辆批量修改",
		skin: 'layui-layer-rim', 
		area: ['600px', '620px'], 
		content: ''
		+ '<form id="publishform01" action='+pathUrl+'/bus/saveBus>'
		+'<div class="ui-form-item toggle bodyToggle"><br> <label class="ui-label mt10">修改字段：</label>'
		+'<select class="ui-input ui-input-mini" name="category" id="category">' 
      	+'<option value="0">自编号</option>' 
      	+'<option value="1">车版号</option>' 
      	+'<option value="2">自编号|线路</option>' 
			+'</select>'
		+'</div>'
	 
		+'<div class="ui-form-item"> <label class="ui-label mt10">修改对应关系：</label>'
		+'<textarea rows="21" id="description1" name="description" cols="50"  style="resize: none;"  onKeyDown="textdown(event)" onKeyUp="textup()" onfocus="if(value==\'比如：要更新的新自编号,目前新自编号\'){value=\'\'}" onblur="if (value==\'\'){value=\'比如：要更新的新自编号,目前新自编号\'}">比如：要更新的新自编号,目前新自编号</textarea></div> '
		+ '<div class="ui-form-item widthdrawBtBox" style="margin-left:40px;">'
		+ '<input type="button" onclick="saveBus3()" class="block-btn" value="确认" ></div>'
		+ '<input type="hidden" value="" name="lineId" id="db_id"></form>'
	});
	 
}






//车辆信息修改
function showBusDetail(pathUrl,tourl,id){
	
	$.ajax({
		url : tourl  + id,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type: 1,
				title: "车辆信息修改",
				skin: 'layui-layer-rim', 
				area: ['550px', '600px'], 
				content: ''
					+ '<form id="publishform01" action='+pathUrl+'/bus/saveBus>'
					+ '<input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="inputs" style="margin-top: 10px;margin-left: -30px;"><input type="hidden" name="id" value="'+data.id+'"/>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">车牌号： </label><input class="ui-input validate[required]"'
					+'type="text" name="plateNumber" id="plateNumber" value="'+data.plateNumber+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">车辆自编号：</label><input  class="ui-input validate[required]"'
					+'type="text" name="serialNumber" value="'+data.serialNumber+'"  data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">旧自编号：</label><input  class="ui-input-d"'
					+'type="text" name="oldSerialNumber" value="'+data.oldSerialNumber+'" id="oldSerialNumber" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
					+ '<div class="ui-form-item">'
					+ '<label class="ui-label mt10">线路名称：</label>'
					+ '<input class="ui-input" value="'+data.line.name+'"  readonly="readonly" data-is="isAmount isEnough">'
					+ '</div>'
					+ '<div id="four"><div class="ui-form-item" id="model_Id">'
					+ '<label class="ui-label mt10">选择车型：</label>'
					+ '<select  class="ui-input bus-model" name="modelId" id="model_id"> <option value="'+data.model.id+'" selected="selected">'+data.model.name+'</option><option value="0">所有类型</option> </select>'
					+ '</div>'
					+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">车辆类别：</label>'
					+'<select class="ui-input ui-input-mini" name="category" id="category">' 
				
					+'<option value="0"'+(data.category=='baoche'? 'selected="selected"':"")+'>包车</option>' 
					+'<option value="1"'+(data.category=='banche'? 'selected="selected"':"")+'>班车</option>' 
					+'<option value="2"'+(data.category=='jidongche'? 'selected="selected"':"")+'>机动车</option>' 
					+'<option value="3"'+(data.category=='yunyingche'? 'selected="selected"':"")+'>运营车</option>' 
					+'</select>'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">营销中心：</span> </label>'
					+ '<select  class="ui-input bus-model" name="companyId" id="companyId"> <option value="'+data.company.id+'" selected="selected">'+data.company.name+'</option> </select>'
					+'</div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">公司名称：</span> </label>'
					+'<input class="ui-input-d"  value="'+data.office+'" name="office" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">分公司名称：</span> </label>'
					+'<input class="ui-input-d"  value="'+data.branch+'" name="branch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">车史：</span> </label>'
					+'<input class="ui-input-d"  value="'+isNotEmptyString(data.bushis)+'" name="bushis" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
					+'<div class="ui-form-item"> <label class="ui-label mt10">车型描述：</label><textarea rows="1" name="description" cols="30"  style="resize: none;" readonly="readonly">'+data.description+'</textarea> </div>'
					+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">变更日期:</label>'
					+'<input class="ui-input datepicker validate[required,custom[date],past[#upDate1]]" type="text" name="uodated1" value="'+$.format.date(data.updated, "yyyy-MM-dd")+'" id="upDate1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
					+'</div>'
					+'</div>'
					+ '<div class="ui-form-item widthdrawBtBox" style="margin-left:40px;">'
					+ '<input type="hidden" value="'+data.line.id+'" name="lineId" id="db_id"><input type="button" onclick="saveBus()" class="block-btn" value="确认" ></div></div>'
					+ '</form>'
			});
			var checkin = $('#upDate1').datepicker()
			.on('click', function (ev) {
				$('.datepicker').css("z-index", "999999999");
			}).data('datepicker');
			var companyN=data.company.name;
			$.ajax({
				url : pathUrl + "/bus/findAllCompany",
				type : "GET",
				success : function(data) {
					$.each(data, function(i, item) {
						if(companyN!=item.name){
							$("#companyId").append(
									$("<option value="+item.id+">" + item.name
											+ "</option>"));
						}
					});
				}}, "text");
		}
		
	}, "text");
	
	
}
//编辑保存
	function saveBus() {
		
		$("#publishform01").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  true,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
        });
		
		
		 if (!$("#publishform01").validationEngine('validateBeforeSubmit'))
	            return;
		var lineid=$("#db_id").val();
		if(lineid==0){
			layer.msg("请选择线路");
			return;
		}
		$('#publishform01').ajaxForm(function(data) {
			if(data.left){
				layer.msg("保存成功");
				table.draw();
				$("#cc").trigger("click");
			}else{
			
				if(data.right.indexOf("_exist")>=0)
				{
					layer.confirm('相同的自编号已经存在,确认保存？', {icon: 3}, function(index){
						var t=$('#publishform01').attr('action');
						$("#publishform01").attr("action",t+"?forceExcute=Y");
					    layer.close(index);
					    saveBus();
					});
				}else {
					layer.msg(data.right);
				}
				
			}
		}).submit();
	}
	
/**
 * 查看购物车判断登陆状态
*/
function tocard(pathurl) {
	var lc = $("#lc").val();
	if (lc == "0") {
		islogin(pathurl);
	}
	if (lc == "1") {
		window.location.href = pathurl + "/toCard";
	}
}

function initSalesAutocomplete(table) {
	
	 $("#salesMan").autocomplete({
		    minLength: 0,
			source : "/user/salesManAutoComplete",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#salesMan').val(ui.item.value);
				table.fnDraw();
			}
		}).focus(function () {
    	  $(this).autocomplete("search");
	 	});
}


function initCustomerAutocomplete(pathurl,table) {
$( "#customerName" ).autocomplete({
	minLength: 0,
		source: pathurl+"/user/queryMyCustomers",
		change: function( event, ui ) { 
			table.fnDraw();
		 },
		 select: function(event,ui) {
		 	$('#customerName').val(ui.item.value);
		 	table.fnDraw();
		 }
}).focus(function () {
		 $(this).autocomplete("search");
	});
//--end autocomplete

}



//微信二维码
$('.wechat').hover(
	function() {
		if($('.erweima').is(':hidden')){
			$('.erweima').show();
		}else{
			$('.erweima').hide();
		}
	}
);
$('.erweima').mouseover(
	function(){
		$('.erweima').show();
	}
)
$('.erweima').mouseout(
	function(){
		$('.erweima').hide();
	}
)

//客服电话提醒
$(".tel i").click(function(){
	/* layer.alert('世巴客服联系电话: 010-87899736', {
		title:false,
        skin: 'layui-layer-lan'
        ,closeBtn: 0
        ,shift: 4 //动画类型
    }); */
	layer.msg('世巴客服联系电话: 010-88510188', {
		 time: 50000, //50s后自动关闭
        btn: ['确定']
    });
});

//QQ客服列表
$(".qq i").click(function(){
	//页面层
	layer.open({
		type: 1,
		title: "QQ客服列表",
	    skin: 'layui-layer-rim', //加上边框
	    area: ['420px', '260px'], //宽高
	    content: ''
	    +'<div class="qq_layer">'
	    +'<span class="child"><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2118878347&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:2118878347:51" alt="联系我们" title="联系我们"/><span>QQ客服：2118878347</span></a></span>'
	    +'<span class="child"><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2148989479&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:2148989479:51" alt="联系我们" title="联系我们"/><span>QQ客服：2148989479</span></a></span>'
	    +'<span class="child"><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3461937837&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:3461937837:51" alt="联系我们" title="联系我们"/><span>QQ客服：3461937837</span></a></span>'
	   	+'</div>'
	});
});

//详情图片放大
$(".sm-imgs img").mouseover(function(){
	var url=$(this).attr('src');
	$('#big_img').attr('src',url);
})

//点击关键词搜索
$('.search-key span').click(function(){
	var val =$(this).text();
	$("#searchText").val(val);
	$("#search-btn").trigger('click');
});

//产品详情图片放大
$(function(){
	$w = $('.con img').width();
	$h = $('.con img').height();
	$w2 = $w + 150;
	$h2 = $h + 150;

	$('.con img').hover(function(){
		 $(this).stop().animate({height:$h2,width:$w2,left:"-10px",top:"-10px"},800);
	},function(){
		 $(this).stop().animate({height:$h,width:$w,left:"0px",top:"0px"},800);
	});
});







