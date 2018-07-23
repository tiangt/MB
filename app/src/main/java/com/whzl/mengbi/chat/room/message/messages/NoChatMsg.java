package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

public class NoChatMsg implements FillHolderMessage{
    private int fromUid;
    private String fromNickname;
    private int toUid;
    private String toNickname;
    private int period;
    private int nochatType;
    private int programId = -1;
    private Context context;

    public NoChatMsg(int fromUid, String fromNickname, int toUid, String toNickname, int period, int nochatType, Context context) {
        this.fromUid = fromUid;
        this.fromNickname = fromNickname;
        this.toUid = toUid;
        this.toNickname = toNickname;
        this.period = period;
        this.nochatType = nochatType;
        this.context = context;
        if (null != ChatRoomInfo.getInstance().getRoomInfoBean()) {
            programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        }
    }

    public int getFromUid() {
        return fromUid;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    public int getToUid() {
        return toUid;
    }

    public String getToNickname() {
        return toNickname;
    }

    public int getPeriod() {
        return period;
    }

    public int getNochatType() {
        return nochatType;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        switch (nochatType) {
            case 1:
            case 3:
            case 5:
            case 10:
                //禁止发言消息
                showNoChatMsg(holder);
                break;
            case 0:
            case 2:
            case 4:
            case 11:
                showRelieveNoChatMsg(holder);
                //解除禁言消息
                break;
            case 8:
                showKickoutMsg(holder);
                //剔出房间
                break;
            default:
                return;
        }
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }


    private void showNoChatMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, toNickname + " 被 ", toUid, programId, WHITE_FONG_COLOR));
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickname, fromUid, programId, Color.parseColor("#20d190")));
        String nochatContent = " 禁止发言" + period + "分钟";
        mHolder.textView.append(LightSpanString.getLightString(nochatContent, Color.parseColor("#ff611b")));
    }

    private void showRelieveNoChatMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, toNickname + " 被 ", toUid, programId, WHITE_FONG_COLOR));
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickname, fromUid, programId, Color.parseColor("#20d190")));
        String nochatContent = " 解除禁言";
        mHolder.textView.append(LightSpanString.getLightString(nochatContent, Color.parseColor("#ff611b")));
    }

    private void showKickoutMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, toNickname + " 被 ", toUid, programId, WHITE_FONG_COLOR));
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickname, fromUid, programId, Color.parseColor("#20d190")));
        String kickouContent = " 踢出房间";
        mHolder.textView.append(LightSpanString.getLightString(kickouContent, Color.parseColor("#ff611b")));
    }
}
