package com.whzl.mengbi.model;

import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.BillPayContract;
import com.whzl.mengbi.model.entity.ApiResult;
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
public class BillPayModel implements BillPayContract.Model {
    @Override
    public Observable<ApiResult<JsonElement>> billPay(String userId, int page, int pageSize) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("page", page);
        paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
        return ApiFactory.getInstance().getApi(Api.class)
                .pretty(ParamsUtils.getSignPramsMap(paramsMap));
    }
}
