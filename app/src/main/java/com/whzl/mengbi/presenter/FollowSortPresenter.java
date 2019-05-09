package com.whzl.mengbi.presenter;

import com.google.gson.JsonElement;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.FollowSortContract;
import com.whzl.mengbi.model.FollowSortModel;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.FollowSortBean;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/12/20
 */
public class FollowSortPresenter extends BasePresenter<FollowSortContract.View> implements FollowSortContract.Presenter {

    private FollowSortModel followSortModel;

    public FollowSortPresenter() {
        followSortModel = new FollowSortModel();
    }


    @Override
    public void getGuardPrograms(int page) {
        if (!isViewAttached()) {
            return;
        }
        followSortModel.getGuardPrograms(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<FollowSortBean>>bindAutoDispose())
                .subscribe(new ApiObserver<FollowSortBean>() {

                    @Override
                    public void onSuccess(FollowSortBean bean) {
                        mView.onGetGuardPrograms(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void getManageProgram(int page) {
        if (!isViewAttached()) {
            return;
        }
        followSortModel.getManageProgram(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<FollowSortBean>>bindAutoDispose())
                .subscribe(new ApiObserver<FollowSortBean>() {

                    @Override
                    public void onSuccess(FollowSortBean bean) {
                        mView.onGetManageProgram(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void getWatchRecord(int page) {
        if (!isViewAttached()) {
            return;
        }
        followSortModel.getWatchReord(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<FollowSortBean>>bindAutoDispose())
                .subscribe(new ApiObserver<FollowSortBean>() {

                    @Override
                    public void onSuccess(FollowSortBean bean) {
                        mView.onGetWatchRecord(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void clearWatchRecord() {
        if (!isViewAttached()) {
            return;
        }
        followSortModel.clearWatchRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<JsonElement>>bindAutoDispose())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        mView.onClearWatchRecord();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void getInfoBatch(String programIds) {
        if (!isViewAttached()) {
            return;
        }
        followSortModel.getInfoBatch(programIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<FollowSortBean>>bindAutoDispose())
                .subscribe(new ApiObserver<FollowSortBean>() {

                    @Override
                    public void onSuccess(FollowSortBean bean) {
                        mView.onGetInfoBatch(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
