package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.ActivityFinishEvent;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回密码
 *
 * @author cliang
 * @date 2018.10.19
 */
public class RetrievePasswordActivity extends BaseActivity {

    @BindView(R.id.btn_back_login)
    Button btnBackLogin;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this,Color.parseColor("#252525"));
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_retrieve_password, R.string.retrieve_psw, false);
    }

    @Override
    protected void setupView() {
        btnBackLogin.setEnabled(true);
    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.btn_back_login)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back_login:
                EventBus.getDefault().post(new ActivityFinishEvent());
                finish();
                break;
        }
    }
}
