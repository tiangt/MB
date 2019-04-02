package com.whzl.mengbi.ui.activity.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
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

    public void setTitleColor(int color) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(color);
    }

    public void setTitleMenuIcon(int rid, View.OnClickListener clickListener) {
        ImageButton imageButton = findViewById(R.id.iv_toolbar_menu);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setBackgroundResource(rid);
        if (clickListener != null) {
            imageButton.setOnClickListener(clickListener);
        }
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
