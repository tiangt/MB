package com.whzl.mengbi.ui.view;

public interface UserInfoView {
    void showPortrait(String filename);

    void showSuccess(String msg);

    void showError(String msg);

    void onModifyNickname(String nickname);
}
