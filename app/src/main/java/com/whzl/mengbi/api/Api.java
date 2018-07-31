package com.whzl.mengbi.api;

import com.whzl.mengbi.model.entity.BackpackListBean;
import com.whzl.mengbi.model.entity.WatchHistoryListBean;
import com.whzl.mengbi.util.network.retrofit.ApiResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author shaw
 * @date 2018/7/27
 */
public interface Api {


    /**
     * 观看记录
     * @param params
     * @return
     */
    @POST("v1/user/watch-record")
    @FormUrlEncoded
    Observable<ApiResult<WatchHistoryListBean>> getWatchHistory(@FieldMap Map<String, String> params);

    /**
     * 背包
     * @param params
     * @return
     */
    @POST("v1/consume/user-combine-gift")
    @FormUrlEncoded
    Observable<ApiResult<BackpackListBean>> getBackpack(@FieldMap Map<String, String> params);
}
