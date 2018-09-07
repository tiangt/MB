package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivity;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;
import com.whzl.mengbi.wxapi.WXPayEntryActivity;

import java.util.HashMap;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/8/24
 */
public class CommWebActivity extends BaseActivity {

    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.webView)
    WebView webView;
    private String title;
    private String url;
    private UserInfo.DataBean user;

    @Override
    protected void initEnv() {
        super.initEnv();
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        long userId = (long) SPUtils.get(this, SpConfig.KEY_USER_ID, 0L);
        if (userId > 0) {
            getUserInfo(userId);
        }
    }

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_comm_web, title, true);
    }

    @Override
    protected void setupView() {
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (isFinishing()) {
                    return;
                }
                progressbar.setProgress(i);
                if (i == 100) {
                    progressbar.setVisibility(View.GONE);
                }
            }
        });

        webView.addJavascriptInterface(new JavaScriptInterface(), "nativeBridge");
    }

    public class JavaScriptInterface {

        public JavaScriptInterface() {
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void jumpToLoginActivity() {
            Intent intent = new Intent(CommWebActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public void jumpToLiveHouse(int programId) {
            Intent intent = new Intent(CommWebActivity.this, LiveDisplayActivity.class);
            intent.putExtra("programId", programId);
            startActivity(intent);
        }

        @JavascriptInterface
        public void jumpToRechargeActivity() {
            Intent intent = new Intent(CommWebActivity.this, WXPayEntryActivity.class);
            startActivity(intent);
        }

        @JavascriptInterface
        public String getUserInfo() {
            if (user == null) {
                return null;
            }
            Gson gson = new Gson();
            return gson.toJson(user);
        }
    }

    @Override
    protected void loadData() {
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    private void getUserInfo(long userId) {
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
                        Gson gson = new Gson();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

}
