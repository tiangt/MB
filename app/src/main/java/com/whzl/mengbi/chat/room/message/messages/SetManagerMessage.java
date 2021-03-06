package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.SetManagerJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * author: yaobo wu
 * date:   On 2018/7/19
 */
public class SetManagerMessage implements FillHolderMessage {
    private String toNickname;
    private long toUserId;
    private String fromNickname;
    private long fromUserId;
    private Context mContext;
    private int programId = 0;

    public SetManagerMessage(SetManagerJson managerJson, Context context) {
        this.mContext = context;
        this.toNickname = managerJson.getContext().getToUserNickname();
        this.toUserId = managerJson.getContext().getToUserId();
        this.fromUserId = managerJson.getContext().getUserId();
        this.fromNickname = managerJson.getContext().getNickname();
        if (ChatRoomInfo.getInstance().getRoomInfoBean() != null) {
            programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        }
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LightSpanString.getNickNameSpan(mContext, toNickname, toUserId, programId, Color.parseColor("#f9f9f9")));
        mHolder.textView.append(LightSpanString.getLightString(" 被 ", Color.parseColor("#FFFF709F")));
        mHolder.textView.append(LightSpanString.getNickNameSpan(mContext, fromNickname, fromUserId, programId, Color.parseColor("#f9f9f9")));
        mHolder.textView.append(LightSpanString.getLightString(" 提升为房管", Color.parseColor("#FFFF709F")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
