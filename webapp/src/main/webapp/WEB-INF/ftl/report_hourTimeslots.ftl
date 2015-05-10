<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="三天时段报表">
<@frame.html title="三天时段报表"
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

            $("#day, #baseY").change(function() {
                $(location).attr('href', "hour?day=" + $("#day").val()
                        + ($("#baseY").is(":checked")? "&baseY=0" : ""));
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
    <div class="withdraw-title fn-clear">
        三天时段报表
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
    yName={"TIMESLOT0":"remain","TIMESLOT1":"remain","TIMESLOT2":"remain"}
    titleY="剩余时长" highChart=remainTimeSlots baseY="${baseY!''}"
    seriesTypes=["TIMESLOT0", "TIMESLOT1", "TIMESLOT2"] />
</div>
</@frame.html>

