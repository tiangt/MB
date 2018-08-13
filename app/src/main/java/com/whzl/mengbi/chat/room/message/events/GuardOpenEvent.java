package com.whzl.mengbi.chat.room.message.events;

/**
 * @author shaw
 * @date 2018/8/7
 */
public class GuardOpenEvent {
    public String Avatar;
    public String nickName;

    public GuardOpenEvent(String avatar, String nickName) {
        Avatar = avatar;
        this.nickName = nickName;
    }
}
