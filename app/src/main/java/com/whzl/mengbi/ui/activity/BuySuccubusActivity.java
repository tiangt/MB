package com.whzl.mengbi.ui.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.chat.room.message.events.SendBroadEvent;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.DemonCarBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/11/30
 */
public class BuySuccubusActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    private long goodsId;
    private int priceId;
    private int mAnchorId;
    private int mProgramId;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColor(this, Color.parseColor("#000000"));
        mProgramId = getIntent().getIntExtra("mProgramId", 0);
        mAnchorId = getIntent().getIntExtra("mAnchorId", 0);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_buy_succubus, "妖姬卡介绍", true);
    }

    @Override
    protected void setupView() {
        toolbar.setBackgroundColor(Color.parseColor("#000000"));
    }

    @Override
    protected void loadData() {
        HashMap params = new HashMap();
        Map signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .getDemonCar(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<DemonCarBean>() {

                    @Override
                    public void onSuccess(DemonCarBean bean) {
                        goodsId = bean.goodsId;
                        priceId = bean.prices.month.priceId;
                        tvPrice.setText(bean.prices.month.rent + "");
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }


    @OnClick({R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_buy:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(R.string.alert);
                dialog.setMessage("是否购买妖姬卡？");
                dialog.setNegativeButton(R.string.cancel, null);
                dialog.setPositiveButton(R.string.confirm, (dialog1, which) -> {
                    buy();
                });
                dialog.show();

                break;
        }
    }

    private void buy() {
        Long userid = (Long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
        BusinessUtils.mallBuy(this, userid + "", goodsId + "", priceId + "", "1",
                "", "", new BusinessUtils.MallBuyListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.toastMessage(BuySuccubusActivity.this, "购买成功");
                        finish();
                        EventBus.getDefault().post(new SendBroadEvent());
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
