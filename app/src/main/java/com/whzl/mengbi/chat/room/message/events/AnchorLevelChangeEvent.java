package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.AnchorLevelChangeJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.util.ResourceMap;

public class AnchorLevelChangeEvent extends BroadEvent {
    private AnchorLevelChangeJson anchorLevelChangeJson;
    private Context mContext;

    public AnchorLevelChangeEvent(AnchorLevelChangeJson anchorLevelChangeJson, Context mContext) {
        super();
        this.anchorLevelChangeJson = anchorLevelChangeJson;
        this.mContext = mContext;
    }

    public AnchorLevelChangeJson getAnchorLevelChangeJson() {
        return anchorLevelChangeJson;
    }

    public Context getmContext() {
        return mContext;
    }

    public void showRunWay(TextView tvRunWayGift) throws Exception {
        tvRunWayGift.setText(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_update_braod));
        tvRunWayGift.append(LightSpanString.getLightString("恭喜",
                Color.parseColor("#f0f0f0")));
        tvRunWayGift.append(LightSpanString.getLightString(anchorLevelChangeJson.context.nickname,
                Color.parseColor("#ff618a")));
        tvRunWayGift.append(LightSpanString.getLightString("主播等级升级到",
                Color.parseColor("#f0f0f0")));
        for (int i = 0; i < anchorLevelChangeJson.context.levels.size(); i++) {
            if ("ANCHOR_LEVEL".equals(anchorLevelChangeJson.context.levels.get(i).levelType)) {
                tvRunWayGift.append(LightSpanString.getLightString(anchorLevelChangeJson.context.levels.get(i).levelName,
                        Color.parseColor("#f0f0f0")));
                tvRunWayGift.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getAnchorLevelIcon(anchorLevelChangeJson.context.levels.get(i).levelValue)));
                break;
            }
        }
    }
}
