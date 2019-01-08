package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.BetsEndJson;
import com.whzl.mengbi.chat.room.message.messageJson.FirstPrizeUserJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.AmountConversionUitls;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class FirsrPrizeUserMessage implements FillHolderMessage {
    private FirstPrizeUserJson prizePoolFullJson;
    private Context context;

    public FirsrPrizeUserMessage(FirstPrizeUserJson prizePoolFullJson, Context context) {
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
        mholder.textView.append(LightSpanString.getLightString("恭喜 ", Color.parseColor("#ffffff")));
        for (int i = 0; i < prizePoolFullJson.context.userNickNameList.size(); i++) {
            if (i != prizePoolFullJson.context.userNickNameList.size() - 1) {
                mholder.textView.append(LightSpanString.getLightString(prizePoolFullJson.context.userNickNameList.get(i) + "、", Color.parseColor("#FFFFCB00")));
            } else {
                mholder.textView.append(LightSpanString.getLightString(prizePoolFullJson.context.userNickNameList.get(i), Color.parseColor("#FFFFCB00")));
            }
        }
        mholder.textView.append(LightSpanString.getLightString("喜中特等奖，瓜分 ", Color.parseColor("#ffffff")));
        mholder.textView.append(LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(prizePoolFullJson.context.firstPrizeTotalValue),
                Color.parseColor("#FFFFCB00")));
        mholder.textView.append(LightSpanString.getLightString(" 萌豆。", Color.parseColor("#ffffff")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
