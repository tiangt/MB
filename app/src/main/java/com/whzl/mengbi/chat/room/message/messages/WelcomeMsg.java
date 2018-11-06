package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.WelcomeTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

import java.io.IOException;
import java.util.List;

public class WelcomeMsg implements FillHolderMessage {
    public String nickName;
    public long uid;
    public int userLevel;
    public List<SpannableString> userSpanList;
    private Context mContext;
    public boolean isAnchor = false;
    private long prettyNum;
    public boolean hasGuard = false;
    public int programId = 0;

    public WelcomeJson getmWelcomeJson() {
        return mWelcomeJson;
    }

    private WelcomeJson mWelcomeJson;
    public int royalLevel;
    public boolean hasVip = false;
    public String prettyNumberOrUserId;

    public WelcomeMsg(WelcomeJson welcomeJson, Context context, List<SpannableString> userSpanList) {
        this.nickName = welcomeJson.getContext().getInfo().getNickname();
        this.uid = welcomeJson.getContext().getInfo().getUserId();
        this.mWelcomeJson = welcomeJson;
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
        this.royalLevel = getRoyalLevel(welcomeJson.getContext().getInfo().getLevelList());
        this.hasVip = userHasVip(welcomeJson.getContext().getInfo().getUserBagList());
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        WelcomeTextViewHolder mHolder = (WelcomeTextViewHolder) holder;
        mHolder.linearLayout.setBackgroundResource(R.drawable.bg_welcome_noguard);
        mHolder.textView.setText("");
        if (royalLevel > 0) {
            try {
                mHolder.textView.append(LevelUtil.getRoyalImageResourceSpan(mContext, royalLevel, mHolder.textView));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHolder.textView.append(" ");
        }
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        if (uid != 0) {
            int levelIcon = 0;
            if (isAnchor) {
                levelIcon = ResourceMap.getResourceMap().getAnchorLevelIcon(userLevel);
            } else {
                levelIcon = ResourceMap.getResourceMap().getUserLevelIcon(userLevel);
            }
            mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, levelIcon));
            mHolder.textView.append(" ");
            if (hasGuard) {
                mHolder.linearLayout.setBackgroundResource(R.drawable.bg_welcome_hasguard);
                mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.guard));
                mHolder.textView.append(" ");
            }
            if (hasVip) {
                mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.ic_vip_chat));
                mHolder.textView.append(" ");
            }
            if (null != userSpanList) {
                for (SpannableString spanString : userSpanList) {
                    mHolder.textView.append(spanString);
                    mHolder.textView.append(" ");
                }
            }
            if (!TextUtils.isEmpty(prettyNumberOrUserId)) {
//                switch (prettyNumberOrUserId.length()) {
//                    case 5:
//                        mHolder.textView.append(LightSpanString.getLightString(prettyNumberOrUserId, Color.parseColor("#8bc1fe")));
//                        mHolder.textView.append(" ");
//                        break;
//                    case 6:
//                        mHolder.textView.append(LightSpanString.getLightString(prettyNumberOrUserId, Color.parseColor("#8bc1fe")));
//                        mHolder.textView.append(" ");
//                        break;
//                    case 7:
//                        mHolder.textView.append(LightSpanString.getLightString(prettyNumberOrUserId, Color.parseColor("#8bc1fe")));
//                        mHolder.textView.append(" ");
//                        break;
//                }
            }
            mHolder.textView.append(LightSpanString.getNickNameSpan(mContext, nickName, uid, programId, Color.parseColor("#d9d9d9")));
            if (royalLevel > 0) {
                mHolder.textView.append(LightSpanString.getLightString(" 闪亮登场", Color.parseColor("#d9d9d9")));
            } else {
                mHolder.textView.append(LightSpanString.getLightString(" 精彩亮相", Color.parseColor("#d9d9d9")));
            }
        } else {
            mHolder.textView.append(LightSpanString.getLightString(nickName, Color.parseColor("#d9d9d9")));
            mHolder.textView.append(LightSpanString.getLightString(" 入场", WHITE_FONG_COLOR));
        }

    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }


    public boolean isHasGuard() {
        return hasGuard;
    }

    public long getUid() {
        return uid;
    }


    private int getUserLevel(long uid, List<WelcomeJson.WelcomeLevelListItem> levelList) {
        if (null == levelList) {
            return 0;
        }
        int level = 0;
        for (WelcomeJson.WelcomeLevelListItem item : levelList) {
            if (isAnchor && item.getLevelType().equals("ANCHOR_LEVEL")) {
                level = item.getLevelValue();
                break;
            } else if (item.getLevelType().equals("USER_LEVEL")) {
                level = item.getLevelValue();
                break;
            }
        }
        return level;
    }

    public int getRoyalLevel(List<WelcomeJson.WelcomeLevelListItem> levelList) {
        if (null == levelList) {
            return 0;
        }
        int level = 0;
        for (WelcomeJson.WelcomeLevelListItem item : levelList) {
            if (item.getLevelType().equals("ROYAL_LEVEL")) {
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
        for (WelcomeJson.UserBagItem bagItem : goodsList) {
            if (bagItem.getGoodsType().equals("GUARD") && bagItem.getBindProgramId() == programId) {
                hasGuard = true;
                break;
            }
        }
        return hasGuard;
    }

    private boolean userHasVip(List<WelcomeJson.UserBagItem> goodsList) {
        if (ChatRoomInfo.getInstance().getRoomInfoBean() == null || goodsList == null) {
            return false;
        }
        boolean hasVip = false;
        for (WelcomeJson.UserBagItem bagItem : goodsList) {
            if (bagItem.getGoodsType().equals("VIP")) {
                hasVip = true;
                break;
            }
        }
        return hasVip;
    }

    public boolean hasBagCar() {
        if (mWelcomeJson.getContext() == null || mWelcomeJson.getContext().getCarObj() == null) {
            return false;
        }
        return true;
    }

    public String getCarName() {
        if (mWelcomeJson.getContext() == null || mWelcomeJson.getContext().getCarObj() == null) {
            return null;
        }
        return mWelcomeJson.getContext().getCarObj().getGoodsName();
    }

    public int getCarId() {
        if (mWelcomeJson.getContext() == null || mWelcomeJson.getContext().getCarObj() == null) {
            return 0;
        }
        return mWelcomeJson.getContext().getCarObj().getCarPicId();
    }

    public long getPrettyNum() {
        if (mWelcomeJson.getContext() == null || mWelcomeJson.getContext().getCarObj() == null) {
            return 0;
        }
        return mWelcomeJson.getContext().getCarObj().getPrettyNumberOrUserId();
    }

    public String getGoodsColor() {
        if (mWelcomeJson.getContext() == null || mWelcomeJson.getContext().getCarObj() == null) {
            return "default";
        }
        return mWelcomeJson.getContext().getCarObj().getGoodsColor();
    }
}
