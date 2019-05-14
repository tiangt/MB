package com.whzl.mengbi.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountConversionUitls {
    /**
     * 货币转换 1000000 return 1百万萌币 10000 return 1万萌币 1000 return 1千萌币
     *
     * @param amount
     * @return
     */
    public static HashMap<String, String> amountConversionMap(long amount) {
        HashMap<String, String> map = new HashMap<String, String>();

        if (amount >= 1000000) {
            map.put("amount", amount / 1000000 + "");
            map.put("amoun_unit", "百万萌币");
            return map;
        }

        if (amount >= 10000) {
            map.put("amount", amount / 10000 + "");
            map.put("amoun_unit", "万萌币");
            return map;
        }

        if (amount >= 1000) {
            map.put("amount", amount / 1000 + "");
            map.put("amoun_unit", "千萌币");
            return map;
        }

        map.put("amount", amount + "");
        map.put("amoun_unit", "萌币");
        return map;
    }

    /**
     * 1000000 return 1百万萌币 10000 return 1万萌币 1000 return 1千萌币
     *
     * @param amount
     * @return
     */
    public static String coinConversion(long amount) {
        if (amount >= 10000000) {
            return amount / 10000 + "万萌币";
        }
        if (amount >= 1000000) {
            return amount / 10000 + "万萌币";
        }

        if (amount >= 10000) {
            return amount / 10000 + "万萌币";
        }

        if (amount >= 1000) {
            return amount / 1000 + "千萌币";
        }

        return amount + "萌币";
    }

    public static String amountConversion(long amount) {
        return "" + amount / 100.0;
//        if(amount >= 10000000){
//            return ""+amount/100;
//        }else if(amount >= 1000000){
//            return ""+amount/100;
//        }else if(amount >= 100000){
//            return ""+amount/100;
//        }else if(amount >= 10000){
//            return ""+amount/100;
//        }else if(amount >= 1000){
//            return ""+amount/100;
//        }else {
//            return ""+amountConversionFormat(amount);
//        }
    }

    /**
     * 10000 return 10,000
     *
     * @param amount
     * @return
     */
    public static String amountConversionFormat(long amount) {
        return NumberFormat.getInstance().format(amount);
    }

    /**
     * 10000 return 10,000
     *
     * @param amount
     * @return
     */
    public static String amountConversionFormat(double amount) {
        return NumberFormat.getInstance().format(amount);
    }

    /**
     * 纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 纯字母
     *
     * @param data
     * @return
     */
    public static boolean isChar(String data) {
        {
            for (int i = data.length(); --i >= 0; ) {
                char c = data.charAt(i);
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }

    }

    public static boolean ispsd(String psd) {
        Pattern p = Pattern
                .compile("^[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]");
        Matcher m = p.matcher(psd);

        return m.matches();
    }
}
