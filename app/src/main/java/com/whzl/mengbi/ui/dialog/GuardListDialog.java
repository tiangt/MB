package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.GuardDetailDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.AudienceListFragment;
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
    private int mProgramId;
    private RoomInfoBean.DataBean.AnchorBean mAnchorBean;
    private ArrayList<String> titles;
    private FragmentPagerAdaper mAdapter;
    private ArrayList<Fragment> fragments;

    public static BaseAwesomeDialog newInstance(int programId, RoomInfoBean.DataBean.AnchorBean anchorBean, int index) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        args.putInt("index", index);
        args.putParcelable("anchor", anchorBean);
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
        mProgramId = getArguments().getInt("programId");
        int index = getArguments().getInt("index");
        mAnchorBean = getArguments().getParcelable("anchor");
        tabLayout.post(() -> {
            try {
                settab(tabLayout);
            } catch (Exception e) {
            }
        });
        titles = new ArrayList<>();
        titles.add("守护");
        titles.add("观众");
        fragments = new ArrayList<>();
        fragments.add(GuardListFragment.newInstance(mProgramId));
        fragments.add(AudienceListFragment.newInstance(mProgramId));
        mAdapter = new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles);
        viewpager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(index);
    }

    @OnClick(R.id.btn_guard)
    public void onClick() {
        dismiss();
        GuardDetailDialog.newInstance(mProgramId, mAnchorBean)
                .setShowBottom(true)
                .setDimAmount(0)
                .show(getFragmentManager());
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
            int indexWidth = UIUtil.dip2px(getContext(), 150);
            int leftMargin = (int) ((screenWidth - indexWidth * 2) / 4f + 0.5);
            params.leftMargin = (leftMargin);
            params.rightMargin = (leftMargin);
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public void setGuardTitle(int size) {
        tabLayout.getTabAt(0).setText("守护（" + size + "）");
    }

    public void setAudienceTitle(int size) {
        tabLayout.getTabAt(1).setText("观众（" + size + "）");
    }
}
