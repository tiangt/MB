package com.whzl.mengbi.ui.dialog.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.gen.UsualGiftDao;
import com.whzl.mengbi.greendao.UsualGift;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.GoodsPriceBatchBean;
import com.whzl.mengbi.ui.adapter.FragmentPagerAdaper;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/8
 */
public class CommonMotherFragment extends BaseFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.pager_index_container)
    LinearLayout pagerIndexContainer;
    @BindView(R.id.tv_common_empty)
    TextView tvCommonEmpty;
    @BindView(R.id.ll_common_empty)
    public LinearLayout llBackPack;
    private int pagers;
    private FragmentPagerAdaper adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public static CommonMotherFragment newInstance() {
        return new CommonMotherFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_commom_mother;
    }

    @Override
    public void init() {
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
        getCommonGIft();
    }


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

    private void getCommonGIft() {
        UsualGiftDao usualGiftDao = BaseApplication.getInstance().getDaoSession().getUsualGiftDao();
        List<UsualGift> usualGiftList = usualGiftDao.queryBuilder().where(UsualGiftDao.Properties.UserId.eq(
                Long.parseLong(SPUtils.get(getContext(), "userId", 0L).toString())))
                .orderDesc(UsualGiftDao.Properties.Times)
                .list();
        if (usualGiftList == null || usualGiftList.isEmpty()) {
            viewPager.setVisibility(View.GONE);
            llBackPack.setVisibility(View.VISIBLE);
        } else {
            if (usualGiftList.size() > AppConfig.NUM_TOTAL_GIFT_DIALOG * 2) {
                List<UsualGift> usualGifts = usualGiftList.subList(0, AppConfig.NUM_TOTAL_GIFT_DIALOG * 2);
                getGoodIds(usualGifts);
            } else {
                getGoodIds(usualGiftList);
            }

        }
    }

    private void getGoodIds(List<UsualGift> usualGifts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < usualGifts.size(); i++) {
            if (i != usualGifts.size() - 1) {
                sb.append(usualGifts.get(i).giftId.intValue() + ",");
            } else {
                sb.append(usualGifts.get(i).giftId.intValue() + "");
            }
        }
        getDatasByGoodIds(sb.toString());
    }

    private void getDatasByGoodIds(String string) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("goodsIds", string);
        Map signPramsMap = ParamsUtils.getSignPramsMap(paramsMap);
        ApiFactory.getInstance().getApi(Api.class)
                .goodsPriceBatch(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GoodsPriceBatchBean>(this) {

                    @Override
                    public void onSuccess(GoodsPriceBatchBean goodsPriceBatchBean) {
                        fragments.clear();
                        if (goodsPriceBatchBean != null && goodsPriceBatchBean.list != null && goodsPriceBatchBean.list.size() > 0) {
                            viewPager.setVisibility(View.VISIBLE);
                            llBackPack.setVisibility(View.GONE);
                            pagers = (int) Math.ceil(goodsPriceBatchBean.list.size() / (float) AppConfig.NUM_TOTAL_GIFT_DIALOG);
                            setupPagerIndex(pagers);
                            for (int i = 0; i < pagers; i++) {
                                GiftCommonFragment fragment;
                                if (i == pagers - 1) {
                                    ArrayList<GoodsPriceBatchBean.ListBean> pagerGiftList = new ArrayList<>();
                                    pagerGiftList.addAll(goodsPriceBatchBean.list.subList(i * AppConfig.NUM_TOTAL_GIFT_DIALOG, goodsPriceBatchBean.list.size()));
                                    fragment = GiftCommonFragment.newInstance(pagerGiftList);
                                } else {
                                    ArrayList<GoodsPriceBatchBean.ListBean> pagerGiftList = new ArrayList<>();
                                    pagerGiftList.addAll(goodsPriceBatchBean.list.subList(i * AppConfig.NUM_TOTAL_GIFT_DIALOG, (i + 1) * AppConfig.NUM_TOTAL_GIFT_DIALOG));
                                    fragment = GiftCommonFragment.newInstance(pagerGiftList);
                                }
                                fragments.add(fragment);
                            }
                        } else {
                            viewPager.setVisibility(View.GONE);
                            llBackPack.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ApiResult<GoodsPriceBatchBean> body) {
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
