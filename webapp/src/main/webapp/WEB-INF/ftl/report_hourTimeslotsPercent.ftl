<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="时段报表">
<@frame.html title="单日时段比例"
js=["js/highcharts/highcharts-3.0.2.js", "js/highcharts/exporting.js", "js/chart.js", "js/jquery-dateFormat.js", "js/jquery-ui/jquery-ui.js", "js/datepicker.js", "js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>
    <style type="text/css">
        .ui-datepicker-calendar.only-month {
            display: none;
        }
        .report-toolbar {
            float: right;
        }

        .report-toolbar .ui-label-mini {
            font-size: 12px;line-height: 35px;
        }
    </style>
    <script type="text/javascript">
        $(function(){
            $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

            $("#day").change(function() {
                $(location).attr('href', "hourp?day=" + $("#day").val());
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
<div class="tabs">
		<a id="tab1" href="${rc.contextPath}/report/day">剩余时段趋势</a>
		<a id="tab2" href="${rc.contextPath}/report/hour">三天时段报表</a>
		<a id="tab3" href="${rc.contextPath}/report/dayp">剩余时段比例</a>
		<a id="tab4" class="active" href="${rc.contextPath}/report/hourp">单日时段比例</a>
		<a id="tab5" href="${rc.contextPath}/report/monthp">全年时段比例</a>
		<a id="tab6" href="${rc.contextPath}/report/wow">时段两周同比</a>
		<a id="tab7" href="${rc.contextPath}/report/mom">时段月同比</a>
		
</div>
 <div style="margin-top: 20px;">
	<span>时间</span>
            <input
                    class="ui-input ui-input-mini datepicker" type="text" name="day"
                    id="day" data-is="isAmount isEnough"
                    autocomplete="off" disableautocomplete="">

</div>
<div class="withdraw-title fn-clear">
      单日时段比例
</div>
    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    yName={"TIMESLOT1":"remain","TIMESLOT2":"ordered"}
    titleY="剩余时长" highChart=remainTimeSlots baseY="${baseY!''}"
    seriesTypes=["TIMESLOT1", "TIMESLOT2"] />
</div>
</@frame.html>

