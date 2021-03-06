package com.whzl.mengbi.presenter;

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
import com.whzl.mengbi.model.entity.PkGuessBean;
import com.whzl.mengbi.model.entity.PkQualifyingBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.model.entity.RoomRedpacketBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RoyalCarListBean;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.UpdownAnchorBean;

public interface OnLiveFinishedListener {
    void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo);

    void onLiveGiftSuccess(GiftInfo giftInfo);

    void onRoomInfoSuccess(RoomInfoBean roomInfoBean);

    void onSuccess();

    void onFellowHostSuccess();

    void onError(String mes);

    void onGetRoomUserInfoSuccess(RoomUserInfo.DataBean data);

    void onSendGiftSuccess(boolean useBag);

    void onGetRunWayListSuccess(RunWayListBean runWayListBean);

    void onGetProgramFirstSuccess(long userId);

    void onActivityListSuccess(GetActivityBean bean);

    void onPkInfoSuccess(PKResultBean bean);

    void onGetAudienceListSuccess(AudienceListBean.DataBean audienceListBean);

    void onGetTotalGuardSuccess(GuardTotalBean.DataBean bean);

    void onGetRoomRankTotalSuccess(RoomRankTotalBean bean);

    void onGetDailyTaskStateSuccuss(GetDailyTaskStateBean dailyTaskStateBean);

    void onGetHeadlineRankSuccess(HeadlineRankBean dataBean);

    void onGetBlackRoomTime(BlackRoomTimeBean dataBean);

    void onGetUserSet(GetUserSetBean dataBean);

    void onGetRoomRedList(RoomRedpackList dataBean);

    void onActivityNativeSuccess(GetActivityBean bean);

    void onAnchorWishSuccess(AnchorWishBean jsonElement);

    void onSendGiftNoMoney();

    void onUpdownAnchors(UpdownAnchorBean jsonElement);

    void onRoyalCarListSuccess(RoyalCarListBean jsonElement);

    void onRightBottomActivitySuccuss(Object o);

    void onQualifyingSuccess(PkQualifyingBean anchorInfoBean);

    void onRoomGameRedpacketSuccess(RoomRedpacketBean jsonElement);

    void onPkGuessSuccess(PkGuessBean pkGuessBean);
}
