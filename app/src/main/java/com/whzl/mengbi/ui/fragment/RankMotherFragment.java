package com.whzl.mengbi.ui.fragment;

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
 * @author shaw
 * @date 2018/8/22
 */
public class RankMotherFragment extends BaseFragment {
    @BindView(R.id.period_tab)
    TabLayout periodTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static RankMotherFragment newInstance(String rankName) {
        Bundle args = new Bundle();
        args.putString("rankName", rankName);
        RankMotherFragment rankMotherFragment = new RankMotherFragment();
        rankMotherFragment.setArguments(args);
        return rankMotherFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rank_mother;
    }

    @Override
    public void init() {
        Bundle args = getArguments();
        String rankName = args.getString("rankName");
        ArrayList<String> titles = new ArrayList<>();
        titles.add("今日");
        titles.add("本周");
        titles.add("本月");
        titles.add("昨日");
        titles.add("上周");
        titles.add("上月");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(RankListFragment.newInstance(rankName, "DAY", "F"));
        fragments.add(RankListFragment.newInstance(rankName, "WEEK", "F"));
        fragments.add(RankListFragment.newInstance(rankName, "MONTH", "F"));
        fragments.add(RankListFragment.newInstance(rankName, "DAY", "T"));
        fragments.add(RankListFragment.newInstance(rankName, "WEEK", "T"));
        fragments.add(RankListFragment.newInstance(rankName, "MONTH", "T"));
        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        periodTab.setupWithViewPager(viewpager);
        periodTab.clearOnTabSelectedListeners();
        periodTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition(), false);
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
