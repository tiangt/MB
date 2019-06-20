package com.whzl.mengbi.presenter;

import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.FlopContract;
import com.whzl.mengbi.model.FlopModel;
import com.whzl.mengbi.model.FlopPriceBean;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.FlopAwardRecordBean;
import com.whzl.mengbi.model.entity.FlopCardBean;
import com.whzl.mengbi.model.entity.UserFlopInfoBean;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2019-06-18
 */
public class FlopPresenter extends BasePresenter<FlopContract.View> implements FlopContract.Presenter {

    private final FlopModel flopModel;

    public FlopPresenter() {
        flopModel = new FlopModel();
    }

    @Override
    public void userFlopInfo(String userId) {
        if (!isViewAttached()) {
            return;
        }
        flopModel.getUserFlopInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.bindAutoDispose())
                .subscribe(new ApiObserver<UserFlopInfoBean>() {

                    @Override
                    public void onSuccess(UserFlopInfoBean userFlopInfoBean) {
                        mView.onUserFlopInfoSuccess(userFlopInfoBean);
                    }
                });
    }

    @Override
    public void flopCard(String userId, String roomId, String flopIndex) {
        if (!isViewAttached()) {
            return;
        }
        flopModel.flopCard(userId, roomId, flopIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.bindAutoDispose())
                .subscribe(new ApiObserver<FlopCardBean>() {
                    @Override
                    public void onSuccess(FlopCardBean flopCardBean) {
                        mView.onFlopCardSuccess(flopCardBean);
                    }
                });
    }

    @Override
    public void startFlop(String userId) {
        if (!isViewAttached()) {
            return;
        }
        flopModel.startFlop(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.bindAutoDispose())
                .subscribe(new ApiObserver<UserFlopInfoBean>() {
                    @Override
                    public void onSuccess(UserFlopInfoBean userFlopInfoBean) {
                        mView.onStartFlopSuccess(userFlopInfoBean);
                    }

                    @Override
                    public void onError(ApiResult<UserFlopInfoBean> body) {
                        mView.onStartFlopError(body.code);
                    }
                });
    }

    @Override
    public void flopAwardRecord() {
        if (!isViewAttached()) {
            return;
        }
        flopModel.flopAwardRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.bindAutoDispose())
                .subscribe(new ApiObserver<FlopAwardRecordBean>() {
                    @Override
                    public void onSuccess(FlopAwardRecordBean flopAwardRecordBean) {
                        mView.onFlopAwardRecordSuccess(flopAwardRecordBean);
                    }
                });
    }

    @Override
    public void flopPrice() {
        if (!isViewAttached()) {
            return;
        }
        flopModel.flopPrice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.bindAutoDispose())
                .subscribe(new ApiObserver<FlopPriceBean>() {
                    @Override
                    public void onSuccess(FlopPriceBean flopPriceBean) {
                        mView.onFlopPriceSuccess(flopPriceBean);
                    }
                });
    }
}
