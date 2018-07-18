package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.LoginPresent;
import com.whzl.mengbi.presenter.impl.LoginPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.LoginView;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/17
 */
public class LoginActivityNew extends BaseActivityNew implements LoginView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private LoginPresent mLoginPresent;
    private UMShareAPI umShareAPI;

    private UMAuthListener umAuthListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            LogUtils.d("onComplete>>>>>>>>>>>>>platform>>>>>>>>>>>>" + platform + "data>>>>>>>>>>>>" + data);
            HashMap hashMap = new HashMap();
            if (SHARE_MEDIA.QQ.equals(platform)) {
                String openid = data.get("openid");
                String access_token = data.get("access_token");
                hashMap.put("type", "QQ");
                hashMap.put("token", access_token);
                hashMap.put("openid", openid);
            } else if (SHARE_MEDIA.WEIXIN.equals(platform)) {
                String openid = data.get("openid");
                String access_token = data.get("access_token");
                LogUtils.d("————————————————————————————" + access_token);
                hashMap.put("type", "WEIXIN");
                hashMap.put("token", access_token);
                hashMap.put("openid", openid);
            }
            mLoginPresent.openLogin(hashMap);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast(t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            LogUtils.d("onError  platform" + platform);
        }
    };
    private String activityFrom;

    @Override
    protected void initEnv() {
        super.initEnv();
        activityFrom = getIntent().getStringExtra("from");
        umShareAPI = UMShareAPI.get(this);
        mLoginPresent = new LoginPresenterImpl(this);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_login_new, R.string.login, R.string.register, true);
    }

    @Override
    protected void setupView() {
        etPhone.addTextChangedListener(new TextWatcher() {
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
                if (isPhone && !TextUtils.isEmpty(password) && password.length() >= 6) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
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
                if (isPhone && !TextUtils.isEmpty(password) && password.length() > 6) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.btn_wechat_login, R.id.btn_qq_login, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wechat_login:
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.btn_qq_login:
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.btn_login:
                String phone = etPhone.getText().toString().trim();
                String passwrd = etPassword.getText().toString();
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("identity", phone);
                paramsMap.put("password", passwrd);
                mLoginPresent.login(paramsMap);
                break;
        }
    }

    @Override
    public void loginSuccess(UserInfo userInfo) {
        showToast(R.string.login_success);
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, userInfo.getData().getUserId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, userInfo.getData().getSessionId());
        if (LiveDisplayActivityNew.class.toString().equals(activityFrom)) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        umShareAPI.release();
    }
}
