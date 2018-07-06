package com.whzl.mengbi.chat.room.message.messages;

import android.support.v7.widget.RecyclerView;


public class PlaceHolderMsg implements FillHolderMessage {
    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {

    }

    @Override
    public int getHolderType() {
        return PLACE_HOLDER;
    }
}
