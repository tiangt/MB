package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.FollowSortFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我守护/管理的主播
 *
 * @author cliang
 * @date 2019.2.20
 */
public class MyGuardActivity extends BaseActivity {

    @BindView(R.id.tab_follow)
    TabLayout tabFollow;
    @BindView(R.id.vp_follow)
    ViewPager vpFollow;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private ArrayList<String> titles;
    private FragmentPagerAdaper fragmentPagerAdaper;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_my_guard);
    }

    @Override
    protected void setupView() {
        titles = new ArrayList<>();
        titles.add("我守护的");
        titles.add("我管理的");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(FollowSortFragment.newInstance(FollowSortFragment.GUARD));
        fragments.add(FollowSortFragment.newInstance(FollowSortFragment.MANAGE));
        fragmentPagerAdaper = new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles);
        vpFollow.setAdapter(fragmentPagerAdaper);
        vpFollow.setOffscreenPageLimit(2);
        tabFollow.setTabMode(TabLayout.MODE_FIXED);
        tabFollow.setTabGravity(TabLayout.GRAVITY_FILL);
        tabFollow.setNeedSwitchAnimation(true);
        tabFollow.setSelectedTabIndicatorWidth(com.whzl.mengbi.util.UIUtil.dip2px(this, 25));
        tabFollow.setupWithViewPager(vpFollow);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.iv_back)
    public void onClick(View view){
        switch (R.id.iv_back){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
