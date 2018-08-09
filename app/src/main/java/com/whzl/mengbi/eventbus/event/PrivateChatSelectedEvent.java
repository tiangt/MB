package com.whzl.mengbi.eventbus.event;

import com.whzl.mengbi.model.entity.RoomUserInfo;

/**
 * @author shaw
 * @date 2018/8/8
 */
public class PrivateChatSelectedEvent {

    public RoomUserInfo.DataBean getDataBean() {
        return dataBean;
    }

    private RoomUserInfo.DataBean dataBean;

    public PrivateChatSelectedEvent(RoomUserInfo.DataBean data) {
        dataBean = data;
    }
}
