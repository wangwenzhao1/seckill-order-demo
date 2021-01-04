package com.seckill.demo.demo.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.util.StringUtils;

/**
 * 时间格式转换工具
 *
 * @ClassName: TimeUtils
 * @version: V1.0
 */
public class TimeUtils {


    /**
     * string转换成日期格式<br>
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date str2Date(String date, String format) throws ParseException {
        if (StringUtils.isEmpty(format)) {
            // format = "YYYY-MM-DD HH:MM:SSZ";
            format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.parse(date);
    }

    /**
    * 字符串转换成日期
    * @param str
    * @return date
    */
    public static Date strToDate(String date, String format) throws ParseException {
    	if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
       return simpleDateFormat.parse(date);
    }

    /**
     * string转换成日期格式 （北京时间）<br>
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date str2DateOnUTC8(String date, String format) throws ParseException {
        if (StringUtils.isEmpty(format)) {
            // format = "YYYY-MM-DD HH:MM:SSZ";
            format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.parse(date);
    }

    /**
     * 日期间隔计算<br>
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 日期间隔值
     * @throws ParseException
     */
    public static int daysBetween(String begin, String end) throws ParseException {
        return daysBetween(str2Date(begin, ""), str2Date(end, ""));
    }
    /**
     * 日期间隔计算<br>
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 日期间隔值
     * @throws ParseException
     */
    public static int dayBetween(String begin, String end) throws ParseException {
    	return daysBetween(strToDate(begin, ""), strToDate(end, ""));
    }

    public static int daysBetween(Date begin, Date end) {
        Calendar calBegin = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(begin);
        calEnd.setTime(end);
        // 设置时间为0时
        calBegin.set(Calendar.HOUR_OF_DAY, 0);
        calBegin.set(Calendar.MINUTE, 0);
        calBegin.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        // 日期相差的天数
        int days = ((int) (calEnd.getTime().getTime() / 1000) - (int) (calBegin.getTime().getTime() / 1000)) / 3600
                / 24;

        return days;
    }

