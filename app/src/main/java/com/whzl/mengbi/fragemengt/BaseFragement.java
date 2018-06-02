package com.whzl.mengbi.fragemengt;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.whzl.mengbi.application.BaseAppliaction;


/**
 * @funciton 主要就是为我们所有的 fragment提供公共的行为或事件
 */
public abstract  class BaseFragement extends Fragment {

    private Activity mContext;

    public Context getContext() {
        if (mContext == null) {
            return BaseAppliaction.getInstance();
        }
        return mContext;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
