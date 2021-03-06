package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.RoyalCarListBean;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseFullScreenDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.ManagerListFragment;
import com.whzl.mengbi.ui.dialog.fragment.UserListFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;
import com.whzl.mengbi.util.UserIdentity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

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

    private ArrayList<String> titles;
    private ArrayList<Fragment> fragments;
    private AudienceListBean.DataBean audienceBean;
    private RoyalCarListBean royalCarListBean;

    public static BaseFullScreenDialog newInstance(AudienceListBean.DataBean audienceBean, RoyalCarListBean royalCarListBean) {
        Bundle args = new Bundle();
        args.putParcelable("audienceBean", audienceBean);
        args.putParcelable("royalCarListBean", royalCarListBean);
        UserListDialog dialog = new UserListDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_user_list;
    }

    @Override
    public void convertView(ViewHolder holder, BaseFullScreenDialog dialog) {
        if (getArguments() != null) {
            audienceBean = getArguments().getParcelable("audienceBean");
            royalCarListBean = getArguments().getParcelable("royalCarListBean");
        }

        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("玩家列表");
        titles.add("房管列表");
        fragments.add(UserListFragment.newInstance(audienceBean, royalCarListBean));
        fragments.add(ManagerListFragment.newInstance(audienceBean,royalCarListBean));
        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        viewpager.setCurrentItem(0);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setSelectedTabIndicatorWidth(20);
        tabLayout.setupWithViewPager(viewpager);
        getManagerAmount(audienceBean);
    }

    @OnClick({R.id.ib_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setUserTitle(long userAmount) {
        tabLayout.getTabAt(0).setText("玩家列表（" + userAmount + "）");
    }

    public void setManagerTitle(int managerAmount) {
        tabLayout.getTabAt(1).setText("房管列表（" + managerAmount + "）");
    }

    private void getManagerAmount(AudienceListBean.DataBean audienceBean) {
        if (audienceBean == null || audienceBean.getList() == null || audienceBean.getList().size() == 0) {
            setUserTitle(0);
        } else {
            setUserTitle(audienceBean.total);
        }

        if (audienceBean == null || audienceBean.getList() == null || audienceBean.getList().size() == 0) {
            setManagerTitle(0);
        } else {
            int num = 0;
            for (int i = 0; i < audienceBean.getList().size(); i++) {
                int mIdentity = audienceBean.getList().get(i).getIdentity();
                if (mIdentity == UserIdentity.OPTR_MANAGER || mIdentity == UserIdentity.ROOM_MANAGER) {
                    num += 1;
                }
            }
            setManagerTitle(num);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragments.clear();
        fragments = null;
    }
}
