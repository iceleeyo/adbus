<#import "../template/template.ftl" as frame> <#global menu="媒体使用汇总">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="媒体使用情况统计" js=["../js/jquery-dateFormat.js"]>

<style type="text/css">
.tdd {
	background-color: #3bafda;
	color: white;
	font-weight: normal;
	text-align: center;
	line-height: 30px;
}

th,td {
	white-space: nowrap;
	border: 1px solid #C5C5C5;
}
</style>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div style="line-height: 30px; padding: 15px;">
		<span>选择月份</span> <select class="ui-input ui-input-mini" name="utype"
			id="utype">
			<option value="sept" selected="selected">2015年9月</option>
			<option value="oct">2015年10月</option>
			<option value="nov">2015年11月</option>
			<option value="dec">2015年12月</option>
		</select>
	</div>

	<div class="withdraw-title">
		<span>2015年各营销中心车身媒体使用情况汇总表【9月】</span>
	</div>

	<table id="table"
		style="line-height: 30px; text-align: center; border: 1px solid #D7D5D5;"
		class="display" width="100%">
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
			<td>自营营销中心</td>
			<td>220</td>
			<td>3872</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>CBS营销中心</td>
			<td>153</td>
			<td>4243</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>白马营销中心</td>
			<td>126</td>
			<td>3212</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>七彩营销中心</td>
			<td>311</td>
			<td>8294</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>

		<tr>
			<th>合计</th>
			<th>810</th>
			<th>19621</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>

		</tr>
	</table>
	<br>

	<div class="withdraw-title">
		<span>2015年车身媒体使用情况汇总表【9月】</span>
	</div>
	<table id="table"
		style="line-height: 30px; text-align: center; border: 1px solid #D7D5D5;"
		class="display" width="100%">
		<tr>
			<td class="tdd">类型</td>
			<td class="tdd">级 别</td>
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
			<td rowspan="5">单层车合计</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>

			<td>A++</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>

			<td>A+</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>

			<td>A</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>小计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<td rowspan="5">双层车合计</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>

			<td>A++</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>

			<td>A+</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>

			<td>A</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>小计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<th colspan="2">合计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
	</table>
	<br />

	<div class="withdraw-title">
		<span>2015年七彩车身媒体使用情况一览表【九月】</span>
	</div>
	<table id="table"
		style="line-height: 30px; text-align: center; border: 1px solid #D7D5D5;"
		class="display" width="100%">
		<tr>
			<td class="tdd">营销中心</td>
			<td class="tdd">类型</td>
			<td class="tdd">级 别</td>
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
			<td rowspan="11">七彩营销中心</td>
			<td rowspan="5">单层大公共</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A++线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A+线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>合计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>

		<tr>
			<td rowspan="5">单层八方达</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A++线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A+线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>合计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<th colspan="2">总计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
	</table>
	<br />

	<div class="withdraw-title">
		<span>2015年白马车身媒体使用情况一览表【九月】</span>
	</div>
	<table id="table"
		style="line-height: 30px; text-align: center; border: 1px solid #D7D5D5;"
		class="display" width="100%">
		<tr>
			<td class="tdd">营销中心</td>
			<td class="tdd">类型</td>
			<td class="tdd">级 别</td>
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
			<td rowspan="11">白马营销中心</td>
			<td rowspan="5">单层车</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A++线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A+线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>合计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>

		<tr>
			<td rowspan="5">双层车</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A++线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A+线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>合计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<th colspan="2">总计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
	</table>
	<br />

	<div class="withdraw-title">
		<span>2015年CBS车身媒体使用情况一览表【九月】</span>
	</div>
	<table id="table"
		style="line-height: 30px; text-align: center; border: 1px solid #D7D5D5;"
		class="display" width="100%">
		<tr>
			<td class="tdd">营销中心</td>
			<td class="tdd">类型</td>
			<td class="tdd">级 别</td>
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
			<td rowspan="11">CBS营销中心</td>
			<td rowspan="5">单层车</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A++线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A+线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>合计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>

		<tr>
			<td rowspan="5">双层车</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A++线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A+线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th>合计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
		<tr>
			<th colspan="2">总计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
	</table>
	<br />

	<div class="withdraw-title">
		<span>2015年自营车身媒体使用情况一览表【九月】</span>
	</div>
	<table id="table"
		style="line-height: 30px; text-align: center; border: 1px solid #D7D5D5;"
		class="display" width="100%">
		<tr>
			<td class="tdd">营销中心</td>
			<td class="tdd">类型</td>
			<td class="tdd">级 别</td>
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
			<td rowspan="5">公交自营中心</td>
			<td rowspan="4">单层车</td>
			<td>特级线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A++线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A+线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>A线路</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th colspan="2">总计</th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
	</table>
	<br />
</div>
</@frame.html>
