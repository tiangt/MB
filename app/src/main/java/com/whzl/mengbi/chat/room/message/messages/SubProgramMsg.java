package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.chat.room.message.messageJson.SubProgramJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.NickNameSpan;
import com.whzl.mengbi.ui.widget.view.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

//关注主播消息
public class SubProgramMsg implements FillHolderMessage{
    SubProgramJson subProJson;
    Context mContext;

    public SubProgramMsg(SubProgramJson subProgramJson, Context context) {
        this.subProJson = subProgramJson;
        this.mContext = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        int levelIcon = ResourceMap.getResourceMap().getUserLevelIcon(subProJson.getContext().getLevelValue());
        mholder.textView.append(LevelUtil.getLevelSpan(subProJson.getContext().getLevelValue(), mContext, levelIcon));
        mholder.textView.append(getNickNameSpan(subProJson.getContext().getNickname(), subProJson.getContext().getUserId()));
        mholder.textView.append(getStringSpan("关注了主播"));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

    private SpannableString getNickNameSpan(final String nickName, final int uid){
        SpannableString nickSpan = new SpannableString(nickName);
        NickNameSpan clickSpan = new NickNameSpan(mContext) {
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

    private SpannableString getStringSpan(String content){
        SpannableString strColorSpan = new SpannableString(content);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
        strColorSpan.setSpan(colorSpan,0,strColorSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strColorSpan;
    }
}
