<#import "template/template.ftl" as frame>
<#import "macro/materialPreview.ftl" as preview>

<@frame.html title="物料详细" js=["js/highslide/highslide-full.js", "js/video-js/video.js", "js/video-js/lang/zh-CN.js"]
css=["js/highslide/highslide.css", "js/video-js/video-js.css"]>
    <#include "template/preview.ftl" />

<script type="text/javascript">
    function go_back(){
        history.go(-1);
    }
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							<!--合同详情展示-->
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
                                                            <td style="width: 240px; text-align: center; vertical-align: middle">
                                                                <div class="th-head">查看</div>
                                                            </td>
                                                            <td style="width: 300px; text-align: center; vertical-align: middle">
																<div class="th-md">物料名称</div>
															</td>
															<td style="text-align: center; vertical-align: middle">
																<div class="th-tail">物料内容</div>
															</td>
														</tr>

<#if view.files?has_content>
    <#list view.files as item>
                                                        <tr>
                                                            <td style="width: 240px;">
                                                                <@preview.materialPreview view=view items=[item]/>
                                                            </td>
                                                            <td>
                                                                ${view.mainView.name!''}

                                                            </td>
                                                            <td>
                                                                ${item.name!''}

                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="3"><li class="ui-list-item"></li></td>
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
                                                        <tr>
                                                            <td colspan="3"><li class="ui-list-item"></li></td>
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