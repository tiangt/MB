package com.whzl.mengbi.eventbus.event;

import com.whzl.mengbi.model.entity.AudienceListBean;

/**
 * @author nobody
 * @date 2019-06-24
 */
public class AudienceEvent {
    public final AudienceListBean.DataBean bean;

    public AudienceEvent(AudienceListBean.DataBean bean) {
        this.bean = bean;
    }
}
