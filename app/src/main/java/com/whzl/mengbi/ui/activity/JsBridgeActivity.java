package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.js.LoginStateBean;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.LogUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class JsBridgeActivity extends BaseActivity {

    private Button button;
    private BridgeWebView bridgeWebView;

    private UserInfo.DataBean user;
    private String anchorId;
    private String programId;

    @Override
    protected void initEnv() {
        super.initEnv();
        anchorId = getIntent().getStringExtra("anchorId");
        programId = getIntent().getStringExtra("programId");
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_jsbridge, "幸运", true);
    }

    @Override
    protected void setupView() {
        button = findViewById(R.id.button3);
        bridgeWebView = findViewById(R.id.JsBridgeWebView);

        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.setWebChromeClient(new WebChromeClient());
        bridgeWebView.getSettings().setJavaScriptEnabled(true);
        bridgeWebView.setWebViewClient(new MyWebViewClient(bridgeWebView));
        // 如果不加这一行，当点击界面链接，跳转到外部时，会出现net::ERR_CACHE_MISS错误
        // 需要在androidManifest.xml文件中声明联网权限
        // <uses-permission android:name="android.permission.INTERNET"/>
        if (Build.VERSION.SDK_INT >= 19) {
            bridgeWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        initRegisterHandler();
        bridgeWebView.loadUrl("file:///android_asset/test5.html");


    }

    private void initRegisterHandler() {
        /**
         * 前端发送消息给客户端  submitFromWeb 是js调用的方法名  安卓返回给js
         */
        bridgeWebView.registerHandler("getLoginState", (data, function) -> {
            //显示接收的消息
            LogUtils.e("ssssssssssssss       " + data);
            //返回给html的消息
            LoginStateBean bean = new LoginStateBean("" + checkLogin());
            Gson gson = new Gson();
            function.onCallBack(gson.toJson(bean));
        });

        bridgeWebView.registerHandler("getUserInfo", (data, function) -> getUserInfo(function));

        bridgeWebView.registerHandler("jumpToLoginActivity", (data, function) -> jumpToLoginActivity());

        bridgeWebView.registerHandler("jumpToLiveHouse", (data, function) -> {
            String[] split = data.split(":");
            String replace = split[1].replace("}", "").replaceAll("\"", "");
            LogUtils.e(replace);
            jumpToLiveHouse(Integer.parseInt(replace));
        });

        bridgeWebView.registerHandler("jumpToRechargeActivity", (data, function) -> jumpToRechargeActivity());

        bridgeWebView.registerHandler("getRoomInfo", (data, function) -> function.onCallBack("anchorId   " + anchorId + "   programId  " + programId));

        button.setOnClickListener(v -> {

            /**
             * 给Html发消息,js接收并返回数据
             */
            bridgeWebView.callHandler("getLoginState", "调用js的方法", new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
                    showToast("===" + data);
                }
            });
        });
    }

    public void jumpToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
                            user = dataBean;
                            function.onCallBack("返回给web的alert  userInfo " + user.getNickname());
                        }

                        @Override
                        public void onError(int code) {

                        }
                    });
        }
    }

    private boolean checkLogin() {
        String sessionId = (String) SPUtils.get(this, SpConfig.KEY_SESSION_ID, "");
        long userId = Long.parseLong(SPUtils.get(this, "userId", (long) 0).toString());
        if (userId == 0 || TextUtils.isEmpty(sessionId)) {
            return false;
        }
        return true;
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
}
