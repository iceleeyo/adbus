<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="时段月同比">
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
    <div class="withdraw-title fn-clear">
        剩余时段月同比趋势图
        <div class="report-toolbar">
            <select class="ui-input ui-input-mini" name="year" id="year">
                <#list -3 .. 3 as i>
                    <option value="${thisYear+i}" <#if year == thisYear + i>selected="selected"</#if>>${thisYear+i}</option>
                </#list>
            </select>
            <span class="ui-label-mini">
                <input type="checkbox" name="baseY" id="baseY" <#if baseY?? && baseY == 0>checked</#if>>用0作为基线
            </span>
        </div>
    </div>

    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    yName={"THIS_YEAR":"remain","PREV_YEAR":"remain"}
    titleY="剩余时长" highChart=remainTimeSlots baseY="${baseY!''}"
    seriesTypes=["THIS_YEAR", "PREV_YEAR"] />
</div>
</@frame.html>

