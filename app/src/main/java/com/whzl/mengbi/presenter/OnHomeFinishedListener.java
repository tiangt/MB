package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.model.entity.HeadlineTopInfo;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.RecommendInfo;

public interface OnHomeFinishedListener {
    void onBannerSuccess(BannerInfo bannerInfo);

    void onRecommendSuccess(RecommendInfo recommendInfo);

    void onLiveShowSuccess(LiveShowInfo liveShowInfo);

    void onHeadlineTopSuccess(HeadlineTopInfo headlineTopInfo);

    void onError(String msg);
}
