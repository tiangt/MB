package com.whzl.mengbi.ui.activity.me;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.GetVipPriceBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class BuyVipActivity extends BaseActivity {
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.btn_buy_vip)
    Button btnBuyVip;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    private GetVipPriceBean priceBean;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_buy_vip, "购买VIP", true);
    }

    @Override
    protected void setupView() {
        tvNick.setText(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, "").toString());
    }

    @Override
    protected void loadData() {
        HashMap paramsMap = new HashMap();
        ApiFactory.getInstance().getApi(Api.class)
                .getVipprices(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetVipPriceBean>(BuyVipActivity.this) {

                    @Override
                    public void onSuccess(GetVipPriceBean bean) {
                        priceBean = bean;
                        tvPrice.setText(String.valueOf(bean.prices.month.rent));
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }


    @OnClick({R.id.tv_nick, R.id.btn_buy_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_nick:
                break;
            case R.id.btn_buy_vip:
                buy();
                break;
        }
    }

    private void buy() {
        BusinessUtils.mallBuy(this, String.valueOf(SPUtils.get(this, SpConfig.KEY_USER_ID, 0L)), String.valueOf(priceBean.goodsId)
                , String.valueOf(priceBean.prices.month.priceId), "1", "", "", "", new BusinessUtils.MallBuyListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showToast("购买成功");
                    }

                    @Override
                    public void onError() {
                    }
                });
    }
}
