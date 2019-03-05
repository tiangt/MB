package com.whzl.mengbi.util;

import android.app.Activity;

import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.ProgramInfoByAnchorBean;
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
                               String count, String toUserId, String salerId, MallBuyListener listener) {
        mallBuyDay(context,userId,goodsId,priceId,count,toUserId,salerId,"",listener);
//        HashMap paramsMap = new HashMap();
//        paramsMap.put("userId", userId);
//        paramsMap.put("goodsId", goodsId);
//        paramsMap.put("priceId", priceId);
//        paramsMap.put("count", count);
//        paramsMap.put("toUserId", toUserId);
//        paramsMap.put("salerId", salerId);
//        ApiFactory.getInstance().getApi(Api.class)
//                .mallBuy(ParamsUtils.getSignPramsMap(paramsMap))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ApiObserver<JsonElement>() {
//
//                    @Override
//                    public void onSuccess(JsonElement bean) {
//                        listener.onSuccess();
//                    }
//
//                    @Override
//                    public void onError(int code) {
//                        listener.onError();
//                    }
//                });
    }

    public static void mallBuyDay(Activity context, String userId, String goodsId, String priceId,
                               String count, String toUserId, String salerId,String days, MallBuyListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        paramsMap.put("goodsId", goodsId);
        paramsMap.put("priceId", priceId);
        paramsMap.put("count", count);
        paramsMap.put("toUserId", toUserId);
        paramsMap.put("salerId", salerId);
        paramsMap.put("days", days);
        ApiFactory.getInstance().getApi(Api.class)
                .mallBuy(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {

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


    //获取用户信息
    public static void getUserInfo(Activity context, String userId, UserInfoListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        ApiFactory.getInstance().getApi(Api.class)
                .getUserInfo(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<UserInfo.DataBean>() {

                    @Override
                    public void onSuccess(UserInfo.DataBean bean) {
                        listener.onSuccess(bean);
                    }

                    @Override
                    public void onError(ApiResult<UserInfo.DataBean> body) {
                        listener.onError(body.code);
                    }

                });
    }

    //获取主播信息
    public static void getAnchorInfo(Activity context, String userId, AnchorInfoListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("anchorId", userId);
        ApiFactory.getInstance().getApi(Api.class)
                .programInfoByAnchorid(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ProgramInfoByAnchorBean>() {

                    @Override
                    public void onSuccess(ProgramInfoByAnchorBean bean) {
                        listener.onSuccess(bean);
                    }


                    @Override
                    public void onError(ApiResult<ProgramInfoByAnchorBean> body) {
                        listener.onError(body.code);
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

    public interface AnchorInfoListener {
        void onSuccess(ProgramInfoByAnchorBean bean);

        void onError(int code);
    }
}
