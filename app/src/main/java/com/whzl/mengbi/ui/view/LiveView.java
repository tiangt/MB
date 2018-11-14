package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PunishWaysBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;

public interface LiveView {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);

    void onLiveGiftSuccess(GiftInfo giftInfo);

    void onRoomInfoSuccess(RoomInfoBean roomInfoBean);

    void onAudienceSuccess(long count);

    void onFollowHostSuccess();

    void onGetRoomUserInFoSuccess(RoomUserInfo.DataBean data);

    void onSendGiftSuccess();

    void onError(String meg);

    void onGetRunWayListSuccess(RunWayListBean runWayListBean);

    void getGuardListSuccess(GuardListBean guardListBean);

    void onGetProgramFirstSuccess(long userId);

    void onTreasureSuccess(TreasureBoxStatusBean treasureBoxStatusBean);

    void onReceiveTreasureSuccess();

    void onActivityListSuccess(GetActivityBean bean);

    void onPkInfoSuccess(PKResultBean bean);

    void onActivityGrandSuccess(ActivityGrandBean bean);

    void onGetAudienceListSuccess(AudienceListBean.DataBean audienceListBean);

    void onGetTotalGuardSuccess(GuardTotalBean.DataBean guardTotalBean);

    void onGetRoomRankTotalSuccess(RoomRankTotalBean bean);

    void onGetPunishWaysSuccess(PunishWaysBean bean);
}
