<#if activitis?exists>
<DIV class="p20bs mt10 color-white-bg border-ec">
	<H3 class="text-xl title-box">
		<p style="text-align: left">
			<A class="black" href="#">历史办理信息</A>
		</p>
	</H3>
	<br>
	<script type="text/javascript">
		var table;
		function initHisTable() {
			table = $('#his_table')
					.dataTable(
							{
								"dom" : 't',
								"searching" : false,
								"ordering" : false,
								"serverSide" : false,
								"iDisplayLength" : 100,
								"language" : {
									"url" : "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
								}
							});
		}

		$(document).ready(function() {
			initHisTable();
		});
	</script>
	<style type="text/css">
#his_table {
	font-size: 14px;
}
</style>
	<div class="uplan-table-box">
		<table id="his_table" class="display" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th width="30px"></th>
					<th width="15%">操作类型</th>
					<th width="12%">人员</th>
					<th width="15%">签收时间</th>
					<th width="15%">办理时间</th>
					<th width="12%">操作意见</th>
					<th>操作备注</th>
				</tr>
			</thead>
			<tbody>
				<#list activitis as act> <#if act.assignee??>
				<tr>
					<#if (act_index)=0>
					<td class="status status-first">&nbsp;</td> <#elseif !act_has_next>
					<td class="status status-check">&nbsp;</td> <#else>
					<td class="status status">&nbsp;</td> </#if>
					<td>${act.name}</td>
					<td>${act.assignee!''}</td>
					<td>${(act.claimTime?string("yyyy-MM-dd HH:mm"))!''}</td>
					<td>${(act.endTime?string("yyyy-MM-dd HH:mm"))!''}</td>
					<td style="text-align: left;">${act.result!''}</td><!-- ${act.comment!''} -->
					<td style="text-align: left;">
					<#if (act.comment!'')?length lt 20 >
						${act.comment!''}&nbsp;
					<#else>
						<a tip="${act.comment!''}" href="javascript:void(0);">${act.comment[0..24]?default("")}...</a>
					</#if>
					</td>
				</tr>
				</#if> </#list>
			</tbody>
		</table>
	</div>
</div>
</#if>
