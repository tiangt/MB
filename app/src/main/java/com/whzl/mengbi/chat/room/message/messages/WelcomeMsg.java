package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.chat.room.util.NickNameSpan;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

import java.util.List;

public class WelcomeMsg implements FillHolderMessage {
    private String nickName;
    private int uid;
    private int userLevel;
    private List<SpannableString> userSpanList;
    private Context mContext;
    private boolean isAnchor = false;
    private boolean hasGuard = false;

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
        }
        this.userLevel = getUserLevel(uid, welcomeJson.getContext().getInfo().getLevelList());
        this.userSpanList = userSpanList;
        this.hasGuard = userHasGuard(welcomeJson.getContext().getInfo().getUserBagList());
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        //mHolder.textView.append(getLightStr("欢迎"));
        if (uid != 0) {
            int levelIcon = 0;
            if (isAnchor) {
                levelIcon = ResourceMap.getResourceMap().getAnchorLevelIcon(userLevel);
            }else {
                levelIcon = ResourceMap.getResourceMap().getUserLevelIcon(userLevel);
            }
            mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, levelIcon));
            mHolder.textView.append(" ");
        }
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
        mHolder.textView.append(getNickNameSpan(nickName, uid));
        mHolder.textView.append(LightSpanString.getLightString(" 进入直播间",Color.WHITE));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

    private int getUserLevel(int uid, List<WelcomeJson.WelcomeLevelListItem> levelList) {
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

    private SpannableString getNickNameSpan(final String nickName, final int uid) {
        SpannableString nickSpan = new SpannableString(nickName);
        NickNameSpan clickSpan = new NickNameSpan(mContext,  Color.parseColor("#f4be2c")) {
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
