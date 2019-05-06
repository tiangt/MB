package com.whzl.mengbi.ui.dialog.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.ClearAllAnim;
import com.whzl.mengbi.eventbus.event.GiftSelectedEvent;
import com.whzl.mengbi.eventbus.event.LiveHouseUserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.BackpackListBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GoodsDetailBean;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/9
 */
public class BackpackFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private BaseListAdapter giftAdapter;
    private boolean flagOnMessageEvent = true;
    private ArrayList<GoodsDetailBean> mDatas = new ArrayList<>();
    private int selectedPosition = -1;

    private int selectId = -1;
    private AnimatorSet animatorSetsuofang;


    public static BackpackFragment newInstance(ArrayList<GoodsDetailBean> pagerGiftList) {
        BackpackFragment fragment = new BackpackFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("data", pagerGiftList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_sort;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        mDatas = getArguments().getParcelableArrayList("data");
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), AppConfig.NUM_SPAN_GIFT_DIALOG));
        giftAdapter = new BaseListAdapter() {
            @Override
            protected int getDataCount() {
                return mDatas == null ? 0 : mDatas.size();
            }

            @Override
            protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_live_house_backpack, parent, false);
                return new GoodsViewHolder(itemView);
            }
        };
        recycler.setAdapter(giftAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (animatorSetsuofang != null && animatorSetsuofang.isRunning()) {
            animatorSetsuofang.cancel();
            animatorSetsuofang.end();
            animatorSetsuofang = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GiftSelectedEvent event) {
        if (!flagOnMessageEvent) {
            flagOnMessageEvent = true;
            return;
        }
        flagOnMessageEvent = false;
        selectedPosition = -1;
        giftAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveHouseUserInfoUpdateEvent event) {
        getBackPack();
    }

    class GoodsViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_gift)
        ImageView ivGift;
        @BindView(R.id.tv_gift_name)
        TextView tvGiftName;
        @BindView(R.id.tv_cost)
        TextView tvCost;
        @BindView(R.id.view_select_mark)
        View selectedMark;
        @BindView(R.id.rl)
        RelativeLayout rl;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GoodsDetailBean goodsDetailBean = mDatas.get(position);
            GlideImageLoader.getInstace().displayImage(getContext(), goodsDetailBean.goodsPic, ivGift);
            tvGiftName.setText(goodsDetailBean.goodsName);
//            tvCost.setText("数量 ");
//            SpannableString spannableString = StringUtils.spannableStringColor(goodsDetailBean.count + "", Color.parseColor("#fdc809"));
            tvCost.setText(goodsDetailBean.count + "");
//            selectedMark.setSelected(position == selectedPosition);
            if (position == selectedPosition) {
//                try {
//                    GifDrawable drawable = new GifDrawable(getResources(), R.drawable.bg_live_house_select);
                rl.setBackgroundResource(R.drawable.bg_live_house_gift_bg);
//                ((ImageView) holder.getView(R.id.iv)).setImageDrawable(drawable);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            holder.getView(R.id.rl).setBackground();
            } else {
                rl.setBackground(null);
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemLongClick(view, position);
            if (selectedPosition == position) {
                return;
            }
            EventBus.getDefault().post(new ClearAllAnim());
            selectedPosition = position;
            if (recycler.getChildCount() > 0) {
                for (int i = 0; i < recycler.getChildCount(); i++) {
                    recycler.getChildAt(i).findViewById(R.id.rl).setBackground(null);
                }
            }
//            GifDrawable drawable = null;
//            try {
//                drawable = new GifDrawable(getResources(), R.drawable.bg_live_house_select);
            (view.findViewById(R.id.rl)).setBackgroundResource(R.drawable.bg_live_house_gift_bg);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            startAnimal(ivGift, position);
            GoodsDetailBean goodsDetailBean = mDatas.get(position);
            GiftInfo.GiftDetailInfoBean giftDetailInfoBean = new GiftInfo.GiftDetailInfoBean();
            giftDetailInfoBean.setGoodsId(goodsDetailBean.goodsId);
            selectId = goodsDetailBean.goodsId;
            giftDetailInfoBean.setBackpack(true);
            flagOnMessageEvent = false;
            EventBus.getDefault().post(new GiftSelectedEvent(giftDetailInfoBean));
        }
    }

    public void startAnimal(View imageView, int position) {
        animatorSetsuofang = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.2f, 1f, 0.8f, 1f);
        scaleX.setRepeatCount(-1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.2f, 1f, 0.8f, 1f);
        scaleY.setRepeatCount(-1);

        animatorSetsuofang.setDuration(1500);
        animatorSetsuofang.setInterpolator(new LinearInterpolator());
        animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
        animatorSetsuofang.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                giftAdapter.setSelectedPosition(position);
            }
        });
        animatorSetsuofang.start();
    }

    private void getBackPack() {
        HashMap params = new HashMap();
        Long userId = (Long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
        params.put("userId", userId);
        Map signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .getBackpack(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<BackpackListBean>(this) {

                    @Override
                    public void onSuccess(BackpackListBean backpackListBean) {
                        if (backpackListBean != null && backpackListBean.list != null) {
                            ((BackpackMotherFragment) getParentFragment()).llBackPack.setVisibility(View.GONE);
//                            for (int i = 0; i < backpackListBean.list.size(); i++) {
//                                for (int j = 0; j < mDatas.size(); j++) {
//                                    if(mDatas.get(j).goodsId == backpackListBean.list.get(i).goodsId){
//                                        mDatas.get(j).count = backpackListBean.list.get(i).count;
//                                    }
//                                }
//                            }

                            int check = check(backpackListBean.list, selectId);
                            if (check != -1) {
                                for (int i = 0; i < mDatas.size(); i++) {
                                    if (mDatas.get(i).goodsId == selectId) {
                                        mDatas.get(i).count = check;
                                        ((TextView)recycler.getChildAt(i).findViewById(R.id.tv_cost)).setText(mDatas.get(i).count+"");
                                    }
                                }
//                                giftAdapter.notifyDataSetChanged();
                            } else {
                                for (int i = 0; i < mDatas.size(); i++) {
                                    if (mDatas.get(i).goodsId == selectId) {
                                        mDatas.remove(i);
                                        selectId = -1;
                                        selectedPosition = -1;
                                        EventBus.getDefault().post(new GiftSelectedEvent(null));
                                        EventBus.getDefault().post(new ClearAllAnim());
                                    }
                                }
//                                giftAdapter.notifyDataSetChanged();
                            }
//                            if (mDatas.size() != backpackListBean.list.size()) {
//                                selectedPosition = -1;
//                                EventBus.getDefault().post(new GiftSelectedEvent(null));
//                            }
//                            mDatas.clear();
//                            mDatas.addAll(backpackListBean.list);
//                            giftAdapter.notifyDataSetChanged();
                        } else {
                            ((BackpackMotherFragment) getParentFragment()).llBackPack.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ApiResult<BackpackListBean> body) {
                    }
                });
    }

    private int check(List<GoodsDetailBean> list, int selectId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).goodsId == selectId) {
                return list.get(i).count;
            }
        }
        return -1;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ClearAllAnim event) {
        if (animatorSetsuofang != null) {
            animatorSetsuofang.end();
            animatorSetsuofang = null;
        }
    }

}
