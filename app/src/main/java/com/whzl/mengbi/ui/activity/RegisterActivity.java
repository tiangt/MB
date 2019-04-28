package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.ib_clean_phone)
    ImageButton ibCleanPhone;
    @BindView(R.id.ib_clean_psw)
    ImageView ibCleanPsw;
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
        setContentView(R.layout.activity_register, "注册", "登录", true);
    }

    @Override
    protected void setupView() {
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));

        etPhone.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etVerifyCode.addTextChangedListener(this);

        ibCleanPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getInputType() == 128) {//如果现在是显示密码模式
                    etPassword.setInputType(129);//设置为隐藏密码
                    ibCleanPsw.setSelected(true);
                } else {
                    etPassword.setInputType(128);//设置为显示密码
                    ibCleanPsw.setSelected(false);
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });
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

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 获取验证码时间定时器
     */
    public void startTimer() {
        btnGetVerifyCode.setEnabled(false);
        btnGetVerifyCode.setTextColor(Color.parseColor("#fefefe"));
        cdt = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetVerifyCode.setText("重新获取 " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                btnGetVerifyCode.setEnabled(true);
                btnGetVerifyCode.setText("获取验证码");
                btnGetVerifyCode.setTextColor(Color.parseColor("#38ebeb"));
            }
        };
        cdt.start();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = etPhone.getText().toString();
        if (text == null || text.length() == 0) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i != 3 && i != 8 && text.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(text.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(text.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            etPhone.setText(sb.toString());
            etPhone.setSelection(index);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String phone = etPhone.getText().toString().trim().replaceAll(" ", "");
        boolean isPhone = StringUtils.isPhone(phone);
        String password = etPassword.getText().toString().trim();
        String verifyCode = etVerifyCode.getText().toString().trim();
        if (phone.length() == 11 && !isPhone && !TextUtils.isEmpty(phone)) {
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
                String phone = etPhone.getText().toString().trim().replaceAll(" ","");
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
                String phoneStr = etPhone.getText().toString().trim().replaceAll(" ", "");
                String password = etPassword.getText().toString().trim();
                String md5Psd = EncryptUtils.md5Hex(password);
                String verifyCode = etVerifyCode.getText().toString().trim();
                registerPresenter.getRegister(phoneStr, md5Psd, verifyCode);
                break;

            case R.id.tv_agreement:
                startActivity(new Intent(this, JsBridgeActivity.class)
                        .putExtra("url", SPUtils.get(this, SpConfig.USERAGREEURL, "").toString())
                        .putExtra("title", "用户协议"));
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
