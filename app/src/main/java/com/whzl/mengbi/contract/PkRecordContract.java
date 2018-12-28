package com.whzl.mengbi.contract;

import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/12/28
 */
public interface PkRecordContract {
    interface Model {
        Observable<ApiResult<PkTimeBean>> getPkTimes(int userId);
        Observable<ApiResult<PkRecordListBean>> getPkRecordList(int userId);
    }

    interface View extends BaseView {
        void onGetPkTimes(PkTimeBean bean);

        void getPkList(PkRecordListBean bean);
    }

    interface Presenter{
        void getPkTime(int userId);

        void getPkRecordList(int anchorId);
    }
}
