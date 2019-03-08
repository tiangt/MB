package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobPrizeMessage implements FillHolderMessage {
    private Context context;
    private RobLuckJson json;

    public RobPrizeMessage(Context context, RobLuckJson json) {
        this.context = context;
        this.json = json;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText(LevelUtil.getImageResourceSpan(context, R.drawable.ic_rob_chat));
        mholder.textView.append(LightSpanString.getLightString(" 恭喜 ",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(LightSpanString.getLightString(json.context.userNickName,
                ContextCompat.getColor(context, R.color.text_color_chat_yellow)));
        mholder.textView.append(LightSpanString.getLightString(" 喜中 ",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(LightSpanString.getLightString(json.context.giftNumber + "",
                ContextCompat.getColor(context, R.color.text_color_chat_yellow)));
        mholder.textView.append(LightSpanString.getLightString(" 个",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(LightSpanString.getLightString(json.context.giftName,
                ContextCompat.getColor(context, R.color.text_color_chat_orange)));
        mholder.textView.append(LightSpanString.getLightString("，",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(getRobSpan(context, "我也要夺宝",
                Color.parseColor("#FFFFFF26")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
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
