package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;

public interface OnLiveFinishedListener {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);
    void onLiveGiftSuccess(GiftInfo giftInfo);
    void onLiveFaceSuccess(EmjoyInfo emjoyInfo);
    void onSuccess();
    void onEroor();
}
