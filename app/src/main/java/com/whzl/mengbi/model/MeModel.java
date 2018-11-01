package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnMeFinishedListener;

public interface MeModel {
    void doUserInfo(OnMeFinishedListener listener);
    void doNewTask(OnMeFinishedListener listener);
}
