package com.pantuo.dao;

import com.pantuo.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by tliu on 10/15/14.
 */
public class Test {
    public static void main(String[] args) {
        Calendar cal = DateUtil.newCalendar();
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        System.out.println(cal.getTime());

        System.out.println(cal.getTimeInMillis());

        System.out.println(new Date(24710400000L + 1388534400000L));
    }
}
