package com.whzl.mengbi.chat.room.message.messages;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.TypedValue;

import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.ui.widget.view.SingleTextViewHolder;
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

        from_level = getUserLevel(msgJson.getFrom_json());
        to_level = getUserLevel(msgJson.getTo_json());

    }



    private int getUserLevel(FromJson fromJson){
        if(fromJson == null){
            return -1;
        }
        List<FromJson.Level> levelList = fromJson.getLevelList();
        if(levelList == null){
            return -1;
        }
        //TODO:先判断是不是主播,再取等级数据
        for(FromJson.Level levelItem : levelList){
            if(levelItem.getLevelType().equals("USER_LEVEL")){
                int levelValue = levelItem.getLevelValue();
                return levelValue;
            }
        }
        return -1;
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
            mholder.textView.append(getLevelSpan(from_level));
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
            mholder.textView.append(getLevelSpan(from_level));
        }
        if (fromSpanString != null) {
            mholder.textView.append(fromSpanString);
        }
        mholder.textView.append(getNickNameSpan(from_nickname,from_uid));
        mholder.textView.append("对");
        mholder.textView.append(getLevelSpan(to_level));
        mholder.textView.append(getNickNameSpan(to_nickName,to_uid));
        mholder.textView.append("：");
        //TODO:表情替换
        mholder.textView.append(contentString);
        //mholder.textView.append(SmileyParser.getInstance().addSmileySpans(contentString));
    }

    private SpannableString getLevelSpan(int level){
        SpannableString levelIcon = new SpannableString("levelIcon");
        Resources res = mContext.getResources();
        int resid = ResourceMap.getResourceMap().getUserLevelIcon(level);
        Drawable levelIconDrawable = res.getDrawable(resid);

        if(levelIconDrawable == null){
            return levelIcon;
        }
        int originWidth = levelIconDrawable.getIntrinsicWidth();
        int originHeight = levelIconDrawable.getIntrinsicHeight();

        float ratio = (float)originWidth / (float)originHeight;

        float heightpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,mContext.getResources().getDisplayMetrics());

        float widthpx = heightpx * ratio;

        levelIconDrawable.setBounds(0,0,(int)widthpx,(int)heightpx);
        levelIcon.setSpan(new ImageSpan(levelIconDrawable),0,levelIcon.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return levelIcon;
    }

    private SpannableString getNickNameSpan(final String nickName, final int uid){
        SpannableString nickSpan = new SpannableString(nickName);
//        NickNameSpan clickSpan = new NickNameSpan(mContext) {
//            @Override
//            public void onClick(View widget) {
//                Log.i("test","点击了 "+nickName);
//                new EnterUserPop().enterUserPop(mContext,uid, ChatRoomInfo.getProgramId());
//            }
//        };

        //nickSpan.setSpan(clickSpan,0,nickSpan.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
