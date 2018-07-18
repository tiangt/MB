package com.whzl.mengbi.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseAtivityNew;
import com.whzl.mengbi.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/17
 */
public class LoginActivityNew extends BaseAtivityNew {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_login_new, R.string.login, R.string.register, true);
    }

    @Override
    protected void setupView() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isPhone = StringUtils.isPhone(etPhone.getText().toString().trim());
                String password = etPassword.getText().toString().trim();
                if(isPhone && !TextUtils.isEmpty(password) && password.length() >= 6){
                    btnLogin.setEnabled(true);
                }else {
                    btnLogin.setEnabled(false);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isPhone = StringUtils.isPhone(etPhone.getText().toString().trim());
                String password = etPassword.getText().toString().trim();
                if(isPhone && !TextUtils.isEmpty(password) && password.length() > 6){
                    btnLogin.setEnabled(true);
                }else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.btn_wechat_login, R.id.btn_qq_login, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wechat_login:
                break;
            case R.id.btn_qq_login:
                break;
            case R.id.btn_login:
                break;
        }
    }
}
