package com.whzl.mengbi.util.network.retrofit;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.whzl.mengbi.BuildConfig;
import com.whzl.mengbi.util.LogUtil;
import com.whzl.mengbi.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author shaw
 * @date 16/8/13
 */
public class ApiFactory {

    private volatile static ApiFactory apiFactory;

    private Retrofit builder;
    private OkHttpClient okHttpClient;

    public static ApiFactory getInstance() {
        if (apiFactory == null) {
            synchronized (ApiFactory.class) {
                if (apiFactory == null) {
                    apiFactory = new ApiFactory();
                }
            }
        }
        return apiFactory;
    }

    private ApiFactory() {

    }


    //  ------- retrofit2.0 -------
    public void build(Context context, String serverUrl, Interceptor interceptor) {
        HttpLoggingInterceptor httpLoggingInterceptor = null;
        httpLoggingInterceptor = new HttpLoggingInterceptor(message ->
                LogUtils.d2(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();


        okHttpBuilder
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS);

        setCache(context, okHttpBuilder);
        okHttpClient = okHttpBuilder.build();
        builder = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public <T> T getApi(Class<T> clazz) {
        return builder.create(clazz);
    }

    private void setCache(Context context, OkHttpClient.Builder okHttpBuilder) {
        File cacheFile = new File(context.getExternalCacheDir(), "responses");
        Cache cache = new Cache(cacheFile, 20 * 1024 * 1024);
        okHttpBuilder.cache(cache);
    }

    /**
     * @descrption 清除缓存
     */
    public void clearHttpCache() {
        try {
            okHttpClient.cache().evictAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
