package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.GetNewTaskBean;
import com.whzl.mengbi.model.entity.UserInfo;

public interface OnMeFinishedListener {
    void onSuccess(UserInfo userInfo);
    void onNewTaskSuccess(GetNewTaskBean bean);
}
