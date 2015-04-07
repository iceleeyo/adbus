<#import "template/template.ftl" as frame>

<@frame.html title="合同详细">

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
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
														<tr class="uplan-table-th" style="width: 651px; ">
															<td style="width: 221px;" align="center">
																<div class="th-head">合同号</div>
															</td>
															<td align="center">
																<div class="th-tail">附件</div>
															</td>
														</tr>
													</tbody>
												</table>
												<li class="ui-list-item dark" >
													<div class="ui-list-item-row fn-clear" style="width: 636px; ">
														<span style="width: 244px; height: 35px; "
															class="ui-list-field text-center w80 fn-left" >
															${view.mainView.contractCode!''}
														</span>
													
														<span style="width: 380px; height: 35px; " 
															class="ui-list-field text-center w80 fn-left">
															<#list view.files as item> ${item.name!''} 
															</#list>
														</span>
													</div>
												</li>
												
													
												
											</div>
										</div>
									</div>
								</div>
							</div>
						</form>
</@frame.html>