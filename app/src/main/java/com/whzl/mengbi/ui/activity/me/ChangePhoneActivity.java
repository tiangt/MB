package com.whzl.mengbi.ui.activity.me;

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
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.w3c.dom.Text;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更换手机
 *
 * @author cliang
 * @date 2019.1.10
 */
public class ChangePhoneActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.tv_old_phone)
    TextView tvOldPhone;
    @BindView(R.id.et_old_phone)
    EditText etOldPhone;
    @BindView(R.id.et_verify_code)
    EditText etVerifyCode;
    @BindView(R.id.btn_get_verify_code)
    Button btnVerifyCode;
    @BindView(R.id.btn_next)
    Button btnNext;

    private CountDownTimer cdt;

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
        String mobile = getIntent().getStringExtra("bindMobile");
        String maskNum = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        tvOldPhone.setText(maskNum);
        etOldPhone.addTextChangedListener(this);
        etVerifyCode.addTextChangedListener(this);
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_get_verify_code, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_verify_code:
                String phone = etOldPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showToast("原手机号不能为空");
                    return;
                }
                if (!StringUtils.isPhone(phone)) {
                    showToast("请输入正确的手机号");
                    return;
                }

                if (ClickUtil.isFastClick()) {
                    getVerifyCode(phone);
                }
                break;

            case R.id.btn_next:
                String mobile = etOldPhone.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    showToast("原手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    showToast("验证码不能为空");
                    return;
                }

                Intent intent = new Intent(ChangePhoneActivity.this, BindingPhoneActivity.class);
                startActivity(intent);
                finish();
//                next(mobile, verifyCode);
                break;

            default:
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
        String oldPhone = etOldPhone.getText().toString().trim();
        String verifyCode = etVerifyCode.getText().toString().trim();
        if (!TextUtils.isEmpty(oldPhone) && !TextUtils.isEmpty(verifyCode)) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }

    /**
     * 获取验证码
     *
     * @param mobile
     */
    private void getVerifyCode(String mobile) {
        startTimer();
        HashMap hashMap = new HashMap();
        hashMap.put("mobile", mobile);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_CODE, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        String msg = jsonObject.get("msg").toString();
                        if (!code.equals("200")) {
                            showToast(msg);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }

    private void next(String mobile, String verifyCode) {
        HashMap hashMap = new HashMap();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_CODE, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        String msg = jsonObject.get("msg").toString();
                        if (code.equals("200")) {
                            Intent intent = new Intent(ChangePhoneActivity.this, BindingPhoneActivity.class);
                            startActivity(intent);
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
}
