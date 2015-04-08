<#import "template/template.ftl" as frame>
<#global menu="合同列表">
<@frame.html title="合同列表">

<script type="text/javascript" >
	function pages(pageNum) {
		var pattern = /^\d+$/;
		var by = ($("#by").val());
		var code = ($("#code").val());
		var name = ($("#name").val());
		var g2 = ($("#textpage").val());
		if (g2 == undefined) {
			g2 = 1;
		}
		if (!isNaN($("#textpage").val())) {
		} else {
			jDialog.Alert("请输入数字");
			return;
		}
		if (parseInt($("#textpage").val()) <= 0) {
			jDialog.Alert("请输入正整数");
			return;
		}
		if ($("#textpage").val() > pageNum) {
			jDialog.Alert("输入的页数超过最大页数");
			return;
		}
		//  var adsts =$("#adsts").val();
		window.location.href = "${rc.contextPath}/contract/list/"+g2+ "?code=" + code + "&name="+ name;
	}

	function page(num) {
		var code = $("#code").val();
		var name = $("#name").val();
		var by = ($("#by").val());
		window.location.href = "${rc.contextPath}/contract/list/"+num+"?code="+ code + "&name=" + name;
	}
	
	
	function sub_code(){
		var code = $("#code").val();
		window.location.href= "${rc.contextPath}/contract/list/1"+"?code="+code
	}
	
	function sub_name(){
		var name = $("#name").val();
		window.location.href= "${rc.contextPath}/contract/list/1"+"?name="+name
		
	}
</script>

						<form id="base_form" action="" method="post"
							dataType="html" enctype="multipart/form-data" class="Page-Form"	
						>
							<div class="module s-clear u-lump-sum p19">
								<div class="u-sum-right">
									<input class="ui-input" type="text" name="code"
										id="code" data-is="isAmount isEnough" 
										autocomplete="off" placeholder="合同号" value="${code!''}"
									/>
									<input type="button" class="block-btn" 
										value="查询" onclick="sub_code();">
									&nbsp;&nbsp;&nbsp; 
									
									<input class="ui-input" type="text"
										name="name" id="name" data-is="isAmount isEnough"
										autocomplete="off" placeholder="合同名称"  value="${name!''}"
									/>

									<input type="button"  class="block-btn" style="height: 30px; margin-top: 5px;"
										value="查询" onclick="sub_name();">
								</div>
							</div>
						</form>
						<form data-name="withdraw" name="userForm2" id="userForm2"
							class="ui-form" method="post" action="saveContract"
							enctype="multipart/form-data">
							<!--合同列表展示-->
							<div class="mt20">
								<div class="u-tab">
									<ul class="u-tab-items s-clear">
										<li class="u-tab-item u-tab-item-active"><a
											class="u-item-a" href="#holding">展示中 <em class="baget">0</em>
										</a></li>
										<li class="u-tab-item"><a class="u-item-a"
											href="#booking">预订中 <em class="baget">0</em>
										</a></li>
										<li class="u-tab-item"><a class="u-item-a"
											href="#exiting">已结束 <em class="baget">0</em>
										</a></li>

									</ul>
								</div>
								<div class="module p20" style="height: 423px;">
									<div class="tab-content">
											<div class="tab-plans-type">
												<ul class="tab-plans">
													<li class="tab-plan-item tab-plan-width"><span>全部</span>
													</li>
													<li class="tab-plan-item tab-plan-width"><span>已审核</span>
													</li>
													<li class="tab-plan-item tab-plan-width"><span>已提交</span>
													</li>
													<li class="tab-plan-item tab-plan-width"><span>已展示</span>
													</li>

												</ul>
											</div>
											<div class="uplan-table-box">
												<table width="100%" class="uplan-table">
													<tbody>
														<tr class="uplan-table-th">
															<td width="22%">
																<div class="th-head">合同号</div>
															</td>
															<td width="15%">
																<div class="th-md">合同名称</div>
															</td>
															<td width="16%">
																<div class="th-md">生效时间</div>
															</td>
															<td width="16%">
																<div class="th-tail">失效时间</div>
															</td>

														</tr>
													</tbody>
												</table>
												<#list list as item>
												<li class="ui-list-item dark">
													<div class="ui-list-item-row fn-clear">
														<span style="width: 235px; height: 35px; "
															class="ui-list-field text-center w80 fn-left" >
															${item.contractCode!''}
														</span>
														 <span style="width: 155px; height: 35px; "
															class="ui-list-field text-center w80 fn-left"><em
															class="value-small">
															<a
																href="${rc.contextPath}/contract/contractDetail?contract_id=${item.id!''}">
																${item.contractName!''} </a>
															</em> 
														 </span>
														 <span
															style="width: 170px; height: 35px; "
															class="ui-list-field text-center w80 fn-left">
														<#setting
															date_format="yyyy-MM-dd">${(item.startDate?date)!''}
														 </span> <span
															style="width: 170px; height: 35px; "
															class="ui-list-field num-s text-center w120 fn-left"><em
															class="value-small">
															<#setting
															date_format="yyyy-MM-dd">${(item.endDate?date)!''}
															</em>
														</span>
													</div>
												</li>
												</#list> 
												<!-- 分页 -->
												<table class="pag_tbl"
													style="width: 100%; border-width: 0px; margin-top: 10px;">
													<tr>
														<td style="width: 70%; text-align: right;">
															<div id="numpage" style="float: right;">
															${paginationHTML!''}	 
															</div>
														</td>
													</tr>
												</table>
										</div>
									</div>
								</div>
							</div>
						</form>
</@frame.html>