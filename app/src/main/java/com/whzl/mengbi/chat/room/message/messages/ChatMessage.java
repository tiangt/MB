package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.chat.room.util.FaceReplace;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.NickNameSpan;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;

import java.util.List;


public class ChatMessage implements FillHolderMessage{
    private int from_level;
    private int from_uid;
    private String from_nickname;

    private String contentString;
    private Context mContext;

    private String to_nickName;
    private int to_level;
    private int to_uid;

    private boolean isAnchor = false;
    private boolean hasGuard = false;

    private SingleTextViewHolder mholder;
    private List<SpannableString> fromSpanList;

    public ChatMessage(ChatCommonJson msgJson, Context context, List<SpannableString> fromSpanList) {
        this.mContext = context;
        from_nickname = msgJson.getFrom_nickname();
        this.fromSpanList = fromSpanList;

        to_nickName = msgJson.getTo_nickname();
        contentString = msgJson.getContent();

        try {
            from_uid = Integer.valueOf(msgJson.getFrom_uid());
            to_uid = Integer.valueOf(msgJson.getTo_uid());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        from_level = LevelUtil.getUserLevel(msgJson.getFrom_json());
        int anchorUid = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getAnchor().getId();
        if (anchorUid == from_uid) {
            isAnchor = true;
            from_level = LevelUtil.getAnchorLevel(msgJson.getFrom_json());
        }
        to_level = LevelUtil.getUserLevel(msgJson.getTo_json());
        hasGuard = hasGuard(msgJson.getFrom_json().getGoodsList());
    }


    @Override
    public void fillHolder(RecyclerView.ViewHolder holder) {
        this.mholder = (SingleTextViewHolder) holder;
        mholder.textView.setText("");
        mholder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        if(to_uid == 0){
            parseNoRecieverMessage();
        }else{
            parseHasRecieverMessage();
        }
    }

    private void parseNoRecieverMessage(){
        //非游客发言
        if(from_uid != 0){
            if (isAnchor) {
                mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getAnchorLevelIcon(from_level)));
            }else {
                mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
            }
            mholder.textView.append(" ");
        }
        if (hasGuard) {
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, R.drawable.guard));
            mholder.textView.append(" ");
        }
        if (fromSpanList != null) {
            for(SpannableString spanString:fromSpanList) {
                mholder.textView.append(spanString);
                mholder.textView.append(" ");
            }
        }
        mholder.textView.append(getNickNameSpan(from_nickname + ":  ",from_uid));
        SpannableString spanString = getLightStr(contentString, Color.WHITE);
        //TODO:表情替换
        FaceReplace.getInstance().faceReplace(mholder.textView, spanString, mContext);
        mholder.textView.append(spanString);
    }

    private void parseHasRecieverMessage(){
        if(from_uid != 0){
            mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
        }
        if (fromSpanList != null) {
            for(SpannableString spanString:fromSpanList) {
                mholder.textView.append(spanString);
                mholder.textView.append("  ");
            }
        }
        mholder.textView.append(getNickNameSpan(from_nickname,from_uid));
        mholder.textView.append("对");
        mholder.textView.append(LevelUtil.getImageResourceSpan(mContext, ResourceMap.getResourceMap().getUserLevelIcon(to_level)));
        mholder.textView.append(getNickNameSpan(to_nickName,to_uid));
        mholder.textView.append("： ");
        //TODO:表情替换
        mholder.textView.append(getLightStr(contentString, Color.WHITE));
        //mholder.textView.append(SmileyParser.getInstance().addSmileySpans(contentString));
    }

    private SpannableString getNickNameSpan(final String nickName, final int uid){
        SpannableString nickSpan = new SpannableString(nickName);
        NickNameSpan clickSpan = new NickNameSpan(mContext) {
            @Override
            public void onClick(View widget) {
                Log.i("chatMsg","点击了 "+nickName);
                //TODO:弹窗
                //new EnterUserPop().enterUserPop(mContext,uid, ChatRoomInfo.getProgramId());
            }
        };

        nickSpan.setSpan(clickSpan,0,nickSpan.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return nickSpan;
    }

    @Override
    public int getHolderType() {
        return SINGLE_TEXTVIEW;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public String getContentString() {
        return contentString;
    }

    private SpannableString getLightStr(String content, int color) {
        //文本内容
        SpannableString ss = new SpannableString(content);
        //设置字符颜色
        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(DensityUtil.dp2px(15)), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private SpannableString replaceFaceList(SpannableString content) {
        SpannableString faceContentSpanString = new SpannableString(content);
        List<EmjoyInfo.FaceBean> faceList;
        return faceContentSpanString;
    }

    private boolean hasGuard(List<FromJson.Good> goodsList) {
        int programId = ChatRoomInfo.getInstance().getRoomInfoBean().getData().getProgramId();
        boolean hasGuard = false;
        for(FromJson.Good good: goodsList) {
            if (good.getGoodsType().equals("GUARD") && good.getBindProgramId() == programId) {
                hasGuard = true;
                break;
            }
        }
        return hasGuard;
    }
}
