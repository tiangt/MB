package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

/**
 * @author nobody
 * @date 2019/1/9
 */
public class PlayNotifyActivity extends BaseActivity {
    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, Color.parseColor("#f9f9f9"), 0);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_play_notify, "开播提醒", true);
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void loadData() {

    }
}
