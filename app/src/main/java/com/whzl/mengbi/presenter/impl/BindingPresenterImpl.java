package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.BindingModel;
import com.whzl.mengbi.model.impl.BindingModelImpl;
import com.whzl.mengbi.presenter.BindingPresenter;
import com.whzl.mengbi.presenter.OnBindingFinishedListener;
import com.whzl.mengbi.ui.view.BindingPhoneView;

/**
 * @author cliang
 * @date 2019.1.14
 */
public class BindingPresenterImpl implements BindingPresenter, OnBindingFinishedListener {

    private BindingPhoneView bindingView;
    private BindingModel bindingModel;

    public BindingPresenterImpl(BindingPhoneView bindingView) {
        this.bindingView = bindingView;
        bindingModel = new BindingModelImpl();
    }

    @Override
    public void getRegexCode(String mobile) {
        bindingModel.doRegexCode(mobile, this);
    }

    @Override
    public void onDestroy() {
        bindingView = null;
    }

    @Override
    public void onRegexCodeSuccess(String code, String msg) {
        if (bindingView != null) {
            bindingView.showRegexCodeMsg(code, msg);
        }
    }

    @Override
    public void onError(String msg) {
        if (bindingView != null) {
            bindingView.onError(msg);
        }
    }
}
