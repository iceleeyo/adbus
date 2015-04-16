package com.pantuo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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

    public static Date trimDate(Date date) {
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static int[] getYearAndMonth(Date d) {
        if (d == null)
            return new int[] {-1, -1};

        Calendar c = newCalendar();
        c.setTime(d);
        return new int[] {c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1};
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
}
