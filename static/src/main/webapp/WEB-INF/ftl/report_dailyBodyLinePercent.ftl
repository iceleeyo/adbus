<#import "template/template.ftl" as frame> <#import
"macro/timeslotChart.ftl" as trendChart> <#global menu="线路细分">
<@frame.html title="售出情况线路细分" js=["js/highcharts/highcharts-3.0.2.js",
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
                $(location).attr('href', "daylinep?day=" + $("#day").val());
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title fn-clear">
		售出情况线路细分
		<div class="report-toolbar">
			<input class="ui-input ui-input-mini datepicker" type="text"
				name="day" id="day" data-is="isAmount isEnough" autocomplete="off"
				disableautocomplete="">
		</div>
	</div>

	<div class="tileContent" style="margin: 8px 10px 0 8px"
		id="remainTimeslots"></div>
	<@trendChart.trendChart chartDiv="remainTimeslots" title=""
	yName=yNames titleY="售出情况线路细分" highChart=remainTimeSlots
	baseY="${baseY!''}" seriesTypes=seriesNames />
</div>
</@frame.html>

