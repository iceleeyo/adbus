<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="当天时段对比">
<@frame.html title="当天时段对比"
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

            $("#day").change(function() {
                $(location).attr('href', "hourp?day=" + $("#day").val());
            });
        });
    </script>
    <div class="withdraw-title fn-clear">
        当天时段对比
        <div class="report-toolbar">
            <input
                    class="ui-input ui-input-mini datepicker" type="text" name="day"
                    id="day" data-is="isAmount isEnough"
                    autocomplete="off" disableautocomplete="">
        </div>
    </div>

    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    yName={"TIMESLOT1":"remain","TIMESLOT2":"ordered"}
    titleY="剩余时长" highChart=remainTimeSlots baseY="${baseY!''}"
    seriesTypes=["TIMESLOT1", "TIMESLOT2"] />
</@frame.html>

