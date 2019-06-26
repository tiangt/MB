package com.whzl.mengbi.util.guide.lifecycle;

import android.app.Fragment;

import com.whzl.mengbi.util.guide.util.LogUtil;


public class ListenerFragment extends Fragment {

    FragmentLifecycle mFragmentLifecycle;

    public void setFragmentLifecycle(FragmentLifecycle lifecycle) {
        mFragmentLifecycle = lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("onStart: ");
        if (mFragmentLifecycle != null) mFragmentLifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFragmentLifecycle != null) mFragmentLifecycle.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentLifecycle != null) mFragmentLifecycle.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("onDestroy: ");
        if (mFragmentLifecycle != null) mFragmentLifecycle.onDestroy();
    }
}
