package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author shaw
 */
public class CustomServiceCenterActivity extends BaseActivityNew {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_custom_service_center_layout, R.string.custom, true);
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.user_custom_official_service_qq_text, R.id.user_custom_guild_entered_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_custom_official_service_qq_text:
                startQQChat("3179747001");
                break;
            case R.id.user_custom_guild_entered_qq:
                startQQChat("1530675255");
                break;
        }
    }

    private void startQQChat(String qq) {
        if (!isQQClientAvailable()) {
            showToast("QQ未安装");
            return;
        }
        final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
    }

    public boolean isQQClientAvailable() {
        final PackageManager packageManager = getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
