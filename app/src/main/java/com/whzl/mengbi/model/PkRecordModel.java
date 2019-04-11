package com.whzl.mengbi.model;

import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;
import com.whzl.mengbi.model.entity.QueryBagByGoodsTypeBean;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/12/28
 */
public class PkRecordModel {
    public Observable<ApiResult<PkTimeBean>> getPkTimes(int userId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        return ApiFactory.getInstance().getApi(Api.class)
                .getPkTimes(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<PkRecordListBean>> getPkRecordList(int userId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        return ApiFactory.getInstance().getApi(Api.class)
                .getPkRecordList(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<BlackRoomTimeBean>> getRoomTime(int userId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        return ApiFactory.getInstance().getApi(Api.class)
                .getRoomTime(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<JsonElement>> rescure(long userId, int anchorId, int hourTime, int goodId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        paramsMap.put("anchorId", anchorId);
        paramsMap.put("hourTime", hourTime);
        paramsMap.put("goodsId", goodId);
        return ApiFactory.getInstance().getApi(Api.class)
                .rescue(ParamsUtils.getSignPramsMap(paramsMap));
    }

    public Observable<ApiResult<QueryBagByGoodsTypeBean>> getCardList(long userId, String black_card) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        paramsMap.put("goodsTypes", black_card);
        return ApiFactory.getInstance().getApi(Api.class)
                .queryBagByGoodsType(ParamsUtils.getSignPramsMap(paramsMap));
    }
}
