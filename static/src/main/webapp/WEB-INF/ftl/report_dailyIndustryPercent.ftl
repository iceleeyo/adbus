<#import "template/template.ftl" as frame> <#import
"macro/timeslotChart.ftl" as trendChart> <#global menu="销售报表">
<@frame.html title="售出情况行业细分" js=["js/highcharts/highcharts-3.0.2.js",
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
            $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

            $("#day").change(function() {
                $(location).attr('href', "dayindustryp?day=" + $("#day").val());
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="tabs">
		<a id="tab1" href="${rc.contextPath}/report/dayorderp">销售报表</a> <a
			id="tab2" class="active" href="${rc.contextPath}/report/dayindustryp">行业细分</a>
	</div>
	<div style="margin-top: 20px;">
		<span>时间</span> <input class="ui-input ui-input-mini datepicker"
			type="text" name="day" id="day" data-is="isAmount isEnough"
			autocomplete="off" disableautocomplete="">
	</div>
	<div class="withdraw-title fn-clears">售出情况行业细分</div>

	<div class="tileContent" style="margin: 8px 10px 0 8px"
		id="remainTimeslots"></div>
	<@trendChart.trendChart chartDiv="remainTimeslots" title=""
	yName=yNames titleY="售出情况行业细分" highChart=remainTimeSlots
	baseY="${baseY!''}" seriesTypes=seriesNames />
</div>
</@frame.html>

