package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.FirstPrizeUserJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.widget.view.WeekStarView;
import com.whzl.mengbi.util.AmountConversionUitls;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class FirstPrizeUserEvent implements WeekHeadEvent {
    public Context context;
    public FirstPrizeUserJson headLineJson;

    public FirstPrizeUserEvent(Context context, FirstPrizeUserJson headLineJson) {
        this.context = context;
        this.headLineJson = headLineJson;
    }

    @Override
    public void init(WeekStarView tvEnter) {
        if (headLineJson != null) {
            FirstPrizeUserJson.ContextBean bean = headLineJson.context;
            tvEnter.setText(LevelUtil.getImageResourceSpanByHeight(this.context, R.drawable.ic_happy_ball_live, 12));
            tvEnter.append(LightSpanString.getLightString("恭喜 ", Color.parseColor("#ffffff")));
            for (int i = 0; i < bean.userNickNameList.size(); i++) {
                if (i != bean.userNickNameList.size() - 1) {
                    tvEnter.append(LightSpanString.getLightString(bean.userNickNameList.get(i) + "、", Color.parseColor("#FFFFCB00")));
                } else {
                    tvEnter.append(LightSpanString.getLightString(bean.userNickNameList.get(i), Color.parseColor("#FFFFCB00")));
                }
            }
            tvEnter.append(LightSpanString.getLightString("喜中特等奖，瓜分 ", Color.parseColor("#ffffff")));
            tvEnter.append(LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(bean.firstPrizeTotalValue),
                    Color.parseColor("#FFFFCB00")));
            tvEnter.append(LightSpanString.getLightString(" 萌豆。", Color.parseColor("#ffffff")));
        }
    }
}
