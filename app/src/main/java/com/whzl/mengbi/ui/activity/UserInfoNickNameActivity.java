package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;

public class UserInfoNickNameActivity extends BaseAtivity implements View.OnClickListener{

    private EditText nicknameET;
    private ImageView backIV;
    private TextView titleTV;
    private TextView rightTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_nick_name_layout);
        initView();
    }

    private void  initView(){
        String nickname = "";
        if(getIntent() != null){
            nickname = getIntent().getStringExtra("nickname");
        }
        View includeView = findViewById(R.id.user_info_nickname_include);
        backIV = (ImageView) includeView.findViewById(R.id.base_toolbar_back);
        titleTV = (TextView) includeView.findViewById(R.id.base_toolbar_title);
        rightTV = (TextView) includeView.findViewById(R.id.base_toolbar_right);
        nicknameET = (EditText) findViewById(R.id.user_info_nickname_ET);


        titleTV.setText("昵称");
        rightTV.setText("保存");

        nicknameET.setText(nickname);
        backIV.setOnClickListener(this);
        rightTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_toolbar_back:
                finish();
                break;
            case R.id.base_toolbar_right:
                Intent mIntent = new Intent(this, UserInfoActivity.class);
                mIntent.putExtra("nickname",nicknameET.getText().toString().trim());
                setResult(RESULT_OK,mIntent);
                finish();
                break;
        }
    }
}
