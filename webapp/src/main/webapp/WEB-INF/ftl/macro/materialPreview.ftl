<#macro materialPreview view items=[]> <#if (items?? && items?size>0)>
<#list items as theItem>
<!-- 如果是合同 -->
<#if (view.mainView.contractName)??>
<a
	href="${rc.contextPath}/upload_temp/${theItem.url!''}"
	onclick="return hs.expand(this)"> </a>
<#else>
<!-- 如果是订单 -->
<#if view.mainView.suppliesType == 1 || view.mainView.suppliesType == 2
|| view.mainView.suppliesType == 3 >
<a
	href="${rc.contextPath}/upload_temp/${theItem.url!''}"
	onclick="return hs.expand(this)"> <img
	src="${rc.contextPath}/upload_temp/${theItem.url!''}"
	class="m11" width="240" />
</a>
<#elseif view.mainView.suppliesType == 0> <#if theItem.type == 3>
<!--资质判断 -->
<a
	href="${rc.contextPath}/upload_temp/${theItem.url!''}"
	onclick="return hs.expand(this)"> <img
	src="${rc.contextPath}/upload_temp/${theItem.url!''}"
	class="m11" width="240" />
</a>
</#if> <#if theItem.type != 3>
<div class="m11">
	<video id="material-video" class="video-js vjs-default-skin" controls
		preload="auto" width="240"
		data-setup='{
		                                        "controlBar": {
		                                            "muteToggle": false
		                                         },
		                                        "bigPlayButton" : true,
		                                        "preload" : "none"
		                                    }'>
		<source
			src="${rc.contextPath}/upload_temp/${theItem.url!''}"
			type='video/mp4'>
		<p class="vjs-no-js">
			请升级浏览器以<a href="http://videojs.com/html5-video-support/"
				target="_blank">支持此播放器</a>
		</p>
	</video>
</div>
</#if> <#else>
<img src="${rc.contextPath}/imgs/noimg.jpg" class="m11" width="140" />
</#if> </#if> </#list> <#else> <#list view.files as theItem> <#if
view.mainView.suppliesType == 1 || view.mainView.suppliesType == 3>
<a
	href="${rc.contextPath}/upload_temp/${theItem.url!''}"
	onclick="return hs.expand(this)"> <img
	src="${rc.contextPath}/upload_temp/${theItem.url!''}"
	class="m11" width="240" />
</a>
<#elseif view.mainView.suppliesType == 0>
<div class="m11">
	<video id="material-video" class="video-js vjs-default-skin" controls
		preload="auto" width="240"
		data-setup='{
                                        "controlBar": {
                                            "muteToggle": false
                                         }
                                        "bigPlayButton" : true,
                                        "preload" : "none"
                                    }'>
		<source
			src="${rc.contextPath}/downloadFile/${theItem.userId!''}/${theItem.id!''}"
			type='video/mp4'>
		<p class="vjs-no-js">
			请升级浏览器以<a href="http://videojs.com/html5-video-support/"
				target="_blank">支持此播放器</a>
		</p>
	</video>
</div>
<#else>
<img src="${rc.contextPath}/imgs/noimg.jpg" class="m11" width="140" />
</#if> </#list> </#if> </#macro>
