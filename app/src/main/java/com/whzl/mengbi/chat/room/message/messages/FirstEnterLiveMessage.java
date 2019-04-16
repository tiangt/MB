package com.whzl.mengbi.chat.room.message.messages;

import android.support.v7.widget.RecyclerView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/4/16
 */
public class FirstEnterLiveMessage implements FillHolderMessage {
    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("go fuck yourself");
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
