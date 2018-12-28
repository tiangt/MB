package com.whzl.mengbi.presenter;

import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.PkRecordContract;
import com.whzl.mengbi.model.PkRecordModel;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/12/28
 */
public class PkRecordPresenter extends BasePresenter<PkRecordContract.View> implements PkRecordContract.Presenter {

    private final PkRecordModel pkRecordModel;

    public PkRecordPresenter() {
        pkRecordModel = new PkRecordModel();
    }

    @Override
    public void getPkTime(int userId) {
        if (!isViewAttached()) {
            return;
        }
        pkRecordModel.getPkTimes(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<PkTimeBean>>bindAutoDispose())
                .subscribe(new ApiObserver<PkTimeBean>() {
                    @Override
                    public void onSuccess(PkTimeBean bean) {
                        mView.onGetPkTimes(bean);
                    }
                });
    }

    @Override
    public void getPkRecordList(int anchorId) {
        if (!isViewAttached()) {
            return;
        }
        pkRecordModel.getPkRecordList(anchorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<PkRecordListBean>>bindAutoDispose())
                .subscribe(new ApiObserver<PkRecordListBean>() {
                    @Override
                    public void onSuccess(PkRecordListBean bean) {
                        mView.getPkList(bean);
                    }
                });
    }
}
