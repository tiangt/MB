package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.CompositeJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author cliang
 * @date 2019.1.15
 */
public class CompositeMessage implements FillHolderMessage {

    private Context context;
    private CompositeJson compositeJson;
    private String nickName;
    private String chipName;
    private String chipCount;
    private String chipUnit;

    public CompositeMessage(Context context, CompositeJson compositeJson) {
        this.context = context;
        this.compositeJson = compositeJson;
        this.nickName = compositeJson.context.nickname;
        this.chipName = compositeJson.context.awardContentName;
        this.chipCount = compositeJson.context.awardContentNum;
        this.chipUnit = compositeJson.context.awardContentUnit;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mHolder = (SingleTextViewHolder) holder;
        mHolder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mHolder.textView.setText("");
        mHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mHolder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_chat_composite));
        mHolder.textView.append(LightSpanString.getLightString(" 功夫不负有心人，恭喜 ", Color.parseColor("#ffffff")));
        mHolder.textView.append(LightSpanString.getLightString(nickName, Color.parseColor("#FED501")));
        mHolder.textView.append(LightSpanString.getLightString(" 合成 ", Color.parseColor("#ffffff")));
        mHolder.textView.append(LightSpanString.getLightString(chipName + " ", Color.parseColor("#FED501")));
        mHolder.textView.append(LightSpanString.getLightString(chipCount, Color.parseColor("#ffffff")));
        mHolder.textView.append(LightSpanString.getLightString(chipUnit, Color.parseColor("#ffffff")));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
