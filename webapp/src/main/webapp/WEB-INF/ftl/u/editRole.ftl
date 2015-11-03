<#import "../template/template.ftl" as frame> <#global menu="角色管理">
<@frame.html title="编辑角色" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/progressbar.js"] css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
	$(document).ready(function() {
	$("#checkAll").click(function(){
         if($(this).attr("checked")=="checked"){
            $("input[name='checkone']:checkbox").attr("checked",true);
         }else{
            $("input[name='checkone']:checkbox").attr("checked",false);
         }
       });
					});

	function sub(){
		if (!$("#userForm2").validationEngine('validateBeforeSubmit'))
            return;
		 var rolename=$("#name").val();
		 var funcode=$("#funcode").val();
		 var fundesc=$("#fundesc").val();
        	var o = document.getElementsByName("checkone");
        	var dIds='';
        	for(var i=0;i<o.length;i++){
                if(o[i].checked)
                dIds+=o[i].value+',';   
           }
           if(dIds==""){
        	   layer.msg("请选择权限");
        	   return false;
           }
   		var param={"ids":dIds,"rolename":rolename,"funcode":funcode,"fundesc":fundesc};
		layer.confirm('确定修改吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		    	 $.ajax({
		    			url:"${rc.contextPath}/user/editRole/${actIdGroup.id!''}",
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:param,
		    			success:function(data){
		    				if (data.left == true) {
		    					layer.msg(data.right);
		    					var uptime = window.setTimeout(function(){
			window.location.href="${rc.contextPath}/user/role_list";
			   	clearTimeout(uptime);
						},2000)
		    					
		    				} else {
		    					layer.msg(data.right,{icon: 5});
		    				}
		    			}
		       });  
		       }
		});		
	}
function go_back(){
	history.go(-1);
}
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

<form data-name="withdraw" name="userForm2" id="userForm2"
	class="ui-form" method="post" action="update"
	enctype="multipart/form-data">
	<div class="withdraw-title fn-clear">
		<span>编辑角色</span>
		<a class="block-btn" style="margin-top: -5px;" href="javascript:void(0);" onclick="go_back()">返回</a>
	</div>
	<div class="withdrawInputs">
		<div class="inputs">

			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>角色名称:</label> <input 
					class="ui-input validate[required]" type="text"  id="name" value="${actIdGroup.name!''}"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="" value="">
					
			</div>
			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>角色简码:</label>
				<input class="ui-input validate[required]" type="text"  id="funcode" readonly="readonly" value="${showId!''}"
					data-is="isAmount isEnough" autocomplete="off"
					disableautocomplete="" value="" readonly>
			</div>
			<div class="ui-form-item">
				<label class="ui-label mt10"><span class="ui-form-required">*</span>权限:</label>
				<input type="checkbox"  id="checkAll"/>全选<br>
	<#if functions?exists> 
				<font color="red">  资源管理</font>&nbsp;<br>
				  <#list functions as item>
				   <#if item.funcode?index_of("res")!=-1>
					<input type="checkbox" value="${item.id}" name="checkone" 
				   <#if funcIDList?seq_contains(item.id)>checked </#if> />${item.name} <br>
				   </#if>
				 </#list>
				 <br>
				 <font color="red">  媒介管理 </font>&nbsp;<br>
				  <#list functions as item>
				   <#if item.funcode?index_of("meijie")!=-1>
					<input type="checkbox" value="${item.id}" name="checkone" 
				   <#if funcIDList?seq_contains(item.id)>checked </#if> />${item.name} <br>
				   </#if>
				 </#list>
				 <br>
				 
				  <font color="red"> 合同管理 </font>&nbsp;<br>
				  <#list functions as item>
				   <#if item.funcode?index_of("contract")!=-1>
					<input type="checkbox" value="${item.id}" name="checkone" 
				   <#if funcIDList?seq_contains(item.id)>checked </#if> />${item.name} <br>
				   </#if>
				 </#list>
				 
				 <br>
				 <font color="red">报表管理</font>&nbsp;<br>
				  <#list functions as item>
				   <#if item.funcode?index_of("report")!=-1>
					<input type="checkbox" value="${item.id}" name="checkone" 
				   <#if funcIDList?seq_contains(item.id)>checked </#if> />${item.name} <br>
				   </#if>
				 </#list>
				 
				  <br>
				   <#list functions as item>
				   <#if item.funcode?index_of("res")==-1 && item.funcode?index_of("meijie")==-1 && item.funcode?index_of("report")==-1 && item.funcode?index_of("contract")==-1>
					<input type="checkbox" value="${item.id}" name="checkone" 
				   <#if funcIDList?seq_contains(item.id)>checked </#if> />${item.name} <br>
				   </#if>
				 </#list>
				 
		 </#if>
			</div>
		</div>
		<div class="ui-form-item widthdrawBtBox">
			<input type="button" id="subWithdraw" class="block-btn"
				onclick="sub();" value="确认">
		</div>
		<div class="worm-tips">
			<div class="tips-title">
				<span class="icon"></span> 温馨提示
			</div>
			<ol>
				<li>1.请输入正确的邮箱地址及联系电话。</li>
				<li>2.如果您填写的信息不正确可能会导致后续操作失败。</li>
				<li>3.平台禁止洗钱、信用卡套现、虚假交易等行为，一经发现并确认，将终止该账户的使用。</li>
			</ol>
		</div>
</form>
</div>
</@frame.html>
