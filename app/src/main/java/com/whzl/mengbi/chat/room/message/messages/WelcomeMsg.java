package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.NickNameSpan;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

import java.util.List;

public class WelcomeMsg implements FillHolderMessage {
    String nickName;
    int uid;
    int userLevel;
    SpannableString userSpanString;
    Context mContext;
    boolean isAnchor;

    public WelcomeMsg(WelcomeJson welcomeJson, Context context, SpannableString userSpanString) {
        this.nickName = welcomeJson.getContext().getInfo().getNickname();
        this.uid = welcomeJson.getContext().getInfo().getUserId();
        mContext = context;
        //TODO:判断主播or用户
        isAnchor = false;
        this.userLevel = getUserLevel(uid, welcomeJson.getContext().getInfo().getLevelList());
        this.userSpanString = userSpanString;
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
            mHolder.textView.append(LevelUtil.getLevelSpan(levelIcon, mContext, levelIcon));
        }
        if (null != userSpanString) {
            mHolder.textView.append(userSpanString);
        }
        mHolder.textView.append(getNickNameSpan(nickName, uid));
        mHolder.textView.append("进入直播间");
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
        NickNameSpan clickSpan = new NickNameSpan(mContext) {
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

    private SpannableString getLightStr(String content) {
        //文本内容
        SpannableString ss = new SpannableString(content);
        //设置字符颜色
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#f1275b")), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
