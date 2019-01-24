package com.whzl.mengbi.presenter;

import android.widget.TextView;

import com.google.gson.JsonElement;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.model.WelfateModel;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.JumpRandomRoomBean;
import com.whzl.mengbi.model.entity.NewTaskBean;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class WelfarePresenter extends BasePresenter<WelfareContract.View> implements WelfareContract.Presenter {

    private WelfateModel welfateModel;

    public WelfarePresenter() {
        welfateModel = new WelfateModel();
    }

    @Override
    public void newTask(String userId) {
        if (!isViewAttached()) {
            return;
        }
        welfateModel.newTask(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<NewTaskBean>>bindAutoDispose())
                .subscribe(new ApiObserver<NewTaskBean>() {

                    @Override
                    public void onSuccess(NewTaskBean bean) {
                        mView.onNewTask(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void receive(TextView tv,String userId, String awardSn) {
        if (!isViewAttached()) {
            return;
        }
        welfateModel.receive(userId,awardSn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<JsonElement>>bindAutoDispose())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        mView.onReceiveSuccess(tv,bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void jump() {
        if (!isViewAttached()) {
            return;
        }
        welfateModel.jumpRandom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<JumpRandomRoomBean>>bindAutoDispose())
                .subscribe(new ApiObserver<JumpRandomRoomBean>() {

                    @Override
                    public void onSuccess(JumpRandomRoomBean bean) {
                        mView.onJumpRandom(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
