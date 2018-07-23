package com.whzl.mengbi.ui.fragment.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.eventbus.event.UserInfoUpdateEvent;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.MePresenter;
import com.whzl.mengbi.presenter.impl.MePresenterImpl;
import com.whzl.mengbi.ui.activity.FollowActivity;
import com.whzl.mengbi.ui.activity.SettingsActivity;
import com.whzl.mengbi.ui.activity.UserInfoActivity;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.ui.view.MeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * @author shaw
 */
public class MeFragment extends BaseFragment implements MeView {

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
    Unbinder unbinder;
    private MePresenter mPresent;
    private UserInfo mUserinfo;

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
        tvNickName.setText(userInfo.getData().getNickname());
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
    public void onMessageEvent(UserInfoUpdateEvent userInfoUpdateEvent){
        mPresent.getUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_recharge, R.id.tv_my_follow, R.id.tv_setting, R.id.btn_edit})
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
            default:
                break;
        }
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
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void jumpToRechargeActivity() {
        Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
        startActivity(intent);
    }

}
