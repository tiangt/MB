package com.whzl.mengbi.chat.room.message;

import android.util.Log;

import com.google.protobuf.ByteString;
import com.whzl.mengbi.chat.client.ByteableMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginMessage implements ByteableMessage {
    private String archives_id;
    private String domain;
    private String uid;
    private String token="";
    private String dev_type="{\"type\":3}";
    private short type = 101;

    public LoginMessage(String archives_id, String domain, long uid, String token){
        this.archives_id = archives_id;
        this.domain = domain;
        this.uid = uid + "";
        this.token = token;

        Log.d("login","archives_id "+ archives_id +" domain " + domain + " uid " + uid + " token " + token);
    }

    @Override
    public byte[] getMessageBytes() {
        List<ByteString> paramsList = new ArrayList<>();
        try {

            paramsList.add( ByteString.copyFrom(archives_id.getBytes("UTF-8")));
            paramsList.add(  ByteString.copyFrom(domain.getBytes("UTF-8")));
            paramsList.add(  ByteString.copyFrom(uid.getBytes("UTF-8")));
            paramsList.add(  ByteString.copyFrom(token.getBytes("UTF-8")));
            paramsList.add(  ByteString.copyFrom(dev_type.getBytes("UTF-8")));
            /*paramsList.add(ByteString.of(archives_id.getBytes("UTF-8")));
            paramsList.add(ByteString.of(domain.getBytes("UTF-8")));
            paramsList.add(ByteString.of(uid.getBytes("UTF-8")));
            paramsList.add(ByteString.of(token.getBytes("UTF-8")));
            paramsList.add(ByteString.of(dev_type.getBytes("UTF-8")));
            */
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        }
        return new MessageWraper(paramsList,type).getWrapedMessage();
    }
}
