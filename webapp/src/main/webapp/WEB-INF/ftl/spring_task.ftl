<#import "template/template_blank.ftl" as frame> <#assign
security=JspTaglibs["/WEB-INF/tlds/security.tld"] /> <@frame.html
title="线路列表" js=["js/jquery-dateFormat.js","js/schedule.js"]
css=["css/classctable.css"]>
<div class="title s-clear">
	<span style="width: 250px"> 系统定时任务[spring @schedule] </span>
</div>
<table id="table" class="display compact" cellspacing="0" width="100%">
	<thead>
		<tr>
			<th orderBy="name">任务名</th>
			<th orderBy="name">类名</th>
			<th orderBy="level">方法名</th>
			<th orderBy="_cars">expression</th>
			<th orderBy="_cars">状态</th>
			<th>上次运行时间</th>
		</tr>
	</thead>
</table>
</div>
</@frame.html>
