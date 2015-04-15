<DIV class="p20bs mt10 color-white-bg border-ec">
<H3 class="text-xl title-box"><A class="black" href="#">历史办理信息</A></H3><br>	
<#if activitis?exists>
<div class="uplan-table-box">
											
	<#list activitis as act>
		<TABLE class="ui-table ui-table-gray">
  		<TBODY>
  			<TR class="dark">
    			<TH width="20%">操作类型</TH>
    				<TD width="30%" style="border-radius: 0 0 0">${act.name}</TD>
    				<TH width="20%" style="border-radius: 0 0 0">操作人</TH>
    				<TD width="30%">${act.assignee!''}</TD>
   			</TR>
  			<TR>
    			<TH>操作意见</TH>
    			<TD colspan=3>${act.comment!''}</TD></TR>
  			<TR >
    			<TH width="20%">签收时间</TH>
    			<TD style="border-radius: 0 0 0"><#setting date_format="yyyy-MM-dd HH:MM">${(act.startTime?date)!''}</TD>
    			<TH width="20%" style="border-radius: 0 0 0">操作时间</TH>
    			<TD ><#setting date_format="yyyy-MM-dd HH:MM">${(act.endTime?date)!''}</TD>
   			</TR>    	
 			</TBODY>
		</TABLE><br>
</#list>
</#if>
</div>
</div>