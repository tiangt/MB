package com.whzl.mengbi.chat.room.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class LightSpanString {
    public static SpannableString getLightString(String content, int color) {
        SpannableString ss = new SpannableString(content);
        //设置字符颜色
        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
