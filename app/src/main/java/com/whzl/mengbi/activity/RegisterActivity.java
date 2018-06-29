package com.whzl.mengbi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.bean.RegisterBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.thread.RegisterRegexCodeThread;
import com.whzl.mengbi.thread.RegisterThread;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RegexUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.widget.view.GenericToolbar;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseAtivity implements View.OnClickListener{

    private Context mContext;
    /**
     * 用户名
     */
    private EditText regexUserNameEt;
    /**
     * 验证码
     */
    private EditText regexCodeEt;
    /**
     * 获取验证码
     */
    private Button regexCodeBut;
    /**
     * 用户密码
     */
    private EditText regexPasswordEt;
    /**
     * 服务协议
     */
    private CheckBox regexUserCb;
    /**
     * 注册
     */
    private Button regexUserBut;
    private RegisterBean registerBean;
    private RegisterThread registerThread;
    private RegisterHandler registerHandler = new RegisterHandler(this);

    /**
     * 获取验证码定时器
     */
    private CountDownTimer cdt;
    private RegisterRegexCodeThread regexCodeThread;
    private RegisterRegexCodeHandler regexCodeHandler = new RegisterRegexCodeHandler(this);

    private String userName;
    private String valiDataCode;
    private String userPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layoyut);
        mContext = this;
        initView();
        initToolBar();
    }


    private void initView() {
        regexUserNameEt = (EditText) findViewById(R.id.register_user_name);
        regexCodeEt = (EditText) findViewById(R.id.register_user_email);
        regexCodeBut = (Button) findViewById(R.id.register_identifying_code);
        regexPasswordEt = (EditText) findViewById(R.id.register_user_password);
        regexUserCb = (CheckBox) findViewById(R.id.register_user_checked);
        regexUserBut = (Button) findViewById(R.id.register_user);
        //
        if(regexUserCb.isChecked()){
            regexUserBut.setEnabled(true);
        }else{
            regexUserBut.setEnabled(false);
        }
        //监听事件
        regexCodeBut.setOnClickListener(this);
        regexUserBut.setOnClickListener(this);
    }

    public void  initToolBar(){
        new GenericToolbar.Builder(this)
                .addTitleText("注册",22f)// 标题文本
                .setBackgroundColorResource(R.color.colorPrimary)// 背景颜色
                .addLeftIcon(1, R.drawable.ic_login_return, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })// 响应左部图标的点击事件
                .addRightText(3, "登录", 22f, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(mIntent);
                    }
                })// 响应右部文本的点击事件
                .setTextColor(getResources().getColor(R.color.colorPrimaryDark))// 会全局设置字体的颜色, 自定义标题除外
                .apply();
    }

    @Override
    public void onClick(View v) {
        userName = regexUserNameEt.getText().toString().trim();
        valiDataCode = regexCodeEt.getText().toString().trim();
        userPassWord = regexPasswordEt.getText().toString().trim();
        switch (v.getId()){
            //获取验证码
            case R.id.register_identifying_code:
                HashMap paramsMapMobile = new HashMap();
                paramsMapMobile.put("mobile",userName);
                regexCodeThread = new RegisterRegexCodeThread(this,paramsMapMobile,regexCodeHandler);
                regexCodeThread.start();
                break;
            //注册登录
            case R.id.register_user:
                    HashMap paramsMap = new HashMap();
                    paramsMap.put("platform","ANDROID");
                    paramsMap.put("password",userPassWord);
                    paramsMap.put("code",valiDataCode);
                    paramsMap.put("username",userName);
                    registerThread = new RegisterThread(this,paramsMap,registerHandler);
                    registerThread.start();
                break;
        }
    }

    /**
     * 获取短信验证码
     */
    private static class RegisterRegexCodeHandler extends Handler{

        private final WeakReference<Activity> activityWeakReference;

        RegisterRegexCodeHandler(Activity activity){
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterActivity registerActivity = (RegisterActivity) activityWeakReference.get();
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(!TextUtils.isEmpty(registerActivity.userName)|| RegexUtils.isMobile(registerActivity.userName)){
                        registerActivity.cdt = new CountDownTimer(60000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                registerActivity.regexCodeBut.setText(millisUntilFinished/1000+"秒");
                            }
                            @Override
                            public void onFinish() {
                                registerActivity.regexCodeBut.setEnabled(true);
                                registerActivity.regexCodeBut.setText("获取验证码");
                            }
                        };
                        registerActivity.cdt.start();
                        registerActivity.regexCodeBut.setEnabled(false);
                    }
                    break;
            }
        }
    }

    /**
     * 注册
     */
    private static class RegisterHandler extends Handler{

        private final WeakReference<Activity> activityWeakReference;

        RegisterHandler(Activity activity){
            activityWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RegisterActivity registerActivity = (RegisterActivity)activityWeakReference.get();
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(registerActivity.registerBean.getCode()==RequestManager.RESPONSE_CODE){
                        ToastUtils.showToast(registerActivity.registerBean.getMsg());
                        Intent mIntent = new Intent(registerActivity.mContext,LoginActivity.class);
                        mIntent.putExtra("userName",registerActivity.userName);
                        registerActivity.setResult(1,mIntent);
                        registerActivity.finish();
                    }else{
                        ToastUtils.showToast(registerActivity.registerBean.getMsg());
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.item_user_login:
                Intent mIntent = new Intent(this,LoginActivity.class);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cdt!=null){
            cdt.cancel();
            cdt=null;
        }
    }
}
