package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.chat.room.util.NickNameSpan;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

public class NoChatMsg implements FillHolderMessage{
    private int fromUid;
    private String fromNickname;
    private int toUid;
    private String toNickname;
    private int period;
    private int nochatType;
    private Context context;

    public NoChatMsg(int fromUid, String fromNickname, int toUid, String toNickname, int period, int nochatType, Context context) {
        this.fromUid = fromUid;
        this.fromNickname = fromNickname;
        this.toUid = toUid;
        this.toNickname = toNickname;
        this.period = period;
        this.nochatType = nochatType;
        this.context = context;
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

    private SpannableString getNickNameSpan(final String nickName, final int uid) {
        SpannableString nickSpan = new SpannableString(nickName);
        NickNameSpan clickSpan = new NickNameSpan(context) {
            @Override
            public void onClick(View widget) {
                Log.i("chatMsg", "点击了 " + nickName);
                //TODO:弹窗
                //new EnterUserPop().enterUserPop(mContext, uid, ChatRoomInfo.getProgramId());
            }
        };

        nickSpan.setSpan(clickSpan, 0, nickSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return nickSpan;
    }

    private SpannableString getLightStr(String content, int color) {
        //文本内容
        SpannableString ss = new SpannableString(content);
        //设置字符颜色
        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void showNoChatMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.append(getNickNameSpan(toNickname, toUid));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(getNickNameSpan(fromNickname, fromUid));
        String nochatContent = " 禁止发言" + period / 50 + "分钟";
        mHolder.textView.append(getLightStr(nochatContent, Color.RED));
    }

    private void showRelieveNoChatMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.append(getNickNameSpan(toNickname, toUid));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(getNickNameSpan(fromNickname, fromUid));
        String nochatContent = " 解除禁言";
        mHolder.textView.append(getLightStr(nochatContent, Color.RED));
    }

    private void showKickoutMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.append(getNickNameSpan(toNickname, toUid));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(getNickNameSpan(fromNickname, fromUid));
        String kickouContent = " 踢出房间";
        mHolder.textView.append(getLightStr(kickouContent, Color.RED));
    }

    private void showRoomManagerMsg(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.append(getNickNameSpan(toNickname, toUid));
        mHolder.textView.append(" 被 ");
        mHolder.textView.append(getNickNameSpan("主播", fromUid));
        String kickouContent = " 设置为房管";
        mHolder.textView.append(getLightStr(kickouContent, Color.RED));
    }
}
