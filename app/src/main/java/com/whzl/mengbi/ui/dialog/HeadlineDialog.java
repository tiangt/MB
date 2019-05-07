package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.ContributionMasterFragment;
import com.whzl.mengbi.ui.dialog.fragment.HeadlineMasterFragment;
import com.whzl.mengbi.ui.dialog.fragment.ManagerListFragment;
import com.whzl.mengbi.ui.dialog.fragment.WeekStarMasterFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * 头条榜，贡献榜，周星榜
 *
 * @author cliang
 * @date 2018.12.19
 */
public class HeadlineDialog extends BaseAwesomeDialog {

    @BindView(R.id.tab_layout_head)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;


    public static BaseAwesomeDialog newInstance(int index, int programId, int anchorId, String nickName, String avatar) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
        args.putInt("index", index);
        args.putInt("anchorId", anchorId);
        args.putString("nickName", nickName);
        args.putString("avatar", avatar);
        HeadlineDialog dialog = new HeadlineDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_head_line;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
        int index = getArguments().getInt("index");
        int programId = getArguments().getInt("programId");
        int anchorId = getArguments().getInt("anchorId");
        String nickName = getArguments().getString("nickName");
        String avatar = getArguments().getString("avatar");
        ArrayList<String> titles = new ArrayList<>();
        titles.add("头条榜");
        titles.add("贡献榜");
        titles.add("周星榜");
        fragments = new ArrayList<>();
        fragments.add(HeadlineMasterFragment.newInstance(anchorId, nickName, avatar, programId));
        fragments.add(ContributionMasterFragment.newInstance(programId));
        fragments.add(WeekStarMasterFragment.newInstance(anchorId, nickName, avatar, programId));
        viewPager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        viewPager.setCurrentItem(index);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setSelectedTabIndicatorWidth(com.whzl.mengbi.util.UIUtil.dip2px(getActivity(), 25));
        tabLayout.setupWithViewPager(viewPager);

        holder.setOnClickListener(R.id.iv_colse_head_line, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismissDialog();
            }
        });
    }
}
