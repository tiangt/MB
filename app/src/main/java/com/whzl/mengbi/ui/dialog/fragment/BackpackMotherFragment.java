package com.whzl.mengbi.ui.dialog.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.BackpackListBean;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/8
 */
public class BackpackMotherFragment extends BaseFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.pager_index_container)
    LinearLayout pagerIndexContainer;
    @BindView(R.id.tv_backpack_empty)
    TextView tvBackpackEmpty;
    private int pagers;
    private FragmentPagerAdaper adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public static BackpackMotherFragment newInstance() {
        return new BackpackMotherFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_backpack_mother;
    }

    @Override
    public void init() {
//        EventBus.getDefault().register(this);
        adapter = new FragmentPagerAdaper(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pagers < 2) {
                    return;
                }
                for (int i = 0; i < pagers; i++) {
                    pagerIndexContainer.getChildAt(i).setSelected(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getBackPack();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(UserInfoUpdateEvent event) {
//        getBackPack();
//    }

    private void setupPagerIndex(int pagers) {
        if (pagers < 2) {
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 4), UIUtil.dip2px(getContext(), 4));
        for (int i = 0; i < pagers; i++) {
            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.selector_common_pager_index);
            if (i == 0) {
                view.setSelected(true);
            } else {
                params.leftMargin = UIUtil.dip2px(getContext(), 10);
            }
            pagerIndexContainer.addView(view, params);
        }
    }

    private void getBackPack() {
        HashMap params = new HashMap();
        Long userId = (Long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
        params.put("userId", userId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .getBackpack(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<BackpackListBean>() {

                    @Override
                    public void onSuccess(BackpackListBean backpackListBean) {
                        if(getContext() == null){
                            return;
                        }
                        fragments.clear();
                        if (backpackListBean != null && backpackListBean.list != null && backpackListBean.list.size() > 0) {
                            viewPager.setVisibility(View.VISIBLE);
                            tvBackpackEmpty.setVisibility(View.GONE);
                            pagers = (int) Math.ceil(backpackListBean.list.size() / 10f);
                            setupPagerIndex(pagers);
                            for (int i = 0; i < pagers; i++) {
                                BackpackFragment fragment;
                                if (i == pagers - 1) {
                                    ArrayList<BackpackListBean.GoodsDetailBean> pagerGiftList = new ArrayList<>();
                                    pagerGiftList.addAll(backpackListBean.list.subList(i * 10, backpackListBean.list.size()));
                                    fragment = BackpackFragment.newInstance(pagerGiftList);
                                } else {
                                    ArrayList<BackpackListBean.GoodsDetailBean> pagerGiftList = new ArrayList<>();
                                    pagerGiftList.addAll(backpackListBean.list.subList(i * 10, (i + 1) * 10));
                                    fragment = BackpackFragment.newInstance(pagerGiftList);
                                }
                                fragments.add(fragment);
                            }
                        } else {
                            viewPager.setVisibility(View.GONE);
                            tvBackpackEmpty.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int code) {
                        if(getContext() == null){
                            return;
                        }
                        viewPager.setVisibility(View.GONE);
                        tvBackpackEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
