package com.whzl.mengbi.ui.fragemengt.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.PersonalInformationActivity;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.thread.MeThread;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.ui.fragemengt.BaseFragement;
import com.whzl.mengbi.widget.view.CircleImageView;

import java.lang.ref.WeakReference;
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
    private UserInfo mUserInfo;

    private MeThread meThread;
    private final MeHandler meHandler = new MeHandler(this);

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
    }

    /**
     * 初始化数据
     */
    public  void  initData(){
        HashMap paramsMap = new HashMap();
        int userId = (Integer) SPUtils.get(mContext,"userId",0);
        paramsMap.put("userId",userId);
        meThread = new MeThread(mContext,paramsMap,meHandler);
        meThread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_center_usereditor://编辑
                Intent mUserEditorIntent = new Intent(mContext, PersonalInformationActivity.class);
                Bundle mUserEditorBundle = new Bundle();
                mUserEditorBundle.putSerializable("userbean",mUserInfo);
                mUserEditorIntent.putExtras(mUserEditorBundle);
                startActivity(mUserEditorIntent);
                break;
        }
    }


    /**
     为避免handler造成的内存泄漏
     1、使用静态的handler，对外部类不保持对象的引用
     2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
    private static class MeHandler extends Handler{
        private final WeakReference<Fragment> mFragmentWeakReference;
        MeHandler(Fragment fragment) {
            this.mFragmentWeakReference = new WeakReference<Fragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            MyFragment myFragment = (MyFragment)mFragmentWeakReference.get();
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    myFragment.mUserInfo = (UserInfo) msg.obj;
                    if(myFragment.mUserInfo!=null){
                        GlideImageLoader.getInstace().displayImage(myFragment.mContext,myFragment.mUserInfo.getData().getAvatar(),myFragment.mUserCircleImageView);
                        myFragment.mNickNameText.setText(myFragment.mUserInfo.getData().getNickname());
                        myFragment.mSproutText.setText(myFragment.mUserInfo.getData().getUserId()+"");
                        myFragment.mGoldText.setText(myFragment.mUserInfo.getData().getWealth().getCoin()+"");
                        myFragment.mGoldBeanText.setText(myFragment.mUserInfo.getData().getWealth().getChengPonit()+"");
                    }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
