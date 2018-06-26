package com.whzl.mengbi.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class RegexUtils {

    /**
     * 正则表达式:验证手机号
     */
    public static final String REGEX_MOBILE="^(13[0-9]|14[57]|15[0-9]|16[56]|17[0134678]|18[0-9]|19[89])\\d{8}$";

    /**
     * 校验手机号
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        if (TextUtils.isEmpty(mobile))
            return false;
        else
            return mobile.matches(REGEX_MOBILE);
    }
}
