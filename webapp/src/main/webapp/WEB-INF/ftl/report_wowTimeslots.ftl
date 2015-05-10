<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="时段两周同比">
<@frame.html title="剩余时段周同比趋势图"
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
                $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

            $("#day, #baseY").change(function() {
                $(location).attr('href', "wow?day=" + $("#day").val()
                        + ($("#baseY").is(":checked")? "&baseY=0" : ""));
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
    <div class="withdraw-title fn-clear">
        剩余时段周同比趋势图
        <div class="report-toolbar">
        <input
                class="ui-input ui-input-mini datepicker" type="text" name="day"
                id="day" data-is="isAmount isEnough"
                autocomplete="off" disableautocomplete="">
        <span class="ui-label-mini">
            <input type="checkbox" name="baseY" id="baseY" <#if baseY?? && baseY == 0>checked</#if>>用0作为基线
        </span>
        </div>
    </div>
    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    yName={"THIS_WEEK":"remain","PREV_WEEK":"remain"}
    titleY="剩余时长" highChart=remainTimeSlots baseY="${baseY!''}"
    seriesTypes=["THIS_WEEK", "PREV_WEEK"] />
</div>
</@frame.html>

