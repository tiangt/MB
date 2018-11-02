package com.whzl.mengbi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 获取系统时间
     * 转换为yyyy-MM-dd HH:mm:ss格式
     *
     * @return
     */
    public static String getStringDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate = format.format(new Date());
        return newDate;
    }

    /**
     * 获取系统时间
     * 转换为yyyy-MM-dd格式
     *
     * @return
     */
    public static String getStringDateYMD() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = format.format(new Date());
        return newDate;
    }

    /**
     * 通过指定转换类型参数   返回String当前时间
     * 转换为默认格式
     *
     * @return
     */
    public static String getStringDate(String sdf) {
        SimpleDateFormat format = new SimpleDateFormat(sdf);
        String newDate = format.format(new Date());
        return newDate;
    }

    /**
     * 通过指定转换类型和日期参数 返回字符类型
     * 转换为默认格式
     *
     * @return
     */
    public static String getStringDate(String sdf, String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(sdf);
        String newDate = format.format(getDate(date));
        return newDate;

    }

    /**
     * 通过参数转换时间返回date类型
     * 转换为默认格式
     *
     * @return
     */
    public static Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(date);
    }


    /**
     * 作用：把日期转换成字符串
     *
     * @param d      被转换的日期对象
     * @param format 传递过来的要被转换的格式
     * @return 格式化之后的字符串
     */
    public static String dateToString(Date d, String format) {
//      SimpleDateFormat sdf = new SimpleDateFormat(format);
//      return sdf.format(d);
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * 作用：把一个字符串格式化成Date日期对象
     *
     * @param s      输入的字符串
     * @param format 按照该格式进行提取
     * @return 日期对象
     * @throws ParseException
     */
    public static Date stringToDate(String s, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(s);
    }

    //可根据需要自行截取数据显示
    //yyyy-MM-dd
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //可根据需要自行截取数据显示
    //yyyy-MM-dd
    public static String getTime(String dateStr) {
        Date date = null;
        try {
            date = getDate(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getTime(date);
    }

    //可根据需要自行截取数据显示
    //yyyy-MM-dd HH:mm:ss
    public static String getTime2(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 日期转化为毫秒
     */
    public static long dateStrToMillis(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long millis = 0;
        try {
            millis = simpleDateFormat.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    /**
     * 判断日期是不是今天
     */
    public static boolean isToday(long timeMills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timeMills);
        String dateStr = simpleDateFormat.format(date);
        long nowTimeMills = System.currentTimeMillis();
        Date today = new Date(nowTimeMills);
        String todayStr = simpleDateFormat.format(today);
        return dateStr.equals(todayStr);
    }

    /**
     * 计算岁数
     */
    public static long getAge(String birthdayStr) {
        long millis = dateStrToMillis(birthdayStr);
        long currentMillis = System.currentTimeMillis();
        return (currentMillis - millis) / 1000 / 60 / 60 / 24 / 365;
    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * @Description: long类型转换成日期
     *
     * @param lo 毫秒数
     * @return String yyyy-MM-dd HH:mm:ss
     */
    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
        return sd.format(date);
    }
}
