package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.BuildConfig;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.ActivityFinishEvent;
import com.whzl.mengbi.eventbus.event.LoginSuccussEvent;
import com.whzl.mengbi.gen.CommonGiftDao;
import com.whzl.mengbi.greendao.CommonGift;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.LoginPresent;
import com.whzl.mengbi.presenter.impl.LoginPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.LoginView;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.OnMultiClickListener;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.URLContentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/17
 */
public class LoginActivity extends BaseActivity implements LoginView, TextWatcher {

    private static final int REQUEST_REGISTER = 520;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.rg_env_switch)
    RadioGroup rgEnvSwitch;
    @BindView(R.id.tv_forget_psw)
    TextView tvForgetPsw;
    @BindView(R.id.ib_clean_phone)
    ImageButton ibCleanPhone;
    @BindView(R.id.ib_clean_psw)
    ImageButton ibCleanPsw;

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
                hashMap.put("type", NetConfig.OPEN_LOGIN_QQ);
                hashMap.put("token", access_token);
                hashMap.put("openid", openid);
            } else if (SHARE_MEDIA.WEIXIN.equals(platform)) {
                String openid = data.get("openid");
                String access_token = data.get("access_token");
                LogUtils.d("————————————————————————————" + access_token);
                hashMap.put("type", NetConfig.OPEN_LOGIN_WEIXIN);
                hashMap.put("token", access_token);
                hashMap.put("openid", openid);
            }
            hashMap.put("channelId", BaseApplication.getInstance().getChannel());
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
            dismissLoading();
            showToast(t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissLoading();
            showToast("用户取消");
            LogUtils.d("onError  platform" + platform);
        }
    };
    private String activityFrom;

    @Override
    protected void initEnv() {
        super.initEnv();
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#252525"));
        activityFrom = getIntent().getStringExtra("from");
        umShareAPI = UMShareAPI.get(this);
        mLoginPresent = new LoginPresenterImpl(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_login_new, R.string.login, R.string.register, true);
    }

    @Override
    protected void setupView() {
        rgEnvSwitch.setVisibility(BuildConfig.API_DEBUG_ENT ? View.VISIBLE : View.GONE);
        rgEnvSwitch.check(URLContentUtils.isDebug ? R.id.rb_debug : R.id.rb_release);
        rgEnvSwitch.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_debug) {
                URLContentUtils.isDebug = true;
            } else {
                URLContentUtils.isDebug = false;
            }
            BaseApplication.getInstance().initApi();
        });
        etPhone.addTextChangedListener(this);

        etPassword.addTextChangedListener(this);

        tvForgetPsw.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
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
        if (isPhone && !TextUtils.isEmpty(password) && password.length() >= 6) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }

        String phone = etPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            ibCleanPhone.setVisibility(View.VISIBLE);
            ibCleanPhone.setOnClickListener((v) -> etPhone.setText(""));
        } else {
            ibCleanPhone.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(password)) {
            ibCleanPsw.setVisibility(View.VISIBLE);
            ibCleanPsw.setOnClickListener((v) -> etPassword.setText(""));
        } else {
            ibCleanPsw.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_REGISTER);
        finish();
    }

    @OnClick({R.id.btn_wechat_login, R.id.btn_qq_login, R.id.btn_login})
    public void onClick(View view) {
        KeyBoardUtil.hideInputMethod(this);
        showLoading("登录中...");
        switch (view.getId()) {
            case R.id.btn_wechat_login:
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.btn_qq_login:
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.btn_login:
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString();
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("username", phone);
                paramsMap.put("password", EncryptUtils.md5Hex(password));
                paramsMap.put("platform", "ANDROID");
                paramsMap.put("channelId", BaseApplication.getInstance().getChannel());
                mLoginPresent.login(paramsMap);
                break;
        }
    }

    @Override
    public void loginSuccess(UserInfo userInfo) {
        dismissLoading();
        showToast(R.string.login_success);
        saveGreenDao(userInfo);
        LogUtils.e("sssssssssssss    " + userInfo.getData().getSessionId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, userInfo.getData().getUserId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, userInfo.getData().getSessionId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, userInfo.getData().getNickname());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));

        if (LiveDisplayActivity.class.toString().equals(activityFrom)) {
            setResult(RESULT_OK);
        } else if (userInfo.getData().getCreateTime() != null
                && userInfo.getData().getCreateTime().equals(userInfo.getData().getLastLoginTime())) {
            setResult(RESULT_OK);
        } else if (JsBridgeActivity.class.toString().equals(activityFrom)) {
            setResult(RESULT_OK);
        } else if ("logindialog".equals(activityFrom) || "account".equals(activityFrom)) {
            setResult(RESULT_OK);
        }
        EventBus.getDefault().post(new LoginSuccussEvent());
        finish();
    }

    private void saveGreenDao(UserInfo userInfo) {
        CommonGiftDao commonGiftDao = BaseApplication.getInstance().getDaoSession().getCommonGiftDao();
        CommonGift unique = commonGiftDao.queryBuilder().where(CommonGiftDao.Properties.UserId.eq(userInfo.getData().getUserId())).unique();
        if (unique == null) {
            CommonGift commonGift = new CommonGift();
            commonGift.setUserId(userInfo.getData().getUserId());
            commonGift.setAvatar(userInfo.getData().getAvatar());
            commonGift.setNickname(userInfo.getData().getNickname());
            commonGift.setSeesionId(userInfo.getData().getSessionId());
            commonGift.setRecharged(userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
            commonGiftDao.insert(commonGift);
        } else {
            unique.setUserId(userInfo.getData().getUserId());
            unique.setAvatar(userInfo.getData().getAvatar());
            unique.setNickname(userInfo.getData().getNickname());
            unique.setSeesionId(userInfo.getData().getSessionId());
            unique.setRecharged(userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
            commonGiftDao.update(unique);
        }
    }

    @Override
    public void showError(String msg) {
        dismissLoading();
        showToast(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        umShareAPI.release();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityFinishEvent event) {

    }
}
