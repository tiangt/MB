package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;

public class SettingsActivity extends BaseAtivity implements View.OnClickListener{

    private Button settingsFeedBack;
    private Button settingsCustomService;
    private Button settingsAbout;
    private Button settingsVersionUpdate;
    private Button settingsExitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);
        initView();
    }

    private void initView(){
        settingsFeedBack = (Button)findViewById(R.id.user_settings_feed_back_but);
        settingsCustomService = (Button)findViewById(R.id.user_settings_custom_service_but);
        settingsAbout = (Button)findViewById(R.id.user_settings_about_but);
        settingsVersionUpdate = (Button)findViewById(R.id.user_settings_version_update_but);
        settingsExitLogin = (Button)findViewById(R.id.user_settings_exit_login_but);

        settingsFeedBack.setOnClickListener(this);
        settingsCustomService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_settings_feed_back_but://意见反馈
                Intent feedbackIntent = new Intent(this,FeedBackActivity.class);
                startActivity(feedbackIntent);
                break;
            case R.id.user_settings_custom_service_but://客服中心
                Intent customServiceIntent = new Intent(this,CustomServiceCenterActivity.class);
                startActivity(customServiceIntent);
                break;
        }
    }
}
