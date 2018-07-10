package com.whzl.mengbi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseAtivity;

public class CustomServiceCenterActivity extends BaseAtivity {


    private TextView officialServiceQQ;
    private TextView guildeEnteredQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service_center_layout);
    }
}
