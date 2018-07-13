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
            case 10:
                //禁止发言消息
                showNoChatMsg(holder);
                break;
            case 0:
            case 2:
            case 11:
                showRelieveNoChatMsg(holder);
                //解除禁言消息
                break;
            case 7:
                showRoomManagerMsg(holder);
                //设置为房管
                break;
            case 8:
                showRoomManagerMsg(holder);
                //剔出房间
                break;
            default :
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
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, toNickname, toUid, programId));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickname, fromUid, programId));
        String nochatContent = " 禁止发言" + period / 60 + "分钟";
        mHolder.textView.append(LightSpanString.getLightString(nochatContent, Color.parseColor("#ff611b")));
    }

    private void showRelieveNoChatMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, toNickname, toUid, programId));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickname, fromUid, programId));
        String nochatContent = " 解除禁言";
        mHolder.textView.append(LightSpanString.getLightString(nochatContent, Color.parseColor("#ff611b")));
    }

    private void showKickoutMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, toNickname, toUid, programId));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickname, fromUid, programId));
        String kickouContent = " 踢出房间";
        mHolder.textView.append(LightSpanString.getLightString(kickouContent, Color.parseColor("#ff611b")));
    }

    private void showRoomManagerMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, toNickname, toUid, programId));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickname, fromUid, programId));
        String kickouContent = " 设置为房管";
        mHolder.textView.append(LightSpanString.getLightString(kickouContent, Color.parseColor("#ff611b")));
    }
}
