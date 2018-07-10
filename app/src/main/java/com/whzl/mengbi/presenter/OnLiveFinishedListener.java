package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;

public interface OnLiveFinishedListener {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);
    void onLiveGiftSuccess(GiftInfo giftInfo);
    void onLiveFaceSuccess(EmjoyInfo emjoyInfo);
    void onRoomInfoSuccess(RoomInfoBean roomInfoBean);
    void onAudienceSuccess(long audienceAccount);
    void onSuccess();
    void onFellowHostSuccess();
    void onEroor();
}
