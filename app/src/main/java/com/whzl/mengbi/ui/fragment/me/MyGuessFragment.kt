package com.whzl.mengbi.ui.fragment.me

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.NetConfig
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.UserGuessListBean
import com.whzl.mengbi.ui.activity.base.FrgActivity
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_guess_bet.*

/**
 *
 * @author nobody
 * @date 2019-06-14
 */
class MyGuessFragment : BasePullListFragment<UserGuessListBean.ListBean, BasePresenter<BaseView>>() {
    override fun init() {
        super.init()
        (activity as FrgActivity).setTitle("我的竞猜")
    }

    override fun loadData(action: Int, mPage: Int) {
        val hashMap = HashMap<String, String>()
        hashMap["userId"] = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L).toString()
        hashMap["page"] = mPage.toString()
        hashMap["pageSize"] = NetConfig.DEFAULT_PAGER_SIZE.toString()
        ApiFactory.getInstance().getApi(Api::class.java)
                .userGuessList(ParamsUtils.getSignPramsMap(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<UserGuessListBean>() {
                    override fun onSuccess(t: UserGuessListBean?) {
                        loadSuccess(t?.list)
                    }
                })
    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val inflate = LayoutInflater.from(activity).inflate(R.layout.item_my_guess, parent, false)
        return GuessViewHolder(inflate)
    }

    inner class GuessViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun onBindViewHolder(position: Int) {

        }

    }
}