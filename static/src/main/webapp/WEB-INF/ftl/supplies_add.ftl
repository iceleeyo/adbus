<#import "template/template.ftl" as frame> <#global menu="物料管理">
<#import "template/pickBuses.ftl" as pickBuses> <@frame.html
title="物料上传" js=["js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
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
	i = 2;
	j = 2;
	$(document).ready(function() {
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
            if(suppliesType=="0" || suppliesType=="1" ){
                $("#text").hide();
                $("#file").show();
            }
            if(suppliesType=="2"){
                $("#text").show();
                $("#file").hide();
            }
            if (suppliesType == "0") {
                $(".videoToggle").show();
            } else {
                $(".videoToggle").hide();
            }
            
            
     });
    
	
    <@security.authorize ifAnyGranted="ShibaOrderManager">    
    //author:pxh 2015-05-20 22:36
	$( "#userId" ).autocomplete({
		source: "${rc.contextPath}/user/autoComplete",
		change: function( event, ui ) { 
		/*if(ui.item!=null){alert(ui.item.value);}*/
		//table.fnDraw();
		},
		select: function(event,ui) {
			$('#userId').val(ui.item.value);
		  	//table.fnDraw();
		}
	});
	 </@security.authorize>
        
    });

	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
				document.getElementById("div_" + o));
	}
	function del_3(o) {
		document.getElementById("newUpload3").removeChild(
				document.getElementById("quadiv_" + o));
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
				jDialog.Alert("文件类型只支持AVI,MP4,RMVB,MPEG2,MPG");
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
			suppotFile[3] = "pdf";
				suppotFile[4] = "png";
			var flag=false;
			for (var i = 0; i < suppotFile.length; i++) {
				if (suppotFile[i] == fileType) {
					flag=true;
				}
			}
			if(flag == false)
			{
				jDialog.Alert("资质类型只支持GIF,BMP,JPG,PNG,PDF");
				return;
			}
		}
		var bb=false;
		var userid=$("#userId").val();
		if(typeof(userid)=="undefined"){
		   bb=true;
		}else{
		$.ajax({
			url:"${rc.contextPath}/user/isAdvertiser/"+userid,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left == true) {
				   bb=true;
				} else {
					bb=false;
					jDialog.Alert(data.right);
				}
			}
      }); 
      }
      if(bb==true) {
		$('#userForm2').ajaxForm(function(data) {
			jDialog.Alert(data.right);
			   var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/supplies/list"
			   	clearTimeout(uptime);
						},2000)
		}).submit();
		document.getElementById('subWithdraw').setAttribute('disabled',true);
		  $("#subWithdraw").css("background-color","#85A2AD");
		    var uploadProcess={upath:'${rc.contextPath}/upload/process'};
		     var uptime = window.setTimeout(function(){
				 $('#progress1').anim_progressbar(uploadProcess);
			   	clearTimeout(uptime);
						},2000)
		 
		  }
	}

	
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
	<form id="userForm2" name="userForm2"
		action="put?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
		enctype="multipart/form-data" method="post"">
		<div class="withdraw-title fn-clear">
			<span>上传广告及相关资质</span> <a class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);" onclick="go_back()">返回</a>
			<!--
            <ul class="fn-clear">
              <li class="first"><a class="addBank fn-right" href="/account/userBank!toAdd.action">xxxx</a></li>
              <li><a class="mgmtBank fn-right" id="mgmtBank" href="/account/info!bank.action">xxxx</a></li>
            </ul>
            -->
		</div>
		<div class="withdrawInputs">
			<div class="inputs">

				<br> <@security.authorize ifAnyGranted="ShibaOrderManager">
				<div class="ui-form-item">
					<label class="ui-label mt10"> <span
						class="ui-form-required">* </span>广告主
					</label> <span> <input id="userId" name="userId"
						class="ui-input validate[required,custom[noSpecialLetterChinese]]"
						placeholder="请选择广告主">
					</span>
				</div>
				</@security.authorize>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>广告名称</label>
					<input
						class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[1],maxSize[120]]"
						type="text" name="name" id="name" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete=""
						placeholder="支持中英文、数字、下划线">
				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>物料类型</label>
					<select class="ui-input" name="suppliesType" id="suppliesType">
						<#if city.mediaType == 'body'>
						<option value="4" selected="selected">车身</option> <#else>

						<option value="0" selected="selected">硬广广告</option>
						<option value="1">INFO图片</option>
						<option value="2">INFO字幕</option>
						<option value="3">二类节目</option> </#if>
					</select>
				</div>
				<div class="ui-form-item videoToggle" style="display:none;">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>广告/节目长度</label>
					<input class="ui-input validate[required,integer,min[5],max[180]]"
						onkeyup="value=value.replace(/[^\d]/g,'')"
						value="<#if prod??>${prod.duration!''}<#else>0</#if>"
						name="duration" id="duration" data-is="isAmount isEnough"
						autocomplete="off" disableautocomplete="" placeholder="5-180秒">
					<p class="ui-term-placeholder"></p>

				</div>

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>所属行业</label>

					<select id="industryId" class="ui-input" name="industryId"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete=""> <#list industries as industry>
						<#if industry.id!=14>
						<option value="${industry.id}">${industry.name}</option> </#if>
						</#list>
					</select>

				</div>
				
				<div class="ui-form-item" id="text" style="display: none;">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>文本信息</label>

					<textarea rows="5" cols="50" id="infoContext"
						data-is="isAmount isEnough" style="resize: none;"
						name="infoContext"></textarea>
				</div>
				<div class="ui-form-item" id="file">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>物料文件</label>
					<div id="newUpload2">
						<div class="filebox" id="div_1">
							<input type="file" name="file" id="Sfile"
								class="validate[required]">
						</div>
					</div>
					<input class="btn-sm btn-success" type="button" id="btn_add2"
						value="增加一行" style="margin-top: 10px;"><br>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">资质文件</label>
					<div id="newUpload3">
						<div id="quadiv_1">
							<input type="file" name="qua" id="Sfile1"
								class="">
						</div>
					</div>
					
					<input class="btn-sm btn-success" type="button" id="btn_add3"
						value="增加一行" style="margin-top: 10px;"><br><span><br>备注：如果有代理公司，需要同时上传代理公司资质</span>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">备注信息</label>
					<textarea rows="4" cols="40" style="resize: none;" name="operFristcomment"></textarea>
				</div>
				<div class="widthdrawBtBox">
					<input type="button" id="subWithdraw" class="block-btn"
						onclick="sub2();" value="开始上传">
					<input type="button" id="subWithdraw" class="block-btn"
						onclick="showdoc();" value="查看物料规格说明">
				</div>

				<div id="progress1">
					<div class="percent"></div>
					<div class="pbar"></div>
					<div class="elapsed"></div>
				</div>
			</div>
		</div>
		<div id="otherType" style="display: none">
			<div class="worm-tips">
				<div class="tips-title">
					<span class="icon"></span> 温馨提示
				</div>
				<ol>
					<li>1.请提供符合产品要求的物料类型，视频类型格式支持<font color="red">AVI，MP4，RMVB，MPEG-2，MPG</font>。
					</li>
					<li>2.图片类型格式支持<font color="red">GIF，PNG，JPG</font>；资质类型格式支持<font
						color="red">GIF，PNG，JPG，PDF</font>。
					</li>
					<li>3.如果物料是INFO图片，除原有格式外必须上传一个PNG格式的。</li>
					<li>4.在必须要的时候，请上传物料说明和广告资质。</li>
					<li>5.如果物料的文件比较大，可能需要一定的时间，请耐心等待。文件大小尽量控制在200M以内。</li>
					<li>6.请勿上传违反国家广告法及相关法律法规的物料文件。</li>
					<li>7.视频节目物料素材应符合合同附件《节目制作规范》的相关要求。</li>
					<li>8.广告物料及节目播出带（三期备播节目物料）须在广告/节目首个发布日的7个工作日前将送达乙方指定地址。如因延误或错误送达导致广告/节目延误发布的，责任由甲方承担。</li>

				</ol>
			</div>
		</div>

		<div id="videoType" style="display: none">
			<div class="worm-tips">
				<div class="tips-title">
					<span class="icon"></span> 全屏视频广告规格要求说明
				</div>
				<ol>
					<li>一、广告素材基础:</li>
					<li>1.广告时长为：5秒，10秒，15秒，30秒。</li>
					<li>2.广告发布素材为模拟，需要准备DVC
						pro带，需把硬广吐到带子中，同时需提供电子版的视频文件（要求：720*576像素，PAL制，<font color="red">MPEG-2</font>文件格式
						帧速率为：<font color="red">24</font>帧每秒。
					</li>
					<li>3.以上物料素材需在广告首个发布日的7个工作日前送达。</li>
					<li>4.硬广前后需各加<font color="red">1-3秒</font>静帧
					</li>
					<li>5.硬广前一秒静帧前必须加上北广的5秒倒计时</li>
					<li>6.<font color="red">声音要求在-12至18db</font></li>
					<li>7.<font color="red">需记录起始码</font></li>

					<li>二、广告素材内容、技术禁止和限制:</li>
					<li>1.杂帧、跳帧、加帧。</li>
					<li>2.非创意黑场画面。</li>
					<li>3.模糊。</li>
					<li>4.画面出框。</li>
					<li>5.声画不同步。</li>
					<li>6.吞音、破音。</li>
					<li>7.三秒以上静帧无伴音。</li>
					<li>8.字幕有错或误导含义。</li>
				</ol>
			</div>
		</div>
	</form>
</div>
</@frame.html>
