<DIV class="p20bs mt20 color-white-bg fn-clear">
<H3 class="text-xl"><A class="black" href="#">历史办理信息</A></H3>	
				               <#if activitis?exists>
				               <div class="uplan-table-box">
											<table width="100%" class="uplan-table">
												<tr class="uplan-table-th">
												<td style="width: 130px;">
														<div class="th-head">签收时间 </div>
													</td>
													<td style="width: 130px;">
														<div class="th-md">办理时间 </div>
													</td>
													<td style="width: 112px;">
														<div class="th-md">操作人</div>
													</td>
													<td style="width: 111px;">
														<div class="th-md">操作类型</div>
													</td>
													
													<!--<td style="width: 111px;">
														<div class="th-md">是否通过</div>
													</td>-->
													<td style="width: 333px;">
														<div class="th-tail">备注</div>
													</td>
												</tr>
											</table>
				               	<#list activitis as act>
				               	<li class="ui-list-item dark">
												<div class="ui-list-item-row fn-clear">
												<span style="width: 130px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														 <#setting
															date_format="yyyy-MM-dd HH:MM">
														${(act.startTime?date)!''} </span> 
													<span style="width: 130px; height: 35px;"
														class="ui-list-field text-center w80 fn-left">
														 <#setting
															date_format="yyyy-MM-dd HH:MM">
														${(act.endTime?date)!''} </span> 
													<span
														style="width: 118px; height: 35px;"
														class="ui-list-field text-center w80 fn-left"><em
														class="value-small">${act.assignee!''} 
													</em> </span>
													 
													<span
														style="width: 129px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small">
															${act.name}</em> </span>
														<!--	<span
														style="width: 129px; height: 35px;"
														class="ui-list-field num-s text-center w120 fn-left"><em
														class="value-small">
															${act.result?string('yes', 'no')}</em> </span>-->
													 <span
														style="width: 240px; height: 35px;"
														class="ui-list-field text-center w80 fn-left last"><em
														class="value-small">
														 ${act.comment!''}
														</em>
														</span>
													</div>		 
											</li>
								</#list>
								</#if>
								</div>
								</div>