package com.whzl.mengbi.contract;

import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.NewTaskBean;

import io.reactivex.Observable;

/**
 * @author nobody
 * @date 2018/10/18
 */
public interface WelfareContract {
    interface Model {
        Observable<ApiResult<NewTaskBean>> newTask(String userId);
        Observable<ApiResult<JsonElement>> receive(String userId,String awardSn);
    }

    interface View extends BaseView {
        void onNewTask(NewTaskBean bean);
        void onReceiveSuccess(TextView tv,JsonElement jsonElement);
    }

    interface Presenter {
        void newTask(String userId);
        void receive(TextView tv,String userId, String awardSn);
    }

}
