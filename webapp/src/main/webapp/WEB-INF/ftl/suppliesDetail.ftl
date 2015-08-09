<#import "template/template_blank.ftl" as frame>
<#import "macro/materialPreview.ftl" as preview>

<@frame.html title="物料详细" js=["js/highslide/highslide-full.js", "js/video-js/video.js", "js/video-js/lang/zh-CN.js"]
css=["js/highslide/highslide.css", "js/video-js/video-js.css"]>
    <#include "template/preview.ftl" />
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN"> 
<script type="text/javascript">
    function go_back(){
        history.go(-1);
    }
</script>

<div class="withdrawS-wrap color-white-bg fn-clear_blank" style="width: 100%;">
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							
							
							<div class="mt20">
							
									<div class="tab-content">
										<div class="tab-content-box s-clear" id="holding"
											style="display: block;">
											<div class="uplan-table-box">
<#if view.mainView.suppliesType!=2>											
												<table width="100%" class="uplan-table">
													<tbody>
														<tr class="uplan-table-th">
                                                            <td style="width: 15%;text-align: center; vertical-align: middle">
                                                                <div class="th-md">预览</div>
                                                            </td>
                                                            <td style="text-align: center; vertical-align: middle">
																<div class="th-md">物料名称</div>
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

    <#list view.files as item>
                                                        <tr>
                                                            <td>
                                                                <@preview.materialPreview view=view items=[item]/>
                                                            </td>
                                                            <td>
                                                                ${view.mainView.name!''}

                                                            </td>
                                                            <td>
                                                            <#if item.type?? && item.type==4>
                                                            物料文件
                                                               <#elseif item.type?? && item.type==3>
                                                                   资质文件
                                                               </#if>

                                                            </td>
                                                            <td>
                                                            <#if item.type?? && item.type==4>
                                                                ${item.name!''}
                                                               <#else>
                                                                  ${item.name!''}
                                                               </#if>

                                                            </td>
                                                            <td>
                                                            	<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">下载</a>
                                                             </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="5"><li class="ui-list-item"></li></td>
                                                        </tr>
    </#list>

													</tbody>
												</table>
<#elseif  view.mainView.suppliesType==2>
<table width="100%" class="uplan-table">
													<tbody>
														<tr class="uplan-table-th">
                                                            <td style="text-align: center; vertical-align: middle" colspan="3">
                                                                <div class="th-md">物料名称</div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="3" style="height: 50px;">
                                                                ${view.mainView.name!''}
                                                            </td>
                                                        </tr>
                                                        
                                                        <tr class="uplan-table-th">
                                                            <td style="text-align: center; vertical-align: middle" colspan="3">
                                                                <div class="th-md">字幕内容</div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="3" style="height: 50px;">
                                                                ${view.mainView.infoContext!''}
                                                            </td>
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
															<td>
																<@preview.materialPreview view=view items=[item]/>
															</td>
															
															<td>
																<#if item.type?? && item.type==4>
                                                                ${item.name!''}
                                                               <#else>
                                                                  ${item.name!''}
                                                               </#if>
															</td>
															
															<td>
																<a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">下载</a>
															</td>
														</tr>
														
														<tr>
                                                            <td colspan="3"><li class="ui-list-item"></li></td>
                                                        </tr>
</#list>												
														
													</tbody>
</table>
</#if>
											</div>
										</div>
									</div>
							</div>
						</form> 
						<br>
								
</div>
</@frame.html>