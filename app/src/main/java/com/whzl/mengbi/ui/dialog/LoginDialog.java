package com.whzl.mengbi.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.LoginSuccussEvent;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.greendao.User;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.LoginPresent;
import com.whzl.mengbi.presenter.impl.LoginPresenterImpl;
import com.whzl.mengbi.ui.activity.JsBridgeActivity;
import com.whzl.mengbi.ui.activity.LoginActivity;
import com.whzl.mengbi.ui.activity.RegisterActivity;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.view.LoginView;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.whzl.mengbi.util.ToastUtils.showToast;

/**
 * @author nobody
 * @date 2018/11/20
 */
public class LoginDialog extends BaseAwesomeDialog implements LoginView {

    @BindView(R.id.ll_wechat)
    LinearLayout llWechat;
    @BindView(R.id.ll_qq)
    LinearLayout llQQ;
    @BindView(R.id.ll_phone_login)
    LinearLayout llPhone;
    @BindView(R.id.ib_close)
    ImageButton ibClose;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_deal)
    TextView tvDeal;
    @BindView(R.id.ll_close)
    LinearLayout llClose;

    private LoginPresent mLoginPresent;
    private UMShareAPI umShareAPI;

    public BaseAwesomeDialog setLoginSuccessListener(LoginSuccessListener loginSuccessListener) {
        this.loginSuccessListener = loginSuccessListener;
        return this;
    }

    private LoginSuccessListener loginSuccessListener;

    public static LoginDialog newInstance() {
        LoginDialog dialog = new LoginDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }


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
            ((BaseActivity) getActivity()).dismissLoading();
            showToast(t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            ((BaseActivity) getActivity()).dismissLoading();
            showToast("用户取消");
            LogUtils.d("onError  platform" + platform);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umShareAPI = UMShareAPI.get(getActivity());
        mLoginPresent = new LoginPresenterImpl(this);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_login;
    }

    @Override
    public void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

    }


    @OnClick({R.id.ll_wechat, R.id.ll_qq, R.id.ll_close, R.id.ll_phone_login, R.id.tv_register, R.id.tv_deal, R.id.ib_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat:
                ((BaseActivity) getActivity()).showLoading("登录中...");
                umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.ll_qq:
                ((BaseActivity) getActivity()).showLoading("登录中...");
                umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.ll_close:
                dismiss();
                break;
            case R.id.ll_phone_login:
                getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class).
                        putExtra("from", "logindialog"), AppUtils.REQUEST_LOGIN);
                dismiss();
                break;
            case R.id.tv_register:
                getActivity().startActivityForResult(new Intent(getActivity(), RegisterActivity.class), AppUtils.REQUEST_LOGIN);
                dismiss();
                break;
            case R.id.tv_deal:
                getActivity().startActivity(new Intent(getActivity(), JsBridgeActivity.class)
                        .putExtra("url", NetConfig.USER_DEAL)
                        .putExtra("title", "用户协议"));
                break;
            case R.id.ib_close:
                dismiss();
                break;
        }
    }

    @Override
    public void showError(String msg) {
        ((BaseActivity) getActivity()).dismissLoading();
        showToast(msg);
    }

    @Override
    public void loginSuccess(UserInfo userInfo) {
        ((BaseActivity) getActivity()).dismissLoading();
        showToast(R.string.login_success);
        saveGreenDao(userInfo);
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, userInfo.getData().getUserId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, userInfo.getData().getSessionId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, userInfo.getData().getNickname());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, userInfo.getData().getLastRechargeTime()
                != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
        if (loginSuccessListener != null) {
            loginSuccessListener.onLoginSuccessListener();
        }
        dismiss();
        EventBus.getDefault().post(new LoginSuccussEvent());
    }

    private void saveGreenDao(UserInfo userInfo) {
        UserDao userDao = BaseApplication.getInstance().getDaoSession().getUserDao();
        User unique = userDao.queryBuilder().where(UserDao.Properties.UserId.eq(userInfo.getData().getUserId())).unique();
        if (unique == null) {
            User commonGift = new User();
            commonGift.setUserId(userInfo.getData().getUserId());
            commonGift.setAvatar(userInfo.getData().getAvatar());
            commonGift.setNickname(userInfo.getData().getNickname());
            commonGift.setSeesionId(userInfo.getData().getSessionId());
            commonGift.setRecharged(userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
            userDao.insert(commonGift);
        } else {
            unique.setUserId(userInfo.getData().getUserId());
            unique.setAvatar(userInfo.getData().getAvatar());
            unique.setNickname(userInfo.getData().getNickname());
            unique.setSeesionId(userInfo.getData().getSessionId());
            unique.setRecharged(userInfo.getData().getLastRechargeTime() != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
            userDao.update(unique);
        }
    }

    public interface LoginSuccessListener {
        void onLoginSuccessListener();
    }

}
