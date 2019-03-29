package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.AnchorWishJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019/3/29
 */
public class AnchorWishAwardMessage implements FillHolderMessage {
    private Context context;
    private AnchorWishJson.ContextBean.GameWishAwardListBean anchorWishJson;

    public AnchorWishAwardMessage(Context context, AnchorWishJson.ContextBean.GameWishAwardListBean anchorWishJson) {
        this.context = context;
        this.anchorWishJson = anchorWishJson;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.setText(LevelUtil.getImageResourceSpan(context, R.drawable.ic_anchor_wish_chat));
        mholder.textView.append(" 恭喜 ");
        mholder.textView.append(LightSpanString.getLightString(anchorWishJson.nickName, Color.rgb(46, 184, 255)));
        mholder.textView.append(" 帮助主播完成心愿，中得 ");
        mholder.textView.append(LightSpanString.getLightString(anchorWishJson.mengCoin + " 萌币奖励",
                Color.rgb(246, 224, 39)));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
