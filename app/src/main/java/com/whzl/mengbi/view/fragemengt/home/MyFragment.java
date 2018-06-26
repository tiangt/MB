package com.whzl.mengbi.view.fragemengt.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.LoginActivity;
import com.whzl.mengbi.activity.PersonalInformationActivity;
import com.whzl.mengbi.activity.RegisterActivity;
import com.whzl.mengbi.application.BaseAppliaction;
import com.whzl.mengbi.bean.UserBean;
import com.whzl.mengbi.glide.GlideImageLoader;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.view.fragemengt.BaseFragement;
import com.whzl.mengbi.widget.view.CircleImageView;
import com.whzl.mengbi.widget.view.GenericToolbar;

import org.w3c.dom.Text;

import java.util.HashMap;


public class MyFragment extends BaseFragement implements View.OnClickListener{

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

    private UserBean mUserBean;


    public static MyFragment newInstance(String info) {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
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
        View mView = inflater.inflate(R.layout.fragment_my_layout,null);
        initData();
        initToolBar();
        initView(mView);
        return mView;
    }

    /**
     * 实例化标题栏
     */
    public void  initToolBar(){
        new GenericToolbar.Builder(mContext)
                .addTitleText("账户中心",22f)// 标题文本
                .setBackgroundColorResource(R.color.colorPrimary)// 背景颜色
                .addLeftIcon(1, R.mipmap.ic_login_return, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFragmentManager().popBackStack();
                    }
                })// 响应左部图标的点击事件
                .setTextColor(getResources().getColor(R.color.colorPrimaryDark))// 会全局设置字体的颜色, 自定义标题除外
                .apply();
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
        GlideImageLoader.getInstace().displayImage(mContext,mUserBean.getData().getAvatar(),mUserCircleImageView);
        mNickNameText.setText(mUserBean.getData().getNickname());
        mSproutText.setText(mUserBean.getData().getUserId());
        mGoldText.setText(mUserBean.getData().getWealth().getCoin());
        mGoldBeanText.setText(mUserBean.getData().getWealth().getChengPonit());
    }

    /**
     * 初始化数据
     */
    public  void  initData(){
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", BaseAppliaction.getInstance().getUserId());
        RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.GET_USER_INFO,RequestManager.TYPE_POST_JSON,paramsMap,
                new RequestManager.ReqCallBack(){
            @Override
            public void onReqSuccess(Object result) {
                mUserBean = GsonUtils.GsonToBean(result.toString(),UserBean.class);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d("onReqFailed"+errorMsg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_center_usereditor:
                Intent mUserEditorIntent = new Intent(mContext, PersonalInformationActivity.class);
                startActivity(mUserEditorIntent);
                break;
        }
    }
}
