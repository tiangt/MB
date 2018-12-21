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
 * 周星榜
 *
 * @author cliang
 * @date 2018.12.21
 */
public class WeekStarMasterFragment extends BaseFragment {

    @BindView(R.id.tab_week_star)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_week_star)
    ViewPager viewPager;

    public static WeekStarMasterFragment newInstance(){
        WeekStarMasterFragment fragment = new WeekStarMasterFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_week_star_master;
    }

    @Override
    public void init() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("本周");
        titles.add("上周");
        titles.add("规则");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(WeekStarListFragment.newInstance());
        fragments.add(WeekStarListFragment.newInstance());
        fragments.add(WeekStarRuleFragment.newInstance());
        viewPager.setOffscreenPageLimit(2);
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
