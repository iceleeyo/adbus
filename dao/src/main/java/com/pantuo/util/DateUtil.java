package com.pantuo.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author tliu
 */
public class DateUtil {
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static Calendar newCalendar() {
        return GregorianCalendar.getInstance(UTC);
    }

    public static SimpleDateFormat newSimpleDateFormat(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(UTC);
        return sdf;
    }
    public static Date trimDateDefault(Date date) { 
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    public static Date trimDate(Date date) {
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date dateAdd(Date date, int days) {
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    public static Date secondAdd(Date date, int second) {
    	Calendar cal = DateUtil.newCalendar();
    	cal.setTime(date);
    	cal.add(Calendar.SECOND, second);
    	return cal.getTime();
    }
    public static Date hourAdd(Date date, int second) {
    	Calendar cal = DateUtil.newCalendar();
    	cal.setTime(date);
    	cal.add(Calendar.HOUR, second);
    	return cal.getTime();
    }
    public static long getQuot(Date date1, Date date2){
    	  long quot = 0;
    	  try {
    	   quot = date1.getTime() - date2.getTime();
    	   quot = quot / 1000 / 60 / 60 / 24;
    	  } catch (Exception e) {
    	   e.printStackTrace();
    	  }
    	  return quot;
    	 }
    public static int[] getYearAndMonthAndHour(Date d) {
        if (d == null)
            return new int[] {-1, -1, -1};

        Calendar c = newCalendar();
        c.setTime(d);
        return new int[] {c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.HOUR_OF_DAY)};
    }

    //计算duration分别在不同小时内有多长时间(S)
    public static Map<Integer, Long> durationSpan (Calendar cal, Date start, long duration) {
        Map<Integer, Long> result = new HashMap<Integer, Long>();
        cal.setTime(start);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        cal.add(Calendar.SECOND, (int)duration);
        int hour2 = cal.get(Calendar.HOUR_OF_DAY);
        //not cross two hours
        if (hour == hour2) {
            result.put(hour, duration);
        } else {
            cal.setTime(start);
            cal.add(Calendar.HOUR_OF_DAY, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long inHour1 = ((cal.getTime().getTime() - start.getTime())/1000);
            long inHour2 = duration - inHour1;

            result.put(hour, inHour1);
            result.put(hour2, inHour2);
        }
        return result;
    }

    public static int getDaysInMonth(int year, int month) {
        Calendar c = newCalendar();
        c.set(year, month - 1, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static final ThreadLocal<SimpleDateFormat> shortDf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf;
        }
    };
    public static final ThreadLocal<SimpleDateFormat> shortDf2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("m@ss;");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf;
        }
    };
    public static final ThreadLocal<SimpleDateFormat> shortDf3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm@ss;");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf;
        }
    };


    public static final ThreadLocal<SimpleDateFormat> longDf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return newSimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> longDf2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return newSimpleDateFormat("MM-dd");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> longDf3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return newSimpleDateFormat("yyyyMMdd");
        }
    };
    public static final ThreadLocal<SimpleDateFormat> longDf4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return newSimpleDateFormat("MMdd");
        }
    };

    public static final ThreadLocal<SimpleDateFormat> monthDf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return newSimpleDateFormat("yyyy-MM");
        }
    };


    public static String toShortStr(long duration) {
        String str = null;
        if (duration < 3600)
            str = shortDf2.get().format(new Date(duration * 1000));
        else
            str = shortDf3.get().format(new Date(duration * 1000));
        return str.replace("@","'").replace(";","\"");
    }
    public static String toShortStr2(long duration) {
        String str = null;
        if (duration < 3600) {
            int m = (int) (duration / 60);
            double s = (duration % 60) / 60.0;

            str = String.valueOf(m + s);
        } else {
            int h = (int) (duration / 3600);
            double m = (int) (duration % 3600) / 3600.0;
            str = String.valueOf(h + m);
        }
        return str;
    }
}
