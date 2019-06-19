package com.whzl.mengbi.model;

import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.FlopAwardRecordBean;
import com.whzl.mengbi.model.entity.FlopCardBean;
import com.whzl.mengbi.model.entity.UserFlopInfoBean;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2019-06-18
 */
public class FlopModel {
    public Observable<ApiResult<UserFlopInfoBean>> getUserFlopInfo(String userId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        return ApiFactory.getInstance().getApi(Api.class)
                .userFlopInfo(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<FlopCardBean>> flopCard(String id, String roomId, String flopIndex) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", id);
        paramsMap.put("roomId", roomId);
        paramsMap.put("flopIndex", flopIndex);
        return ApiFactory.getInstance().getApi(Api.class)
                .flopCard(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<UserFlopInfoBean>> startFlop(String id) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", id);
        return ApiFactory.getInstance().getApi(Api.class)
                .startFlop(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<FlopAwardRecordBean>> flopAwardRecord() {
        HashMap paramsMap = new HashMap();
        return ApiFactory.getInstance().getApi(Api.class)
                .flopAwardRecord(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<FlopPriceBean>> flopPrice() {
        HashMap paramsMap = new HashMap();
        return ApiFactory.getInstance().getApi(Api.class)
                .flopPrice(ParamsUtils.getSignPramsMap(paramsMap));
    }

}
