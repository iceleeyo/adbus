<#import "template/template.ftl" as frame> <#import
"macro/timeslotChart.ftl" as trendChart> <#global menu="财务收入">
<@frame.html title="财务收入月报" js=["js/highcharts/highcharts-3.0.2.js",
"js/highcharts/exporting.js", "js/chart.js", "js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.js", "js/datepicker.js",
"js/jquery.datepicker.region.cn.js"] css=["js/jquery-ui/jquery-ui.css"]>
<style type="text/css">
.ui-datepicker-calendar.only-month {
	display: none;
}

.report-toolbar {
	float: right;
}

.report-toolbar .ui-label-mini {
	font-size: 12px;
	line-height: 35px;
}
</style>
<script type="text/javascript">
        $(function(){
            $("#year").change(function() {
                $(location).attr('href', "monthsalesp?year=" + $("#year").val());
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="tabs">
		<a id="tab1" href="${rc.contextPath}/report/daysalesp">财务收入日报</a> <a
			id="tab2" class="active" href="${rc.contextPath}/report/monthsalesp">财务收入月报</a>
	</div>
	<div style="margin-top: 20px;">
		<span>时间</span> <select class="ui-input ui-input-mini" name="year"
			id="year"> <#list -3 .. 3 as i>
			<option value="${thisYear+i}"<#if year == thisYear +
				i>selected="selected"</#if>>${thisYear+i}</option> </#list>
		</select>
	</div>
	<div class="withdraw-title fn-clears">财务收入月报</div>

	<div class="tileContent" style="margin: 8px 10px 0 8px"
		id="remainTimeslots"></div>
	<@trendChart.trendChart chartDiv="remainTimeslots" title=""
	yName=yNames titleY="销售收入" highChart=remainTimeSlots
	baseY="${baseY!''}" seriesTypes=seriesNames />
</div>
</@frame.html>

