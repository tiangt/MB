package com.whzl.mengbi.contract;

import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;

/**
 * @author nobody
 * @date 2018/12/28
 */
public interface PkRecordContract {

    interface View extends BaseView {
        void onGetPkTimes(PkTimeBean bean);

        void getPkList(PkRecordListBean bean);

        void getRoomTime(BlackRoomTimeBean bean);

        void onRescure();
    }

    interface Presenter{
        void getPkTime(int userId);

        void getPkRecordList(int anchorId);

        void getRoomTime(int userId);

        void rescure(long userId,int anchorId,int hourTime);
    }
}
