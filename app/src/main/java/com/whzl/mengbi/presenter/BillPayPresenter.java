package com.whzl.mengbi.presenter;

import com.google.gson.JsonElement;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.BillPayContract;
import com.whzl.mengbi.contract.WelfareContract;
import com.whzl.mengbi.model.BillPayModel;
import com.whzl.mengbi.model.WelfateModel;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.PackPrettyBean;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author nobody
 * @date 2018/10/18
 */
public class BillPayPresenter extends BasePresenter<BillPayContract.View> implements BillPayContract.Presenter {

    private BillPayContract.Model billPayModel;

    public BillPayPresenter() {
        billPayModel = new BillPayModel();
    }

    @Override
    public void billPay(String userId, int page, int pageSize) {
        if (!isViewAttached()) {
            return;
        }
        billPayModel.billPay(userId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<JsonElement>>bindAutoDispose())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement bean) {
                        mView.onBillPaySuccess(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
