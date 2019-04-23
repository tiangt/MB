package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RankChangeJson;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

import java.util.HashMap;

/**
 * @author nobody
 * @date 2019/4/23
 */
public class RankChangeMessage implements FillHolderMessage {
    private RankChangeJson rankChangeJson;
    private Context context;
    private HashMap<String, SpannableString> levelIcon;

    public RankChangeMessage(Context context, RankChangeJson rankChangeJson, HashMap<String, SpannableString> levelIcon) {
        this.context = context;
        this.rankChangeJson = rankChangeJson;
        this.levelIcon = levelIcon;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mHolder.textView.setText("");
        if (rankChangeJson.context.msgInfoList != null && !rankChangeJson.context.msgInfoList.isEmpty()) {
            for (int i = 0; i < rankChangeJson.context.msgInfoList.size(); i++) {
                RankChangeJson.ContextBean.MsgInfoListBean msgInfoListBean = rankChangeJson.context.msgInfoList.get(i);
                if (msgInfoListBean.msgType.equals("TEXT")) {
                    mHolder.textView.append(LightSpanString.getLightString(msgInfoListBean.msgValue, Color.parseColor("#" + msgInfoListBean.color)));
                } else if (msgInfoListBean.msgType.equals("IMG")) {
                    mHolder.textView.append(levelIcon.get(msgInfoListBean.msgValue));
                }
            }
        }
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
