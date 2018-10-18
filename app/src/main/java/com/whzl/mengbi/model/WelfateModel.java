package com.whzl.mengbi.model;

import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.model.entity.ApiResult;
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
    public Observable<ApiResult<PackPrettyBean>> pretty(String userId, int page, int pageSize) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("page", page);
        paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
        return ApiFactory.getInstance().getApi(Api.class)
                .pretty(ParamsUtils.getSignPramsMap(paramsMap));
    }
}
