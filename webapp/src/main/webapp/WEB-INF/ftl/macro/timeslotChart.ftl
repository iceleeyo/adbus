<#macro trendChart chartDiv title titleY highChart yName baseY menuId="" seriesTypes=[] yName={}>
<script type="text/javascript">
    var chart;

    $(document).ready(function() {
        chart = new Highcharts.Chart({
            exporting: {
                enabled: false
            },
            chart: {
                renderTo: '${chartDiv}',
                <#if highChart.stacked>
                    type: "${highChart.type!'column'}",
                <#else>
                    type: "${highChart.type!'line'}",
                </#if>
                marginLeft: 80,
                marginRight: 30 * ${highChart.scaleTypeList?size},
                marginBottom: 60,
                xType: "${highChart.xType}",
            },
            title: {
                text: '${title}',
                style: {
               	        color: '#000',
               	        fontSize: '14px'
                        },
                x: 0 //center
            },
            xAxis:
            {
                <#if highChart.xType == "DATE">
                    type: 'datetime',
                    //tickInterval: 24 * 3600 * 1000,
                    labels: {
                        rotation: -25,
                        align: 'right',
                        style: { font: 'normal 10px Verdana, sans-serif'},
                        formatter: xLabelFormatter,
                    }
                <#else>
                    type: 'categories',
                    categories: [
                        <#list highChart.xAxis as x>
                        ${x}
                            <#if x_has_next>,</#if>
                        </#list>
                    ],
                    labels: {
                        //rotation: 12,
                        align: 'right',
                        style: { font: 'normal 10px Verdana, sans-serif'},
                        formatter: xLabelFormatter,
                    }
                </#if>
            },
            yAxis:[
            <#list highChart.scaleTypeList as scaleType>
                    {
                        title: {
                            text: '${scaleType.desc}'
                        },
                        id: '${scaleType}',
                        <#if baseY?? && baseY != "">
                            min: ${baseY!0},
                        </#if>
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: '#808080'
                        }],
                        labels: {
                            formatter: yLabelFormatter,
                        },
                        <#if scaleType == 'PERCENT' && highChart.scaleTypeList?size &gt; 1>
                            opposite: true,
                        </#if>
                    },
            </#list>
            ],
            tooltip:
            {
                formatter: tooltipFormatter,
            },
            plotOptions: {
                series: {
                    <#if highChart.stacked>
                        stacking: 'percent',
                    </#if>
                    cursor: 'pointer',
                    marker: {
                        lineWidth: 1
                    },
                    point: {
                        <#if menuId!="">
                            events: {
                                click: function(e) {
                                    selectedDate = $.format.date(e.point.x,'yyyy-MM-dd');
                                    curSeries=this.series.name;

                                    $('#trend-menu ul li').hide();
                                    $('#trend-menu [menu-type=${menuId}]').show();
                                    //$.vscontext(e, {menuBlock: 'trend-menu'});
                                }
                            }
                        </#if>
                    }
                }
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom',
                x: 0,
                y: 0,
                borderWidth: 0
            },
            series: [
                <#list seriesTypes as type>
                    {
                        name: "${highChart.seriesListAsMap['${type}'].getName("${yName[type]}")}",
                        data: [${highChart.seriesListAsMap['${type}'].getData("${yName[type]}")}],
                        yType: "${highChart.seriesListAsMap['${type}'].seriesType.yType}",
                        xType: "${highChart.xType}",
                        marker: {
                            enabled: ${highChart.seriesListAsMap['${type}'].pointerEnabled?c},
                            radius: ${highChart.seriesListAsMap['${type}'].pointerRadius!'2'}
                        },
//                        dataLabels: {
//                            enabled: true,
//                            rotation: 90,
//                        }
                    }
                    <#if type_has_next>,</#if>
                </#list>
            ]
        });
    });
</script>
</#macro>