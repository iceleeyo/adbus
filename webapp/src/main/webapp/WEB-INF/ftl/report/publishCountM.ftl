<#import "../template/template.ftl" as frame>
<#global menu="月发布统计">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="月发布统计" js=["../js/jquery-dateFormat.js"]>
    
<script type="text/javascript">
    
</script>

<div class="withdraw-wrap color-white-bg fn-clear">

                <div class="withdraw-title">
					<span>2015年各营销中心本月媒体发布情况统计表【9月】</span>
				</div>
                <table id="table" class="display" width="100%">
                    <thead>
                    <tr>
                        <th rowspan=2>营销中心</th>
                        <th rowspan=2>车型</th>
                        <th rowspan=2>合同数</th>
                        <th colspan='5'>上刊发布车辆数</th>
                        <th colspan='5'>续刊车数</th>
                        <th colspan='6'>刊期分类（车数）</th>
                    </tr>
                    </thead>
                    <tr>
                        <th  colspan='3'></th>
                        <th >特级</th>
                        <th >A++</th>
                        <th >A+</th>
                        <th >A</th>
                        <th >合计</th>
                        <th >特级</th>
                        <th >A++</th>
                        <th >A+</th>
                        <th >A</th>
                        <th >合计</th>
                        <th >1个月</th>
                        <th >3个月</th>
                        <th >6个月</th>
                        <th >12个月</th>
                        <th >其他期刊</th>
                        <th >合计</th>
                    </tr>
					<tr>
                        <td >自营</td>
                        <td >单层</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        
                    </tr>
                </table>
                
                <div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>温馨提示：未绑定订单的物料才能够删除。
		</div>
		</div>
</div>
</@frame.html>