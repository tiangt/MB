package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.RegisterInfo;

public interface RegisterView {
    void showRegexCodeMsg(String code,String msg);

    void navigateToAll(RegisterInfo registerInfo);

    void onError(String msg);
}
