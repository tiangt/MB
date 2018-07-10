package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;

import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.util.LevelUtil;
import com.whzl.mengbi.chat.room.util.NickNameSpan;
import com.whzl.mengbi.ui.viewholder.SingleTextViewHolder;
import com.whzl.mengbi.util.ResourceMap;


public class ChatMessage implements FillHolderMessage{
    private int from_level;
    private int from_uid;
    private String from_nickname;

    private String contentString;
    private Context mContext;

    private String to_nickName;
    private int to_level;
    private int to_uid;

    private SingleTextViewHolder mholder;
    private SpannableString fromSpanString;

    public ChatMessage(ChatCommonJson msgJson, Context context, SpannableString fromSpanString) {
        this.mContext = context;
        from_nickname = msgJson.getFrom_nickname();
        this.fromSpanString = fromSpanString;

        to_nickName = msgJson.getTo_nickname();
        contentString = msgJson.getContent();

        try {
            from_uid = Integer.valueOf(msgJson.getFrom_uid());
            to_uid = Integer.valueOf(msgJson.getTo_uid());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        from_level = LevelUtil.getUserLevel(msgJson.getFrom_json());
        to_level = LevelUtil.getUserLevel(msgJson.getTo_json());

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
            mholder.textView.append(LevelUtil.getLevelSpan(from_level, mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
        }
        if (fromSpanString != null) {
            mholder.textView.append(fromSpanString);
        }
        mholder.textView.append(getNickNameSpan(from_nickname,from_uid));
        mholder.textView.append(":");
        //TODO:表情替换
        mholder.textView.append(contentString);
        //mholder.textView.append(SmileyParser.getInstance().addSmileySpans(contentString));
    }

    private void parseHasRecieverMessage(){
        if(from_uid != 0){
            mholder.textView.append(LevelUtil.getLevelSpan(from_level, mContext, ResourceMap.getResourceMap().getUserLevelIcon(from_level)));
        }
        if (fromSpanString != null) {
            mholder.textView.append(fromSpanString);
        }
        mholder.textView.append(getNickNameSpan(from_nickname,from_uid));
        mholder.textView.append("对");
        mholder.textView.append(LevelUtil.getLevelSpan(to_level, mContext, ResourceMap.getResourceMap().getUserLevelIcon(to_level)));
        mholder.textView.append(getNickNameSpan(to_nickName,to_uid));
        mholder.textView.append("：");
        //TODO:表情替换
        mholder.textView.append(contentString);
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
}
