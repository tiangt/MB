package com.whzl.mengbi.chat.room;

/**
 * Created by qishui on 15/6/18.
 */
public class ChatToUser{
    int uid;
    String nickName;

    public ChatToUser(int uid, String nickName) {
        this.uid = uid;
        this.nickName = nickName;
    }

    public int getUid() {
        return uid;
    }

    public String getNickName() {
        return nickName;
    }
}
