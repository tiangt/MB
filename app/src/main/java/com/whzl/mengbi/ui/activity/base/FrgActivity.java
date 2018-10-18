package com.whzl.mengbi.ui.activity.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.whzl.mengbi.R;


public class FrgActivity extends BaseActivity {

    public static final String FRAGMENT_CLASS = "fragment";


    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_fg, "", true);
    }

    public void setTitle(String title) {
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(title);
    }

    @Override
    protected void setupView() {
        if (getIntent().getSerializableExtra(FRAGMENT_CLASS) == null
                || !(getIntent().getSerializableExtra(FRAGMENT_CLASS) instanceof Class)) {
            return;
        }
        Class fragmentClass = (Class) getIntent().getSerializableExtra(FRAGMENT_CLASS);
        intentTo(fragmentClass);
    }

    @Override
    protected void loadData() {

    }

    private void intentTo(Class fragmentClass) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment == null) {
            return;
        }
        transaction.replace(R.id.fl_contain, fragment, fragmentClass.getName());
        transaction.commit();
    }

}
