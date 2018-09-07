package com.whzl.mengbi.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.AppDataBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;
import com.whzl.mengbi.model.entity.UpdateInfoBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.fragment.main.FollowFragment;
import com.whzl.mengbi.ui.fragment.main.HomeFragmentNew;
import com.whzl.mengbi.ui.fragment.main.MeFragment;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.AsyncRun;
import com.whzl.mengbi.util.DateUtils;
import com.whzl.mengbi.util.DownloadManagerUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/18
 */
public class MainActivity extends BaseActivity {
    private static final int REQUEST_LOGIN = 250;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    private Fragment[] fragments;
    private int currentSelectedIndex = 0;
    private ProgressDialog progressDialog;
    private boolean isExit;
    private AwesomeDialog awesomeDialog;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setupView() {
        fragments = new Fragment[]{new HomeFragmentNew(), FollowFragment.newInstance(), MeFragment.newInstance()};
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
                    if (checkLogin()) {
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
        rgTab.check(rgTab.getChildAt(0).getId());
    }

    private boolean checkLogin() {
        String sessionId = (String) SPUtils.get(MainActivity.this, SpConfig.KEY_SESSION_ID, "");
        long userId = Long.parseLong(SPUtils.get(MainActivity.this, "userId", (long) 0).toString());
        //long userId =  (long) SPUtils.get(MainActivity.this, SpConfig.KEY_USER_ID, 0);
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
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CHECK_UPDATE, RequestManager.TYPE_POST_JSON, new HashMap<>(), new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                UpdateInfoBean updateInfoBean = GsonUtils.GsonToBean(strJson, UpdateInfoBean.class);
                if (updateInfoBean.code == 200) {
                    showUpgradeDialog(updateInfoBean.data.phone);
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
        if (!checkLogin() && !checkAwardHasShowed()) {
            getNewUserAward(false);
        }
    }

    private boolean checkAwardHasShowed() {
        long lastShowTimeMills = (long) SPUtils.get(this, SpConfig.AWARD_SHOW_TIME, 0L);
        return DateUtils.isToday(lastShowTimeMills);
    }

    private void getNewUserAward(boolean isLogin) {
        ApiFactory.getInstance().getApi(Api.class)
                .getImgUrl(ParamsUtils.getSignPramsMap(new HashMap<>()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<AppDataBean>() {
                    @Override
                    public void onSuccess(AppDataBean appDataBean) {
                        if (appDataBean != null && appDataBean.newUserAward != null) {
                            if (!isLogin && !TextUtils.isEmpty(appDataBean.newUserAward.guestUserAward)) {
                                SPUtils.put(MainActivity.this, SpConfig.AWARD_SHOW_TIME, System.currentTimeMillis());
                                showGiftDialog(appDataBean.newUserAward.guestUserAward, isLogin);
                            }

                            if (isLogin && !TextUtils.isEmpty(appDataBean.newUserAward.loginUserAward)) {
                                showGiftDialog(appDataBean.newUserAward.loginUserAward, isLogin);
                            }
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    private void showGiftDialog(String guestUserAward, boolean isLogin) {
        AwesomeDialog.init().setLayoutId(R.layout.dialog_rookie_gift)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                        ImageView ivGifts = holder.getConvertView().findViewById(R.id.iv_gifts);
                        GlideImageLoader.getInstace().displayImage(dialog.getContext(), guestUserAward, ivGifts);
                        holder.setOnClickListener(R.id.iv_gifts, v -> {
                            if (isLogin) {
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                login();
                            }
                        });
                    }
                })
                .setAnimStyle(-1)
                .show(getSupportFragmentManager());
    }

    private void showUpgradeDialog(UpdateInfoBean.PhoneBean phone) {
        int versionCode = AppUtils.getVersionCode(this);
        if (versionCode >= phone.versionCode) {
            return;
        }
        awesomeDialog = AwesomeDialog.init();
        awesomeDialog.setLayoutId(R.layout.dialog_upgrade)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {
                        holder.setText(R.id.tv_version_name, "V." + phone.versionName);
                        holder.setText(R.id.tv_update_info, phone.upgradeDetail);
                        holder.setOnClickListener(R.id.btn_dismiss, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (NetConfig.FORCE_UPDATE.equals(phone.updateType)) {
                                    finish();
                                }
                            }
                        });
                        holder.setOnClickListener(R.id.btn_upgrade, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.getConvertView().findViewById(R.id.btn_upgrade).setEnabled(false);
                                RxPermissions permissions = new RxPermissions(MainActivity.this);
                                permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        if (aBoolean) {
                                            dialog.dismiss();
                                            downLoad(phone.appUrl);
                                        } else {
                                            showToast(R.string.permission_storage_denid);
                                        }

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        showToast(e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                            }
                        });
                    }
                })
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private void downLoad(String appUrl) {
        DownloadManagerUtil.getInstance().download(this,
                appUrl,
                "/mengbi.apk", new DownloadManagerUtil.DownLoadListener() {
                    @Override
                    public void onProgress(int progress) {
                        AsyncRun.run(() -> {
                            if (progressDialog == null) {
                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog.setCancelable(false);
                                progressDialog.setMax(100);
                            }
                            progressDialog.show();
                            progressDialog.setProgress(progress);
                        });
                    }

                    @Override
                    public void onSucceed(String filePath) {
                        AsyncRun.run(() -> {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            AppUtils.install(filePath, MainActivity.this);
                        });
                    }

                    @Override
                    public void onFailed() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppUtils.REQUEST_INSTALL) {
            finish();
        }
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                getNewUserAward(true);
            }
        }
    }

    public void setCheck(int index) {
        rgTab.check(rgTab.getChildAt(index).getId());
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            super.onBackPressed();
            return;
        }
        Observable.just(1)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        isExit = true;
                        showToast("再按一次退出");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        isExit = false;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        awesomeDialog = null;
    }
}
