package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnWeekStarFinishedListener;

/**
 * @author cliang
 * @date 2018.12.27
 */
public interface WeekStarModel {

    void doGiftList(OnWeekStarFinishedListener listener);

    void doRankList(String type, String preRound, OnWeekStarFinishedListener listener);
}
