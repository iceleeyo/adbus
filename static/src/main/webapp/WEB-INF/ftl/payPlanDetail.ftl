<#import "template/template_blank.ftl" as frame> <#global menu="订单分期详情">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="订单分期详情"
js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js","index_js/sift_common.js",
"js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/basecss.css">

<script type="text/javascript">
    $(document).ready(function() {
        initPayPlanTable('${rc.contextPath}',${orderId});
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<table id="payPlanTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>期数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>备注</th>
								<th>操作</th>
			</tr>
		</thead>

	</table>
	<div class="worm-tips">
		<div class="tips-title"></div>
	</div>
</div>
</@frame.html>
