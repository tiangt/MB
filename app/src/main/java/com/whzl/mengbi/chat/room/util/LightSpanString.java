package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.util.UIUtil;

public class LightSpanString {
    public static SpannableString getLightString(String content, int color) {
        SpannableString ss = new SpannableString(content);
        //设置字符颜色
        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString getNickNameSpan(Context context, final String nickName, final long uid, final int programId) {
        return LightSpanString.getNickNameSpan(context, nickName, uid, programId, Color.parseColor("#75bbfb"));
    }

    public static SpannableString getNickNameSpan(Context context, final String nickName, final long uid, final int programId, int color) {
        SpannableString nickSpan = new SpannableString(nickName);
        NickNameSpan clickSpan = new NickNameSpan(context, color) {
            @Override
            public void onClick(View widget) {
                if (uid <= 0) {
                    ((LiveDisplayActivity) context).showAudienceInfoDialog(nickName);
                    return;
                }
                Log.i("chatMsg", "点击了 " + nickName);
                ((LiveDisplayActivity) context).showAudienceInfoDialog(uid, false);
                //new EnterUserPop().enterUserPop(mContext,uid, ChatRoomInfo.getProgramId());
            }
        };

        nickSpan.setSpan(clickSpan, 0, nickSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return nickSpan;
    }

    public static SpannableString getPrettyNumSpan(Context context,String num, int bgColor, int textColor) {
        SpannableString spannableString = new SpannableString(num);
        RoundBackgroundColorSpan span = new RoundBackgroundColorSpan(context,bgColor, textColor, UIUtil.dip2px(context,1));
        spannableString.setSpan(span, 0, num.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString getPrettyNumBgSpan(Context context,String num, int bgColor, int textColor) {
        SpannableString spannableString = new SpannableString(num);
        RoundBackgroundColorSpan2 span = new RoundBackgroundColorSpan2(context,bgColor, textColor, UIUtil.dip2px(context,1));
        spannableString.setSpan(span, 0, num.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
