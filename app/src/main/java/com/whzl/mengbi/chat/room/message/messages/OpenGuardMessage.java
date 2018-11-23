package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.OpenGuardJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

public class OpenGuardMessage implements FillHolderMessage {
    private int userLevel;
    private String nickname;
    private long uid;
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
        mHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
//        mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.guard_msg_icon));
//        mHolder.textView.append(LightSpanString.getLightString(" 恭喜 ", Color.parseColor("#e5b82b")));
//        mHolder.textView.append(" ");
        mHolder.textView.append(LightSpanString.getNickNameSpan(mContext, nickname, uid, programId, ContextCompat.getColor(mContext, R.color.text_color_focus_nick)));
//        mHolder.textView.append(LightSpanString.getLightString(" 成为", WHITE_FONG_COLOR));
        mHolder.textView.append(LightSpanString.getLightString(" 开通了爱心守护", ContextCompat.getColor(mContext, R.color.text_color_open_guard)));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}

