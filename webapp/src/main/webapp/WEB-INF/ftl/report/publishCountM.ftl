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
				<div style="line-height: 30px;">
				<span>选择月份</span>
				<select class="ui-input ui-input-mini" name="utype" id="utype">
                  	<option value="screen" selected="selected">2015年10月</option>
                  	<option value="body">2015年11月</option>
                  	<option value="pub">2015年12月</option>
         		</select>'
				</div>
                <table id="table" style="line-height: 30px;text-align: center;border: 1px solid #D7D5D5;" class="display" width="100%">
                   
                    <tr>
                        <td rowspan=2>营销中心</td>
                        <td rowspan=2>车型</td>
                        <td rowspan=2>合同数</td>
                        <td colspan='5'>上刊发布车辆数</td>
                        <td colspan='5'>续刊车数</td>
                        <td colspan='6'>刊期分类（车数）</td>
                    </tr>
                    
                    <tr>
                        
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
                    <tr>
                        <td rowspan=2>SBC</td>
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
                    <tr>
                        
                        <td >双层</td>
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
                    <tr>
                        <td rowspan=2>白马</td>
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
                    <tr>
                        
                        <td >双层</td>
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
                    <tr>
                        <td >七彩</td>
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
                    <tr>
                        <th colspan=2>合计</th>
                        
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</th>
                        <th >11</td>
                        <th >11</td>
                    </tr>
                </table>
                
                
</div>
</@frame.html>