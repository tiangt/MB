package com.whzl.mengbi.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author shaw
 * @date 2018/7/24
 */
public class AboutUsActivity extends BaseActivityNew {
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_about_us, R.string.about_us, true);
    }

    @Override
    protected void setupView() {
        tvVersionName.setText("版本 v" + AppUtils.getVersionName(this));
        tvHome.setText("官方网站：");
        SpannableString spannableString = StringUtils.spannableStringColor("www.mengbitv.com", Color.parseColor("#07808d"));
        tvHome.append(spannableString);
    }

    @Override
    protected void loadData() {

    }

}
