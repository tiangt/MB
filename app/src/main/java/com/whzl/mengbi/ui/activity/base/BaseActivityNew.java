package com.whzl.mengbi.ui.activity.base;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.EventBusBean;
import com.whzl.mengbi.eventbus.EventBusUtils;
import com.whzl.mengbi.ui.common.ActivityStackManager;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.util.KeyBoardUtil;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author shaw
 */
public abstract class BaseActivityNew extends AppCompatActivity {

    private static final String TAG_LOADING_DIALOG = "dialogLoading";
    private Unbinder mBinder;
    private BaseAwesomeDialog mLoadingDialog;
    private ProgressDialog dialog;

    /**
     * 输出日志
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
        initEnv();
        setupContentView();
        mBinder = ButterKnife.bind(this);
        setupView();
        loadData();
        ActivityStackManager.getInstance().pushActivity(this);
    }

    protected void initEnv() {
    }

    protected abstract void setupContentView();

    protected abstract void setupView();

    protected abstract void loadData();


    protected void setContentView(int layoutId, int titleRes, int rightMenuRes, boolean isShowBack) {
        super.setContentView(layoutId);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(titleRes);
        TextView tvToolbarMenuText = findViewById(R.id.tv_toolbar_menu_text);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(isShowBack);
            supportActionBar.setDisplayHomeAsUpEnabled(isShowBack);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        if (tvToolbarMenuText != null) {
            tvToolbarMenuText.setText(rightMenuRes);
            tvToolbarMenuText.setOnClickListener(v -> onToolbarMenuClick());
        }
    }

    protected void setContentView(int layoutId, String title, String rightMenu, boolean isShowBack) {
        super.setContentView(layoutId);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(title);
        TextView tvToolbarMenuText = findViewById(R.id.tv_toolbar_menu_text);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(isShowBack);
            supportActionBar.setDisplayHomeAsUpEnabled(isShowBack);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        if (tvToolbarMenuText != null) {
            tvToolbarMenuText.setText(rightMenu);
            tvToolbarMenuText.setOnClickListener(v -> onToolbarMenuClick());
        }
    }

    protected void setContentView(int layoutId, int titleRes, boolean isShowBack) {
        setContentView(layoutId, titleRes, R.string.nothing, isShowBack);
    }

    protected void setContentView(int layoutId, String title, boolean isShowBack) {
        setContentView(layoutId, title, "", isShowBack);
    }

    protected void onToolbarMenuClick() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
        ActivityStackManager.getInstance().popActivity(this);
    }

    public boolean isActivityFinished() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isFinishing() || isDestroyed();
        } else {
            return isFinishing();
        }
    }

    protected void showToast(String msg) {
        if (isActivityFinished()) {
            return;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int msgRes) {
        if (isActivityFinished()) {
            return;
        }
        Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            KeyBoardUtil.hideInputMethod(this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showLoading(String msg) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(msg);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    protected void dismissLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void showLoading() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("加载中，请稍后...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        dialog.show();
    }

}
