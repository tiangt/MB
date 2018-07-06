package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnHomeFinishedListener;

/**
 * Class Note:首页的操作的接口，实现类为LoginModelImpl.相当于MVP模式中的Model层
 */
public interface HomeModel {

    void doBanner(OnHomeFinishedListener listenter);

    void doRecommend(OnHomeFinishedListener listenter);

    void doLiveShow(OnHomeFinishedListener listenter);

}
