package com.whzl.mengbi.contract;

import com.whzl.mengbi.model.entity.FollowSortBean;

/**
 * @author nobody
 * @date 2018/12/20
 */
public interface FollowSortContract {

    interface View extends BaseView {
        void onGetGuardPrograms(FollowSortBean bean);

        void onGetManageProgram(FollowSortBean bean);

        void onGetWatchRecord(FollowSortBean bean);

        void onClearWatchRecord();
    }

    interface Presenter {
        void getGuardPrograms(int page);

        void getManageProgram(int page);

        void getWatchRecord(int page);

        void clearWatchRecord();
    }
}
