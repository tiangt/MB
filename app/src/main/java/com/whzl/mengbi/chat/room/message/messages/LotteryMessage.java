package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.LotteryJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.JsBridgeActivity;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.SPUtils;

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
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        viewHolder.textView.setText("");
        viewHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_lottery));
        viewHolder.textView.append(LightSpanString.getLightString(" 恭喜 ", Color.parseColor("#f9f9f9")));
        viewHolder.textView.append(LightSpanString.getLightString(mLotteryJson.context.nickname, Color.parseColor("#2DA8EE")));
        viewHolder.textView.append(LightSpanString.getLightString(" 被幸运女神眷顾，", Color.parseColor("#f9f9f9")));
        viewHolder.textView.append(LightSpanString.getLightString("抽中 ", Color.parseColor("#F8C330")));
        viewHolder.textView.append(LightSpanString.getLightString(mLotteryJson.context.awardContentNum, Color.parseColor("#f9f9f9")));
        viewHolder.textView.append(LightSpanString.getLightString(" 个", Color.parseColor("#f9f9f9")));
        viewHolder.textView.append(LightSpanString.getLightString(mLotteryJson.context.awardContentName + "，", Color.parseColor("#F8C330")));
        viewHolder.textView.append(LightSpanString.getClickSpan(mContext, "我也要抽奖！", Color.parseColor("#F8C330"),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, JsBridgeActivity.class);
                        intent.putExtra("url", SPUtils.get(mContext, SpConfig.LUCKDRAWURL, "").toString());
                        mContext.startActivity(intent);
                    }
                }));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
