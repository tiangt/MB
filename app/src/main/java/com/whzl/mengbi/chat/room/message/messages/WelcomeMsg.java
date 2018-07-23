package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

import java.util.List;

public class WelcomeMsg implements FillHolderMessage {
    private String nickName;
    private long uid;
    private int userLevel;
    private List<SpannableString> userSpanList;
    private Context mContext;
    private boolean isAnchor = false;
    private boolean hasGuard = false;
    private int programId = 0;

    public WelcomeMsg(WelcomeJson welcomeJson, Context context, List<SpannableString> userSpanList) {
        this.nickName = welcomeJson.getContext().getInfo().getNickname();
        this.uid = welcomeJson.getContext().getInfo().getUserId();
        mContext = context;
        //TODO:判断主播or用户
        if (ChatRoomInfo.getInstance().getRoomInfoBean() != null) {
            int anchorUid = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getAnchor().getId();
            if (anchorUid == this.uid) {
                isAnchor = true;
            }
            programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        }
        this.userLevel = getUserLevel(uid, welcomeJson.getContext().getInfo().getLevelList());
        this.userSpanList = userSpanList;
        this.hasGuard = userHasGuard(welcomeJson.getContext().getInfo().getUserBagList());
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        if (uid != 0) {
            int levelIcon = 0;
            if (isAnchor) {
                levelIcon = ResourceMap.getResourceMap().getAnchorLevelIcon(userLevel);
            }else {
                levelIcon = ResourceMap.getResourceMap().getUserLevelIcon(userLevel);
            }
            mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, levelIcon));
            mHolder.textView.append(" ");
            if (hasGuard) {
                mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.guard));
                mHolder.textView.append(" ");
            }
            if (null != userSpanList) {
                for(SpannableString spanString : userSpanList) {
                    mHolder.textView.append(spanString);
                    mHolder.textView.append(" ");
                }
            }
            mHolder.textView.append(LightSpanString.getNickNameSpan(mContext, nickName, uid, programId, Color.parseColor("#f4be2c")));
        }else {
            mHolder.textView.append(LightSpanString.getLightString("欢迎 ", WHITE_FONG_COLOR));
            mHolder.textView.append(LightSpanString.getLightString(nickName, Color.parseColor("#ffffff")));

        }
        mHolder.textView.append(LightSpanString.getLightString(" 进入直播间", WHITE_FONG_COLOR));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

    private int getUserLevel(long uid, List<WelcomeJson.WelcomeLevelListItem> levelList) {
        if (null == levelList) {
            return 0;
        }
        int level = 0;
        for(WelcomeJson.WelcomeLevelListItem item:levelList) {
            if (isAnchor && item.getLevelType().equals("ANCHOR_LEVEL")) {
                level = item.getLevelValue();
                break;
            }else if (item.getLevelType().equals("USER_LEVEL")) {
                level = item.getLevelValue();
                break;
            }
        }
        return level;
    }


    private boolean userHasGuard(List<WelcomeJson.UserBagItem> goodsList) {
        if (ChatRoomInfo.getInstance().getRoomInfoBean() == null || goodsList == null) {
            return false;
        }
        int programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        boolean hasGuard = false;
        for(WelcomeJson.UserBagItem bagItem: goodsList) {
            if (bagItem.getGoodsType().equals("GUARD") && bagItem.getBindProgramId() == programId) {
                hasGuard = true;
                break;
            }
        }
        return hasGuard;
    }
}
