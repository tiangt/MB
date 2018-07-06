package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.RegisterInfo;

public interface OnRegisterFinishedListener {
    void onRegexCodeSuccess(String code,String msg);

    void onSuccess(RegisterInfo registerInfo);

    void onError(String msg);
}
