<#macro trendChart chartDiv title titleY toolTipText highChart menuId="" seriesTypes=[]>
<script type="text/javascript">
    var chart;

    $(document).ready(function() {
        chart = new Highcharts.Chart({
            exporting: {
                enabled: false
            },
            chart: {
                renderTo: '${chartDiv}',
                type: 'line',
                marginLeft: 80,
                marginRight: 30 * ${highChart.scaleTypeList?size},
                marginBottom: 60
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
                type: 'categories',
                categories: [
                    <#list highChart.xAxis as x>
                        ${x}
                        <#if x_has_next>,</#if>
                    </#list>
                ],
                labels: {
                    rotation: 12,
                    align: 'right',
                    style: { font: 'normal 10px Verdana, sans-serif'},
                    formatter: function() {
                    return this.value;
                    }
                }
            },
            yAxis:[
            <#list highChart.scaleTypeList as scaleType>
                    {
                        title: {
                            text: '${scaleType.desc}'
                        },
                        id: '${scaleType}',
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: '#808080'
                        }],
                        labels: {
                            formatter: function() {
                                var remain = this.value;
                                <#if scaleType == 'TIME_COUNT'>
                                    var d = new Date(remain * 1000);
                                    d = new Date(remain * 1000 + d.getTimezoneOffset() * 60000);
                                    remain = $.format.date(d, "H:mm,ss;").replace(",","'").replace(";","\"");
                                <#elseif scaleType == 'PERCENT'>
                                    remain = remain + "%";
                                </#if>
                                return remain +'${toolTipText}'
                            }
                        },
                        <#if scaleType == 'PERCENT' && highChart.scaleTypeList?size &gt; 1>
                            opposite: true,
                        </#if>
                    },
            </#list>
        ],
        tooltip: {
                formatter: function() {
                    var yStr = this.y;
                    var data = this.series.data[this.key-1];
                    var day = data["day"];
                    if (this.series.userOptions['scaleType'] == 'TIME_COUNT') {
                        var d = new Date(this.y * 1000);
                        d = new Date(this.y * 1000 + d.getTimezoneOffset() * 60000);
                        yStr = $.format.date(d, "H:mm,ss;").replace(",","'").replace(";","\"");
                    } else if (this.series.userOptions['scaleType'] == 'PERCENT') {
                        yStr = this.y + "%";
                    }
                    return $.format.date(day, 'yyyy-MM-dd') +'<br/>'+
                            this.series.name +': <b>'+ yStr +'${toolTipText}</b>';
                }
            },
            plotOptions: {
                series: {
                    cursor: 'pointer',
                    marker: {
                        lineWidth: 1
                    },
                    point: {
                        <#if menuId!="">
                            events: {
                                click: function(e) {
                                    selectedDate = $.format.date(e.point.x,'yyyyMMdd');
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
                        name: "${highChart.seriesListAsMap['${type}'].name}",
                        data: [${highChart.seriesListAsMap['${type}'].data}],
                        scaleType: "${highChart.seriesListAsMap['${type}'].seriesType.scaleType}",
                        yAxis: "${highChart.seriesListAsMap['${type}'].seriesType.scaleType}",
                        marker: {
                            symbol: "<#if highChart.seriesListAsMap['${type}'].seriesType.scaleType == 'PERCENT'>circle<#else>square</#if>"
                        }
                    }
                    <#if type_has_next>,</#if>
                </#list>
            ]
        });
    });
</script>
<#--
    <#list seriesTypes as type>
    ${type}
    </#list>
-->

</#macro>