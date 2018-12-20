package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author cliang
 * @date 2018.12.20
 */
public class ContributionMasterFragment extends BaseFragment {

    @BindView(R.id.tab_contribution)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_contribution)
    ViewPager viewPager;

    public static ContributionMasterFragment newInstance(int programId) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        ContributionMasterFragment fragment = new ContributionMasterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contribution;
    }

    @Override
    public void init() {
        int programId = getArguments().getInt("programId");
        ArrayList<String> titles = new ArrayList<>();
        titles.add("本场榜");
        titles.add("七日榜");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ContributeRankFragment.newInstance("day", programId));
        fragments.add(ContributeRankFragment.newInstance("sevenDay", programId));
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
