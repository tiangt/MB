package com.whzl.mengbi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.whzl.mengbi.R;
import com.whzl.mengbi.presenter.RegisterPresenter;
import com.whzl.mengbi.presenter.impl.RegisterPresenterImpl;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;
import com.whzl.mengbi.model.entity.RegisterInfo;
import com.whzl.mengbi.ui.view.RegisterView;
import com.whzl.mengbi.ui.widget.view.GenericToolbar;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.ToastUtils;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseAtivity implements RegisterView, View.OnClickListener{

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
    private RegisterInfo registerInfo;

    /**
     * 获取验证码定时器
     */
    private CountDownTimer cdt;

    private String userName;
    private String valiDataCode;
    private String userPassWord;

    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layoyut);
        mContext = this;
        initView();
    }


    private void initView() {
        regexUserNameEt = (EditText) findViewById(R.id.register_user_name);
        regexCodeEt = (EditText) findViewById(R.id.register_user_email);
        regexCodeBut = (Button) findViewById(R.id.register_identifying_code);
        regexPasswordEt = (EditText) findViewById(R.id.register_user_password);
        regexUserCb = (CheckBox) findViewById(R.id.register_user_checked);
        regexUserBut = (Button) findViewById(R.id.register_user);

        registerPresenter = new RegisterPresenterImpl(this);

        //复选框
        if(regexUserCb.isChecked()){
            regexUserBut.setEnabled(true);
        }else{
            regexUserBut.setEnabled(false);
        }

        //监听事件
        regexCodeBut.setOnClickListener(this);
        regexUserBut.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        userName = regexUserNameEt.getText().toString().trim();
        valiDataCode = regexCodeEt.getText().toString().trim();
        userPassWord = regexPasswordEt.getText().toString().trim();
        switch (v.getId()){
            //获取验证码
            case R.id.register_identifying_code:
                registerPresenter.getRegexCode(userName);
                break;
            //注册登录
            case R.id.register_user:
                registerPresenter.getRegister(userName,userPassWord,valiDataCode);
                break;
        }
    }

    @Override
    public void showRegexCodeMsg(String code,String msg) {
        if(code.equals("200")){
            startTimer();
        }else{
            ToastUtils.showToast(msg);
        }
    }

    @Override
    public void navigateToAll(RegisterInfo registerInfo) {
            Intent mIntent = new Intent(mContext,LoginActivity.class);
            mIntent.putExtra("userName",userName);
            setResult(1,mIntent);
            finish();
    }

    @Override
    public void onError(String msg) {
        ToastUtils.showToast(msg);
    }

    /**
     *获取验证码时间定时器
     */
    public void startTimer(){
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
                mIntent.putExtra("visitor",true);
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
        registerPresenter.onDestroy();
    }
}
