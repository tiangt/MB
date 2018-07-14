package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.ui.dialog.AudienceInfoDialog;

public class LightSpanString {
    public static SpannableString getLightString(String content, int color) {
        SpannableString ss = new SpannableString(content);
        //设置字符颜色
        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString getNickNameSpan(Context context, final String nickName, final int uid, final int programId){
        return LightSpanString.getNickNameSpan(context, nickName, uid, programId, Color.parseColor("#75bbfb"));
    }

    public static SpannableString getNickNameSpan(Context context, final String nickName, final int uid, final int programId, int color){
        SpannableString nickSpan = new SpannableString(nickName);
        NickNameSpan clickSpan = new NickNameSpan(context, color) {
            @Override
            public void onClick(View widget) {
                if (uid <= 0) {
                    return;
                }
                Log.i("chatMsg","点击了 "+nickName);
                AudienceInfoDialog.newInstance(uid, programId).show(((AppCompatActivity)context).getSupportFragmentManager());
                //new EnterUserPop().enterUserPop(mContext,uid, ChatRoomInfo.getProgramId());
            }
        };

        nickSpan.setSpan(clickSpan,0,nickSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return nickSpan;
    }
}
