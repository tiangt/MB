package com.whzl.mengbi.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.LoginPresent;
import com.whzl.mengbi.presenter.impl.LoginPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.view.LoginView;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;

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

    @BindView(R.id.btn_wechat_login)
    ImageButton btnWechatLogin;
    @BindView(R.id.btn_qq_login)
    ImageButton btnQqLogin;

    private LoginPresent mLoginPresent;
    private UMShareAPI umShareAPI;

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


    @OnClick({R.id.btn_wechat_login, R.id.btn_qq_login})
    public void onViewClicked(View view) {
        ((BaseActivity) getActivity()).showLoading("登录中...");
        switch (view.getId()) {
            case R.id.btn_wechat_login:
                umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.btn_qq_login:
                umShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.QQ, umAuthListener);
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
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, userInfo.getData().getUserId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, userInfo.getData().getSessionId());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, userInfo.getData().getNickname());
        SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, userInfo.getData().getLastRechargeTime()
                != null && !TextUtils.isEmpty(userInfo.getData().getLastRechargeTime()));
        dismiss();
    }
}
