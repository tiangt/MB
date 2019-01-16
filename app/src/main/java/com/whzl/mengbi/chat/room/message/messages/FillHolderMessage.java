package com.whzl.mengbi.chat.room.message.messages;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

public interface FillHolderMessage {
    int SINGLE_TEXTVIEW = 1;
    int PLACE_HOLDER = 2; //暂时没用
    public static final int WHITE_FONG_COLOR = Color.parseColor("#f0f0f0");

    void fillHolder(RecyclerView.ViewHolder holder);

    int getHolderType();

}
