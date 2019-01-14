package com.whzl.mengbi.presenter;


/**
 * @author cliang
 * @date 2019.1.14
 */
public interface OnBindingFinishedListener {

    void onRegexCodeSuccess(String code,String msg);

    void onError(String msg);
}
