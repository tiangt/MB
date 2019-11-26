package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RobBigLuckyJson;
import com.whzl.mengbi.chat.room.message.messageJson.RobLuckJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobBigPrizeMessage implements FillHolderMessage {
    private Context context;
    private RobBigLuckyJson json;

    public RobBigPrizeMessage(Context context, RobBigLuckyJson json) {
        this.context = context;
        this.json = json;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText(LevelUtil.getImageResourceSpan(context, R.drawable.ic_rob_live));
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
        mholder.textView.append(LightSpanString.getLightString(json.context.giftName, Color.parseColor("#FF4676")));
        mholder.textView.append(LightSpanString.getLightString("，",
                ContextCompat.getColor(context, R.color.text_color_chat)));
        mholder.textView.append(LightSpanString.getRobSpan(context, "我也要夺宝",
                Color.parseColor("#FFFFFF26")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

}
