package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.widget.RadioGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

import butterknife.BindView;

/**
 * @author shaw
 */
public class GenderModifyActivity extends BaseActivity {


    @BindView(R.id.rg_gender)
    RadioGroup rbGender;
    private String gender;

    @Override
    protected void initEnv() {
        super.initEnv();
        gender = getIntent().getStringExtra("gender");
    }

    @Override
    protected void onToolbarMenuClick() {
        super.onToolbarMenuClick();
        int checkedRadioButtonId = rbGender.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            showToast("请先选择性别");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("gender", checkedRadioButtonId == R.id.rb_man ? "M" : "W");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_user_info_sex_layout, R.string.gender, R.string.save, true);
    }

    @Override
    protected void setupView() {
        if ("M".equals(gender)) {
            rbGender.check(R.id.rb_man);
        } else if ("W".equals(gender)) {
            rbGender.check(R.id.rb_woman);
        } else {
            rbGender.check(R.id.rb_man);
        }
    }

    @Override
    protected void loadData() {

    }

}
