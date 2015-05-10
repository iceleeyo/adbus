<#import "template/template.ftl" as frame>

<@frame.html title="物料详细">
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
							
							<div class="mt20">
							
								<div class="module p20" style="height: 423px;">
								<button type="button" onclick="go_back()" class="block-btn" style="margin-bottom: 10px;">返回</button>
									<div class="tab-content">
										<div class="tab-content-box s-clear" id="holding"
											style="display: block;">
											<div class="uplan-table-box">
												<table width="100%" class="uplan-table">
													<tbody>
														<tr class="uplan-table-th">
															<td style="width: 225px; ">
																<div class="th-head">物料名称</div>
															</td>
															<td>
																<div class="th-tail">物料内容</div>
															</td>
														</tr>
													</tbody>
												</table>
												<#if view.files?has_content>
												<#list view.files as item>
												<li class="ui-list-item dark" >
													<div class="ui-list-item-row fn-clear">
														<span style=" height: 35px; width: 231px"
															class="ui-list-field text-center w80 fn-left" >
															 ${view.mainView.name!''}
														</span>
													
														<span style="height: 35px; width: 485px;text-align: right;" 
															class="ui-list-field text-center w80 fn-left" >
															 <a href="${rc.contextPath}/downloadFile/${item.userId!''}/${item.id!''}">${item.name!''}</a>
															<#if view.mainView.suppliesType == 1>
															&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;
															 <a href="${rc.contextPath}/upload_temp/${item.url!''}" target="_Blank">点击查看</a></#if>
														</span>
													</div>
												</li>
													</#list>
													<#else>
													     <li class="ui-list-item dark" >
													<div class="ui-list-item-row fn-clear">
														<span style=" height: 35px; width: 231px"
															class="ui-list-field text-center w80 fn-left" >
															 ${view.mainView.name!''}
														</span>
													
														<span style="height: 35px; width: 485px" 
															class="ui-list-field text-center w80 fn-left">
															 ${(view.mainView.infoContext)!''}
														</span>
													</div>
												</li>
													</#if>
													
											</div>
										</div>
									</div>
								</div>
							</div>
						</form>
</div>
</@frame.html>