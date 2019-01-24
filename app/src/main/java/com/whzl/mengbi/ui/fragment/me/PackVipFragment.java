package com.whzl.mengbi.ui.fragment.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.PackvipBean;
import com.whzl.mengbi.ui.activity.me.BuyVipActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/10/16
 */
public class PackVipFragment extends BasePullListFragment<PackvipBean.ListBean,BasePresenter> {
    private int REQUESTCODE = 200;

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected boolean setShouldLoadMore() {
        return false;
    }

    @Override
    public void init() {
        super.init();
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_packvip, getPullView(), false);
        getAdapter().addHeaderView(view);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_vip_pack, getPullView(), false);
        TextView tvOpen = view2.findViewById(R.id.tv_open);
        tvOpen.setOnClickListener(v -> {
            Intent intent = new Intent(getMyActivity(), BuyVipActivity.class);
            startActivityForResult(intent, REQUESTCODE);
        });
        setEmptyView(view2);
        getPullView().setRefBackgroud(Color.parseColor("#ffffff"));
    }

    public static PackVipFragment newInstance() {
        Bundle args = new Bundle();
        PackVipFragment packVipFragment = new PackVipFragment();
        packVipFragment.setArguments(args);
        return packVipFragment;
    }

    @Override
    protected void loadData(int action, int mPage) {
        if (action == PullRecycler.ACTION_LOAD_DATA || action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            HashMap paramsMap = new HashMap();
            paramsMap.put("userId", SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L));
            ApiFactory.getInstance().getApi(Api.class)
                    .myVip(ParamsUtils.getSignPramsMap(paramsMap))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<PackvipBean>(this) {

                        @Override
                        public void onSuccess(PackvipBean bean) {
                            loadSuccess(bean.list);
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
        }
    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View itemView = getLayoutInflater().inflate(R.layout.item_vip_pack, parent, false);
        return new ViewHolder(itemView);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_add)
        TextView tvAdd;
        @BindView(R.id.tv_receive)
        TextView tvReceive;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            PackvipBean.ListBean bean = mDatas.get(position);
            tvName.setText(bean.goodsName);
            tvDay.setText("剩余" + bean.surplusDay + "天");
            tvAdd.setOnClickListener(v -> {
                Intent intent = new Intent(getMyActivity(), BuyVipActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            });

            if (bean.surplusDay == 0) {
                tvReceive.setText("领取");
                tvReceive.setBackgroundResource(R.drawable.bg_button_pack_gray);
            } else {
                tvReceive.setText(bean.awardReceived ? "已领取" : "领取");
                tvReceive.setBackgroundResource(bean.awardReceived ? R.drawable.bg_button_pack_gray : R.drawable.bg_button_pack_orange);
                tvReceive.setEnabled(bean.awardReceived ? false : true);
                tvReceive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap paramsMap = new HashMap();
                        paramsMap.put("userId", SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L));
                        paramsMap.put("goodsId", bean.goodsId);
                        ApiFactory.getInstance().getApi(Api.class)
                                .vipAward(ParamsUtils.getSignPramsMap(paramsMap))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ApiObserver<JsonElement>(PackVipFragment.this) {

                                    @Override
                                    public void onSuccess(JsonElement bean) {
                                        tvReceive.setText("已领取");
                                        tvReceive.setBackgroundResource(R.drawable.bg_button_pack_gray);
                                        tvReceive.setEnabled(false);
                                        ToastUtils.showCustomToast(getMyActivity(), "成功领取蓝色妖姬×66，广播卡×1");
                                    }

                                    @Override
                                    public void onError(int code) {

                                    }
                                });
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (resultCode == getMyActivity().RESULT_OK) {
                mPage = 1;
                loadData(PullRecycler.ACTION_PULL_TO_REFRESH, mPage);
            }
        }
    }
}
