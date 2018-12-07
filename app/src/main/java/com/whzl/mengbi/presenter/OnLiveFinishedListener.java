package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GetDailyTaskStateBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;

public interface OnLiveFinishedListener {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);

    void onLiveGiftSuccess(GiftInfo giftInfo);

    void onRoomInfoSuccess(RoomInfoBean roomInfoBean);

    void onSuccess();

    void onFellowHostSuccess();

    void onError(String mes);

    void onGetRoomUserInfoSuccess(RoomUserInfo.DataBean data);

    void onSendGiftSuccess();

    void onGetRunWayListSuccess(RunWayListBean runWayListBean);

    void onGetProgramFirstSuccess(long userId);

    void onActivityListSuccess(GetActivityBean bean);

    void onPkInfoSuccess(PKResultBean bean);

    void onActivityGrandSuccess(ActivityGrandBean bean);

    void onGetAudienceListSuccess(AudienceListBean.DataBean audienceListBean);

    void onGetTotalGuardSuccess(GuardTotalBean.DataBean bean);

    void onGetRoomRankTotalSuccess(RoomRankTotalBean bean);

    void onGetAnchorTaskSuccess(AnchorTaskBean dataBean);

    void onGetDailyTaskStateSuccuss(GetDailyTaskStateBean dailyTaskStateBean);
}
