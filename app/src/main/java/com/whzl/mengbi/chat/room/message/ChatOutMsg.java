package com.whzl.mengbi.chat.room.message;

import android.util.Log;


import com.whzl.mengbi.chat.client.ByteableMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import com.google.protobuf.ByteString;




public class ChatOutMsg implements ByteableMessage {
    private String content;
    private String archives_id;
    private String domain;
    private String from_uid;
    private String from_nickname;
    private String to_uid;
    private String to_nickname;
    private short type = 102;
    private String chat_type;

    public ChatOutMsg(String content, String archives_id, String domain, String from_uid,
                      String from_nickname, String to_uid, String to_nickname, String chat_type) {
        this.content = content;
        this.archives_id = archives_id;
        this.domain = domain;
        this.from_uid = from_uid;
        this.from_nickname = from_nickname;
        this.to_uid = to_uid;
        this.to_nickname = to_nickname;
        this.chat_type = chat_type;
    }

    @Override
    public byte[] getMessageBytes() {
        ArrayList<ByteString> params_list = new ArrayList<ByteString>();
        try {
            params_list.add(ByteString.copyFrom(archives_id.getBytes("UTF-8")));
            params_list.add(ByteString.copyFrom(domain.getBytes("UTF-8")));
            params_list.add(ByteString.copyFrom(from_uid.getBytes("UTF-8")));
            params_list.add(ByteString.copyFrom(from_nickname.getBytes("UTF-8")));
            params_list.add(ByteString.copyFrom(to_uid.getBytes("UTF-8")));
            params_list.add(ByteString.copyFrom(to_nickname.getBytes("UTF-8")));
            params_list.add(ByteString.copyFrom(content.getBytes("UTF-8")));
            params_list.add(ByteString.copyFrom(chat_type.getBytes("UTF-8")));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }

        Log.i("chat","============");
        Log.i("chat","archives_id "+archives_id);
        Log.i("chat","domain "+domain);
        Log.i("chat","from_uid "+from_uid);
        Log.i("chat","from_nickname "+from_nickname);
        Log.i("chat","to_uid "+to_uid);
        Log.i("chat","to_nickname "+to_nickname);
        Log.i("chat","content "+content);
        Log.i("chat","chat_type "+chat_type);

        return new MessageWraper(params_list,type).getWrapedMessage();
    }
}
