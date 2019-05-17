package com.whzl.mengbi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.eventbus.event.JumpMainActivityEvent;
import com.whzl.mengbi.gen.CommonGiftDao;
import com.whzl.mengbi.gen.UserDao;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.ProgramInfoByAnchorBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.model.entity.VistorWatchBean;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/9/20
 */
public class BusinessUtils {

    public static void mallBuy(Activity context, String userId, String goodsId, String priceId,
                               String count, String toUserId, String salerId, MallBuyListener listener) {
        mallBuyDay(context, userId, goodsId, priceId, count, toUserId, salerId, "", listener);
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
                                  String count, String toUserId, String salerId, String days, MallBuyListener listener) {
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

    public static void saveVistorHistory(int programId) {
        String s = SPUtils.get(BaseApplication.getInstance(), SpConfig.VISITOR_WATCH_HISTORY, "").toString();
        long timeDiff = (long) SPUtils.get(BaseApplication.getInstance(), SpConfig.TIME_DIFF, 0L);
        Gson gson = new Gson();
        VistorWatchBean vistorWatchBean = gson.fromJson(s, VistorWatchBean.class);
        if (vistorWatchBean == null) {
            vistorWatchBean = new VistorWatchBean();
            vistorWatchBean.list = new ArrayList<>();
        }

        VistorWatchBean.VistorWatchDetailBean vistorWatchDetailBean = new VistorWatchBean.VistorWatchDetailBean();
        vistorWatchDetailBean.programId = programId;
        vistorWatchDetailBean.timestamp = System.currentTimeMillis() / 1000 + timeDiff;

        vistorWatchBean.list.add(0, vistorWatchDetailBean);

        SPUtils.put(BaseApplication.getInstance(), SpConfig.VISITOR_WATCH_HISTORY, gson.toJson(vistorWatchBean));
    }

    public static void uploadVistorHistory() {
        String s = SPUtils.get(BaseApplication.getInstance(), SpConfig.VISITOR_WATCH_HISTORY, "").toString();
        if (TextUtils.isEmpty(s)) {
            return;
        }
        Gson gson = new Gson();
        VistorWatchBean vistorWatchBean = gson.fromJson(s, VistorWatchBean.class);
        List<VistorWatchBean.VistorWatchDetailBean> list = vistorWatchBean.list;
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L).toString());
        paramsMap.put("watchRecord", gson.toJson(list));
        ApiFactory.getInstance().getApi(Api.class)
                .saveWatchRecord(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement bean) {
                    }


                    @Override
                    public void onError(ApiResult<JsonElement> body) {
                    }
                });
        SPUtils.put(BaseApplication.getInstance(), SpConfig.VISITOR_WATCH_HISTORY, "");
    }

    public static void clearVistorHistory() {
        SPUtils.put(BaseApplication.getInstance(), SpConfig.VISITOR_WATCH_HISTORY, "");
    }

    public static void transferVistor(Activity context) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.LOGOUT, RequestManager.TYPE_POST_JSON, new HashMap<>(), new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                ResponseInfo responseInfo = GsonUtils.GsonToBean(strJson, ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_BIND_MOBILE, "");
                    context.startActivity(new Intent(context, MainActivity.class));
                    EventBus.getDefault().post(new JumpMainActivityEvent(0));

                    UserDao userDao = BaseApplication.getInstance().getDaoSession().getUserDao();
                    userDao.deleteAll();

                    SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L);
                    HashMap paramsMap = new HashMap();
                    paramsMap.put("platform", RequestManager.CLIENTTYPE);
                    RxPermisssionsUitls.getDevice(context, new RxPermisssionsUitls.OnPermissionListener() {
                        @Override
                        public void onGranted() {
                            paramsMap.put("deviceNumber", DeviceUtils.getDeviceId(context));
                            visitorLogin(paramsMap);
                        }

                        @Override
                        public void onDeny() {
                            paramsMap.put("deviceNumber", "");
                            visitorLogin(paramsMap);
                        }
                    });
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }

    private static void visitorLogin(HashMap paramsMap) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.USER_VISITOR_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object object) {
                        VisitorUserInfo visitorUserInfo = GsonUtils.GsonToBean(object.toString(), VisitorUserInfo.class);
                        if (visitorUserInfo.getCode() == RequestManager.RESPONSE_CODE) {
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, visitorUserInfo.getData().getUserId());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, visitorUserInfo.getData().getSessionId());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, visitorUserInfo.getData().getNickname());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, false);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                    }
                });
    }
}
