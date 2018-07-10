package com.whzl.mengbi.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.MePresenter;
import com.whzl.mengbi.presenter.impl.MePresenterImpl;
import com.whzl.mengbi.ui.activity.RechargeActivity;
import com.whzl.mengbi.ui.activity.SettingsActivity;
import com.whzl.mengbi.ui.activity.UserInfoActivity;
import com.whzl.mengbi.ui.view.MeView;
import com.whzl.mengbi.ui.widget.view.CircleImageView;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.ui.fragment.BaseFragement;



public class MeFragment extends BaseFragement implements MeView,View.OnClickListener{

    private ImageView mUserEditorImg;
    private CircleImageView mUserCircleImageView;
    private TextView mNickNameText;
    private TextView mSproutText;
    private TextView mGoldText;
    private TextView mGoldBeanText;
    private Button mRechargeBut;
    private Button mMyFollowBut;
    private Button mWatchRecordBut;
    private Button mSettingsBut;
    private UserInfo mUserInfo;

    private MePresenter mePresenter;

    public static MeFragment newInstance(String info) {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_me_layout,null);
        mePresenter = new MePresenterImpl(this);
        initData();
        initView(mView);
        return mView;
    }

    /**
     *实例化控件
     */
    public void initView(View contentView){
        mUserEditorImg = (ImageView) contentView.findViewById(R.id.account_center_usereditor);
        mUserCircleImageView = (CircleImageView) contentView.findViewById(R.id.account_center_user_img);
        mNickNameText = (TextView) contentView.findViewById(R.id.account_center_user_nickname);
        mSproutText = (TextView)contentView.findViewById(R.id.account_center_sprout);
        mGoldText = (TextView) contentView.findViewById(R.id.account_center_gold_count);
        mGoldBeanText = (TextView) contentView.findViewById(R.id.account_center_goldbean_count);
        mRechargeBut = (Button) contentView.findViewById(R.id.account_center_recharge_but);
        mMyFollowBut = (Button) contentView.findViewById(R.id.account_center_myfollow_but);
        mWatchRecordBut = (Button) contentView.findViewById(R.id.account_center_watchrecord_but);
        mSettingsBut = (Button) contentView.findViewById(R.id.account_center_settings_but);

        mUserEditorImg.setOnClickListener(this);
        mRechargeBut.setOnClickListener(this);
        mMyFollowBut.setOnClickListener(this);
        mWatchRecordBut.setOnClickListener(this);
        mSettingsBut.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    public  void  initData(){
        mePresenter.getUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_center_usereditor://编辑
                Intent mUserEditorIntent = new Intent(mContext, UserInfoActivity.class);
                Bundle mUserEditorBundle = new Bundle();
                mUserEditorBundle.putSerializable("userbean",mUserInfo);
                mUserEditorIntent.putExtras(mUserEditorBundle);
                startActivity(mUserEditorIntent);
                break;
            case R.id.account_center_recharge_but://充值
                Intent rechargeIntent = new Intent(mContext, RechargeActivity.class);
                rechargeIntent.putExtra("profile",mUserInfo.getData().getAvatar());//用户头像
                rechargeIntent.putExtra("userId",mUserInfo.getData().getUserId());//用户账号
                rechargeIntent.putExtra("coin",mUserInfo.getData().getWealth().getCoin());//萌币余额
                startActivity(rechargeIntent);
                break;
            case R.id.account_center_settings_but://设置
                Intent settingsIntent = new Intent(mContext, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        GlideImageLoader.getInstace().circleCropImage(mContext, userInfo.getData().getAvatar(), mUserCircleImageView);
        mNickNameText.setText(userInfo.getData().getNickname());
        mSproutText.setText(userInfo.getData().getUserId()+"");
        mGoldText.setText(userInfo.getData().getWealth().getCoin()+"");
        mGoldBeanText.setText(userInfo.getData().getWealth().getChengPonit()+"");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mePresenter.onDestroy();
    }
}
