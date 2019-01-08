package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.PrizePoolFullJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.widget.view.WeekStarView;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.DateUtils;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class PrizePoolFullEvent implements WeekHeadEvent {
    public Context context;
    public PrizePoolFullJson headLineJson;

    public PrizePoolFullEvent(Context context, PrizePoolFullJson headLineJson) {
        this.context = context;
        this.headLineJson = headLineJson;
    }

    @Override
    public void init(WeekStarView tvEnter) {
        if (headLineJson != null) {
            PrizePoolFullJson.ContextBean bean = headLineJson.context;
            tvEnter.setText(LevelUtil.getImageResourceSpanByHeight(this.context, R.drawable.ic_happy_ball_live, 12));
            tvEnter.append(LightSpanString.getLightString(bean.periodNumber, Color.parseColor("#FFFFCB00")));
            tvEnter.append(LightSpanString.getLightString(" 期欢乐球奖池已达到", Color.parseColor("#ffffff")));
            tvEnter.append(LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(bean.prizePoolNumber), Color.parseColor("#FFFFCB00")));
            tvEnter.append(LightSpanString.getLightString("萌豆， ", Color.parseColor("#ffffff")));
            tvEnter.append(LightSpanString.getLightString(DateUtils.msToM(bean.countDownSecond), Color.parseColor("#FFFFCB00")));
            tvEnter.append(LightSpanString.getLightString(" 分钟投注正式开始，我要投注。", Color.parseColor("#ffffff")));
        }
    }
}
