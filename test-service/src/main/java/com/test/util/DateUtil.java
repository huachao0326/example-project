package com.test.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @program: Dam
 * @description: 时间工具类
 * @author: mao_xuanming
 * @create: 2019-10-22 01:12:11
 **/
public class DateUtil {

    public static final String DATE_FORMAT1 = "yyyy/MM/dd";

    public static final String DATE_FORMAT2 = "yyyyMMdd";

    public static final String DATE_FORMAT3 = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT4 = "yyyyMMddHHmmssSSS";

    public static final String DATE_FORMAT5 = "yyyy-MM-dd";

    public static final String DATE_FORMAT6 = "yyyy-MM-dd HH:mm:ss";

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 根据时间戳格式化日期
     *
     * @param timestamp
     * @return
     */
    public static String formatDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.format(new Date(Long.valueOf(timestamp)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return format.format(new Date());
        }
    }

    public static String formatPubDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.format(new Date(Long.valueOf(timestamp)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return format.format(new Date());
        }
    }

    public static String formatPubTime(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            return format.format(new Date(Long.valueOf(timestamp)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return format.format(new Date());
        }
    }

    /**
     * 根据时间转换时间戳
     *
     * @param dateStr
     * @return
     */
    public static Long getTimestamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse;
        try {
            parse = format.parse(dateStr);
            return parse.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return System.currentTimeMillis();

    }

    /**
     * 获取指定格式的Date
     *
     * @param date
     * @param format
     * @return
     */
    public static Date getDateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return stringToDate(sdf.format(date), format);
    }

    /**
     * 获取上一日 日期
     *
     * @return
     */
    public static Date getYesterday() {
        //获取今天的日期
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -1);
        //这是昨天
        Date yesterday = c.getTime();
        return yesterday;
    }

    /**
     * 获取下一日日期
     *
     * @return
     */
    public static Date getNextDate() {
        //获取今天的日期
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 1);
        //这是明天
        Date tomorrow = c.getTime();
        return tomorrow;
    }

    /**
     * 获取过去N天前日期
     *
     * @param days
     * @return
     */
    public static Date getLastNdate(int days) {
        //获取今天的日期
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, -days);
        //这是N天前日期
        Date lastNdate = c.getTime();
        return lastNdate;
    }

    /**
     * 获取去年的今天
     */
    public static Date getLastYearDay(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(date, pos)); // 设置为当前时间
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1); // 设置为上一个月
        return calendar.getTime();
    }

    /**
     * 获取制定日期过去N天前日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getLastNdate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -days);
        //这是N天前日期
        Date lastNdate = c.getTime();
        return lastNdate;
    }

    /**
     * 获取制定日期接下来N天日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getNextNdate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, +days);
        //这是N天前日期
        Date lastNdate = c.getTime();
        return lastNdate;
    }

    /**
     * 获取n小时之前的时间戳
     *
     * @param hour
     * @return
     */
    public Long getBeforeByHourTimestamp(int hour) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY - hour));
            return calendar.getTime().getTime();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY - 24));
            return calendar.getTime().getTime();
        }
    }

    //将GMT格式的时间转换成yyyy-MM-dd HH:mm:ss格式
    public static String changeGmtTimeToDateTime(String GMTTime) {
//		String str="/Date(1487053489965+0800)/";
        String datdString = GMTTime.replace("GMT", "").replaceAll("\\(.*\\)", "");
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss z", Locale.ENGLISH);
        Date dateTrans = null;
        try {
            dateTrans = format.parse(datdString);
            return new SimpleDateFormat("yyyy-MM-dd").format(dateTrans);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据时间Str转换时间
     *
     * @param dateStr
     * @return
     */
    public static Date getDateObj(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(dateStr);
    }

    /**
     * 格式类型分析
     *
     * @param dateStr
     * @param format
     * @return
     * @throws Exception
     */
    public static Date getDateUseFormat(String dateStr, String format) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysOfTwo(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (60 * 60 * 24 * 1000));
    }

    /**
     * @MethodName: getDate
     * @Description: 根据传入的格式获取时间
     * @Param: [formatStrDate]
     * @Return: java.util.Date
     **/
    public static Date getDate(String formatStrDate) {

        //创建SimpleDateFormat对象实例并定义好转换格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            // 注意格式需要与上面一致，不然会出现异常
            date = sdf.parse(formatStrDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 得到当前指定格式时间:yyyyMMddhhmmssSSS
     *
     * @return
     */
    public static String getCurrentTime(String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    /**
     * 得到当前指定格式时间:
     *
     * @return
     */
    public static Date getCurrentformatTime(String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        Calendar cal = Calendar.getInstance();
        return stringToDate(sdf.format(cal.getTime()), formatType);
    }

    /**
     * 得到当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    /**
     * 获取当前日期之后的日期
     *
     * @return
     */
    public static Long getAgoDate(Integer addDays) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, addDays);
        return cal.getTime().getTime();
    }


    /**
     * 字符串转换为Date类型
     *
     * @param dteValue
     * @param strFormat String 日期串
     * @return Date
     */
    public static String dateToString(Date dteValue, String strFormat) {
        if (dteValue == null) {
            return "";
        }
        SimpleDateFormat clsFormat = new SimpleDateFormat(strFormat);
        return clsFormat.format(dteValue);
    }


    /**
     * 将字符串转换为时间
     *
     * @param strValue String 字符串日期
     * @return Date
     */
    public static Date stringToDate(String strValue, String dateFormat) {
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }
        SimpleDateFormat clsFormat = new SimpleDateFormat(dateFormat);
        ParsePosition pos = new ParsePosition(0);
        return clsFormat.parse(strValue, pos);
    }

    /**
     * 功能描述:日期格式转换
     *
     * @param date
     * @param sourceFormat
     * @param destFormat
     * @return:java.lang.String
     * @since: 1.0.0
     * @Author:huachao
     * @Date: 2021/3/27 下午7:59
     */
    public static String formatTransfer(String date, String sourceFormat, String destFormat) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat source = new SimpleDateFormat(sourceFormat);
        Date parseDate = source.parse(date, pos);
        SimpleDateFormat dest = new SimpleDateFormat(destFormat);
        return dest.format(parseDate);
    }

    public static String gmtNow() {
        long epochNow = Instant.now().getEpochSecond();
        return Instant.ofEpochSecond(epochNow).atZone(ZoneId.of("Z")).format(DateTimeFormatter
                .ofPattern("uuuu-MM-dd'T'HH:mm:ss"));
    }

    /**
     * 根据日期计算年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(String birthDay) {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(stringToDate(birthDay, DateUtil.DATE_FORMAT5));

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

}
