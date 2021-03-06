<#import "../template/template.ftl" as frame> <#global menu="添加角色">
<@frame.html title="添加角色" js=["js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

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
		layer.confirm('确定创建该角色吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		    	 $.ajax({
		    			url:"${rc.contextPath}/user/saveRole",
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
			<span>添加角色</span> <a class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);" onclick="go_back()">返回</a>
		</div>
		<div class="withdrawInputs">
			<div class="inputs">

				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>角色名称:</label>
					<input class="ui-input validate[required]" type="text" id="name"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="">
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10"><span class="ui-form-required">*</span>角色简码:</label>
					<input class="ui-input validate[required]" type="text" id="funcode"
						data-is="isAmount isEnough" autocomplete="off"
						disableautocomplete="" value="">
				</div>
				<div class="ui-form-item">

					<label class="ui-label mt10"><span class="ui-form-required">*</span>权限:</label>
					<input type="checkbox" id="checkAll" />全选<br>
					<div class="recommand timer pd">
						<div class="re-box2 clearfix">
							<#if functions?exists>
							<div class="select-items clearfix">
								<font color="red"> 资源管理</font>&nbsp;<br> <#list functions
								as item> <#if item.funcode?index_of("res")!=-1> <input
									type="checkbox" value="${item.id}" name="checkone" />${item.name}
								<br> </#if> </#list> <br>
							</div>
							<div class="select-items clearfix">
								<font color="red"> 媒介管理 </font>&nbsp;<br> <#list functions
								as item> <#if item.funcode?index_of("meijie")!=-1> <input
									type="checkbox" value="${item.id}" name="checkone" />${item.name}
								<br> </#if> </#list> <br>
							</div>
							<div class="select-items clearfix">

								<font color="red"> 合同管理 </font>&nbsp;<br> <#list functions
								as item> <#if item.funcode?index_of("contract")!=-1> <input
									type="checkbox" value="${item.id}" name="checkone" />${item.name}
								<br> </#if> </#list> <br>
							</div>
							<div class="select-items clearfix">
								<font color="red">报表管理</font>&nbsp;<br> <#list functions as
								item> <#if item.funcode?index_of("report")!=-1> <input
									type="checkbox" value="${item.id}" name="checkone" />${item.name}
								<br> </#if> </#list> <br>
							</div>
							<div class="select-items clearfix">

								<font color="red">用户角色管理</font>&nbsp;<br> <#list functions
								as item> <#if item.funcode?index_of("sys_userList")!=-1 ||
								item.funcode?index_of("body_roleManager")!=-1> <input
									type="checkbox" value="${item.id}" name="checkone" />${item.name}
								<br> </#if> </#list> <br> <font color="red">检查部门</font>&nbsp;<br>
								<#list functions as item> <#if
								item.funcode?index_of("check_")!=-1 > <input type="checkbox"
									value="${item.id}" name="checkone" />${item.name} <br>
								</#if> </#list> <br>
							</div>
							<div class="select-items clearfix">

								<font color="red">车身套餐管理</font>&nbsp;<br> <#list functions
								as item> <#if item.funcode?index_of("sale_")!=-1 > <input
									type="checkbox" value="${item.id}" name="checkone" />${item.name}
								<br> </#if> </#list> <br> <font color="red">媒介推荐</font>&nbsp;<br>
								<#list functions as item> <#if
								item.funcode?index_of("landmatch")!=-1 ||
								item.funcode?index_of("trackmatch")!=-1 ||
								item.funcode?index_of("relatemedia")!=-1 > <input
									type="checkbox" value="${item.id}" name="checkone" />${item.name}
								<br> </#if> </#list>
							</div>
						</div>
					</div>




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
					<li>1.请为角色选择合理的权限。</li>
				</ol>
			</div>
	</form>
</div>
</@frame.html>
