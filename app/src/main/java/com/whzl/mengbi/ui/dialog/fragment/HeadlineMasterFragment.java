package com.whzl.mengbi.ui.dialog.fragment;

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
public class HeadlineMasterFragment extends BaseFragment {

    @BindView(R.id.tab_headline)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_headline)
    ViewPager viewPager;

    public static HeadlineMasterFragment newInstance(){
        HeadlineMasterFragment fragment = new HeadlineMasterFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_headline_master;
    }

    @Override
    public void init() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("本轮头条");
        titles.add("上轮头条");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HeadlineListFragment.newInstance("T"));
        fragments.add(HeadlineListFragment.newInstance("F"));
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
