package com.whzl.mengbi.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.application.BaseAppliaction;
import com.whzl.mengbi.bean.ResultBean;
import com.whzl.mengbi.bean.VisitorUserBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.EncryptUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RegexUtil;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;

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
        Toolbar mLoginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mLoginToolbar.setTitle("");
        mLoginToolbar.setNavigationIcon(R.mipmap.ic_login_return);
        setSupportActionBar(mLoginToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(BaseAppliaction.getInstance().getUserId()==0){
            //visitorLogin();
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

    @Override
    public void onClick(View v) {
        userName = longinUserName.getText().toString().trim();
        userPassword = longinUserPassword.getText().toString().trim();
        switch (v.getId()){
            case R.id.login_user_login:
                //用户登录
                HashMap paramsMap = new HashMap();
                paramsMap.put("username",userName);
                String pass= EncryptUtil.md5Hex(userPassword);
                paramsMap.put("password",pass);
                paramsMap.put("platform","ANDROID");
                RequestManager.getInstance(this).requestAsyn(URLContentUtils.USER_NORMAL_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                        new RequestManager.ReqCallBack<Object>() {
                            @Override
                            public void onReqSuccess(Object result) {
                                ResultBean resultBean=  new Gson().fromJson(result.toString(),ResultBean.class);
                                if (resultBean.getCode()==200){
                                    Intent mIntent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(mIntent);
                                }else{
                                    ToastUtils.showToast(resultBean.getMsg());
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
                            Intent mIntent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(mIntent);
                            saveUserInfo(visitorUserBean);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg.toString());
                    }
                });
    }

    /**
     * 保存用户名
     * @param visitorUser
     */
    private void saveUserInfo(VisitorUserBean visitorUser){
        SPUtils.put(this,"username",visitorUser.getData().getNickname()+"");
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
