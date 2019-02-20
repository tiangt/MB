package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.gen.CommonGiftDao;
import com.whzl.mengbi.model.entity.GetNewTaskBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.presenter.MePresenter;
import com.whzl.mengbi.presenter.impl.MePresenterImpl;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.activity.SettingActivity;
import com.whzl.mengbi.ui.activity.UserInfoActivity;
import com.whzl.mengbi.ui.activity.me.AccountSwitchActivity;
import com.whzl.mengbi.ui.activity.me.BillActivity;
import com.whzl.mengbi.ui.activity.me.ChipCompositeActivity;
import com.whzl.mengbi.ui.activity.me.MyGuardActivity;
import com.whzl.mengbi.ui.activity.me.MyLevelActivity;
import com.whzl.mengbi.ui.activity.me.MyWalletActivity;
import com.whzl.mengbi.ui.activity.me.PackActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.MeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.StringUtils;
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

import static android.app.Activity.RESULT_OK;

/**
 * @author cliang
 * @date 2019.2.15
 */
public class MineFragment extends BaseFragment implements MeView {

    private static final int REQUEST_SETTING = 100;
    private static final int REQUEST_BINDING = 101;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.iv_user_gender)
    ImageView ivGender;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_mengbi)
    TextView tvMengbi;
    @BindView(R.id.tv_mengdou)
    TextView tvMengdou;
    @BindView(R.id.iv_mine_noble)
    ImageView ivMineNoble;
    @BindView(R.id.iv_mine_level)
    ImageView ivMineLevel;
    @BindView(R.id.tv_follow_count)
    TextView tvFollowCount;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;

    private MePresenter mPresent;
    private UserInfo mUserInfo;
    private String deviceId;
    private String mMobile;
    private long mMengbi;
    private long mMengdou;
    private long mMengdian;
    private long mUserId;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        mPresent = new MePresenterImpl(this);
        EventBus.getDefault().register(this);
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
    public void showUserInfo(UserInfo userInfo) {
        if (userInfo == null || userInfo.getData() == null) {
            return;
        }
        mUserInfo = userInfo;
        GlideImageLoader.getInstace().displayImage(getContext(), userInfo.getData().getAvatar(), ivAvatar);
        tvNickName.setText(mUserInfo.getData().getNickname().trim());
        SPUtils.put(getContext(), SpConfig.KEY_USER_NAME, mUserInfo.getData().getNickname().trim());

        //性别
        if ("M".equals(mUserInfo.getData().getGender())) {
            ivGender.setImageResource(R.drawable.ic_mine_man);
        } else if ("W".equals(mUserInfo.getData().getGender())) {
            ivGender.setImageResource(R.drawable.ic_mine_woman);
        }

        //昵称，头像，ID
        tvUserId.setText("萌号：" + userInfo.getData().getUserId());
        mUserId = userInfo.getData().getUserId();

        //粉丝，关注
        tvFansCount.setText(userInfo.getData().getFansNum() + "");
        tvFollowCount.setText(userInfo.getData().getMyFollowNum() + "");

        mMengbi = userInfo.getData().getWealth().getCoin();
        mMengdou = userInfo.getData().getWealth().getMengDou();
        mMengdian = userInfo.getData().getWealth().getChengPonit();

        tvMengbi.setText(StringUtils.formatNumber(mMengbi) + "萌币");
        tvMengdou.setText(StringUtils.formatNumber(mMengdou) + "萌豆");
        mMobile = userInfo.getData().getBindMobile();

        List<UserInfo.DataBean.LevelListBean> levelList = userInfo.getData().getLevelList();
        if (NetConfig.USER_TYPE_ANCHOR.equals(userInfo.getData().getUserType())) {
            for (UserInfo.DataBean.LevelListBean levelListBean : levelList) {
                if (NetConfig.LEVEL_TYPE_ANCHOR.equals(levelListBean.getLevelType())) {
                    ivMineLevel.setImageResource(ResourceMap.getResourceMap().getAnchorLevelIcon(levelListBean.getLevelValue()));
                    break;
                }
            }
        } else {
            for (UserInfo.DataBean.LevelListBean levelListBean : levelList) {
                if (NetConfig.LEVEL_TYPE_USER.equals(levelListBean.getLevelType())) {
                    ivMineLevel.setImageResource(ResourceMap.getResourceMap().getUserLevelIcon(levelListBean.getLevelValue()));
                    break;
                }
            }
        }

        //贵族等级
        for (UserInfo.DataBean.LevelListBean levelListBean : levelList) {
            if ("ROYAL_LEVEL".equals(levelListBean.getLevelType())) {
                Glide.with(this).asGif().load(ResourceMap.getResourceMap().getRoyalLevelIcon(levelListBean.getLevelValue())).into(ivMineNoble);
                break;
            }
        }
    }

    @Override
    public void getNewTask(GetNewTaskBean bean) {

    }

    @OnClick({R.id.iv_switch, R.id.iv_setting, R.id.rl_info_edit, R.id.btn_recharge, R.id.rl_shop,
            R.id.tv_mine_tool, R.id.tv_mine_vip, R.id.tv_mine_pretty, R.id.tv_mine_car,
            R.id.tv_mine_coupon, R.id.rl_composite, R.id.rl_bill, R.id.rl_my_wallet, R.id.ll_my_level,
            R.id.rl_my_guard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_switch:
                jumpToAccountSwitchActivity();
                break;

            case R.id.iv_setting:
                jumpToSettingActivity();
                break;

            case R.id.rl_info_edit:
                jumpToPersonalInfoActivity();
                break;

            case R.id.btn_recharge:
                jumpToRechargeActivity();
                break;

            case R.id.rl_shop:
                startActivity(new Intent(getMyActivity(), ShopActivity.class));
                break;

            case R.id.tv_mine_tool:
                Intent intent = new Intent(getMyActivity(), PackActivity.class);
                intent.putExtra("index", 0);
                startActivity(intent);
                break;

            case R.id.tv_mine_vip:
                Intent intentVip = new Intent(getMyActivity(), PackActivity.class);
                intentVip.putExtra("index", 1);
                startActivity(intentVip);
                break;

            case R.id.tv_mine_pretty:
                Intent intentPretty = new Intent(getMyActivity(), PackActivity.class);
                intentPretty.putExtra("index", 2);
                startActivity(intentPretty);
                break;

            case R.id.tv_mine_car:
                Intent intentCar = new Intent(getMyActivity(), PackActivity.class);
                intentCar.putExtra("index", 3);
                startActivity(intentCar);
                break;

            case R.id.tv_mine_coupon:
                Intent intentCoupon = new Intent(getMyActivity(), PackActivity.class);
                intentCoupon.putExtra("index", 4);
                startActivity(intentCoupon);
                break;

            case R.id.rl_composite:
                jumpToChipCompositeActivity();
                break;

            case R.id.rl_bill:
                startActivity(new Intent(getMyActivity(), BillActivity.class));
                break;

            case R.id.rl_my_wallet:
                jumpToMyWalletActivity();
                break;

            case R.id.ll_my_level:
                jumpToMyLevelActivity();
                break;

            case R.id.rl_my_guard:
                jumpToMyGuardActivity();
                break;

            default:
                break;
        }
    }

    private void jumpToAccountSwitchActivity() {
        Intent intent = new Intent(getContext(), AccountSwitchActivity.class);
        startActivity(intent);
    }

    private void jumpToSettingActivity() {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivityForResult(intent, REQUEST_SETTING);
    }

    private void jumpToPersonalInfoActivity() {
        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra("userbean", mUserInfo);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getMyActivity(), ivAvatar, "iv_avatar");
        startActivity(intent, optionsCompat.toBundle());
    }

    private void jumpToRechargeActivity() {
        Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
        startActivity(intent);
    }

    private void jumpToChipCompositeActivity() {
        Intent intent = new Intent(getContext(), ChipCompositeActivity.class);
        startActivity(intent);
    }

    private void jumpToMyWalletActivity() {
        Intent intent = new Intent(getContext(), MyWalletActivity.class);
        intent.putExtra("mengbi", mMengbi);
        intent.putExtra("mengdou", mMengdou);
        intent.putExtra("mengdian", mMengdian);
        startActivity(intent);
    }

    private void jumpToMyLevelActivity() {
        Intent intent = new Intent(getContext(), MyLevelActivity.class);
        intent.putExtra("userId", mUserId);
        startActivity(intent);
    }

    private void jumpToMyGuardActivity(){
        Intent intent = new Intent(getContext(), MyGuardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING) {
            if (resultCode == RESULT_OK) {
                ((MainActivity) getActivity()).setCheck(0);
                Long aLong = (Long) SPUtils.get(getContext(), SpConfig.KEY_USER_ID, 0L);
                removeGreenDao(aLong);
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

        if (requestCode == REQUEST_BINDING) {
            if (resultCode == RESULT_OK) {
                mPresent.getUserInfo();
            }
        }
    }

    private void removeGreenDao(Long aLong) {
        CommonGiftDao commonGiftDao = BaseApplication.getInstance().getDaoSession().getCommonGiftDao();
        commonGiftDao.deleteByKey(aLong);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
