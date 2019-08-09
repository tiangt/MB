package com.whzl.mengbi.presenter;

public interface HomePresenter {

    void getBanner();

    void getRecommend();

    void getAnchorList(int pager,String sortProperty);

    void getHeadlineTop();

    void onDestroy();
}
