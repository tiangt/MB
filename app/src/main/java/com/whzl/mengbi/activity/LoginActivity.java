package com.whzl.mengbi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.application.BaseAppliaction;
import com.whzl.mengbi.bean.UserBean;
import com.whzl.mengbi.bean.TouristUserBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.thread.LoginThread;
import com.whzl.mengbi.thread.TouristLoginThread;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.PermissionUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.widget.view.GenericToolbar;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * 登录页面
 */
public class LoginActivity extends BaseAtivity implements View.OnClickListener{
    /**
     *用户名，手机号
     */
    private EditText longinUserName;
    private String mUserName;

    /**
     *用户密码
     */
    private EditText longinUserPassword;
    private String mUserPassword;

    /**
     * 登录按钮
     */
    private Button login;
    /**
     * QQ登陆
     */
    private ImageView qqUserLoginIv;
    /**
     * 微信登陆
     */
    private ImageView weixinUserLoginIv;

    private UserBean mUserBean;
    /**
     * 匿名用户信息
     */
    private TouristUserBean mTouristUserBean;
    private TouristLoginThread mTouristLoginThread;
    private TouristHandler mTouristHandler = new TouristHandler(this);

    private LoginThread mLoginThread;
    private LoginHandler mLoginHandler = new LoginHandler(this);
    private String touristFlag ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        //PermissionUtils.requestPermission(this,PermissionUtils.CODE_READ_PHONE_STATE,mPermissionGrant);
        requestPermissions(this,PermissionUtils.CODE_READ_PHONE_STATE);
        if(getIntent()!=null){
            touristFlag  = getIntent().getStringExtra("touristFlag") ;
        }

