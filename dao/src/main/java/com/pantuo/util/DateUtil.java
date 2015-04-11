package com.pantuo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author tliu
 */
public class DateUtil {
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

    public static String toShortStr(long duration) {
        String str = null;
        if (duration < 3600)
            str = shortDf2.get().format(new Date(duration * 1000));
        else
            str = shortDf3.get().format(new Date(duration * 1000));
        return str.replace("@","'").replace(";","\"");
    }
}
