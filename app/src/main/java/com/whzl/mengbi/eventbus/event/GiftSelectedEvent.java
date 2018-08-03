package com.whzl.mengbi.eventbus.event;

import com.whzl.mengbi.model.entity.GiftInfo;

/**
 * @author shaw
 * @date 2018/7/11
 */
public class GiftSelectedEvent {
    public Object object;
    public GiftSelectedEvent(GiftInfo.GiftDetailInfoBean giftDetailInfoBean) {
        this.giftDetailInfoBean = giftDetailInfoBean;
    }

    public GiftInfo.GiftDetailInfoBean giftDetailInfoBean;
}
