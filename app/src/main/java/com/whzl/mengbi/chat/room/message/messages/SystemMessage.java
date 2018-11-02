package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.SystemMsgJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.GsonUtils;

/**
 * @author shaw
 * @date 2018/8/28
 */
public class SystemMessage implements FillHolderMessage {
    private SystemMsgJson mSystemMsgJson;
    private Context mContext;
    private String pic;
    private String link;
    private String msg;

    public String getLink() {
        return link;
    }

    public String getPic() {
        return pic;
    }

    public SystemMessage(SystemMsgJson systemMsgJson, Context context) {
        mSystemMsgJson = systemMsgJson;
        mContext = context;
        SystemMsgJson.Message message = GsonUtils.GsonToBean(systemMsgJson.context.message, SystemMsgJson.Message.class);
        if (message == null) {
            return;
        }
        link = message.contentLink;
        pic = message.pic;
        msg = message.message;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder viewHolder = (SingleTextViewHolder) holder;
        viewHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        viewHolder.textView.setText("");
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.system_msg));
        viewHolder.textView.append(LightSpanString.getLightString(" " + msg.replaceAll("\n", ""),
                ContextCompat.getColor(mContext, R.color.text_color_system)));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
