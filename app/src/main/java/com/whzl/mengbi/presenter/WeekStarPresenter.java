package com.whzl.mengbi.presenter;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public interface WeekStarPresenter {

    void getGiftList();

    void getRankList(String rankIds, String preRound);

    void onDestroy();
}