        initView();
        initToolBar();
        touristLogin();
    }

    private void initView(){
        longinUserName = (EditText)findViewById(R.id.login_user_name);
        longinUserPassword = (EditText)findViewById(R.id.login_user_password);
        login =(Button) findViewById(R.id.login_user_login);
        qqUserLoginIv = (ImageView) findViewById(R.id.login_qq_login);
        weixinUserLoginIv = (ImageView) findViewById(R.id.login_weixin_login);
        login.setOnClickListener(this);
        qqUserLoginIv.setOnClickListener(this);
        weixinUserLoginIv.setOnClickListener(this);
    }

    public void  initToolBar(){
        new GenericToolbar.Builder(this)
                .addTitleText("登录",22f)// 标题文本
                .setBackgroundColorResource(R.color.colorPrimary)// 背景颜色
                .addLeftIcon(1, R.drawable.ic_login_return, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })// 响应左部图标的点击事件
                .addRightText(3, "注册", 22f, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mTntent = new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivityForResult(mTntent,0);
                    }
                })// 响应右部文本的点击事件
                .setTextColor(getResources().getColor(R.color.colorPrimaryDark))// 会全局设置字体的颜色, 自定义标题除外
                .apply();
    }

    @Override
    public void onClick(View v) {
        mUserName = longinUserName.getText().toString().trim();
        mUserPassword = longinUserPassword.getText().toString().trim();
        switch (v.getId()){
            case R.id.login_user_login:
                autoLogin();
                break;
            case R.id.login_qq_login:
                ToastUtils.showToast("暂未开放");
                break;
            case R.id.login_weixin_login:
                ToastUtils.showToast("暂未开放");
                break;
            case 3://标题注册跳转
                Intent mTntent = new Intent(this,RegisterActivity.class);
                startActivityForResult(mTntent,0);
                break;
             default:
                 break;
            }
        }

    /**
     * 匿名登录
     */
    public void touristLogin(){
        int userId = (Integer) SPUtils.get(this,"userId",0);
        if(userId==0){
            if (!TextUtils.isEmpty(touristFlag)&&touristFlag.equals("0")) {
                return;
            }else {
                HashMap paramsMap = new HashMap();
                paramsMap.put("platform","ANDROID");
                paramsMap.put("deviceNumber", DeviceUtils.getDeviceId(this));
                mTouristLoginThread = new TouristLoginThread(this,paramsMap,mTouristHandler);
                mTouristLoginThread.start();
            }
        }else{
            autoLogin();
        }
    }

    /**
     * 自动登录
     */
    public void autoLogin(){
        String userName = SPUtils.get(this,"username","").toString();
        String passWord = SPUtils.get(this,"password","").toString();
        HashMap paramsMap = new HashMap();
        if(!TextUtils.isEmpty(userName)||!TextUtils.isEmpty(passWord)){
            paramsMap.put("username",userName);
            String pass= EncryptUtils.md5Hex(passWord);
            paramsMap.put("password",pass);
            paramsMap.put("platform","ANDROID");
        }else{
            paramsMap.put("username",mUserName);
            String pass= EncryptUtils.md5Hex(mUserPassword);
            paramsMap.put("password",pass);
            paramsMap.put("platform","ANDROID");
        }
        mLoginThread = new LoginThread(this,paramsMap,mLoginHandler);
        mLoginThread.start();
    }

    /**
     * 用户登录Handler
     为避免handler造成的内存泄漏
     1、使用静态的handler，对外部类不保持对象的引用
     2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
    private static class LoginHandler extends Handler{
        private final WeakReference<Activity> mActivityWeakReference;
        LoginHandler(Activity activity){
            this.mActivityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity loginActivity = (LoginActivity)mActivityWeakReference.get();
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    loginActivity.mUserBean = (UserBean) msg.obj;
                    //保存登录用户信息
                    if(!TextUtils.isEmpty(loginActivity.mUserName)||!TextUtils.isEmpty(loginActivity.mUserPassword)){
                        SPUtils.put(loginActivity,"username",loginActivity.mUserName);
                        SPUtils.put(loginActivity,"password",loginActivity.mUserPassword);
                    }
                    SPUtils.put(loginActivity,"userId",loginActivity.mUserBean.getData().getUserId());
                    SPUtils.put(loginActivity,"sessionId",loginActivity.mUserBean.getData().getSessionId());
                    SPUtils.put(loginActivity,"islogin",true);
                    SPUtils.put(loginActivity,"isautologin",true);
                    Intent mIntent = new Intent(loginActivity,HomeActivity.class);
                    loginActivity.startActivity(mIntent);
                    break;
            }
        }
    }

    /**
     * 游客登录Handler
     为避免handler造成的内存泄漏
     1、使用静态的handler，对外部类不保持对象的引用
     2、但Handler需要与Activity通信，所以需要增加一个对Activity的弱引用
     */
    private static class TouristHandler extends Handler{
        private final WeakReference<Activity> mActivityWeakReference;
        TouristHandler(Activity activity){
            this.mActivityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity loginActivity = (LoginActivity)mActivityWeakReference.get();
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //保存匿名登录用户信息
                    loginActivity.mTouristUserBean = (TouristUserBean) msg.obj;
                    SPUtils.put(loginActivity,"userId",loginActivity.mTouristUserBean.getData().getUserId());
                    SPUtils.put(loginActivity,"nickname",loginActivity.mTouristUserBean.getData().getNickname());
                    SPUtils.put(loginActivity,"sessionId",loginActivity.mTouristUserBean.getData().getSessionId());
                    SPUtils.put(loginActivity,"islogin",false);
                    Intent mIntent = new Intent(loginActivity,HomeActivity.class);
                    loginActivity.startActivity(mIntent);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.item_user_register:
                Intent mTntent = new Intent(this,RegisterActivity.class);
                startActivityForResult(mTntent,0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            String result = data.getStringExtra("userName");
            longinUserName.setText(result);
        }
    }

//    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
//        @Override
//        public void onPermissionGranted(int requestCode) {
//            switch (requestCode) {
//                case PermissionUtils.CODE_RECORD_AUDIO:
//                    //Toast.makeText(LoginActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

//    /**
//     * 当权限请求已完成时接收。
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        PermissionUtils.requestPermissionsResult(this,requestCode,permissions,grantResults,mPermissionGrant);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
