<DIV class="p20bs mt10 color-white-bg border-ec">
<H3 class="text-xl title-box"><A class="black" href="#">历史办理信息</A></H3><br>	
<#if activitis?exists>
    <script type="text/javascript">
        var table;
        function initHisTable () {
            table = $('#his_table').dataTable( {
                "dom": 't',
                "searching": false,
                "ordering": false,
                "serverSide": false,
                "language": {
                    "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
                }
            } );
        }

        $(document).ready(function() {
            initHisTable();
        } );
    </script>
    <style type="text/css">
        #his_table {font-size: 12px;}
    </style>
<div class="uplan-table-box">
    <table id="his_table" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
        	<th width="5%"></th>
            <th width="15%">操作类型</th>
            <th width="12%">人员</th>
            <th width="15%">签收时间</th>
            <th width="15%">办理时间</th>
            <th width="10%">办理结果</th>
            <th>操作意见</th>
        </tr>
        </thead>
        <tbody>
    <#list activitis as act>
        <#if act.assignee??>
        <tr>
        	<td class="status status-first">&nbsp;</td>
            <td>${act.name}</td>
            <td>${act.assignee!''}</td>
            <td> ${(act.claimTime?string("yyyy-MM-dd HH:mm"))!''}</td>
            <td> ${(act.endTime?string("yyyy-MM-dd HH:mm"))!''}</td>
            <td>${act.result!''} 
              </td>
            <td style="text-align:left;">${act.comment!''}</td>
        </tr>
        </#if>
    </#list>
        </tbody>
    </table>
</#if>
</div>
</div>