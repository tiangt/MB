package com.whzl.mengbi.util;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/20
 */
public class BusinessUtils {

    public static void mallBuy(Activity context, String userId, String goodsId, String priceId,
                               String count, String toUserId, String salerId, String programId, MallBuyListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        paramsMap.put("goodsId", goodsId);
        paramsMap.put("priceId", priceId);
        paramsMap.put("count", count);
        paramsMap.put("toUserId", toUserId);
        paramsMap.put("salerId", salerId);
        paramsMap.put("programId", programId);
        ApiFactory.getInstance().getApi(Api.class)
                .mallBuy(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>(context) {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(int code) {
                        listener.onError();
                    }
                });
    }


    public static void getUserInfo(Activity context, String userId, UserInfoListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        ApiFactory.getInstance().getApi(Api.class)
                .getUserInfo(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<UserInfo.DataBean>(context) {

                    @Override
                    public void onSuccess(UserInfo.DataBean bean) {
                        listener.onSuccess(bean);
                    }

                    @Override
                    public void onError(int code) {
                        listener.onError(code);
                    }
                });
    }


    public interface MallBuyListener {
        void onSuccess();

        void onError();
    }

    public interface UserInfoListener {
        void onSuccess(UserInfo.DataBean bean);

        void onError(int code);
    }
}