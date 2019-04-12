package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.ClickUtil;
import com.whzl.mengbi.util.SPUtils;

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
                if (ClickUtil.isFastClick()) {
                    startActivity(new Intent(this, JsBridgeActivity.class)
                            .putExtra("url", SPUtils.get(this, SpConfig.USERAGREEURL, "").toString())
                            .putExtra("title", "用户协议"));
                }
                break;
            case R.id.rl_tiaoli:
                if (ClickUtil.isFastClick()) {
                    startActivity(new Intent(this, JsBridgeActivity.class)
                            .putExtra("url", SPUtils.get(this, SpConfig.ANCHORAGREEURL, "").toString())
                            .putExtra("title", "规范条例"));
                }
                break;
        }
    }
}
