package com.neuedu.busines.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {
    public static final String STANDER = "yyyy-MM-dd mm:HH:ss";

    /**
     * 将时间类型转为字符串类型
     * @param date
     * @return
     */
    public static String date2Str(Date date) {
        if (date == null) {
            return "";
        }
        DateTime dateTime = new DateTime();
        return dateTime.toString(STANDER);
    }

    public static String date2Str(Date date, String formate) {
        if (date == null) {
            return "";
        }
        DateTime dateTime = new DateTime();
        return dateTime.toString(formate);
    }

    /**
     * 将字符串类型转换为Date
     * @param strDate
     * @return
     */
    public static Date str2Date(String strDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDER);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
        return dateTime.toDate();
    }
}
