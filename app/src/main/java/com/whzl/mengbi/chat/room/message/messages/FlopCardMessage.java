package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.FlopCardJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/5/30
 */
public class FlopCardMessage implements FillHolderMessage {
    private final FlopCardJson flopCardJson;
    private final Context context;

    public FlopCardMessage(FlopCardJson flopCardJson, Context context) {
        this.flopCardJson = flopCardJson;
        this.context = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_flop_card));
        mholder.textView.append(" 恭喜 ");
        mholder.textView.append(LightSpanString.getLightString(flopCardJson.context.nickName, Color.rgb(255, 203, 0)));
        mholder.textView.append(" 翻到 ");
        if ("GOODS".equals(flopCardJson.context.prizeType)) {
            mholder.textView.append(LightSpanString.getLightString(flopCardJson.context.goodsName + " x " + flopCardJson.context.goodsCount, Color.rgb(255, 110, 35)));
        } else if ("EXP".equals(flopCardJson.context.prizeType)) {
            mholder.textView.append(LightSpanString.getLightString("用户经验 x " + flopCardJson.context.expNumber, Color.rgb(255, 110, 35)));
        } else if ("WEALTH".equals(flopCardJson.context.prizeType)) {
            if ("MENG_DOU".equals(flopCardJson.context.wealthType)) {
                mholder.textView.append(LightSpanString.getLightString("萌豆 x " + flopCardJson.context.wealthNumber, Color.rgb(255, 110, 35)));
            } else if ("COIN".equals(flopCardJson.context.wealthType)) {
                mholder.textView.append(LightSpanString.getLightString("萌币 x " + flopCardJson.context.wealthNumber, Color.rgb(255, 110, 35)));
            }
        }
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
