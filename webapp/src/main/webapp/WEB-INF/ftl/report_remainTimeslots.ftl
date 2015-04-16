<#import "template/template.ftl" as frame>
<#import "macro/timeslotChart.ftl" as trendChart>

<#global menu="剩余时段报表">
<@frame.html title="剩余时段趋势图"
js=["highcharts/highcharts-3.0.2.js", "highcharts/exporting.js", "jquery-dateFormat.js", "jquery-ui/jquery-ui.js", "datepicker.js", "jquery.datepicker.region.cn.js"]
css=["jquery-ui/jquery-ui.css"]>
    <script type="text/javascript">
        $(function(){
            $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

            $("#day").change(function() {
                var day = $(this).val();
                $(location).attr('href', "timeslot?day=" + day);
            });
        });
    </script>

    <div class="withdraw-title fn-clear">
        剩余时段趋势图
        <input
                class="ui-input ui-input-mini datepicker" type="text" name="day"
                id="day" data-is="isAmount isEnough"
                autocomplete="off" disableautocomplete="">
    </div>

    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    titleY="剩余时长" toolTipText="" highChart=remainTimeSlots
    seriesTypes=["TIMESLOT", "TIMESLOT_PEAK"] />
</@frame.html>

