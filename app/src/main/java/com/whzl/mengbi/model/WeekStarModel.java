package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnWeekStarFinishedListener;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public interface WeekStarModel {

    void doGiftList(OnWeekStarFinishedListener listener);

    void doRankList(String rankIds, String preRound, OnWeekStarFinishedListener listener);
}
