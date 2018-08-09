package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

import java.util.List;


public class ChatMessage implements FillHolderMessage {
    private int from_level;
    private int from_uid;
    private String from_nickname;

    private String contentString;
    private Context mContext;

    private String to_nickName;
    private int to_level;
    private int to_uid;

    private boolean isAnchor = false;
    private boolean hasGuard = false;
    private int programId = 0;
    private SingleTextViewHolder mholder;
    private List<SpannableString> fromSpanList;
    private boolean isPrivate = false;

    public ChatMessage(ChatCommonJson msgJson, Context context, List<SpannableString> fromSpanList, boolean isPrivate) {
        this.mContext = context;
        from_nickname = msgJson.getFrom_nickname();
        this.fromSpanList = fromSpanList;

        to_nickName = msgJson.getTo_nickname();
        contentString = msgJson.getContent();

        try {
            from_uid = Integer.valueOf(msgJson.getFrom_uid());
            to_uid = Integer.valueOf(msgJson.getTo_uid());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        from_level = LevelUtil.getUserLevel(msgJson.getFrom_json());
        if (ChatRoomInfo.getInstance().getRoomInfoBean() != null) {
            int anchorUid = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getAnchor().getId();
            if (anchorUid == from_uid) {
                isAnchor = true;
                from_level = LevelUtil.getAnchorLevel(msgJson.getFrom_json());
            }
            programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        }
        to_level = LevelUtil.getUserLevel(msgJson.getTo_json());
        hasGuard = userHasGuard(msgJson.getFrom_json().getGoodsList());
        this.isPrivate = isPrivate;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        this.mholder = (SingleTextViewHolder) holder;
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        if (isPrivate) {
            parsePrivateMessage();
        } else if (to_uid == 0) {
            parseNoRecieverMessage();
        } else {
            parseHasRecieverMessage();
        }
    }

    private void parsePrivateMessage() {
        if (from_uid != 0) {
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
        }
        mholder.textView.append(LightSpanString.getNickNameSpan(mContext, from_nickname, from_uid, programId));
        mholder.textView.append(LightSpanString.getLightString("对", WHITE_FONG_COLOR));
        mholder.textView.append(LightSpanString.getLightString(to_nickName, Color.parseColor("#75bbfb")));
        mholder.textView.append(LightSpanString.getLightString("说:  ", WHITE_FONG_COLOR));
        //TODO:表情替换
        SpannableString spanString = LightSpanString.getLightString(contentString, WHITE_FONG_COLOR);
        FaceReplace.getInstance().faceReplace(mholder.textView, spanString, mContext);
        mholder.textView.append(spanString);
    }

    private void parseNoRecieverMessage() {
        //非游客发言
        if (from_uid != 0) {
            if (isAnchor) {
                mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getAnchorLevelIcon(from_level)));
            } else {
                mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
            }
            mholder.textView.append(" ");
        }
        if (hasGuard) {
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.guard));
            mholder.textView.append(" ");
        }
        if (fromSpanList != null) {
            for (SpannableString spanString : fromSpanList) {
                mholder.textView.append(spanString);
                mholder.textView.append(" ");
            }
        }
        mholder.textView.append(LightSpanString.getNickNameSpan(mContext, from_nickname + ": ", from_uid, programId));
        SpannableString spanString = LightSpanString.getLightString(contentString, WHITE_FONG_COLOR);
        //TODO:表情替换
        FaceReplace.getInstance().faceReplace(mholder.textView, spanString, mContext);
        mholder.textView.append(spanString);
    }

    private void parseHasRecieverMessage() {
        if (from_uid != 0) {
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
        }
        if (fromSpanList != null) {
            for (SpannableString spanString : fromSpanList) {
                mholder.textView.append(spanString);
                mholder.textView.append("  ");
            }
        }
        mholder.textView.append(LightSpanString.getNickNameSpan(mContext, from_nickname, from_uid, programId));
        mholder.textView.append(LightSpanString.getLightString("对 您 说:  ", WHITE_FONG_COLOR));
        //TODO:表情替换
        SpannableString spanString = LightSpanString.getLightString(contentString, WHITE_FONG_COLOR);
        FaceReplace.getInstance().faceReplace(mholder.textView, spanString, mContext);
        mholder.textView.append(spanString);
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public String getContentString() {
        return contentString;
    }

    private SpannableString replaceFaceList(SpannableString content) {
        SpannableString faceContentSpanString = new SpannableString(content);
        List<EmjoyInfo.FaceBean> faceList;
        return faceContentSpanString;
    }

    private boolean userHasGuard(List<FromJson.Good> goodsList) {
        if (ChatRoomInfo.getInstance().getRoomInfoBean() == null || goodsList == null) {
            return false;
        }
        boolean hasGuard = false;
        int programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        for (FromJson.Good good : goodsList) {
            if (good.getGoodsType().equals("GUARD") && good.getBindProgramId() == programId) {
                hasGuard = true;
                break;
            }
        }
        return hasGuard;
    }
}
