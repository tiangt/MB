package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;

public class PkEvent implements BroadEvent {
    private PkJson pkJson;
    private Context mContext;
    private int programId;

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public PkEvent(PkJson pkJson, Context mContext) {
        super();
        this.pkJson = pkJson;
        this.mContext = mContext;
    }

    public PkJson getPkJson() {
        return pkJson;
    }

    public Context getmContext() {
        return mContext;
    }

    public void showRunWay(TextView tvRunWayGift) throws Exception {
        //FF75BBFB
        tvRunWayGift.setText("");
        tvRunWayGift.setMovementMethod(LinkMovementMethod.getInstance());
        tvRunWayGift.append(LevelUtil.getImageResourceSpanByHeight(mContext, R.drawable.ic_pk_icon, 10));
        tvRunWayGift.append(" ");
        tvRunWayGift.append(LightSpanString.getLightString("恭喜 ", Color.parseColor("#f9f9f9")));
        tvRunWayGift.append("V".equals(pkJson.context.result) ?
                LightSpanString.getLightString(pkJson.context.launchPkUserNickname + " ", Color.parseColor("#FFFF538C")) :
                LightSpanString.getLightString(pkJson.context.pkUserNickname + " ", Color.parseColor("#FFFF538C"))
        );
        tvRunWayGift.append(LightSpanString.getLightString("在PK中战胜 ", Color.parseColor("#f9f9f9")));
        tvRunWayGift.append("V".equals(pkJson.context.result) ?
                LightSpanString.getLightString(pkJson.context.pkUserNickname + " ", Color.parseColor("#FFFF538C")) :
                LightSpanString.getLightString(pkJson.context.launchPkUserNickname + " ", Color.parseColor("#FFFF538C"))
        );
        tvRunWayGift.append(LightSpanString.getLightString("赢下一局。本场最佳MVP为 ", Color.parseColor("#f9f9f9")));
        tvRunWayGift.append(LightSpanString.getLightString(pkJson.context.mvpNickname, Color.parseColor("#FFF8C330")));
    }
}