    /**
     * 返回时间long值<br>
     *
     * @param dateStr 时间字符串
     * @param formate 时间格式
     * @return
     * @throws ParseException
     */
    public static long paserL(String dateStr, String formate) throws ParseException {
        if (StringUtils.isEmpty(formate)) {
            formate = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        Date date = sdf.parse(dateStr);
        return date.getTime();
    }

    public static String long2Date(long dateTime, String format) throws ParseException {
        if (dateTime < 0.1) {
            return "";
        }
        TimeZone timeZone = null;
        if (StringUtils.isEmpty(format)) {
            // format = "YYYY-MM-DD HH:MM:SSZ";
            format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            timeZone = TimeZone.getTimeZone("UTC");
        } else {
            timeZone = TimeZone.getDefault();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(timeZone);
        Timestamp ss = new Timestamp(dateTime * 1000);
        return simpleDateFormat.format(ss);
    }

    public static String formatDate(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        String dateString = formatter.format(date);
        return dateString;
    }


    /**
     * getNowTime  获取当前时间的毫秒值
     *
     * @return int    返回类型
     * @throws
     */
    public static int getNowTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 根据日期获取当前时间年月日上午/下午時分字符串
     *
     * @return String
     */
    public static String getTimeStr(Date date) {

        Calendar ca = Calendar.getInstance();
        String amPmStr = "";
        ca.setTime(date);
        // 获取年份
        int year = ca.get(Calendar.YEAR);
        // 获取月份
        int month = ca.get(Calendar.MONTH) + 1;
        // 获取日
        int day = ca.get(Calendar.DATE);
        // 小时
        int hour = ca.get(Calendar.HOUR);
        // 分
        int minute = ca.get(Calendar.MINUTE);
        // 秒
        // int second=ca.get(Calendar.SECOND);
        // 上午或者下午
        int amPm = ca.get(Calendar.AM_PM);

        if (amPm == Calendar.AM) {
            amPmStr = "上午";
        } else if (amPm == Calendar.PM) {
            amPmStr = "下午";
        }
        return year + "年" + month + "月" + day + "日 " + amPmStr + hour + "时"
                + minute + "分";
    }

    /**
     * 指定时间+1天时间
     * getAppointDayEndTime
     * TODO(这里用一句话描述这个方法的作用)
     * @return
     * @throws ParseException    参数
     * long    返回类型
     */
    public static int getDayAddTime(Integer time) throws ParseException{
    	Calendar calendar = Calendar.getInstance();
    	Date date = new Date(time*1000L);
    	calendar.setTime(date);
    	calendar.add(Calendar.DATE,1);
    	return (int) (calendar.getTimeInMillis()/1000);
    }
    /**
     * 获取某月的第一天
     * @Title:getFirstDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     * @throws
     */
    public static String getFirstDayOfMonth(int year,int month)
    {
    	Calendar cal = Calendar.getInstance();
    	//设置年份
    	cal.set(Calendar.YEAR,year);
    	//设置月份
    	cal.set(Calendar.MONTH, month-1);
    	//获取某月最大天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
    	//设置天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
    	//格式化日期
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	//获取某月第一天
    	String firstDayOfMonth = sdf.format(cal.getTime());

    	return firstDayOfMonth;
    }

    /**
     * 获取当前月的第一天毫秒值
     *
     * @param year
     * @param month
     * @return
     */
    public static long getCurrentFirstDayOfMonth() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取某月第一天
        String firstDayOfMonth = sdf.format(cal.getTime());
        System.out.println(firstDayOfMonth);
        long paserL = 0;
        try {
            paserL = paserL(firstDayOfMonth, "yyyy-MM-dd") / 1000;
        } catch (ParseException e) {
            //
            e.printStackTrace();
        }

        return paserL;
    }
    /**
     * 获取某月的最后一天
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     * @throws
     */
    public static String getLastDayOfMonth(int year,int month)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 比较两个时间戳的相隔天数
     *
     * @param begin
     * @param end
     * @return
     */
    public static long timeStampBetween(Long begin, Long end) {
        Date dateBegin = new Date(begin);
        Date dateEnd = new Date(end);
        LocalDate beginLocalDate = dateBegin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = dateEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return endLocalDate.toEpochDay() - beginLocalDate.toEpochDay();
    }

    /**
     * 日期转换 时间戳 int -》 String
     * @param date
     * @param formatStr
     * @return
     */
    public static String formatIntToString(Date date) {
        String formatStr = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static String getTheYear() {
        Calendar date = Calendar.getInstance();
        return String.valueOf(date.get(Calendar.YEAR));
    }

    /**
     * 获取当前年的当前月1日毫秒值 13位
     *
     * @return
     */
    public static long getMonthOneDay() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String format2 = format.format(cale.getTime());
        long time = 0;
        try {
            time = format.parse(format2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static Date getDateTimeNow() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取当指定时间戳的是几日
     *
     * @param date
     *            10位时间戳
     * @return 返回几日
     */
    public static Integer getAppointedDay(long date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = new Date(date * 1000L);
        String format = simpleDateFormat.format(date1);
        Integer day = Integer.valueOf(format.substring(8, 10));
        return day;
    }

    /**
     * 根据时间戳 获取 当天的结束时间
     * @param time
     * @return
     * @throws ParseException
     */
    public static Integer getDayEndTime(Integer timestamps) throws ParseException {
        Long time=new Long(timestamps)*1000;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = s.format(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(s.parse(d));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return (int)(calendar.getTimeInMillis()/1000);
    }

    /**
     * 获取当指定时间戳的对象 年月日时分秒
     *
     * @param date
     *            13位时间戳
     * @return 返回几日
     */
    public static Calendar getCalendarByTime(long time) {

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time);

        // int year = calendar2.get(Calendar.YEAR);
        // int month = calendar2.get(Calendar.MONTH+1);
        // int day = calendar2.get(Calendar.DAY_OF_MONTH);
        // int hour = calendar2.get(Calendar.HOUR_OF_DAY);// 24小时制
        // 不用int hour = calendar2.get(Calendar.HOUR);//12小时制
        // int minute = calendar2.get(Calendar.MINUTE);
        // int second = calendar2.get(Calendar.SECOND);

        // System.out.println(year + "年" + "月" + day + "日" + hour + "时" + minute
        // + "分" + second + "秒");
        return calendar2;
    }



    /**
     * 查看两个时间戳是否是同一年月
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isSameMonth(long time1, long time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String sd1 = sdf.format(new Date(time1 * 1000L));
        String sd2 = sdf.format(new Date(time2 * 1000L));
        return sd1.equals(sd2);

    }




    /**
     * 生成消息编号
     * @return
     */
    public static String getMessageNum() {
        return String.format("scf%s%d","m",System.currentTimeMillis() / 100);
    }

}

