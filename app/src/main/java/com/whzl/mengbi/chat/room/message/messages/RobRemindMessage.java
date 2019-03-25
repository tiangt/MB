package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RobLuckJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobRemindMessage implements FillHolderMessage {
    private Context context;
    private RobLuckJson json;

    public RobRemindMessage(Context context, RobLuckJson json) {
        this.context = context;
        this.json = json;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText(LevelUtil.getImageResourceSpan(context, R.drawable.ic_rob_chat));
        mholder.textView.append(LightSpanString.getLightString(" " + json.context.giftName,
                ContextCompat.getColor(context, R.color.text_color_chat_orange)));
        mholder.textView.append(LightSpanString.getLightString(" 已累计到 ",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(LightSpanString.getLightString(json.context.giftNumber + "",
                ContextCompat.getColor(context, R.color.text_color_chat_yellow)));
        mholder.textView.append(LightSpanString.getLightString(" 个，距开奖还有 ",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(LightSpanString.getLightString(json.context.openPrizeSecond / 60 + "",
                ContextCompat.getColor(context, R.color.text_color_chat_yellow)));
        mholder.textView.append(LightSpanString.getLightString(" 分钟，",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(LightSpanString.getRobSpan(context, "现在去夺宝",
                Color.parseColor("#FFFFFF26")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

}
