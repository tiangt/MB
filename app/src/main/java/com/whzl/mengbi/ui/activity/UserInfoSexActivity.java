package com.whzl.mengbi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.whzl.mengbi.R;

public class UserInfoSexActivity extends AppCompatActivity {

    private RadioGroup sexRadio;
    private RadioButton sexRadioM;
    private RadioButton sexRadioW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_sex_layout);
    }

   private void  initView(){
       String sex="";
        if(getIntent() != null){
            sex = getIntent().getStringExtra("sex");
        }

        sexRadio = (RadioGroup) findViewById(R.id.user_info_sex_rg);
        sexRadioM = findViewById(R.id.user_info_sex_m_rb);
        sexRadioW = findViewById(R.id.user_info_sex_w_rb);
        if (sex.equals("M")){
            sexRadioM.setChecked(true);
        }else{
            sexRadioW.setChecked(true);
        }
   }

}
