package com.whzl.mengbi.presenter;

public interface HomePresenter {

    void getBanner();

    void getRecommend();

    void getAnchorList(int pager);

    void onDestroy();
}
