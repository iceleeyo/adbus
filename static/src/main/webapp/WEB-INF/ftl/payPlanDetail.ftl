<#import "template/template_blank.ftl" as frame> <#global menu="订单分期详情">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="订单分期详情"
js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js","index_js/sift_common.js",
"js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/basecss.css">

<script type="text/javascript">
    $(document).ready(function() {
    
        initPayPlanTable('${rc.contextPath}',${orderId},'<@security.authorize
			ifAnyGranted="ShibaFinancialManager"></@security.authorize>','${type}');
    } );
</script>
<style type="text/css">
.ls-101 {
	margin-left: 0px;
}
</style>
<div class="withdraw-wrap color-white-bg fn-clear" style="width:100%">
	<table id="payPlanTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>期数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>付款方式</th>
								<th>状态</th>
								<th>付款人</th>
								<th>处理人</th>
								<th>最后操作时间</th>
								<th>备注</th>
								<@security.authorize ifAnyGranted="ShibaFinancialManager"> 
								<th>操作</th></@security.authorize>
								
							<@security.authorize ifNotGranted="ShibaFinancialManager"> 
								<th></th></@security.authorize>
			</tr>
		</thead>

	</table>
	<br><br><br>
	<div class="worm-tips">
	
		<div class="tips-title">
		注:已支付的期数不能修改或是删除
		</div>
	</div>
</div>
</@frame.html>
