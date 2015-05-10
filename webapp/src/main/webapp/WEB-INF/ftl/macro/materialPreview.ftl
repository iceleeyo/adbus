<#macro materialPreview view items=[]>
<#if view.mainView.suppliesType == 2>
<a href="#" onclick="return hs.htmlExpand(this)">${(view.mainView.infoContext)!''}</a>
<div class="highslide-maincontent" style="display:none;font-size: 24px;">
${(view.mainView.infoContext)!''}
</div>
<#else>
<#if (items?? && items?size>0)>
<#list items as theItem>
    <#if view.mainView.suppliesType == 1>
    <a href="${rc.contextPath}/downloadFile/${theItem.userId!''}/${theItem.id!''}"
       onclick="return hs.expand(this)">
        <img src="${rc.contextPath}/downloadFile/${theItem.userId!''}/${theItem.id!''}"
             class="m11" width="240"/>
    </a>
    <#elseif view.mainView.suppliesType == 0>
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
            <source src="${rc.contextPath}/downloadFile/${theItem.userId!''}/${theItem.id!''}" type='video/mp4'>
            <p class="vjs-no-js">
                请升级浏览器以<a href="http://videojs.com/html5-video-support/" target="_blank">支持此播放器</a>
            </p>
        </video>
    </div>
    <#else>
    <img src="${rc.contextPath}/imgs/noimg.jpg"
         class="m11" width="140"/>
    </#if>
</#list>
<#else>
    <#list view.files as theItem>
        <#if view.mainView.suppliesType == 1>
        <a href="${rc.contextPath}/downloadFile/${theItem.userId!''}/${theItem.id!''}"
           onclick="return hs.expand(this)">
            <img src="${rc.contextPath}/downloadFile/${theItem.userId!''}/${theItem.id!''}"
                 class="m11" width="240"/>
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
                <source src="${rc.contextPath}/downloadFile/${theItem.userId!''}/${theItem.id!''}" type='video/mp4'>
                <p class="vjs-no-js">
                    请升级浏览器以<a href="http://videojs.com/html5-video-support/" target="_blank">支持此播放器</a>
                </p>
            </video>
        </div>
        <#else>
        <img src="${rc.contextPath}/imgs/noimg.jpg"
             class="m11" width="140"/>
        </#if>
    </#list>
</#if>
</#if>
</#macro>