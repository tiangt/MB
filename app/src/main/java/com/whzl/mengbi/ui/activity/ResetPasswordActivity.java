package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.eventbus.event.ActivityFinishEvent;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author cliang
 * @date 2018.10.19
 */
public class ResetPasswordActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_again)
    EditText etPassWordAgain;
    @BindView(R.id.tv_psw_msg)
    TextView tvPswMsg;
    @BindView(R.id.btn_confirm_reset)
    Button btnConfirmReset;
    private String mPassword, mPasswordAgain;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_reset_password, R.string.reset_psw, true);
    }

    @Override
    protected void setupView() {
        etPassword.addTextChangedListener(this);
        etPassWordAgain.addTextChangedListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvPswMsg.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {
        mPassword = etPassword.getText().toString().trim();
        mPasswordAgain = etPassWordAgain.getText().toString().trim();
        if (!TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mPasswordAgain)) {
            btnConfirmReset.setEnabled(true);
        } else {
            btnConfirmReset.setEnabled(false);
        }
    }

    @OnClick({R.id.btn_confirm_reset})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_reset:
                if (StringUtils.isNumber(mPassword) && StringUtils.isNumber(mPasswordAgain)) {
                    tvPswMsg.setVisibility(View.VISIBLE);
                    tvPswMsg.setText("密码不能为纯数字，请重新输入");
                    return;
                }
                if (mPassword.length() < 6 && mPasswordAgain.length() < 6){
                    tvPswMsg.setVisibility(View.VISIBLE);
                    tvPswMsg.setText("密码长度为6-16位，请重新输入");
                    return;
                }
                if (!mPassword.equals(mPasswordAgain)) {
                    tvPswMsg.setVisibility(View.VISIBLE);
                    tvPswMsg.setText("两次输入的密码不一致，请重新输入");
                    return;
                }
                Intent intent = new Intent(ResetPasswordActivity.this, RetrievePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityFinishEvent event){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
