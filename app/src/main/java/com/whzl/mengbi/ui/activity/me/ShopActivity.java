package com.whzl.mengbi.ui.activity.me;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/9/18
 */
public class ShopActivity extends BaseActivity {
    public static String SELECT = "select";
    @BindView(R.id.tab_shop)
    TabLayout tabShop;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private int currentSelectedIndex = 0;
    private ArrayList<String> titles;
    private ArrayList<Fragment> fragments;
    public UserInfo.DataBean currentUser;

    @Override
    protected void initEnv() {
        super.initEnv();
        int select = getIntent().getIntExtra(SELECT, 0);
        currentSelectedIndex = select;
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_shop, "购物商城", true);
    }

    @Override
    protected void setupView() {
        initViewPager();
    }

    private void initViewPager() {
        titles = new ArrayList<>();
        titles.add("道具");
        titles.add("VIP");
        titles.add("靓号");
        titles.add("贵族");

        fragments = new ArrayList<>();
        fragments.add(new PropFragment());
        fragments.add(new VipFragment());
        fragments.add(new GoodnumFragment());
        fragments.add(new NobilityFragment());


        tabShop.setTabMode(TabLayout.MODE_FIXED);
        tabShop.setTabGravity(TabLayout.GRAVITY_FILL);
        tabShop.setNeedSwitchAnimation(true);
        tabShop.setSelectedTabIndicatorWidth(UIUtil.dip2px(this, 23));

        FragmentPagerAdaper fragmentPagerAdaper = new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(fragmentPagerAdaper);
        viewpager.setOffscreenPageLimit(4);
        tabShop.setupWithViewPager(viewpager);
        viewpager.postDelayed(() -> viewpager.setCurrentItem(currentSelectedIndex), 300);
    }


    @Override
    protected void loadData() {
        Long userId = (Long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
        BusinessUtils.getUserInfo(this, String.valueOf(userId), new BusinessUtils.UserInfoListener() {
            @Override
            public void onSuccess(UserInfo.DataBean bean) {
                currentUser = bean;
            }

            @Override
            public void onError(int code) {

            }
        });
    }

}
