package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.model.entity.GetProsListBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment;
import com.whzl.mengbi.ui.widget.view.LongClickButton;
import com.whzl.mengbi.util.AmountConversionUitls;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author niko
 * @date 2018/9/18
 */
public class PropFragment extends BasePullListFragment<GetProsListBean.ListBean, BasePresenter> {

    private BaseAwesomeDialog awesomeDialog;
    private int num;

    @Override
    protected boolean setLoadMoreEndShow() {
        return false;
    }

    @Override
    protected void loadData(int action, int mPage) {
        HashMap paramsMap = new HashMap();
        ApiFactory.getInstance().getApi(Api.class)
                .getProsList(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetProsListBean>(this) {
                    @Override
                    public void onSuccess(GetProsListBean bean) {
                        loadSuccess(bean.list);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });

    }

    @Override
    protected BaseViewHolder setViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(getMyActivity()).inflate(R.layout.item_prop_shop, getPullView(), false);
        return new ViewHolder(inflate);
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_item_prop_shop)
        ImageView imageView;
        @BindView(R.id.tv_name_item_prop_shop)
        TextView tvName;
        @BindView(R.id.tv_price_item_prop_shop)
        TextView tvPrice;
        @BindView(R.id.tv_welfare_item_prop_shop)
        TextView tvWelfare;
        @BindView(R.id.tv_use_item_prop_shop)
        TextView tvUse;
        @BindView(R.id.btn_buy_item_prop_shop)
        Button btnBuy;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            GetProsListBean.ListBean listBean = mDatas.get(position);
            if (listBean == null) {
                return;
            }
            RoundedCorners roundedCorners = new RoundedCorners(UIUtil.dip2px(getContext(), 5));
            RequestOptions requestOptions = new RequestOptions().transform(roundedCorners);
            Glide.with(PropFragment.this).load(listBean.goodsPic).apply(requestOptions).into(imageView);
            tvName.setText(listBean.goodsName);
            tvPrice.setText(LightSpanString.getLightString
                    (AmountConversionUitls.amountConversionFormat(listBean.rent), Color.parseColor("#f4545a")));
            tvPrice.append(LightSpanString.getLightString(" 萌币/个", Color.parseColor("#70505050")));
            tvWelfare.setText(getString(R.string.welfare_item_prop_shop, listBean.givingMengDou));
            tvUse.setText(listBean.remark);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (awesomeDialog != null && awesomeDialog.isAdded()) {
                        return;
                    }
                    showDialog(listBean);
                }
            });
        }
    }

    private void showDialog(GetProsListBean.ListBean listBean) {
        num = 1;
        UserInfo.DataBean currentUser = ((ShopActivity) getMyActivity()).currentUser;
        awesomeDialog = AwesomeDialog.init().setLayoutId(R.layout.dialog_prop_shop).setConvertListener(new ViewConvertListener() {
            @Override
            protected void convertView(com.whzl.mengbi.ui.dialog.base.ViewHolder holder, BaseAwesomeDialog dialog) {
                holder.setText(R.id.tv_title_dialog_prop_shop, "购买" + listBean.goodsName);
                holder.setText(R.id.tv_time_dialog_prop_shop, listBean.days + "天");
                TextView textView = (TextView) holder.getView(R.id.tv_total_dialog_prop_shop);
                TextView tvNum = (TextView) holder.getView(R.id.tv_num_diaolog_prop_shop);
                calculate(textView, listBean, num);
                holder.setOnClickListener(R.id.tv_reduce_diaolog_prop_shop, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        num -= 1;
//                        if (num < 1) {
//                            num = 1;
//                        }
//                        holder.setText(R.id.tv_num_diaolog_prop_shop, String.valueOf(num));
                        if (Integer.parseInt(tvNum.getText().toString()) <= 1) {
                            return;
                        }
                        tvNum.setText(String.valueOf(Integer.parseInt(tvNum.getText().toString()) - 1));
                        calculate(textView, listBean, Integer.parseInt(tvNum.getText().toString()));
                    }
                });
                holder.setOnClickListener(R.id.tv_add_diaolog_prop_shop, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        num += 1;
//                        if (num > 9999) {
//                        }
//                        holder.setText(R.id.tv_num_diaolog_prop_shop, String.valueOf(num));
                        if (Integer.parseInt(tvNum.getText().toString()) >= 9999) {
                            return;
                        }
                        tvNum.setText(String.valueOf(Integer.parseInt(tvNum.getText().toString()) + 1));
                        calculate(textView, listBean, Integer.parseInt(tvNum.getText().toString()));
                    }
                });
                LongClickButton tvReduce = holder.getView(R.id.tv_reduce_diaolog_prop_shop);
                LongClickButton tvAdd = holder.getView(R.id.tv_add_diaolog_prop_shop);
                tvReduce.setLongClickRepeatListener(new LongClickButton.LongClickRepeatListener() {
                    @Override
                    public void repeatAction() {
                        if (Integer.parseInt(tvNum.getText().toString()) <= 1) {
                            return;
                        }
                        tvNum.setText(String.valueOf(Integer.parseInt(tvNum.getText().toString()) - 1));
                        calculate(textView, listBean, Integer.parseInt(tvNum.getText().toString()));
                    }
                });
                tvAdd.setLongClickRepeatListener(new LongClickButton.LongClickRepeatListener() {
                    @Override
                    public void repeatAction() {
                        if (Integer.parseInt(tvNum.getText().toString()) >= 9999) {
                            return;
                        }
                        tvNum.setText(String.valueOf(Integer.parseInt(tvNum.getText().toString()) + 1));
                        calculate(textView, listBean, Integer.parseInt(tvNum.getText().toString()));
                    }
                });
                holder.setOnClickListener(R.id.btn_buy, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buy(listBean, Integer.parseInt(tvNum.getText().toString()));
                    }
                });
            }
        }).setDimAmount(0).setShowBottom(true).show(getFragmentManager());
    }

    private void buy(GetProsListBean.ListBean listBean, int i) {
        BusinessUtils.mallBuyDay(getMyActivity(), String.valueOf(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L))
                , listBean.goodsId + "", listBean.priceId + "", i + "", "", "", String.valueOf(listBean.days)
                , new BusinessUtils.MallBuyListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showToastUnify(getMyActivity(), "购买成功");
                        if (awesomeDialog != null) {
                            awesomeDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void calculate(TextView view, GetProsListBean.ListBean listBean, int num) {
        long totalRent = listBean.rent * num;
        long totalMengdou = listBean.givingMengDou * num;
        view.setText("");
        view.append(LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(totalRent),
                Color.parseColor("#f4545a")));
        view.append(LightSpanString.getLightString(" 萌币 （赠送 ", Color.parseColor("#646464")));
        view.append(LightSpanString.getLightString(AmountConversionUitls.amountConversionFormat(totalMengdou),
                Color.parseColor("#f4545a")));
        view.append(LightSpanString.getLightString(" 萌豆 ）", Color.parseColor("#646464")));
    }
}
