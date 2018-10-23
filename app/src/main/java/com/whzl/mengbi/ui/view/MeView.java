package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.GetNewTaskBean;
import com.whzl.mengbi.model.entity.UserInfo;

public interface MeView {
    void showUserInfo(UserInfo userInfo);
    void getNewTask(GetNewTaskBean bean);
}
