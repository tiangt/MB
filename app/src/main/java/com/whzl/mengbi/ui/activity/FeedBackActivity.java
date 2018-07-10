package com.whzl.mengbi.ui.activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;

public class FeedBackActivity extends BaseAtivity{

    private EditText feedBackContent;
    private EditText feedBackQQ;
    private EditText feedBackEmail;
    private Button feedBackSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_layout);
    }

    private void initView(){
        feedBackContent = (EditText)findViewById(R.id.user_settings_feed_back_content_ET);
        feedBackQQ = (EditText)findViewById(R.id.user_settings_feed_back_qq_ET);
        feedBackEmail = (EditText)findViewById(R.id.user_settings_feed_back_email_ET);
        feedBackSubmit = (Button)findViewById(R.id.user_settings_feed_back_submit_but);
    }
}
