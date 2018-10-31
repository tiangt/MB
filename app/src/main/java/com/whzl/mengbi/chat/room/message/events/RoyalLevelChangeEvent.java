package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RoyalLevelChangeJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;

public class RoyalLevelChangeEvent extends BroadEvent {
    private RoyalLevelChangeJson royalLevelChangeJson;
    private Context mContext;

    public RoyalLevelChangeEvent(RoyalLevelChangeJson royalLevelChangeJson, Context mContext) {
        super();
        this.royalLevelChangeJson = royalLevelChangeJson;
        this.mContext = mContext;
    }

    public RoyalLevelChangeJson getRoyalLevelChangeJson() {
        return royalLevelChangeJson;
    }

    public Context getmContext() {
        return mContext;
    }

    public void showRunWay(TextView tvRunWayGift) throws Exception {
        //FF75BBFB
        tvRunWayGift.setText(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_update_braod));
        tvRunWayGift.append(LightSpanString.getLightString("恭喜",
                Color.parseColor("#f0f0f0")));
        tvRunWayGift.append(LightSpanString.getLightString(royalLevelChangeJson.context.nickname,
                Color.parseColor("#fece4d")));
        tvRunWayGift.append(LightSpanString.getLightString("升级到",
                Color.parseColor("#f0f0f0")));
        for (int i = 0; i < royalLevelChangeJson.context.levels.size(); i++) {
            if ("ROYAL_LEVEL".equals(royalLevelChangeJson.context.levels.get(i).levelType)) {
                tvRunWayGift.append(LightSpanString.getLightString(royalLevelChangeJson.context.levels.get(i).levelName,
                        Color.parseColor("#f0f0f0")));
//                tvRunWayGift.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getRoyalLevelIcon(royalLevelChangeJson.context.levels.get(i).levelValue)));
                if (royalLevelChangeJson.context.levels.get(i).levelValue > 0) {
                    tvRunWayGift.append(LevelUtil.getRoyalImageResourceSpan(mContext, royalLevelChangeJson.context.levels.get(i).levelValue, tvRunWayGift));
                }
                break;
            }
        }

    }
}
