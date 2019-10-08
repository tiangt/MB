package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketBackJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2019-09-25
 */
public class UserRedpacketBackMessage implements FillHolderMessage {
    private final Context context;
    private final UserRedpacketBackJson json;

    public UserRedpacketBackMessage(Context context, UserRedpacketBackJson userRedpacketBackJson) {
        this.context = context;
        this.json = userRedpacketBackJson;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_redpacket_msg));
        mholder.textView.append(" 尊敬的 ");
        mholder.textView.append(LightSpanString.getLightString(json.context.nickname, Color.rgb(254, 186, 48)));
        mholder.textView.append(" ，未抽完的红包奖励已退回到您的账户，请注意查收！");
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
