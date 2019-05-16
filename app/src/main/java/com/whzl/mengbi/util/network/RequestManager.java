package com.whzl.mengbi.util.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.whzl.mengbi.BuildConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.common.ActivityStackManager;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.BusinessUtils;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestManager {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final String TAG = RequestManager.class.getSimpleName();
    private static final String APPKEY = "mb_android";
    private static final String APPSECRET = "3b2d8c0d1d88d44f1ef99b015caa5fe4";
    public static final String CLIENTTYPE = "ANDROID";

    private static volatile RequestManager mInstance;//单利引用
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    public static final int TYPE_POST_URL = 3;//post请求参数为表单
    public static final int RESPONSE_CODE = 200;//响应成功标识码
    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler okHttpHandler;//全局处理子线程和M主线程通信


    /**
     * 生成sign
     *
     * @param params_map
     * @return
     */
    private static String getSign(HashMap<String, String> params_map) {
        List<String> key_array = new ArrayList<String>(params_map.keySet());
        Collections.sort(key_array);
        String params = "";
        String sign = "";

        for (int i = 0; i < key_array.size(); i++) {
            String this_key = key_array.get(i);
            String this_value = String.valueOf(params_map.get(this_key));
            String sub_param = this_key + "=" + this_value;
            if (i != key_array.size() - 1) {
                sub_param = sub_param + "&";
            }
            params = params + sub_param;
        }
        try {
            String encodeParams = URLEncoder.encode(params, "UTF-8");
            //Log.e("http", "params=" + params + ",encodeParams=" + encodeParams);
            sign = EncryptUtils.md5Hex(encodeParams);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 初始化RequestManager
     */
    public RequestManager(Context context) {
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
        //初始化Handler
        okHttpHandler = new Handler(context.getMainLooper());
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static RequestManager getInstance(Context context) {
        RequestManager inst = mInstance;
        if (inst == null) {
            synchronized (RequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new RequestManager(context.getApplicationContext());
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * okHttp同步请求统一入口
     *
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     */
    public void requestSyn(String actionUrl, int requestType, HashMap<String, String> paramsMap) {
        switch (requestType) {
            case TYPE_GET:
                requestGetBySyn(actionUrl, paramsMap);
                break;
            case TYPE_POST_JSON:
                requestPostBySyn(actionUrl, paramsMap);
                break;
            case TYPE_POST_FORM:
                requestPostBySynWithForm(actionUrl, paramsMap);
                break;
        }
    }

    /**
     * okHttp get同步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    private void requestGetBySyn(String actionUrl, HashMap<String, String> paramsMap) {
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
            String requestUrl = String.format("%s/%s?%s", URLContentUtils.getBaseUrl(), actionUrl, tempParams.toString());
            //创建一个请求
            Request request = addHeaders().url(requestUrl).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            final Response response = call.execute();
            response.body().string();
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * okHttp post同步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    private void requestPostBySyn(String actionUrl, HashMap<String, String> paramsMap) {
        try {
            //处理参数
            paramsMap.put("appKey", APPKEY);
            paramsMap.put("timestamp", new Date().getTime() / 1000 + "");
            paramsMap.put("appSecret", APPSECRET);
            paramsMap.put("clientType", CLIENTTYPE);
            paramsMap.put("clientVersion", BuildConfig.VERSION_NAME + "");

//            StringBuilder tempParams = new StringBuilder();
//            int pos = 0;
//            for (String key : paramsMap.keySet()) {
//                if (pos > 0) {
//                    tempParams.append("&");
//                }
//                tempParams.append(String.format("%s=%s", key, EncryptUtils.md5Hex(URLEncoder.encode(paramsMap.get(key), "utf-8"))));
//                pos++;
//            }
//
//            tempParams.d
            String sign = getSign(paramsMap);
            paramsMap.put("sign", sign);
            paramsMap.remove("appSecret");
            String params = JSON.toJSONString(paramsMap);
            //补全请求地址
            String requestUrl = String.format("%s/%s", URLContentUtils.getBaseUrl(), actionUrl);
            //生成参数
            //String params = paramsMap.toString();
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(body).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
                LogUtils.e("response ----->" + response.body().string());
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * okHttp post同步请求表单提交
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    private void requestPostBySynWithForm(String actionUrl, HashMap<String, String> paramsMap) {
        try {
            //创建一个FormBody.Builder
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                builder.add(key, paramsMap.get(key));
            }
            //生成表单实体对象
            RequestBody formBody = builder.build();
            //补全请求地址
            String requestUrl = String.format("%s/%s", URLContentUtils.getBaseUrl(), actionUrl);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            if (response.isSuccessful()) {
                LogUtils.e("response ----->" + response.body().string());
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * okHttp异步请求统一入口
     *
     * @param actionUrl   接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack    请求返回数据回调
     * @param <T>         数据泛型
     **/
    public <T> Call requestAsyn(String actionUrl, int requestType, HashMap<String, String> paramsMap, ReqCallBack<T> callBack) {
        Call call = null;
        switch (requestType) {
            case TYPE_GET:
                call = requestGetByAsyn(actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_JSON:
                call = requestPostByAsyn(actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_FORM:
                call = requestPostByAsynWithForm(actionUrl, paramsMap, callBack);
                break;
            case TYPE_POST_URL:
                call = requestPostByUrl(actionUrl, paramsMap, callBack);
                break;
        }
        return call;
    }

//    /**
//     * 上传头像
//     */
//    public <T> void uploadAvatar(String userId, File file, String actionUrl, ReqCallBack<T> reqCallBack) {
//        String sessionId = (String) SPUtils.get(BaseApplication.getInstance(), "sessionId", "");
//        HashMap paramsMap = new HashMap();
//        paramsMap.put("appKey", APPKEY);
//        paramsMap.put("timestamp", System.currentTimeMillis() / 1000 + "");
//        paramsMap.put("appSecret", APPSECRET);
//        paramsMap.put("sessionId", sessionId);
//        paramsMap.put("clientType", CLIENTTYPE);
//        paramsMap.put("clientVersion", BuildConfig.VERSION_CODE + "");
//        paramsMap.put("userId", userId + "");
//        String sign = getSign(paramsMap);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("avatar", "avatar_image", fileBody)
//                .addFormDataPart("userId", userId)
//                .addFormDataPart("appKey", APPKEY)
//                .addFormDataPart("sessionId", sessionId)
//                .addFormDataPart("timestamp", System.currentTimeMillis() / 1000 + "")
////                .addFormDataPart("appSecret", APPSECRET)
//                .addFormDataPart("clientType", CLIENTTYPE)
//                .addFormDataPart("clientVersion", BuildConfig.VERSION_CODE + "")
//                .addFormDataPart("sign", sign)
//                .build();
//
//        String requestUrl = String.format("%s/%s", URLContentUtils.getBaseUrl(), actionUrl);
//        Request request = addHeaders().url(requestUrl).post(requestBody).build();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                failedCallBack("访问失败", reqCallBack);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String string = response.body().string();
//                    LogUtils.e("url=" + actionUrl + ",response ----->" + string);
//                    successCallBack((T) string, reqCallBack);
//                } else {
//                    failedCallBack("服务器错误", reqCallBack);
//                }
//            }
//        });
//    }


    /**
     * okHttp get异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestGetByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = String.format("%s/%s?%s", URLContentUtils.getBaseUrl(), actionUrl, tempParams.toString());
            final Request request = addHeaders().url(requestUrl).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    LogUtils.e(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        LogUtils.e("response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestPostByAsyn(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        String sessionId = (String) SPUtils.get(BaseApplication.getInstance(), "sessionId", "");
        long timeDiff = (long) SPUtils.get(BaseApplication.getInstance(), SpConfig.TIME_DIFF, 0L);
        try {
            paramsMap.put("appKey", APPKEY);
            paramsMap.put("timestamp", System.currentTimeMillis() / 1000 + timeDiff + "");
            paramsMap.put("appSecret", APPSECRET);
            paramsMap.put("sessionId", sessionId);
            paramsMap.put("clientType", CLIENTTYPE);
            paramsMap.put("clientVersion", BuildConfig.VERSION_NAME + "");
//            StringBuilder tempParams = new StringBuilder();
//            int pos = 0;
//            for (String key : paramsMap.keySet()) {
//                if (pos > 0) {
//                    tempParams.append("&");
//                }
//                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
//                pos++;
//            }
            String sign = getSign(paramsMap);
            paramsMap.put("sign", sign);
            paramsMap.remove("appSecret");
            String params = JSON.toJSONString(paramsMap);
            LogUtils.e2(paramsMap.toString());
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            String requestUrl = String.format("%s/%s", URLContentUtils.getBaseUrl(), actionUrl);
            final CacheControl.Builder builder = new CacheControl.Builder();
            builder.maxAge(10, TimeUnit.MILLISECONDS);
            CacheControl cache = builder.build();
            final Request request = addHeaders().cacheControl(cache).url(requestUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    LogUtils.e(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        LogUtils.e("url=" + actionUrl + ",response ----->" + string);
                        ApiResult apiResult = GsonUtils.GsonToBean(string, ApiResult.class);
                        if (apiResult.code == -17) {
                            BusinessUtils.transferVistor(ActivityStackManager.getInstance().getTopActivity());
                            return;
                        }
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return null;
    }

    /**
     * okHttp post异步请求表单提交
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call requestPostByAsynWithForm(String actionUrl, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            String requestUrl = String.format("%s/%s", URLContentUtils.getBaseUrl(), actionUrl);
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    LogUtils.e(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        LogUtils.e("response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return null;
    }

    public <T> Call getImage(String url, final ReqCallBack<T> reqCallBack) {
        Request request = new Request.Builder().url(url).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("url=" + url + " getImage error" + e.toString());
                failedCallBack(e.toString(), reqCallBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    byte[] body = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(body, 0, body.length);
                    successCallBack((T) bitmap, reqCallBack);
                } else {
                    failedCallBack("服务器错误", reqCallBack);
                }
            }
        });
        return null;
    }

    public interface ReqCallBack<T> {
        /**
         * 响应成功
         */
        void onReqSuccess(T result);

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0");
        return builder;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param <T>
     */
    private <T> void successCallBack(final T result, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private <T> void failedCallBack(final String errorMsg, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
    }

    /**
     * okHttp post异步请求
     *
     * @param <T>       数据泛型
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param callBack  请求返回数据回调
     * @return
     */
    private <T> Call requestPostByUrl(String actionUrl, HashMap<String, String> paramsMap, ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            String params = JSON.toJSONString(paramsMap);
            LogUtils.e(params.toString());
            RequestBody formBody = builder.build();
            String requestUrl = String.format("%s", /*"https://t3.mengbitv.com/",*/ actionUrl);
            LogUtils.e("ssssssssss   " + requestUrl);
            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    LogUtils.e(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        LogUtils.e("response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return null;
    }
}
