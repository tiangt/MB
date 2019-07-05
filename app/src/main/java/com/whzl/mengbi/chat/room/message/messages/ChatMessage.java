package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.SPUtils;

import java.io.IOException;
import java.util.List;


public class ChatMessage implements FillHolderMessage {
    public ChatCommonJson chatJson;
    private int from_level;
    private int royal_level;
    public int from_uid;
    private String from_nickname;

    private String contentString;
    private Context mContext;

    private String to_nickName;
    private int to_level;
    public int to_uid;

    private boolean isAnchor = false;
    private boolean hasGuard = false;
    private boolean hasVip = false;
    private boolean hasSuccubus = false;
    private int programId = 0;
    private SingleTextViewHolder mholder;
    private List<SpannableString> fromSpanList;
    private boolean isPrivate = false;
    private String prettyNumColor;
    private String prettyNum;

    public ChatMessage(ChatCommonJson msgJson, Context context, List<SpannableString> fromSpanList, boolean isPrivate) {
        this.chatJson = msgJson;
        this.isPrivate = isPrivate;
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
        royal_level = LevelUtil.getRoyalLevel(msgJson.getFrom_json());
        prettyNumColor = LevelUtil.getPrettyNumColor(msgJson.getFrom_json());
        prettyNum = LevelUtil.getPrettyNum(msgJson.getFrom_json());

        if (ChatRoomInfo.getInstance().getRoomInfoBean() != null) {
            int anchorUid = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getAnchor().getId();
            if (anchorUid == from_uid) {
                isAnchor = true;
                from_level = LevelUtil.getAnchorLevel(msgJson.getFrom_json());
            }
            programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        }
        to_level = LevelUtil.getUserLevel(msgJson.getTo_json());
        if (msgJson.getFrom_json() != null && msgJson.getFrom_json().getGoodsList() != null) {
            userHasGuard(msgJson.getFrom_json().getGoodsList());
//            hasVip = userHasVip(msgJson.getFrom_json().getGoodsList());
        }
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
        if (royal_level > 0) {
//            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getRoyalLevelIcon(royal_level)));
            try {
                mholder.textView.append(LevelUtil.getRoyalImageResourceSpan(mContext, royal_level, mholder.textView));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mholder.textView.append(" ");
        }
        long userId = (long) SPUtils.get(mContext, SpConfig.KEY_USER_ID, 0L);
        if (from_uid != 0) {
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
        }
        mholder.textView.append(LightSpanString.getNickNameSpan(mContext, userId == from_uid ? "  你" : "  " + from_nickname, from_uid, programId));
        mholder.textView.append(LightSpanString.getLightString(" 对 ", WHITE_FONG_COLOR));
        mholder.textView.append(LightSpanString.getLightString(userId == to_uid ? "你" : to_nickName, Color.parseColor("#75bbfb")));
        mholder.textView.append(LightSpanString.getLightString("说:  ", WHITE_FONG_COLOR));
        //TODO:表情替换
        SpannableString spanString = LightSpanString.getLightString(contentString, WHITE_FONG_COLOR);
        FaceReplace.getInstance().faceReplace(mholder.textView, spanString, mContext);
        if (hasGuard) {
            FaceReplace.getInstance().guardFaceReplace(mholder.textView, spanString, mContext);
        }
        FaceReplace.getInstance().vipFaceReplace(mholder.textView, spanString, mContext);
        mholder.textView.append(spanString);
    }

    private void parseNoRecieverMessage() {
        if (hasGuard) {
            mholder.textView.setBackgroundResource(R.drawable.bg_welcome_hasguard);
        } else {
            mholder.textView.setBackgroundResource(R.drawable.bg_welcome_noguard);
        }
//        非游客发言
        if (royal_level > 0) {
//            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getRoyalLevelIcon(royal_level)));
            try {
                mholder.textView.append(LevelUtil.getRoyalImageResourceSpan(mContext,
                        royal_level, mholder.textView));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mholder.textView.append(" ");
        }
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
        if (hasVip) {
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_vip));
            mholder.textView.append(" ");
        }
        if (hasSuccubus) {
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_succubus));
            mholder.textView.append(" ");
        }
        if (fromSpanList != null) {
            for (SpannableString spanString : fromSpanList) {
                mholder.textView.append(spanString);
                mholder.textView.append(" ");
            }
        }
        if (!TextUtils.isEmpty(prettyNum)) {
            if ("A".equals(prettyNumColor)) {
                mholder.textView.append(LightSpanString.getAPrettyNumSpan(mContext, prettyNum, ContextCompat.getColor(mContext, R.color.a_level_preety_num)));
            } else if ("B".equals(prettyNumColor)) {
                mholder.textView.append(LightSpanString.getBPrettyNumSpan(mContext, prettyNum, ContextCompat.getColor(mContext, R.color.b_level_preety_num)));
            } else if ("C".equals(prettyNumColor)) {
                mholder.textView.append(LightSpanString.getPrettyNumSpan(mContext, prettyNum, ContextCompat.getColor(mContext, R.color.c_level_preety_num)));
            } else if ("D".equals(prettyNumColor)) {
                mholder.textView.append(LightSpanString.getPrettyNumSpan(mContext, prettyNum, ContextCompat.getColor(mContext, R.color.d_level_preety_num)));
            } else if ("E".equals(prettyNumColor)) {
                mholder.textView.append(LightSpanString.getPrettyNumSpan(mContext, prettyNum, ContextCompat.getColor(mContext, R.color.e_level_preety_num)));
            } else {
                mholder.textView.append(LightSpanString.getPrettyNumSpan(mContext, prettyNum, ContextCompat.getColor(mContext, R.color.e_level_preety_num)));
            }
            mholder.textView.append("  ");
        }
        if (from_uid > 0 && from_uid == ChatRoomInfo.getInstance().getProgramFirstId()) {
            mholder.textView.append(LightSpanString.getNickNameSpan(mContext, from_nickname, from_uid, programId, Color.parseColor("#2da8ee")));
            mholder.textView.append(LightSpanString.getLightString("：", Color.parseColor("#2da8ee")));
        } else {
            if (hasVip) {
                mholder.textView.append(LightSpanString.getNickNameSpan(mContext, from_nickname, from_uid, programId, Color.parseColor("#fff607")));
                mholder.textView.append(LightSpanString.getLightString("：", Color.parseColor("#fff607")));
            } else if (from_uid == 0) {
                mholder.textView.append(LightSpanString.getNickNameSpan(mContext, from_nickname, from_uid, programId, Color.parseColor("#d9d9d9")));
                mholder.textView.append(LightSpanString.getLightString("：", Color.parseColor("#d9d9d9")));
            } else {
                mholder.textView.append(LightSpanString.getNickNameSpan(mContext, from_nickname, from_uid, programId, Color.parseColor("#2da8ee")));
                mholder.textView.append(LightSpanString.getLightString("：", Color.parseColor("#2da8ee")));
            }
        }

        SpannableString spanString;
        if (from_uid > 0 && from_uid == ChatRoomInfo.getInstance().getProgramFirstId()) {
            spanString = LightSpanString.getLightString(contentString, Color.parseColor("#fc8f7a"));
        } else {
//            spanString = LightSpanString.getLightString(contentString, Color.parseColor("#ffffff"));
            spanString = new SpannableString(contentString);
        }
        FaceReplace.getInstance().faceReplace(mholder.textView, spanString, mContext);
        if (hasGuard) {
            FaceReplace.getInstance().guardFaceReplace(mholder.textView, spanString, mContext);
        }
        FaceReplace.getInstance().vipFaceReplace(mholder.textView, spanString, mContext);
        mholder.textView.append(spanString);
    }

    private void parseHasRecieverMessage() {
        if (royal_level > 0) {
//            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getRoyalLevelIcon(royal_level)));
            try {
                mholder.textView.append(LevelUtil.getRoyalImageResourceSpan(mContext, royal_level, mholder.textView));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mholder.textView.append(" ");
        }
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
        if (hasGuard) {
            FaceReplace.getInstance().guardFaceReplace(mholder.textView, spanString, mContext);
        }
        FaceReplace.getInstance().vipFaceReplace(mholder.textView, spanString, mContext);
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

    private void userHasGuard(List<FromJson.Good> goodsList) {
        if (ChatRoomInfo.getInstance().getRoomInfoBean() == null || goodsList == null) {
            return;
        }
        int programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        for (FromJson.Good good : goodsList) {
            if (good.getGoodsType().equals("GUARD") && good.getBindProgramId() == programId) {
                hasGuard = true;
            }
            if (good.getGoodsType().equals("VIP")) {
                hasVip = true;
            }
            if (good.getGoodsType().equals("DEMON_CARD")) {
                hasSuccubus = true;
            }
        }

    }
}
