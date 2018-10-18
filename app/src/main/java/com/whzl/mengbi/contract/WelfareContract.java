package com.whzl.mengbi.contract;

import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.PackPrettyBean;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/10/18
 */
public interface WelfareContract {
    interface Model {
        Observable<ApiResult<PackPrettyBean>> pretty(String userId, int page, int pageSize);
    }

    interface View extends BaseView {
        void onPrettySuccess(PackPrettyBean bean);
    }

    interface Presenter {
        void pretty(String userId, int page, int pageSize);
    }

}
