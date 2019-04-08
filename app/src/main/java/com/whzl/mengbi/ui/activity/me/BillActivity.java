package com.whzl.mengbi.ui.activity.me;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.me.BillAwardFragment;
import com.whzl.mengbi.ui.fragment.me.BillGiftFragment;
import com.whzl.mengbi.ui.fragment.me.BillPayFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/10/16
 */
public class BillActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_bill, "账单", true);
    }

    @Override
    protected void setupView() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("消费记录");
        titles.add("送礼记录");
        titles.add("奖励记录");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(BillPayFragment.newInstance());
        fragments.add(BillGiftFragment.newInstance());
        fragments.add(BillAwardFragment.newInstance());
        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(new FragmentPagerAdaper(getSupportFragmentManager(), fragments, titles));
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setSelectedTabIndicatorWidth(UIUtil.dip2px(this, 23));
        tabLayout.setupWithViewPager(viewpager);
    }


    @Override
    protected void loadData() {

    }

}
