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
public class AnchorWishSuccessMessage implements FillHolderMessage {
    private Context context;
    private AnchorWishJson anchorWishJson;

    public AnchorWishSuccessMessage(Context context, AnchorWishJson anchorWishJson) {
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
        mholder.textView.append(LightSpanString.getLightString(anchorWishJson.context.nickName, Color.rgb(255, 126, 168)));
        mholder.textView.append(" 完成心愿，获得 ");
        mholder.textView.append(LightSpanString.getLightString(anchorWishJson.context.needGiftNumber + "个" + anchorWishJson.context.wishGiftName + "，",
                Color.parseColor("#fe4d87")));
        mholder.textView.append("所有支持者获得1个经验大礼包");
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
