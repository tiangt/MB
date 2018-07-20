package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.chat.room.message.messageJson.GiftJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

import java.util.List;


public class GiftMsg implements FillHolderMessage {
    private int fromUid;
    private String fromNickName;
    private int toUid;
    private String toNickName;
    private int fromLevel;

    private String giftName;

    private int giftCount;
    private SpannableString giftPicSpan;

    private Context context;
    private int programId = 0;
    private boolean isAnchor = false;

    public GiftMsg(GiftJson giftJson, Context context, SpannableString giftPicSpan) {
        fromUid = giftJson.getContext().getUserId();
        fromNickName = giftJson.getContext().getNickname();
        toNickName = giftJson.getContext().getToNickname();
        fromLevel = giftJson.getContext().getLevelValue();
        giftName = giftJson.getContext().getGoodsName();
        giftCount = giftJson.getContext().getCount();
        this.context = context;
        this.giftPicSpan = giftPicSpan;
        if (ChatRoomInfo.getInstance().getRoomInfoBean() != null) {
            programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
            if (fromUid == ChatRoomInfo.getInstance().getRoomInfoBean().getData().getAnchor().getId()) {
                isAnchor = true;
                fromLevel = getAnchorLevel(giftJson);
            }
        }
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        if (isAnchor) {
            mHolder.textView.append(LevelUtil.getImageResourceSpan(context, ResourceMap.getResourceMap().getAnchorLevelIcon(fromLevel)));
        }else {
            mHolder.textView.append(LevelUtil.getImageResourceSpan(context, ResourceMap.getResourceMap().getUserLevelIcon(fromLevel)));
        }
        mHolder.textView.append(" ");
        mHolder.textView.append(LightSpanString.getNickNameSpan(context, fromNickName, fromUid, programId));
        mHolder.textView.append(" ");
        mHolder.textView.append(LightSpanString.getLightString("赠送给 主播 ", WHITE_FONG_COLOR));
        //mHolder.textView.append(getNickNameSpan(toNickName,toUid));
        mHolder.textView.append(LightSpanString.getLightString(giftCount +"", Color.parseColor("#f1275b")));
        mHolder.textView.append(LightSpanString.getLightString("个" + giftName, WHITE_FONG_COLOR));
        if (giftPicSpan != null) {
            mHolder.textView.append(" ");
            mHolder.textView.append(giftPicSpan);
        }
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

    private int getAnchorLevel(GiftJson giftJson) {
        int fromLevel = 0;
        List<GiftJson.LevelEntity> levelList = giftJson.getContext().getLevels();
        if (levelList == null) {
            return fromLevel;
        }
        for(GiftJson.LevelEntity levelEntity: levelList) {
            if (levelEntity.getLevelType().equals("ANCHOR_LEVEL")) {
                fromLevel = levelEntity.getLevelValue();
            }
        }
        return fromLevel;
    }
}
