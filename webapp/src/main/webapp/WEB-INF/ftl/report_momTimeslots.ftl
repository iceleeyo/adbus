<#import "template/template.ftl" as frame>
<#import "macro/momChart.ftl" as trendChart>

<#global menu="时段月同比">
<@frame.html title="剩余时段月同比趋势图"
js=["highcharts/highcharts-3.0.2.js", "highcharts/exporting.js", "jquery-dateFormat.js", "jquery-ui/jquery-ui.js", "datepicker.js", "jquery.datepicker.region.cn.js"]
css=["jquery-ui/jquery-ui.css"]>
    <style type="text/css">
        .ui-datepicker-calendar {
            display: none;
        }
    </style>
    <script type="text/javascript">
        $(function(){
            $("#year").change(function() {
                var year = $(this).val();
                $(location).attr('href', "mom?year=" + year);
            });
        });
    </script>
    <div class="withdraw-title fn-clear">
        剩余时段月同比趋势图
        <select class="ui-input ui-input-mini" name="year" id="year">
            <#list -3 .. 3 as i>
                <option value="${thisYear+i}" <#if year == thisYear + i>selected="selected"</#if>>${thisYear+i}</option>
            </#list>
        </select>
    </div>

    <div class="tileContent" style="margin:8px 10px 0 8px" id="remainTimeslots"></div>
    <@trendChart.trendChart chartDiv="remainTimeslots" title=""
    titleY="剩余时长" toolTipText="" highChart=remainTimeSlots
    seriesTypes=["THIS_YEAR", "PREV_YEAR"] />
</@frame.html>

