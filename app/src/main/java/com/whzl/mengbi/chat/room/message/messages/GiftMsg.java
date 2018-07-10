package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.chat.room.message.messageJson.GiftJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.NickNameSpan;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;


public class GiftMsg implements FillHolderMessage {
    int fromUid;
    String fromNickName;
    int toUid;
    String toNickName;
    int fromLevel;

    String giftName;

    int giftCount;
    SpannableString giftPicSpan;

    Context context;

    private SingleTextViewHolder mHolder;

    public GiftMsg(GiftJson giftJson, Context context, SpannableString giftPicSpan) {
        fromUid = giftJson.getContext().getUserId();
        fromNickName = giftJson.getContext().getNickname();
        toNickName = giftJson.getContext().getToNickname();
        fromLevel = giftJson.getContext().getLevelValue();
        giftName = giftJson.getContext().getGoodsName();
        giftCount = giftJson.getContext().getCount();
        this.context = context;
        this.giftPicSpan = giftPicSpan;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LevelUtil.getLevelSpan(fromLevel, context, ResourceMap.getResourceMap().getUserLevelIcon(fromLevel)));
        mHolder.textView.append(getNickNameSpan(fromNickName,fromUid));
        mHolder.textView.append("送");
        mHolder.textView.append(getNickNameSpan(toNickName,toUid));
        mHolder.textView.append(getGiftCountSpan());
        if (giftPicSpan != null) {
            mHolder.textView.append(giftPicSpan);
        }
    }

    private SpannableString getNickNameSpan(final String nickName, final int uid){
        SpannableString nickSpan = new SpannableString(nickName);
        NickNameSpan clickSpan = new NickNameSpan(context) {
            @Override
            public void onClick(View widget) {
                Log.i("chatMsg", "点击了 " + nickName);
                //TODO:弹窗
                //new EnterUserPop().enterUserPop(context,uid, ChatRoomInfo.getProgramId());
            }
        };

        nickSpan.setSpan(clickSpan,0,nickSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return nickSpan;
    }

    private SpannableString getGiftCountSpan(){
        String countStr = giftCount + "个" + giftName;
        SpannableString countColorSpanStr = new SpannableString(countStr);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
        countColorSpanStr.setSpan(colorSpan,0,countColorSpanStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return countColorSpanStr;
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
