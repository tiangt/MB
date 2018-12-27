package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.WeekStarModel;
import com.whzl.mengbi.model.entity.WeekStarGiftInfo;
import com.whzl.mengbi.model.entity.WeekStarRankInfo;
import com.whzl.mengbi.model.impl.WeekStarModelImpl;
import com.whzl.mengbi.presenter.OnWeekStarFinishedListener;
import com.whzl.mengbi.presenter.WeekStarPresenter;
import com.whzl.mengbi.ui.view.WeekStarListView;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class WeekStarPresenterImpl implements WeekStarPresenter, OnWeekStarFinishedListener {

    private WeekStarListView weekStarListView;
    private WeekStarModel weekStarModel;

    public WeekStarPresenterImpl(WeekStarListView weekStarView) {
        this.weekStarListView = weekStarView;
        weekStarModel = new WeekStarModelImpl();
    }

    @Override
    public void onGiftListSuccess(WeekStarGiftInfo weekStarGiftInfo) {
        if (weekStarListView != null) {
            weekStarListView.showGiftList(weekStarGiftInfo);
        }
    }

    @Override
    public void onRankListSuccess(WeekStarRankInfo weekStarRankInfo) {
        if (weekStarListView != null) {
            weekStarListView.showRankList(weekStarRankInfo);
        }
    }

    @Override
    public void getGiftList() {
        weekStarModel.doGiftList(this);
    }

    @Override
    public void getRankList(String rankIds, String preRound) {
        weekStarModel.doRankList(rankIds, preRound, this);
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onDestroy() {

    }
}
