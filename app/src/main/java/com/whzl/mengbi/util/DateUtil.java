package com.whzl.mengbi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 获取系统时间
     * 转换为yyyy-MM-dd HH:mm:ss格式
     * @return
     */
    public static String getStringDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newDate=format.format(new Date());
        return newDate;
    }

    /**
     * 获取系统时间
     * 转换为yyyy-MM-dd格式
     * @return
     */
    public static String getStringDateYMD(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String newDate=format.format(new Date());
        return newDate;
    }

    /**
     * 通过指定转换类型参数   返回String当前时间
     * 转换为默认格式
     * @return
     */
    public static String getStringDate(String sdf){
        SimpleDateFormat format=new SimpleDateFormat(sdf);
        String newDate=format.format(new Date());
        return newDate;
    }

    /**
     * 通过指定转换类型和日期参数 返回字符类型
     * 转换为默认格式
     * @return
     */
    public static String getStringDate(String sdf,String date) throws ParseException{
        SimpleDateFormat format=new SimpleDateFormat(sdf);
        String newDate=format.format(getDate(date));
        return newDate;

    }
    /**
     * 通过参数转换时间返回date类型
     * 转换为默认格式
     * @return
     */
    public static Date getDate(String date) throws ParseException{
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        return  format.parse(date);
    }


    /**
     * 作用：把日期转换成字符串
     * @param d
     *          被转换的日期对象
     * @param format
     *          传递过来的要被转换的格式
     * @return 格式化之后的字符串
     */
    public static String dateToString(Date d, String format){
//      SimpleDateFormat sdf = new SimpleDateFormat(format);
//      return sdf.format(d);
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * 作用：把一个字符串格式化成Date日期对象
     * @param s 输入的字符串
     * @param format 按照该格式进行提取
     * @return 日期对象
     * @throws ParseException
     */
    public static Date stringToDate(String s, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(s);
    }

}
