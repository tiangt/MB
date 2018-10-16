package com.whzl.mengbi.chat.room.message.events;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.BroadCastBottomJson;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.util.LogUtils;

public class BroadCastBottomEvent extends BroadEvent {
    private BroadCastBottomJson broadCastBottomJson;
    private Context mContext;
    private int programId;

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public BroadCastBottomEvent(BroadCastBottomJson broadCastBottomJson, Context mContext) {
        super();
        this.broadCastBottomJson = broadCastBottomJson;
        this.mContext = mContext;
    }

    public BroadCastBottomJson getBroadCastBottomJson() {
        return broadCastBottomJson;
    }

    public Context getmContext() {
        return mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showRunWay(TextView tvRunWayGift) throws Exception {
//        33edfe
        SpannableString lightString = LightSpanString.getLightString(broadCastBottomJson.context.nickname + ": ",
                Color.parseColor("#33edfe"));
        SpannableString lightString1 = LightSpanString.getLightString(broadCastBottomJson.context.message,
                Color.parseColor("#f0f0f0"));
        FaceReplace.getInstance().faceReplace(tvRunWayGift, lightString1, mContext);
        FaceReplace.getInstance().guardFaceReplace16(tvRunWayGift, lightString1, mContext);
//        SpannableStringBuilder string = new SpannableStringBuilder();

//        string.setSpan(new RoundBackgroundColorSpan(R.color.orange,R.color.white),0,string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        string.append(lightString).append(lightString1);
        tvRunWayGift.setText("");
        tvRunWayGift.append(lightString);
        tvRunWayGift.append(lightString1);

        tvRunWayGift.setOnClickListener(v -> {
            if (programId == broadCastBottomJson.context.programId) {
                return;
            }
            if (((LiveDisplayActivity) mContext).isFinishing()) {
                return;
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle(R.string.alert);
            dialog.setMessage(mContext.getString(R.string.jump_live_house, broadCastBottomJson.context.anchorNickname));
            dialog.setNegativeButton(R.string.cancel, null);
            dialog.setPositiveButton(R.string.confirm, (dialog1, which) -> {
                Intent intent = new Intent(mContext, LiveDisplayActivity.class);
                intent.putExtra(BundleConfig.PROGRAM_ID, broadCastBottomJson.context.programId);
                mContext.startActivity(intent);
                programId = broadCastBottomJson.context.programId;
            });
            dialog.show();
        });
    }
}