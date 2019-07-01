package com.whzl.mengbi.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.library.StatusBarUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.greendao.User;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.LoginPresent;
import com.whzl.mengbi.presenter.impl.LoginPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.view.LoginView;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/17
 */
public class FirstActivity extends BaseActivity implements LoginView {

    private static final int REQUEST_LOGIN = 200;
    @BindView(R.id.iv_first)
    ImageView ivFirst;
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
    private Disposable disposable;

    @Override
    protected void initEnv() {
        super.initEnv();
        umShareAPI = UMShareAPI.get(this);
        mLoginPresent = new LoginPresenterImpl(this);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.statusbar_black));
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_first);
    }

    @Override
    protected void setupView() {
    }

    private int[] imgs = new int[]{
            R.drawable.img_first_1,
            R.drawable.img_first_2,
            R.drawable.img_first_3,
            R.drawable.img_first_4,
            R.drawable.img_first_5};

    @SuppressLint("CheckResult")
    @Override
    protected void loadData() {
        disposable = Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    LogUtils.e("sssssssssssss  " + aLong);
                    RequestOptions requestOptions = new RequestOptions().dontAnimate().placeholder(ivFirst.getDrawable());

                    Glide.with(this).load(imgs[(int) (aLong % 5)]).apply(requestOptions).into(ivFirst);
                });
    }

    @OnClick({R.id.btn_wechat_first, R.id.btn_qq_first, R.id.tv_xieyi_first, R.id.btn_phone_first, R.id.tv_random_first})
    public void onClick(View view) {
        KeyBoardUtil.hideInputMethod(this);
        switch (view.getId()) {
            case R.id.btn_wechat_first:
                showLoading("登录中...");
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.btn_qq_first:
                showLoading("登录中...");
                umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.btn_phone_first:
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class)
                        .putExtra("from", FirstActivity.class.toString());
                startActivityForResult(intent, REQUEST_LOGIN);
                break;
            case R.id.tv_random_first:
                Intent intent2 = new Intent(FirstActivity.this, MainActivity.class);
                intent2.putExtra("isNormalLogin", false);
                startActivity(intent2);
                finish();
                break;
        }
    }

    @Override
    public void loginSuccess(UserInfo userInfo) {
        dismissLoading();
        showToast(R.string.login_success);
        saveGreenDao(userInfo);
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, userInfo.getData().getUserId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, userInfo.getData().getSessionId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, userInfo.getData().getNickname());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_BIND_MOBILE, userInfo.getData().getBindMobile());

        BusinessUtils.uploadVistorHistory();
        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
        intent.putExtra("isNormalLogin", true);
        startActivity(intent);
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
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                intent.putExtra("isNormalLogin", true);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        umShareAPI.release();
        if (disposable != null) {
            disposable.dispose();
        }
    }

}
