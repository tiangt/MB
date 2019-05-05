package com.whzl.mengbi.eventbus.event;

import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * @author nobody
 * @date 2019/4/25
 */
public class QuickPayEvent {
    public BaseResp baseResp;

    public QuickPayEvent(BaseResp baseResp) {
        this.baseResp = baseResp;
    }
}
