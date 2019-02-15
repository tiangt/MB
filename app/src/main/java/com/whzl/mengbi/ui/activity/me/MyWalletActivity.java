package com.whzl.mengbi.ui.activity.me;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * @author cliang
 * @date 2019.2.15
 */
public class MyWalletActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_my_wallet);
    }

    @Override
    protected void setupView() {
        tvTitle.setText("我的钱包");
        rlBack.setOnClickListener((v -> finish()));
    }

    @Override
    protected void loadData() {

    }
}
