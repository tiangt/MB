package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RobBigLuckyJson;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobBigRemindEvent implements RobLuckTotalEvent {
    private Context context;
    public RobBigLuckyJson robLuckJson;

    public RobBigRemindEvent(Context context, RobBigLuckyJson json) {
        this.robLuckJson = json;
        this.context = context;
    }

    @Override
    public void initTv(TextView tvEnter) {
        if (robLuckJson != null) {
            RobBigLuckyJson.ContextBean bean = robLuckJson.context;
            tvEnter.setMovementMethod(LinkMovementMethod.getInstance());
            tvEnter.setText("");
            tvEnter.append(LightSpanString.getLightString(bean.giftName,
                    ContextCompat.getColor(this.context, R.color.text_color_chat_yellow)));
            tvEnter.append(" 已累计到 ");
            tvEnter.append(LightSpanString.getLightString(String.valueOf(bean.robNumber), Color.parseColor("#25EDFF")));
            tvEnter.append(" 个，距开奖还差 ");
            tvEnter.append(LightSpanString.getLightString(String.valueOf(bean.totalNumber - bean.robNumber),
                    ContextCompat.getColor(this.context, R.color.text_color_chat_yellow)));
            tvEnter.append(" 次夺宝，");
            tvEnter.append(LightSpanString.getClickSpan(this.context, "现在去夺宝",
                    Color.parseColor("#FFFFFF26"), false, 9,
                    v -> ((LiveDisplayActivity) this.context).showSnatchDialog()));
        }
    }
}
