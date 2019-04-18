package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.model.entity.RoomAnnouceBean;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/4/18
 */
public class FirstEnterMessage implements FillHolderMessage {
    public RoomAnnouceBean.ListBean listBean;
    private Context context;

    public FirstEnterMessage(RoomAnnouceBean.ListBean listBean, Context context) {
        this.listBean = listBean;
        this.context = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder viewHolder = (SingleTextViewHolder) holder;
        viewHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        viewHolder.textView.setText("");
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.system_msg));
        viewHolder.textView.append(LightSpanString.getLightString(" " + listBean.content.replaceAll("\n", ""),
                ContextCompat.getColor(context, R.color.text_color_system)));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
