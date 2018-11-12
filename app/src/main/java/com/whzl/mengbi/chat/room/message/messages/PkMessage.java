package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2018/11/12
 */
public class PkMessage implements FillHolderMessage {
    private SingleTextViewHolder mholder;
    private Context context;
    private String nickname;
    private String userId;
    private int currentProgramId;
    private int programId;
    private String busiCode;
    private String result;
    private String launchPkUsernickName;
    private String pkUserNickname;
    private String mvpNickname;
    public PkJson pkJson;

    public void setProgramId(int programId) {
        this.currentProgramId = programId;
    }

    public PkMessage(PkJson pkJson, Context context) {
        this.context = context;
        this.pkJson = pkJson;
        busiCode = pkJson.context.busiCode;
        if ("PK_FIRST_BLOOD".equals(busiCode)) {
            nickname = pkJson.context.firstBloodUserDto.nickname;
            userId = pkJson.context.firstBloodUserDto.userId;
            programId = pkJson.context.programId;
        } else if ("PK_RECORD".equals(busiCode)) {
            result = pkJson.context.result;
            launchPkUsernickName = pkJson.context.launchPkUserNickname;
            pkUserNickname = pkJson.context.pkUserNickname;
            mvpNickname = pkJson.context.mvpNickname;
        }
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        this.mholder = (SingleTextViewHolder) holder;
        ((SingleTextViewHolder) holder).textView.setBackgroundResource(R.drawable.bg_chat_normal);
        if ("PK_FIRST_BLOOD".equals(busiCode)) {
            mholder.textView.setText("");
            mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
            mholder.textView.append(LevelUtil.getImageResourceSpanByHeight(context, R.drawable.ic_pk_icon,10));
            mholder.textView.append(" ");
            mholder.textView.append(LightSpanString.getLightString("恭喜 ", Color.parseColor("#f9f9f9")));
            mholder.textView.append(LightSpanString.getNickNameSpan(context, nickname + " ", Long.parseLong(userId), 0, Color.parseColor("#FF3C7AED")));
            mholder.textView.append(programId == currentProgramId ?
                    LightSpanString.getLightString("为我方拿下 ", Color.parseColor("#f9f9f9")) :
                    LightSpanString.getLightString("为对方拿下 ", Color.parseColor("#f9f9f9")));
            mholder.textView.append(LightSpanString.getLightString("FIRST BLOOD", Color.parseColor("#FFFF538C")));
        } else if ("PK_RECORD".equals(busiCode)) {
            mholder.textView.setText("");
            mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
            mholder.textView.append(LevelUtil.getImageResourceSpanByHeight(context, R.drawable.ic_pk_icon,10));
            mholder.textView.append(" ");
            mholder.textView.append(LightSpanString.getLightString("恭喜 ", Color.parseColor("#f9f9f9")));
            mholder.textView.append("V".equals(result) ?
                    LightSpanString.getLightString(launchPkUsernickName + " ", Color.parseColor("#FFFF538C")) :
                    LightSpanString.getLightString(pkUserNickname + " ", Color.parseColor("#FFFF538C"))
            );
            mholder.textView.append(LightSpanString.getLightString("在PK中战胜 ", Color.parseColor("#f9f9f9")));
            mholder.textView.append("V".equals(result) ?
                    LightSpanString.getLightString(pkUserNickname + " ", Color.parseColor("#FFFF538C")) :
                    LightSpanString.getLightString(launchPkUsernickName + " ", Color.parseColor("#FFFF538C"))
            );
            mholder.textView.append(LightSpanString.getLightString("赢下一局。本场最佳MVP为 ", Color.parseColor("#f9f9f9")));
            mholder.textView.append(LightSpanString.getLightString(mvpNickname, Color.parseColor("#FFF8C330")));
        }
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }
}
