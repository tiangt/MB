package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.OpenGuardJson;
import com.whzl.mengbi.chat.room.message.messageJson.ProgramFirstNotifyJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

public class ProgramFirstMessage implements FillHolderMessage {
    private String nickname;
    private Context mContext;


    public ProgramFirstMessage(ProgramFirstNotifyJson notifyJson, Context context) {
        this.nickname = notifyJson.getContext().getNickName();
        this.mContext = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.congratulation));
        mHolder.textView.append(LightSpanString.getLightString("  恭喜 ", Color.parseColor("#f00f0f")));
        mHolder.textView.append(LightSpanString.getLightString(nickname, Color.parseColor("#eff161")));
        mHolder.textView.append(LightSpanString.getLightString(" 成为本场第一富豪", Color.parseColor("#f00f0f")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}

