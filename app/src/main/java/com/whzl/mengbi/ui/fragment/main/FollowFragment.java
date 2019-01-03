package com.whzl.mengbi.ui.fragment.main;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.FollowSortFragment;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author nobody
 * @date 2018/12/18
 */
public class FollowFragment extends BaseFragment {
    @BindView(R.id.tab_follow)
    TabLayout tabFollow;
    @BindView(R.id.vp_follow)
    ViewPager vpFollow;
    @BindView(R.id.ib_clear)
    ImageButton ibClear;
    private ArrayList<String> titles;
    private FragmentPagerAdaper fragmentPagerAdaper;

    public static Fragment newInstance() {
        FollowFragment followFragment = new FollowFragment();
        return followFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_follow_main;
    }

    @Override
    public void init() {
        titles = new ArrayList<>();
        titles.add("订阅");
        titles.add("守护");
        titles.add("管理");
        titles.add("看过");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(com.whzl.mengbi.ui.fragment.FollowFragment.newInstance());
        fragments.add(FollowSortFragment.newInstance("guard"));
        fragments.add(FollowSortFragment.newInstance("manage"));
        fragments.add(FollowSortFragment.newInstance("watch"));
        fragmentPagerAdaper = new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles);
        vpFollow.setAdapter(fragmentPagerAdaper);
        vpFollow.setOffscreenPageLimit(4);
        vpFollow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    ibClear.setVisibility(View.VISIBLE);
                } else {
                    ibClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabFollow.setTabMode(TabLayout.MODE_FIXED);
        tabFollow.setTabGravity(TabLayout.GRAVITY_FILL);
        tabFollow.setNeedSwitchAnimation(true);
        tabFollow.setSelectedTabIndicatorWidth(com.whzl.mengbi.util.UIUtil.dip2px(getContext(), 11));
        tabFollow.setupWithViewPager(vpFollow);
    }


    @OnClick({R.id.ib_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_clear:
                FollowSortFragment item = (FollowSortFragment) fragmentPagerAdaper.getItem(3);
                item.clickIbClear();
                break;
        }
    }

}
