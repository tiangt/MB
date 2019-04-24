package com.whzl.mengbi.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.gen.UsualGiftDao;
import com.whzl.mengbi.greendao.UsualGift;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.UpdateInfoBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.activity.me.BindingPhoneActivity;
import com.whzl.mengbi.ui.activity.me.ChangePhoneActivity;
import com.whzl.mengbi.ui.activity.me.MsgPushActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.dialog.base.AwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.BaseAwesomeDialog;
import com.whzl.mengbi.ui.dialog.base.ViewConvertListener;
import com.whzl.mengbi.ui.dialog.base.ViewHolder;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.AsyncRun;
import com.whzl.mengbi.util.DownloadManagerUtil;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author shaw
 * @date 2018/7/24
 */
public class SettingActivity extends BaseActivity {

    private static final int REQUEST_BINDING = 101;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;
    @BindView(R.id.tv_bind_phone)
    TextView tvBindPhone;
    private ProgressDialog progressDialog;
    private AwesomeDialog awesomeDialog;
    private String mMobile;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_setting_new, R.string.setting, true);
    }

    @Override
    protected void setupView() {
        tvVersionName.setText("V " + AppUtils.getVersionName(this));
        String from = getIntent().getStringExtra("from");
        btnLoginOut.setVisibility("live".equals(from) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void loadData() {
        getUserInfo();
    }


    @OnClick({R.id.rl_version_container, R.id.btn_login_out, R.id.tv_feedback,
            R.id.tv_custom, R.id.about_us, R.id.rl_binding_phone, R.id.tv_push_setting,R.id.tv_rate_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_version_container:
                RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CHECK_UPDATE, RequestManager.TYPE_POST_JSON, new HashMap<>(), new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String strJson = result.toString();
                        UpdateInfoBean updateInfoBean = GsonUtils.GsonToBean(strJson, UpdateInfoBean.class);
                        if (updateInfoBean.code == 200) {
                            showDudateDialog(updateInfoBean.data.phone);
                        }

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d(errorMsg);
                    }
                });
                break;
            case R.id.btn_login_out:
                showConfirmDialog();
                break;
            case R.id.tv_feedback:
                jumpToFeedbackActivity();
                break;
            case R.id.tv_custom:
                Intent customServiceIntent = new Intent(this, CustomServiceCenterActivity.class);
                startActivity(customServiceIntent);
                break;
            case R.id.about_us:
                Intent aboutIntent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.rl_binding_phone:
                if (TextUtils.isEmpty(mMobile)) {
                    jumpToBindingPhoneActivity();
                } else {
                    jumpToChangePhoneActivity();
                }
                break;
            case R.id.tv_push_setting:
                startActivity(new Intent(this, MsgPushActivity.class));
                break;
            case R.id.tv_rate_setting:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    private void jumpToBindingPhoneActivity() {
        Intent intent = new Intent(this, BindingPhoneActivity.class);
        startActivityForResult(intent, REQUEST_BINDING);
    }

    private void jumpToChangePhoneActivity() {
        Intent intent = new Intent(this, ChangePhoneActivity.class);
        intent.putExtra("bindMobile", mMobile);
        startActivity(intent);
    }

    private void jumpToFeedbackActivity() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    private void showConfirmDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否确定退出登录");
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", (dialog1, which) -> logOut());
        dialog.show();
    }

    private void showDudateDialog(UpdateInfoBean.PhoneBean phone) {
        int versionCode = AppUtils.getVersionCode(this);
        if (versionCode >= phone.versionCode) {
            showToast("当前已是最新版本");
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
                                RxPermissions permissions = new RxPermissions(SettingActivity.this);
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

    private void logOut() {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.LOGOUT, RequestManager.TYPE_POST_JSON, new HashMap<>(), new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                ResponseInfo responseInfo = GsonUtils.GsonToBean(strJson, ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    showToast("已退出登录");
                    setResult(RESULT_OK);
                    finish();
                    delete();
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }

    private void delete() {
        UserDao userDao = BaseApplication.getInstance().getDaoSession().getUserDao();
        userDao.deleteByKey(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString()));
        UsualGiftDao usualGiftDao = BaseApplication.getInstance().getDaoSession().getUsualGiftDao();
        List<UsualGift> list = usualGiftDao.queryBuilder().where(UsualGiftDao.Properties.UserId.
                eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString()))).list();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                usualGiftDao.deleteByKey(list.get(i).getId());
            }
        }
        loadData();
    }

    private void downLoad(String appUrl) {
        DownloadManagerUtil.getInstance().download(this,
                appUrl, "/mengbi.apk", new DownloadManagerUtil.DownLoadListener() {
                    @Override
                    public void onProgress(int progress) {
                        AsyncRun.run(() -> {
                            if (progressDialog == null) {
                                progressDialog = new ProgressDialog(SettingActivity.this);
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
                            AppUtils.install(filePath, SettingActivity.this);
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
        if (requestCode == REQUEST_BINDING) {
            if (resultCode == RESULT_OK) {
                getUserInfo();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        awesomeDialog = null;
    }

    private void getUserInfo() {
        HashMap paramsMap = new HashMap<>();
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        paramsMap.put("userId", userId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                UserInfo userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo.class);
                if (userInfo.getCode() == 200) {
                    mMobile = userInfo.getData().getBindMobile();
                    if (!TextUtils.isEmpty(mMobile)) {
                        String maskNum = mMobile.substring(0, 3) + "xxxx" + mMobile.substring(7, mMobile.length());
                        tvBindPhone.setText("已绑定 " + maskNum);
                    } else {
                        if (tvBindPhone != null) {
                            tvBindPhone.setText("未绑定");
                        }
                    }
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                Toast.makeText(SettingActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
