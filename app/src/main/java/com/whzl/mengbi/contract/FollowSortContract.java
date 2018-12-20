package com.whzl.mengbi.contract;

import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.FollowSortBean;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/12/20
 */
public interface FollowSortContract {
    interface Model {
        Observable<ApiResult<FollowSortBean>> getGuardPrograms();

        Observable<ApiResult<FollowSortBean>> getManageProgram();

        Observable<ApiResult<FollowSortBean>> getWatchReord(int page);
    }

    interface View extends BaseView {
        void onGetGuardPrograms(FollowSortBean bean);

        void onGetManageProgram(FollowSortBean bean);

        void onGetWatchRecord(FollowSortBean bean);
    }

    interface Presenter {
        void getGuardPrograms();

        void getManageProgram();

        void getWatchRecord(int page);
    }
}
