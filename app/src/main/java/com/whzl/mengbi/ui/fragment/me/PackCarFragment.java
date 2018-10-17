package com.whzl.mengbi.ui.fragment.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.PackcarBean;
import com.whzl.mengbi.ui.activity.me.BuyVipActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
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
public class PackCarFragment extends BasePullListFragment<PackcarBean.ListBean> {
    private int REQUESTCODE = 200;
    private AwesomeDialog dialog;

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected boolean setShouldLoadMore() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_car_pack, getPullView(), false);
        getAdapter().addHeaderView(view);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_vip_pack, getPullView(), false);
        TextView tvOpen = view2.findViewById(R.id.tv_open);
        tvOpen.setOnClickListener(v -> {
            Intent intent = new Intent(getMyActivity(), BuyVipActivity.class);
            startActivityForResult(intent, REQUESTCODE);
        });
        setEmptyView(view2);
    }

    public static PackCarFragment newInstance() {
        Bundle args = new Bundle();
        PackCarFragment packCarFragment = new PackCarFragment();
        packCarFragment.setArguments(args);
        return packCarFragment;
    }

    @Override
    protected void loadData(int action, int mPage) {
        if (action == PullRecycler.ACTION_LOAD_DATA || action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            HashMap paramsMap = new HashMap();
            paramsMap.put("userId", SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L));
            paramsMap.put("page", mPage);
            paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
            ApiFactory.getInstance().getApi(Api.class)
                    .myCard(ParamsUtils.getSignPramsMap(paramsMap))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<PackcarBean>(this) {

                        @Override
                        public void onSuccess(PackcarBean bean) {
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
        View itemView = getLayoutInflater().inflate(R.layout.item_car_pack, parent, false);
        return new ViewHolder(itemView);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_control)
        TextView tvControl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            PackcarBean.ListBean bean = mDatas.get(position);
            tvName.setText(bean.goodsName);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), bean.goodsPic, iv);
            tvDay.setText("剩余" + bean.surplusDay + "天");
            tvState.setText("T".equals(bean.isEquip) ? "使用中" : "闲置");
            tvState.setTextColor("T".equals(bean.isEquip) ? Color.parseColor("#2cd996") : Color.parseColor("#ff611b"));
            tvControl.setText("T".equals(bean.isEquip) ? "暂停" : "使用");
            tvControl.setBackgroundResource("T".equals(bean.isEquip) ? R.drawable.bg_button_pack_orange : R.drawable.bg_button_pack_blue);
            tvControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = AwesomeDialog.init();
                    dialog.setLayoutId(R.layout.dialog_car_pack)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                protected void convertView(com.whzl.mengbi.ui.dialog.base.ViewHolder holder, BaseAwesomeDialog dialog) {
                                    if ("T".equals(bean.isEquip)) {
                                        //暂停
                                        holder.setText(R.id.tv_content, "确定暂停该座驾吗？");
                                    } else {
                                        //使用
                                        holder.setText(R.id.tv_content, "确定使用该座驾吗？");
                                    }
                                    holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if ("T".equals(bean.isEquip)) {
                                                //暂停
                                                control("F", bean.goodsSn, tvState, tvControl);
                                            } else {
                                                //使用
                                                control("T", bean.goodsSn, tvState, tvControl);
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }).show(getFragmentManager())
                    ;
                }
            });
        }
    }

    private void control(String t, int goodsSn, TextView tvState, TextView tvControl) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("goodsSn", goodsSn);
        paramsMap.put("equip", t);
        ApiFactory.getInstance().getApi(Api.class)
                .equip(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        if ("T".equals(t)) {
                            ToastUtils.showCustomToast(getMyActivity(), "座驾启用成功！");
                            tvState.setText("使用中");
                            tvState.setTextColor(Color.parseColor("#2cd996"));
                            tvControl.setText("暂停");
                            tvControl.setBackgroundResource(R.drawable.bg_button_pack_orange);
                        } else {
                            ToastUtils.showCustomToast(getMyActivity(), "座驾暂停成功！");
                            tvState.setText("闲置");
                            tvState.setTextColor(Color.parseColor("#ff611b"));
                            tvControl.setText("使用");
                            tvControl.setBackgroundResource(R.drawable.bg_button_pack_blue);
                        }
                    }

                    @Override
                    public void onError(int code) {
                        if (code == 503) {
                            ToastUtils.showCustomToast(getMyActivity(), "启用失败，请先暂停正在使用的座驾");
                        }
                    }
                });
    }


}
