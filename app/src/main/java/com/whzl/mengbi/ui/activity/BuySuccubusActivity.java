package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/11/30
 */
public class BuySuccubusActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColor(this, Color.parseColor("#000000"));
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

    }

}
