package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.WeekStarGiftInfo;
import com.whzl.mengbi.model.entity.WeekStarRankInfo;

/**
 * @author cliang
 * @date 2018.12.27
 */
public interface WeekStarListView {

    void showGiftList(WeekStarGiftInfo weekStarGiftInfo);

    void showRankList(WeekStarRankInfo weekStarRankInfo);

    void onError(String msg);
}
