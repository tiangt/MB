package com.whzl.mengbi.presenter;

public interface OnUserInfoFInishedListener {
    void onPortraitSuccess(String filename);

    void onSuccess(String nickname);

    void onError(String msg);

    void onModifyNicknameSuc(String nickname);
}
