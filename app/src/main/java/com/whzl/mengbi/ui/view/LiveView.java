package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;

public interface LiveView {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);

    void onLiveFaceSuccess(EmjoyInfo emjoyInfo);

    void onLiveGiftSuccess(GiftInfo giftInfo);

    void onRoomInfoSuccess(RoomInfoBean roomInfoBean);

    void onAudienceSuccess(long count);

    void onFollowHostSuccess();

    void onGetRoomUserInFoSuccess(RoomUserInfo.DataBean data);
}
