<#import "template/template.ftl" as frame>
<#import "template/proDetail.ftl" as proDetail>
<#global menu="购买产品">
<@frame.html title="未绑定物料订单" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css"]>



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
    });
</script>

<script type="text/javascript">

$(document).ready(function(){ 
    //$("#subWithdraw").attr('disabled',false); //移除disabled属性
}); 
function sub2() {
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
		//	jDialog.Alert(data.right);
		 $("#uploadbutton").attr("disabled",true);
         $("#uploadbutton").css("background-color","#85A2AD");
         layer.msg('上传成功!');
			var uptime = window.setTimeout(function(){
			$("#suppliesId").append(
				$("<option value="+data.left.id+" selected='selected'>" + data.left.name + "</option>")
			);
			$("#cc").trigger("click");
			clearTimeout(uptime);
			},3000)
		}).submit();
		document.getElementById('subWithdraw').setAttribute('disabled',true);
		
		 var uploadProcess={upath:'${rc.contextPath}/upload/process'};
		 $('#progress1').anim_progressbar(uploadProcess);
		 
}
	function sub() {
        var startTime = $("#startTime").val();
        var d = new Date(startTime.replace(/-/g,"/")); 
        date = new Date();
        var str  = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
        var d2 = new Date(str.replace(/-/g,"/")); 
        if(startTime=""){
         jDialog.Alert('请填写开播日期');
         return;
         }
           var days=Math.floor((d-d2)/(24*3600*1000));
        if(days<3) {
            jDialog.Alert('开播日期请选择3天以后');
            return;
         } 
         //author :impanxh 阻止2次点击 ,当所有表单都验证通过时才提交 抄自注册页面
         if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
         $("#subWithdraw").attr("disabled",true);
         $("#subWithdraw").css("background-color","#85A2AD");
         $('#userForm2').submit();
	}

	function check() {
		var c = $("#contractCode").val();
		$.ajax({
			url : "${rc.contextPath}/contract/contractCodeCheck",
			type : "POST",
			data : {
				"code" : c
			},
			success : function(data) {
				jDialog.Alert(data.right);
			}
		}, "text");
	}
</script>

							<form data-name="withdraw" name="userForm2" id="userForm2"
								class="ui-form" method="post" action="../creOrder2"
								enctype="multipart/form-data">
                                <@proDetail.proDetail prod=prod buyLink=false/>


<div class="p20bs mt10 color-white-bg border-ec">
                <H3 class=".withdraw-title text-xl title-box"><A class="black" href="#">填写订单信息</A></H3><br>
									<div class="inputs">
                                            <input type="hidden" readonly="readonly" name="product.Id" id="productId" value="${prod.id!''}"/>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
												class="ui-form-required">*</span>开播日期</label> <input
                                                class="ui-input datepicker validate[required,custom[date]" 
                                                type="text" name="startTime1"
                                                id="startTime" data-is="isAmount isEnough"
                                                autocomplete="off" disableautocomplete="">
                                                
                                               	 
                                        </div>
										<div class="ui-form-item">
											<label class="ui-label mt10">选择物料:</label>
                                            <select class="ui-input" 
                                            name="supplies.id" id="suppliesId">
                                                <option value="1" selected="selected"></option>
                                                <#list supplies as s>
                                                    <option value="${s.id}">${s.name}</option>
                                                </#list>
                                            </select>&nbsp;&nbsp;&nbsp;&nbsp;
                                            	<a  href="javascript:;" onclick="supEnter('${rc.contextPath}',${city.mediaType})">上传物料</a>
											<p class="ui-term-placeholder"></p>
											
										</div>
										
										<div class="ui-form-item" id="orderRemark">
											<label class="ui-label mt10">备注信息:</label>
                                            <textarea rows="4" cols="40" style="resize: none;" name="ordRemark"></textarea>
											<p class="ui-term-placeholder"></p>
											
										</div>
										
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" onclick="sub()" class="block-btn"
										id="subWithdraw" value="确认提交" >
									</div>
								</div>

							</form>
</@frame.html>
