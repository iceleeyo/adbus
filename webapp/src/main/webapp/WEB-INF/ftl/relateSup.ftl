<#import "template/template.ftl" as frame>
<#import "template/pickBuses.ftl" as pickBuses>
<@frame.html title="未绑定物料订单" js=["js/jquery-ui/jquery-ui.min.js","js/layer-v1.9.3/layer/layer.js"]>
<script type="text/javascript">
    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
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
     });
</script>


<script type="text/javascript">

$(function() {
	$("#tb1").show();
	     $("#tb2").hide();
});
function go_back(){
	history.go(-1);
	
}
function showtb1(){
	     $("#tb1").show();
	     $("#tb2").hide();
	}
	function showtb2(){
	     $("#tb1").hide();
	     $("#tb2").show();
	}
function pay() {
	 
	    var contractid="";
	     var payType="";
	     var invoiceid=0;
	     var contents="";
	     var receway="";
	     var temp=document.getElementsByName("payType");
	       var isinvoice=0;
	       if($("input[type='checkbox']").is(':checked')==true){
	       isinvoice=1;
	       }else{
	       isinvoice=0;
	       }
	      for(var i=0;i<temp.length;i++)
         {
           if(temp[i].checked)
            payType = temp[i].value;
         }
	        if(payType=="contract"){
	            contractid=$("#contractCode  option:selected").val();
	            if(contractid==""){
	              jDialog.Alert("请选择合同");
	              return;
	            }
	         }else{
	            contractid=-1;
	         }
	    
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		if(isinvoice==1){
		        contents=$("#contents  option:selected").val();
	            receway=$("#receway  option:selected").val();
	            invoiceid=$('#invoiceTab :radio[name=invoiceTit]:checked').val();
	            if(contents==""){
	              jDialog.Alert("请选择发票开具内容");
	              return;
	            }
	            if(receway==""){
	              jDialog.Alert("请选择发票领取方式");
	              return;
	            }
	            if(typeof (invoiceid) == "undefined"){
	              jDialog.Alert("请选择发票");
	              return;
	            }
	  }
		$.ajax({
			url : "${rc.contextPath}/order/payment",
			type : "POST",
			data : {
				"orderid" :orderid,
				"taskid" :taskid,
				"contractid":contractid,
				"payType":payType,
				"isinvoice":isinvoice,
				"invoiceid":invoiceid,
				"contents":contents,
				"receway":receway
			},
			success : function(data) {
				jDialog.Alert(data.right);
				var uptime = window.setTimeout(function(){
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myTask/1';
            	document.body.appendChild(a);
             	a.click();
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}
	function relatSup() {
	        var supplieid=$("#supplieid  option:selected").val();
	            if(supplieid==""){
	              jDialog.Alert("请选择物料");
	              return;
	            }
		var orderid = $("#orderid").val();
		var taskid = $("#taskid").val();
		$.ajax({
			url : "${rc.contextPath}/order/modifyOrder",
			type : "POST",
			data : {
				"orderid" :orderid,
				"taskid" :taskid,
				"supplieid":supplieid
			},
			success : function(data) {
				jDialog.Alert(data.left + " # " + data.right);
				var uptime = window.setTimeout(function(){
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/myOrders/1';
            	document.body.appendChild(a);
             	a.click();
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}
	function showContract(){
	     $("#contractCode").show();
	}
	function hideContract(){
	     $("#contractCode").hide();
	}
	
	function sub(){
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
	   	var checked=document.getElementById("invoiceShow").checked;
	    var title = ($("#title").val());
		var taxrenum = ($("#taxrenum").val());
		var bankname = $("#bankname").val();
		var mailaddr = $("#mailaddr").val();
		if(title==""){
			jDialog.Alert("请填写发票抬头");
			return;
		}
		if(taxrenum==""){
			jDialog.Alert("请填写税务登记证");
			return;
		}
		if(mailaddr==""){
			jDialog.Alert("请填写发票邮寄地址");
			return;
		}
		if(bankname==""){
			jDialog.Alert("请填写基本户开户银行");
			return;
		}
	   	if(title==""){
			jDialog.Alert("请填写发票抬头");
			return;
		}
	   document.getElementById('subWithdraw').setAttribute('disabled',true); 
		$('#userForm2').ajaxForm(function(data) {
		$("#cc").trigger("click");
		window.location.reload();
		}).submit();

	}
	
	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}
	function del_3(o) {
		document.getElementById("newUpload3").removeChild(
				document.getElementById("div_" + o));
	}
	
		function sub2() {
        if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
		var name = ($("#name").val());
		var infoContext = ($("#infoContext").val());
		var suppliesType = ($("#suppliesType").val());
		Sfile= ($("#Sfile").val());
		Sfile1= ($("#Sfile1").val());
		if(Sfile== "" && infoContext=="" ){
			jDialog.Alert("请填写完整信息");
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
				jDialog.Alert("文件类型只支持AVI,MP4,RMVB");
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
				jDialog.Alert("文件类型只支持GIF,PNG,JPG");
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
				jDialog.Alert("资质类型只支持GIF,BMP,JPG");
				return;
			}
		}
		
		$('#userForm1').ajaxForm(function(data) {
			$("#cc").trigger("click");
		}).submit();
		document.getElementById('subWithdraw').setAttribute('disabled',true);
		 var uploadProcess={upath:'${rc.contextPath}/upload/process'};
		  $('#progress1').anim_progressbar(uploadProcess);
}
	//Radio反选
var isChecked = false;
function qCheck(obj){
	isChecked = isChecked ? false : true;
    obj.checked = isChecked;
}

function qEdit(id){
	$.ajax({
			url : "${rc.contextPath}/user/invoice_detail/"+id,
			type : "POST",
			data : {
			},
			success : function(data) {
				layer.open({
	    		type: 1,
	    		skin: 'layui-layer-rim', //加上边框
	    		area: ['580px', '500px'], //宽高
	    		content: '<form data-name="withdraw" name="userForm2" id="userForm2" class="ui-form" method="post" action="${rc.contextPath}/user/saveInvoice" enctype="multipart/form-data"> <input type="hidden" name="id" value="'+data.mainView.id+'"/>'
						 +'<br/><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="ui-form-required">* </span>发票抬头: </label>  <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
	    				 +'type="text" name="title" id="title" value="'+data.mainView.title+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
	    				 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>税务登记证号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="taxrenum" value="'+data.mainView.taxrenum+'" id="taxrenum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>基本户开户银行名称:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="bankname" value="'+data.mainView.bankname+'" id="bankname" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>基本户开户账号:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="accountnum" value="'+data.mainView.accountnum+'" id="accountnum" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>注册场所地址:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="regisaddr" value="'+data.mainView.regisaddr+'" id="regisaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
                         +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>注册固定电话:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="fixphone" value="'+data.mainView.fixphone+'" id="fixphone" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>邮寄地址:</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"'
                         +'type="text" name="mailaddr" value="'+data.mainView.mailaddr+'" id="mailaddr" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
						 +'<div class="ui-form-item widthdrawBtBox"> <input type="button" id="subWithdraw" class="block-btn" onclick="sub();" value="确认"> </div></form>'
		});
			}
		}, "text");
	
}

function supEnter(city){
		$.ajax({
			url : "${rc.contextPath}/supplies/getIndustry/",
			type : "GET",
			data : {
			},
			success : function(data) {
				layer.open({
	    		type: 1,
	    		skin: 'layui-layer-rim', //加上边框
	    		area: ['600px', '500px'], //宽高
	    		content: '<br><br><form id="userForm1" name="userForm1" action="put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1" enctype="multipart/form-data" method="post"">'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>物料名称</label> <input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]"'
						 +'type="text" name="name" id="name" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="支持中英文、数字、下划线"> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>物料类型</label> <select class="ui-input" name="suppliesType" id="suppliesType">'
						 +'</select> </div>'
						 +'<div class="ui-form-item"> <label class="ui-label mt10"><span class="ui-form-required">*</span>所属行业:</label> <select id="industryId" class="ui-input" name="industryId" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" >'
						 +'</select> </div>'
						 +'<div class="ui-form-item" id="text" style="display:none;"> <label class="ui-label mt10"><span class="ui-form-required">*</span>文本信息</label>'
						 +'<input class="ui-input" type="text" name="infoContext" id="infoContext" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" style="height: 91px; width: 367px; "> </div>'
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
			}
		}, "text");
	
}
</script>
<div class="color-white-bg fn-clear">
  <div id="process" class="section4">
		            <div class="node fore ready"><ul><li class="tx1">&nbsp;</li><li class="tx2">提交订单</li><li id="track_time_0" class="tx3"><#setting date_format="yyyy-MM-dd">${(orderview.order.created?date)!''}</li><li id="track_time_0" class="tx3"> 10:12:30</li></ul></div>
            		<div class="proce ready"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">支付与绑定物料</li><li id="track_time_4" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">物料审核</li><li id="track_time_1" class="tx3"></li></ul></div>
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">广告播出</li><li id="track_time_5" class="tx3"></li></ul></div>		
            		<div class="proce wait"><ul><li class="tx1">&nbsp;</li></ul></div>		
            		<div class="node wait"><ul><li class="tx1">&nbsp;</li><li class="tx2">播出完成</li><li id="track_time_6" class="tx3"></li></ul></div>
            	</div>
							  <DIV class="p20bs color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">订单详情-${orderview.longOrderId!''}</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
                  <LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>下单用户：</SPAN><SPAN class="con">${(order.creator)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>起播时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.startTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>到期时间：</SPAN><SPAN class="con"><#setting date_format="yyyy-MM-dd">${(order.endTime?date)!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">${prod.type.typeName!''}</SPAN></LI>
  <#if orderview.payTypeString?has_content>
 				  <LI style="width: 240px;"><SPAN>支付方式：</SPAN><SPAN class="con">${(orderview.payTypeString)!''}</SPAN></LI>
 				  <#if orderview.payTypeString?has_content && orderview.payTypeString=="合同">
  				  <LI style="width: 240px;"><SPAN>合同号：</SPAN><SPAN class="con">${(orderview.order.contractCode)!''}</SPAN></LI>
  				   <#elseif orderview.payTypeString?has_content && orderview.payTypeString=="线上支付">
  				   <LI style="width: 240px;"><SPAN>流水号：</SPAN><SPAN class="con">123912800234</SPAN></LI>
  				   </#if>
  				   </#if>
  				    <LI style="width: 240px;"><SPAN>是否开发票：</SPAN><SPAN class="con">
 				  <#if orderview.order.isInvoice==1 >
                  <a target="_blank" href="${rc.contextPath}/order/invoiceDetail/${orderview.order.userId!''}" > 是</a>
				   <#else>
				      否    
				  </#if></SPAN></LI>
				  <li style="width: 800; border-bottom: 1px solid #F7F7F7"></li>
				    <#if suppliesView.mainView.seqNumber?has_content >
  				  <LI style="width: 240px;"><SPAN>物料编号：</SPAN><SPAN class="con">${(suppliesView.mainView.seqNumber)!''}</SPAN></LI>
  				   </#if>
  				   <#if quafiles.files?has_content>
  				  <LI style="width: 240px;"><SPAN>物料详情：</SPAN><SPAN class="con"><a href="${rc.contextPath}/supplies/suppliesDetail/${(suppliesView.mainView.id)!''}">查看物料与用户资质</a></SPAN></LI>
  				  </#if>

</UL>
</DIV>
</DIV>
<div id="relateSup" >
<#if city.mediaType == 'body'>
<@pickBuses.pickBuses order=order product=prod city=city lineLevel=prod.lineLevel categories=categories/>
</#if>
              <div class="p20bs mt10 color-white-bg border-ec">
                 <H3 class="text-xl title-box">
                    <input type="button"  onclick="showtb1()" class="block-btn" value="支付订单">
                    <#if order?exists>
                       <#if order.supplies.id = 1 >
                        	<input type="button"  onclick="showtb2()" class="block-btn" value="绑定素材">
                       </#if>
                    </#if>
				 
                 </H3><BR>	
                 <TABLE class="ui-table ui-table-gray" id="tb1">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD width="100%" colspan=4 style="border-radius: 5px 5px 0 0;"><H4>广告主支付订单</H4></TD>
  								</TR>  	
									<TR style="height:45px;">
    								<TH style="padding:0,10px;">支付方式</TH>
    							<TD style="padding:0,10px;">
    										<input type="radio" name="payType" onchange="showContract()" value="contract" checked="checked">关联合同
				             		<input type="radio" name="payType" value="online" onchange="hideContract()" >线上支付
				             	<input type="radio" name="payType" value="others"  onchange="hideContract()">其他支付
				             	</TD>
				             	
				             	<TD style="padding:0,10px;">
				             	<div id="contractCode">
				             	<select class="ui-input" name="contractCode" id="contractCode">
                                                <option value="" selected="selected">请选择合同</option>
                                                <#if contracts?exists>
                                                <#list contracts as c>
                                                    <option value="${c.id}">${c.contractName!''}</option>
                                                </#list>
                                                </#if>
                  		</select>
                  		</div>
                  		</TD>
    									
  					</TR>
  					           <TR style="height:45px;">
    									<TH >是否开具发票</TH>
    									<TD colspan=3>
    									    <input type="checkbox" id="invoiceShow"/>开具发票
    									</TD>
				               </TR>
				            
				              <TR style="display:none;" id="invoiceTab">
				              <TH>个人发票列表</TH>
				              <TD colspan="3">
				               			<table>
				               			<#list InvoiceList as ilist>
				               				<tr>
				               				<td>
				               					<input type="radio" value="${ilist.id}" onclick="qCheck(this)" name="invoiceTit">
				               					<label onclick="qEdit(${ilist.id})">${ilist.title}</label>
				               				</td>
				               				<td>
				               					<label><font color="#FF9966">邮寄地址：${ilist.mailaddr}</font></label>
				               				</td>
				               				</tr>
				               			</#list>
				               				<tr>
				               				
				               				<td colspan="2">
				               					<select style="margin: 20px;" id="contents">
				               						<option>请选择发票开具内容</option>
				               						<option value="广告发布费">广告发布费</option>
				               						<option value="广告制作费">广告制作费</option>
				               						<option value="其他">其他</option>
				               					</select>
				               					
				               					<select id="receway">
				               						<option value="">请选择发票领取方式</option>
				               						<option value="自取">自取</option>
				               						<option value="邮寄">邮寄</option>
				               					</select>
				               				</td>
				               				</tr>
				               			</table>
				               	</TD>
				               	</TR>
				               	</TBODY>
				               	<TR>
    						        <TD colspan="4" style="text-align:center;">
    									<button type="button" onclick="pay()" class="block-btn" >确认支付</button>
    								</TD>
    							</TR>
								</TABLE>	<br>
						<TABLE class="ui-table ui-table-gray" id="tb2">
  								<TBODY>
									<TR class="dark" style="height:40px;text-align:center;border-radius: 5px 5px 0 0;">
    									<TD colspan=4 style="border-radius: 5px 5px 0 0;"><H4>绑定物料</H4></TD>
  								</TR>  	
									<TR style="height:45px;">
    									<TH width="0%">绑定素材</TH>
    									<TD colspan=3><select class="ui-input" name="supplieid" id="supplieid" style="margin: 20px;">
                                                <option value="" selected="selected">请选择物料</option>
                                                <#if supplieslist?exists>
                                                <#list supplieslist as c>
                                                    <option value="${c.id}">${c.name!''}</option>
                                                </#list>
                                                
                                                </#if>
                                       </select>
                  		               <a href="javascript:;" onclick="supEnter(${city.mediaType})">上传物料</a>
                  		               </TD>
				             	    </TR>
				             	  <TR style="height:45px;">
    						        <TD colspan=4 align="center">
									<button type="button" onclick="relatSup()" class="block-btn" >确认</button><br>
									</TD>
  								</TR>
								</TABLE>	<br>
             </div>	
			</div> 
			<input type="hidden" id="orderid" value="${orderview.order.id!''}"/>
<input type="hidden" id="taskid" value="${taskid!''}"/>
</div>

<script type="text/javascript">

  $(document).ready(function() {
       
$('#invoiceShow').on('click', function(){
var checked=document.getElementById("invoiceShow").checked;
	if(checked){
		document.getElementById("invoiceTab").style.display="";
	}else{
		document.getElementById("invoiceTab").style.display="none";
	} 
});



    } );
  </script> 
</@frame.html>






