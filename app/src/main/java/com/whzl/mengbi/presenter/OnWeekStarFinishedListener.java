package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.WeekStarGiftInfo;
import com.whzl.mengbi.model.entity.WeekStarRankInfo;

/**
 * @author cliang
 * @date 2018.12.27
 */
public interface OnWeekStarFinishedListener {

    void onGiftListSuccess(WeekStarGiftInfo weekStarGiftInfo);

    void onError(String msg);
}
