package com.job.util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 子羽
 * @date 2018年03月06日
 */
public class DateUtil {

    /**
     * 当前时间加几秒
     * @param startTime
     * @param seconds
     * @return
     */
    public static Date dateAddSeconds(Date startTime, int seconds){
        DateTime start =new DateTime(startTime);
        start=start.plusSeconds(seconds);
        return start.toDate();
    }

    /**
     * 当前时间加几分钟
     * @param startTime
     * @param minutes
     * @return
     */
    public static Date dateAddMinutes(Date startTime,int minutes){
        DateTime start =new DateTime(startTime);
        start=start.plusMinutes(minutes);
        return start.toDate();
    }

    /**
     * 当前时间加几小时
     * @param startTime
     * @param hour
     * @return
     */
    public static Date dateAddHours(Date startTime,int hour){
        DateTime start =new DateTime(startTime);
        start=start.plusHours(hour);
        return start.toDate();
    }

    /**
     * 当前时间加几天
     * @param startTime
     * @param day
     * @return
     */
    public static Date dateAddDays(Date startTime,int day){
        DateTime start =new DateTime(startTime);
        start=start.plusDays(day);
        return start.toDate();
    }


    /**
     * 计算两个时间差
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getDatePoor(long startTime, long endTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff =endTime - startTime;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒
         long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟"+sec+"秒";
    }

    /**
     * 获取当前日期格式yyyy-MM-dd
     * @return
     */
    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        return currentDate;
    }

    /**
     * 获取当前日期格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentDateTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = df.format(new Date());
        return currentTime;
    }

}
