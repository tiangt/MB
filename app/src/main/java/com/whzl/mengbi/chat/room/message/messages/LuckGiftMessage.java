package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.LuckGiftBigJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.AmountConversionUitls;

/**
 * @author nobody
 * @date 2019-08-16
 */
public class LuckGiftMessage implements FillHolderMessage {
    private final LuckGiftBigJson luckGiftJson;
    private final Context context;

    public LuckGiftMessage(LuckGiftBigJson luckGiftJson, Context context) {
        this.luckGiftJson = luckGiftJson;
        this.context = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_luck_gift));
        mHolder.textView.append(" 恭喜 ");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, luckGiftJson.context.nickname,
                luckGiftJson.context.userId,
                ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId(), Color.rgb(255, 214, 0)));
        mHolder.textView.append(" 爆 ");
        mHolder.textView.append(
                LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(getTotalLuckMengBi()), Color.rgb(255, 214, 0)));
        mHolder.textView.append(" 萌币");
    }

    //获取中奖的萌币数
    public long getTotalLuckMengBi() {
        long num = 0L;
        for (int i = 0; i < luckGiftJson.context.prizes.size(); i++) {
            LuckGiftBigJson.ContextBean.PrizesBean prizesBean = luckGiftJson.context.prizes.get(i);
            num = prizesBean.giftPrice * prizesBean.rewardRatio * prizesBean.times + num;
        }
        return num;
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
