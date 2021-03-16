package com.xhtt.hiddendangermaster.util;

import com.hg.zero.datetime.ZDateTimeUtils;

import java.util.Calendar;

/**
 * Created by Hollow Goods on 2019-04-15.
 */
public class DateUtils {

    public static String getSmartDate(long date) {

        if (isToday(date)) {
            return ZDateTimeUtils.getDateTimeString(date, ZDateTimeUtils.DateFormatMode.Time_HM);
        } else if (isYesterday(date)) {
            return "昨天 " + ZDateTimeUtils.getDateTimeString(date, ZDateTimeUtils.DateFormatMode.Time_HM);
        }

        return ZDateTimeUtils.getDateTimeString(date, ZDateTimeUtils.DateFormatMode.LINE_YMDHM);
    }

    /**
     * 是否是今天
     *
     * @param date long
     * @return boolean
     */
    public static boolean isToday(long date) {

        String str = ZDateTimeUtils.getDateTimeString(System.currentTimeMillis(), ZDateTimeUtils.DateFormatMode.LINE_YMD);
        long n = ZDateTimeUtils.getDateTimeLong(str, ZDateTimeUtils.DateFormatMode.LINE_YMD);

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(n);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTimeInMillis(n);
        tomorrow.add(Calendar.DATE, 1);

        return date >= today.getTimeInMillis() && date < tomorrow.getTimeInMillis();
    }

    /**
     * 是否是昨天
     *
     * @param date long
     * @return boolean
     */
    public static boolean isYesterday(long date) {

        String str = ZDateTimeUtils.getDateTimeString(System.currentTimeMillis(), ZDateTimeUtils.DateFormatMode.LINE_YMD);
        long n = ZDateTimeUtils.getDateTimeLong(str, ZDateTimeUtils.DateFormatMode.LINE_YMD);

        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(n);

        Calendar yesterday = Calendar.getInstance();
        yesterday.setTimeInMillis(n);
        yesterday.add(Calendar.DATE, -1);

        return date >= yesterday.getTimeInMillis() && date < today.getTimeInMillis();
    }

}
