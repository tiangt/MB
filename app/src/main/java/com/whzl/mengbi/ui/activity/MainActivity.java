package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.ui.fragment.main.FollowFragment;
import com.whzl.mengbi.ui.fragment.main.HomeFragment;
import com.whzl.mengbi.ui.fragment.main.HomeFragmentNew;
import com.whzl.mengbi.ui.fragment.main.MeFragment;
import com.whzl.mengbi.util.SPUtils;

import butterknife.BindView;

/**
 * @author shaw
 * @date 2018/7/18
 */
public class MainActivity extends BaseActivityNew {
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    private Fragment[] fragments;
    private int currentSelectedIndex = 0;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setupView() {
        fragments = new Fragment[]{new HomeFragmentNew(), FollowFragment.newInstance("关注"), MeFragment.newInstance("我的")};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragments[0]).commit();
        rgTab.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_home:
                    setTabChange(0);
                    break;
                case R.id.rb_follow:
                    if (checkLogin()) {
                        setTabChange(1);
                        return;
                    }
                    login();
                    break;
                case R.id.rb_me:
                    if(checkLogin()){
                        setTabChange(2);
                        return;
                    }
                    login();
                    break;
            }
        });
    }

    private void login() {
        currentSelectedIndex = 0;
        Intent intent = new Intent(MainActivity.this, LoginActivityNew.class);
        startActivity(intent);
        rgTab.check(rgTab.getChildAt(0).getId());
    }

    private boolean checkLogin() {
        String sessionId = (String) SPUtils.get(MainActivity.this, SpConfig.KEY_SESSION_ID, "");
        int userId = (int) SPUtils.get(MainActivity.this, SpConfig.KEY_USER_ID, 0);
        if (userId == 0 || TextUtils.isEmpty(sessionId)) {
            return false;
        }
        return true;
    }

    private void setTabChange(int index) {
        if (index == currentSelectedIndex) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments[currentSelectedIndex]);
        if (fragments[index].isAdded()) {
            fragmentTransaction.show(fragments[index]);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragments[index]);
        }
        fragmentTransaction.commit();
        currentSelectedIndex = index;
    }

    @Override
    protected void loadData() {

    }

}
