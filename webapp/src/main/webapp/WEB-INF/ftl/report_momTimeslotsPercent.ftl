<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="全年时段比例">
<@frame.html title="全年时段比例"
js=["highcharts/highcharts-3.0.2.js", "highcharts/exporting.js", "chart.js", "jquery-dateFormat.js", "jquery-ui/jquery-ui.js", "datepicker.js", "jquery.datepicker.region.cn.js"]
css=["jquery-ui/jquery-ui.css"]>
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
            $("#year").change(function() {
                $(location).attr('href', "mom?year=" + $("#year").val());
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
    <div class="withdraw-title fn-clear">
        全年时段比例
        <div class="report-toolbar">
            <select class="ui-input ui-input-mini" name="year" id="year">
                <#list -3 .. 3 as i>
                    <option value="${thisYear+i}" <#if year == thisYear + i>selected="selected"</#if>>${thisYear+i}</option>
                </#list>
            </select>
        </div>
    </div>

    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    yName={"MONTH1":"remain","MONTH2":"ordered", "MONTH_PEAK1":"remain", "MONTH_PEAK2":"ordered"}
    titleY="剩余时长" highChart=remainTimeSlots baseY="${baseY!''}"
    seriesTypes=["MONTH1", "MONTH2","MONTH_PEAK1", "MONTH_PEAK2"] />
</div>
</@frame.html>

