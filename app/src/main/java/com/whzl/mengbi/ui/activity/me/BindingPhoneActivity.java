package com.whzl.mengbi.ui.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.presenter.impl.BindingPresenterImpl;
import com.whzl.mengbi.ui.activity.ResetPasswordActivity;
import com.whzl.mengbi.ui.activity.RetrievePasswordActivity;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.BindingPhoneView;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定手机
 *
 * @author cliang
 * @date 2019.1.9
 */
public class BindingPhoneActivity extends BaseActivity implements BindingPhoneView, TextWatcher {

    @BindView(R.id.et_binding_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    Button btnVerifyCode;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private BindingPresenterImpl bindingPresenter;
    private CountDownTimer cdt;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_binding_phone, "绑定手机", true);
        bindingPresenter = new BindingPresenterImpl(this);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupView() {
        etPhone.addTextChangedListener(this);
        etVerifyCode.addTextChangedListener(this);
        etPsw.addTextChangedListener(this);
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_get_verify_code, R.id.btn_confirm})
    public void onClick(View view) {
        KeyBoardUtil.hideInputMethod(this);
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showToast("手机号码不能为空");
                    return;
                }
                if (!StringUtils.isPhone(phone)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                if (ClickUtil.isFastClick()) {
                    bindingPresenter.getRegexCode(phone);
                }
                break;

            case R.id.btn_confirm:
                String mobile = etPhone.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                String password = etPsw.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    showToast("手机号码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    showToast("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("密码不能为空");
                    return;
                }
                if (StringUtils.isNumber(password)) {
                    showToast("密码不能为纯数字，请重新输入");
                    return;
                }

                bindPhone(mobile, verifyCode, password);
                break;

            default:
                break;
        }
    }

    @Override
    public void showRegexCodeMsg(String code, String msg) {
        if(code.equals("200")){
            startTimer();
        }else{
            showToast(msg);
        }
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
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
        String verifyCode = etVerifyCode.getText().toString().trim();
        String password = etPsw.getText().toString().trim();
        if (isPhone && !TextUtils.isEmpty(password) && password.length() >= 6
                && !TextUtils.isEmpty(verifyCode) && verifyCode.length() == 6) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }
    }

    /**
     * 验证码倒计时
     */
    private void startTimer() {
        btnVerifyCode.setEnabled(false);
        cdt = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnVerifyCode.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                btnVerifyCode.setEnabled(true);
                btnVerifyCode.setText("获取验证码");
            }
        };
        cdt.start();
    }

    /**
     * 绑定手机
     */
    private void bindPhone(String mobile, String verifyCode, String password) {
        String md5Psd = EncryptUtils.md5Hex(password);
        HashMap params = new HashMap();
        params.put("identifyCode", mobile);
        params.put("bindType", "MOBILE");
        params.put("code", verifyCode);
        params.put("passwd", md5Psd);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.BIND_PHONE, RequestManager.TYPE_POST_JSON, params,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        String msg = jsonObject.get("msg").toString();
                        if (code.equals("200")) {
                            showToast("绑定成功");
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showToast(msg);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cdt != null) {
            cdt.cancel();
        }
    }
}
