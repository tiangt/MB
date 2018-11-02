package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PkInfoBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;

public interface OnLiveFinishedListener {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);
    void onLiveGiftSuccess(GiftInfo giftInfo);
    void onRoomInfoSuccess(RoomInfoBean roomInfoBean);
    void onAudienceSuccess(long audienceAccount);
    void onSuccess();
    void onFellowHostSuccess();
    void onError(String mes);

    void onGetRoomUserInfoSuccess(RoomUserInfo.DataBean data);

    void onSendGiftSuccess();

    void onGetRunWayListSuccess(RunWayListBean runWayListBean);

    void onGetGuardListSuccess(GuardListBean guardListBean);

    void onGetProgramFirstSuccess(long userId);

    void onTreasureStatusSuccess(TreasureBoxStatusBean treasureBoxStatusBean);

    void onReceiveTreasureSuccess();

    void onActivityListSuccess(GetActivityBean bean);

    void onPkInfoSuccess(PkInfoBean bean);

    void onActivityGrandSuccess(ActivityGrandBean bean);

    void onGetAudienceListSuccess(AudienceListBean.DataBean audienceListBean);
}
