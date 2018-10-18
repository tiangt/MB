package com.whzl.mengbi.presenter;

import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.model.WelfateModel;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class WelfarePresenter extends BasePresenter<WelfareContract.View> implements WelfareContract.Presenter {

    private WelfareContract.Model welfateModel;

    public WelfarePresenter() {
        welfateModel = new WelfateModel();
    }

    @Override
    public void pretty(String userId, int page, int pageSize) {
        welfateModel.pretty(userId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<PackPrettyBean>() {

                    @Override
                    public void onSuccess(PackPrettyBean bean) {
                        mView.onPrettySuccess(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
