package com.whzl.mengbi.ui.fragment.me;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.GetPrettyBean;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.BusinessUtils;
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
public class PackPrettyFragment extends BasePullListFragment<PackPrettyBean.ListBean,BasePresenter> {
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
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_pretty_pack, getPullView(), false);
        getAdapter().addHeaderView(view);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_car_pack, getPullView(), false);
        setEmptyView(view2);
    }

    public static PackPrettyFragment newInstance() {
        Bundle args = new Bundle();
        PackPrettyFragment packPrettyFragment = new PackPrettyFragment();
        packPrettyFragment.setArguments(args);
        return packPrettyFragment;
    }

    @Override
    protected void loadData(int action, int mPage) {
        if (action == PullRecycler.ACTION_LOAD_DATA || action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            HashMap paramsMap = new HashMap();
            paramsMap.put("userId", SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L));
            paramsMap.put("page", mPage);
            paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
            ApiFactory.getInstance().getApi(Api.class)
                    .pretty(ParamsUtils.getSignPramsMap(paramsMap))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<PackPrettyBean>(this) {

                        @Override
                        public void onSuccess(PackPrettyBean bean) {
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
        View itemView = getLayoutInflater().inflate(R.layout.item_pretty_pack, parent, false);
        return new ViewHolder(itemView);
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.tv_control)
        TextView tvControl;
        @BindView(R.id.tv_add)
        TextView tvAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            PackPrettyBean.ListBean bean = mDatas.get(position);
            tvName.setText(bean.goodsName);
            tvDay.setText("剩余" + bean.surplusDay + "天");
            tvState.setText("T".equals(bean.isEquip) ? "使用中" : "闲置");
            tvState.setTextColor("T".equals(bean.isEquip) ? ContextCompat.getColor(getMyActivity(), R.color.text_color_use_pack) :
                    ContextCompat.getColor(getMyActivity(), R.color.text_color_wait_pack));
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
                                        holder.setText(R.id.tv_content, "确定暂停该靓号吗？");
                                    } else {
                                        //使用
                                        holder.setText(R.id.tv_content, "确定使用该靓号吗？");
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
                                    holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }).show(getFragmentManager());
                }
            });
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPretty(bean.goodsId);
                }
            });
        }
    }

    private void getPretty(int goodsId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("goodsId", goodsId);
        ApiFactory.getInstance().getApi(Api.class)
                .getprettyprices(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetPrettyBean>(this) {
                    @Override
                    public void onSuccess(GetPrettyBean bean) {
                        dialog = AwesomeDialog.init();
                        dialog.setLayoutId(R.layout.dialog_pretty_pack)
                                .setConvertListener(new ViewConvertListener() {
                                    @Override
                                    protected void convertView(com.whzl.mengbi.ui.dialog.base.ViewHolder holder, BaseAwesomeDialog dialog) {
                                        holder.setText(R.id.tv_num, bean.goodsName);
                                        holder.setText(R.id.tv_price, String.valueOf(bean.prices.month.rent));
                                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Long userid = (Long) SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L);
                                                BusinessUtils.mallBuy(getMyActivity(), String.valueOf(userid), String.valueOf(bean.goodsId)
                                                        , String.valueOf(bean.prices.month.priceId), "1", "", "", "",
                                                        new BusinessUtils.MallBuyListener() {
                                                            @Override
                                                            public void onSuccess() {
                                                                dialog.dismiss();
                                                                ToastUtils.showCustomToast(getMyActivity(), "续费成功！");
                                                                mPage = 1;
                                                                loadData(PullRecycler.ACTION_PULL_TO_REFRESH, mPage);
                                                            }

                                                            @Override
                                                            public void onError() {

                                                            }
                                                        });
                                            }
                                        });
                                    }
                                }).show(getFragmentManager());
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void control(String t, long goodsSn, TextView tvState, TextView tvControl) {
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
                            ToastUtils.showCustomToast(getMyActivity(), "靓号启用成功！");
                            tvState.setText("使用中");
                            tvState.setTextColor(Color.parseColor("#2cd996"));
                            tvControl.setText("暂停");
                            tvControl.setBackgroundResource(R.drawable.bg_button_pack_orange);
                        } else {
                            ToastUtils.showCustomToast(getMyActivity(), "靓号暂停成功");
                            tvState.setText("闲置");
                            tvState.setTextColor(Color.parseColor("#ff611b"));
                            tvControl.setText("使用");
                            tvControl.setBackgroundResource(R.drawable.bg_button_pack_blue);
                        }
                    }

                    @Override
                    public void onError(int code) {
                        if (code == 503) {
                            ToastUtils.showCustomToast(getMyActivity(), "启用失败，请先暂停正在使用的靓号");
                        }
                    }
                });
    }


}
