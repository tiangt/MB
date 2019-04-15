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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.BuildConfig;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.ActivityFinishEvent;
import com.whzl.mengbi.eventbus.event.LoginSuccussEvent;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.greendao.User;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.LoginPresent;
import com.whzl.mengbi.presenter.impl.LoginPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.LoginView;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.OnMultiClickListener;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.network.RequestManager;
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
    ImageView ibCleanPsw;
    @BindView(R.id.tv_xieyi_login)
    TextView tvXieyi;

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
        activityFrom = getIntent().getStringExtra("from");
        umShareAPI = UMShareAPI.get(this);
        mLoginPresent = new LoginPresenterImpl(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_login_new, "登录", "注册", true);
    }

    @Override
    protected void setupView() {
        getTitleRightText().setTextColor(Color.parseColor("#ff2b3f"));

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

        if (!isPhone && !TextUtils.isEmpty(phone) && phone.length() == 11) {
            showToast("请输入正确的手机号");
            return;
        }

        if (phone.length() == 11) {
            getVerifyCode(phone);
        }

        if (isPhone && !TextUtils.isEmpty(password) && password.length() >= 6) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }

        if (!TextUtils.isEmpty(phone)) {
            ibCleanPhone.setVisibility(View.VISIBLE);
            ibCleanPhone.setOnClickListener((v) -> etPhone.setText(""));
        } else {
            ibCleanPhone.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(password)) {
            ibCleanPsw.setVisibility(View.VISIBLE);
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


    @OnClick({R.id.btn_wechat_login, R.id.btn_qq_login, R.id.btn_login, R.id.tv_xieyi_login})
    public void onClick(View view) {
        KeyBoardUtil.hideInputMethod(this);
        switch (view.getId()) {
            case R.id.btn_wechat_login:
                showLoading("登录中...");
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.btn_qq_login:
                showLoading("登录中...");
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.btn_login:
                showLoading("登录中...");
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString();
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("username", phone);
                paramsMap.put("password", EncryptUtils.md5Hex(password));
                paramsMap.put("platform", "ANDROID");
                paramsMap.put("channelId", BaseApplication.getInstance().getChannel());
                mLoginPresent.login(paramsMap);
                break;
            case R.id.tv_xieyi_login:
                if (ClickUtil.isFastClick()) {
                    startActivity(new Intent(this, JsBridgeActivity.class)
                            .putExtra("url", SPUtils.get(this, SpConfig.USERAGREEURL, "").toString())
                            .putExtra("title", "用户协议"));
                }
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
        UserDao userDao = BaseApplication.getInstance().getDaoSession().getUserDao();
        User unique = userDao.queryBuilder().where(UserDao.Properties.UserId.eq(userInfo.getData().getUserId())).unique();
        if (unique == null) {
            User user = new User();
            user.setUserId(userInfo.getData().getUserId());
            user.setAvatar(userInfo.getData().getAvatar());
            user.setNickname(userInfo.getData().getNickname());
            user.setSeesionId(userInfo.getData().getSessionId());
            user.setRecharged(userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
            userDao.insert(user);
        } else {
            unique.setUserId(userInfo.getData().getUserId());
            unique.setAvatar(userInfo.getData().getAvatar());
            unique.setNickname(userInfo.getData().getNickname());
            unique.setSeesionId(userInfo.getData().getSessionId());
            unique.setRecharged(userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
            userDao.update(unique);
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
                            showToast("该手机号不存在");
                            btnLogin.setEnabled(false);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }
}
