package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.presenter.MePresenter;
import com.whzl.mengbi.presenter.impl.MePresenterImpl;
import com.whzl.mengbi.ui.activity.FollowActivity;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.activity.SettingActivity;
import com.whzl.mengbi.ui.activity.UserInfoActivity;
import com.whzl.mengbi.ui.activity.WatchHistoryActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.MeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * @author shaw
 */
public class MeFragment extends BaseFragment implements MeView {

    private static final int REQUEST_SETTING = 100;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.iv_level_icon)
    ImageView ivLevelIcon;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_mengbi_amount)
    TextView tvMengbiAmount;
    @BindView(R.id.tv_mengdou_amount)
    TextView tvMengdouAmount;
    private MePresenter mPresent;
    private UserInfo mUserinfo;
    private String deviceId;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresent = new MePresenterImpl(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        if (userInfo == null || userInfo.getData() == null) {
            return;
        }
        mUserinfo = userInfo;
        GlideImageLoader.getInstace().displayImage(getContext(), userInfo.getData().getAvatar(), ivAvatar);
        String nickname = SPUtils.get(getContext(), SpConfig.KEY_USER_NAME, "0").toString();
        tvNickName.setText(nickname);
        tvId.setText("萌号：" + userInfo.getData().getUserId());
        tvMengbiAmount.setText(userInfo.getData().getWealth().getCoin() + "");
        tvMengdouAmount.setText(userInfo.getData().getWealth().getChengPonit() + "");
        List<UserInfo.DataBean.LevelListBean> levelList = userInfo.getData().getLevelList();
        if (NetConfig.USER_TYPE_ANCHOR.equals(userInfo.getData().getUserType())) {
            for (UserInfo.DataBean.LevelListBean levelListBean : levelList) {
                if (NetConfig.LEVEL_TYPE_ANCHOR.equals(levelListBean.getLevelType())) {
                    ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelListBean.getLevelValue()));
                    break;
                }
            }
        } else {
            for (UserInfo.DataBean.LevelListBean levelListBean : levelList) {
                if (NetConfig.LEVEL_TYPE_USER.equals(levelListBean.getLevelType())) {
                    ivLevelIcon.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelListBean.getLevelValue()));
                    break;
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void init() {
        mPresent.getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfoUpdateEvent userInfoUpdateEvent) {
        mPresent.getUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_recharge, R.id.tv_my_follow, R.id.tv_setting, R.id.btn_edit, R.id.tv_watch_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_recharge:
                jumpToRechargeActivity();
                break;
            case R.id.tv_my_follow:
                jumpToFollowActivity();
                break;
            case R.id.tv_setting:
                jumpToSettingActivity();
                break;
            case R.id.btn_edit:
                jumpToPersonalInfoActivity();
                break;
            case R.id.tv_watch_history:
                jumpToWatchHistoryActivity();
                break;
            default:
                break;
        }
    }

    private void jumpToWatchHistoryActivity() {
        Intent intent = new Intent(getContext(), WatchHistoryActivity.class);
        startActivity(intent);
    }

    private void jumpToFollowActivity() {
        Intent intent = new Intent(getContext(), FollowActivity.class);
        startActivity(intent);
    }

    private void jumpToPersonalInfoActivity() {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra("userbean", mUserinfo);
        startActivity(intent);
    }

    private void jumpToSettingActivity() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivityForResult(intent, REQUEST_SETTING);
    }

    private void jumpToRechargeActivity() {
        Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING) {
            if (resultCode == RESULT_OK) {
                ((MainActivity) getActivity()).setCheck(0);
                HashMap paramsMap = new HashMap();
                paramsMap.put("platform", RequestManager.CLIENTTYPE);
                RxPermisssionsUitls.getDevice(getMyActivity(), new RxPermisssionsUitls.OnPermissionListener() {
                    @Override
                    public void onGranted() {
                        deviceId = DeviceUtils.getDeviceId(getContext());
                        paramsMap.put("deviceNumber", deviceId);
                        visitorLogin(paramsMap);
                    }

                    @Override
                    public void onDeny() {
                        paramsMap.put("deviceNumber", "");
                        visitorLogin(paramsMap);
                    }
                });
                visitorLogin(paramsMap);
            }
        }
    }

    private void visitorLogin(HashMap paramsMap) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.USER_VISITOR_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object object) {
                        VisitorUserInfo visitorUserInfo = GsonUtils.GsonToBean(object.toString(), VisitorUserInfo.class);
                        if (visitorUserInfo.getCode() == RequestManager.RESPONSE_CODE) {
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, visitorUserInfo.getData().getUserId());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, visitorUserInfo.getData().getSessionId());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, visitorUserInfo.getData().getNickname());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresent.getUserInfo();
        }
    }
}
