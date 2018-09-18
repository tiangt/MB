package com.whzl.mengbi.ui.activity.me;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.fragment.main.FollowFragment;
import com.whzl.mengbi.ui.fragment.main.HomeFragmentNew;
import com.whzl.mengbi.ui.fragment.main.MeFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author niko
 * @date 2018/9/18
 */
public class ShopActivity extends BaseActivity {
    @BindView(R.id.rb_vip_shop)
    RadioButton rbVipShop;
    @BindView(R.id.rv_guard_shop)
    RadioButton rvGuardShop;
    @BindView(R.id.rb_nobility_shop)
    RadioButton rbNobilityShop;
    @BindView(R.id.rb_goodnum_shop)
    RadioButton rbGoodnumShop;
    @BindView(R.id.fl_shop)
    FrameLayout flShop;
    @BindView(R.id.rg_shop)
    RadioGroup rgShop;

    private Fragment[] fragments;
    private int currentSelectedIndex = 0;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_shop, "商城", true);
    }

    @Override
    protected void setupView() {
        fragments = new Fragment[]{FollowFragment.newInstance(), FollowFragment.newInstance(), MeFragment.newInstance(), MeFragment.newInstance()};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_shop, fragments[0]).commit();
        rgShop.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_vip_shop:
                    setTabChange(0);
                    break;
                case R.id.rv_guard_shop:
//                    if (checkLogin()) {
                    setTabChange(1);
//                        return;
//                    }
//                    login();
                    break;
                case R.id.rb_nobility_shop:
//                    if (checkLogin()) {
                    setTabChange(2);
//                        return;
//                    }
//                    login();
                    break;
                case R.id.rb_goodnum_shop:
//                    if (checkLogin()) {
                    setTabChange(3);
//                        return;
//                    }
//                    login();
                    break;
            }
        });
    }

    @Override
    protected void loadData() {

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
            fragmentTransaction.add(R.id.fl_shop, fragments[index]);
        }
        fragmentTransaction.commit();
        currentSelectedIndex = index;
    }

}
