package com.whzl.mengbi.chat.room.message.messages;

import android.support.v7.widget.RecyclerView;

public interface FillHolderMessage  {
    int SINGLE_TEXTVIEW = 1;
    int PLACE_HOLDER = 2;

    void fillHolder(RecyclerView.ViewHolder holder);
    int getHolderType();
}
