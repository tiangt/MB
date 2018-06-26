package com.whzl.mengbi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.activity.base.BaseAtivity;
import com.whzl.mengbi.bean.ResultBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RegexUtils;
import com.whzl.mengbi.util.ToastUtils;
import com.whzl.mengbi.widget.view.GenericToolbar;

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

    /**
     * 获取验证码定时器
     */
    private CountDownTimer cdt;

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
                .addLeftIcon(1, R.mipmap.ic_login_return, new View.OnClickListener() {
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
                RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.SEND_CODE, RequestManager.TYPE_POST_JSON, paramsMapMobile,
                        new RequestManager.ReqCallBack<Object>() {
                            @Override
                            public void onReqSuccess(Object result) {
                                ResultBean resultBean=  GsonUtils.GsonToBean(result.toString(),ResultBean.class);
                                if(resultBean.getCode()!=200){
                                    ToastUtils.showToast(resultBean.getMsg());
                                }
                            }

                            @Override
                            public void onReqFailed(String errorMsg) {
                                LogUtils.d("errorMsg"+errorMsg.toString());
                            }
                        });
                if(!TextUtils.isEmpty(userName)|| RegexUtils.isMobile(userName)){
                    cdt = new CountDownTimer(60000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            regexCodeBut.setText(millisUntilFinished/1000+"秒");
                        }
                        @Override
                        public void onFinish() {
                            regexCodeBut.setEnabled(true);
                            regexCodeBut.setText("获取验证码");
                        }
                    };
                    cdt.start();
                    regexCodeBut.setEnabled(false);
                }
                break;
            //注册登录
            case R.id.register_user:
                    HashMap paramsMap = new HashMap();
                    paramsMap.put("platform","ANDROID");
                    paramsMap.put("password",userPassWord);
                    paramsMap.put("code",valiDataCode);
                    paramsMap.put("username",userName);
                    RequestManager.getInstance(this).requestAsyn(URLContentUtils.REGISTER, RequestManager.TYPE_POST_JSON, paramsMap,
                            new RequestManager.ReqCallBack<Object>() {
                                @Override
                                public void onReqSuccess(Object result) {
                                    ResultBean resultBean=  GsonUtils.GsonToBean(result.toString(),ResultBean.class);
                                    if(resultBean.getCode()==RequestManager.RESULT_CODE){
                                        ToastUtils.showToast(resultBean.getMsg());
                                        Intent mIntent = new Intent(mContext,LoginActivity.class);
                                        mIntent.putExtra("userName",userName);
                                        setResult(1,mIntent);
                                        finish();
                                    }else{
                                        ToastUtils.showToast(resultBean.getMsg());
                                    }
                                }

                                @Override
                                public void onReqFailed(String errorMsg) {

                                }
                            });
                break;
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
