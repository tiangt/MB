package com.whzl.mengbi.api;

import com.google.gson.JsonElement;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.BackpackListBean;
import com.whzl.mengbi.model.entity.GuardPriceBean;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.WatchHistoryListBean;
import com.whzl.mengbi.model.entity.ApiResult;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * @author shaw
 * @date 2018/7/27
 */
public interface Api {


    /**
     * 观看记录
     *
     * @param params
     * @return
     */
    @POST("v1/user/watch-record")
    @FormUrlEncoded
    Observable<ApiResult<WatchHistoryListBean>> getWatchHistory(@FieldMap Map<String, String> params);

    /**
     * 背包
     *
     * @param params
     * @return
     */
    @POST("v1/consume/user-combine-gift")
    @FormUrlEncoded
    Observable<ApiResult<BackpackListBean>> getBackpack(@FieldMap Map<String, String> params);

    /**
     * 跑道
     *
     * @param params
     * @return
     */
    @POST("v1/room/runway")
    @FormUrlEncoded
    Observable<ApiResult<RunWayListBean>> getRunWayList(@FieldMap Map<String, String> params);

    /**
     * 守护列表
     *
     * @param params
     * @return
     */
    @POST("v1/room/guard-self-list")
    @FormUrlEncoded
    Observable<ApiResult<GuardListBean>> getGuardList(@FieldMap Map<String, String> params);

    /**
     * 观众列表
     *
     * @param params
     * @return
     */
    @POST("v1/room/online")
    @FormUrlEncoded
    Observable<ApiResult<GuardListBean>> getAudienceList(@FieldMap Map<String, String> params);

    /**
     * 观众列表
     *
     * @param params
     * @return
     */
    @POST("v1/mall/get-guard-prices")
    @FormUrlEncoded
    Observable<ApiResult<GuardPriceBean>> getGuardPrice(@FieldMap Map<String, String> params);

    /**
     * 观众列表
     *
     * @param params
     * @return
     */
    @POST("v1/consume/mall-buy")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> buy(@FieldMap Map<String, String> params);

    /**
     * 观众列表
     *
     * @param params
     * @return
     */
    @POST("v1/feedback/feedback")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> feedback(@FieldMap Map<String, String> params);

    /**
     * 用户服务器操作
     *
     * @param params
     * @return
     */
    @POST("v1/room/user-server-operate")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> serverOprate(@FieldMap Map<String, String> params);

    /**
     * 升房管
     *
     * @param params
     * @return
     */
    @POST("v1/room/set-manager")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> setManager(@FieldMap Map<String, String> params);

    /**
     * 取消房管
     *
     * @param params
     * @return
     */
    @POST("v1/room/remove-manager")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> cancleManager(@FieldMap Map<String, String> params);


    /**
     * 上传文件
     *
     * @param params
     * @param parts
     * @return
     */
    @Multipart
    @POST("v1/user/modify-avatar")
    Observable<ApiResult<JsonElement>> uploadFile(@PartMap Map<String, RequestBody> params,
                                                  @Part MultipartBody.Part parts);

}
