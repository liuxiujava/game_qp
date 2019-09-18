package x.utils;


import org.slf4j.Logger;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    final static Logger logger = org.slf4j.LoggerFactory.getLogger(DateUtil.class);
    private static final int[][] fields = new int[][]{{14}, {13}, {12}, {11, 10}, {5, 5, 9}, {2, 1001}, {1}, {0}};
    private static Date SERVER_START_DATE;
    private static Date SERVER_MERGE_DATE;
    private static Date ACTIVITY_START_DATE;

    /**
     * 标准格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String NORM_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DAY_FORMAT = "yyyy-MM-dd";

    private static final int[] dayArray = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    // private static SimpleDateFormat sdf = new SimpleDateFormat();
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;

    public static final long DayTime = 24 * 60 * 60 * 1000;
    public static final long ResetOffset = 10 * 60 * 1000;

    public static Calendar getCalendar() {
        return GregorianCalendar.getInstance();
    }

    public static void initServerStartDate(String startTime) {
        try {
            ACTIVITY_START_DATE = parseToDate(startTime, NORM_FORMAT);
            SERVER_START_DATE = getZeroClock(parseToDate(startTime, NORM_FORMAT));
        } catch (ParseException e) {
            logger.error("parse activityStart server date error, time: " + startTime);
        }
    }

    public static void initServerMergeDate(String mergeTime) {
        try {
            SERVER_MERGE_DATE = getZeroClock(parseToDate(mergeTime, NORM_FORMAT));
        } catch (ParseException e) {
            logger.error("parse merge server date error, time: " + mergeTime);
        }
    }

    public static Date getZeroClock(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat normalFormat = new SimpleDateFormat(NORM_FORMAT);
        return normalFormat.parse(format.format(date) + " 00:00:00");
    }

    /**
     * 定时任务执行时间差计算
     *
     * @param tday :设定什么时候执行
     * @return str:相隔多少分钟
     * @author addby achou.lau
     */
    public static int dateTaskByMinutes(Date tday) throws ParseException {
        String format = "yyyy-MM-dd HH:mm";

        // 精确到分钟
        SimpleDateFormat sd = new SimpleDateFormat(format);
        if (sd.format(tday).compareTo(sd.format(new Date())) < 0) {
//            tday = DateUtils.addDays(tday, 1);
        }

        return Math.abs(miDiff(tday.getTime()));
    }

    /**
     * 定时任务执行时间差计算
     *
     * @param tday :设定什么时候执行
     * @return str:相隔多少分钟
     * @author lsq
     */
    public static int dateTask(String tday) throws ParseException {
        int str = 0;
        // 精确到分钟
        SimpleDateFormat sd = new SimpleDateFormat(NORM_FORMAT);
        Date startIme;
        startIme = sd.parse(tday);
        if (startIme.before(new Date())) {
            startIme = addDays(startIme, 1);
        }
        str = miDiff(startIme.getTime());

        return Math.abs(str);
    }


    /**
     * 定时任务执行时间差计算
     *
     * @param tday :设定什么时候执行
     * @return str:相隔多少分钟
     * @author lsq
     */
    public static int dateTask(Date tday) throws ParseException {
        int str = 0;
        // 精确到分钟
        SimpleDateFormat sd = new SimpleDateFormat(NORM_FORMAT);
        //Date startIme;
        //startIme = sd.parse(tday);
        if (tday.before(new Date())) {
            tday = addDays(tday, 1);
        }
        str = miDiff(tday.getTime());
        return Math.abs(str);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    /**
     * 根据传入时间，计算与当前时间相差的分钟
     *
     * @param startTime
     * @return 两个时间相差的秒
     * @author lsq
     */
    public static int secDiff(long startTime) {
        // SimpleDateFormat sd = new SimpleDateFormat(NORM_FORMAT);
        long diff = getTimestamp().getTime() - startTime;
        long str = TimeUnit.MILLISECONDS.toSeconds(diff);
        return (int) str;
    }

    /**
     * 定时任务执行时间差计算
     *
     * @param tDay :设定什么时候执行
     * @return str:相隔多少秒
     * @author Jason.liu
     */
    public static int dateTaskForSecond(Date tDay) throws ParseException {
        if (tDay.before(new Date())) {
            tDay = addDays(tDay, 1);
        }
        return Math.abs(secDiff(tDay.getTime()));
    }

    /**
     * 定时任务执行时间差计算
     *
     * @param tDay :设定什么时候执行
     * @return str:相隔多少秒
     * @author Jason.liu
     */
    public static int dateByWeekTaskForSecond(Date tDay) throws ParseException {
        if (tDay.before(new Date())) {
            tDay = addDays(tDay, 7);
        }
        return Math.abs(secDiff(tDay.getTime()));
    }

    /**
     * 根据传入时间，计算两时间相差的分钟
     *
     * @param startTime :开始时间
     * @param endTime   :结束时间
     * @return 两个时间相差的分钟
     * @author lsq
     */
    public static int miDiff(Timestamp startTime, Timestamp endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        long str = TimeUnit.MILLISECONDS.toMinutes(diff);
        return (int) str;
    }

    public static int miDiff(long startTime, long endTime) {
        long diff = endTime - startTime;
        long str = TimeUnit.MILLISECONDS.toMinutes(diff);
        return (int) str;
    }

    /**
     * 根据传入时间，计算两时间相差多少天
     *
     * @param startTime :开始时间
     * @param endTime   :结束时间
     * @return 两个时间相差的天数
     * @author lsq
     */
    public static int dayDiff(Timestamp startTime, Timestamp endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        long str = TimeUnit.MILLISECONDS.toDays(diff);
        return (int) str;
    }

    public static int dayDiff(long startTime, long endTime) {
        long diff = endTime - startTime;
        long str = TimeUnit.MILLISECONDS.toDays(diff);
        return (int) str;
    }

    /**
     * 根据传入时间，计算与当前时间相差的分钟
     *
     * @param startTime
     * @return 两个时间相差的分钟
     * @author lsq
     */
    public static int miDiff(long startTime) {
        // SimpleDateFormat sd = new SimpleDateFormat(NORM_FORMAT);
        long diff = getTimestamp().getTime() - startTime;
        long str = TimeUnit.MILLISECONDS.toMinutes(diff);
        return (int) str;
    }

    /**
     * 计算两个日期相差多少天
     *
     * @param optime :日期
     * @return：相差天数
     * @author lsq
     */
    public static int dateMyDiff(Date optime) throws ParseException {
        int day = 0;
        String today = new SimpleDateFormat(NORM_FORMAT).format(new Date());// 获得系统当前时间
        SimpleDateFormat sd = new SimpleDateFormat(NORM_FORMAT);
        long diff;
        diff = sd.parse(today).getTime() - optime.getTime();
        day = (int) (diff / DAY);// 计算差多少天

        return day;
    }

    // 返回两日期差的小时数
    public static int DateDiffForHour(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / 3600 / 1000);
    }

    // 返回两日期相差的天数
    public static int DateDiffForDay(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / 3600 / 1000 / 24);
    }

    // 得到星期
    public static int getWeek(Date date, boolean startWithMonday) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (startWithMonday && week == 0)
            return week = 7;
        else
            return week;
    }

    /**
     * 获得系统当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
//    public static String getTime() {
//        return DateFormatUtils.format(new Date(), DateUtil.NORM_FORMAT);
//    }
//
//    /**
//     * Date 转 String yyyy-MM-dd HH:mm:ss
//     *
//     * @return
//     */
//    public static String DateToString(Date date) {
//        return DateFormatUtils.format(date, DateUtil.NORM_FORMAT);
//    }
//
//    /**
//     * 获取当前小时和分钟
//     *
//     * @return
//     */
//    public static String getHOURTime() {
//        return DateFormatUtils.format(new Date(), "HH:mm");
//    }

    /**
     * 同一个月
     *
     * @param date1
     * @param date2
     * @return
     * @author JackChu
     * @version 2011-7-28 下午02:58:26
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if ((date1 == null) || (date2 == null)) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && (cal1
                .get(Calendar.MONTH) == cal2.get(Calendar.MONTH)));
    }


    /**
     * 同一天
     *
     * @param date1
     * @param date2
     * @return
     * @author JackChu
     * @version 2011-7-28 下午02:58:26
     */
    public static boolean isSameday(Date date1, Date date2) {
        if ((date1 == null) || (date2 == null)) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }


    /**
     * 计算 月号 之间相差天数
     *
     * @param date1
     * @param date2
     */
    public static int getDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        //保证 cal2大于 cal1
        if (cal1.after(cal2)) {
            cal1 = cal2;
            cal2.setTime(date1);
        }
        int betweenYears = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        int betweenDays = cal2.get(Calendar.DAY_OF_YEAR) - cal1.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < betweenYears; i++) {
            cal1.set(Calendar.YEAR, (cal1.get(Calendar.YEAR) + 1));
            betweenDays += cal1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return betweenDays;
    }


    /**
     * 字符转换为日期型
     *
     * @param strdate
     * @return
     */
    public static Calendar getFormatDate(String strdate) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Calendar optime = null;
        sdf.applyPattern("yyyy-MM-dd");
        try {
            sdf.parse(strdate);
            optime = sdf.getCalendar();
        } catch (Exception e) {
        }
        return optime;
    }

    /**
     * @return String
     */
    public static String getDateMilliFormat() {
        Calendar cal = Calendar.getInstance();

        return getDateMilliFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static String getDateMilliFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static String getDateMilliFormat(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return getDateFormat(date, pattern);
    }

    /**
     * @return String
     */
    public static String getNormDate() {
        Date date = new Date();

        String pattern = NORM_FORMAT;
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarMilliFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static Date parseDateMilliFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static String getDateSecondFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateSecondFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static String getDateSecondFormat(Calendar cal) {
        String pattern = NORM_FORMAT;
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static String getDateSecondFormat(Date date) {
        String pattern = NORM_FORMAT;
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarSecondFormat(String strDate) {
        String pattern = NORM_FORMAT;
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static Date parseDateSecondFormat(String strDate) {
        String pattern = NORM_FORMAT;
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static String getDateMinuteFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateMinuteFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static String getDateMinuteFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd HH:mm";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static String getDateMinuteFormat(Date date) {
        String pattern = "yyyy-MM-dd HH:mm";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarMinuteFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static Date parseDateMinuteFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static String getDateDayFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateDayFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static String getDateDayFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static String getDateDayFormat(Date date) {
        String pattern = "yyyy-MM-dd";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarDayFormat(String strDate) {
        String pattern = "yyyy-MM-dd";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static Date parseDateDayFormat(String strDate) {
        String pattern = "yyyy-MM-dd";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static String getDateFileFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateFileFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static String getDateFileFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static String getDateFileFormat(Date date) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarFileFormat(String strDate) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static Date parseDateFileFormat(String strDate) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static String getDateW3CFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateW3CFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static String getDateW3CFormat(Calendar cal) {
        String pattern = NORM_FORMAT;
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static String getDateW3CFormat(Date date) {
        String pattern = NORM_FORMAT;
        return getDateFormat(date, pattern);
    }

    /**
     * 只显示到分钟
     *
     * @param date
     * @return
     */
    public static String getDateW3CFormatTomm(Date date) {
        String pattern = "yyyy-MM-dd HH:mm";
        return getDateFormat(date, pattern);
    }

    public static String getDateFormatTomm(Date date) {
        String pattern = "MM-dd";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarW3CFormat(String strDate) {
        String pattern = NORM_FORMAT;
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static Date parseDateW3CFormat(String strDate) {
        String pattern = NORM_FORMAT;
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @param cal
     * @return String
     */
    public static String getDateFormat(Calendar cal) {
        String pattern = NORM_FORMAT;
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static String getDateFormat(Date date) {
        String pattern = NORM_FORMAT;
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarFormat(String strDate) {
        String pattern = NORM_FORMAT;
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static Date parseDateFormat(String strDate) {
        String pattern = NORM_FORMAT;
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @param cal
     * @param pattern
     * @return String
     */
    public static String getDateFormat(Calendar cal, String pattern) {
        return getDateFormat(cal.getTime(), pattern);
    }

    /**
     * @param date
     * @param pattern
     * @return String
     */
    public static String getDateFormat(Date date, String pattern) {
        // (sdf) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        String str = null;
        sdf.applyPattern(pattern);
        str = sdf.format(date);
        return str;
        // }
    }

    /**
     * @param strDate
     * @param pattern
     * @return java.util.Calendar
     */
    public static Calendar parseCalendarFormat(String strDate, String pattern) {
        // (sdf) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Calendar cal = null;
        sdf.applyPattern(pattern);
        try {
            sdf.parse(strDate);
            cal = sdf.getCalendar();
        } catch (Exception e) {
        }
        return cal;
        // }
    }

    /**
     * @param strDate
     * @param pattern
     * @return java.util.Date
     */
    public static Date parseDateFormat(String strDate, String pattern) {
        // (sdf) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date date = null;
        sdf.applyPattern(pattern);
        try {
            date = sdf.parse(strDate);
        } catch (Exception e) {
        }
        return date;
        // }
    }

    public static int getLastDayOfMonth(int month) {
        if (month < 1 || month > 12) {
            return -1;
        }
        int retn = 0;
        if (month == 2) {
            if (isLeapYear()) {
                retn = 29;
            } else {
                retn = dayArray[month - 1];
            }
        } else {
            retn = dayArray[month - 1];
        }
        return retn;
    }

    public static int getLastDayOfMonth(int year, int month) {
        if (month < 1 || month > 12) {
            return -1;
        }
        int retn = 0;
        if (month == 2) {
            if (isLeapYear(year)) {
                retn = 29;
            } else {
                retn = dayArray[month - 1];
            }
        } else {
            retn = dayArray[month - 1];
        }
        return retn;
    }

    public static boolean isLeapYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    public static boolean isLeapYear(int year) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        if ((year % 400) == 0)
            return true;
        else if ((year % 4) == 0) {
            if ((year % 100) == 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    /**
     * 判断指定日期的年份是否是闰年
     *
     * @param date 指定日期。
     * @return 是否闰年
     */
    public static boolean isLeapYear(Date date) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        // int year = date.getYear();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        int year = gc.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    public static boolean isLeapYear(Calendar gc) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        int year = gc.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 得到指定日期的前一个工作日
     *
     * @param date 指定日期。
     * @return 指定日期的前一个工作日
     */
    public static Date getPreviousWeekDay(Date date) {
        {
            /**
             * 详细设计： 1.如果date是星期日，则减3天 2.如果date是星期六，则减2天 3.否则减1天
             */
            GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
            gc.setTime(date);
            return getPreviousWeekDay(gc);
            // switch ( gc.get( Calendar.DAY_OF_WEEK ) )
            // {
            // case ( Calendar.MONDAY ):
            // gc.add( Calendar.DATE, -3 );
            // break;
            // case ( Calendar.SUNDAY ):
            // gc.add( Calendar.DATE, -2 );
            // break;
            // default:
            // gc.add( Calendar.DATE, -1 );
            // break;
            // }
            // return gc.getTime();
        }
    }

    public static Date getPreviousWeekDay(Calendar gc) {
        {
            /**
             * 详细设计： 1.如果date是星期日，则减3天 2.如果date是星期六，则减2天 3.否则减1天
             */
            switch (gc.get(Calendar.DAY_OF_WEEK)) {
                case (Calendar.MONDAY):
                    gc.add(Calendar.DATE, -3);
                    break;
                case (Calendar.SUNDAY):
                    gc.add(Calendar.DATE, -2);
                    break;
                default:
                    gc.add(Calendar.DATE, -1);
                    break;
            }
            return gc.getTime();
        }
    }

    /**
     * 得到指定日期的后一个工作日
     *
     * @param date 指定日期。
     * @return 指定日期的后一个工作日
     */
    public static Date getNextWeekDay(Date date) {
        /**
         * 详细设计： 1.如果date是星期五，则加3天 2.如果date是星期六，则加2天 3.否则加1天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, 3);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, 2);
                break;
            default:
                gc.add(Calendar.DATE, 1);
                break;
        }
        return gc.getTime();
    }

    public static Calendar getNextWeekDay(Calendar gc) {
        /**
         * 详细设计： 1.如果date是星期五，则加3天 2.如果date是星期六，则加2天 3.否则加1天
         */
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, 3);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, 2);
                break;
            default:
                gc.add(Calendar.DATE, 1);
                break;
        }
        return gc;
    }

    /**
     * 取得指定日期的下一个月的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个月的最后一天
     */
    public static Date getLastDayOfNextMonth(Date date) {
        /**
         * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getLastDayOfMonth
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtil.getNextMonth(gc.getTime()));
        gc.setTime(DateUtil.getLastDayOfMonth(gc.getTime()));
        return gc.getTime();
    }

    /**
     * 取得指定日期的下一个星期的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个星期的最后一天
     */
    public static Date getLastDayOfNextWeek(Date date) {
        /**
         * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getLastDayOfWeek
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtil.getNextWeek(gc.getTime()));
        gc.setTime(DateUtil.getLastDayOfWeek(gc.getTime()));
        return gc.getTime();
    }

    /**
     * 取得指定日期的下一个月的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个月的第一天
     */
    public static Date getFirstDayOfNextMonth(Date date) {
        /**
         * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getFirstDayOfMonth
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtil.getNextMonth(gc.getTime()));
        gc.setTime(DateUtil.getFirstDayOfMonth(gc.getTime()));
        return gc.getTime();
    }

    public static Calendar getFirstDayOfNextMonth(Calendar gc) {
        /**
         * 详细设计： 1.调用getNextMonth设置当前时间 2.以1为基础，调用getFirstDayOfMonth
         */
        gc.setTime(DateUtil.getNextMonth(gc.getTime()));
        gc.setTime(DateUtil.getFirstDayOfMonth(gc.getTime()));
        return gc;
    }

    /**
     * 取得指定日期的下一个星期的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个星期的第一天
     */
    public static Date getFirstDayOfNextWeek(Date date) {
        /**
         * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getFirstDayOfWeek
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtil.getNextWeek(gc.getTime()));
        gc.setTime(DateUtil.getFirstDayOfWeek(gc.getTime()));
        return gc.getTime();
    }

    public static Calendar getFirstDayOfNextWeek(Calendar gc) {
        /**
         * 详细设计： 1.调用getNextWeek设置当前时间 2.以1为基础，调用getFirstDayOfWeek
         */
        gc.setTime(DateUtil.getNextWeek(gc.getTime()));
        gc.setTime(DateUtil.getFirstDayOfWeek(gc.getTime()));
        return gc;
    }

    /**
     * 取得指定日期的下一个月
     *
     * @param date 指定日期。
     * @return 指定日期的下一个月
     */
    public static Date getNextMonth(Date date) {
        /**
         * 详细设计： 1.指定日期的月份加1
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.MONTH, 1);
        return gc.getTime();
    }

    public static Calendar getNextMonth(Calendar gc) {
        /**
         * 详细设计： 1.指定日期的月份加1
         */
        gc.add(Calendar.MONTH, 1);
        return gc;
    }

    /**
     * 取得指定日期的下一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一天
     */
    public static Date getNextDay(Date date) {
        /**
         * 详细设计： 1.指定日期加1天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        return gc.getTime();
    }

    public static Calendar getNextDay(Calendar gc) {
        /**
         * 详细设计： 1.指定日期加1天
         */
        gc.add(Calendar.DATE, 1);
        return gc;
    }

    /**
     * 取得指定日期的下一个星期
     *
     * @param date 指定日期。
     * @return 指定日期的下一个星期
     */
    public static Date getNextWeek(Date date) {
        /**
         * 详细设计： 1.指定日期加7天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, 7);
        return gc.getTime();
    }

    public static Calendar getNextWeek(Calendar gc) {
        /**
         * 详细设计： 1.指定日期加7天
         */
        gc.add(Calendar.DATE, 7);
        return gc;
    }

    /**
     * 取得指定日期的所处星期的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处星期的最后一天
     */
    public static Date getLastDayOfWeek(Date date) {
        /**
         * 详细设计： 1.如果date是星期日，则加6天 2.如果date是星期一，则加5天 3.如果date是星期二，则加4天
         * 4.如果date是星期三，则加3天 5.如果date是星期四，则加2天 6.如果date是星期五，则加1天
         * 7.如果date是星期六，则加0天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.SUNDAY):
                gc.add(Calendar.DATE, 6);
                break;
            case (Calendar.MONDAY):
                gc.add(Calendar.DATE, 5);
                break;
            case (Calendar.TUESDAY):
                gc.add(Calendar.DATE, 4);
                break;
            case (Calendar.WEDNESDAY):
                gc.add(Calendar.DATE, 3);
                break;
            case (Calendar.THURSDAY):
                gc.add(Calendar.DATE, 2);
                break;
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, 1);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, 0);
                break;
        }
        return gc.getTime();
    }

    /**
     * 取得指定日期的所处星期的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处星期的第一天
     */
    public static Date getFirstDayOfWeek(Date date) {
        /**
         * 详细设计： 1.如果date是星期日，则减0天 2.如果date是星期一，则减1天 3.如果date是星期二，则减2天
         * 4.如果date是星期三，则减3天 5.如果date是星期四，则减4天 6.如果date是星期五，则减5天
         * 7.如果date是星期六，则减6天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.SUNDAY):
                gc.add(Calendar.DATE, 0);
                break;
            case (Calendar.MONDAY):
                gc.add(Calendar.DATE, -1);
                break;
            case (Calendar.TUESDAY):
                gc.add(Calendar.DATE, -2);
                break;
            case (Calendar.WEDNESDAY):
                gc.add(Calendar.DATE, -3);
                break;
            case (Calendar.THURSDAY):
                gc.add(Calendar.DATE, -4);
                break;
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, -5);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, -6);
                break;
        }
        return gc.getTime();
    }

    public static Calendar getFirstDayOfWeek(Calendar gc) {
        /**
         * 详细设计： 1.如果date是星期日，则减0天 2.如果date是星期一，则减1天 3.如果date是星期二，则减2天
         * 4.如果date是星期三，则减3天 5.如果date是星期四，则减4天 6.如果date是星期五，则减5天
         * 7.如果date是星期六，则减6天
         */
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.SUNDAY):
                gc.add(Calendar.DATE, 0);
                break;
            case (Calendar.MONDAY):
                gc.add(Calendar.DATE, -1);
                break;
            case (Calendar.TUESDAY):
                gc.add(Calendar.DATE, -2);
                break;
            case (Calendar.WEDNESDAY):
                gc.add(Calendar.DATE, -3);
                break;
            case (Calendar.THURSDAY):
                gc.add(Calendar.DATE, -4);
                break;
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, -5);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, -6);
                break;
        }
        return gc;
    }

    /**
     * 取得指定日期的所处月份的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处月份的最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        /**
         * 详细设计： 1.如果date在1月，则为31日 2.如果date在2月，则为28日 3.如果date在3月，则为31日
         * 4.如果date在4月，则为30日 5.如果date在5月，则为31日 6.如果date在6月，则为30日
         * 7.如果date在7月，则为31日 8.如果date在8月，则为31日 9.如果date在9月，则为30日
         * 10.如果date在10月，则为31日 11.如果date在11月，则为30日 12.如果date在12月，则为31日
         * 1.如果date在闰年的2月，则为29日
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.MONTH)) {
            case 0:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 1:
                gc.set(Calendar.DAY_OF_MONTH, 28);
                break;
            case 2:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 3:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 4:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 5:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 6:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 7:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 8:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 9:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 10:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 11:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        // 检查闰年
        if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY) && (isLeapYear(gc.get(Calendar.YEAR)))) {
            gc.set(Calendar.DAY_OF_MONTH, 29);
        }
        return gc.getTime();
    }

    public static Calendar getLastDayOfMonth(Calendar gc) {
        /**
         * 详细设计： 1.如果date在1月，则为31日 2.如果date在2月，则为28日 3.如果date在3月，则为31日
         * 4.如果date在4月，则为30日 5.如果date在5月，则为31日 6.如果date在6月，则为30日
         * 7.如果date在7月，则为31日 8.如果date在8月，则为31日 9.如果date在9月，则为30日
         * 10.如果date在10月，则为31日 11.如果date在11月，则为30日 12.如果date在12月，则为31日
         * 1.如果date在闰年的2月，则为29日
         */
        switch (gc.get(Calendar.MONTH)) {
            case 0:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 1:
                gc.set(Calendar.DAY_OF_MONTH, 28);
                break;
            case 2:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 3:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 4:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 5:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 6:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 7:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 8:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 9:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 10:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 11:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        // 检查闰年
        if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY) && (isLeapYear(gc.get(Calendar.YEAR)))) {
            gc.set(Calendar.DAY_OF_MONTH, 29);
        }
        return gc;
    }

    /**
     * 取得指定日期的所处月份的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处月份的第一天
     */
    public static Date getFirstDayOfMonth(Date date) {
        /**
         * 详细设计： 1.设置为1号
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        return gc.getTime();
    }

    public static Calendar getFirstDayOfMonth(Calendar gc) {
        /**
         * 详细设计： 1.设置为1号
         */
        gc.set(Calendar.DAY_OF_MONTH, 1);
        return gc;
    }

    /**
     * 将日期对象转换成为指定ORA日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
     *
     * @param theDate 将要转换为字符串的日期对象。
     * @param hasTime 如果返回的字符串带时间则为true
     * @return 转换的结果
     */
    public static String toOraString(Date theDate, boolean hasTime) {
        /**
         * 详细设计： 1.如果有时间，则设置格式为getOraDateTimeFormat()的返回值
         * 2.否则设置格式为getOraDateFormat()的返回值 3.调用toString(Date theDate, DateFormat
         * theDateFormat)
         */
        DateFormat theFormat;
        if (hasTime) {
            theFormat = getOraDateTimeFormat();
        } else {
            theFormat = getOraDateFormat();
        }
        return toString(theDate, theFormat);
    }

    /**
     * 将日期对象转换成为指定日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
     *
     * @param theDate 将要转换为字符串的日期对象。
     * @param hasTime 如果返回的字符串带时间则为true
     * @return 转换的结果
     */
    public static String toString(Date theDate, boolean hasTime) {
        /**
         * 详细设计： 1.如果有时间，则设置格式为getDateTimeFormat的返回值 2.否则设置格式为getDateFormat的返回值
         * 3.调用toString(Date theDate, DateFormat theDateFormat)
         */
        DateFormat theFormat;
        if (hasTime) {
            theFormat = getDateTimeFormat();
        } else {
            theFormat = getDateFormat();
        }
        return toString(theDate, theFormat);
    }

    /**
     * 标准日期格式
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    /**
     * 标准时间格式
     */
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    /**
     * 带时分秒的标准时间格式
     */
    //private static final SimpleDateFormat DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    /**
     * ORA标准日期格式
     */
    private static final SimpleDateFormat ORA_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    /**
     * ORA标准时间格式
     */
    private static final SimpleDateFormat ORA_DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");

    /**
     * 带时分秒的ORA标准时间格式
     */
    //private static final SimpleDateFormat ORA_DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 创建一个标准日期格式的克隆
     *
     * @return 标准日期格式的克隆
     */
    public static DateFormat getDateFormat() {
        /**
         * 详细设计： 1.返回DATE_FORMAT
         */
        SimpleDateFormat theDateFormat = (SimpleDateFormat) DATE_FORMAT.clone();
        theDateFormat.setLenient(false);
        return theDateFormat;
    }

    /**
     * 创建一个标准时间格式的克隆
     *
     * @return 标准时间格式的克隆
     */
    public static DateFormat getDateTimeFormat() {
        /**
         * 详细设计： 1.返回DATE_TIME_FORMAT
         */
        SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) DATE_TIME_FORMAT.clone();
        theDateTimeFormat.setLenient(false);
        return theDateTimeFormat;
    }

    /**
     * 创建一个标准ORA日期格式的克隆
     *
     * @return 标准ORA日期格式的克隆
     */
    public static DateFormat getOraDateFormat() {
        /**
         * 详细设计： 1.返回ORA_DATE_FORMAT
         */
        SimpleDateFormat theDateFormat = (SimpleDateFormat) ORA_DATE_FORMAT.clone();
        theDateFormat.setLenient(false);
        return theDateFormat;
    }

    /**
     * 创建一个标准ORA时间格式的克隆
     *
     * @return 标准ORA时间格式的克隆
     */
    public static DateFormat getOraDateTimeFormat() {
        /**
         * 详细设计： 1.返回ORA_DATE_TIME_FORMAT
         */
        SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) ORA_DATE_TIME_FORMAT.clone();
        theDateTimeFormat.setLenient(false);
        return theDateTimeFormat;
    }

    /**
     * 将一个日期对象转换成为指定日期、时间格式的字符串。 如果日期对象为空，返回一个空字符串，而不是一个空对象。
     *
     * @param theDate       要转换的日期对象
     * @param theDateFormat 返回的日期字符串的格式
     * @return 转换结果
     */
    public static String toString(Date theDate, DateFormat theDateFormat) {
        /**
         * 详细设计： 1.theDate为空，则返回"" 2.否则使用theDateFormat格式化
         */
        if (theDate == null)
            return "";
        return theDateFormat.format(theDate);
    }

    // 由java.util.Date到java.sql.Date的类型转换
    public static java.sql.Date getSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    // 返回一个日期字符串在星期中的顺序
    public static int getDateInWeek(String strDate) {
        DateFormat df = DateFormat.getDateInstance();
        try {
            df.parse(strDate);
            Calendar c = df.getCalendar();
            return c.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
        } catch (ParseException e) {
            return -1;
        }
    }

    // 一个日期上加上月
    public static String MonthAdd(Date startDate, int mm) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        calendar.add(Calendar.MONTH, mm);

        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern(NORM_FORMAT);
        String returnDate = d.format(calendar.getTime());

        return returnDate;

    }

    // 一个日期上加天数
    public static String DateAdd(String startDate, int dd) {
        DateFormat df = DateFormat.getDateInstance();
        Date date = new Date();
        try {
            date = df.parse(startDate);
        } catch (Exception ex) {
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, dd);

        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        String returnDate = d.format(calendar.getTime());

        return returnDate;

    }

    // 一个日期上加天数
    public static long DateAdd(Date date, int dd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, dd);

        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        return calendar.getTimeInMillis();
    }

    // 一个日期上加小时
    public static long HourAdd(Date date, int dd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.HOUR, dd);

        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        return calendar.getTimeInMillis();
    }

    // 一个日期上加分钟
    public static long MinuteAdd(Date date, int dd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MINUTE, dd);

        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd HH:mm:ss");
        return calendar.getTimeInMillis();
    }

    // 一个日期上加年
    public static long YearAdd(Date date, int dd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.YEAR, dd);

        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        return calendar.getTimeInMillis();
    }

    // 一个日期上加dd个工作日
    public static String DateAddWorkday(String startDate, int dd) {
        DateFormat df = DateFormat.getDateInstance();
        Date date = new Date();
        try {
            date = df.parse(startDate);
        } catch (Exception ex) {
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        while (dd > 0) {
            // Calendar.DAY_OF_WEEK返回的 1 是星期天
            if ((calendar.get(Calendar.DAY_OF_WEEK) == 1) || (calendar.get(Calendar.DAY_OF_WEEK) == 7)) {
                calendar.add(Calendar.DATE, 1);
            } else {
                calendar.add(Calendar.DATE, 1);
                dd = -1;
            }
        }

        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        String returnDate = d.format(calendar.getTime());

        return returnDate;

    }

    // 返回当前日期,类型为格式"yyyy-mm-dd"的字符串
    public static String getDate() {
        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        Date nowdate = new Date();
        String str_date = d.format(nowdate);
        return str_date;
    }

    // 返回指定日期,类型为格式"yyyy-mm-dd"的字符串
    public static String getDate(long time) {
        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern("yyyy-MM-dd");
        Date nowdate = new Date(time);
        String str_date = d.format(nowdate);
        return str_date;
    }

    public static String DateToStr(Date date) {
        return DateToStr(date, "yyyy-MM-dd");
    }

    public static String DateToStr(Date date, String format) {
        SimpleDateFormat d = new SimpleDateFormat();
        d.applyPattern(format);
        String str_date = d.format(date);
        return str_date;
    }

    // 返回指定年月的天数
    public static int getMonthDayNum(int year, int month) {
        if (month == 2) {
            return year % 400 != 0 && (year % 4 != 0 || year % 100 == 0) ? 28 : 29;
        }
        String SmallMonth = ",4,6,9,11,";
        return SmallMonth.indexOf(String.valueOf(String.valueOf((new StringBuffer(",")).append(String.valueOf(month))
                .append(",")))) < 0 ? 31 : 30;
    }

    public static int getYearMonthDate(String strDate, String style) {
        int year;
        int month;
        int day;
        int firstDash;
        int secondDash;
        if (strDate == null) {
            return 0;
        }
        firstDash = strDate.indexOf('-');
        secondDash = strDate.indexOf('-', firstDash + 1);
        if ((firstDash > 0) & (secondDash > 0) & (secondDash < strDate.length() - 1)) {
            year = Integer.parseInt(strDate.substring(0, firstDash));
            month = Integer.parseInt(strDate.substring(firstDash + 1, secondDash));
            day = Integer.parseInt(strDate.substring(secondDash + 1));
        } else {
            return 0;
        }
        if (style.equalsIgnoreCase("Y")) {
            return year;
        } else if (style.equalsIgnoreCase("M")) {
            return month;
        } else if (style.equalsIgnoreCase("D")) {
            return day;
        } else {
            return 0;
        }
    }

    /**
     * @param today
     * @param tomorrow
     * @return
     * @author JackChu
     * @version 2011-4-11 下午04:07:01
     */
    public static boolean isTomorrow(Date today, Date tomorrow) {
        if (today == null || tomorrow == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(today);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(tomorrow);

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && (cal1
                .get(Calendar.DAY_OF_YEAR) - cal2.get(Calendar.DAY_OF_YEAR) == -1));

    }

    public static boolean isToday(Date day) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && (cal1
                .get(Calendar.DAY_OF_YEAR) - cal2.get(Calendar.DAY_OF_YEAR) == 0));

    }

    //获取当前时间Timestamp格式
    public static Timestamp getTimestamp() {
        // try {
        // java.text.SimpleDateFormat myFormat = new SimpleDateFormat(
        // NORM_FORMAT);
        // java.util.Calendar calendar = java.util.Calendar.getInstance();
        // String mystrdate = myFormat.format(calendar.getTime());
        // return java.sql.Timestamp.valueOf(mystrdate);
        // } catch (Exception e) {
        // return null;
        // }
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getTimestamp(String dax) {
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat(NORM_FORMAT);
            String mystrdate = myFormat.format(myFormat.parse(dax));
            return Timestamp.valueOf(mystrdate);
        } catch (Exception e) {
            return null;
        }
    }


    public static Date StrToDate(String strDate) {
        int year;
        int month;
        int day;
        int firstDash;
        int secondDash;
        if (strDate == null) {
            return null;
        }
        firstDash = strDate.indexOf('-');
        secondDash = strDate.indexOf('-', firstDash + 1);
        if ((firstDash > 0) & (secondDash > 0) & (secondDash < strDate.length() - 1)) {
            year = Integer.parseInt(strDate.substring(0, firstDash));
            month = Integer.parseInt(strDate.substring(firstDash + 1, secondDash));
            day = Integer.parseInt(strDate.substring(secondDash + 1));
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            return c.getTime();
        }
        return null;
    }

    // 得到当前时间最靠近的一个工作日（向前）
    public static String getWordDay(String date) {

        if (date.equals("2007-10-01") || date.equals("2007-10-02") || date.equals("2007-10-03")
                || date.equals("2007-10-04") || date.equals("2007-10-05") || date.equals("2007-10-06")
                || date.equals("2007-10-07") || date.equals("2007-10-08")) {

            return "2007-9-28";

        } else {
            int x = getDateInWeek(date);
            if (x == 6 || x == 7) {
                return DateAdd(date, 5 - x);
            } else {
                String temp = DateAdd(date, -1);
                int y = getDateInWeek(temp);
                if (y < 6) {
                    return temp;
                } else {
                    return getWordDay(temp);
                }
            }
        }

    }

    // 得到当前时间最靠近的一个星期五（向前）
    public static String getFriDay(String date) {

        if (date.equals("2007-10-06") || date.equals("2007-10-07") || date.equals("2007-10-08")
                || date.equals("2007-10-09") || date.equals("2007-10-10") || date.equals("2007-10-11")
                || date.equals("2007-10-12")) {

            return "2007-09-28";

        } else {
            int x = getDateInWeek(date);
            if (x == 6 || x == 7) {
                return DateAdd(date, 5 - x);
            } else {
                return DateAdd(date, -(2 + x));
            }
        }
    }

    public static String getLastWorkDayInLastMonth(String date) {

        Date d1 = getFirstDayOfMonth(parseDateDayFormat(date));

        String l_date = DateAdd(DateUtil.getDateDayFormat(d1), -1);
        int x = getDateInWeek(l_date);
        if (x == 6 || x == 7) {
            return DateAdd(l_date, 5 - x);
        } else {
            return l_date;
        }

    }

    // 得到当前时间最靠近的一个工作日（向前）
    public static boolean checkWordDay(String date) {

        if (date.equals("2007-10-01") || date.equals("2007-10-02") || date.equals("2007-10-03")
                || date.equals("2007-10-04") || date.equals("2007-10-05") || date.equals("2007-10-06")
                || date.equals("2007-10-07") || date.equals("2007-10-08")) {
            return false;
        } else {
            int x = getDateInWeek(date);
            if (x == 0 || x == 6) {
                return false;
            } else {
                return true;
            }
        }

    }

    public static Date parseStrToTime(String timeStr) throws NumberFormatException {
        String timeStr1 = timeStr.trim();
        StringTokenizer st = new StringTokenizer(timeStr1, "- / : .", false);
        String timeStrArray[] = new String[10];
        Date currentDate = null;
        try {
            int dataCount = st.countTokens();
            for (int i = 0; i < dataCount; i++)
                timeStrArray[i] = st.nextToken().trim();

            if (dataCount < 6 && dataCount >= 3) {
                int part1 = Integer.parseInt(timeStrArray[0]);
                int month = Integer.parseInt(timeStrArray[1]);
                int part3 = Integer.parseInt(timeStrArray[2]);
                int year = 0;
                int day = 0;
                if (part1 > 1000) {
                    year = part1;
                    day = part3;
                } else {
                    year = part3;
                    day = part1;
                }
                Calendar mCalendar = new GregorianCalendar(year, month - 1, day);
                currentDate = mCalendar.getTime();
            } else if (dataCount == 6) {
                int part1 = Integer.parseInt(timeStrArray[0]);
                int month = Integer.parseInt(timeStrArray[1]);
                int part3 = Integer.parseInt(timeStrArray[2]);
                int year = 0;
                int day = 0;
                if (part1 > 1000) {
                    year = part1;
                    day = part3;
                } else {
                    year = part3;
                    day = part1;
                }
                Calendar mCalendar = new GregorianCalendar(year, month - 1, day, Integer.parseInt(timeStrArray[3]),
                        Integer.parseInt(timeStrArray[4]), Integer.parseInt(timeStrArray[5]));
                currentDate = mCalendar.getTime();
            } else {
                throw new NumberFormatException((new StringBuilder("'")).append(timeStr)
                        .append("' is a invalid date format").toString());
            }
        } catch (NoSuchElementException e) {
            return null;
        } catch (NumberFormatException ne) {
            throw new NumberFormatException((new StringBuilder("'")).append(timeStr)
                    .append("' is a invalid date format").toString());
        }
        return currentDate;
    }

    public static String parseTimeToStr(Date aTime) {
        String MonStr = null;
        String DayStr = null;
        String HourStr = null;
        String MinStr = null;
        String SecStr = null;
        Calendar m_Calendar = Calendar.getInstance();
        m_Calendar.setTime(aTime);
        int Year = m_Calendar.get(1);
        int Mon = m_Calendar.get(2) + 1;
        if (Mon < 10)
            MonStr = (new StringBuilder("0")).append(Integer.toString(Mon)).toString();
        else
            MonStr = Integer.toString(Mon);
        int Day = m_Calendar.get(5);
        if (Day < 10)
            DayStr = (new StringBuilder("0")).append(Integer.toString(Day)).toString();
        else
            DayStr = Integer.toString(Day);
        int Hour = m_Calendar.get(11);
        if (Hour < 10)
            HourStr = (new StringBuilder("0")).append(Integer.toString(Hour)).toString();
        else
            HourStr = Integer.toString(Hour);
        int Min = m_Calendar.get(12);
        if (Min < 10)
            MinStr = (new StringBuilder("0")).append(Integer.toString(Min)).toString();
        else
            MinStr = Integer.toString(Min);
        int Sec = m_Calendar.get(13);
        if (Sec < 10)
            SecStr = (new StringBuilder("0")).append(Integer.toString(Sec)).toString();
        else
            SecStr = Integer.toString(Sec);
        return (new StringBuilder(String.valueOf(Integer.toString(Year)))).append("-").append(MonStr).append("-")
                .append(DayStr).append(" ").append(HourStr).append(":").append(MinStr).append(":").append(SecStr)
                .toString();
    }

    public static Date parseToDate(String aDate, String pattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        date = df.parse(aDate);

        return date;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date1).equals(df.format(date2));
    }

    /**
     * 时间在前个日期之间
     *
     * @return
     */
    public static boolean betweenTwoDay(Date date, Date date1, Date date2) {
        if (date.getTime() >= date1.getTime() && date.getTime() < date2.getTime()) {
            return true;
        }

        return false;
    }


    /**
     * 时间在当前日的[beginHour,endHour)小时区间
     *
     * @param date
     * @param beginHour
     * @param endHour
     * @return
     * @author JackChu
     * @version 2011-4-7 下午02:39:56
     */
    public static boolean betweenTodayHours(Date date, int beginHour, int endHour) {
        if (beginHour > endHour) {
            throw new IllegalArgumentException("beginHour must small than endHour");
        }
        if (date == null) {
            return false;
        }
        if (!isSameDay(date, new Date())) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= beginHour && hour < endHour) {
            return true;
        }
        return false;
    }

    /**
     * 今天已经过了几秒
     *
     * @return
     * @author JackChu
     * @version 2011-4-12 上午09:51:27
     */
    public static int todayHasGoneSeconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar = truncate(calendar, Calendar.HOUR);
        return (int) (new Date().getTime() - calendar.getTime().getTime()) / 1000;
    }

    public static Calendar truncate(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar truncated = (Calendar) date.clone();
            modify(truncated, field, 0);
            return truncated;
        }
    }

    private static void modify(Calendar val, int field, int modType) {
        if (val.get(1) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        } else if (field != 14) {
            Date date = val.getTime();
            long time = date.getTime();
            boolean done = false;
            int millisecs = val.get(14);
            if (0 == modType || millisecs < 500) {
                time -= (long) millisecs;
            }

            if (field == 13) {
                done = true;
            }

            int seconds = val.get(13);
            if (!done && (0 == modType || seconds < 30)) {
                time -= (long) seconds * 1000L;
            }

            if (field == 12) {
                done = true;
            }

            int minutes = val.get(12);
            if (!done && (0 == modType || minutes < 30)) {
                time -= (long) minutes * 60000L;
            }

            if (date.getTime() != time) {
                date.setTime(time);
                val.setTime(date);
            }

            boolean roundUp = false;

            for (int i = 0; i < fields.length; ++i) {
                int offset;
                for (offset = 0; offset < fields[i].length; ++offset) {
                    if (fields[i][offset] == field) {
                        if (modType == 2 || modType == 1 && roundUp) {
                            if (field == 1001) {
                                if (val.get(5) == 1) {
                                    val.add(5, 15);
                                } else {
                                    val.add(5, -15);
                                    val.add(2, 1);
                                }
                            } else if (field == 9) {
                                if (val.get(11) == 0) {
                                    val.add(11, 12);
                                } else {
                                    val.add(11, -12);
                                    val.add(5, 1);
                                }
                            } else {
                                val.add(fields[i][0], 1);
                            }
                        }

                        return;
                    }
                }

                offset = 0;
                boolean offsetSet = false;
                switch (field) {
                    case 9:
                        if (fields[i][0] == 11) {
                            offset = val.get(11);
                            if (offset >= 12) {
                                offset -= 12;
                            }

                            roundUp = offset >= 6;
                            offsetSet = true;
                        }
                        break;
                    case 1001:
                        if (fields[i][0] == 5) {
                            offset = val.get(5) - 1;
                            if (offset >= 15) {
                                offset -= 15;
                            }

                            roundUp = offset > 7;
                            offsetSet = true;
                        }
                }

                if (!offsetSet) {
                    int min = val.getActualMinimum(fields[i][0]);
                    int max = val.getActualMaximum(fields[i][0]);
                    offset = val.get(fields[i][0]) - min;
                    roundUp = offset > (max - min) / 2;
                }

                if (offset != 0) {
                    val.set(fields[i][0], val.get(fields[i][0]) - offset);
                }
            }

            throw new IllegalArgumentException("The field " + field + " is not supported");
        }
    }


    public static Timestamp getCurrentDate() {
        return new Timestamp(truncate(Calendar.getInstance(), Calendar.DATE).getTime().getTime());
    }

    /**
     * 凌晨
     *
     * @param day
     * @return
     * @author JackChu
     * @version 2011-12-20 下午06:47:19
     */
    public static Date getMorning(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * day后的凌晨
     *
     * @param day
     * @return
     * @author JackChu
     * @version 2011-12-20 下午06:47:29
     */
    public static Date getMorningNextDay(Date day, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + n);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static int miDiff(Calendar start, Calendar end) {
        long diff = end.getTimeInMillis() - start.getTimeInMillis();
        long str = TimeUnit.MILLISECONDS.toMinutes(diff);
        return (int) str;
    }

    public static boolean betweenTwoDays(Calendar updateDate, Calendar currentDate, int hour) {
        int updateHour = updateDate.get(Calendar.HOUR_OF_DAY);
        if (updateHour < hour) {
            updateDate.set(Calendar.HOUR_OF_DAY, hour);
            return updateDate.before(currentDate);
        }
        return false;
    }

    // 获得当前日期与本周一相差的天数
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    // 获得相应周的周日的日期
    public static String getSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    public static void main(String[] arg) {        /*
     * Date date=parseDateFormat("2010-10-31 10:00:00.000","yyyy-MM-dd");
     * Date date2=parseDateFormat("2010-11-5 09:00:00.000","yyyy-MM-dd");
     *
     */

        //float a = 100;
        //float b = 90;
        //float c = b / a;

    }

    /**
     * 获取活动激活时间
     *
     * @return
     */
    public static long getStartActivityTime() {
        return ACTIVITY_START_DATE.getTime();
    }

    /**
     * 获取开服时间
     *
     * @return
     */
    public static long getStartServerTime() {
        return SERVER_START_DATE.getTime();
    }

    /**
     * 获取合服时间
     *
     * @return
     */
    public static long getMergeServerTime() {
        if (SERVER_MERGE_DATE == null) {
            return 0;
        }
        return SERVER_MERGE_DATE.getTime();
    }

    //获得当前已经开服多少天
    public static int getStartServerInterval() {
        return (int) ((new Date().getTime() - SERVER_START_DATE.getTime()) / DayTime);
    }

    //获得服务器开启时间后第n天的日期
    public static Date getIntervalStartServerDate(int interval, String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat normalFormat = new SimpleDateFormat(NORM_FORMAT);
        Date targetDate = new Date(SERVER_START_DATE.getTime() + 24 * 3600 * 1000 * interval);
        try {
            return normalFormat.parse(format.format(targetDate) + " " + time);
        } catch (ParseException e) {
            logger.error("getIntervalStartServerDate", e);
        }
        return null;
    }

    public static Date getNextWeekDate(Date now, int dayOfWeek, String time) {
        int today = getWeek(now, false);
        int interval = dayOfWeek - today;
        if (interval < 0) {
            interval += 7;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat normalFormat = new SimpleDateFormat(NORM_FORMAT);
        try {
            Date targetDate = new Date(getZeroClock(now).getTime() + 24 * 3600 * 1000 * interval);
            return normalFormat.parse(format.format(targetDate) + " " + time);
        } catch (ParseException e) {
            logger.error("getNextWeekDate", e);
        }
        return null;
    }

    public static Date getRestDate(String resetTimeStr) {
        Calendar calendar = DateUtil.getCalendar();
        Date currentDate = calendar.getTime();
        Date date = null;
        try {
            date = parseToDate(DateUtil.getDate(System.currentTimeMillis() + ResetOffset) + " " + resetTimeStr,
                    DateUtil.NORM_FORMAT);
        } catch (ParseException e) {
            logger.error("getRestDate", e);
        }
        if (currentDate.before(date)) {
            date = new Date(date.getTime() - 24 * 3600 * 1000);
        }
        return date;
    }

    /**
     * 获取当前某时刻date
     *
     * @param format 例如 22:00
     * @return
     */
    public static Date getDateByTimeFormat(String format) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String allFormat = sdf.format(now) + " " + format;
        SimpleDateFormat nf = new SimpleDateFormat(NORM_FORMAT);
        try {
            return nf.parse(allFormat);
        } catch (ParseException e) {
            logger.error("getDateByTimeFormat", e);
        }
        return now;
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static boolean isFirstDay() {
        return isSameDay(SERVER_START_DATE, new Date());
    }

    public static boolean isZeroClock(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) == 0 &&
                calendar.get(Calendar.MINUTE) == 0 &&
                calendar.get(Calendar.SECOND) == 0;
    }

    public static String getMonday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        calendar.add(Calendar.DATE, -dayOfWeek + 1);
        return DateToStr(calendar.getTime());
    }

    /*
     *
     */
    public static String getDateByWeek(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        int tmp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (tmp == 0) {
            tmp = 7;
        }
        calendar.add(Calendar.DATE, -tmp + dayOfWeek);
        return DateToStr(calendar.getTime());
    }

    //获取当前时间零点的time
    public static long getThisZeroTime() {
        String date = DateUtil.getDateDayFormat(new Date());
        Date endTime = DateUtil.parseDateMinuteFormat(date + " 23:59:59");
        return endTime.getTime();
    }
}
