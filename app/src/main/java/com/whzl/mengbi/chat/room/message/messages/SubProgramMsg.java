package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.SubProgramJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

//关注主播消息
public class SubProgramMsg implements FillHolderMessage{
    private SubProgramJson subProJson;
    private Context mContext;
    private int programId;

    public SubProgramMsg(SubProgramJson subProgramJson, Context context) {
        this.subProJson = subProgramJson;
        this.mContext = context;
        if (ChatRoomInfo.getInstance().getRoomInfoBean() != null) {
            programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        }
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        int levelIcon = ResourceMap.getResourceMap().getUserLevelIcon(subProJson.getContext().getLevelValue());
        mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, levelIcon));
        mholder.textView.append(LightSpanString.getNickNameSpan(mContext, " " + subProJson.getContext().getNickname() + " ", subProJson.getContext().getUserId(), programId, ContextCompat.getColor(mContext, R.color.text_color_focus_nick)));
        mholder.textView.append(LightSpanString.getLightString("订阅了主播", ContextCompat.getColor(mContext, R.color.text_color_focus)));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

}
