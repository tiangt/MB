package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.GetNewTaskBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.presenter.MePresenter;
import com.whzl.mengbi.presenter.impl.MePresenterImpl;
import com.whzl.mengbi.ui.activity.FollowActivity;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.activity.SettingActivity;
import com.whzl.mengbi.ui.activity.UserInfoActivity;
import com.whzl.mengbi.ui.activity.WatchHistoryActivity;
import com.whzl.mengbi.ui.activity.base.FrgActivity;
import com.whzl.mengbi.ui.activity.me.BillActivity;
import com.whzl.mengbi.ui.activity.me.BindingPhoneActivity;
import com.whzl.mengbi.ui.activity.me.ChipCompositeActivity;
import com.whzl.mengbi.ui.activity.me.MyChipActivity;
import com.whzl.mengbi.ui.activity.me.PackActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.fragment.me.WelfareFragment;
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
    @BindView(R.id.tv_shop)
    TextView tvShop;
    @BindView(R.id.tv_packsack)
    TextView tvPacksack;
    @BindView(R.id.tv_welfare)
    TextView tvWelfare;
    @BindView(R.id.tv_bill)
    TextView tvBill;
    @BindView(R.id.view_notify)
    View viewNotify;
    @BindView(R.id.rl_binding_phone)
    RelativeLayout rlBindingPhone;
    @BindView(R.id.tv_composite)
    TextView tvComposite;

    Unbinder unbinder;
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
//        String nickname = SPUtils.get(getContext(), SpConfig.KEY_USER_NAME, "0").toString();
        tvNickName.setText(mUserinfo.getData().getNickname().trim());
        SPUtils.put(getContext(), SpConfig.KEY_USER_NAME, mUserinfo.getData().getNickname().trim());
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
    public void getNewTask(GetNewTaskBean bean) {
        if (bean.awardUngrant == 0) {
            viewNotify.setVisibility(View.INVISIBLE);
        } else {
            viewNotify.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.btn_recharge, R.id.tv_my_follow, R.id.tv_setting, R.id.btn_edit,
            R.id.tv_watch_history, R.id.rl_binding_phone, R.id.tv_composite})
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
            case R.id.rl_binding_phone:
                jumpToBindingPhoneActivity();
                break;
            case R.id.tv_composite:
                jumpToChipCompositeActivity();
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
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getMyActivity(), ivAvatar, "iv_avatar");
        startActivity(intent, optionsCompat.toBundle());
    }

    private void jumpToSettingActivity() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivityForResult(intent, REQUEST_SETTING);
    }

    private void jumpToRechargeActivity() {
        Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
        startActivity(intent);
    }

    private void jumpToBindingPhoneActivity() {
        Intent intent = new Intent(getContext(), BindingPhoneActivity.class);
        startActivity(intent);
    }

    private void jumpToChipCompositeActivity(){
        Intent intent = new Intent(getContext(), ChipCompositeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING) {
            if (resultCode == RESULT_OK) {
                ((MainActivity) getActivity()).setCheck(0);
                SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L);
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
//                visitorLogin(paramsMap);
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
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, false);
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

    @Override
    public void onResume() {
        super.onResume();
        mPresent.getUserInfo();
        mPresent.getNewTask();
    }

    @OnClick({R.id.tv_shop, R.id.tv_packsack, R.id.tv_welfare, R.id.tv_bill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shop:
                startActivity(new Intent(getMyActivity(), ShopActivity.class));
                break;
            case R.id.tv_packsack:
                startActivity(new Intent(getMyActivity(), PackActivity.class));
                break;
            case R.id.tv_welfare:
                startActivity(new Intent(getMyActivity(), FrgActivity.class)
                        .putExtra(FrgActivity.FRAGMENT_CLASS, WelfareFragment.class));
                break;
            case R.id.tv_bill:
                startActivity(new Intent(getMyActivity(), BillActivity.class));
                break;
        }
    }
}
