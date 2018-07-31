package com.whzl.mengbi.ui.dialog.fragment;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.GiftSelectedEvent;
import com.whzl.mengbi.model.entity.BackpackListBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.ui.adapter.base.BaseListAdapter;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
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
    private ArrayList<BackpackListBean.GoodsDetailBean> mDatas = new ArrayList<>();
    private int selectedPosition = -1;


    public static BackpackFragment newInstance() {
        BackpackFragment fragment = new BackpackFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_gift_sort;
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);
        recycler.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
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
        getBackPack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GiftSelectedEvent event) {
        if(!flagOnMessageEvent){
            flagOnMessageEvent = true;
            return;
        }
        flagOnMessageEvent = false;
        selectedPosition = -1;
        giftAdapter.notifyDataSetChanged();
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

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            BackpackListBean.GoodsDetailBean goodsDetailBean = mDatas.get(position);
            GlideImageLoader.getInstace().displayImage(getContext(), goodsDetailBean.goodsPic, ivGift);
            tvGiftName.setText(goodsDetailBean.goodsName);
            tvCost.setText("数量 ");
            SpannableString spannableString = StringUtils.spannableStringColor(goodsDetailBean.count + "", Color.parseColor("#fdc809"));
            tvCost.append(spannableString);
            selectedMark.setSelected(position == selectedPosition);
        }

        @Override
        public void onItemClick(View view, int position) {
            super.onItemLongClick(view, position);
            BackpackListBean.GoodsDetailBean goodsDetailBean = mDatas.get(position);
            selectedPosition = position;
            giftAdapter.notifyDataSetChanged();
            GiftInfo.GiftDetailInfoBean giftDetailInfoBean = new GiftInfo.GiftDetailInfoBean();
            giftDetailInfoBean.setGoodsId(goodsDetailBean.goodsId);
            giftDetailInfoBean.setBackpack(true);
            flagOnMessageEvent = false;
            EventBus.getDefault().post(new GiftSelectedEvent(giftDetailInfoBean));
        }
    }

    private void getBackPack(){
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
                        if(backpackListBean != null){
                            mDatas.clear();
                            mDatas.addAll(backpackListBean.list);
                            giftAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
