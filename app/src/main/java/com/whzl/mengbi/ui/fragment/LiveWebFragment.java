package com.whzl.mengbi.ui.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;

import butterknife.BindView;

/**
 * @author nobody
 * @date 2018/10/25
 */
public class LiveWebFragment extends BaseFragment {

    @BindView(R.id.JsBridgeWebView)
    BridgeWebView bridgeWebView;
    @BindView(R.id.view)
    View view;
    private String url;
    private ClickListener listener;

    public static LiveWebFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        LiveWebFragment liveWebFragment = new LiveWebFragment();
        liveWebFragment.setArguments(args);
        return liveWebFragment;
    }


    @Override
    protected void initEnv() {
        super.initEnv();
        Bundle args = getArguments();
        url = args.getString("url");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_liveweb;
    }


    public interface ClickListener {
        void clickListener();
    }

    public void setOnclickListener(ClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void init() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickListener();
            }
        });
        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.setWebChromeClient(new WebChromeClient());
        bridgeWebView.getSettings().setJavaScriptEnabled(true);
        bridgeWebView.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        bridgeWebView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        bridgeWebView.setWebViewClient(new MyWebViewClient(bridgeWebView));
        // 如果不加这一行，当点击界面链接，跳转到外部时，会出现net::ERR_CACHE_MISS错误
        // 需要在androidManifest.xml文件中声明联网权限
        // <uses-permission android:name="android.permission.INTERNET"/>
        if (Build.VERSION.SDK_INT >= 19) {
            bridgeWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

//        bridgeWebView.loadUrl("file:///android_asset/test5.html");
        bridgeWebView.loadUrl(url);
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
    public void onDestroy() {
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
