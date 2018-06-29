package com.whzl.mengbi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.activity.camera.CameraActivity;
import com.whzl.mengbi.bean.UserBean;
import com.whzl.mengbi.util.PermissionUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.widget.view.CircleImageView;
import com.whzl.mengbi.widget.view.CustomPopWindow;
import com.whzl.mengbi.widget.view.GenericToolbar;

public class PersonalInformationActivity extends BaseAtivity implements View.OnClickListener{

    private UserBean mUserBean;
    private CircleImageView mCircleImageView;
    private TextView mSpout;
    private TextView mNickName;
    private TextView mSex;
    private TextView mAddress;
    private TextView mBirthday;
    private TextView mAnchorLevel;
    private ImageView mAnchorImg;
    private TextView mUserLevel;
    private ImageView mUserImg;
    private Button mQuit;

    //更换头像
    private CustomPopWindow mCustomPopWindow;
    private Button butPhoto;
    private Button butAlbum;
    private Button butCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_layout);
        initToolBar();
        initView();
    }

    /**
     * 实例化toolbar标题
     */
    public void  initToolBar(){
       new GenericToolbar.Builder(this)
               .addTitleText("个人信息")// 标题文本
               .setBackgroundColorResource(R.color.colorPrimary)// 背景颜色
               .addLeftIcon(1, R.drawable.ic_login_return,new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       finish();
                   }
               })// 响应左部图标的点击事件
               .setTextColor(getResources().getColor(R.color.colorPrimaryDark))// 会全局设置字体的颜色, 自定义标题除外
               .apply();

   }

    /**
     * 实例化控件
     */
   public void  initView(){
        mCircleImageView = (CircleImageView) findViewById(R.id.personal_information_head_img);
        mSpout =(TextView)  findViewById(R.id.personal_information_sprount);
        mNickName = (TextView) findViewById(R.id.personal_information_address);
        mSex = (TextView) findViewById(R.id.personal_information_sex);
        mAddress = (TextView) findViewById(R.id.personal_information_address);
        mBirthday = (TextView) findViewById(R.id.personal_information_birthday);
        mAnchorLevel = (TextView) findViewById(R.id.personal_information_anchorlevel);
        mAnchorImg = (ImageView) findViewById(R.id.personal_information_anchorlevel_img);
        mUserLevel = (TextView) findViewById(R.id.personal_information_userlevel);
        mUserImg = findViewById(R.id.personal_information_userlevel_img);
        mQuit = (Button) findViewById(R.id.personal_information_quit_but);

        mCircleImageView.setOnClickListener(this);
        mQuit.setOnClickListener(this);
    }

    /**
     * 监听view触发事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = DensityUtil.dp2px(200);
        switch (v.getId()){
            case R.id.personal_information_head_img:
                requestPermissions(this,PermissionUtils.CODE_CAMERA);
                View view = getLayoutInflater().inflate(R.layout.activity_personal_information_photo_pop_layout,null);
                mCustomPopWindow  = new CustomPopWindow.PopupWindowBuilder(this)
                        .setView(view)//显示的布局，还可以通过设置一个View
                        .size(width,height) //设置显示的大小，不设置就默认包裹内容
                        .setFocusable(true)//是否获取焦点，默认为ture
                        .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                        .create()//创建PopupWindow
                        .showAtLocation(mCircleImageView, Gravity.BOTTOM,0,0);
                //初始化CustomPopWindow控件
                initPopView(view);
                break;
            case R.id.personal_information_quit_but:
                Intent mIntent = new Intent(PersonalInformationActivity.this,LoginActivity.class);
                mIntent.putExtra("touristFlag","0");
                startActivity(mIntent);
                finish();
                SPUtils.clear(this);
                break;
        }
    }

    private void initPopView(View view){
        butPhoto = (Button) view.findViewById(R.id.personal_information_photo);
        butAlbum = (Button) view.findViewById(R.id.personal_information_album);
        butCancel = (Button) view.findViewById(R.id.personal_information_cancel);
        butPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(PersonalInformationActivity.this, CameraActivity.class);
                startActivity(photoIntent);
            }
        });
        butAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomPopWindow.dissmiss();
            }
        });
    }
}
