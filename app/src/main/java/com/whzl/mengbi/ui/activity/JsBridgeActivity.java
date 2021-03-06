package com.whzl.mengbi.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.ProgramInfoByAnchorBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.js.JumpRoomBean;
import com.whzl.mengbi.model.entity.js.LoginCallbackBean;
import com.whzl.mengbi.model.entity.js.LoginStateBean;
import com.whzl.mengbi.model.entity.js.RoomInfoBean;
import com.whzl.mengbi.model.entity.js.UserInfoBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.ui.activity.me.ChipCompositeActivity;
import com.whzl.mengbi.ui.activity.me.ShopActivity;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class JsBridgeActivity extends BaseActivity {
    private int REQUEST_LOGIN = 120;

    private Button button;
    private BridgeWebView bridgeWebView;

    private UserInfo.DataBean user;
    private String anchorId;
    private String programId;
    private String url;
    private String title;
    private ProgressBar progressbar;

    @Override
    protected void initEnv() {
        anchorId = getIntent().getStringExtra("anchorId");
        programId = getIntent().getStringExtra("programId");
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        LogUtils.e("url    " + url);
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_jsbridge, "", true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void setupView() {
        button = findViewById(R.id.button3);
        bridgeWebView = findViewById(R.id.JsBridgeWebView);
        progressbar = findViewById(R.id.progressbar);

        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.setWebChromeClient(new WebChromeClient());
        bridgeWebView.getSettings().setJavaScriptEnabled(true);
        bridgeWebView.setWebViewClient(new MyWebViewClient(bridgeWebView));
        // 如果不加这一行，当点击界面链接，跳转到外部时，会出现net::ERR_CACHE_MISS错误
        // 需要在androidManifest.xml文件中声明联网权限
        // <uses-permission android:name="android.permission.INTERNET"/>
        if (Build.VERSION.SDK_INT >= 19) {
            bridgeWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        bridgeWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (isFinishing()) {
                    return;
                }
                progressbar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title2) {
                super.onReceivedTitle(view, title2);
                if (!TextUtils.isEmpty(title)) {
                    setTitle(title);
                } else {
                    setTitle(title2);
                }
            }
        });
        bridgeWebView.loadUrl(url);
        initRegisterHandler();


    }

    private void initRegisterHandler() {
        /**
         * 前端发送消息给客户端  submitFromWeb 是js调用的方法名  安卓返回给js
         */
        bridgeWebView.registerHandler("getLoginState", (data, function) -> {
            //显示接收的消息
            LogUtils.e("ssssssssssssss       " + data);
            //返回给html的消息
            LoginStateBean bean = new LoginStateBean(checkLogin());
            Gson gson = new Gson();
            function.onCallBack(gson.toJson(bean));
        });

        bridgeWebView.registerHandler("getUserInfo", (data, function) ->
//                getUserInfo(function));
        {
            Long userId = (Long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
            String sessionId = (String) SPUtils.get(this, SpConfig.KEY_SESSION_ID, "");
            String nickname = SPUtils.get(this, SpConfig.KEY_USER_NAME, "").toString();
            UserInfoBean bean = new UserInfoBean(String.valueOf(userId), sessionId, nickname);
            Gson gson = new Gson();
            function.onCallBack(gson.toJson(bean));
        });

        bridgeWebView.registerHandler("jumpToLoginActivity", (data, function) ->
                jumpToLoginActivity());

        bridgeWebView.registerHandler("jumpToRoomActivity", (data, function) -> {
            JumpRoomBean jumpRoomBean = GsonUtils.GsonToBean(data, JumpRoomBean.class);
            jumpToLiveHouse(Integer.parseInt(jumpRoomBean.pId));
        });

        bridgeWebView.registerHandler("jumpToPayActivity", (data, function) -> jumpToRechargeActivity());

        bridgeWebView.registerHandler("getRoomInfo", (data, function) -> {
            RoomInfoBean bean = new RoomInfoBean(programId, anchorId);
            Gson gson = new Gson();
            function.onCallBack(gson.toJson(bean));
        });

        bridgeWebView.registerHandler("jumpToRandomRoom", (data, function) -> jumpToRandomRoom());
        button.setOnClickListener(v -> {

            /**
             * 给Html发消息,js接收并返回数据
             */
            LoginStateBean bean = new LoginStateBean(checkLogin());
            Gson gson = new Gson();
            bridgeWebView.callHandler("loginedNotice", gson.toJson(bean), data -> {

                    }
//                    showToast("===" + data)
            );
        });

        bridgeWebView.registerHandler("jumpToLiveActivityByAnchorId", (data, function) -> {
            JumpRoomBean jumpRoomBean = GsonUtils.GsonToBean(data, JumpRoomBean.class);
            if (jumpRoomBean == null) {
                return;
            }
            if (programId.equals(jumpRoomBean.pId)) {
                finish();
            } else {
                jumpToLiveHouseByAnchorId(Integer.parseInt(jumpRoomBean.pId));
            }
        });

        bridgeWebView.registerHandler("jumpToShopActivity", (data, function) ->
                jumpToShopActivity());

        bridgeWebView.registerHandler("startQQChat", (data, function) -> {
            JumpRoomBean jumpRoomBean = GsonUtils.GsonToBean(data, JumpRoomBean.class);
            if (jumpRoomBean == null) {
                return;
            }
            startQQChat(jumpRoomBean.pId);
        });

        bridgeWebView.registerHandler("jumpToChipCompositeActivity", (data, function) ->
                jumpToChipCompositeActivity());

    }

    private void jumpToShopActivity() {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

    private void jumpToChipCompositeActivity() {
        Intent intent = new Intent(this, ChipCompositeActivity.class);
        startActivity(intent);
    }


    private void jumpToLiveHouseByAnchorId(int i) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("anchorId", i);
        ApiFactory.getInstance().getApi(Api.class)
                .programInfoByAnchorid(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ProgramInfoByAnchorBean>(this) {

                    @Override
                    public void onSuccess(ProgramInfoByAnchorBean dataBean) {
                        if (dataBean != null && dataBean.list != null && !dataBean.list.isEmpty()) {
                            Intent intent = new Intent(JsBridgeActivity.this, LiveDisplayActivity.class);
                            intent.putExtra("programId", dataBean.list.get(0).programId);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    public void jumpToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("from", this.getClass().toString());
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    public void jumpToLiveHouse(int programId) {
        Intent intent = new Intent(this, LiveDisplayActivity.class);
        intent.putExtra("programId", programId);
        startActivity(intent);
    }

    public void jumpToRechargeActivity() {
        Intent intent = new Intent(this, WXPayEntryActivity.class);
        startActivity(intent);
    }

    public void jumpToRandomRoom() {
        finish();
//        HashMap paramsMap = new HashMap();
//        ApiFactory.getInstance().getApi(Api.class)
//                .random(ParamsUtils.getSignPramsMap(paramsMap))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ApiObserver<JumpRandomRoomBean>() {
//
//                    @Override
//                    public void onSuccess(JumpRandomRoomBean bean) {
//                        Intent intent = new Intent(JsBridgeActivity.this, LiveDisplayActivity.class);
//                        intent.putExtra(BundleConfig.PROGRAM_ID, bean.programId);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onError(int code) {
//
//                    }
//                });
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

    private void getUserInfo(CallBackFunction function) {
        long userId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
        if (userId > 0) {
            HashMap paramsMap = new HashMap();
            paramsMap.put("userId", userId);
            ApiFactory.getInstance().getApi(Api.class)
                    .getUserInfo(ParamsUtils.getSignPramsMap(paramsMap))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<UserInfo.DataBean>(this) {

                        @Override
                        public void onSuccess(UserInfo.DataBean dataBean) {
                            UserInfoBean bean = new UserInfoBean(dataBean.getUserId() + "", dataBean.getSessionId(), dataBean.getNickname());
                            Gson gson = new Gson();
                            function.onCallBack(gson.toJson(bean));
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
        }
    }

    private String checkLogin() {
        String sessionId = (String) SPUtils.get(this, SpConfig.KEY_SESSION_ID, "");
        long userId = Long.parseLong(SPUtils.get(this, "userId", (long) 0).toString());
        if (userId == 0 || TextUtils.isEmpty(sessionId)) {
            return "F";
        }
        return "T";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理返回键，在webview界面，按下返回键，不退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (bridgeWebView != null && bridgeWebView.canGoBack()) {
                bridgeWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (bridgeWebView != null && bridgeWebView.canGoBack()) {
                bridgeWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void loadData() {

    }

    class MyWebViewClient extends BridgeWebViewClient {
        public MyWebViewClient(BridgeWebView context) {
            super(context);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bridgeWebView != null) {
            ViewParent parent = bridgeWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(bridgeWebView);
            }
            bridgeWebView.stopLoading();
            bridgeWebView.getSettings().setJavaScriptEnabled(false);
            bridgeWebView.destroy();
            bridgeWebView = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN) {
            if (RESULT_OK == resultCode) {
                setResult(RESULT_OK);
                LoginCallbackBean bean = new LoginCallbackBean(checkLogin());
                Gson gson = new Gson();
                bridgeWebView.callHandler("loginedNotice", gson.toJson(bean), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
//                        showToast("===" + data);
                    }
                });
            }
        }
    }
}
