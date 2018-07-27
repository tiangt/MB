package com.whzl.mengbi.util.network.retrofit;

import android.net.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.whzl.mengbi.util.AsyncRun;
import com.whzl.mengbi.util.GenericUtil;
import com.whzl.mengbi.util.ToastUtils;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @author shaw
 * @date 16/7/29
 */
public abstract class ApiObserver<T> implements Observer<ApiResult<T>> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private static final String TAG = "ApiCallback";

    private Gson gson = new Gson();

    public ApiObserver() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        //HTTP错误
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {

                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    AsyncRun.run(() -> ToastUtils.showToast("服务端开小差了，请稍后再试"));
                    onError(0);
                    break;
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                default:
                    AsyncRun.run(() -> ToastUtils.showToast("网络连接失败，请检查网络设置"));
                    onError(0);
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {

            //均视为解析错误
            AsyncRun.run(() -> ToastUtils.showToast("数据异常，请稍后再试"));
            onError(0);
        } else if (e instanceof ConnectException) {
            //均视为网络错误
            AsyncRun.run(() -> ToastUtils.showToast("网络连接失败，请稍后再试"));
            onError(0);
        } else if (e instanceof UnknownHostException
                || e instanceof SocketTimeoutException) {
            AsyncRun.run(() -> ToastUtils.showToast("网络连接失败，请检查网络设置"));
            onError(0);
        } else {
            //未知错误
            AsyncRun.run(() -> ToastUtils.showToast("发生未知错误"));
            onError(0);
        }
        onComplete();
    }

    @Override
    public void onNext(ApiResult<T> body) {
        if (!isAlive()) {
            return;
        }

        if (body == null) {
            AsyncRun.run(() -> ToastUtils.showToast("数据异常，请稍后再试"));
            onError(0);
            return;
        } else {
            if (body.code == ApiResult.REQUEST_SUCCESS) {
                Type type = GenericUtil.getGenericClass(this);
                if (type != null) {
                    T result = body.getResultBean(type);
                    if (result == null) {
                        onSuccess(null);
                        return;
                    } else {
                        try {
                            onSuccess(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        onSuccess(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                AsyncRun.run(() -> ToastUtils.showToast(body.msg));
                onError(body.code);

            }
        }
        onComplete();
    }

    public abstract void onSuccess(T t);

    public abstract void onError(int code);

    @Override
    public void onComplete() {

    }


    @Override
    public void onSubscribe(Disposable disposable) {

    }

    /**
     * 检查是否继续，比如activiy已经结束
     *
     * @return
     */
    protected boolean isAlive() {
        return true;
    }
}
