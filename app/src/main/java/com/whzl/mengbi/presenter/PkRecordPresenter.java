package com.whzl.mengbi.presenter;

import com.google.gson.JsonElement;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.contract.BasePresenter;
import com.whzl.mengbi.contract.PkRecordContract;
import com.whzl.mengbi.model.PkRecordModel;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.PkRecordListBean;
import com.whzl.mengbi.model.entity.PkTimeBean;
import com.whzl.mengbi.model.entity.QueryBagByGoodsTypeBean;
import com.whzl.mengbi.util.ToastUtils;
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

    @Override
    public void getRoomTime(int anchorId) {
        if (!isViewAttached()) {
            return;
        }
        pkRecordModel.getRoomTime(anchorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<BlackRoomTimeBean>>bindAutoDispose())
                .subscribe(new ApiObserver<BlackRoomTimeBean>() {
                    @Override
                    public void onSuccess(BlackRoomTimeBean bean) {
                        mView.getRoomTime(bean);
                    }
                });
    }

    @Override
    public void rescure(long userId, int anchorId, int hourTime, int goodsId) {
        if (!isViewAttached()) {
            return;
        }
        pkRecordModel.rescure(userId, anchorId, hourTime, goodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<JsonElement>>bindAutoDispose())
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement bean) {
                        mView.onRescure();
                    }

                    @Override
                    public void onError(ApiResult<JsonElement> body) {
                        if (body.code == AppConfig.ANCHOR_NOT_IN_BLACK_ROOM) {
                            ToastUtils.showToast("主播已被解救成功，无需重复解救。");
                        } else {
                            ToastUtils.showToast(body.msg);
                        }
                    }
                });
    }

    @Override
    public void getCardList(long userId,String black_card) {
        if (!isViewAttached()) {
            return;
        }
        pkRecordModel.getCardList(userId,black_card)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.<ApiResult<QueryBagByGoodsTypeBean>>bindAutoDispose())
                .subscribe(new ApiObserver<QueryBagByGoodsTypeBean>() {
                    @Override
                    public void onSuccess(QueryBagByGoodsTypeBean bean) {
                        mView.onGetCardList(bean);
                    }

                    @Override
                    public void onError(ApiResult<QueryBagByGoodsTypeBean> body) {
                    }
                });
    }
}
