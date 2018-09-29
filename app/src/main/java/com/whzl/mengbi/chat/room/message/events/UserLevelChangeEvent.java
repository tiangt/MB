package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.UserLevelChangeJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.util.ResourceMap;

public class UserLevelChangeEvent extends BroadEvent {
    private UserLevelChangeJson userLevelChangeJson;
    private Context mContext;

    public UserLevelChangeEvent(UserLevelChangeJson userLevelChangeJson, Context mContext) {
        super();
        this.userLevelChangeJson = userLevelChangeJson;
        this.mContext = mContext;
    }

    public UserLevelChangeJson getUserLeverChangeJson() {
        return userLevelChangeJson;
    }

    public Context getmContext() {
        return mContext;
    }

    public void showRunWay(TextView tvRunWayGift) throws Exception {
        //FF75BBFB
        tvRunWayGift.setText(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_update_braod));
        tvRunWayGift.append(LightSpanString.getLightString("恭喜",
                Color.parseColor("#f0f0f0")));
        tvRunWayGift.append(LightSpanString.getLightString(userLevelChangeJson.context.nickname,
                Color.parseColor("#FF75BBFB")));
        tvRunWayGift.append(LightSpanString.getLightString("富豪等级升级到",
                Color.parseColor("#f0f0f0")));
        for (int i = 0; i < userLevelChangeJson.context.levels.size(); i++) {
            if ("USER_LEVEL".equals(userLevelChangeJson.context.levels.get(i).levelType)) {
                tvRunWayGift.append(LightSpanString.getLightString(userLevelChangeJson.context.levels.get(i).levelName,
                        Color.parseColor("#f0f0f0")));
                tvRunWayGift.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(userLevelChangeJson.context.levels.get(i).levelValue)));
                break;
            }
        }

    }
}
