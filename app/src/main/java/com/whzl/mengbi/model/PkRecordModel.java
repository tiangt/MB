package com.whzl.mengbi.model;

import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.contract.PkRecordContract;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/12/28
 */
public class PkRecordModel implements PkRecordContract.Model {
    @Override
    public Observable<ApiResult<PkTimeBean>> getPkTimes(int userId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        return ApiFactory.getInstance().getApi(Api.class)
                .getPkTimes(ParamsUtils.getSignPramsMap(paramsMap));
    }

    @Override
    public Observable<ApiResult<PkRecordListBean>> getPkRecordList(int userId) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        return ApiFactory.getInstance().getApi(Api.class)
                .getPkRecordList(ParamsUtils.getSignPramsMap(paramsMap));
    }
}
