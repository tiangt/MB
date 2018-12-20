package com.whzl.mengbi.model;

import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.NetConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.contract.FollowSortContract;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.FollowSortBean;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/12/20
 */
public class FollowSortModel implements FollowSortContract.Model {
    @Override
    public Observable<ApiResult<FollowSortBean>> getGuardPrograms(int page) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("page", page);
        paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
        return ApiFactory.getInstance().getApi(Api.class)
                .getGuardProgram(ParamsUtils.getSignPramsMap(paramsMap));
    }

    @Override
    public Observable<ApiResult<FollowSortBean>> getManageProgram(int page) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        paramsMap.put("page", page);
        paramsMap.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
        return ApiFactory.getInstance().getApi(Api.class)
                .getManageProgram(ParamsUtils.getSignPramsMap(paramsMap));
    }

    @Override
    public Observable<ApiResult<FollowSortBean>> getWatchReord(int page) {
        HashMap param = new HashMap();
        param.put("userId", SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, 0L));
        param.put("page", page);
        param.put("pageSize", NetConfig.DEFAULT_PAGER_SIZE);
        return ApiFactory.getInstance().getApi(Api.class)
                .getWatchHistory(ParamsUtils.getSignPramsMap(param));
    }
}
