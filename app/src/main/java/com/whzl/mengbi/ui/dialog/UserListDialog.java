package com.whzl.mengbi.ui.dialog;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseFullScreenDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.AudienceListFragment;
import com.whzl.mengbi.ui.dialog.fragment.GuardListFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;
import com.whzl.mengbi.util.UIUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * 用户，房管列表
 *
 * @author cliang
 * @date 2018.12.18
 */
public class UserListDialog extends BaseFullScreenDialog {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private int mProgramId;
    private RoomInfoBean.DataBean.AnchorBean mAnchorBean;
    private ArrayList<String> titles;
    private FragmentPagerAdaper mAdapter;
    private ArrayList<Fragment> fragments;

    public static BaseFullScreenDialog newInstance(){
        UserListDialog dialog = new UserListDialog();
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_user_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseFullScreenDialog dialog) {
        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.user_list,0));
        titles.add(getString(R.string.room_manager_list,0));
        fragments = new ArrayList<>();
        fragments.add(GuardListFragment.newInstance(mProgramId));
        fragments.add(AudienceListFragment.newInstance(mProgramId));
        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        viewpager.setCurrentItem(0);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setSelectedTabIndicatorWidth(20);
        tabLayout.setupWithViewPager(viewpager);

    }

    private void settab(TabLayout tabLayout) throws NoSuchFieldException, IllegalAccessException {
        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout llTab = (LinearLayout) tabStrip.get(tabLayout);
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            int screenWidth = UIUtil.getScreenWidthPixels(getContext());
            int indexWidth = UIUtil.dip2px(getContext(), 100);
            int leftMargin = (int) ((screenWidth - indexWidth * 2) / 4f + 0.5);
            params.leftMargin = (leftMargin);
            params.rightMargin = (leftMargin);
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
