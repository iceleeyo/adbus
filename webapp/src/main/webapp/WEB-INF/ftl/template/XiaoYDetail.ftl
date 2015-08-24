<#import "template_blank.ftl" as frame>
<@frame.html title="小样详细" js=[ "js/highslide/highslide-full.js","js/video-js/lang/zh-CN.js"]
css=["js/highslide/highslide.css","css/lrtk.css", "js/video-js/video-js.css"]>
    <#include "preview.ftl" />
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN"> 
<script type="text/javascript">
    function go_back(){
        history.go(-1);
    }
</script>

<div class="withdrawS-wrap color-white-bg fn-clear_blank" style="width: 100%;">
						<form >
							<div class="mt20">
									<div class="tab-content">
										<div class="tab-content-box s-clear" id="holding"
											style="display: block;">
											<div class="uplan-table-box">
												<table width="100%" class="uplan-table">
													<tbody>
														<tr class="uplan-table-th">
                                                            <td style="width: 15%;text-align: center; vertical-align: middle">
                                                                <div class="th-md">预览</div>
                                                            </td>
                                                            <td style="text-align: center; vertical-align: middle">
																<div class="th-md">类型</div>
															</td>
															<td  style="width: 15%;text-align: center; vertical-align: middle">
																<div class="th-md">文件名称</div>
															</td>
															<td style="text-align: center; vertical-align: middle">
																<div class="th-md">操作</div>
															</td>
														</tr>
<#if attachments?has_content>
                                     <#list attachments as item>
                                                        <tr>
                                                            <td>
                                                            <a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
	                                                              onclick="return hs.expand(this)">
	                                                    <img src="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}"
	                                                            class="m11" width="240"/>
	                                                           </a>
                                                            </td>
                                                            <td>
                                                            <#if item.type==12>
                                                                                                                                                                                         车身小样
                                                                <#else>
                                                                                                                                                                                          施工照片
                                                                </#if>                                                                                                                         
                                                            </td>
                                                            <td>
                                                                ${item.name!''}
                                                            </td>
                                                            <td>
                                                            	<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">下载</a>
                                                             </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="5"><li class="ui-list-item"></li></td>
                                                        </tr>
                                          </#list>
                                          <#else>
                                           <tr>
                                                            <td colspan="5" align="center">未上传照片</td>
                                                        </tr>
                                        
</#if>
													</tbody>
												</table>
											</div>
										</div>
									</div>
							</div>
						</form> 
						<br>
</div>
</@frame.html>