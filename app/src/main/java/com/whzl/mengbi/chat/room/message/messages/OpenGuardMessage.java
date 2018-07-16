package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.chat.room.message.messageJson.OpenGuardJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

public class OpenGuardMessage implements FillHolderMessage {
    private int userLevel;
    private String nickname;
    private int uid;
    private Context mContext;
    private int programId;

    public OpenGuardMessage(OpenGuardJson openGuardJson, Context mContext) {
        this.userLevel = 0;
        this.nickname = openGuardJson.getContext().getNickname();
        this.uid = openGuardJson.getContext().getUserId();
        this.mContext = mContext;
        if (ChatRoomInfo.getInstance().getRoomInfoBean() != null) {
            this.programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        }
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LightSpanString.getNickNameSpan(mContext, nickname, uid, programId, WHITE_FONG_COLOR));
        mHolder.textView.append(LightSpanString.getLightString(" 开通了守护", Color.parseColor("#f1275b")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
