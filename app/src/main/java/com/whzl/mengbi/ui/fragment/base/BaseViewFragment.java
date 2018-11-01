package com.whzl.mengbi.ui.fragment.base;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * @author cliang
 * @date 2018.10.24
 */
public class BaseViewFragment extends Fragment {

    protected String tag = "BaseViewFragment";
    private int index;
    protected Activity mActivity;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    /**
     * 得到可靠地Activity,避免NullPointerException
     *
     * @return
     */
    public Activity getMyActivity() {
        return mActivity;
    }
}
