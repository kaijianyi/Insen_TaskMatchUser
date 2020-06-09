package com.insenuser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:时间工具类
 *
 * @author kjy
 * @since Apr 5, 2020 9:49:29 AM
 */
public class TimeUtils {
    // 常用转换格式
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转date
     * 
     * @param dateStr
     * @return
     */
    public static Date string2Date(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * date转字符串
     * 
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    /**
     * 判断时间是否在范围内
     * 
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
