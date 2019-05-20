package com.whzl.mengbi.chat.room.message.messages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.RedPackJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.config.BundleConfig;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.RedbagActivity;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class RedPackPoolMessage implements FillHolderMessage {
    private RedPackJson redPackJson;
    private Context mcontext;
    private int currentProgramId;

    public RedPackPoolMessage(Context context, RedPackJson redPackJson) {
        this.redPackJson = redPackJson;
        this.mcontext = context;
    }

    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        SingleTextViewHolder mholder = (SingleTextViewHolder) holder;
        RedPackJson.ContextBean context = redPackJson.context;
        mholder.textView.setBackgroundResource(R.drawable.bg_chat_normal);
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        mholder.textView.append(LevelUtil.getImageResourceSpan(mcontext, R.drawable.ic_red_pack_chat));
        mholder.textView.append(" 恭喜 ");
        mholder.textView.append(LightSpanString.getLightString(context.sendObjectNickname, Color.rgb(254, 186, 48)));
        mholder.textView.append(" 中得红包基金分红，共获得 ");
        mholder.textView.append(LightSpanString.getLightString(context.awardCoin + "", Color.rgb(255, 87, 5)));
        mholder.textView.append(" 萌币，");
        mholder.textView.append(LightSpanString.getClickSpan(mcontext, "我也要分红！", Color.rgb(252, 233, 3), v -> {
            mcontext.startActivity(new Intent(mcontext, RedbagActivity.class).
                    putExtra("programId", ((LiveDisplayActivity) mcontext).mProgramId));
            ((LiveDisplayActivity) mcontext).closeDrawLayoutNoAnimal();
        }));
    }

    private void jumpToActivity(Context context, String nickName, int programId) {
        if (currentProgramId == programId) {
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.alert);
        dialog.setMessage(context.getString(R.string.jump_live_house, nickName));
        dialog.setNegativeButton(R.string.cancel, null);
        dialog.setPositiveButton(R.string.confirm, (dialog1, which) -> {
            jumpToLive(context, programId);
        });
        dialog.show();
    }

    public void jumpToLive(Context context, int programId) {
        Intent intent = new Intent(context, LiveDisplayActivity.class);
        intent.putExtra(BundleConfig.PROGRAM_ID, programId);
        context.startActivity(intent);
    }

    private void initHead(SingleTextViewHolder mholder) {

    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

    public void setProgramId(int mProgramId) {
        currentProgramId = mProgramId;
    }
}
