package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.AudienceListFragment;
import com.whzl.mengbi.ui.dialog.fragment.ContributeRankFragment;
import com.whzl.mengbi.ui.dialog.fragment.GuardListFragment;
import com.whzl.mengbi.util.UIUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/8/3
 */
public class GuardListDialog extends BaseAwesomeDialog {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static BaseAwesomeDialog newInstance(int programId) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        GuardListDialog dialog = new GuardListDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_guard_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        int programId = getArguments().getInt("programId");
        tabLayout.post(() -> {
            try {
                settab(tabLayout);
            } catch (Exception e) {
            }
        });
        ArrayList<String> titles = new ArrayList<>();
        titles.add("守护");
        titles.add("观众");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(GuardListFragment.newInstance(programId));
        fragments.add(AudienceListFragment.newInstance(programId));
        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewpager);
    }

    @OnClick(R.id.btn_guard)
    public void onClick() {
    }

    private void settab(TabLayout tabLayout) throws NoSuchFieldException, IllegalAccessException {
        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            int screenWidth = UIUtil.getScreenWidthPixels(getContext());
            int indexWidth = UIUtil.dip2px(getContext(), 80);
            int leftMargin = (int) ((screenWidth - indexWidth * 2) / 4f + 0.5);
            params.leftMargin = (leftMargin);
            params.rightMargin = (leftMargin);
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
