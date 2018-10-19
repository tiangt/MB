package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jaeger.library.StatusBarUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 *
 * @author cliang
 * @date 2018.10.19
 */
public class ForgetPasswordActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    Button btnGetVerifyCode;
    @BindView(R.id.tv_phone_msg)
    TextView tvPhoneMsg;
    @BindView(R.id.btn_next)
    Button btnNext;
    private CountDownTimer cdt;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_forget_psw, R.string.forget_psw, true);
    }

    @Override
    protected void setupView() {
        etPhone.addTextChangedListener(this);
        etVerifyCode.addTextChangedListener(this);
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_get_verify_code, R.id.btn_next})
    public void onClick(View view) {
        KeyBoardUtil.hideInputMethod(this);
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                String phone = etPhone.getText().toString().trim();
                if (!StringUtils.isPhone(phone)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                getVerifyCode(phone);
                break;
            case R.id.btn_next:
                Intent intent = new Intent(getBaseContext(), ResetPasswordActivity.class);
                startActivity(intent);
                finish();
                break;
        }
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
        if (isPhone && !TextUtils.isEmpty(verifyCode) && verifyCode.length() == 6) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }

    /**
     * 获取验证码倒计时
     */
    private void startTimer() {
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

    /**
     * 获取验证码
     */
    private void getVerifyCode(String mobile){
        startTimer();
        HashMap paramsMapMobile = new HashMap();
        paramsMapMobile.put("mobile",mobile);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_CODE, RequestManager.TYPE_POST_JSON, paramsMapMobile,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        if(!code.equals(200)){
                            String msg = jsonObject.get("msg").toString();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg"+errorMsg.toString());
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
