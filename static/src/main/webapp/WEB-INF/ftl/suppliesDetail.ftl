<#import "template/template_blank.ftl" as frame> <@frame.html title="物料详细"
js=["js/highslide/highslide-full.js", "js/video-js/video.js",
"js/video-js/lang/zh-CN.js"] css=["js/highslide/highslide.css",
"js/video-js/video-js.css"]> <#include "template/preview.ftl" />
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN">
<script type="text/javascript">
    function go_back(){
        history.go(-1);
    }
</script>

<div class="withdrawB-wrap color-white-bg fn-clear_blank"
	style="width: 95%;">
	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="saveContract"
		enctype="multipart/form-data">


		<div class="mt20">

			<div class="tab-content">
				<div class="tab-content-box s-clear" id="holding"
					style="display: block;">
					<div class="uplan-table-box">
						<#if view.mainView.suppliesType?? &&
						view.mainView.suppliesType!=2>
						<div class="withdraw-title">
					 	<span style="font-size:18px;">目前支持mpg视频格式上传预览</span>  
						</div>
						<table width="100%" class="uplan-table">
							<tbody>
								<tr class="uplan-table-th">
									<td
										style="width: 45%; text-align: center; vertical-align: middle">
										<div class="th-md">预览</div>
									</td>
									<td style="text-align: center; vertical-align: middle">
										<div class="th-md">物料名称</div>
									</td>
									<td style="width: 10%;text-align: center; vertical-align: middle">
										<div class="th-md">类型</div>
									</td>
									<td
										style="width: 15%; text-align: center; vertical-align: middle">
										<div class="th-md">文件名称</div>
									</td>
									<td style="width: 7%; text-align: center; vertical-align: middle">
										<div class="th-md">操作</div>
									</td>
								</tr>

								<#list view.files as item>
								<tr>
									<td>
									<#if  item.url?contains("mpg") || item.url?contains("mp4")>
									<video width="450" height="330"  autoplay="autoplay" controls="controls" >
  <source src="/upload_temp/${(  ( item.url)?replace('mpg','mp4')  )!''}"   type="video/mp4" />
</video>
<#else>
   <img src="/upload_temp/${(  ( item.url)?replace('mpg','mp4')  )!''}"  width="450" height="330"/>
</#if>
<#--
<object classid="clsid:05589FA1-C356-11CE-BF01-00AA0055595A" id="ActiveMovie1" width="239" height="250">
<param name="Appearance" value="0">
<param name="AutoStart" value="-1">
<param name="AllowChangeDisplayMode" value="-1">
<param name="AllowHideDisplay" value="0">
<param name="AllowHideControls" value="-1">
<param name="AutoRewind" value="-1">
<param name="Balance" value="0">
<param name="CurrentPosition" value="0">
<param name="DisplayBackColor" value="0">
<param name="DisplayForeColor" value="16777215">
<param name="DisplayMode" value="0">
<param name="Enabled" value="-1">
<param name="EnableContextMenu" value="-1">
<param name="EnablePositionControls" value="-1">
<param name="EnableSelectionControls" value="0">
<param name="EnableTracker" value="-1">
<param name="Filename" value="/upload_temp/${item.url!''}" valuetype="ref">
<param name="FullScreenMode" value="0">
<param name="MovieWindowSize" value="0">
<param name="PlayCount" value="1">
<param name="Rate" value="1">
<param name="SelectionStart" value="-1">
<param name="SelectionEnd" value="-1">
<param name="ShowControls" value="-1">
<param name="ShowDisplay" value="-1">
<param name="ShowPositionControls" value="0">
<param name="ShowTracker" value="-1">
<param name="Volume" value="-480">
</object>
-->
									</td>
									<td>${view.mainView.name!''}</td>
									<td><#if item.type?? && item.type==4> 物料文件 <#elseif
										item.type?? && item.type==3> 资质文件 </#if></td>
									<td><#if item.type?? && item.type==4> ${item.name!''}
										<#else> ${item.name!''} </#if></td>
									<td><a
										href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">下载</a>
									</td>
								</tr>
								<tr>
									<td colspan="5"><li class="ui-list-item"></li></td>
								</tr>
								</#list>

							</tbody>
						</table>
						<#elseif view.mainView.suppliesType?? &&
						view.mainView.suppliesType==2>
						<table width="100%" class="uplan-table">
							<tbody>
								<tr class="uplan-table-th">
									<td style="text-align: center; vertical-align: middle"
										colspan="3">
										<div class="th-md">物料名称</div>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="height: 50px;">
										${view.mainView.name!''}</td>
								</tr>

								<tr class="uplan-table-th">
									<td style="text-align: center; vertical-align: middle"
										colspan="3">
										<div class="th-md">字幕内容</div>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="height: 50px;">
										${view.mainView.infoContext!''}</td>
								</tr>
								<tr class="uplan-table-th">
									<td style="text-align: center; vertical-align: middle">
										<div class="th-md">资质附件</div>
									</td>
									<td style="text-align: center; vertical-align: middle">
										<div class="th-md">文件名称</div>
									</td>
									<td style="text-align: center; vertical-align: middle">
										<div class="th-md">操作</div>
									</td>
								</tr>
								<#list view.files as item>
								<tr>
									<td><@preview.materialPreview view=view items=[item]/></td>

									<td><#if item.type?? && item.type==4> ${item.name!''}
										<#else> ${item.name!''} </#if></td>

									<td><a
										href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">下载</a>
									</td>
								</tr>

								<tr>
									<td colspan="3"><li class="ui-list-item"></li></td>
								</tr>
								</#list>

							</tbody>
						</table>

						<#elseif !(view.mainView.suppliesType??)> 请上传素材 </#if>
					</div>
				</div>
			</div>
		</div>
	</form>
	<br>

</div>
</@frame.html>
