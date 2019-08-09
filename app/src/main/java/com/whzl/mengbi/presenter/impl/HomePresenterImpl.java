package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.HomeModel;
import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.model.entity.HeadlineTopInfo;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.model.impl.HomeModelImpl;
import com.whzl.mengbi.presenter.HomePresenter;
import com.whzl.mengbi.presenter.OnHomeFinishedListener;
import com.whzl.mengbi.ui.view.HomeView;
import com.whzl.mengbi.util.LogUtils;

public class HomePresenterImpl implements HomePresenter, OnHomeFinishedListener {
    private HomeView homeView;
    private HomeModel homeModel;


    public HomePresenterImpl(HomeView homeView) {
        this.homeView = homeView;
        homeModel = new HomeModelImpl();
    }

    @Override
    public void getBanner() {
        homeModel.doBanner(this);
    }

    @Override
    public void getRecommend() {
        homeModel.doRecommend(this);
    }

    @Override
    public void getAnchorList(int pager,String sortProperty) {
        homeModel.doAnchorList(pager,sortProperty, this);
    }

    @Override
    public void getHeadlineTop() {
        homeModel.doHeadlineTop(this);
    }

    @Override
    public void onBannerSuccess(BannerInfo bannerInfo) {
        if (homeView != null) {
            homeView.showBanner(bannerInfo);
        }
    }

    @Override
    public void onRecommendSuccess(RecommendInfo recommendInfo) {
        if (homeView != null) {
            homeView.showRecommend(recommendInfo);
        }
    }

    @Override
    public void onLiveShowSuccess(LiveShowInfo liveShowInfo) {
        if (homeView != null) {
            homeView.showLiveShow(liveShowInfo);
        }
    }

    @Override
    public void onHeadlineTopSuccess(HeadlineTopInfo headlineTopInfo) {
        if (homeView != null) {
            homeView.showHeadlineTop(headlineTopInfo);
        }
    }

    @Override
    public void onError(String msg) {
        if (homeView != null) {
            homeView.onError(msg);
        }
    }


    @Override
    public void onDestroy() {
        homeView = null;
    }
}
