package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.WeekStarJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class WeekStarMessage implements FillHolderMessage {
    private WeekStarJson weekStarJson;
    private Context context;

    public WeekStarMessage(Context context, WeekStarJson weekStarJson) {
        this.weekStarJson = weekStarJson;
        this.context = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_week_star_chat));
        mholder.textView.append(LightSpanString.getLightString(" 恭喜 ", Color.parseColor("#ffcc33")));
        mholder.textView.append(LightSpanString.getLightString(weekStarJson.context.nickName, Color.parseColor("#ffcc33")));
        mholder.textView.append(LightSpanString.getLightString(" 在周星礼物 ", Color.parseColor("#ffcc33")));
        mholder.textView.append(LightSpanString.getLightString( weekStarJson.context.giftName, Color.parseColor("#ffcc33")));
        mholder.textView.append(LightSpanString.getLightString( " 争夺中上升到第一名", Color.parseColor("#ffcc33")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
