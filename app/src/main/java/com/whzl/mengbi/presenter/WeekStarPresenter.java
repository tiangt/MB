package com.whzl.mengbi.presenter;

/**
 * @author cliang
 * @date 2018.12.27
 */
public interface WeekStarPresenter {

    void getGiftList();

    void getRankList(String type, String preRound);

    void onDestroy();
}
