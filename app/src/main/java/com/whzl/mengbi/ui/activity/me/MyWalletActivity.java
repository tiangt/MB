package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.StringUtils;

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
    @BindView(R.id.tv_mengbi)
    TextView tvMengbi;
    @BindView(R.id.tv_mengdou)
    TextView tvMengdou;
    @BindView(R.id.tv_mengdian)
    TextView tvMengdian;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_my_wallet);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupView() {
        tvTitle.setText("我的钱包");
        rlBack.setOnClickListener((v -> finish()));

        long mengbi = getIntent().getLongExtra("mengbi",0);
        long mengdou = getIntent().getLongExtra("mengdou",0);
        long mengdian = getIntent().getLongExtra("mengdian",0);

        tvMengbi.setText(StringUtils.formatNumber(mengbi));
        tvMengdou.setText(StringUtils.formatNumber(mengdou));
        tvMengdian.setText(StringUtils.formatNumber(mengdian));
    }

    @Override
    protected void loadData() {

    }
}
