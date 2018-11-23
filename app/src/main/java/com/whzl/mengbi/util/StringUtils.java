package com.whzl.mengbi.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shaw
 * @date 17/7/15
 */
public class StringUtils {

    public static final String PHONE_REGEX = "^1+[1234567890]+\\d{9}";
    public static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    public static final String NUMBAER = "^[0-9]*$";

    public static final boolean isPhone(String phone) {
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        return matcher.matches();
    }

    public static String parseString(float f) {
        if (f % 1 == 0) {
            return String.valueOf((int) f);
        } else {
            return String.valueOf(f);
        }
    }

    public static boolean contains(String curProcessName, String s) {
        if (curProcessName == null || s == null) {
            return false;
        }

        if (curProcessName.indexOf(s) > 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(String flag, String saveValue) {
        if (flag == null) {
            return false;
        }
        if (flag.equals(saveValue)) {
            return true;
        }
        return false;
    }

    public static String split(List array, String split) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.size(); i++) {
            sb.append(array.get(i)).append(split);
        }
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }


    public static SpannableString spannableStringColor(String spanStr, int color) {
        if (TextUtils.isEmpty(spanStr))
            spanStr = "--";
        SpannableString string = new SpannableString(spanStr);
        string.setSpan(new ForegroundColorSpan(color), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static SpannableString spannableStringRelaSize(String spanStr, float proportion) {
        if (TextUtils.isEmpty(spanStr))
            spanStr = "--";
        SpannableString string = new SpannableString(spanStr);
        string.setSpan(new RelativeSizeSpan(proportion), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static SpannableString spannableStringAbsSize(String spanStr, int textSize) {
        if (TextUtils.isEmpty(spanStr))
            spanStr = "--";
        SpannableString string = new SpannableString(spanStr);
        string.setSpan(new AbsoluteSizeSpan(textSize, true), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static SpannableString spannableSpan(String spanStr, int textSize, int color) {//dp
        if (TextUtils.isEmpty(spanStr))
            spanStr = "--";
        SpannableString string = new SpannableString(spanStr);
        if (textSize != 0)
            string.setSpan(new AbsoluteSizeSpan(textSize, true), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (color != 0)
            string.setSpan(new ForegroundColorSpan(color), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile(NUMBAER);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 格式化数字，每3位数加","
     *
     * @param number
     * @return
     */
    public static String formatNumber(long number) {
        String firstStr = "";//第一个字符
        String middleStr = "";//中间字符
        if (number < 0) {
            firstStr = "-";
        } else if (number != 0 && number < 0.1) {
            return number + "";
        }
        String numberStr = Math.abs((long) number) + "";//取正

        int firstIndex = numberStr.length() % 3;
        int bitCount = numberStr.length() / 3;

        if (firstIndex > 0) {
            middleStr += numberStr.substring(0, firstIndex) + ",";
        }
        for (int i = 0; i < bitCount; i++) {
            middleStr += numberStr.substring(firstIndex + i * 3, firstIndex + i * 3 + 3) + ",";
        }
        if (middleStr.length() > 1) {
            middleStr = middleStr.substring(0, middleStr.length() - 1);
        }
        return firstStr + middleStr;
    }

}
