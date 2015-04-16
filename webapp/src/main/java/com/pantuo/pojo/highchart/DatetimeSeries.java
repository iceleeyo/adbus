/**
 * 
 */
package com.pantuo.pojo.highchart;

import com.pantuo.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


/**
 * @author tliu
 *
 */
public class DatetimeSeries extends Series<Date ,Number> {

	private static final long serialVersionUID = -766803877214280915L;

	protected DatetimeSeries(String name, final SeriesType seriesType, final DayList dayList) {
        super(name, seriesType, dayList.toDayList());
	}

    @Override
	/**
	 * Something like this:
	 * 
	 *          [Date.UTC(2010, 0, 1), 29.9],
     *          [Date.UTC(2010, 0, 2), 71.5],
     *          [Date.UTC(2010, 0, 3), 106.4],
     *          [Date.UTC(2010, 0, 6), 129.2],
     *          [Date.UTC(2010, 0, 7), 144.0],
     *          [Date.UTC(2010, 0, 8), 176.0]
	 */
	public String getData() {

        if(null == xAxis || xAxis.isEmpty()){
            return "";
        }

        StringBuilder builder = new StringBuilder();

        Calendar cal = DateUtil.newCalendar();
        Iterator<Date> iter = xAxis.iterator();
        while (iter.hasNext()) {
            Date day = iter.next();
            Number value = data.get(day);

            cal.setTime(day);

            if(null == value){
                builder.append("[Date.UTC(")
                        .append(cal.get(Calendar.YEAR)).append(", ")
                        .append(cal.get(Calendar.MONTH)).append(", ")
                        .append(cal.get(Calendar.DATE)).append("), ")
                        .append("null").append("],\r\n");
                continue;
            }

            if (value instanceof Double && false == Double.isNaN(value.doubleValue())) {
                Double originalValue = value.doubleValue();
                if(originalValue == Double.POSITIVE_INFINITY) {
                    value = maxY*10;
                }
                if(originalValue == Double.NEGATIVE_INFINITY) {
                    value = minY*10;
                }

                value = Math.round(value.doubleValue() * 100) / 100.0;

                if(Double.isInfinite(originalValue)){
                    builder.append("{x:Date.UTC(").append(cal.get(Calendar.YEAR)).append(", ")
                            .append(cal.get(Calendar.MONTH)).append(", ")
                            .append(cal.get(Calendar.DATE)).append("), ")
                            .append("y:").append(value).append(",")
                            .append("fillColor:").append("'white',")
                            .append("lineColor:").append("'gray'")
                            .append("},\r\n");
                    continue;
                }
            }

            builder.append("[Date.UTC(")
                    .append(cal.get(Calendar.YEAR)).append(", ")
                    .append(cal.get(Calendar.MONTH)).append(", ")
                    .append(cal.get(Calendar.DATE)).append("), ")
                    .append(Double.isNaN(value.doubleValue())?"null":value).append("],\r\n");
        }
            return builder.toString();
    }

    /**
     * @return
     * return value array such as [20.0,30.1,33.2,1.1]
     * currently used by sparkline in user or app list page
     */
    public String getDataArray() {

        if(null == xAxis || xAxis.isEmpty()){
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");

        Iterator<Date> iter = xAxis.iterator();
        while (iter.hasNext()) {
            Date day = iter.next();
            Number value = data.get(day);

            if(null == value){
                builder.append("null,");
                continue;
            }

            if (value instanceof Double && false == Double.isNaN(value.doubleValue())) {
                Double originalValue = value.doubleValue();
                if(originalValue == Double.POSITIVE_INFINITY) {
                    value = maxY*10;
                }
                if(originalValue == Double.NEGATIVE_INFINITY) {
                    value = minY*10;
                }

                value = Math.round(value.doubleValue() * 100) / 100.0;

                if(Double.isInfinite(originalValue)){
                    builder.append(value).append(",");
                    continue;
                }
            }

            builder.append(Double.isNaN(value.doubleValue())?"null":value).append(",");
        }
        builder.replace(builder.length()-1, builder.length(), "]");
        return builder.toString();
    }
}
