package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.dialog.base.BaseFullScreenDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.dialog.fragment.AudienceListFragment;
import com.whzl.mengbi.ui.dialog.fragment.GuardListFragment;
import com.whzl.mengbi.ui.dialog.fragment.ManagerListFragment;
import com.whzl.mengbi.ui.dialog.fragment.UserListFragment;
import com.whzl.mengbi.ui.widget.tablayout.TabLayout;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.UserIdentity;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private Disposable disposable;
    private ArrayList<String> titles;
    private ArrayList<Fragment> fragments;
    private List<AudienceListBean.AudienceInfoBean> audienceInfoBeans = new ArrayList<>();
    private int mIdentity;

    public static BaseFullScreenDialog newInstance(int programId) {
        Bundle args = new Bundle();
        args.putInt("programId", programId);
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
        mProgramId = getArguments().getInt("programId");
        titles = new ArrayList<>();
        titles.add("玩家列表");
        titles.add("房管列表");
        fragments = new ArrayList<>();
        fragments.add(UserListFragment.newInstance(mProgramId));
        fragments.add(ManagerListFragment.newInstance(mProgramId));
        viewpager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments, titles));
        viewpager.setCurrentItem(0);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setSelectedTabIndicatorWidth(20);
        tabLayout.setupWithViewPager(viewpager);

        getManagerAmount();
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

    private void getManagerAmount(){
        disposable = Observable.interval(0, 60, TimeUnit.SECONDS).subscribe((Long aLong) -> {
            mProgramId = getArguments().getInt("programId");
            HashMap paramsMap = new HashMap();
            paramsMap.put("programId", mProgramId);
            HashMap signPramsMap = ParamsUtils.getSignPramsMap(paramsMap);
            ApiFactory.getInstance().getApi(Api.class)
                    .getAudienceList(signPramsMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<AudienceListBean.DataBean>() {
                        @Override
                        public void onSuccess(AudienceListBean.DataBean dataBean) {
                            if (dataBean != null) {
                                audienceInfoBeans.clear();
                                for (int i = 0; i < dataBean.getList().size(); i++) {
                                    mIdentity = dataBean.getList().get(i).getIdentity();
                                    if (mIdentity == UserIdentity.OPTR_MANAGER || mIdentity == UserIdentity.ROOM_MANAGER) {
                                        audienceInfoBeans.add(dataBean.getList().get(i));
                                    }
                                }
                                if(audienceInfoBeans != null){
                                    setManagerTitle(audienceInfoBeans.size());
                                }
                            }
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
