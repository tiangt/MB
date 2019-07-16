package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2019.2.15
 */
public class MyWalletActivity extends BaseActivity {

    @BindView(R.id.tv_mengbi)
    TextView tvMengbi;
    @BindView(R.id.tv_mengdou)
    TextView tvMengdou;
    @BindView(R.id.tv_mengdian)
    TextView tvMengdian;
    @BindView(R.id.rl_mengbi)
    RelativeLayout rlMengbi;
    private long mengbi;
    private long mengdou;
    private long mengdian;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_my_wallet, "我的钱包", true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
    }

    @Override
    protected void setupView() {
        tvMengbi = findViewById(R.id.tv_mengbi);
        tvMengdou = findViewById(R.id.tv_mengdou);
        tvMengdian = findViewById(R.id.tv_mengdian);
    }

    @Override
    protected void loadData() {
        HashMap paramsMap = new HashMap();
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        if (userId == 0) {
            return;
        }
        paramsMap.put("userId", userId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            if (userInfo == null || userInfo.getData() == null) {
                                return;
                            }
                            mengbi = userInfo.getData().getWealth().getCoin();
                            mengdou = userInfo.getData().getWealth().getMengDou();
                            mengdian = userInfo.getData().getWealth().getChengPonit();
                            tvMengbi.setText(StringUtils.formatNumber(mengbi));
                            tvMengdou.setText(StringUtils.formatNumber(mengdou));
                            tvMengdian.setText(StringUtils.formatNumber(mengdian));
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg);
                    }
                });
    }


    @OnClick({R.id.rl_mengbi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_mengbi:
                startActivity(new Intent(this, WXPayEntryActivity.class));
                break;

        }
    }
}
