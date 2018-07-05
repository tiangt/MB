package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;

public interface LiveView {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);

    void onLiveFaceSuccess(EmjoyInfo emjoyInfo);

    void onLiveGiftSuccess(GiftInfo giftInfo);
}
