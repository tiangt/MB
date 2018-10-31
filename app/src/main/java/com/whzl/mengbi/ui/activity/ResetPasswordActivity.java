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
import com.whzl.mengbi.eventbus.event.ActivityFinishEvent;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

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
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    Button btnVerifyCode;

    private CountDownTimer cdt;
    private String phone, verifyCode, mPassword, mPasswordAgain;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
        EventBus.getDefault().register(this);
        phone = getIntent().getStringExtra("mobile");
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

    @OnClick({R.id.btn_get_verify_code, R.id.btn_confirm_reset})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_verify_code:
                if(ClickUtil.isFastClick()){
                    getVerifyCode(phone);
                }
                break;

            case R.id.btn_confirm_reset:
                String verifyCode = etVerifyCode.getText().toString().trim();
                if (TextUtils.isEmpty(verifyCode)) {
                    tvPswMsg.setVisibility(View.VISIBLE);
                    tvPswMsg.setText("验证码不能为空");
                    return;
                }
                if (StringUtils.isNumber(mPassword) && StringUtils.isNumber(mPasswordAgain)) {
                    tvPswMsg.setVisibility(View.VISIBLE);
                    tvPswMsg.setText("密码不能为纯数字，请重新输入");
                    return;
                }
                if (mPassword.length() < 6 && mPasswordAgain.length() < 6) {
                    tvPswMsg.setVisibility(View.VISIBLE);
                    tvPswMsg.setText("密码长度为6-16位，请重新输入");
                    return;
                }
                if (!mPassword.equals(mPasswordAgain)) {
                    tvPswMsg.setVisibility(View.VISIBLE);
                    tvPswMsg.setText("两次输入的密码不一致，请重新输入");
                    return;
                }
                if (ClickUtil.isFastClick()){
                    resetPsw(phone, mPassword, verifyCode);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityFinishEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cdt != null) {
            cdt.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取验证码时间定时器
     */
    public void startTimer() {
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
     * 获取手机验证码
     *
     * @param mobile
     */
    private void getVerifyCode(String mobile) {
        startTimer();
        HashMap paramsMapMobile = new HashMap();
        paramsMapMobile.put("mobile", mobile);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.FORGET_PASS_CODE, RequestManager.TYPE_POST_JSON, paramsMapMobile,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        if (code.equals("-1231")) {
                            showToast("该手机号未注册");
                        } else if (code.equals("-1240")) {
                            showToast("当日短信发送达到上限");
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }

    /**
     * 重置密码
     *
     * @param mobile     手机号
     * @param psw        密码
     * @param verifyCode 验证码
     */
    private void resetPsw(String mobile, String psw, String verifyCode) {
        String md5Psd = EncryptUtils.md5Hex(psw);
        HashMap params = new HashMap();
        params.put("mobile", mobile);
        params.put("password", md5Psd);
        params.put("code", verifyCode);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RESET_PSW, RequestManager.TYPE_POST_JSON, params,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        if (code.equals("200")) {
                            Intent intent = new Intent(ResetPasswordActivity.this, RetrievePasswordActivity.class);
                            startActivity(intent);
                        } else if (code.equals("-1205")) {
                            showToast("验证码错误或者过期");
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());

                    }
                });
    }

}
