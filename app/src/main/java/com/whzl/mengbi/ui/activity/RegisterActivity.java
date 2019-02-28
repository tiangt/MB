package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.RegisterInfo;
import com.whzl.mengbi.presenter.impl.RegisterPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.RegisterView;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/23
 */
public class RegisterActivity extends BaseActivity implements RegisterView, TextWatcher {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_menu_text)
    TextView tvMenu;
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
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.ib_clean_phone)
    ImageButton ibCleanPhone;
    @BindView(R.id.ib_clean_psw)
    ImageButton ibCleanPsw;
    private CountDownTimer cdt;
    private RegisterPresenterImpl registerPresenter;
    private boolean isBindPhone = true;

    @Override
    protected void initEnv() {
        super.initEnv();
        registerPresenter = new RegisterPresenterImpl(this);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void setupView() {
        tvTitle.setText("注册");
        tvMenu.setText("登录");
        rlBack.setOnClickListener((v -> finish()));
        tvMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

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

//    @Override
//    protected void onToolbarMenuClick() {
//        super.onToolbarMenuClick();
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }

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
        String phone = etPhone.getText().toString().trim();
        if (!isPhone && !TextUtils.isEmpty(phone) && phone.length() == 11) {
            showToast("请输入正确的手机号");
            return;
        }

        if (phone.length() == 11) {
            getVerifyCode(phone);
        }

        if (isPhone && !TextUtils.isEmpty(password) && password.length() >= 6
                && !TextUtils.isEmpty(verifyCode) && verifyCode.length() == 6) {
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setEnabled(false);
        }

        if (!TextUtils.isEmpty(phone)) {
            ibCleanPhone.setVisibility(View.VISIBLE);
        } else {
            ibCleanPhone.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(password)) {
            ibCleanPsw.setVisibility(View.VISIBLE);
        } else {
            ibCleanPsw.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_get_verify_code, R.id.btn_register, R.id.tv_agreement, R.id.ib_clean_phone, R.id.ib_clean_psw})
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

                if (!isBindPhone) {
                    registerPresenter.getRegexCode(phone);
                }
                break;

            case R.id.btn_register:
                String phoneStr = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String md5Psd = EncryptUtils.md5Hex(password);
                String verifyCode = etVerifyCode.getText().toString().trim();
                if (cbAgree.isChecked()) {
                    registerPresenter.getRegister(phoneStr, md5Psd, verifyCode);
                } else {
                    showToast("请先阅读用户服务协议");
                }
                break;

            case R.id.tv_agreement:
                Intent intent = new Intent(this, JsBridgeActivity.class);
                intent.putExtra("url", NetConfig.USER_DEAL);
                intent.putExtra("title", "用户协议");
                startActivity(intent);
                break;

            case R.id.ib_clean_phone:
                etPhone.setText("");
                break;

            case R.id.ib_clean_psw:
                etPassword.setText("");
                break;

            default:
                break;
        }
    }

    /**
     * 验证手机号是否存在
     * <p>
     * 验证手机
     */
    private void getVerifyCode(String mobile) {
        HashMap paramsMapMobile = new HashMap();
        paramsMapMobile.put("identifyCode", mobile);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.IS_PHONE, RequestManager.TYPE_POST_JSON, paramsMapMobile,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        if (code.equals("-1231")) {
                            isBindPhone = false;
                        } else if (code.equals("200")) {
                            isBindPhone = true;
                            showToast("该手机号码已存在");
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
