package com.whzl.mengbi.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.FollowRefreshEvent;
import com.whzl.mengbi.eventbus.event.JumpMainActivityEvent;
import com.whzl.mengbi.model.entity.AppDataBean;
import com.whzl.mengbi.model.entity.UpdateInfoBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.BindingPhoneDialog;
import com.whzl.mengbi.ui.dialog.LoginDialog;
import com.whzl.mengbi.ui.dialog.QuitAppDialog;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.ui.fragment.RankFragment;
import com.whzl.mengbi.ui.fragment.main.FollowFragment;
import com.whzl.mengbi.ui.fragment.main.HomeFragment;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/18
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    private Fragment[] fragments;
    private int currentSelectedIndex = 0;
    private ProgressDialog progressDialog;
    public boolean isExit;
    private AwesomeDialog awesomeDialog;
    private static final String TAG_EXIT = "exit";
    private int[] colors = new int[]{Color.parseColor("#f9f9f9"), Color.parseColor("#f9f9f9"),
            Color.parseColor("#f9f9f9"), Color.parseColor("#21D790")};

    @Override
    protected void initEnv() {
        super.initEnv();
        EventBus.getDefault().register(this);
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, colors[0], 0);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setupView() {
        fragments = new Fragment[]{new HomeFragment(), RankFragment.newInstance(), FollowFragment.newInstance(), MeFragment.newInstance()};
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragments[0]).commit();
        rgTab.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_home:
                    setTabChange(0);
                    break;
                case R.id.rb_rank:
//                    if (checkLogin()) {
                    setTabChange(1);
//                        return;
//                    }
//                    login();
                    break;
                case R.id.rb_follow:
                    if (checkLogin()) {
                        setTabChange(2);
                        EventBus.getDefault().post(new FollowRefreshEvent());
                        return;
                    }
                    login();
                    break;
                case R.id.rb_me:
                    if (checkLogin()) {
                        setTabChange(3);
                        return;
                    }
                    login();
                    break;
            }
        });
    }

    private void login() {
//        currentSelectedIndex = 0;
//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivityForResult(intent, REQUEST_LOGIN);
//        LoginDialog.newInstance()
//                .setLoginSuccessListener(() -> getNewUserAward(true))
//                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
//                .setDimAmount(0.7f)
//                .show(getSupportFragmentManager());

        LoginDialog.newInstance()
                .setLoginSuccessListener(new LoginDialog.LoginSuccessListener() {
                    @Override
                    public void onLoginSuccessListener() {
                        getNewUserAward(true);
                        isFirstLogin();
                    }
                })
                .setAnimStyle(R.style.Theme_AppCompat_Dialog)
                .setDimAmount(0.7f)
                .show(getSupportFragmentManager());
        rgTab.check(rgTab.getChildAt(currentSelectedIndex).getId());
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
        StatusBarUtil.setColor(this, colors[index], 0);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments[currentSelectedIndex]);
        if (fragments[index].isAdded()) {
            fragmentTransaction.show(fragments[index]);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragments[index]);
        }
        fragmentTransaction.commitAllowingStateLoss();
        currentSelectedIndex = index;
    }

    @Override
    protected void loadData() {
        if (!checkLogin() && !checkAwardHasShowed()) {
            getNewUserAward(false);
        } else {
            getUpdate();
        }
    }

    private void getUpdate() {
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
                        SPUtils.put(MainActivity.this, SpConfig.REDPACKETURL, appDataBean.redpacketUrl);
                        if (appDataBean != null && appDataBean.newUserAward != null) {
                            if (!isLogin && !TextUtils.isEmpty(appDataBean.newUserAward.guestUserAward)) {
                                SPUtils.put(MainActivity.this, SpConfig.AWARD_SHOW_TIME, System.currentTimeMillis());
                                showGiftDialog(appDataBean.newUserAward.guestUserAward, isLogin);
                            }

                            if (isLogin && !TextUtils.isEmpty(appDataBean.newUserAward.loginUserAward)) {
                                showGiftDialog(appDataBean.newUserAward.loginUserAward, isLogin);
                            }
                        }
                        getUpdate();
                    }

                    @Override
                    public void onError(int code) {
                        getUpdate();
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
                        holder.setText(R.id.tv_version_name, "发现新版本(" + phone.versionName + ")");
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
        if (requestCode == AppUtils.REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                getNewUserAward(true);
                isFirstLogin();
            }
        }
    }

    public void setCheck(int index) {
        rgTab.check(rgTab.getChildAt(index).getId());
    }

    private void showBindingDialog() {
        BindingPhoneDialog.newInstance()
                .setAnimStyle(R.style.dialog_enter_from_right_out_from_right)
                .show(getSupportFragmentManager());
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            super.onBackPressed();
            return;
        }
//        Observable.just(1)
//                .delay(2, TimeUnit.SECONDS)
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        isExit = true;
//                        showToast("再按一次退出");
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        isExit = false;
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        QuitAppDialog.newInstance()
                .setListener(new QuitAppDialog.OnClickListener() {
                    @Override
                    public void onQuitAppClick() {
                        exit();
                    }
                })
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        awesomeDialog = null;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(JumpMainActivityEvent event) {
        setTabChange(event.check);
        setCheck(event.check);
    }

    /**
     * 退出应用
     */
    private void exit() {
//        super.finish();
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.TAG_EXIT, true);
        startActivity(intent);
        System.exit(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    /**
     * 判断用户是否首次登陆
     */
    private void isFirstLogin() {
        HashMap paramsMap = new HashMap();
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        paramsMap.put("userId", userId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            String createTime = userInfo.getData().getCreateTime();
                            String lastLoginTime = userInfo.getData().getLastLoginTime();
                            String mobile = userInfo.getData().getBindMobile();
                            if (createTime.equals(lastLoginTime) && TextUtils.isEmpty(mobile)) {
                                showBindingDialog();
                            }
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg);
                    }
                });
    }
}
