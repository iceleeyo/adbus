<#import "../template/template.ftl" as frame>
<#global menu="月发布统计">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="月发布统计" js=["../js/jquery-dateFormat.js"]>
    
<script type="text/javascript">
    
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

                <div class="withdraw-title">
					<span>2015年各营销中心本月媒体发布情况统计表【9月】</span>
					<a class="block-btn" href="${rc.contextPath}/supplies/new" style="margin-left:10px">添加物料</a>
					<@security.authorize ifAnyGranted="ShibaOrderManager">
					<a class="block-btn" href="${rc.contextPath}/supplies/newBlackAd">添加底板</a>
					</@security.authorize>
				</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th orderBy="name">营销中心</th>
                        <th orderBy="name">车型</th>
                        <th orderBy="suppliesType">合同数</th>
                        <th orderBy="industry.id" >上刊发布车辆数</th>
                        <th orderBy="created">续刊车数</th>
                        <th orderBy="created">刊期分类（车数）</th>
                    </tr>
                    </thead>
                    <tr>
                        <th orderBy="name">营销中心</th>
                        <th orderBy="name">车型</th>
                        <th orderBy="suppliesType">合同数</th>
                        <th orderBy="industry.id">上刊发布车辆数</th>
                        <th orderBy="created">续刊车数</th>
                        <th orderBy="created">刊期分类（车数）</th>
                    </tr>

                </table>
                
                <div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>温馨提示：未绑定订单的物料才能够删除。
		</div>
		</div>
</div>
</@frame.html>