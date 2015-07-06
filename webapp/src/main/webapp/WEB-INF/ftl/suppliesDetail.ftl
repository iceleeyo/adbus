<#import "template/template_blank.ftl" as frame>
<#import "macro/materialPreview.ftl" as preview>

<@frame.html title="物料详细" js=["js/highslide/highslide-full.js", "js/video-js/video.js", "js/video-js/lang/zh-CN.js"]
css=["js/highslide/highslide.css", "js/video-js/video-js.css"]>
    <#include "template/preview.ftl" />

<script type="text/javascript">
    function go_back(){
        history.go(-1);
    }
</script>

<div class="withdraw-wrap color-white-bg fn-clear" style="position:absolute;left:250px;">
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							<H3 class="text-xl title-box"><A class="black" href="#">物料及资质</A>
						</H3>
							
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

<#if view.files?has_content>
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
                                                               <#else>
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
<#else>
                                                        <tr>
                                                            <td style="height: 35px;">
                                                                <@preview.materialPreview view=view/>
                                                            </td>
                                                            <td>
                                                                ${view.mainView.name!''}
                                                            </td>
                                                            <td>
                                                                ${(view.mainView.infoContext)!''}

                                                            </td>
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
						<div class="ui-form-item widthdrawBtBox" style="padding: 0 5px 20px 730px;">
						<button type="button" onclick="go_back()" class="block-btn" style="margin-bottom: 10px;">返回</button>
						</div>
								
</div>
</@frame.html>