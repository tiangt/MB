package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RankRiseJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/3/1
 */
public class RankRiseMessage implements FillHolderMessage {
    private final RankRiseJson json;
    private final Context context;

    public RankRiseMessage(RankRiseJson json, Context context) {
        this.json = json;
        this.context = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder viewHolder = (SingleTextViewHolder) holder;
        viewHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        viewHolder.textView.setText("");
        viewHolder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_rank_rise));
        viewHolder.textView.append(LightSpanString.getLightString(" 恭喜 ", Color.parseColor("#f9f9f9")));
        viewHolder.textView.append(LightSpanString.getLightString(json.context.nickName, Color.parseColor("#FFD6B510")));
        viewHolder.textView.append(LightSpanString.getLightString(" 总榜名次上升到 ", Color.parseColor("#f9f9f9")));
        viewHolder.textView.append(LightSpanString.getLightString("第"+json.context.rank+"名", Color.parseColor("#FFD6B510")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
