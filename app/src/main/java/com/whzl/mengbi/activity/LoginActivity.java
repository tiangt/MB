package com.whzl.mengbi.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.application.BaseAppliaction;
import com.whzl.mengbi.bean.UserBean;
import com.whzl.mengbi.bean.VisitorUserBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.widget.view.GenericToolbar;

import java.util.HashMap;

/**
 * 登录页面
 */
public class LoginActivity extends BaseAtivity implements View.OnClickListener{
    /**
     *用户名，手机号
     */
    private EditText longinUserName;
    private String userName;

    /**
     *用户密码
     */
    private EditText longinUserPassword;
    private String userPassword;

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
    /**
     * 匿名用户信息
     */
    private VisitorUserBean visitorUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        initView();
        initToolBar();
       if(!BaseAppliaction.getInstance().isIslogin()){
           visitorLogin();
       }
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
                .addLeftIcon(1, R.mipmap.ic_login_return, new View.OnClickListener() {
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
        userName = longinUserName.getText().toString().trim();
        userPassword = longinUserPassword.getText().toString().trim();
        switch (v.getId()){
            case R.id.login_user_login:
                //用户登录
                HashMap paramsMap = new HashMap();
                paramsMap.put("username",userName);
                String pass= EncryptUtils.md5Hex(userPassword);
                paramsMap.put("password",pass);
                paramsMap.put("platform","ANDROID");
                RequestManager.getInstance(this).requestAsyn(URLContentUtils.USER_NORMAL_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                        new RequestManager.ReqCallBack<Object>() {
                            @Override
                            public void onReqSuccess(Object result) {
                                UserBean userBean=  GsonUtils.GsonToBean(result.toString(),UserBean.class);
                                if (userBean.getCode()==200){
                                    BaseAppliaction.getInstance().setUserId(userBean.getData().getUserId());
                                    BaseAppliaction.getInstance().setSessionId(userBean.getData().getSessionId());
                                    Intent mIntent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(mIntent);
                                }else{
                                    ToastUtils.showToast(userBean.getMsg());
                                }
                            }

                            @Override
                            public void onReqFailed(String errorMsg) {
                                LogUtils.e("onReqFailed"+errorMsg.toString());
                            }
                        });
                break;
            case R.id.login_qq_login:
                ToastUtils.showToast("暂未开放");
                break;
            case R.id.login_weixin_login:
                ToastUtils.showToast("暂未开放");
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
    public void visitorLogin(){
        HashMap paramsMap = new HashMap();
        paramsMap.put("platform","ANDROID");
        paramsMap.put("deviceNumber", DeviceUtils.getDeviceId(this));
        RequestManager.getInstance(this).requestAsyn(URLContentUtils.USER_VISITOR_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String strJson = result.toString();
                        visitorUserBean = new Gson().fromJson(strJson,VisitorUserBean.class);
                        if(visitorUserBean.getCode()==200){
                            //保存匿名登录用户信息
                            BaseAppliaction.getInstance().setUserId(visitorUserBean.getData().getUserId());
                            BaseAppliaction.getInstance().setSessionId(visitorUserBean.getData().getSessionId());
                            BaseAppliaction.getInstance().setIslogin(false);
                            SPUtils.put(LoginActivity.this,"sessionId",visitorUserBean.getData().getSessionId());
                            SPUtils.put(LoginActivity.this,"userId",visitorUserBean.getData().getUserId());
                            SPUtils.put(LoginActivity.this,"nickname",visitorUserBean.getData().getNickname());
                            if(userPassword!=null){
                                SPUtils.put(LoginActivity.this,"password",userPassword);
                            }
                            Intent mIntent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(mIntent);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg.toString());
                    }
                });
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
}
