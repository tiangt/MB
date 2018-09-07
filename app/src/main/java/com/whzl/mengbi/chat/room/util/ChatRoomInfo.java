package com.whzl.mengbi.chat.room.util;

import com.whzl.mengbi.model.entity.RoomInfoBean;

public class ChatRoomInfo {

    private long programFirstId;
    private RoomInfoBean roomInfoBean;

    private static class ChatRoomInfoHolder {
        private static final ChatRoomInfo instance = new ChatRoomInfo();
    }

    public static final ChatRoomInfo getInstance(){
        return ChatRoomInfoHolder.instance;
    }

    public RoomInfoBean getRoomInfoBean() {
        return roomInfoBean;
    }

    public void setRoomInfoBean(RoomInfoBean roomInfoBean) {
        this.roomInfoBean = roomInfoBean;
    }

    public void setProgramFirstId(long programFirstId) {
        this.programFirstId = programFirstId;
    }

    public long getProgramFirstId() {
        return programFirstId;
    }
}
