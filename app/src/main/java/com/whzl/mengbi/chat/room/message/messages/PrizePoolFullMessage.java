package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.PrizePoolFullJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.DateUtils;

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
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_happy_ball_chat));
        mholder.textView.append(LightSpanString.getLightString(prizePoolFullJson.context.periodNumber, Color.parseColor("#FFFFCB00")));
        mholder.textView.append(LightSpanString.getLightString("期欢乐球奖池已达到", Color.parseColor("#ffffff")));
        mholder.textView.append(LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(prizePoolFullJson.context.prizePoolNumber),
                Color.parseColor("#FFFFCB00")));
        mholder.textView.append(LightSpanString.getLightString("萌豆，", Color.parseColor("#ffffff")));
        int ceil = (int) Math.ceil(prizePoolFullJson.context.countDownSecond / 60);
        mholder.textView.append(LightSpanString.getLightString((String.valueOf(ceil)), Color.parseColor("#FFFFCB00")));
        mholder.textView.append(LightSpanString.getLightString(" 分钟投注正式开始", Color.parseColor("#ffffff")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
