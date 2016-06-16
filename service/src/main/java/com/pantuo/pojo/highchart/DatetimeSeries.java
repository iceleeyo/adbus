/**
 * 
 */
package com.pantuo.pojo.highchart;

import com.pantuo.Reportable;
import com.pantuo.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


/**
 * @author tliu
 *
 */
public class DatetimeSeries<Y extends Reportable> extends Series<Date, Y> {

	private static final long serialVersionUID = -766803877214280915L;

	protected DatetimeSeries(final SeriesType seriesType, final DayList dayList) {
        super(seriesType, dayList.toDayList());
	}

    @Override
	/**
	 * Something like this:
	 * 
	 *          {x:Date.UTC(2010, 0, 1), y:29.9, meta:xxx},
     *          {x:Date.UTC(2010, 0, 2), y:71.5, meta:xxx},
     *          {x:Date.UTC(2010, 0, 3), y:106.4, meta:xxx},
     *          {x:Date.UTC(2010, 0, 6), y:129.2, meta:xxx},
     *          {x:Date.UTC(2010, 0, 7), y:144.0, meta:xxx},
     *          {x:Date.UTC(2010, 0, 8), y:176.0, meta:xxx},
	 */
	public String getData(String yKey) {

        if(null == xAxis || xAxis.isEmpty()){
            return "";
        }

        StringBuilder builder = new StringBuilder();

        Calendar cal = DateUtil.newCalendar();
        Iterator<Date> iter = xAxis.iterator();
        while (iter.hasNext()) {
            Date day = iter.next();
            Y yObj = data.get(day);

            cal.setTime(day);

            if(null == yObj){
                builder.append("{x:Date.UTC(")
                        .append(cal.get(Calendar.YEAR)).append(", ")
                        .append(cal.get(Calendar.MONTH)).append(", ")
                        .append(cal.get(Calendar.DATE)).append("), ")
                        .append("},\r\n");
                continue;
            }

            String xValue = "Date.UTC(" + cal.get(Calendar.YEAR) + ", " +
                    cal.get(Calendar.MONTH) + ", " + cal.get(Calendar.DATE) + ")";
            builder.append(yObj.toString(xValue, yKey)).append(",\r\n");
        }
            return builder.toString();
    }
}
