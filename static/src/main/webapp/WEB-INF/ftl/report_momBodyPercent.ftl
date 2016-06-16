<#import "template/template.ftl" as frame> <#import
"macro/timeslotChart.ftl" as trendChart> <#global menu="年售出情况">
<@frame.html title="全年售出比例" js=["js/highcharts/highcharts-3.0.2.js",
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
                $(location).attr('href', "monthp?year=" + $("#year").val());
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title fn-clear">
		全年售出比例
		<div class="report-toolbar">
			<select class="ui-input ui-input-mini" name="year" id="year">
				<#list -3 .. 3 as i>
				<option value="${thisYear+i}"<#if year == thisYear +
					i>selected="selected"</#if>>${thisYear+i}</option> </#list>
			</select>
		</div>
	</div>

	<div class="tileContent" style="margin: 8px 10px 0 8px"
		id="remainTimeslots"></div>
	<@trendChart.trendChart chartDiv="remainTimeslots" title=""
	yName={"MONTH1":"remain","MONTH2":"ordered"} titleY="剩余数量"
	highChart=remainTimeSlots baseY="${baseY!''}" seriesTypes=["MONTH1",
	"MONTH2"] />
</div>
</@frame.html>

