package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RedPackJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class RedPackMessage implements FillHolderMessage {
    private RedPackJson redPackJson;
    private Context context;

    public RedPackMessage(Context context, RedPackJson redPackJson) {
        this.redPackJson = redPackJson;
        this.context = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        RedPackJson.ContextBean context = redPackJson.context;
        initHead(mholder);
        if (context.busiCodeName.equals("RP_RETURN_TO_U")) {
            mholder.textView.append(LightSpanString.getLightString(" " + context.returnObjectNickname, Color.parseColor("#FF2EE9FF")));
            mholder.textView.append(LightSpanString.getLightString(" 未抢到红包余额已退回到您的账号,请注意查收", Color.parseColor("#ffffff")));
        } else if (context.busiCodeName.equals(AppConfig.USER_SEND_REDPACKET)) {
            if ("localroom".equals(context.messageSubType)) {
                mholder.textView.append(LightSpanString.getLightString(" " + context.sendObjectNickname, Color.parseColor("#FF2EE9FF")));
                mholder.textView.append(LightSpanString.getLightString("发了一个", Color.parseColor("#ffffff")));
                mholder.textView.append(LightSpanString.getLightString(context.redPacketType.equals("RANDOM") ? "手气红包," : "普通红包,", Color.parseColor("#FFFC3C79")));
                mholder.textView.append(LightSpanString.getLightString(context.leftSeconds + "秒 ", Color.parseColor("#FFFC3C79")));
                mholder.textView.append(LightSpanString.getLightString("后开抢,速度围观哦！", Color.parseColor("#ffffff")));
            } else {
                if (context.sendObjectType == null) {
                    return;
                }
                if (context.sendObjectType.equals("USER")) {
                    mholder.textView.append(LightSpanString.getLightString(" " + context.sendObjectNickname, Color.parseColor("#FF2EE9FF")));
                    mholder.textView.append(LightSpanString.getLightString(" 在 ", Color.parseColor("#ffffff")));
                    mholder.textView.append(LightSpanString.getJumpNameSpan(this.context, context.founderUserNickname, context.programId, Color.parseColor("#FF2EE9FF")));
                    mholder.textView.append(LightSpanString.getLightString(" 的直播间发了一个", Color.parseColor("#ffffff")));
                    mholder.textView.append(LightSpanString.getLightString(context.redPacketType.equals("RANDOM") ? "手气红包," : "普通红包,", Color.parseColor("#FFFC3C79")));
                    mholder.textView.append(LightSpanString.getLightString(context.leftSeconds + "秒 ", Color.parseColor("#FFFC3C79")));
                    mholder.textView.append(LightSpanString.getLightString("后开抢,速度围观哦！", Color.parseColor("#ffffff")));
                }
            }
        } else if (context.busiCodeName.equals(AppConfig.OFFICIAL_SEND_REDPACKET)) {
            mholder.textView.append(LightSpanString.getLightString(" 萌比直播官方", Color.parseColor("#FF78CBFF")));
            mholder.textView.append(LightSpanString.getLightString(" 发了一个", Color.parseColor("#ffffff")));
            mholder.textView.append(LightSpanString.getLightString("红包", Color.parseColor("#FFFC3C79")));
            mholder.textView.append(LightSpanString.getLightString(context.leftSeconds + "秒 ", Color.parseColor("#FFFC3C79")));
            mholder.textView.append(LightSpanString.getLightString("后开抢,速度围观哦！", Color.parseColor("#ffffff")));

        } else if (context.busiCodeName.equals(AppConfig.PROGRAM_TREASURE_SEND_REDPACKET)) {
            mholder.textView.append(LightSpanString.getJumpNameSpan(this.context, " " + context.founderUserNickname, context.programId, Color.parseColor("#FFFF7E97")));
            mholder.textView.append(LightSpanString.getLightString(" 发了一个", Color.parseColor("#ffffff")));
            mholder.textView.append(LightSpanString.getLightString("红包", Color.parseColor("#FFFC3C79")));
            mholder.textView.append(LightSpanString.getLightString(context.leftSeconds + "秒 ", Color.parseColor("#FFFC3C79")));
            mholder.textView.append(LightSpanString.getLightString("后开抢,速度围观哦！", Color.parseColor("#ffffff")));
        } else if (context.busiCodeName.equals(AppConfig.OPEN_REDPACKET)) {
            mholder.textView.append(LightSpanString.getLightString(" 恭喜 ", Color.parseColor("#ffffff")));
            mholder.textView.append(LightSpanString.getLightString(context.openUserNickname, Color.parseColor("#FFFC3C79")));
            mholder.textView.append(LightSpanString.getLightString(" 抢到了 ", Color.parseColor("#ffffff")));
            mholder.textView.append(LightSpanString.getLightString(context.sendObjectNickname, Color.parseColor("#FFFC3C79")));
            mholder.textView.append(LightSpanString.getLightString("发的", Color.parseColor("#ffffff")));
            mholder.textView.append(LightSpanString.getLightString(context.openAmount + " 萌币", Color.parseColor("#FFFF5705")));
        }
    }

    private void initHead(SingleTextViewHolder mholder) {
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(context, R.drawable.ic_red_pack_chat));
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

}
