package com.whzl.mengbi.api;

import com.google.gson.JsonElement;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.entity.AppDataBean;
import com.whzl.mengbi.model.entity.BackpackListBean;
import com.whzl.mengbi.model.entity.GuardPriceBean;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;
import com.whzl.mengbi.model.entity.UserInfo;
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
     * 上传头像
     *
     * @param params
     * @param parts
     * @return
     */
    @Multipart
    @POST("v1/user/modify-avatar")
    Observable<ApiResult<JsonElement>> uploadFile(@PartMap Map<String, RequestBody> params,
                                                  @Part MultipartBody.Part parts);

    /**
     * 取消房管
     *
     * @param params
     * @return
     */
    @POST("v1/rank/rank")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> getRankList(@FieldMap Map<String, String> params);

    /**
     * 场榜第一
     *
     * @param params
     * @return
     */
    @POST("v1/rank/room-rank-one")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> getProgramFirst(@FieldMap Map<String, String> params);

    /**
     * app
     *
     * @param params
     * @return
     */
    @POST("v1/common/get-app-html")
    @FormUrlEncoded
    Observable<ApiResult<AppDataBean>> getImgUrl(@FieldMap Map<String, String> params);


    /**
     * 在线宝箱状态信息
     *
     * @param params
     * @return
     */
    @POST("v1/activity/online-box-status")
    @FormUrlEncoded
    Observable<ApiResult<TreasureBoxStatusBean>> getTreasureBoxStatus(@FieldMap Map<String, String> params);

    /**
     * 在线宝箱状态信息
     *
     * @param params
     * @return
     */
    @POST("v1/activity/online-receive")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> receiveTreasure(@FieldMap Map<String, String> params);

    /**
     * 获取用户详情接口
     *
     * @param params
     * @return
     */
    @POST("v1/user/get-user-info")
    @FormUrlEncoded
    Observable<ApiResult<UserInfo.DataBean>> getUserInfo(@FieldMap Map<String, String> params);

}
