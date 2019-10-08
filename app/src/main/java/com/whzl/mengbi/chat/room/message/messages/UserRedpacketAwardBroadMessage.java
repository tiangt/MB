package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketAwardJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019-09-18
 */
public class UserRedpacketAwardBroadMessage implements FillHolderMessage {
    private final Context context;
    private final UserRedpacketAwardJson json;

    public UserRedpacketAwardBroadMessage(Context context, UserRedpacketAwardJson userRedpacketAwardJson) {
        this.context = context;
        this.json = userRedpacketAwardJson;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_redpacket_msg));
        mholder.textView.append(" 恭喜 ");
        mholder.textView.append(LightSpanString.getLightString(json.context.userInfoDto.awardNickname, Color.rgb(46, 233, 255)));
        mholder.textView.append(" 抽中 ");
        mholder.textView.append(LightSpanString.getLightString(json.context.userInfoDto.nickname, Color.rgb(254, 186, 48)));
        mholder.textView.append(" 发起的");
        if (json.context.gameRedpacketAwardDto.awardType.equals("COIN")) {
            mholder.textView.append(LightSpanString.getLightString(json.context.gameRedpacketAwardDto.awardPrice + "萌币", Color.rgb(252, 60, 101)));
        } else {
            mholder.textView.append(LightSpanString.getLightString(json.context.gameRedpacketAwardDto.awardGoodsNum + "个" + json.context.uGameRedpacketDto.awardGoodsName
                    , Color.rgb(252, 60, 101)));
        }
        mholder.textView.append(" 红包（共 ");
        if (json.context.gameRedpacketAwardDto.awardType.equals("COIN")) {
            mholder.textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardTotalPrice + "", Color.rgb(252, 60, 101)));
            mholder.textView.append(" 萌币)");
        } else {
            mholder.textView.append(LightSpanString.getLightString(json.context.uGameRedpacketDto.awardGoodsNum + ""
                    , Color.rgb(252, 60, 101)));
            mholder.textView.append(" 个)");
        }
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
