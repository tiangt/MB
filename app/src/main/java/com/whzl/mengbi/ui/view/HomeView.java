package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.RecommendInfo;

public interface HomeView {
    void showBanner(BannerInfo bannerInfo);

    void showRecommend(RecommendInfo recommendInfo);

    void showLiveShow(LiveShowInfo liveShowInfo);

    void onError(String msg);
}
