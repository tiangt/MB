package com.whzl.mengbi.ui.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author shaw
 * @date 18/7/07
 */
public abstract class BaseFragment extends Fragment {


    protected Activity mActivity;
    protected View rootView;
    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEnv();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    public abstract int getLayoutId();

    protected void initEnv() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();

    }

    public abstract void init();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    /**
     * 得到可靠地Activity,避免NullPointerException
     *
     * @return
     */
    public Activity getMyActivity() {
        return mActivity;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
