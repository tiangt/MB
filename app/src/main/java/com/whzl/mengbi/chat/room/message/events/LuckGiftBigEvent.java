package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.LuckGiftBigJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;

public class LuckGiftBigEvent implements BroadEvent {
    private LuckGiftBigJson luckGiftBigJson;
    private Context mContext;

    public LuckGiftBigEvent(LuckGiftBigJson luckGiftBigJson, Context mContext) {
        super();
        this.luckGiftBigJson = luckGiftBigJson;
        this.mContext = mContext;
    }

    public LuckGiftBigJson getLuckGiftBigJson() {
        return luckGiftBigJson;
    }

    public Context getmContext() {
        return mContext;
    }

    public void showRunWay(TextView tvRunWayGift) throws Exception {
        tvRunWayGift.setText(LevelUtil.getImageResourceSpan(mContext, R.drawable.congratulation));
        tvRunWayGift.append(LightSpanString.getLightString("恭喜",
                Color.parseColor("#f0f0f0")));
        tvRunWayGift.append(LightSpanString.getLightString(luckGiftBigJson.context.nickname,
                Color.parseColor("#FF611B")));
        tvRunWayGift.append(LightSpanString.getLightString("送幸运礼物喜中",
                Color.parseColor("#f0f0f0")));

        long num = 0L;
        for (int i = 0; i < luckGiftBigJson.context.prizes.size(); i++) {
            LuckGiftBigJson.ContextBean.PrizesBean prizesBean = luckGiftBigJson.context.prizes.get(i);
            num = prizesBean.giftPrice * prizesBean.rewardRatio * prizesBean.times + num;
        }

        tvRunWayGift.append(LightSpanString.getLightString(String.valueOf(num) + "萌币",
                Color.parseColor("#FF611B")));
    }
}
