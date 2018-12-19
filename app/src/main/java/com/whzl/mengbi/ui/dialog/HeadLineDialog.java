package com.whzl.mengbi.ui.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.ManagerListFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 头条榜，贡献榜，周星榜
 *
 * @author cliang
 * @date 2018.12.19
 */
public class HeadLineDialog extends BaseAwesomeDialog {

    @BindView(R.id.tab_layout_head)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;

    public static BaseAwesomeDialog newInstance(){
        HeadLineDialog dialog = new HeadLineDialog();
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_head_line;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("头条榜");
        titles.add("贡献榜");
        titles.add("周星榜");
        fragments = new ArrayList<>();
        fragments.add(ManagerListFragment.newInstance());
        fragments.add(ManagerListFragment.newInstance());
        fragments.add(ManagerListFragment.newInstance());
        viewPager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        viewPager.setCurrentItem(0);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setSelectedTabIndicatorWidth(20);
        tabLayout.setupWithViewPager(viewPager);
    }
}
