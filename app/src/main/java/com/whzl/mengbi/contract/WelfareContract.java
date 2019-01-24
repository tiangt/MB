package com.whzl.mengbi.contract;

import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.model.entity.JumpRandomRoomBean;
import com.whzl.mengbi.model.entity.NewTaskBean;

/**
 * @author nobody
 * @date 2018/10/18
 */
public interface WelfareContract {

    interface View extends BaseView {
        void onNewTask(NewTaskBean bean);
        void onReceiveSuccess(TextView tv,JsonElement jsonElement);
        void onJumpRandom(JumpRandomRoomBean bean);
    }

    interface Presenter {
        void newTask(String userId);
        void receive(TextView tv,String userId, String awardSn);
        void jump();
    }

}
