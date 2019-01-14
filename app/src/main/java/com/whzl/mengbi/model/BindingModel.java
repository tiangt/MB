package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnBindingFinishedListener;

/**
 * @author cliang
 * @date 2019.1.14
 */
public interface BindingModel {

    void doRegexCode(String mobile, OnBindingFinishedListener listener);
}
