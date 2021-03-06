package com.whzl.mengbi.ui.activity.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.umeng.socialize.UMShareAPI;
import com.whzl.mengbi.R;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.BaseView;
import com.whzl.mengbi.ui.common.ActivityStackManager;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.util.KeyBoardUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author shaw
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected T mPresenter;

    private static final String TAG_LOADING_DIALOG = "dialogLoading";
    private Unbinder mBinder;
    private BaseAwesomeDialog mLoadingDialog;
    private ProgressDialog dialog;
    private boolean isClickable = true;
    private long lastClickTime;
    protected Toolbar toolbar;
    private TextView tvToolbarMenuText;
    private TextView tvToolbarTitle;

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
//        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#23273e"));
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initEnv();
        setupContentView();
        setStatusBar();
        mBinder = ButterKnife.bind(this);
        setupView();
        loadData();
        ActivityStackManager.getInstance().pushActivity(this);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        setLightStatusBarForM(this, true);
    }

    private void setLightStatusBarForM(Activity activity, boolean dark) {
        Window window = activity.getWindow();
        if (window != null) {
            View decor = window.getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (dark) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(ui);
        }
    }

    protected void initEnv() {
    }

    protected abstract void setupContentView();

    protected abstract void setupView();

    protected abstract void loadData();


    protected void setContentView(int layoutId, int titleRes, int rightMenuRes, boolean isShowBack) {
        super.setContentView(layoutId);
        toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(titleRes);
        tvToolbarMenuText = findViewById(R.id.tv_toolbar_menu_text);
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
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(title);
        tvToolbarMenuText = findViewById(R.id.tv_toolbar_menu_text);
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

    protected void setTitle(String title) {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(title);
        }
    }

    public TextView getTitleRightText() {
        return tvToolbarMenuText;
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
        if (mPresenter != null) {
            mPresenter.detachView();
        }
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
//            KeyBoardUtil.hideInputMethod(this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showLoading(String msg) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(msg);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        dialog.show();
    }

    public void dismissLoading() {
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

    public Activity getBaseActivity() {
        return this;
    }

    /**
     * 绑定生命周期 防止MVP内存泄漏
     *
     * @param <T>
     * @return
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
