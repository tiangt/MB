package com.whzl.mengbi.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.presenter.LoginPresenter;
import com.whzl.mengbi.presenter.impl.LoginPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.ui.view.LoginView;
import com.whzl.mengbi.ui.widget.view.GenericToolbar;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 登录页面
 */
public class LoginActivity extends BaseAtivity implements LoginView, View.OnClickListener {
    /**
     * 用户名，手机号
     */
    private EditText longinUserName;
    private String mUserName;

    /**
     * 用户密码
     */
    private EditText longinUserPassword;
    private String mUserPassword;

    /**
     * 登录按钮
     */
    private Button login;
    /**
     * QQ登陆
     */
    private ImageView qqUserLoginIv;
    /**
     * 微信登陆
     */
    private ImageView weixinUserLoginIv;

    /**
     * 游客身份标识
     */
    private String visitorFlag;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        if (getIntent() != null) {
            visitorFlag = getIntent().getStringExtra("touristFlag");
        }
        loginPresenter = new LoginPresenterImpl(this);
        //手机设备ID
        String deviceid = RxPermisssionsUitls.getDevice(this);
        //游客登录
        loginPresenter.visitorValidateCredentials(deviceid);
        //自动登录
        autoLogin();
        initView();
    }

    private void initView() {
        longinUserName = findViewById(R.id.login_user_name);
        longinUserPassword = findViewById(R.id.login_user_password);
        login = findViewById(R.id.login_user_login);
        qqUserLoginIv = findViewById(R.id.login_qq_login);
        weixinUserLoginIv = findViewById(R.id.login_weixin_login);
        login.setOnClickListener(this);
        qqUserLoginIv.setOnClickListener(this);
        weixinUserLoginIv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        mUserName = longinUserName.getText().toString().trim();
        mUserPassword = longinUserPassword.getText().toString().trim();
        switch (v.getId()) {
            case R.id.login_user_login://登录
                HashMap paramsMap = new HashMap();
                paramsMap.put("username", mUserName);
                String pass = EncryptUtils.md5Hex(mUserPassword);
                paramsMap.put("password", pass);
                paramsMap.put("platform", "ANDROID");
                loginPresenter.validateCredentials(paramsMap, URLContentUtils.USER_NORMAL_LOGIN);
                SPUtils.put(BaseAppliaction.getInstance(), "username", mUserName);
                SPUtils.put(BaseAppliaction.getInstance(), "password", mUserPassword);
                break;
            case R.id.login_qq_login://QQ第三方登录
                UMShareConfig qqConfig = new UMShareConfig();
                qqConfig.isNeedAuthOnGetUserInfo(true);
                UMShareAPI.get(this).setShareConfig(qqConfig);
                UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.login_weixin_login://微信第三方登录
                UMShareConfig weixinConfig = new UMShareConfig();
                weixinConfig.isNeedAuthOnGetUserInfo(true);
                UMShareAPI.get(this).setShareConfig(weixinConfig);
                UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case 3://标题注册跳转
                Intent mTntent = new Intent(this, RegisterActivity.class);
                startActivityForResult(mTntent, 0);
                break;
            default:
                break;
        }
    }


    /**
     * 游客登录成功回调
     */
    @Override
    public void visitorNavigateToHome() {
        int userId = (Integer) SPUtils.get(this, "userId", 0);
        if (userId == 0) {
            if (visitorFlag != null && visitorFlag.equals("0")) {
                return;
            } else {
                Intent mIntent = new Intent(this, HomeActivity.class);
                startActivity(mIntent);
                finish();
            }
        }
    }

    /**
     * 用户登录成功回调
     */
    @Override
    public void navigateToHome() {
        Intent mIntent = new Intent(this, HomeActivity.class);
        startActivity(mIntent);
        finish();
    }

    /**
     * 用户自动登录
     */
    private void autoLogin() {
        boolean islogin = (boolean) SPUtils.get(this, "islogin", false);
        if (islogin) {
            HashMap paramsMap = new HashMap();
            String userName = SPUtils.get(this, "username", "").toString();
            String passWord = SPUtils.get(this, "password", "").toString();
            if (!TextUtils.isEmpty(userName) || !TextUtils.isEmpty(passWord)) {
                paramsMap.put("username", userName);
                String pass = EncryptUtils.md5Hex(passWord);
                paramsMap.put("password", pass);
                paramsMap.put("platform", "ANDROID");
                loginPresenter.validateCredentials(paramsMap, URLContentUtils.USER_NORMAL_LOGIN);
            }
        }
    }

    /**
     * 登录失败回调消息提示
     *
     * @param msg
     */
    @Override
    public void showError(String msg) {
        ToastUtils.showToast(msg);
    }


    /**
     * 友盟第三方登录回调监听
     */
    UMAuthListener umAuthListener = new UMAuthListener() {
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
            HashMap hashMap = new HashMap();
            if (SHARE_MEDIA.QQ.equals(platform)) {
                String openid = data.get("openid");
                String access_token = data.get("access_token");
                LogUtils.d("QQ>>>>>openid>>>>>>>>>>>>" + openid + "access_token>>>>>>>>>" + access_token);
                hashMap.put("type", SHARE_MEDIA.QQ);
                hashMap.put("token", access_token);
                hashMap.put("openid", openid);
            } else if (SHARE_MEDIA.WEIXIN.equals(platform)) {
                String openid = data.get("openid");
                String access_token = data.get("access_token");
                LogUtils.d("WEIXIN>>>>>>>openid>>>>>>>>>>>>" + openid + "access_token>>>>>>>>>" + access_token);
                hashMap.put("type", SHARE_MEDIA.WEIXIN);
                hashMap.put("token", access_token);
                hashMap.put("openid", openid);
            }
            LogUtils.d("onComplete>>>>>>>>>>>>>platform>>>>>>>>>>>>" + platform + "data>>>>>>>>>>>>" + data);
            loginPresenter.validateCredentials(hashMap, URLContentUtils.OPEN_LOGIN);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_user_register:
                Intent mTntent = new Intent(this, RegisterActivity.class);
                startActivityForResult(mTntent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String result = data.getStringExtra("userName");
            longinUserName.setText(result);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestroy();
    }
}
