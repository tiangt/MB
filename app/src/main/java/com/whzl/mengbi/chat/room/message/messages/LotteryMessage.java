package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.LotteryJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 */
public class LotteryMessage implements FillHolderMessage {
    private LotteryJson mLotteryJson;
    private Context mContext;


    public LotteryMessage(LotteryJson lotteryJson, Context context) {
        mLotteryJson = lotteryJson;
        mContext = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder viewHolder = (SingleTextViewHolder) holder;
        viewHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        viewHolder.textView.setText("");
        viewHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_lottery));
        viewHolder.textView.append(LightSpanString.getLightString(" 恭喜 ", ContextCompat.getColor(mContext, R.color.text_color_lottery)));
        viewHolder.textView.append(LightSpanString.getLightString(mLotteryJson.context.nickname, ContextCompat.getColor(mContext, R.color.text_color_lottery)));
        viewHolder.textView.append(LightSpanString.getLightString(" 被幸运女神眷顾，抽中 ", ContextCompat.getColor(mContext, R.color.text_color_lottery)));
        viewHolder.textView.append(LightSpanString.getLightString(mLotteryJson.context.awardContentNum, ContextCompat.getColor(mContext, R.color.text_color_lottery)));
        viewHolder.textView.append(LightSpanString.getLightString(" 个", ContextCompat.getColor(mContext, R.color.text_color_lottery)));
        viewHolder.textView.append(LightSpanString.getLightString(mLotteryJson.context.awardContentName + "，", ContextCompat.getColor(mContext, R.color.text_color_lottery)));
        viewHolder.textView.append(LightSpanString.getLightString("我也要抽奖！", ContextCompat.getColor(mContext, R.color.text_color_lottery)));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
