package com.whzl.mengbi.ui.activity.me;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class BuyVipActivity extends BaseActivity {
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.btn_buy_vip)
    Button btnBuyVip;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_buy_vip, "购买VIP", true);
    }

    @Override
    protected void setupView() {
        tvNick.setText(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, "0").toString());
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.tv_nick, R.id.btn_buy_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_nick:
                break;
            case R.id.btn_buy_vip:
                break;
        }
    }
}
