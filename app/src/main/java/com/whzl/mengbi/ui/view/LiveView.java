package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.model.entity.AnchorWishBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GetDailyTaskStateBean;
import com.whzl.mengbi.model.entity.GetUserSetBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PunishWaysBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomRedPackTreasure;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.UpdownAnchorBean;

public interface LiveView {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);

    void onLiveGiftSuccess(GiftInfo giftInfo);

    void onRoomInfoSuccess(RoomInfoBean roomInfoBean);

    void onFollowHostSuccess();

    void onGetRoomUserInFoSuccess(RoomUserInfo.DataBean data);

    void onSendGiftSuccess();

    void onError(String meg);

    void onGetRunWayListSuccess(RunWayListBean runWayListBean);

    void onGetProgramFirstSuccess(long userId);

    void onActivityListSuccess(GetActivityBean bean);

    void onPkInfoSuccess(PKResultBean bean);

    void onActivityGrandSuccess(ActivityGrandBean bean);

    void onGetAudienceListSuccess(AudienceListBean.DataBean audienceListBean);

    void onGetTotalGuardSuccess(GuardTotalBean.DataBean guardTotalBean);


    void onGetRoomRankTotalSuccess(RoomRankTotalBean bean);

    void onGetPunishWaysSuccess(PunishWaysBean bean);

    void onGetAnchorTaskSuccess(AnchorTaskBean dataBean);

    void onGetDailyTaskStateSuccess(GetDailyTaskStateBean dailyTaskStateBean);

    void onGetHeadlineRankSuccess(HeadlineRankBean dataBean);

    void onGetBlackRoomTimeSuccess(BlackRoomTimeBean dataBean);

    void onGetUsetSetSuccesd(GetUserSetBean dataBean);

    void onGetRoomRedpackTreasureSuccess(RoomRedPackTreasure dataBean);

    void onGetRoomRedListSuccess(RoomRedpackList dataBean);

    void onActivityNativeSuccess(GetActivityBean bean);

    void onAnchorWishSuccess(AnchorWishBean bean);

    void onSendGiftNoMoney();

    void onUpdownAnchors(UpdownAnchorBean jsonElement);

    void onRightBottomActivityError();
}
