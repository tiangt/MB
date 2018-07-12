package com.whzl.mengbi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author shaw
 * @date 2018/7/12
 */
public class DateUtil {

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
     * 计算岁数
     */
    public static long getAge(String birthdayStr) {
        long millis = dateStrToMillis(birthdayStr);
        long currentMillis = System.currentTimeMillis();
        return (currentMillis - millis) / 1000 / 60 / 60 / 24 / 365;
    }

}
