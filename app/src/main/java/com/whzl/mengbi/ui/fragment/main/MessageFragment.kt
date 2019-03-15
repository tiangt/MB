package com.whzl.mengbi.ui.fragment.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.whzl.mengbi.R
import com.whzl.mengbi.api.Api
import com.whzl.mengbi.config.SpConfig
import com.whzl.mengbi.contract.BasePresenter
import com.whzl.mengbi.contract.BaseView
import com.whzl.mengbi.model.entity.GetUnreadMsgBean
import com.whzl.mengbi.ui.adapter.base.BaseViewHolder
import com.whzl.mengbi.ui.fragment.base.BasePullListFragment
import com.whzl.mengbi.util.SPUtils
import com.whzl.mengbi.util.network.retrofit.ApiFactory
import com.whzl.mengbi.util.network.retrofit.ApiObserver
import com.whzl.mengbi.util.network.retrofit.ParamsUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * 消息
 *
 * @author cliang
 * @date 2019.2.14
 */
class MessageFragment : BasePullListFragment<String, BasePresenter<BaseView>>() {

    override fun init() {
        super.init()
        val titleView = LayoutInflater.from(activity).inflate(R.layout.headtip_msg, pullView, false)
        addHeadTips(titleView)
    }

    override fun loadData(action: Int, mPage: Int) {
        val userid = SPUtils.get(activity, SpConfig.KEY_USER_ID, 0L)
        val param = HashMap<String, String>()
        param.put("userId", userid.toString())
        ApiFactory.getInstance().getApi(Api::class.java)
                .getUnreadMsg(ParamsUtils.getSignPramsMap(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ApiObserver<GetUnreadMsgBean>(this) {

                    override fun onSuccess(watchHistoryListBean: GetUnreadMsgBean?) {
                    }

                    override fun onError(code: Int) {
                        loadFail()
                    }
                })

    }

    override fun setViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_msg_main, parent, false)
        return ViewHolder(itemView)
    }

    internal inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onBindViewHolder(position: Int) {
            val bean = mDatas[position]

        }
    }


    companion object {
        fun newInstance(): MessageFragment {
            val messageFragment = MessageFragment()
            return messageFragment
        }
    }

}
