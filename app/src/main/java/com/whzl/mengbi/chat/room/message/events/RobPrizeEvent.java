package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RobLuckJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.chat.room.util.RobSpan;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.widget.view.WeekStarView;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobPrizeEvent implements WeekHeadEvent {
    private Context context;
    public RobLuckJson robLuckJson;

    public RobPrizeEvent(Context context, RobLuckJson json) {
        this.robLuckJson = json;
        this.context = context;
    }

    @Override
    public void init(WeekStarView tvEnter) {
        if (robLuckJson != null) {
            RobLuckJson.ContextBean bean = robLuckJson.context;
            tvEnter.setMovementMethod(LinkMovementMethod.getInstance());
            tvEnter.setText(LevelUtil.getImageResourceSpanByHeight(context, R.drawable.ic_rob_live, 12));
            tvEnter.append(" 恭喜 ");
            tvEnter.append(LightSpanString.getLightString(bean.userNickName,
                    ContextCompat.getColor(context, R.color.text_color_chat_yellow)));
            tvEnter.append(" 喜中 ");
            tvEnter.append(LightSpanString.getLightString(bean.giftNumber + "",
                    ContextCompat.getColor(context, R.color.text_color_chat_yellow)));
            tvEnter.append(" 个");
            tvEnter.append(LightSpanString.getLightString(bean.giftName,
                    ContextCompat.getColor(context, R.color.text_color_chat_yellow)));
            tvEnter.append("，");
            tvEnter.append(getRobSpan(context, "我也要夺宝",
                    Color.parseColor("#FFFFFF26")));
        }
    }

    public SpannableString getRobSpan(Context context, String content, int color) {
        SpannableString nickSpan = new SpannableString(content);
        RobSpan clickSpan = new RobSpan(context, color) {
            @Override
            public void onClick(View widget) {
                ((LiveDisplayActivity) context).showSnatchDialog();
            }
        };

        nickSpan.setSpan(clickSpan, 0, nickSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return nickSpan;
    }
}