<#import "../template/template.ftl" as frame> <#global menu="制作二维码 ">
<@frame.html title="制作二维码 " js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>


<script type="text/javascript">

    $(document).ready(function() {
        $("#userForm2").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
        });
        
        
        
    });
</script>

<script type="text/javascript">

	function sub(){
	if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
			return;
		$('#userForm2').ajaxForm(function(data) {
				if(typeof(data)!='undefined' &&　data.img!='' && data.error==1){
					$("#img").attr("src",data.img);
						layer.msg("增加成功");
				}else {
					if(data.error==0){
						layer.msg("增加失败,系统已存在相同的链接.", {icon: 5});
					}else {
						layer.msg("操作失败");
					}
				}
				
		}).submit();
	}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="http://60.205.168.48:9009/url/save"
		enctype="multipart/form-data">
		<div class="withdrawInputs">
			<div class="inputs">
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>请输入url:</label>
					<input
						class="ui-input  validate[required]" style="width:440px;"
						type="text" name="sourceUrl_s" id="sourceUrl_s" value=""
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">描述:</label>
					<input
						class="ui-input " style="width:440px;"
						type="text" name="description_s" id="description_s" value=""
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" placeholder="">
				</div>
				
            <div >
                     <img src="" id="img" width="300" height="300" style="margin-left:200px" border="1px solid #d0d0d0;"/>
			</div>
			<div class="widthdrawBtBox">
				<input type="button" id="subWithdraw" class="block-btn"
					onclick="sub();" value="提交">
			</div>
		</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>1.请输入正确的url。</li>
			</ol>
		</div>
	
</div>
</@frame.html>
