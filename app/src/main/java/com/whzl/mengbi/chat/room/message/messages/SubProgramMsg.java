package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

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
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        int levelIcon = ResourceMap.getResourceMap().getUserLevelIcon(subProJson.getContext().getLevelValue());
        mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, levelIcon));
        mholder.textView.append(LightSpanString.getNickNameSpan(mContext," " + subProJson.getContext().getNickname() + " ", subProJson.getContext().getUserId(), programId));
        mholder.textView.append(LightSpanString.getLightString("关注了主播", Color.parseColor("#f1275b")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

}
