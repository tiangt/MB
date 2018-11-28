package com.whzl.mengbi.ui.fragment;

import android.os.Bundle;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

/**
 * @author nobody
 * @date 2018/11/28
 */
public class LiveWeekRankFragment extends BaseFragment {
    public static LiveWeekRankFragment newInstance() {
        LiveWeekRankFragment weekRankFragment = new LiveWeekRankFragment();
        Bundle bundle = new Bundle();
        weekRankFragment.setArguments(bundle);
        return weekRankFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_live_week_rank;
    }

    @Override
    public void init() {

    }
}
