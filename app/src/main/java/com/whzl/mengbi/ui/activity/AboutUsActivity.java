package com.whzl.mengbi.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author shaw
 * @date 2018/7/24
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.rl_yonghu)
    RelativeLayout rlYonghu;
    @BindView(R.id.rl_tiaoli)
    RelativeLayout rlTiaoli;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_about_us, R.string.about_us, true);
    }

    @Override
    protected void setupView() {
        tvVersionName.setText("v " + AppUtils.getVersionName(this));
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.rl_yonghu, R.id.rl_tiaoli})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_yonghu:
                break;
            case R.id.rl_tiaoli:
                break;
        }
    }
}
