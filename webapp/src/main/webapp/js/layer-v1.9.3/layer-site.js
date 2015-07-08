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
	    		area: ['450px', '650px'], 
	    		content: ' <input type="hidden" name="id" value="'+data.id+'"/>'
						 +'<br/>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="ui-form-required">* </span>套餐名称: </label>  <input readonly="readonly" class="ui-input "'
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
                         +'<div class="ui-form-item"> <label class="ui-label mt10" style="width: 145px;">套餐描述:</label><textarea rows="4" cols="30" style="resize: none;margin-left: -20px;" >'+data.remarks+'</textarea>  </div>'
		});
			}
		}, "text");
	
}
//查看合同详情
function contractdetail(tourl,contractid){
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
	    		area: ['450px', '600px'], //宽高
	    		content: '<form data-name="withdraw"  enctype="multipart/form-data"> '
						 +'<br/>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"> 合同编号: </label>  <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
						 +'type="text" name="title" id="title" value="'+data.mainView.contractCode+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 合同名称: </label>  <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.mainView.contractName+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 合同类型: </label>  <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.mainView.contractType+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">广告主:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="mailaddr" value="'+data.mainView.userId+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">金额（￥）:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="taxrenum" value="'+data.mainView.amounts+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">所在行业:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="bankname" value="'+data.industryname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">开始日期:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="accountnum" value="'+ $.format.date(data.mainView.startDate, "yyyy-MM-dd")+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">终止日期:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="regisaddr" value="'+$.format.date(data.mainView.endDate, "yyyy-MM-dd")+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item" id="contractfile"> <label class="ui-label mt10">附件:</label>  </div>'
						 +'<div class="ui-form-item widthdrawBtBox">  </div></form>'
		});
				
				$.each(data.files, function(i, item) {
					$("#contractfile").append("<a href='"+tourl+"/downloadFile/"+item.userId+"/"+item.id+"'>"+item.name+"</a><br> ");
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
						 +'<div class="ui-form-item"> <label class="ui-label mt10"> 发票抬头: </label>  <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
						 +'type="text" name="title" id="title" value="'+data.detailView.title+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 发票开具内容: </label>  <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.detailView.contents+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> 领取方式: </label>  <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.detailView.receway+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10">邮寄地址:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="mailaddr" value="'+data.detailView.mailaddr+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div id="other"><div class="ui-form-item"> <label class="ui-label mt10">税务登记证号:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="taxrenum" value="'+data.detailView.taxrenum+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户银行名称:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="bankname" value="'+data.detailView.bankname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">基本户开户账号:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="accountnum" value="'+data.detailView.accountnum+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册场所地址:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="regisaddr" value="'+data.detailView.regisaddr+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10">注册固定电话:</label> <input readonly="readonly" class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
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
				   +'<div class = "ad-agreement"><br> <TEXTAREA id="agreementstr" name="agreementstr" readonly="true" type="text" cols="85" rows="22" style="margin-left:20px;border:none;">'
				   +data.left+'同意购买'+data.right
				   +'\n1.特别提示'
				   +'\n1.1 广告拟合竞价系统中心（以下称“系统中心”）同意按照本协议的规定提供竞价等相关服务（以下称“本服务”）。为获得本服务，服务使用人（以下称“用户”）应当同意本协议的全部条款并按照页面上的提示完成全部的注册程序。'
				   +'\n1.2 一旦注册并使用系统中心提供的本服务，即视为用户已了解并完全同意本条款各项内容，包括系统中心对本协议随时所做的任何修改。'
				   +'</TEXTAREA> </div>'
				   
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
		if(city=="body" && type=="车身"){
				$("#suppliesType").append(
						"<option value='3'>车身</option>"
				);
		}else if(type=="视频"){
			$("#suppliesType").append(
						"<option value='0'>视频</option>"
				);
		}else if(type=="图片"){
			$("#suppliesType").append(
			"<option value='1'>图片</option>"
			);
		}else if(type=="文本"){
			$("#suppliesType").append(
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
//提交发票
function subIvc(){
    if (!$("#userForm3").validationEngine('validateBeforeSubmit'))
        return;
	$('#userForm3').ajaxForm(function(data) {
		 $("#subWithdrawi").attr("disabled",true);
	     $("#subWithdrawi").css("background-color","#85A2AD");
	       layer.msg(data.right);
		var uptime = window.setTimeout(function(){
			window.location.reload();
		   	clearTimeout(uptime);
					},2000)
	}).submit();
	var type=$('input:radio[name="type"]:checked').val();
	if(type=="special"){
	 var uploadProcess={upath:'${rc.contextPath}/upload/process'};
	 $('#progress1').anim_progressbar(uploadProcess);
	}
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
    		area: ['560px', '700px'], //宽高
    		content: '<style type="text/css">.ui-form-item div{display:inline-block}</style><form data-name="withdraw" name="userForm3" id="userForm3" class="ui-form" method="post" action="'+tourl+'/user/saveInvoice" enctype="multipart/form-data">'
    				 +'<div class="withdrawInputs" style="margin-left: 10px;"> <div class="inputs"> <div class="ui-form-item"> <label class="ui-labeli mt10"> <span class="ui-form-required">* </span>发票类型: </label>'
    				 +'<div class="mt10 radiobox" style="display:inline-block"> <input type="radio" name="type" checked="checked" onchange="showother()" value="special">&nbsp;增值税专用发票'
    				 +'<input type="radio" name="type"  onchange="hideother()" value="normal">&nbsp;普通发票&nbsp;&nbsp; </div> </div>'
    				 +'<div class="ui-form-item"> <label class="ui-labeli mt10"> <span class="ui-form-required">* </span>发票抬头: </label>'
    				 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="title" id="title" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""></div>'
    				 +'<div class="ui-form-item"> <label class="ui-labeli mt10"><span class="ui-form-required">*</span>邮寄地址:</label>'
					 +'<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]" type="text" name="mailaddr" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
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
					 +'<div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdrawi" class="block-btn" onclick="subIvc();" value="提交"></div>'
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
			initiCheck();
			initInvoiceRadioIcheck();

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
						 +'<div class="ui-form-item"> <label class="ui-label mt10">发票类型:</label>  '+type+'</div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="ui-form-required">* </span>发票抬头: </label>  <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.mainView.title+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄地址:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="mailaddr" value="'+data.mainView.mailaddr+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
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
	$("#cc").trigger("click");
	window.location.reload();
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
					 +'<textarea id="closeRemark" type="textarea" style="margin-left:25px;height: 391px; width: 367px;">'
					 + ''
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
			url : tourl  + uid,
			type : "GET",
			data : {
			},
			success : function(data) {
				layer.open({
	    		type: 1,
	    		title: "下单用户信息",
	    		skin: 'layui-layer-rim', 
	    		area: ['420px', '540px'], 
	    		content: 
	    				 '<br><div class="ui-form-item"> <label class="ui-label mt10"> <span class="ui-form-required">* </span>真实姓名: </label>  <input readonly="readonly" class="ui-input "'
	    				 +'type="text" name="title" id="title" value="'+data.user.firstName+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>邮箱地址:</label><input readonly="readonly" class="ui-input "'
                         +'type="text" name="taxrenum" value="'+getEmptyIfNull(data.user.email)+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>联系电话:</label> <input readonly="readonly" class="ui-input "'
                         +'type="text" name="bankname" value="'+getEmptyIfNull(data.phone)+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>所属公司:</label> <input readonly="readonly"  class="ui-input "'
                         +'type="text" name="accountnum" value="'+getEmptyIfNull(data.company)+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>所属部门:</label> <input readonly="readonly"  class="ui-input"'
                         +'type="text" name="regisaddr" value="'+getEmptyIfNull(data.department)+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	 });
			}
		}, "text");
	
}







