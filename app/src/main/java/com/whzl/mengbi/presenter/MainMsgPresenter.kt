package com.whzl.mengbi.presenter

import com.google.gson.JsonElement
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.MainMsgContract
import com.whzl.mengbi.model.MainMsgModer
import com.whzl.mengbi.model.entity.GetGoodMsgBean
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author nobody
 * @date 2019/3/18
 */
class MainMsgPresenter : BasePresenter<MainMsgContract.View>(), MainMsgContract.Presenter {


    var moder: MainMsgModer? = null

    init {
        moder = MainMsgModer()
    }

    override fun getMsgList(page: Int) {
        if (!isViewAttached) {
            return
        }
        moder!!.getMainMsgList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GetGoodMsgBean>() {
                    override fun onSuccess(t: GetGoodMsgBean?) {
                        if (t != null) {
                            mView.onGetMsgListSuccess(t)
                        }
                    }
                })
    }

    override fun updateMsgRead(messageId: Int, messageType: String) {
        if (!isViewAttached) {
            return
        }
        moder!!.updateMsgRead(messageId, messageType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JsonElement>() {
                    override fun onSuccess(t: JsonElement?) {
                        mView.onUpdateMsgReadSuccess()
                    }

                })
    }

    override fun updateMsgReadByType( messageType: String) {
//        if (!isViewAttached) {
//            return
//        }
        moder!!.updateMsgReadByType(messageType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<JsonElement>() {
                    override fun onSuccess(t: JsonElement?) {
                        mView.onUpdateMsgReadSuccess()
                    }

                })
    }
}
