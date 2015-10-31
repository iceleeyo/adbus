<#import "../template/template.ftl" as frame>
<#global menu="媒体使用情况统计">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="媒体使用情况统计" js=["../js/jquery-dateFormat.js"]>
    
<script type="text/javascript">
    
</script>

<style type="text/css">
.tdd{
    background-color: #3bafda;
    color: white;
    font-weight: normal;
    text-align: center;
    line-height: 30px;
    }
</style>
<div class="withdraw-wrap color-white-bg fn-clear">
				<div style="line-height: 30px; padding:15px;">
				<span>选择月份</span>
				<select class="ui-input ui-input-mini" name="utype" id="utype">
                  	<option value="screen" selected="selected">2015年10月</option>
                  	<option value="body">2015年11月</option>
                  	<option value="pub">2015年12月</option>
         		</select>
				</div>
				
                <div class="withdraw-title">
					<span>2015年各营销中心车身媒体使用情况汇总表【9月】</span>
				</div>
				
                <table id="table" style="line-height: 30px;text-align: center;border: 1px solid #D7D5D5;" class="display" width="100%">
                    <tr>
                        <td class="tdd">营销中心</td>
                        <td class="tdd">线路数</td>
                        <td class="tdd">总配车数</td>
                        <td class="tdd">在刊广告</td>
                        <td class="tdd">合同预定</td>
                        <td class="tdd">本月可使用车数</td>
                        <td class="tdd">次月下刊车数</td>
                        <td class="tdd">次月可使用车数</td>
                        <td class="tdd">广告在刊率</td>
                        <td class="tdd">车身媒体占有率</td>
                    </tr>
                    <tr>
                        <td >自营营销中心</td>
                        <td >单层</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                    </tr>
					<tr>
                        <td >CBS营销中心</td>
                        <td >单层</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                    </tr>
                    <tr>
                        <td >白马营销中心</td>
                        <td >单层</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                    </tr>
                    <tr>
                        <td >七彩营销中心</td>
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
                    
                    <tr>
                        <th >合计</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        
                    </tr>
                </table><br>
                
                <div class="withdraw-title">
					<span>2015年车身媒体使用情况汇总表【9月】</span>
				</div>
                <table id="table" style="line-height: 30px;text-align: center;border: 1px solid #D7D5D5;" class="display" width="100%">
                    <tr>
                        <td class="tdd">类型</td>
                        <td class="tdd">级   别</td>
                        <td class="tdd">线路数</td>
                        <td class="tdd">总配车数</td>
                        <td class="tdd">在刊广告</td>
                        <td class="tdd">合同预定</td>
                        <td class="tdd">本月可使用车数</td>
                        <td class="tdd">次月下刊车数</td>
                        <td class="tdd">次月可使用车数</td>
                        <td class="tdd">广告在刊率</td>
                        <td class="tdd">车身媒体占有率</td>
                    </tr>
                    <tr>
                        <td rowspan=5>单层车合计</td>
                        <td >特级线路</td>
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
					<tr>
                        
                        <td >A++</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td><td >11</td>
                    </tr>
                    <tr>
                        
                        <td >A+</td>
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
                    <tr>
                        
                        <td >A</td>
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
                    <tr>
                        <th >合计</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                    </tr>
                    <tr>
                        <td rowspan=5>双层车合计</td>
                        <td >特级线路</td>
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
					<tr>
                        
                        <td >A++</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td>
                        <td >11</td><td >11</td>
                    </tr>
                    <tr>
                        
                        <td >A+</td>
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
                    <tr>
                        
                        <td >A</td>
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
                    <tr>
                        <th >合计</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                    </tr>
                </table>
</div>
</@frame.html>