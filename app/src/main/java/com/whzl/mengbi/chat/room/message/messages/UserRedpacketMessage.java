package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;


/**
 * @author nobody
 * @date 2019-09-17
 */
public class UserRedpacketMessage implements FillHolderMessage {

    private final Context context;
    private final UserRedpacketJson json;

    public UserRedpacketMessage(Context context, UserRedpacketJson userRedpacketJson) {
        this.context = context;
        this.json = userRedpacketJson;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_redpacket_msg));
        mholder.textView.append(" 土豪金主 ");
        mholder.textView.append(LightSpanString.getLightString(json.context.userInfo.nickname, Color.rgb(46, 233, 255)));
        mholder.textView.append(" 在 ");
        mholder.textView.append(LightSpanString.getLightString(json.context.anchorInfo.nickname, Color.rgb(254, 186, 48)));
        mholder.textView.append(" 直播间发起了 ");
        if (json.context.uGameRedpacketDto.awardType.equals("COIN")) {
            mholder.textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardTotalPrice + "萌币", Color.rgb(252, 60, 101)));
        } else {
            mholder.textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardGoodsNum + "个" +
                    json.context.uGameRedpacketDto.awardGoodsName, Color.rgb(252, 60, 101)));
            mholder.textView.append("（价值");
            mholder.textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardTotalPrice + "", Color.rgb(252, 60, 101)));
            mholder.textView.append("萌币）");
        }
        mholder.textView.append(" 红包抽奖，");
        mholder.textView.append(LightSpanString.getLightString("赶紧围观~", Color.rgb(252, 233, 3)));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
