<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="时段报表">
<@frame.html title="剩余时段月同比趋势图"
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
            $("#year, #baseY").change(function() {
                $(location).attr('href', "mom?year=" + $("#year").val()
                        + ($("#baseY").is(":checked")? "&baseY=0" : ""));
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
<div class="tabs">
		<a id="tab1" href="${rc.contextPath}/report/day">剩余时段报表</a>
		<a id="tab2" href="${rc.contextPath}/report/hour">三天时段报表</a>
		<a id="tab3" href="${rc.contextPath}/report/dayp">剩余时段比例</a>
		<a id="tab4" href="${rc.contextPath}/report/hourp">单日时段比例</a>
		<a id="tab5" href="${rc.contextPath}/report/monthp">全年时段比例</a>
		<a id="tab6" href="${rc.contextPath}/report/wow">时段两周同比</a>
		<a id="tab7" class="active" href="${rc.contextPath}/report/mom">时段月同比</a>
		
</div>
 <div style="margin-top: 20px;">
	<span>时间</span>
            <select class="ui-input ui-input-mini" name="year" id="year">
                <#list -3 .. 3 as i>
                    <option value="${thisYear+i}" <#if year == thisYear + i>selected="selected"</#if>>${thisYear+i}</option>
                </#list>
            </select>
            <span class="ui-label-mini">
                <input type="checkbox" name="baseY" id="baseY" <#if baseY?? && baseY == 0>checked</#if>>用0作为基线
            </span>
</div>
<div class="withdraw-title fn-clear">
       剩余时段月同比趋势图
</div>
    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    yName={"THIS_YEAR":"remain","PREV_YEAR":"remain"}
    titleY="剩余时长" highChart=remainTimeSlots baseY="${baseY!''}"
    seriesTypes=["THIS_YEAR", "PREV_YEAR"] />
</div>
</@frame.html>

