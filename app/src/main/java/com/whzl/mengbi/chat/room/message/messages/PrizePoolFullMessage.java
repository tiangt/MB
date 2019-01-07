package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.whzl.mengbi.chat.room.message.messageJson.PrizePoolFullJson;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class PrizePoolFullMessage implements FillHolderMessage {
    private PrizePoolFullJson prizePoolFullJson;
    private Context context;
    public PrizePoolFullMessage(PrizePoolFullJson prizePoolFullJson, Context context) {
        this.prizePoolFullJson = prizePoolFullJson;
        this.context = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {

    }

    @Override
    public int getHolderType() {
        return FillHolderMessage.SINGLE_TEXTVIEW;
    }
}
