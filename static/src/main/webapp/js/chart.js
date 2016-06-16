var yLabelFormatter = function() {
    var label = this.value;
    var isPercent = this.chart.options.plotOptions.series.stacking == 'percent';
    if (isPercent) {
        return label + "%";
    }
    var id = this.axis.userOptions.id;
    var toolTipText = this.axis.userOptions.tooltipText;
    if (!id)
        return toolTipText || '';

    if (id == 'TIME_COUNT') {
        var d = new Date(label * 1000);
        d = new Date(label * 1000 + d.getTimezoneOffset() * 60000);
        label = $.format.date(d, "H:mm,ss;").replace(",","'").replace(";","\"");
    } else if (id == 'LONG_TIME_COUNT') {
        label = (label /3600).toFixed(1);
    } else if (id == 'BUS_COUNT') {
        label = label + "辆";
    } else if (id == 'INCOME') {
        if (label > 100) {
            label = (label/ 10000).toFixed(2) + "万元";
        } else {
            label = label + "元";
        }
    }
    return label + (toolTipText || '');
}


var tooltipFormatter = function() {
    var yStr = this.y;
    var yType = this.series.userOptions.yType;
    var xType = this.series.userOptions.xType;
    if (yType == 'TIME_COUNT') {
        var d = new Date(this.y * 1000);
        d = new Date(this.y * 1000 + d.getTimezoneOffset() * 60000);
        yStr = $.format.date(d, "H:mm,ss;").replace(",","'").replace(";","\"");
    } else if (yType == 'LONG_TIME_COUNT') {
        yStr = (this.y /3600).toFixed(1) + '小时';
    } else if (yType == 'PERCENT') {
        yStr = this.y + "%";
    } else if (yType == 'BUS_COUNT') {
        yStr = this.y + "辆";
    } else if (yType == 'INCOME') {
        if (this.y > 100) {
            yStr = (this.y / 10000).toFixed(2) + "万元";
        } else {
         yStr = this.y + "元";
        }
    }

    var xStr = this.x;
    if (xType == 'DATE') {
        xStr = $.format.date(this.x, 'yyyy-MM-dd');
    } else if (xType == 'WEEK') {
        xStr =  $.format.date(this.point["day"], 'yyyy-MM-dd');
    } else if (xType == 'MONTH') {
        xStr = this.point["year"] + "年" + this.point["month"] + "月";
    } else if (xType == 'HOUR') {
        xStr = $.format.date(this.point["day"], 'MM-dd') + ' ' + this.x + ':00'
    }
    return  xStr +'<br/>'+
        this.series.name +': <b>'+ yStr +'</b>';
}


var xLabelFormatter = function() {
    var xType = this.chart.options.chart["xType"];
    if (xType == 'DATE') {
        return $.format.date(this.value,'MM-dd');
    } else if (xType == 'WEEK') {
        return this.value;
    } else if (xType == 'MONTH') {
        return this.value + '月';
    } else {
        return this.value;
    }
}