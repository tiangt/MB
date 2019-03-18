package com.whzl.mengbi.ui.fragment.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.GetPrettyBean;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.PullRecycler;
import com.whzl.mengbi.util.BusinessUtils;
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
public class PackPrettyFragment extends BasePullListFragment<PackPrettyBean.ListBean, BasePresenter> {
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
    protected void initEnv() {
        super.initEnv();
        ((FrgActivity) getMyActivity()).setTitle("我的靓号");
        ((FrgActivity) getMyActivity()).setTitleMenuIcon(R.drawable.ic_jump_shop_mine, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getMyActivity(), ShopActivity.class).putExtra(ShopActivity.SELECT, 2));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(PullRecycler.ACTION_PULL_TO_REFRESH, 1);
    }

    @Override
    public void init() {
        super.init();
        View view = LayoutInflater.from(getMyActivity()).inflate(R.layout.head_pretty_pack, getPullView(), false);
        getAdapter().addHeaderView(view);
        View view2 = LayoutInflater.from(getMyActivity()).inflate(R.layout.empty_pretty_pack, getPullView(), false);
        setEmptyView(view2);
        getPullView().setRefBackgroud(Color.parseColor("#ffffff"));
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
        @BindView(R.id.tv_day)
        TextView tvDay;
        @BindView(R.id.tv_control)
        Switch tvControl;
        @BindView(R.id.tv_add)
        TextView tvAdd;
        @BindView(R.id.iv_num_item_pretty_pack)
        ImageView ivnum;
        @BindView(R.id.tv_num_item_pretty_pack)
        TextView tvnum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            PackPrettyBean.ListBean bean = mDatas.get(position);
            initTv(tvnum, ivnum, bean);
            tvDay.setText("剩余");
            tvDay.append(LightSpanString.getLightString(String.valueOf(bean.surplusDay), Color.parseColor("#ff2d4e")));
            tvDay.append("天");
            tvControl.setChecked("T".equals(bean.isEquip));
//            tvControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        control("T", bean, tvControl);
//                    } else {
//                        control("F", bean, tvControl);
//                    }
//                }
//            });
            tvControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tvControl.isChecked()) {
                        control("F", bean, tvControl);
                    } else {
                        control("T", bean, tvControl);
                    }
                }
            });
//            tvControl.setText("T".equals(bean.isEquip) ? "暂停" : "使用");
//            tvControl.setBackgroundResource("T".equals(bean.isEquip) ? R.drawable.bg_button_pack_orange : R.drawable.bg_button_pack_blue);
//            tvControl.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog = AwesomeDialog.init();
//                    dialog.setLayoutId(R.layout.dialog_car_pack)
//                            .setConvertListener(new ViewConvertListener() {
//                                @Override
//                                protected void convertView(com.whzl.mengbi.ui.dialog.base.ViewHolder holder, BaseAwesomeDialog dialog) {
//                                    if ("T".equals(bean.isEquip)) {
//                                        //暂停
//                                        holder.setText(R.id.tv_content, "确定暂停该靓号吗？");
//                                    } else {
//                                        //使用
//                                        holder.setText(R.id.tv_content, "确定使用该靓号吗？");
//                                    }
//                                    holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if ("T".equals(bean.isEquip)) {
//                                                //暂停
//                                                control("F", bean, tvControl);
//                                            } else {
//                                                //使用
//                                                control("T", bean, tvControl);
//                                            }
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                }
//                            }).show(getFragmentManager());
//                }
//            });
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPretty(bean.goodsId);
                }
            });
        }
    }

    private void initTv(TextView tvNum, ImageView ivNum, PackPrettyBean.ListBean digitsBean) {
        int i = digitsBean.goodsName.length();
        switch (i) {
            case 4:
                tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.9f);
                break;
            case 5:
                tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.9f);
                break;
            case 6:
                tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.1f);
                break;
            case 7:
                tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.1f);
                break;
            default:
                tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.1f);
                break;
        }
        tvNum.setText(digitsBean.goodsName);
        if ("A".equals(digitsBean.goodsColor)) {
            tvNum.setTextColor(Color.parseColor("#f42434"));
            tvNum.setBackgroundResource(R.drawable.bg_pretty_num_a);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_a, ivNum);
        } else if ("B".equals(digitsBean.goodsColor)) {
            tvNum.setTextColor(Color.parseColor("#ff9601"));
            tvNum.setBackgroundResource(R.drawable.bg_pretty_num_b);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_b, ivNum);
        } else if ("C".equals(digitsBean.goodsColor)) {
            tvNum.setTextColor(Color.parseColor("#9887f9"));
            tvNum.setBackgroundResource(R.drawable.bg_pretty_num_c);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_c, ivNum);
        } else if ("D".equals(digitsBean.goodsColor)) {
            tvNum.setTextColor(Color.parseColor("#5ecac2"));
            tvNum.setBackgroundResource(R.drawable.bg_pretty_num_d);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_d, ivNum);
        } else if ("E".equals(digitsBean.goodsColor)) {
            tvNum.setTextColor(Color.parseColor("#5dbaf6"));
            tvNum.setBackgroundResource(R.drawable.bg_pretty_num_e);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_e, ivNum);
        } else {
            tvNum.setTextColor(Color.parseColor("#5dbaf6"));
            tvNum.setBackgroundResource(R.drawable.bg_pretty_num_e);
            GlideImageLoader.getInstace().displayImage(getMyActivity(), R.drawable.ic_pretty_e, ivNum);
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
                                                        , String.valueOf(bean.prices.month.priceId), "1", "", "",
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
                                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
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

    private void control(String t, PackPrettyBean.ListBean listBean, Switch tvControl) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("goodsSn", listBean.goodsSn);
        paramsMap.put("equip", t);
        ApiFactory.getInstance().getApi(Api.class)
                .equip(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(this) {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        if ("T".equals(t)) {
                            listBean.isEquip = "T";
                            ToastUtils.showCustomToast(getMyActivity(), "靓号启用成功！");
//                            tvControl.setText("暂停");
//                            tvControl.setBackgroundResource(R.drawable.bg_button_pack_orange);
                            tvControl.setChecked(true);
                        } else {
                            listBean.isEquip = "F";
                            ToastUtils.showCustomToast(getMyActivity(), "靓号暂停成功");
//                            tvControl.setText("使用");
//                            tvControl.setBackgroundResource(R.drawable.bg_button_pack_blue);
                            tvControl.setChecked(false);
                        }
                    }

                    @Override
                    public void onError(int code) {
                        if (code == 503) {
//                            ToastUtils.showCustomToast(getMyActivity(), "启用失败，请先暂停正在使用的靓号");
                            tvControl.setChecked(false);
                        }
                    }
                });
    }

}
