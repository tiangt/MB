package com.whzl.mengbi.ui.dialog.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/7/8
 */
public class GiftSortMotherFragment extends BaseFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.pager_index_container)
    LinearLayout pagerIndexContainer;

    public static GiftSortMotherFragment newInstance(ArrayList<GiftInfo.GiftDetailInfoBean> giftList) {

        GiftSortMotherFragment fragment = new GiftSortMotherFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("gifts", giftList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_sort_mother;
    }

    @Override
    public void init() {
        ArrayList<GiftInfo.GiftDetailInfoBean> giftList = getArguments().getParcelableArrayList("gifts");
        int pagers = (int) Math.ceil(giftList.size() / 8f);
        setupPagerIndex(pagers);
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < pagers; i++) {
            GiftSortFragment fragment;
            if (i == pagers - 1) {
                ArrayList<GiftInfo.GiftDetailInfoBean> pagerGiftList = new ArrayList<>();
                pagerGiftList.addAll(giftList.subList(i * 8, giftList.size()));
                fragment = GiftSortFragment.newInstance(pagerGiftList);
            } else {
                ArrayList<GiftInfo.GiftDetailInfoBean> pagerGiftList = new ArrayList<>();
                pagerGiftList.addAll(giftList.subList(i * 8, (i + 1) * 8));
                fragment = GiftSortFragment.newInstance(pagerGiftList);
            }
            fragments.add(fragment);
        }
        viewPager.setOffscreenPageLimit(pagers);
        viewPager.setAdapter(new FragmentPagerAdaper(getChildFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pagers < 2){
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
    }

    private void setupPagerIndex(int pagers) {
        if(pagers < 2){
            return;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtil.dip2px(getContext(), 8), UIUtil.dip2px(getContext(), 8));
        for (int i = 0; i < pagers; i++) {
            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.selector_common_pager_index);
            if (i == 0) {
                view.setSelected(true);
            } else {
                params.leftMargin = UIUtil.dip2px(getContext(), 7.5f);
            }
            pagerIndexContainer.addView(view, params);
        }
    }

}
