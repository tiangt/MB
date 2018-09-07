package com.whzl.mengbi.ui.activity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.RegisterInfo;
import com.whzl.mengbi.presenter.impl.RegisterPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.RegisterView;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/23
 */
public class RegisterActivity extends BaseActivity implements RegisterView, TextWatcher {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    Button btnGetVerifyCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private CountDownTimer cdt;
    private RegisterPresenterImpl registerPresenter;


    @Override
    protected void initEnv() {
        super.initEnv();
        registerPresenter = new RegisterPresenterImpl(this);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_register, R.string.register, true);
    }

    @Override
    protected void setupView() {
        etPhone.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etVerifyCode.addTextChangedListener(this);

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void showRegexCodeMsg(String code, String msg) {
        startTimer();
    }

    @Override
    public void navigateToAll(RegisterInfo registerInfo) {
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, registerInfo.getData().getUserId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, registerInfo.getData().getSessionId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, registerInfo.getData().getNickName());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, registerInfo.getData().getLastRechargeTime() != null);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    /**
     * 获取验证码时间定时器
     */
    public void startTimer() {
        btnGetVerifyCode.setEnabled(false);
        cdt = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetVerifyCode.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                btnGetVerifyCode.setEnabled(true);
                btnGetVerifyCode.setText("获取验证码");
            }
        };
        cdt.start();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean isPhone = StringUtils.isPhone(etPhone.getText().toString().trim());
        String password = etPassword.getText().toString().trim();
        String verifyCode = etVerifyCode.getText().toString().trim();
        if (isPhone && !TextUtils.isEmpty(password) && password.length() >= 6
                && !TextUtils.isEmpty(verifyCode) && verifyCode.length() == 6) {
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setEnabled(false);
        }
    }

    @OnClick({R.id.btn_get_verify_code, R.id.btn_register})
    public void onClick(View view) {
        KeyBoardUtil.hideInputMethod(this);
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                String phone = etPhone.getText().toString().trim();
                if (!StringUtils.isPhone(phone)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                registerPresenter.getRegexCode(phone);
                break;
            case R.id.btn_register:
                String phoneStr = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String md5Psd = EncryptUtils.md5Hex(password);
                String verifyCode = etVerifyCode.getText().toString().trim();
                registerPresenter.getRegister(phoneStr, md5Psd, verifyCode);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cdt != null) {
            cdt.cancel();
        }
    }
}
