package com.whzl.mengbi.chat.room.message.events;

/**
 * @author shaw
 * @date 2018/8/7
 */
public class GuardOpenEvent {
    public String avatar;
    public String nickName;
    public long userId;

    public GuardOpenEvent(String avatar, String nickName, long userId) {
        this.avatar = avatar;
        this.nickName = nickName;
        this.userId = userId;
    }
}
