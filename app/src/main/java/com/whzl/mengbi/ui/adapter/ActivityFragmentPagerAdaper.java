package com.whzl.mengbi.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shaw
 * @date 2018/7/7
 */
public class ActivityFragmentPagerAdaper extends FragmentStatePagerAdapter{
    List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private ArrayList<String> titleList = new ArrayList<>();

    public ActivityFragmentPagerAdaper(FragmentManager fm, ArrayList<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public ActivityFragmentPagerAdaper(FragmentManager fm, ArrayList<BaseFragment> fragmentList, ArrayList<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList.size() == 0) {
            return "";
        }
        return titleList.get(position % titleList.size());
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
