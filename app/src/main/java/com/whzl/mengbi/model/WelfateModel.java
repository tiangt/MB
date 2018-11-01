package com.whzl.mengbi.model;

import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.JumpRandomRoomBean;
import com.whzl.mengbi.model.entity.NewTaskBean;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class WelfateModel implements WelfareContract.Model {
    @Override
    public Observable<ApiResult<NewTaskBean>> newTask(String userId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        return ApiFactory.getInstance().getApi(Api.class)
                .newTask(ParamsUtils.getSignPramsMap(paramsMap));
    }

    @Override
    public Observable<ApiResult<JsonElement>> receive(String userId, String awardSn) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("awardSn", awardSn);
        return ApiFactory.getInstance().getApi(Api.class)
                .receive(ParamsUtils.getSignPramsMap(paramsMap));
    }

    @Override
    public Observable<ApiResult<JumpRandomRoomBean>> jumpRandom() {
        HashMap paramsMap = new HashMap();
        return ApiFactory.getInstance().getApi(Api.class)
                .random(ParamsUtils.getSignPramsMap(paramsMap));
    }
}
