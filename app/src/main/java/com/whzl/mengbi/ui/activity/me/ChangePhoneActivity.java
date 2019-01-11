package com.whzl.mengbi.ui.activity.me;

import android.graphics.Color;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

/**
 * 更换手机
 *
 * @author cliang
 * @date 2019.1.10
 */
public class ChangePhoneActivity extends BaseActivity {

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_change_phone, "更换手机", true);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void loadData() {

    }
}