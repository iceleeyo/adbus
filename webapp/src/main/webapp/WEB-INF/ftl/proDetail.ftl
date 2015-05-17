<#import "template/template.ftl" as frame>

<@frame.html title="套餐详细">

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							  <DIV class="color-white-bg border-ec">
                <H3 class="text-xl title-box"><A class="black" href="#">套餐详情</A></H3>
               <DIV class="summary mt10 uplan-summary-div">
              <UL class="uplan-detail-ul">
  <LI style="width: 720px;"><SPAN>套餐名称：</SPAN><SPAN class="con">${prod.name!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>时长（秒）：</SPAN><SPAN class="con">${prod.duration!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>单日播放次数：</SPAN><SPAN class="con">${prod.playNumber!''}</SPAN></LI>
 <#if (!prod?? || prod.type == 'video')>
  <LI style="width: 240px;"><SPAN>首播次数：</SPAN><SPAN class="con">${prod.firstNumber!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>末播次数：</SPAN><SPAN class="con">${prod.lastNumber!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>高峰时段占比：</SPAN><SPAN class="con">${prod.hotRatio!''}</SPAN></LI>
 </#if>
  <LI style="width: 200px;"><SPAN>套餐播放天数：</SPAN><SPAN class="con">${prod.days!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>套餐价格：</SPAN><SPAN class="con" style="color: rgb(245, 135, 8);">${prod.price!''}</SPAN></LI>
  <LI style="width: 240px;"><SPAN>媒体类型：</SPAN><SPAN class="con">
        <#if (!prod?? || prod.type == 'video')>视频</#if>
      <#if (!prod?? || prod.type == 'image')>图片</#if>
      <#if (!prod?? || prod.type == 'info')>文本</#if>
  </SPAN></LI>
</UL>
</DIV>
</DIV>
						</form>
</div>
</@frame.html>