package com.whzl.mengbi.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.js.JumpRoomBean;
import com.whzl.mengbi.model.entity.js.LoginStateBean;
import com.whzl.mengbi.model.entity.js.RoomInfoBean;
import com.whzl.mengbi.model.entity.js.UserInfoBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.activity.LoginActivity;
import com.whzl.mengbi.ui.fragment.base.BaseFragment;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
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
 * @author nobody
 * @date 2018/10/25
 */
public class LiveWebFragment extends BaseFragment {
    private int REQUEST_LOGIN = 120;

    @BindView(R.id.JsBridgeWebView)
    BridgeWebView bridgeWebView;
    @BindView(R.id.view)
    View view;
    private String url;
    private String anchorId;
    private String programId;
    private String title;
    private ClickListener listener;

    public static LiveWebFragment newInstance(String url, String anchorid, String mProgramId) {
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("anchorId", anchorid + "");
        args.putString("programId", mProgramId + "");
        LiveWebFragment liveWebFragment = new LiveWebFragment();
        liveWebFragment.setArguments(args);
        return liveWebFragment;
    }


    @Override
    protected void initEnv() {
        super.initEnv();
        Bundle args = getArguments();
        url = args.getString("url");
        anchorId = args.getString("anchorId");
        programId = args.getString("programId");
        title = args.getString("title");
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
            Long userId = (Long) SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L);
            String sessionId = (String) SPUtils.get(getMyActivity(), SpConfig.KEY_SESSION_ID, "");
            String nickname = SPUtils.get(getMyActivity(), SpConfig.KEY_USER_NAME, "").toString();
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

    }

    public void jumpToLoginActivity() {
        Intent intent = new Intent(getMyActivity(), LoginActivity.class);
        intent.putExtra("from", this.getClass().toString());
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    public void jumpToLiveHouse(int programId) {
        Intent intent = new Intent(getMyActivity(), LiveDisplayActivity.class);
        intent.putExtra("programId", programId);
        startActivity(intent);
    }

    public void jumpToRechargeActivity() {
        Intent intent = new Intent(getMyActivity(), WXPayEntryActivity.class);
        startActivity(intent);
    }

    private void getUserInfo(CallBackFunction function) {
        long userId = (long) SPUtils.get(getMyActivity(), SpConfig.KEY_USER_ID, 0L);
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
        String sessionId = (String) SPUtils.get(getMyActivity(), SpConfig.KEY_SESSION_ID, "");
        long userId = Long.parseLong(SPUtils.get(getMyActivity(), "userId", (long) 0).toString());
        if (userId == 0 || TextUtils.isEmpty(sessionId)) {
            return "F";
        }
        return "T";
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
