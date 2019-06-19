package com.whzl.mengbi.contract;

import com.whzl.mengbi.model.FlopPriceBean;
import com.whzl.mengbi.model.entity.FlopAwardRecordBean;
import com.whzl.mengbi.model.entity.FlopCardBean;
import com.whzl.mengbi.model.entity.UserFlopInfoBean;

/**
 * @author nobody
 * @date 2019-06-18
 */
public interface FlopContract {
    interface View extends BaseView {
        void onUserFlopInfoSuccess(UserFlopInfoBean userFlopInfoBean);

        void onFlopCardSuccess(FlopCardBean flopCardBean);

        void onStartFlopSuccess(UserFlopInfoBean userFlopInfoBean);

        void onFlopAwardRecordSuccess(FlopAwardRecordBean flopAwardRecordBean);

        void onFlopPriceSuccess(FlopPriceBean flopPriceBean);
    }

    interface Presenter {
        void userFlopInfo(String userId);

        void flopCard(String userId, String roomId, String flopIndex);

        void startFlop(String userId);

        void flopAwardRecord();

        void flopPrice();
    }
}
