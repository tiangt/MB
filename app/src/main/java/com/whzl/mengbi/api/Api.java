package com.whzl.mengbi.api;

import com.google.gson.JsonElement;
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
    @POST("wish/banner/list")
    @FormUrlEncoded
    Observable<ApiResult<JsonElement>> test(@FieldMap Map params);
}
