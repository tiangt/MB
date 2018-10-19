package com.whzl.mengbi.contract;

import com.google.gson.JsonElement;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.PackPrettyBean;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/10/18
 */
public interface BillPayContract {
    interface Model {
        Observable<ApiResult<JsonElement>> billPay(String userId, int page, int pageSize);
    }

    interface View extends BaseView {
        void onBillPaySuccess(JsonElement bean);
    }

    interface Presenter {
        void billPay(String userId, int page, int pageSize);
    }

}
