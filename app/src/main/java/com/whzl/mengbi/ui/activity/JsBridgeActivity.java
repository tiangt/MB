package com.whzl.mengbi.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.activity.base.BaseActivity;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class JsBridgeActivity extends BaseActivity {

    private Button button;
    private BridgeWebView bridgeWebView;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_jsbridge, "幸运", true);
    }

    @Override
    protected void setupView() {
        button = (Button) findViewById(R.id.button3);
        bridgeWebView = (BridgeWebView) findViewById(R.id.JsBridgeWebView);

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

        bridgeWebView.loadUrl("file:///android_asset/test.html");

        /**
         * 前端发送消息给客户端  submitFromWeb 是js调用的方法名  安卓返回给js
         */
        bridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //显示接收的消息
                showToast(data);
                //返回给html的消息
                function.onCallBack("返回给Toast的alert");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 给Html发消息,js接收并返回数据
                 */
                bridgeWebView.callHandler("functionInJs", "调用js的方法", new CallBackFunction() {

                    @Override
                    public void onCallBack(String data) {
                        showToast("===" + data);
                    }
                });
            }
        });
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
}
