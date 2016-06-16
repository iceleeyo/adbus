<#import "template/template.ftl" as frame> <#import
"macro/timeslotChart.ftl" as trendChart> <#global menu="财务收入">
<@frame.html title="财务收入日报" js=["js/highcharts/highcharts-3.0.2.js",
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
            $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

            $("#day").change(function() {
                $(location).attr('href', "daysalesp?day=" + $("#day").val());
            });
        });
    </script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="tabs">
		<a id="tab1" class="active" href="${rc.contextPath}/report/daysalesp">财务收入日报</a>
		<a id="tab2" href="${rc.contextPath}/report/monthsalesp">财务收入月报</a>
	</div>
	<div style="margin-top: 20px;">
		<span>时间</span> <input class="ui-input ui-input-mini datepicker"
			type="text" name="day" id="day" data-is="isAmount isEnough"
			autocomplete="off" disableautocomplete="">
	</div>



	<div class="withdraw-title fn-clears">财务收入日报</div>
	<div class="tileContent" style="margin: 8px 10px 0 8px"
		id="remainTimeslots"></div>

	<@trendChart.trendChart chartDiv="remainTimeslots" title=""
	yName=yNames seriesTypes=seriesNames titleY="财务收入"
	highChart=remainTimeSlots baseY="${baseY!''}"/>
</div>

</@frame.html>

<!-- <script type="text/javascript">
	$('.tabs a').click(function(){
		$(this).parent().children().removeClass();
		$(this).addClass('active');
	});
	
	$('#tab1').click(function(){
		$('.tab-content').children().hide();
		$('.tab1').show();
	});
	$('#tab2').click(function(){
	$('.tab-content').children().hide();
		$('.tab2').show();
	});
	$('#tab3').click(function(){
		$('.tab-content').children().hide();
		$('.tab3').show();
	});
</script> -->

