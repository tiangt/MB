package com.whzl.mengbi.ui.activity;

import android.graphics.Color;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

/**
 * @author cliang
 * @date 2018.11.30
 */
public class PersonalInfoActivity extends BaseActivity {

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_personal_info);
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void loadData() {

    }


}
